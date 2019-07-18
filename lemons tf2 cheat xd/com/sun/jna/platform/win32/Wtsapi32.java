// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.win32.StdCallLibrary;

public interface Wtsapi32 extends StdCallLibrary
{
    public static final Wtsapi32 INSTANCE = (Wtsapi32)Native.loadLibrary("Wtsapi32", Wtsapi32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int NOTIFY_FOR_ALL_SESSIONS = 1;
    public static final int NOTIFY_FOR_THIS_SESSION = 0;
    public static final int WTS_CONSOLE_CONNECT = 1;
    public static final int WTS_CONSOLE_DISCONNECT = 2;
    public static final int WTS_REMOTE_CONNECT = 3;
    public static final int WTS_REMOTE_DISCONNECT = 4;
    public static final int WTS_SESSION_LOGON = 5;
    public static final int WTS_SESSION_LOGOFF = 6;
    public static final int WTS_SESSION_LOCK = 7;
    public static final int WTS_SESSION_UNLOCK = 8;
    public static final int WTS_SESSION_REMOTE_CONTROL = 9;
    
    boolean WTSRegisterSessionNotification(final WinDef.HWND p0, final int p1);
    
    boolean WTSUnRegisterSessionNotification(final WinDef.HWND p0);
}
