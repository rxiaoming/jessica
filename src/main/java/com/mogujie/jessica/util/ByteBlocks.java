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
package com.mogujie.jessica.util;


public class ByteBlocks
{
    public byte[][] buffers;

    public ByteBlocks(byte[][] buffers)
    {
        this.buffers = buffers;
    }

    public static ByteBlocks copyRef(ByteBlockPool bbp)
    {
        byte[][] buffers = bbp.buffers;
        return new ByteBlocks(buffers);
    }

    public final BytesRef setBytesRef(BytesRef term, int textStart)
    {
        final byte[] bytes = term.bytes = buffers[textStart >> ByteBlockPool.BYTE_BLOCK_SHIFT];
        int pos = textStart & ByteBlockPool.BYTE_BLOCK_MASK;
        if ((bytes[pos] & 0x80) == 0)
        {
            // length is 1 byte
            term.length = bytes[pos];
            term.offset = pos + 1;
        } else
        {
            // length is 2 bytes
            term.length = (bytes[pos] & 0x7f) + ((bytes[pos + 1] & 0xff) << 7);
            term.offset = pos + 2;
        }
        assert term.length >= 0;
        return term;
    }
}
