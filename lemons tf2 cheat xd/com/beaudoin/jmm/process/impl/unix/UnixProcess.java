// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.process.impl.unix;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import java.util.Iterator;
import java.io.IOException;
import com.sun.jna.Pointer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import com.beaudoin.jmm.process.Module;
import java.util.Map;
import com.beaudoin.jmm.natives.unix.unix;
import com.beaudoin.jmm.process.NativeProcess;

public final class UnixProcess implements NativeProcess
{
    private final int id;
    private unix.iovec local;
    private unix.iovec remote;
    private Map<String, Module> modules;
    
    public UnixProcess(final int id) {
        this.local = new unix.iovec();
        this.remote = new unix.iovec();
        this.modules = new HashMap<String, Module>();
        this.id = id;
        this.initModules();
    }
    
    @Override
    public int id() {
        return this.id;
    }
    
    @Override
    public void initModules() {
        try {
            for (final String line : Files.readAllLines(Paths.get("/proc/" + this.id() + "/maps", new String[0]))) {
                final String[] split = line.split(" ");
                final String[] regionSplit = split[0].split("-");
                final long start = Long.parseLong(regionSplit[0], 16);
                final long end = Long.parseLong(regionSplit[1], 16);
                final long offset = Long.parseLong(split[2], 16);
                if (offset <= 0L) {
                    continue;
                }
                String path = "";
                for (int i = 5; i < split.length; ++i) {
                    final String s = split[i].trim();
                    if (!s.isEmpty()) {
                        path += split[i];
                    }
                    if (s.isEmpty() && ++i > split.length) {
                        break;
                    }
                    if (s.isEmpty() && !split[i].trim().isEmpty()) {
                        path += split[i];
                    }
                }
                final String modulename = path.substring(path.lastIndexOf("/") + 1, path.length());
                this.modules.put(modulename, new Module(this, modulename, Pointer.createConstant(start), end - start));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Module findModule(final String moduleName) {
        return this.modules.get(moduleName);
    }
    
    @Override
    public MemoryBuffer read(final Pointer address, final int size) {
        final MemoryBuffer buffer = Cacheable.buffer(size);
        this.local.iov_base = buffer;
        this.remote.iov_base = address;
        final unix.iovec remote = this.remote;
        this.local.iov_len = size;
        remote.iov_len = size;
        if (unix.process_vm_readv(this.id, this.local, 1L, this.remote, 1L, 0L) != size) {
            throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
        }
        return buffer;
    }
    
    @Override
    public NativeProcess write(final Pointer address, final MemoryBuffer buffer) {
        this.local.iov_base = buffer;
        this.remote.iov_base = address;
        final unix.iovec remote = this.remote;
        final unix.iovec local = this.local;
        final int size = buffer.size();
        local.iov_len = size;
        remote.iov_len = size;
        if (unix.process_vm_writev(this.id, this.local, 1L, this.remote, 1L, 0L) != buffer.size()) {
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
