// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

public class TlbParameterNotFoundException extends RuntimeException
{
    public TlbParameterNotFoundException() {
    }
    
    public TlbParameterNotFoundException(final String msg) {
        super(msg);
    }
    
    public TlbParameterNotFoundException(final Throwable cause) {
        super(cause);
    }
    
    public TlbParameterNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
