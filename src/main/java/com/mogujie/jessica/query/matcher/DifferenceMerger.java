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

package com.mogujie.jessica.query.matcher;

import com.mogujie.jessica.query.RawMatch;
import com.mogujie.jessica.util.AbstractSkippableIterable;
import com.mogujie.jessica.util.AbstractSkippableIterator;
import com.mogujie.jessica.util.SkippableIterable;
import com.mogujie.jessica.util.SkippableIterator;

class DifferenceMerger extends AbstractSkippableIterable<RawMatch>
{
    private final SkippableIterable<RawMatch> included;
    private final SkippableIterable<RawMatch> excluded;

    DifferenceMerger(SkippableIterable<RawMatch> left, SkippableIterable<RawMatch> right)
    {
        this.included = left;
        this.excluded = right;
    }

    @Override
    public SkippableIterator<RawMatch> iterator()
    {
        return new DifferenceIterator(included.iterator(), excluded.iterator());
    }

    private static class DifferenceIterator extends AbstractSkippableIterator<RawMatch>
    {
        private final SkippableIterator<RawMatch> included;
        private final SkippableIterator<RawMatch> excluded;
        private RawMatch excluedRawMatch = null;

        public DifferenceIterator(SkippableIterator<RawMatch> included, SkippableIterator<RawMatch> excluded)
        {
            this.included = included;
            this.excluded = excluded;
        }

        private static int id(RawMatch r)
        {
            return r.getRawId();
        }

        @Override
        protected RawMatch computeNext()
        {
            if (excluedRawMatch == null && excluded.hasNext())
            {
                excluedRawMatch = excluded.next();
            }

            while (included.hasNext())
            {
                RawMatch candidate = included.next();
                // check if the candidate is excluded
                if (excluedRawMatch != null && id(excluedRawMatch) > id(candidate))
                {// skip exclusions lower than candidate
                    excluded.skipTo(id(candidate));
                    if (excluded.hasNext())
                    {
                        excluedRawMatch = excluded.next();
                    }
                }

                // if candidate was excluded search for another candidate
                if (excluedRawMatch != null && id(candidate) == id(excluedRawMatch))
                {
                    continue;
                }
                return candidate;
            }

            return endOfData();
        }

        @Override
        public void skipTo(int i)
        {
            this.included.skipTo(i);
            this.excluded.skipTo(i - 1);
        }
    }
}
