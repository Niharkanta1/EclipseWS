// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.mac;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.LastErrorException;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public final class mac
{
    public static native int task_for_pid(final int p0, final int p1, final IntByReference p2);
    
    public static native int mach_task_self();
    
    public static native int vm_write(final int p0, final Pointer p1, final Pointer p2, final int p3);
    
    public static native int vm_read(final int p0, final Pointer p1, final int p2, final Pointer p3, final IntByReference p4);
    
    public static native int vm_read(final int p0, final Pointer p1, final int p2, final PointerByReference p3, final IntByReference p4);
    
    public static native String mach_error_string(final int p0) throws LastErrorException;
    
    static {
        Native.register(NativeLibrary.getInstance("c"));
    }
}
