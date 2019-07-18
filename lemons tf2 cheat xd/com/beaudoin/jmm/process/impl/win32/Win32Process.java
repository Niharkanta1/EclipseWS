// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.process.impl.win32;

import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.Native;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.win32.Psapi;
import com.beaudoin.jmm.process.Module;
import java.util.Map;
import com.sun.jna.Pointer;
import com.beaudoin.jmm.process.NativeProcess;

public final class Win32Process implements NativeProcess
{
    private final int id;
    private final Pointer handle;
    private Map<String, Module> modules;
    
    public Win32Process(final int id, final Pointer handle) {
        this.id = id;
        this.handle = handle;
        this.initModules();
    }
    
    public Pointer pointer() {
        return this.handle;
    }
    
    @Override
    public int id() {
        return this.id;
    }
    
    @Override
    public void initModules() {
        this.modules = Psapi.getModules(this);
    }
    
    @Override
    public Module findModule(final String moduleName) {
        Module module = this.modules.get(moduleName);
        if (module == null) {
            int attempts = 60;
            while (attempts-- > 0 && module == null) {
                module = this.modules.get(moduleName);
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.initModules();
            }
            if (module == null) {
                throw new RuntimeException(moduleName + " was not found!");
            }
        }
        return this.modules.get(moduleName);
    }
    
    @Override
    public MemoryBuffer read(final Pointer address, final int size) {
        final MemoryBuffer buffer = Cacheable.buffer(size);
        if (Kernel32.ReadProcessMemory(this.pointer(), address, buffer, size, 0) == 0L) {
            throw new Win32Exception(Native.getLastError());
        }
        return buffer;
    }
    
    @Override
    public NativeProcess write(final Pointer address, final MemoryBuffer buffer) {
        if (Kernel32.WriteProcessMemory(this.pointer(), address, buffer, buffer.size(), 0) == 0L) {
            throw new Win32Exception(Native.getLastError());
        }
        return this;
    }
    
    @Override
    public boolean canRead(final Pointer address, final int size) {
        return Kernel32.ReadProcessMemory(this.pointer(), address, Cacheable.buffer(size), size, 0) != 0L;
    }
}
