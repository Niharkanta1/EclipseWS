// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.process;

import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.Native;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.process.impl.win32.Win32Process;
import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.Pointer;

public final class Module implements ReadableRegion
{
    private final NativeProcess process;
    private final String name;
    private final long address;
    private final int size;
    private final Pointer pointer;
    private MemoryBuffer data;
    
    public Module(final NativeProcess process, final String name, final Pointer pointer, final long size) {
        this.process = process;
        this.name = name;
        this.address = Pointer.nativeValue(pointer);
        this.size = (int)size;
        this.pointer = pointer;
    }
    
    public NativeProcess process() {
        return this.process;
    }
    
    public Pointer pointer() {
        return this.pointer;
    }
    
    public String name() {
        return this.name;
    }
    
    public int size() {
        return this.size;
    }
    
    public long address() {
        return this.address;
    }
    
    public MemoryBuffer data() {
        return this.data(false);
    }
    
    public MemoryBuffer data(final boolean forceNew) {
        if (forceNew || this.data == null) {
            this.data = this.process().read(this.pointer(), this.size());
        }
        return this.data;
    }
    
    @Override
    public MemoryBuffer read(final Pointer offset, final int size) {
        final MemoryBuffer buffer = Cacheable.buffer(size);
        if (Kernel32.ReadProcessMemory(((Win32Process)this.process()).pointer(), Cacheable.pointer(this.address() + Pointer.nativeValue(offset)), buffer, size, 0) == 0L) {
            throw new Win32Exception(Native.getLastError());
        }
        return buffer;
    }
    
    @Override
    public NativeProcess write(final Pointer offset, final MemoryBuffer buffer) {
        if (Kernel32.WriteProcessMemory(((Win32Process)this.process()).pointer(), Cacheable.pointer(this.address() + Pointer.nativeValue(offset)), buffer, buffer.size(), 0) == 0L) {
            throw new Win32Exception(Native.getLastError());
        }
        return this.process();
    }
    
    @Override
    public boolean canRead(final Pointer offset, final int size) {
        return Kernel32.ReadProcessMemory(((Win32Process)this.process()).pointer(), Cacheable.pointer(this.address() + Pointer.nativeValue(offset)), Cacheable.buffer(size), size, 0) != 0L;
    }
}
