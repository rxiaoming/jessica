package com.mogujie.jessica.index;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.mogujie.jessica.util.Bits;
import com.mogujie.jessica.util.OpenBitSet;

/**
 * 目前只支持int类型的rangQuery
 * 
 * @author xuanxi
 * 
 */
public class RangeFieldListener
{
    private final static Logger logger = Logger.getLogger(RangeFieldListener.class);
    final InvertedIndexer dwpt;
    final private ConcurrentHashMap<String, OpenBitSet> ranges = new ConcurrentHashMap<String, OpenBitSet>();
    final private ConcurrentHashMap<String, List<RangeDo>> fieldRanges = new ConcurrentHashMap<String, List<RangeDo>>();

    public RangeFieldListener(RangeFieldListener rangeFieldListener)
    {
        this.dwpt = rangeFieldListener.dwpt;
        // TODO 初始化
    }

    public RangeFieldListener(InvertedIndexer dwpt)
    {
        this.dwpt = dwpt;
    }

    public synchronized void newRange(RangeDo rangeDo)
    {
        // dobule check
        OpenBitSet bits = ranges.get(rangeDo.toString());
        if (bits != null)
        {
            return;
        }

        bits = new OpenBitSet(1 << 24);// 直接分配一个1<<24大小的数组

        // 放入map中
        ranges.put(rangeDo.toString(), bits);
        List<RangeDo> list = fieldRanges.get(rangeDo.field);
        if (list == null)
        {
            list = new ArrayList<RangeFieldListener.RangeDo>();
            fieldRanges.putIfAbsent(rangeDo.field, list);
            list = fieldRanges.get(rangeDo.field);
        }
        list.add(rangeDo);

        // FIXME 初始化
        /*
         * try {
         * 
         * RAMReader ramReader = dwpt.getReader();
         * 
         * Terms terms = ramReader.terms(rangeDo.field); TermsEnum termsEnum =
         * terms.iterator();
         * 
         * BytesRef startBoundary = new BytesRef(rangeDo.start + ""); BytesRef
         * endBoundary = new BytesRef(rangeDo.end + "");
         * 
         * while (termsEnum.next() != null) { BytesRef bytesRef =
         * termsEnum.term(); logger.error("term:" + bytesRef.utf8ToString()); if
         * (bytesRef.compareTo(startBoundary) >= 0 &&
         * bytesRef.compareTo(endBoundary) <= 0) { Term term = new
         * Term(rangeDo.field, bytesRef); int nextId =
         * DocIdSetIterator.NO_MORE_DOCS; int docId = nextId; try {
         * DocsAndPositionsEnum tp =
         * terms.docsAndPositions(ramReader.getLiveDocs(), term.bytes(), null);
         * do { docId = tp.advance(nextId); if (logger.isDebugEnabled()) {
         * logger.debug("fieldRang  doc:" + docId); } if (docId !=
         * DocIdSetIterator.NO_MORE_DOCS) { int rawId = tp.docID();
         * bits.set(rawId); nextId = rawId - 1; } } while (docId !=
         * DocIdSetIterator.NO_MORE_DOCS); } catch (IOException e) {
         * logger.error(e.getMessage(), e); } } } } catch (IOException e) {
         * logger.error(e.getMessage(), e); }
         */

    }

    /**
     * 有新的数据进来 开始放入ranges中
     */
    public void newValue(int docId,String field, String term)
    {
        List<RangeDo> list = fieldRanges.get(field);
        Integer termValue = Integer.parseInt(term);
        if (termValue == null)
        {
            termValue = 0;
        }

        for (RangeDo rangeDo : list)
        {
            if (rangeDo.start <= termValue && rangeDo.end >= termValue)
            {
                OpenBitSet openBitSet = ranges.get(rangeDo.toString());
                openBitSet.fastSet(docId);
            }
        }
    }

    public Bits getRange(String field, int start, int end)
    {

        String key = field + "_" + start + "_" + end;

        Bits bits = ranges.get(key);
        if (bits == null)
        {// 第一次没有数据 好戏来啦
            RangeDo rangeDo = new RangeDo(field, start, end);
            newRange(rangeDo);
            bits = ranges.get(key);
        }
        return bits;
    }

    private class RangeDo
    {
        public String field;
        public int start;
        public int end;

        public RangeDo(String field, int start, int end)
        {
            this.field = field;
            this.start = start;
            this.end = end;
        }

        public String toString()
        {
            return field + "_" + start + "_" + end;
        }
    }

}
