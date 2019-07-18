// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.unix;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.LastErrorException;

public final class unix
{
    public static native long process_vm_readv(final int p0, final iovec p1, final long p2, final iovec p3, final long p4, final long p5) throws LastErrorException;
    
    public static native long process_vm_writev(final int p0, final iovec p1, final long p2, final iovec p3, final long p4, final long p5) throws LastErrorException;
    
    static {
        Native.register(NativeLibrary.getInstance("c"));
    }
    
    public static class iovec extends Structure
    {
        public Pointer iov_base;
        public int iov_len;
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("iov_base", "iov_len");
        }
    }
}
