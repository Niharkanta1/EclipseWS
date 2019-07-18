// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.lang.reflect.Field;

enum ModernHotSpotStringHash implements StringHash
{
    INSTANCE;
    
    private static final long valueOffset;
    
    @Override
    public long longHash(final String s, final LongHashFunction hashFunction, final int off, final int len) {
        final char[] value = (char[])UnsafeAccess.UNSAFE.getObject(s, ModernHotSpotStringHash.valueOffset);
        return hashFunction.hashChars(value, off, len);
    }
    
    static {
        try {
            final Field valueField = String.class.getDeclaredField("value");
            valueOffset = UnsafeAccess.UNSAFE.objectFieldOffset(valueField);
        }
        catch (NoSuchFieldException e) {
            throw new AssertionError((Object)e);
        }
    }
}
