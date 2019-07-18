// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.lang.reflect.Field;

enum HotSpotPrior7u6StringHash implements StringHash
{
    INSTANCE;
    
    private static final long valueOffset;
    private static final long offsetOffset;
    
    @Override
    public long longHash(final String s, final LongHashFunction hashFunction, final int off, final int len) {
        final char[] value = (char[])UnsafeAccess.UNSAFE.getObject(s, HotSpotPrior7u6StringHash.valueOffset);
        final int offset = UnsafeAccess.UNSAFE.getInt(s, HotSpotPrior7u6StringHash.offsetOffset);
        return hashFunction.hashChars(value, offset + off, len);
    }
    
    static {
        try {
            final Field valueField = String.class.getDeclaredField("value");
            valueOffset = UnsafeAccess.UNSAFE.objectFieldOffset(valueField);
            final Field offsetField = String.class.getDeclaredField("offset");
            offsetOffset = UnsafeAccess.UNSAFE.objectFieldOffset(offsetField);
        }
        catch (NoSuchFieldException e) {
            throw new AssertionError((Object)e);
        }
    }
}
