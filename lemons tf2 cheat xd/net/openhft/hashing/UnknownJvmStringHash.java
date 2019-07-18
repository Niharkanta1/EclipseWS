// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

enum UnknownJvmStringHash implements StringHash
{
    INSTANCE;
    
    @Override
    public long longHash(final String s, final LongHashFunction hashFunction, final int off, final int len) {
        return hashFunction.hashNativeChars(s, off, len);
    }
}
