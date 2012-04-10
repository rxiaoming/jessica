package com.mogujie.jessica.index;

import com.mogujie.jessica.service.thrift.TField;

/**
 * 
 * @author xuanxi
 * 
 */
public class ThriftField
{
    public TField tField;

    public ThriftField(TField tField)
    {
        this.tField = tField;
    }

    public String name()
    {
        return tField.getName();
    }
}
