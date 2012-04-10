package com.mogujie.jessica.query;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class MatchAllQuery extends QueryNode
{

    private static final long serialVersionUID = 1L;

    @Override
    public String toString()
    {
        return "<MatchAllQuery>";
    }

    @Override
    public int hashCode()
    {
        return 55; // an arbitrary number.
    }

    @Override
    public QueryNode duplicate()
    {
        return new MatchAllQuery();
    }

}
