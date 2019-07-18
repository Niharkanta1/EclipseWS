// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Psapi extends StdCallLibrary
{
    public static final Psapi INSTANCE = (Psapi)Native.loadLibrary("psapi", Psapi.class, W32APIOptions.DEFAULT_OPTIONS);
    
    int GetModuleFileNameExA(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final byte[] p2, final int p3);
    
    int GetModuleFileNameExW(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final char[] p2, final int p3);
    
    int GetModuleFileNameEx(final WinNT.HANDLE p0, final WinNT.HANDLE p1, final Pointer p2, final int p3);
}
