// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.COMException;

public interface IConnectionPoint
{
    IComEventCallbackCookie advise(final Class<?> p0, final IComEventCallbackListener p1) throws COMException;
    
    void unadvise(final Class<?> p0, final IComEventCallbackCookie p1);
}
