// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

final class ByteBufferAccess extends Access<ByteBuffer>
{
    public static final ByteBufferAccess INSTANCE;
    
    private ByteBufferAccess() {
    }
    
    @Override
    public long getLong(final ByteBuffer input, final long offset) {
        return input.getLong((int)offset);
    }
    
    @Override
    public long getUnsignedInt(final ByteBuffer input, final long offset) {
        return Primitives.unsignedInt(this.getInt(input, offset));
    }
    
    @Override
    public int getInt(final ByteBuffer input, final long offset) {
        return input.getInt((int)offset);
    }
    
    @Override
    public int getUnsignedShort(final ByteBuffer input, final long offset) {
        return Primitives.unsignedShort(this.getShort(input, offset));
    }
    
    @Override
    public int getShort(final ByteBuffer input, final long offset) {
        return input.getShort((int)offset);
    }
    
    @Override
    public int getUnsignedByte(final ByteBuffer input, final long offset) {
        return Primitives.unsignedByte(this.getByte(input, offset));
    }
    
    @Override
    public int getByte(final ByteBuffer input, final long offset) {
        return input.get((int)offset);
    }
    
    @Override
    public ByteOrder byteOrder(final ByteBuffer input) {
        return input.order();
    }
    
    static {
        INSTANCE = new ByteBufferAccess();
    }
}
