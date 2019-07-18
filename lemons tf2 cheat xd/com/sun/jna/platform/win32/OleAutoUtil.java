// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.COM.COMUtils;

public abstract class OleAutoUtil
{
    public static OaIdl.SAFEARRAY.ByReference createVarArray(final int size) {
        final OaIdl.SAFEARRAYBOUND[] rgsabound = { new OaIdl.SAFEARRAYBOUND(size, 0) };
        final OaIdl.SAFEARRAY.ByReference psa = OleAuto.INSTANCE.SafeArrayCreate(new WTypes.VARTYPE(12), 1, rgsabound);
        return psa;
    }
    
    public static void SafeArrayPutElement(final OaIdl.SAFEARRAY array, final long index, final Variant.VARIANT arg) {
        final long[] idx = { index };
        final WinNT.HRESULT hr = OleAuto.INSTANCE.SafeArrayPutElement(array, idx, arg);
        COMUtils.SUCCEEDED(hr);
    }
    
    public static Variant.VARIANT SafeArrayGetElement(final OaIdl.SAFEARRAY array, final long index) {
        final long[] idx = { index };
        final Variant.VARIANT result = new Variant.VARIANT();
        final WinNT.HRESULT hr = OleAuto.INSTANCE.SafeArrayGetElement(array, idx, result.getPointer());
        COMUtils.SUCCEEDED(hr);
        return result;
    }
}
