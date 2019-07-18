// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.win32;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

public final class Kernel32
{
    public static native Pointer CreateToolhelp32Snapshot(final WinDef.DWORD p0, final int p1);
    
    public static native boolean CloseHandle(final Pointer p0);
    
    public static native Pointer OpenProcess(final int p0, final boolean p1, final int p2);
    
    public static native boolean Process32Next(final Pointer p0, final Tlhelp32.PROCESSENTRY32 p1);
    
    public static native long ReadProcessMemory(final Pointer p0, final Pointer p1, final MemoryBuffer p2, final int p3, final int p4);
    
    public static native long WriteProcessMemory(final Pointer p0, final Pointer p1, final MemoryBuffer p2, final int p3, final int p4);
    
    public static native WinDef.HMODULE GetModuleHandle(final String p0);
    
    public static native boolean GetExitCodeProcess(final Pointer p0, final IntByReference p1);
    
    static {
        Native.register(NativeLibrary.getInstance("Kernel32", W32APIOptions.UNICODE_OPTIONS));
    }
}
