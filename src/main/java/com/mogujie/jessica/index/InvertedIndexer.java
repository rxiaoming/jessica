/*
 * Copyright (c) 2011 LinkedIn, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
import com.mogujie.jessica.store.FakeBits;
import com.mogujie.jessica.store.PostingListStore;
import com.mogujie.jessica.util.Bits;
import com.mogujie.jessica.util.ByteBlockPool;
import com.mogujie.jessica.util.ByteBlockPool.DirectAllocator;
import com.mogujie.jessica.util.RamUsageEstimator;

public class InvertedIndexer implements Indexer
{
    private final static Logger logger = Logger.getLogger(InvertedIndexer.class);
    private static final int HASH_INIT_SIZE = 4;

    private volatile int maxDocCount;
    private final AtomicLong bytesUsed;
    final PostingListStore plStore;
    private final DirectAllocator allocator = new DirectAllocator();
    private final ConcurrentHashMap<String, InvertedIndexPerField> invertedIndexPerFields = new ConcurrentHashMap<String, InvertedIndexPerField>();
    private final Map<Integer, Integer> uid2docMap = new HashMap<Integer, Integer>();
    private final int doc2uidArray[] = new int[1 << 24];// 64M
    private final int delDocIds[] = new int[1 << 24];// 64M
    private final IntBlockPool intPool;
    ByteBlockPool termPool;
    private final int numPostingInt = 2;
    private MRangeFieldListener rangeFieldListener;

    public InvertedIndexer()
    {
        bytesUsed = new AtomicLong(0);
        intPool = new IntBlockPool(this);
        termPool = new ByteBlockPool(allocator);
        plStore = new PostingListStore(this);
        rangeFieldListener = new MRangeFieldListener(this);
    }

    @Override
    public void add(TDocument document)
    {
        internalAdd(document);
    }

    private void internalAdd(TDocument document)
    {
        int docId = ++maxDocCount;
        int objectId = document.object_id;

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
            }
        }

    }

    /**
     * @param macDocId
     *            对应当前最大的macDocId
     * @return Bits 一个可以判断是否该为数据为删除的bits
     */
    public Bits liveDocs(int maxDocId)
    {
        FakeBits newLiveDocs = new FakeBits(delDocIds, maxDocId);
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
}
