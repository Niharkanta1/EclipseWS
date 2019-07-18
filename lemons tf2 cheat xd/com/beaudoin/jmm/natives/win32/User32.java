// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public final class User32
{
    public static native int GetMessageW(final WinUser.MSG p0, final WinDef.HWND p1, final int p2, final int p3);
    
    public static native short GetKeyState(final int p0);
    
    public static native WinDef.HWND GetForegroundWindow();
    
    public static native int GetWindowTextW(final WinDef.HWND p0, final char[] p1, final int p2);
    
    public static native boolean UnhookWindowsHookEx(final WinUser.HHOOK p0);
    
    public static native WinUser.HHOOK SetWindowsHookExW(final int p0, final WinUser.HOOKPROC p1, final WinDef.HINSTANCE p2, final int p3);
    
    static {
        Native.register("user32");
    }
}
