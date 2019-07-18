// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;

public class COMBindingBaseObject extends COMInvoker
{
    public static final WinDef.LCID LOCALE_USER_DEFAULT;
    public static final WinDef.LCID LOCALE_SYSTEM_DEFAULT;
    private IUnknown iUnknown;
    private IDispatch iDispatch;
    private PointerByReference pDispatch;
    private PointerByReference pUnknown;
    
    public COMBindingBaseObject(final IDispatch dispatch) {
        this.pDispatch = new PointerByReference();
        this.pUnknown = new PointerByReference();
        this.iDispatch = dispatch;
    }
    
    public COMBindingBaseObject(final Guid.CLSID clsid, final boolean useActiveInstance) {
        this(clsid, useActiveInstance, 21);
    }
    
    public COMBindingBaseObject(final Guid.CLSID clsid, final boolean useActiveInstance, final int dwClsContext) {
        this.pDispatch = new PointerByReference();
        this.pUnknown = new PointerByReference();
        WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, 2);
        if (COMUtils.FAILED(hr)) {
            Ole32.INSTANCE.CoUninitialize();
            throw new COMException("CoInitialize() failed!");
        }
        if (useActiveInstance) {
            hr = OleAuto.INSTANCE.GetActiveObject(clsid, null, this.pUnknown);
            if (COMUtils.SUCCEEDED(hr)) {
                this.iUnknown = new Unknown(this.pUnknown.getValue());
                hr = this.iUnknown.QueryInterface(new Guid.GUID.ByValue(IDispatch.IID_IDISPATCH), this.pDispatch);
            }
            else {
                hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, this.pDispatch);
            }
        }
        else {
            hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, this.pDispatch);
        }
        if (COMUtils.FAILED(hr)) {
            throw new COMException("COM object with CLSID " + clsid.toGuidString() + " not registered properly!");
        }
        this.iDispatch = new Dispatch(this.pDispatch.getValue());
    }
    
    public COMBindingBaseObject(final String progId, final boolean useActiveInstance, final int dwClsContext) throws COMException {
        this.pDispatch = new PointerByReference();
        this.pUnknown = new PointerByReference();
        WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, 2);
        if (COMUtils.FAILED(hr)) {
            this.release();
            throw new COMException("CoInitialize() failed!");
        }
        final Guid.CLSID.ByReference clsid = new Guid.CLSID.ByReference();
        hr = Ole32.INSTANCE.CLSIDFromProgID(progId, clsid);
        if (COMUtils.FAILED(hr)) {
            Ole32.INSTANCE.CoUninitialize();
            throw new COMException("CLSIDFromProgID() failed!");
        }
        if (useActiveInstance) {
            hr = OleAuto.INSTANCE.GetActiveObject(clsid, null, this.pUnknown);
            if (COMUtils.SUCCEEDED(hr)) {
                this.iUnknown = new Unknown(this.pUnknown.getValue());
                hr = this.iUnknown.QueryInterface(new Guid.GUID.ByValue(IDispatch.IID_IDISPATCH), this.pDispatch);
            }
            else {
                hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, this.pDispatch);
            }
        }
        else {
            hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, this.pDispatch);
        }
        if (COMUtils.FAILED(hr)) {
            throw new COMException("COM object with ProgID '" + progId + "' and CLSID " + clsid.toGuidString() + " not registered properly!");
        }
        this.iDispatch = new Dispatch(this.pDispatch.getValue());
    }
    
    public COMBindingBaseObject(final String progId, final boolean useActiveInstance) throws COMException {
        this(progId, useActiveInstance, 21);
    }
    
    public IDispatch getIDispatch() {
        return this.iDispatch;
    }
    
    public PointerByReference getIDispatchPointer() {
        return this.pDispatch;
    }
    
    public IUnknown getIUnknown() {
        return this.iUnknown;
    }
    
    public PointerByReference getIUnknownPointer() {
        return this.pUnknown;
    }
    
    public void release() {
        if (this.iDispatch != null) {
            this.iDispatch.Release();
        }
        Ole32.INSTANCE.CoUninitialize();
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final String name, final Variant.VARIANT[] pArgs) throws COMException {
        if (pDisp == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        final WString[] ptName = { new WString(name) };
        final OaIdl.DISPIDByReference pdispID = new OaIdl.DISPIDByReference();
        final WinNT.HRESULT hr = pDisp.GetIDsOfNames(new Guid.GUID.ByValue(Guid.IID_NULL), ptName, 1, COMBindingBaseObject.LOCALE_USER_DEFAULT, pdispID);
        COMUtils.checkRC(hr);
        return this.oleMethod(nType, pvResult, pDisp, pdispID.getValue(), pArgs);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT[] pArgs) throws COMException {
        if (pDisp == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        int _argsLen = 0;
        Variant.VARIANT[] _args = null;
        final OleAuto.DISPPARAMS.ByReference dp = new OleAuto.DISPPARAMS.ByReference();
        final OaIdl.EXCEPINFO.ByReference pExcepInfo = new OaIdl.EXCEPINFO.ByReference();
        final IntByReference puArgErr = new IntByReference();
        if (pArgs != null && pArgs.length > 0) {
            _argsLen = pArgs.length;
            _args = new Variant.VARIANT[_argsLen];
            int revCount = _argsLen;
            for (int i = 0; i < _argsLen; ++i) {
                _args[i] = pArgs[--revCount];
            }
        }
        if (nType == 4) {
            dp.cNamedArgs = new WinDef.UINT((long)_argsLen);
            dp.rgdispidNamedArgs = new OaIdl.DISPIDByReference(OaIdl.DISPID_PROPERTYPUT);
        }
        if (_argsLen > 0) {
            dp.cArgs = new WinDef.UINT((long)_args.length);
            dp.rgvarg = new Variant.VariantArg.ByReference(_args);
            dp.write();
        }
        final WinNT.HRESULT hr = pDisp.Invoke(dispId, new Guid.GUID.ByValue(Guid.IID_NULL), COMBindingBaseObject.LOCALE_SYSTEM_DEFAULT, new WinDef.WORD((long)nType), dp, pvResult, pExcepInfo, puArgErr);
        COMUtils.checkRC(hr, pExcepInfo, puArgErr);
        return hr;
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final String name, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final String name) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, (Variant.VARIANT[])null);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final IDispatch pDisp, final OaIdl.DISPID dispId) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, (Variant.VARIANT[])null);
    }
    
    protected void checkFailed(final WinNT.HRESULT hr) {
        COMUtils.checkRC(hr, null, null);
    }
    
    static {
        LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
        LOCALE_SYSTEM_DEFAULT = Kernel32.INSTANCE.GetSystemDefaultLCID();
    }
}
