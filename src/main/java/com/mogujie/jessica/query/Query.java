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

package com.mogujie.jessica.query;

import java.io.Serializable;

/**
 * Wrapper for IndexTank queries, holds query metadata.
 */
public final class Query implements Serializable
{
    private static final long serialVersionUID = 1L;

    private QueryNode root;
    private String originalStr;
    private int now;

    /**
     * Default constructor.
     * 
     * @param originalStr
     *            the original user generated query string, if applicable.
     */
    public Query(QueryNode root, String originalStr)
    {
        this.root = root;
        this.originalStr = originalStr;
        this.now = (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Abstract method that returns the original user-generated query string.
     * 
     * @return The original user-generated query string, or null.
     */
    public String getOriginalStr()
    {
        return originalStr;
    }

    /**
     * Returns the query root node.
     * 
     * @return the query root node.
     */
    public QueryNode getRoot()
    {
        return root;
    }

    /**
     * Returns the query creation timestamp.
     */
    public int getNow()
    {
        return now;
    }


    public String toString()
    {
        return root.toString();
    }

    public Query duplicate()
    {
        return new Query(this.root.duplicate(), this.originalStr);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + now;
        result = prime * result + ((originalStr == null) ? 0 : originalStr.hashCode());
        result = prime * result + ((root == null) ? 0 : root.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Query other = (Query) obj;
        if (now != other.now)
            return false;
        if (originalStr == null)
        {
            if (other.originalStr != null)
                return false;
        } else if (!originalStr.equals(other.originalStr))
            return false;
        if (root == null)
        {
            if (other.root != null)
                return false;
        } else if (!root.equals(other.root))
            return false;
        return true;
    }

}
