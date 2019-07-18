// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.misc;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public final class MemoryBuffer extends Pointer
{
    private int size;
    
    public MemoryBuffer(final int size) {
        super(Native.malloc(size));
        this.size = size;
    }
    
    public MemoryBuffer putBoolean(final boolean value) {
        this.setByte(0L, (byte)(value ? 1 : 0));
        return this;
    }
    
    public MemoryBuffer putByte(final int value) {
        this.setByte(0L, (byte)value);
        return this;
    }
    
    public MemoryBuffer putShort(final int value) {
        this.setShort(0L, (short)value);
        return this;
    }
    
    public MemoryBuffer putInt(final int value) {
        this.setInt(0L, value);
        return this;
    }
    
    public MemoryBuffer putLong(final long value) {
        this.setLong(0L, value);
        return this;
    }
    
    public MemoryBuffer putFloat(final float value) {
        this.setFloat(0L, value);
        return this;
    }
    
    public MemoryBuffer putDouble(final double value) {
        this.setDouble(0L, value);
        return this;
    }
    
    public void get(final byte[] dest) {
        this.read(0L, dest, 0, dest.length);
    }
    
    public boolean getBoolean() {
        return this.getByte() == 1;
    }
    
    public int getByte() {
        return this.getByte(0L);
    }
    
    public int getShort() {
        return this.getShort(0L);
    }
    
    public int getInt() {
        return this.getInt(0L);
    }
    
    public long getLong() {
        return this.getLong(0L);
    }
    
    public float getFloat() {
        return this.getFloat(0L);
    }
    
    public double getDouble() {
        return this.getDouble(0L);
    }
    
    public int size() {
        return this.size;
    }
    
    public byte[] array() {
        final byte[] data = Cacheable.array(this.size);
        this.get(data);
        return data;
    }
}
