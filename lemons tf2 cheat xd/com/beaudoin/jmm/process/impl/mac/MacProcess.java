// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.process.impl.mac;

import com.beaudoin.jmm.natives.mac.mac;
import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.Pointer;
import java.util.HashMap;
import com.beaudoin.jmm.process.Module;
import java.util.Map;
import com.beaudoin.jmm.process.NativeProcess;

public final class MacProcess implements NativeProcess
{
    private final int id;
    private final int task;
    private Map<String, Module> modules;
    
    public MacProcess(final int id, final int mach_task) {
        this.modules = new HashMap<String, Module>();
        this.id = id;
        this.task = mach_task;
        this.initModules();
    }
    
    public int task() {
        return this.task;
    }
    
    @Override
    public int id() {
        return this.id;
    }
    
    @Override
    public void initModules() {
    }
    
    @Override
    public Module findModule(final String moduleName) {
        return null;
    }
    
    @Override
    public MemoryBuffer read(final Pointer address, final int size) {
        final MemoryBuffer buffer = Cacheable.buffer(size);
        if (mac.vm_read(this.task(), address, size, buffer, Cacheable.INT_BY_REF) != 0 || Cacheable.INT_BY_REF.getValue() != size) {
            throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
        }
        Pointer.nativeValue(buffer, Pointer.nativeValue(buffer.getPointer(0L)));
        return buffer;
    }
    
    @Override
    public NativeProcess write(final Pointer address, final MemoryBuffer buffer) {
        if (mac.vm_write(this.task(), address, buffer, buffer.size()) != 0) {
            throw new RuntimeException("Write memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
        }
        return this;
    }
    
    @Override
    public boolean canRead(final Pointer address, final int size) {
        try {
            this.read(address, size);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
