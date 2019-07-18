// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

final class Primitives
{
    private Primitives() {
    }
    
    static long unsignedInt(final int i) {
        return (long)i & 0xFFFFFFFFL;
    }
    
    static int unsignedShort(final int s) {
        return s & 0xFFFF;
    }
    
    static int unsignedByte(final int b) {
        return b & 0xFF;
    }
}
