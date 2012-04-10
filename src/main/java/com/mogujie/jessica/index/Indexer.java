package com.mogujie.jessica.index;

import com.mogujie.jessica.service.thrift.TDocument;


public interface Indexer {
    public abstract void add(TDocument document);
}
