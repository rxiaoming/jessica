package com.mogujie.jessica.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.mogujie.jessica.util.ArrayUtil;
import com.mogujie.jessica.util.RamUsageEstimator;

public class ParallelPostingsArray
{
    final static int BYTES_PER_POSTING = 3 * RamUsageEstimator.NUM_BYTES_INT;

    public final int size;
    public final int[] textStarts;
    public int freqProxUptos[]; // docId已经写到的位置 该位置是没有数据的 如果要读数据
    public int freqProxStarts[]; // docId开始写入的位置
    public int termFreqs[];

    ParallelPostingsArray(final int size)
    {
        this.size = size;
        textStarts = new int[size];
        freqProxUptos = new int[size];
        freqProxStarts = new int[size];
        termFreqs = new int[size];
    }

    int bytesPerPosting()
    {
        return BYTES_PER_POSTING;
    }

    ParallelPostingsArray newInstance(int size)
    {
        return new ParallelPostingsArray(size);
    }

    final ParallelPostingsArray grow()
    {
        int newSize = ArrayUtil.oversize(size + 1, bytesPerPosting());
        ParallelPostingsArray newArray = newInstance(newSize);
        copyTo(newArray, size);
        return newArray;
    }

    void copyTo(ParallelPostingsArray toArray, int numToCopy)
    {
        System.arraycopy(textStarts, 0, toArray.textStarts, 0, numToCopy);
        System.arraycopy(freqProxUptos, 0, toArray.freqProxUptos, 0, numToCopy);
        System.arraycopy(freqProxStarts, 0, toArray.freqProxStarts, 0, numToCopy);
        System.arraycopy(termFreqs, 0, toArray.termFreqs, 0, numToCopy);
    }
}
