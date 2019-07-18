// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface OleAuto extends StdCallLibrary
{
    public static final int DISPATCH_METHOD = 1;
    public static final int DISPATCH_PROPERTYGET = 2;
    public static final int DISPATCH_PROPERTYPUT = 4;
    public static final int DISPATCH_PROPERTYPUTREF = 8;
    public static final int FADF_AUTO = 1;
    public static final int FADF_STATIC = 2;
    public static final int FADF_EMBEDDED = 4;
    public static final int FADF_FIXEDSIZE = 16;
    public static final int FADF_RECORD = 32;
    public static final int FADF_HAVEIID = 64;
    public static final int FADF_HAVEVARTYPE = 128;
    public static final int FADF_BSTR = 256;
    public static final int FADF_UNKNOWN = 512;
    public static final int FADF_DISPATCH = 1024;
    public static final int FADF_VARIANT = 2048;
    public static final int FADF_RESERVED = 61448;
    public static final OleAuto INSTANCE = (OleAuto)Native.loadLibrary("OleAut32", OleAuto.class, W32APIOptions.UNICODE_OPTIONS);
    
    WTypes.BSTR SysAllocString(final String p0);
    
    void SysFreeString(final WTypes.BSTR p0);
    
    void VariantInit(final Variant.VARIANT.ByReference p0);
    
    void VariantInit(final Variant.VARIANT p0);
    
    WinNT.HRESULT VariantCopy(final Pointer p0, final Variant.VARIANT p1);
    
    WinNT.HRESULT VariantClear(final Pointer p0);
    
    OaIdl.SAFEARRAY.ByReference SafeArrayCreate(final WTypes.VARTYPE p0, final int p1, final OaIdl.SAFEARRAYBOUND[] p2);
    
    WinNT.HRESULT SafeArrayPutElement(final OaIdl.SAFEARRAY p0, final long[] p1, final Variant.VARIANT p2);
    
    WinNT.HRESULT SafeArrayGetElement(final OaIdl.SAFEARRAY p0, final long[] p1, final Pointer p2);
    
    WinNT.HRESULT SafeArrayLock(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT SafeArrayUnLock(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT GetActiveObject(final Guid.GUID p0, final WinDef.PVOID p1, final PointerByReference p2);
    
    WinNT.HRESULT LoadRegTypeLib(final Guid.GUID p0, final int p1, final int p2, final WinDef.LCID p3, final PointerByReference p4);
    
    WinNT.HRESULT LoadTypeLib(final WString p0, final PointerByReference p1);
    
    int SystemTimeToVariantTime(final WinBase.SYSTEMTIME p0, final DoubleByReference p1);
    
    public static class DISPPARAMS extends Structure
    {
        public Variant.VariantArg.ByReference rgvarg;
        public OaIdl.DISPIDByReference rgdispidNamedArgs;
        public WinDef.UINT cArgs;
        public WinDef.UINT cNamedArgs;
        
        public DISPPARAMS() {
        }
        
        public DISPPARAMS(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("rgvarg", "rgdispidNamedArgs", "cArgs", "cNamedArgs");
        }
        
        public static class ByReference extends DISPPARAMS implements Structure.ByReference
        {
        }
    }
}
