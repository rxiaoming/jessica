package com.mogujie.jessica.index;

import java.util.Arrays;

public final class IntBlockPool extends IntBlocks
{

    public int bufferUpto = -1; // Which buffer we are upto
    public int intUpto = InvertedIndexer.INT_BLOCK_SIZE; // Where we
                                                         // are in head
                                                         // buffer

    public int[] buffer; // Current head buffer
    public int intOffset = -InvertedIndexer.INT_BLOCK_SIZE; // Current
                                                            // head
                                                            // offset

    final private InvertedIndexer docWriter;

    public IntBlockPool(InvertedIndexer docWriter)
    {
        super(new int[10][]);
        this.docWriter = docWriter;
    }

    public void reset()
    {
        if (bufferUpto != -1)
        {
            // Reuse first buffer
            if (bufferUpto > 0)
            {
                docWriter.recycleIntBlocks(buffers, 1, bufferUpto - 1);
                Arrays.fill(buffers, 1, bufferUpto, null);
            }
            bufferUpto = 0;
            intUpto = 0;
            intOffset = 0;
            buffer = buffers[0];
        }
    }

    public void nextBuffer()
    {
        if (1 + bufferUpto == buffers.length)
        {
            int[][] newBuffers = new int[(int) (buffers.length * 1.5)][];
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffer = buffers[1 + bufferUpto] = docWriter.getIntBlock();
        bufferUpto++;

        intUpto = 0;
        intOffset += InvertedIndexer.INT_BLOCK_SIZE;
    }
}
