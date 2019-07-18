// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.unix;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public final class libc
{
    public static native int getuid();
    
    public static native int getpid();
    
    static {
        Native.register(NativeLibrary.getInstance("c"));
    }
}
