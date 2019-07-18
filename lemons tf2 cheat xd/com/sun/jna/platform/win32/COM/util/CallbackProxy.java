// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Variant;
import java.util.List;
import java.util.Collection;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.IDispatch;
import java.util.ArrayList;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.COM.util.annotation.ComEventCallback;
import java.util.HashMap;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;
import java.lang.reflect.Method;
import com.sun.jna.platform.win32.OaIdl;
import java.util.Map;
import com.sun.jna.platform.win32.COM.DispatchListener;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.IDispatchCallback;

public class CallbackProxy implements IDispatchCallback
{
    Factory factory;
    Class<?> comEventCallbackInterface;
    IComEventCallbackListener comEventCallbackListener;
    Guid.GUID.ByValue listenedToRiid;
    public DispatchListener dispatchListener;
    Map<OaIdl.DISPID, Method> dsipIdMap;
    ExecutorService executorService;
    
    public CallbackProxy(final Factory factory, final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
        this.factory = factory;
        this.comEventCallbackInterface = comEventCallbackInterface;
        this.comEventCallbackListener = comEventCallbackListener;
        this.listenedToRiid = this.createRIID(comEventCallbackInterface);
        this.dsipIdMap = this.createDispIdMap(comEventCallbackInterface);
        this.dispatchListener = new DispatchListener(this);
        this.executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                final Thread thread = new Thread(r, "COM Event Callback executor");
                thread.setDaemon(true);
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(final Thread t, final Throwable e) {
                        CallbackProxy.this.factory.comThread.uncaughtExceptionHandler.uncaughtException(t, e);
                    }
                });
                return thread;
            }
        });
    }
    
    Guid.GUID.ByValue createRIID(final Class<?> comEventCallbackInterface) {
        final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
        if (null == comInterfaceAnnotation) {
            throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
        }
        final String iidStr = comInterfaceAnnotation.iid();
        if (null == iidStr || iidStr.isEmpty()) {
            throw new COMException("ComInterface must define a value for iid");
        }
        return new Guid.GUID.ByValue(new Guid.IID(iidStr).getPointer());
    }
    
    Map<OaIdl.DISPID, Method> createDispIdMap(final Class<?> comEventCallbackInterface) {
        final Map<OaIdl.DISPID, Method> map = new HashMap<OaIdl.DISPID, Method>();
        for (final Method meth : comEventCallbackInterface.getMethods()) {
            final ComEventCallback annotation = meth.getAnnotation(ComEventCallback.class);
            if (null != annotation) {
                int dispId = annotation.dispid();
                if (-1 == dispId) {
                    dispId = this.fetchDispIdFromName(annotation);
                }
                map.put(new OaIdl.DISPID(dispId), meth);
            }
        }
        return map;
    }
    
    int fetchDispIdFromName(final ComEventCallback annotation) {
        return -1;
    }
    
    void invokeOnThread(final OaIdl.DISPID dispIdMember, final Guid.GUID.ByValue riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams) {
        final List<Object> rjargs = new ArrayList<Object>();
        if (pDispParams.cArgs.intValue() > 0) {
            final Variant.VariantArg vargs = pDispParams.rgvarg;
            vargs.setArraySize(pDispParams.cArgs.intValue());
            for (final Variant.VARIANT varg : vargs.variantArg) {
                final Object jarg = Convert.toJavaObject(varg);
                if (jarg instanceof IDispatch) {
                    final IDispatch dispatch = (IDispatch)jarg;
                    final PointerByReference ppvObject = new PointerByReference();
                    final Guid.IID iid = IUnknown.IID_IUNKNOWN;
                    dispatch.QueryInterface(new Guid.GUID.ByValue(iid), ppvObject);
                    final Unknown rawUnk = new Unknown(ppvObject.getValue());
                    final long unknownId = Pointer.nativeValue(rawUnk.getPointer());
                    final int n = rawUnk.Release();
                    final com.sun.jna.platform.win32.COM.util.IUnknown unk = this.factory.createProxy(com.sun.jna.platform.win32.COM.util.IUnknown.class, unknownId, dispatch);
                    rjargs.add(unk);
                }
                else {
                    rjargs.add(jarg);
                }
            }
        }
        final List<Object> jargs = new ArrayList<Object>(rjargs);
        final Runnable invokation = new Runnable() {
            @Override
            public void run() {
                try {
                    if (CallbackProxy.this.dsipIdMap.containsKey(dispIdMember)) {
                        final Method eventMethod = CallbackProxy.this.dsipIdMap.get(dispIdMember);
                        if (eventMethod.getParameterTypes().length != jargs.size()) {
                            CallbackProxy.this.comEventCallbackListener.errorReceivingCallbackEvent("Trying to invoke method " + eventMethod + " with " + jargs.size() + " arguments", null);
                        }
                        else {
                            try {
                                final List<Object> margs = new ArrayList<Object>();
                                final Class<?>[] params = eventMethod.getParameterTypes();
                                for (int i = 0; i < eventMethod.getParameterTypes().length; ++i) {
                                    final Class<?> paramType = params[i];
                                    final Object jobj = jargs.get(i);
                                    if (jobj != null && paramType.getAnnotation(ComInterface.class) != null) {
                                        if (!(jobj instanceof com.sun.jna.platform.win32.COM.util.IUnknown)) {
                                            throw new RuntimeException("Cannot convert argument " + jobj.getClass() + " to ComInterface " + paramType);
                                        }
                                        final com.sun.jna.platform.win32.COM.util.IUnknown unk = (com.sun.jna.platform.win32.COM.util.IUnknown)jobj;
                                        final Object mobj = unk.queryInterface(paramType);
                                        margs.add(mobj);
                                    }
                                    else {
                                        margs.add(jobj);
                                    }
                                }
                                eventMethod.invoke(CallbackProxy.this.comEventCallbackListener, margs.toArray());
                            }
                            catch (Exception e) {
                                CallbackProxy.this.comEventCallbackListener.errorReceivingCallbackEvent("Exception invoking method " + eventMethod, e);
                            }
                        }
                    }
                    else {
                        CallbackProxy.this.comEventCallbackListener.errorReceivingCallbackEvent("No method found with dispId = " + dispIdMember, null);
                    }
                }
                catch (Exception e2) {
                    CallbackProxy.this.comEventCallbackListener.errorReceivingCallbackEvent("Exception receiving callback event ", e2);
                }
            }
        };
        this.executorService.execute(invokation);
    }
    
    @Override
    public Pointer getPointer() {
        return this.dispatchListener.getPointer();
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoCount(final WinDef.UINTByReference pctinfo) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final WinDef.UINT iTInfo, final WinDef.LCID lcid, final PointerByReference ppTInfo) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT GetIDsOfNames(final Guid.GUID.ByValue riid, final WString[] rgszNames, final int cNames, final WinDef.LCID lcid, final OaIdl.DISPIDByReference rgDispId) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT Invoke(final OaIdl.DISPID dispIdMember, final Guid.GUID.ByValue riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
        this.invokeOnThread(dispIdMember, riid, lcid, wFlags, pDispParams);
        return WinError.S_OK;
    }
    
    @Override
    public WinNT.HRESULT QueryInterface(final Guid.GUID.ByValue refid, final PointerByReference ppvObject) {
        if (null == ppvObject) {
            return new WinNT.HRESULT(-2147467261);
        }
        if (refid.equals(this.listenedToRiid)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (new Guid.IID(refid.getPointer()).equals(Unknown.IID_IUNKNOWN)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (new Guid.IID(refid.getPointer()).equals(Dispatch.IID_IDISPATCH)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        return new WinNT.HRESULT(-2147467262);
    }
    
    @Override
    public int AddRef() {
        return 0;
    }
    
    @Override
    public int Release() {
        return 0;
    }
}
