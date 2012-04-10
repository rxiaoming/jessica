/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package com.mogujie.jessica.service.thrift;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TField implements org.apache.thrift.TBase<TField, TField._Fields>, java.io.Serializable, Cloneable
{
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TField");

    private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short) 1);
    private static final org.apache.thrift.protocol.TField TOKEN_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("tokenCount", org.apache.thrift.protocol.TType.STRING, (short) 2);
    private static final org.apache.thrift.protocol.TField INDEXABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("indexable", org.apache.thrift.protocol.TType.BOOL, (short) 3);
    private static final org.apache.thrift.protocol.TField TOKENS_FIELD_DESC = new org.apache.thrift.protocol.TField("tokens", org.apache.thrift.protocol.TType.LIST, (short) 4);
    private static final org.apache.thrift.protocol.TField PAYLOAD_FIELD_DESC = new org.apache.thrift.protocol.TField("payload", org.apache.thrift.protocol.TType.I32, (short) 5);

    public String name; // required
    public String tokenCount; // required
    public boolean indexable; // required
    public List<TToken> tokens; // required
    public int payload; // required

    /**
     * The set of fields this struct contains, along with convenience methods
     * for finding and manipulating them.
     */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum
    {
        NAME((short) 1, "name"), TOKEN_COUNT((short) 2, "tokenCount"), INDEXABLE((short) 3, "indexable"), TOKENS((short) 4, "tokens"), PAYLOAD((short) 5, "payload");

        private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

        static
        {
            for (_Fields field : EnumSet.allOf(_Fields.class))
            {
                byName.put(field.getFieldName(), field);
            }
        }

        /**
         * Find the _Fields constant that matches fieldId, or null if its not
         * found.
         */
        public static _Fields findByThriftId(int fieldId)
        {
            switch (fieldId)
            {
            case 1: // NAME
                return NAME;
            case 2: // TOKEN_COUNT
                return TOKEN_COUNT;
            case 3: // INDEXABLE
                return INDEXABLE;
            case 4: // TOKENS
                return TOKENS;
            case 5: // PAYLOAD
                return PAYLOAD;
            default:
                return null;
            }
        }

        /**
         * Find the _Fields constant that matches fieldId, throwing an exception
         * if it is not found.
         */
        public static _Fields findByThriftIdOrThrow(int fieldId)
        {
            _Fields fields = findByThriftId(fieldId);
            if (fields == null)
                throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
            return fields;
        }

        /**
         * Find the _Fields constant that matches name, or null if its not
         * found.
         */
        public static _Fields findByName(String name)
        {
            return byName.get(name);
        }

        private final short _thriftId;
        private final String _fieldName;

        _Fields(short thriftId, String fieldName)
        {
            _thriftId = thriftId;
            _fieldName = fieldName;
        }

        public short getThriftFieldId()
        {
            return _thriftId;
        }

        public String getFieldName()
        {
            return _fieldName;
        }
    }

    // isset id assignments
    private static final int __INDEXABLE_ISSET_ID = 0;
    private static final int __PAYLOAD_ISSET_ID = 1;
    private BitSet __isset_bit_vector = new BitSet(2);

    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static
    {
        Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
        tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.REQUIRED, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.TOKEN_COUNT, new org.apache.thrift.meta_data.FieldMetaData("tokenCount", org.apache.thrift.TFieldRequirementType.REQUIRED, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.INDEXABLE, new org.apache.thrift.meta_data.FieldMetaData("indexable", org.apache.thrift.TFieldRequirementType.REQUIRED, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
        tmpMap.put(_Fields.TOKENS, new org.apache.thrift.meta_data.FieldMetaData("tokens", org.apache.thrift.TFieldRequirementType.REQUIRED, new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TToken.class))));
        tmpMap.put(_Fields.PAYLOAD, new org.apache.thrift.meta_data.FieldMetaData("payload", org.apache.thrift.TFieldRequirementType.OPTIONAL, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
        metaDataMap = Collections.unmodifiableMap(tmpMap);
        org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TField.class, metaDataMap);
    }

    public TField()
    {
    }

    public TField(String name, String tokenCount, boolean indexable, List<TToken> tokens)
    {
        this();
        this.name = name;
        this.tokenCount = tokenCount;
        this.indexable = indexable;
        setIndexableIsSet(true);
        this.tokens = tokens;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public TField(TField other)
    {
        __isset_bit_vector.clear();
        __isset_bit_vector.or(other.__isset_bit_vector);
        if (other.isSetName())
        {
            this.name = other.name;
        }
        if (other.isSetTokenCount())
        {
            this.tokenCount = other.tokenCount;
        }
        this.indexable = other.indexable;
        if (other.isSetTokens())
        {
            List<TToken> __this__tokens = new ArrayList<TToken>();
            for (TToken other_element : other.tokens)
            {
                __this__tokens.add(new TToken(other_element));
            }
            this.tokens = __this__tokens;
        }
        this.payload = other.payload;
    }

    public TField deepCopy()
    {
        return new TField(this);
    }

    @Override
    public void clear()
    {
        this.name = null;
        this.tokenCount = null;
        setIndexableIsSet(false);
        this.indexable = false;
        this.tokens = null;
        setPayloadIsSet(false);
        this.payload = 0;
    }

    public String getName()
    {
        return this.name;
    }

    public TField setName(String name)
    {
        this.name = name;
        return this;
    }

    public void unsetName()
    {
        this.name = null;
    }

    /**
     * Returns true if field name is set (has been assigned a value) and false
     * otherwise
     */
    public boolean isSetName()
    {
        return this.name != null;
    }

    public void setNameIsSet(boolean value)
    {
        if (!value)
        {
            this.name = null;
        }
    }

    public String getTokenCount()
    {
        return this.tokenCount;
    }

    public TField setTokenCount(String tokenCount)
    {
        this.tokenCount = tokenCount;
        return this;
    }

    public void unsetTokenCount()
    {
        this.tokenCount = null;
    }

    /**
     * Returns true if field tokenCount is set (has been assigned a value) and
     * false otherwise
     */
    public boolean isSetTokenCount()
    {
        return this.tokenCount != null;
    }

    public void setTokenCountIsSet(boolean value)
    {
        if (!value)
        {
            this.tokenCount = null;
        }
    }

    public boolean isIndexable()
    {
        return this.indexable;
    }

    public TField setIndexable(boolean indexable)
    {
        this.indexable = indexable;
        setIndexableIsSet(true);
        return this;
    }

    public void unsetIndexable()
    {
        __isset_bit_vector.clear(__INDEXABLE_ISSET_ID);
    }

    /**
     * Returns true if field indexable is set (has been assigned a value) and
     * false otherwise
     */
    public boolean isSetIndexable()
    {
        return __isset_bit_vector.get(__INDEXABLE_ISSET_ID);
    }

    public void setIndexableIsSet(boolean value)
    {
        __isset_bit_vector.set(__INDEXABLE_ISSET_ID, value);
    }

    public int getTokensSize()
    {
        return (this.tokens == null) ? 0 : this.tokens.size();
    }

    public java.util.Iterator<TToken> getTokensIterator()
    {
        return (this.tokens == null) ? null : this.tokens.iterator();
    }

    public void addToTokens(TToken elem)
    {
        if (this.tokens == null)
        {
            this.tokens = new ArrayList<TToken>();
        }
        this.tokens.add(elem);
    }

    public List<TToken> getTokens()
    {
        return this.tokens;
    }

    public TField setTokens(List<TToken> tokens)
    {
        this.tokens = tokens;
        return this;
    }

    public void unsetTokens()
    {
        this.tokens = null;
    }

    /**
     * Returns true if field tokens is set (has been assigned a value) and false
     * otherwise
     */
    public boolean isSetTokens()
    {
        return this.tokens != null;
    }

    public void setTokensIsSet(boolean value)
    {
        if (!value)
        {
            this.tokens = null;
        }
    }

    public int getPayload()
    {
        return this.payload;
    }

    public TField setPayload(int payload)
    {
        this.payload = payload;
        setPayloadIsSet(true);
        return this;
    }

    public void unsetPayload()
    {
        __isset_bit_vector.clear(__PAYLOAD_ISSET_ID);
    }

    /**
     * Returns true if field payload is set (has been assigned a value) and
     * false otherwise
     */
    public boolean isSetPayload()
    {
        return __isset_bit_vector.get(__PAYLOAD_ISSET_ID);
    }

    public void setPayloadIsSet(boolean value)
    {
        __isset_bit_vector.set(__PAYLOAD_ISSET_ID, value);
    }

    public void setFieldValue(_Fields field, Object value)
    {
        switch (field)
        {
        case NAME:
            if (value == null)
            {
                unsetName();
            } else
            {
                setName((String) value);
            }
            break;

        case TOKEN_COUNT:
            if (value == null)
            {
                unsetTokenCount();
            } else
            {
                setTokenCount((String) value);
            }
            break;

        case INDEXABLE:
            if (value == null)
            {
                unsetIndexable();
            } else
            {
                setIndexable((Boolean) value);
            }
            break;

        case TOKENS:
            if (value == null)
            {
                unsetTokens();
            } else
            {
                setTokens((List<TToken>) value);
            }
            break;

        case PAYLOAD:
            if (value == null)
            {
                unsetPayload();
            } else
            {
                setPayload((Integer) value);
            }
            break;

        }
    }

    public Object getFieldValue(_Fields field)
    {
        switch (field)
        {
        case NAME:
            return getName();

        case TOKEN_COUNT:
            return getTokenCount();

        case INDEXABLE:
            return Boolean.valueOf(isIndexable());

        case TOKENS:
            return getTokens();

        case PAYLOAD:
            return Integer.valueOf(getPayload());

        }
        throw new IllegalStateException();
    }

    /**
     * Returns true if field corresponding to fieldID is set (has been assigned
     * a value) and false otherwise
     */
    public boolean isSet(_Fields field)
    {
        if (field == null)
        {
            throw new IllegalArgumentException();
        }

        switch (field)
        {
        case NAME:
            return isSetName();
        case TOKEN_COUNT:
            return isSetTokenCount();
        case INDEXABLE:
            return isSetIndexable();
        case TOKENS:
            return isSetTokens();
        case PAYLOAD:
            return isSetPayload();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that)
    {
        if (that == null)
            return false;
        if (that instanceof TField)
            return this.equals((TField) that);
        return false;
    }

    public boolean equals(TField that)
    {
        if (that == null)
            return false;

        boolean this_present_name = true && this.isSetName();
        boolean that_present_name = true && that.isSetName();
        if (this_present_name || that_present_name)
        {
            if (!(this_present_name && that_present_name))
                return false;
            if (!this.name.equals(that.name))
                return false;
        }

        boolean this_present_tokenCount = true && this.isSetTokenCount();
        boolean that_present_tokenCount = true && that.isSetTokenCount();
        if (this_present_tokenCount || that_present_tokenCount)
        {
            if (!(this_present_tokenCount && that_present_tokenCount))
                return false;
            if (!this.tokenCount.equals(that.tokenCount))
                return false;
        }

        boolean this_present_indexable = true;
        boolean that_present_indexable = true;
        if (this_present_indexable || that_present_indexable)
        {
            if (!(this_present_indexable && that_present_indexable))
                return false;
            if (this.indexable != that.indexable)
                return false;
        }

        boolean this_present_tokens = true && this.isSetTokens();
        boolean that_present_tokens = true && that.isSetTokens();
        if (this_present_tokens || that_present_tokens)
        {
            if (!(this_present_tokens && that_present_tokens))
                return false;
            if (!this.tokens.equals(that.tokens))
                return false;
        }

        boolean this_present_payload = true && this.isSetPayload();
        boolean that_present_payload = true && that.isSetPayload();
        if (this_present_payload || that_present_payload)
        {
            if (!(this_present_payload && that_present_payload))
                return false;
            if (this.payload != that.payload)
                return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    public int compareTo(TField other)
    {
        if (!getClass().equals(other.getClass()))
        {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        int lastComparison = 0;
        TField typedOther = (TField) other;

        lastComparison = Boolean.valueOf(isSetName()).compareTo(typedOther.isSetName());
        if (lastComparison != 0)
        {
            return lastComparison;
        }
        if (isSetName())
        {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, typedOther.name);
            if (lastComparison != 0)
            {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetTokenCount()).compareTo(typedOther.isSetTokenCount());
        if (lastComparison != 0)
        {
            return lastComparison;
        }
        if (isSetTokenCount())
        {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tokenCount, typedOther.tokenCount);
            if (lastComparison != 0)
            {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetIndexable()).compareTo(typedOther.isSetIndexable());
        if (lastComparison != 0)
        {
            return lastComparison;
        }
        if (isSetIndexable())
        {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.indexable, typedOther.indexable);
            if (lastComparison != 0)
            {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetTokens()).compareTo(typedOther.isSetTokens());
        if (lastComparison != 0)
        {
            return lastComparison;
        }
        if (isSetTokens())
        {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tokens, typedOther.tokens);
            if (lastComparison != 0)
            {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetPayload()).compareTo(typedOther.isSetPayload());
        if (lastComparison != 0)
        {
            return lastComparison;
        }
        if (isSetPayload())
        {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.payload, typedOther.payload);
            if (lastComparison != 0)
            {
                return lastComparison;
            }
        }
        return 0;
    }

    public _Fields fieldForId(int fieldId)
    {
        return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException
    {
        org.apache.thrift.protocol.TField field;
        iprot.readStructBegin();
        while (true)
        {
            field = iprot.readFieldBegin();
            if (field.type == org.apache.thrift.protocol.TType.STOP)
            {
                break;
            }
            switch (field.id)
            {
            case 1: // NAME
                if (field.type == org.apache.thrift.protocol.TType.STRING)
                {
                    this.name = iprot.readString();
                } else
                {
                    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                }
                break;
            case 2: // TOKEN_COUNT
                if (field.type == org.apache.thrift.protocol.TType.STRING)
                {
                    this.tokenCount = iprot.readString();
                } else
                {
                    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                }
                break;
            case 3: // INDEXABLE
                if (field.type == org.apache.thrift.protocol.TType.BOOL)
                {
                    this.indexable = iprot.readBool();
                    setIndexableIsSet(true);
                } else
                {
                    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                }
                break;
            case 4: // TOKENS
                if (field.type == org.apache.thrift.protocol.TType.LIST)
                {
                    {
                        org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                        this.tokens = new ArrayList<TToken>(_list0.size);
                        for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                        {
                            TToken _elem2; // required
                            _elem2 = new TToken();
                            _elem2.read(iprot);
                            this.tokens.add(_elem2);
                        }
                        iprot.readListEnd();
                    }
                } else
                {
                    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                }
                break;
            case 5: // PAYLOAD
                if (field.type == org.apache.thrift.protocol.TType.I32)
                {
                    this.payload = iprot.readI32();
                    setPayloadIsSet(true);
                } else
                {
                    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                }
                break;
            default:
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
            }
            iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked
        // in the validate method
        if (!isSetIndexable())
        {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'indexable' was not found in serialized data! Struct: " + toString());
        }
        validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException
    {
        validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (this.name != null)
        {
            oprot.writeFieldBegin(NAME_FIELD_DESC);
            oprot.writeString(this.name);
            oprot.writeFieldEnd();
        }
        if (this.tokenCount != null)
        {
            oprot.writeFieldBegin(TOKEN_COUNT_FIELD_DESC);
            oprot.writeString(this.tokenCount);
            oprot.writeFieldEnd();
        }
        oprot.writeFieldBegin(INDEXABLE_FIELD_DESC);
        oprot.writeBool(this.indexable);
        oprot.writeFieldEnd();
        if (this.tokens != null)
        {
            oprot.writeFieldBegin(TOKENS_FIELD_DESC);
            {
                oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, this.tokens.size()));
                for (TToken _iter3 : this.tokens)
                {
                    _iter3.write(oprot);
                }
                oprot.writeListEnd();
            }
            oprot.writeFieldEnd();
        }
        if (isSetPayload())
        {
            oprot.writeFieldBegin(PAYLOAD_FIELD_DESC);
            oprot.writeI32(this.payload);
            oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("TField(");
        boolean first = true;

        sb.append("name:");
        if (this.name == null)
        {
            sb.append("null");
        } else
        {
            sb.append(this.name);
        }
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("tokenCount:");
        if (this.tokenCount == null)
        {
            sb.append("null");
        } else
        {
            sb.append(this.tokenCount);
        }
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("indexable:");
        sb.append(this.indexable);
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("tokens:");
        if (this.tokens == null)
        {
            sb.append("null");
        } else
        {
            sb.append(this.tokens);
        }
        first = false;
        if (isSetPayload())
        {
            if (!first)
                sb.append(", ");
            sb.append("payload:");
            sb.append(this.payload);
            first = false;
        }
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException
    {
        // check for required fields
        if (name == null)
        {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'name' was not present! Struct: " + toString());
        }
        if (tokenCount == null)
        {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'tokenCount' was not present! Struct: " + toString());
        }
        // alas, we cannot check 'indexable' because it's a primitive and you
        // chose the non-beans generator.
        if (tokens == null)
        {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'tokens' was not present! Struct: " + toString());
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
    {
        try
        {
            write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
        } catch (org.apache.thrift.TException te)
        {
            throw new java.io.IOException(te);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException
    {
        try
        {
            // it doesn't seem like you should have to do this, but java
            // serialization is wacky, and doesn't call the default constructor.
            __isset_bit_vector = new BitSet(1);
            read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
        } catch (org.apache.thrift.TException te)
        {
            throw new java.io.IOException(te);
        }
    }

}
