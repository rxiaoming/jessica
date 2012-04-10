package com.mogujie.jessica.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.mogujie.jessica.service.thrift.TDocument;
import com.mogujie.jessica.service.thrift.TField;
import com.mogujie.jessica.service.thrift.TToken;
import com.mogujie.jessica.store.VersionBits;
import com.mogujie.jessica.store.PostingListStore;
import com.mogujie.jessica.util.Bits;
import com.mogujie.jessica.util.ByteBlockPool;
import com.mogujie.jessica.util.ByteBlockPool.DirectAllocator;
import com.mogujie.jessica.util.RamUsageEstimator;

public class InvertedIndexer
{
    private final static Logger logger = Logger.getLogger(InvertedIndexer.class);
    private volatile int maxDocCount;
    private final AtomicLong bytesUsed;
    public final PostingListStore plStore;
    private final DirectAllocator allocator = new DirectAllocator();
    private final ConcurrentHashMap<String, InvertedIndexPerField> invertedIndexPerFields = new ConcurrentHashMap<String, InvertedIndexPerField>();
    private final Map<Integer, Integer> uid2docMap = new HashMap<Integer, Integer>();
    private final int doc2uidArray[] = new int[1 << 24];// 64M
    private final int delDocIds[] = new int[1 << 24];// 64M
    final ByteBlockPool termPool;
    private RangeFieldListener rangeFieldListener;

    public InvertedIndexer()
    {
        bytesUsed = new AtomicLong(0);
        termPool = new ByteBlockPool(allocator);
        plStore = new PostingListStore(this);
        rangeFieldListener = new RangeFieldListener(this);
    }

    public void add(TDocument document)
    {
        internalAdd(document);
    }

    private void internalAdd(TDocument document)
    {
        int docId = ++maxDocCount;
        int objectId = document.object_id;

        setDocUid(docId, objectId);

        for (TField tField : document.getFields())
        {
            ThriftField tf = new ThriftField(tField);
            List<TToken> tks = tf.tField.getTokens();
            String name = tField.name;
            InvertedIndexPerField invertedIndexPerField = invertedIndexPerFields.get(name);
            if (invertedIndexPerField == null)
            {
                invertedIndexPerField = new InvertedIndexPerField(this);
                invertedIndexPerFields.put(name, invertedIndexPerField);
            }
            for (TToken tToken : tks)
            {
                invertedIndexPerField.process(tToken, docId);
                rangeFieldListener.newValue(docId, name, tToken.value);
            }
        }

    }

    public void setDocUid(int docId, int uid)
    {
        doc2uidArray[docId] = uid;
        Integer oldDocId = uid2docMap.get(uid);
        if (oldDocId != null)
        {
            delDocIds[oldDocId] = docId;
        }
        uid2docMap.put(uid, docId);
    }

    /**
     * @param macDocId
     *            对应当前最大的macDocId
     * @return Bits 一个可以判断是否该为数据为删除的bits
     */
    public Bits liveDocs(int maxDocId)
    {
        VersionBits newLiveDocs = new VersionBits(delDocIds, maxDocId);
        return newLiveDocs;
    }

    /*
     * Initial chunks size of the shared int[] blocks used to store postings
     * data
     */
    public final static int INT_BLOCK_SHIFT = 15;
    public final static int INT_BLOCK_SIZE = 1 << INT_BLOCK_SHIFT;
    public final static int INT_BLOCK_MASK = INT_BLOCK_SIZE - 1;

    /* Allocate another int[] from the shared pool */
    int[] getIntBlock()
    {
        int[] b = new int[INT_BLOCK_SIZE];
        bytesUsed.addAndGet(INT_BLOCK_SIZE * RamUsageEstimator.NUM_BYTES_INT);
        return b;
    }

    void recycleIntBlocks(int[][] blocks, int offset, int length)
    {
        bytesUsed.addAndGet(-(length * (INT_BLOCK_SIZE * RamUsageEstimator.NUM_BYTES_INT)));
    }

    public int maxDoc()
    {
        return maxDocCount;
    }

    public InvertedIndexPerField getIndexPerField(String field)
    {
        return invertedIndexPerFields.get(field);
    }

    /**
     * 获得一个特定的rangeQuery
     * 
     * @param field
     * @param termFrom
     * @param termTo
     * @return
     */
    public Bits getRange(String field, String termFrom, String termTo)
    {
        return rangeFieldListener.getRange(field, Integer.parseInt(termFrom), Integer.parseInt(termTo));
    }

    public int[] getDocUids()
    {
        return doc2uidArray;
    }
}
