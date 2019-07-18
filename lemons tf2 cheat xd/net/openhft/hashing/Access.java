// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public abstract class Access<T>
{
    public static <T> Access<T> unsafe() {
        return (Access<T>)UnsafeAccess.INSTANCE;
    }
    
    public static Access<ByteBuffer> toByteBuffer() {
        return ByteBufferAccess.INSTANCE;
    }
    
    public static <T extends CharSequence> Access<T> toNativeCharSequence() {
        return (Access<T>)CharSequenceAccess.nativeCharSequenceAccess();
    }
    
    public static <T extends CharSequence> Access<T> toCharSequence(final ByteOrder backingOrder) {
        return (Access<T>)CharSequenceAccess.charSequenceAccess(backingOrder);
    }
    
    protected Access() {
    }
    
    public long getLong(final T input, final long offset) {
        if (this.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
            return this.getUnsignedInt(input, offset) | this.getUnsignedInt(input, offset + 4L) << 32;
        }
        return this.getUnsignedInt(input, offset + 4L) | this.getUnsignedInt(input, offset) << 32;
    }
    
    public long getUnsignedInt(final T input, final long offset) {
        return (long)this.getInt(input, offset) & 0xFFFFFFFFL;
    }
    
    public int getInt(final T input, final long offset) {
        if (this.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
            return this.getUnsignedShort(input, offset) | this.getUnsignedShort(input, offset + 2L) << 16;
        }
        return this.getUnsignedShort(input, offset + 2L) | this.getUnsignedShort(input, offset) << 16;
    }
    
    public int getUnsignedShort(final T input, final long offset) {
        if (this.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
            return this.getUnsignedByte(input, offset) | this.getUnsignedByte(input, offset + 1L) << 8;
        }
        return this.getUnsignedByte(input, offset + 1L) | this.getUnsignedByte(input, offset) << 8;
    }
    
    public int getShort(final T input, final long offset) {
        return (short)this.getUnsignedShort(input, offset);
    }
    
    public int getUnsignedByte(final T input, final long offset) {
        return this.getByte(input, offset) & 0xFF;
    }
    
    public abstract int getByte(final T p0, final long p1);
    
    public abstract ByteOrder byteOrder(final T p0);
}
