package com.mogujie.jessica.query.matcher;

import com.mogujie.jessica.query.DocTermMatch;
import com.mogujie.jessica.util.SkippableIterable;

public interface TermMatcher
{

    public SkippableIterable<DocTermMatch> getMatches(String field, String term);

    /**
     * Range (prefix) matching. All terms (existing in the index) between
     * termFrom and termTo (lexicographically speaking) will be matched for the
     * field.
     * 
     * @param field
     *            the field to match in
     * @param termFrom
     *            the left boundary of the range (inclusive)
     * @param termTo
     *            the rigth boundary of the range (exclusive)
     * 
     * @return a NavigableMap from the terms to the resulting Iterables of
     *         DocTermMatch
     */
    public SkippableIterable<DocTermMatch> getMatches(String field, String termFrom, String termTo);

    public SkippableIterable<Integer> getAllDocs();

    public boolean hasChanges(Integer docid);

}
