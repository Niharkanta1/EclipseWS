// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OaIdl;

public class COMException extends RuntimeException
{
    private OaIdl.EXCEPINFO pExcepInfo;
    private IntByReference puArgErr;
    private int uArgErr;
    
    public COMException() {
    }
    
    public COMException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public COMException(final String message) {
        super(message);
    }
    
    public COMException(final String message, final OaIdl.EXCEPINFO pExcepInfo, final IntByReference puArgErr) {
        super(message + " (puArgErr=" + ((null == puArgErr) ? "" : Integer.valueOf(puArgErr.getValue())) + ")");
        this.pExcepInfo = pExcepInfo;
        this.puArgErr = puArgErr;
    }
    
    public COMException(final Throwable cause) {
        super(cause);
    }
    
    public OaIdl.EXCEPINFO getExcepInfo() {
        return this.pExcepInfo;
    }
    
    public IntByReference getArgErr() {
        return this.puArgErr;
    }
    
    public int getuArgErr() {
        return this.uArgErr;
    }
    
    public void setuArgErr(final int uArgErr) {
        this.uArgErr = uArgErr;
    }
}
