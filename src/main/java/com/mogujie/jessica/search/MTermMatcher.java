package com.mogujie.jessica.search;

import com.mogujie.jessica.index.InvertedIndexer;
import com.mogujie.jessica.query.DocTermMatch;
import com.mogujie.jessica.query.matcher.TermMatcher;
import com.mogujie.jessica.store.PostingList;
import com.mogujie.jessica.util.AbstractSkippableIterable;
import com.mogujie.jessica.util.AbstractSkippableIterator;
import com.mogujie.jessica.util.Bits;
import com.mogujie.jessica.util.DocIdSetIterator;
import com.mogujie.jessica.util.SkippableIterable;
import com.mogujie.jessica.util.SkippableIterator;

public class MTermMatcher implements TermMatcher
{
    private final InvertedIndexer indexer;

    public MTermMatcher(InvertedIndexer indexer)
    {
        this.indexer = indexer;
    }

    @Override
    public SkippableIterable<DocTermMatch> getMatches(String field, String term)
    {

        final PostingList postingList = null;
        return new AbstractSkippableIterable<DocTermMatch>()
        {
            @Override
            public SkippableIterator<DocTermMatch> iterator()
            {
                return new MTermSkippableIterator(postingList);
            }
        };
    }

    @Override
    public SkippableIterable<DocTermMatch> getMatches(final String field, final String termFrom, final String termTo)
    {
        return new AbstractSkippableIterable<DocTermMatch>()
        {
            @Override
            public SkippableIterator<DocTermMatch> iterator()
            {

                return new AbstractSkippableIterator<DocTermMatch>()
                {
                    final Bits bits = indexer.getRange(field, termFrom, termTo);
                    int current = indexer.maxDoc();

                    @Override
                    public void skipTo(int i)
                    {
                        current = i;
                    }

                    @Override
                    protected DocTermMatch computeNext()
                    {
                        while (bits.get(current) == false && current != 0)
                        {
                            current--;
                        }
                        if (current == 0)
                        {
                            return endOfData();
                        }

                        int docId = current;
                        if (docId != DocIdSetIterator.NO_MORE_DOCS)
                        {
                            current--;
                            return new DocTermMatch(docId, new int[1], 0);
                        } else
                        {
                            return endOfData();
                        }
                    }
                };
            }
        };
    }

    @Override
    public SkippableIterable<Integer> getAllDocs()
    {

        return new AbstractSkippableIterable<Integer>()
        {
            @Override
            public SkippableIterator<Integer> iterator()
            {
                return new AbstractSkippableIterator<Integer>()
                {
                    int current;
                    final int maxDoc;
                    final Bits liveDocs;
                    {
                        maxDoc = indexer.maxDoc();
                        liveDocs = indexer.liveDocs(maxDoc);
                        current = maxDoc;
                    }

                    @Override
                    public void skipTo(int i)
                    {
                        current = i;
                    }

                    @Override
                    protected Integer computeNext()
                    {
                        while (current-- > 0)
                        {
                            if (liveDocs.get(current) == false)
                            {
                                return current;
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    @Override
    public boolean hasChanges(Integer docid)
    {
        return false;
    }

}
