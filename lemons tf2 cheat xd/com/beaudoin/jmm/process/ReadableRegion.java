// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Strings;
import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.Pointer;

public interface ReadableRegion
{
    MemoryBuffer read(final Pointer p0, final int p1);
    
    NativeProcess write(final Pointer p0, final MemoryBuffer p1);
    
    boolean canRead(final Pointer p0, final int p1);
    
    default boolean readBoolean(final long address) {
        return this.read(address, 1).getBoolean();
    }
    
    default int readByte(final long address) {
        return this.read(address, 1).getByte();
    }
    
    default int readShort(final long address) {
        return this.read(address, 2).getShort();
    }
    
    default int readInt(final long address) {
        return this.read(address, 4).getInt();
    }
    
    default long readUnsignedInt(final long address) {
        return Integer.toUnsignedLong(this.read(address, 4).getInt());
    }
    
    default long readLong(final long address) {
        return this.read(address, 8).getLong();
    }
    
    default float readFloat(final long address) {
        return this.read(address, 4).getFloat();
    }
    
    default double readDouble(final long address) {
        return this.read(address, 8).getDouble();
    }
    
    default String readString(final long address, final int length) {
        final byte[] bytes = Cacheable.array(length);
        this.read(address, bytes.length).get(bytes);
        return Strings.transform(bytes);
    }
    
    default MemoryBuffer read(final long address, final int size) {
        return this.read(Cacheable.pointer(address), size);
    }
    
    default NativeProcess writeBoolean(final long address, final boolean value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(1).putBoolean(value));
    }
    
    default NativeProcess writeByte(final long address, final int value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(1).putByte(value));
    }
    
    default NativeProcess writeShort(final long address, final int value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(2).putShort(value));
    }
    
    default NativeProcess writeInt(final long address, final int value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(4).putInt(value));
    }
    
    default NativeProcess writeLong(final long address, final long value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(8).putLong(value));
    }
    
    default NativeProcess writeFloat(final long address, final float value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(4).putFloat(value));
    }
    
    default NativeProcess writeDouble(final long address, final double value) {
        return this.write(Cacheable.pointer(address), Cacheable.buffer(8).putDouble(value));
    }
    
    default boolean canRead(final long address, final int size) {
        return this.canRead(Cacheable.pointer(address), size);
    }
}
