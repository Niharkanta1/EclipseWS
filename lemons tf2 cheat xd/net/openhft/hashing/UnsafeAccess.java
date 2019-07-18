// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

final class UnsafeAccess extends Access<Object>
{
    public static final UnsafeAccess INSTANCE;
    static final Unsafe UNSAFE;
    static final long BOOLEAN_BASE;
    static final long BYTE_BASE;
    static final long CHAR_BASE;
    static final long SHORT_BASE;
    static final long INT_BASE;
    static final long LONG_BASE;
    
    private UnsafeAccess() {
    }
    
    @Override
    public long getLong(final Object input, final long offset) {
        return UnsafeAccess.UNSAFE.getLong(input, offset);
    }
    
    @Override
    public long getUnsignedInt(final Object input, final long offset) {
        return Primitives.unsignedInt(this.getInt(input, offset));
    }
    
    @Override
    public int getInt(final Object input, final long offset) {
        return UnsafeAccess.UNSAFE.getInt(input, offset);
    }
    
    @Override
    public int getUnsignedShort(final Object input, final long offset) {
        return Primitives.unsignedShort(this.getShort(input, offset));
    }
    
    @Override
    public int getShort(final Object input, final long offset) {
        return UnsafeAccess.UNSAFE.getShort(input, offset);
    }
    
    @Override
    public int getUnsignedByte(final Object input, final long offset) {
        return Primitives.unsignedByte(this.getByte(input, offset));
    }
    
    @Override
    public int getByte(final Object input, final long offset) {
        return UnsafeAccess.UNSAFE.getByte(input, offset);
    }
    
    @Override
    public ByteOrder byteOrder(final Object input) {
        return ByteOrder.nativeOrder();
    }
    
    static {
        INSTANCE = new UnsafeAccess();
        try {
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
            BOOLEAN_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(boolean[].class);
            BYTE_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(byte[].class);
            CHAR_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(char[].class);
            SHORT_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(short[].class);
            INT_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(int[].class);
            LONG_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(long[].class);
        }
        catch (Exception e) {
            throw new AssertionError((Object)e);
        }
    }
}
