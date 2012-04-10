package com.mogujie.jessica.index;

import static com.mogujie.jessica.store.PostingListStore.INT_SLICE_SIZE;

import com.mogujie.jessica.service.thrift.TToken;
import com.mogujie.jessica.store.Pointer;
import com.mogujie.jessica.store.PostingListStore;
import com.mogujie.jessica.store.PostingListStore;
import com.mogujie.jessica.util.AttributeSource;
import com.mogujie.jessica.util.BytesRef;
import com.mogujie.jessica.util.BytesRefHash;
import com.mogujie.jessica.util.CharTermAttribute;
import com.mogujie.jessica.util.OffsetAttribute;
import com.mogujie.jessica.util.TermToBytesRefAttribute;

public class InvertedIndexPerField
{
    private final SingleTokenAttributeSource singleToken = new SingleTokenAttributeSource();
    private TermToBytesRefAttribute termAtt;
    private BytesRef termBytesRef;
    private final BytesRefHash bytesHash;
    private final ParallelPostingsArray parallelArray;
    private final PostingListStore plStore;
    private int uniqueTermCount = 0;

    public InvertedIndexPerField(InvertedIndexer writer)
    {
        bytesHash = new BytesRefHash(writer.termPool);
        plStore = writer.plStore;
        parallelArray = new ParallelPostingsArray(4);
    }

    public void process(final TToken tToken, final int docId)
    {

        String stringValue = tToken.getValue();
        final int valueLength = stringValue.length();
        singleToken.reinit(stringValue, 0, valueLength);

        int termId = 0;
        try
        {
            termAtt = singleToken.getAttribute(TermToBytesRefAttribute.class);
            termBytesRef = termAtt.getBytesRef();
            termId = bytesHash.add(termBytesRef, termAtt.fillBytesRef());
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        if (termId >= 0)
        {// New posting
            bytesHash.byteStart(termId);
            // 为新的term创建一个postingList 初始大小为2
            int pointer = plStore.newTerm();
            Pointer p = new Pointer(pointer);
            p = new Pointer(p.poolIdx, p.sliceIdx, 1);
            // 初始化term的freqProx指针 指向刚刚分配好的内存地址
            parallelArray.freqProxStarts[termId] = pointer;
            parallelArray.freqProxUptos[termId] = p.pointer;
            newTerm(termId, docId, tToken);
        } else
        {
            termId = (-termId) - 1;
            addTerm(termId, docId, tToken);
        }
    }

    void newTerm(final int termId, final int docId, final TToken tToken)
    {
        parallelArray.termFreqs[termId]++;
        writeProxFreq(termId, docId, tToken.position);
        uniqueTermCount++;
        captureTerm(termId, docId, tToken);
    }

    // TODO cache
    void captureTerm(int termID, int docID, final TToken tToken)
    {
    }

    void addTerm(final int termId, final int docId, final TToken tToken)
    {
        parallelArray.termFreqs[termId]++;
        writeProxFreq(termId, docId, tToken.position);
        captureTerm(termId, docId, tToken);
    }

    // 将当前term的文档Id 位置信息写入到postingListStore当中
    // 真正的写入操作在termsHashPerField中完成
    // 存入的docId最大只能是1600万 位置信息最大不能超过255 对于twitter应用已经够用了
    private void writeProxFreq(int termID, int docID, int position)
    {
        int value = (docID << 8) | position & 0xFF;
        writeInt(termID, value);
    }

    // 和原有的lucene存储不同的是 jessica并不做压缩处理
    // 这样虽会带来一定的内存开销 但是最大的优点是可以从后往前遍历
    void writeInt(int termId, int value)
    {
        // Slice写满 要重新分配一个块出来给该Term
        // 初始化term的freqProx指针 指向刚刚分配好的内存地址
        Pointer p = new Pointer(parallelArray.freqProxUptos[termId]);
        int intUptoStart = p.sliceIdx * PostingListStore.INT_SLICE_SIZE[p.poolIdx] + p.offsetIdx;
        int bufferIdx = intUptoStart >> InvertedIndexer.INT_BLOCK_SHIFT;
        int startIdx = intUptoStart & InvertedIndexer.INT_BLOCK_MASK;
        IntBlockPool intBlockPool = plStore.intBlockPools[p.poolIdx];
        int[] buffer = intBlockPool.buffers[bufferIdx];

        // 如果指针已经指向该Slice的最后一个位置了 就开始分配一个新的Slice出来
        // 并在2个Slice的始末位置赋值为各自的指针 做成双向链表
        int sliceSize = PostingListStore.INT_SLICE_SIZE[p.poolIdx];
        if (p.offsetIdx == sliceSize - 1)
        {
            // 开始分配新的Slice 最多只有4层intBlock
            int poolIdx = p.poolIdx == 3 ? p.poolIdx : p.poolIdx + 1;
            IntBlockPool upLevelIntBlockPool = plStore.intBlockPools[poolIdx];
            if (upLevelIntBlockPool.intUpto + INT_SLICE_SIZE[poolIdx] > InvertedIndexer.INT_BLOCK_SIZE)
            {
                upLevelIntBlockPool.nextBuffer();
            }

            // 得出改slice在该层的位置
            int sliceIdx = (upLevelIntBlockPool.intOffset + upLevelIntBlockPool.intUpto) / INT_SLICE_SIZE[poolIdx];
            // 将该层块的一个slice标记为已经使用
            upLevelIntBlockPool.intUpto += INT_SLICE_SIZE[poolIdx];
            // 交换指针 新的Slice其实到了第二个位置才开始存储数据的 第一个位置为指针
            Pointer next = new Pointer(poolIdx, sliceIdx, 1);
            buffer[startIdx] = next.pointer;

            // 指向刚分配slice的第一个位置
            intUptoStart = sliceIdx * PostingListStore.INT_SLICE_SIZE[poolIdx];
            // 更换索引
            bufferIdx = intUptoStart >> InvertedIndexer.INT_BLOCK_SHIFT;
            // 缓冲指向下一个buffer
            buffer = upLevelIntBlockPool.buffers[bufferIdx];
            // 跟换下标
            startIdx = intUptoStart & InvertedIndexer.INT_BLOCK_MASK;
            // 将新的Buffer的第一个位置指向旧的Slice最后一个存储数据的位置
            Pointer pre = new Pointer(p.poolIdx, p.sliceIdx, p.offsetIdx - 1);
            buffer[startIdx] = pre.pointer;
            // 下标加1
            startIdx++;
            // 新的指针
            p = new Pointer(poolIdx, sliceIdx, 1);
        }

        // 赋值操作
        buffer[startIdx] = value;

        // 将posting的头指针指向下一个可写的位置
        p.offsetIdx++;
        p = new Pointer(p.poolIdx, p.sliceIdx, p.offsetIdx);

        parallelArray.freqProxUptos[termId] = p.pointer;
    }

    static class SingleTokenAttributeSource extends AttributeSource
    {
        final CharTermAttribute termAttribute;
        final OffsetAttribute offsetAttribute;

        private SingleTokenAttributeSource()
        {
            termAttribute = addAttribute(CharTermAttribute.class);
            offsetAttribute = addAttribute(OffsetAttribute.class);
        }

        public void reinit(String stringValue, int startOffset, int endOffset)
        {
            termAttribute.setEmpty().append(stringValue);
            offsetAttribute.setOffset(startOffset, endOffset);
        }
    }

}
