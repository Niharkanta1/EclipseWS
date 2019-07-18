// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Arrays;
import com.sun.jna.ptr.IntByReference;
import java.util.List;
import com.sun.jna.WString;

public final class User32Util
{
    public static final int registerWindowMessage(final String lpString) {
        final int messageId = User32.INSTANCE.RegisterWindowMessage(lpString);
        if (messageId == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return messageId;
    }
    
    public static final WinDef.HWND createWindow(final String className, final String windowName, final int style, final int x, final int y, final int width, final int height, final WinDef.HWND parent, final WinDef.HMENU menu, final WinDef.HINSTANCE instance, final WinDef.LPVOID param) {
        return createWindowEx(0, className, windowName, style, x, y, width, height, parent, menu, instance, param);
    }
    
    public static final WinDef.HWND createWindowEx(final int exStyle, final String className, final String windowName, final int style, final int x, final int y, final int width, final int height, final WinDef.HWND parent, final WinDef.HMENU menu, final WinDef.HINSTANCE instance, final WinDef.LPVOID param) {
        final WinDef.HWND hWnd = User32.INSTANCE.CreateWindowEx(exStyle, new WString(className), windowName, style, x, y, width, height, parent, menu, instance, param);
        if (hWnd == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return hWnd;
    }
    
    public static final void destroyWindow(final WinDef.HWND hWnd) {
        if (!User32.INSTANCE.DestroyWindow(hWnd)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static final List<WinUser.RAWINPUTDEVICELIST> GetRawInputDeviceList() {
        final IntByReference puiNumDevices = new IntByReference(0);
        final WinUser.RAWINPUTDEVICELIST placeholder = new WinUser.RAWINPUTDEVICELIST();
        final int cbSize = placeholder.sizeof();
        int returnValue = User32.INSTANCE.GetRawInputDeviceList(null, puiNumDevices, cbSize);
        if (returnValue != 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final int deviceCount = puiNumDevices.getValue();
        final WinUser.RAWINPUTDEVICELIST[] records = (WinUser.RAWINPUTDEVICELIST[])placeholder.toArray(deviceCount);
        returnValue = User32.INSTANCE.GetRawInputDeviceList(records, puiNumDevices, cbSize);
        if (returnValue == -1) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (returnValue != records.length) {
            throw new IllegalStateException("Mismatched allocated (" + records.length + ") vs. received devices count (" + returnValue + ")");
        }
        return Arrays.asList(records);
    }
}
