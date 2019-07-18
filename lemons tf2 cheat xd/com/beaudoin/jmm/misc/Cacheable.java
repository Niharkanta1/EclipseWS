// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.misc;

import java.util.HashMap;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import java.util.function.Function;
import java.util.Map;

public final class Cacheable
{
    private static final Map<Integer, MemoryBuffer> bufferCache;
    private static final Function<Integer, MemoryBuffer> butterCreate;
    private static final Map<Integer, byte[]> arrayCache;
    private static final Function<Integer, byte[]> arrayCreate;
    private static final Pointer cachedPointer;
    public static final IntByReference INT_BY_REF;
    
    public static MemoryBuffer buffer(final int size) {
        return Cacheable.bufferCache.computeIfAbsent(size, Cacheable.butterCreate);
    }
    
    public static byte[] array(final int size) {
        return Cacheable.arrayCache.computeIfAbsent(size, Cacheable.arrayCreate);
    }
    
    public static Pointer pointer(final long address) {
        Pointer.nativeValue(Cacheable.cachedPointer, address);
        return Cacheable.cachedPointer;
    }
    
    static {
        bufferCache = new HashMap<Integer, MemoryBuffer>();
        butterCreate = MemoryBuffer::new;
        arrayCache = new HashMap<Integer, byte[]>();
        arrayCreate = (x$0 -> new byte[x$0]);
        cachedPointer = new Pointer(0L);
        INT_BY_REF = new IntByReference();
    }
}
