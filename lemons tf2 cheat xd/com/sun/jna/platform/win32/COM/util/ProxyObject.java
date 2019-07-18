// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.COM.IUnknownCallback;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.ConnectionPointContainer;
import com.sun.jna.platform.win32.COM.ConnectionPoint;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.IUnknown;
import java.util.concurrent.Callable;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;
import java.lang.reflect.InvocationHandler;

public class ProxyObject implements InvocationHandler, IDispatch, IRawDispatchHandle
{
    long unknownId;
    Class<?> theInterface;
    Factory factory;
    ComThread comThread;
    com.sun.jna.platform.win32.COM.IDispatch rawDispatch;
    public static final WinDef.LCID LOCALE_USER_DEFAULT;
    public static final WinDef.LCID LOCALE_SYSTEM_DEFAULT;
    
    public ProxyObject(final Class<?> theInterface, final com.sun.jna.platform.win32.COM.IDispatch rawDispatch, final Factory factory) {
        this.unknownId = -1L;
        this.rawDispatch = rawDispatch;
        this.comThread = factory.getComThread();
        this.theInterface = theInterface;
        this.factory = factory;
        final int n = this.rawDispatch.AddRef();
        this.getUnknownId();
        factory.register(this);
    }
    
    ProxyObject(final Class<?> theInterface, final long unknownId, final com.sun.jna.platform.win32.COM.IDispatch rawDispatch, final Factory factory) {
        this.unknownId = unknownId;
        this.rawDispatch = rawDispatch;
        this.comThread = factory.getComThread();
        this.theInterface = theInterface;
        this.factory = factory;
        final int n = this.rawDispatch.AddRef();
        factory.register(this);
    }
    
    long getUnknownId() {
        if (-1L == this.unknownId) {
            try {
                final PointerByReference ppvObject = new PointerByReference();
                final Thread current = Thread.currentThread();
                final String tn = current.getName();
                final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                    @Override
                    public WinNT.HRESULT call() throws Exception {
                        final Guid.IID iid = com.sun.jna.platform.win32.COM.IUnknown.IID_IUNKNOWN;
                        return ProxyObject.this.getRawDispatch().QueryInterface(new Guid.GUID.ByValue(iid), ppvObject);
                    }
                });
                if (!WinNT.S_OK.equals(hr)) {
                    final String formatMessageFromHR = Kernel32Util.formatMessage(hr);
                    throw new COMException("getUnknownId: " + formatMessageFromHR);
                }
                final Dispatch dispatch = new Dispatch(ppvObject.getValue());
                this.unknownId = Pointer.nativeValue(dispatch.getPointer());
                final int n = dispatch.Release();
            }
            catch (Exception e) {
                throw new COMException("Error occured when trying get Unknown Id ", e);
            }
        }
        return this.unknownId;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.dispose(1);
    }
    
    public void dispose(final int r) {
        if (!((Dispatch)this.rawDispatch).getPointer().equals(Pointer.NULL)) {
            for (int i = 0; i < r; ++i) {
                final int n2;
                final int n = n2 = this.rawDispatch.Release();
            }
            this.factory.unregister(this, r);
            ((Dispatch)this.rawDispatch).setPointer(Pointer.NULL);
        }
    }
    
    @Override
    public com.sun.jna.platform.win32.COM.IDispatch getRawDispatch() {
        return this.rawDispatch;
    }
    
    @Override
    public boolean equals(final Object arg) {
        if (null == arg) {
            return false;
        }
        if (arg instanceof ProxyObject) {
            final ProxyObject other = (ProxyObject)arg;
            return this.getUnknownId() == other.getUnknownId();
        }
        if (Proxy.isProxyClass(arg.getClass())) {
            final InvocationHandler handler = Proxy.getInvocationHandler(arg);
            if (handler instanceof ProxyObject) {
                try {
                    final ProxyObject other2 = (ProxyObject)handler;
                    return this.getUnknownId() == other2.getUnknownId();
                }
                catch (Exception e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getUnknownId().intValue();
    }
    
    @Override
    public String toString() {
        return this.theInterface.getName() + "{unk=" + this.hashCode() + "}";
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return this.invokeSynchronised(proxy, method, args);
    }
    
    synchronized Object invokeSynchronised(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.equals(Object.class.getMethod("toString", (Class<?>[])new Class[0]))) {
            return this.toString();
        }
        if (method.equals(Object.class.getMethod("equals", Object.class))) {
            return this.equals(args[0]);
        }
        if (method.equals(Object.class.getMethod("hashCode", (Class<?>[])new Class[0]))) {
            return this.hashCode();
        }
        if (method.equals(IRawDispatchHandle.class.getMethod("getRawDispatch", (Class<?>[])new Class[0]))) {
            return this.getRawDispatch();
        }
        if (method.equals(IUnknown.class.getMethod("queryInterface", Class.class))) {
            return this.queryInterface((Class<Object>)args[0]);
        }
        if (method.equals(IConnectionPoint.class.getMethod("advise", Class.class, IComEventCallbackListener.class))) {
            return this.advise((Class<?>)args[0], (IComEventCallbackListener)args[1]);
        }
        if (method.equals(IConnectionPoint.class.getMethod("unadvise", Class.class, IComEventCallbackCookie.class))) {
            this.unadvise((Class<?>)args[0], (IComEventCallbackCookie)args[1]);
            return null;
        }
        final Class<?> returnType = method.getReturnType();
        final boolean isVoid = Void.TYPE.equals(returnType);
        final ComProperty prop = method.getAnnotation(ComProperty.class);
        if (null != prop) {
            if (isVoid) {
                final String propName = this.getMutatorName(method, prop);
                this.setProperty(propName, args[0]);
                return null;
            }
            final String propName = this.getAccessorName(method, prop);
            return this.getProperty(returnType, propName, args);
        }
        else {
            final ComMethod meth = method.getAnnotation(ComMethod.class);
            if (null != meth) {
                final String methName = this.getMethodName(method, meth);
                final Object res = this.invokeMethod(returnType, methName, args);
                return res;
            }
            return null;
        }
    }
    
    ConnectionPoint fetchRawConnectionPoint(final Guid.IID iid) throws InterruptedException, ExecutionException, TimeoutException {
        final IConnectionPointContainer cpc = this.queryInterface(IConnectionPointContainer.class);
        final Dispatch rawCpcDispatch = (Dispatch)cpc.getRawDispatch();
        final ConnectionPointContainer rawCpc = new ConnectionPointContainer(rawCpcDispatch.getPointer());
        final Guid.REFIID adviseRiid = new Guid.REFIID(iid.getPointer());
        final PointerByReference ppCp = new PointerByReference();
        final WinNT.HRESULT hr = this.factory.getComThread().execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
            @Override
            public WinNT.HRESULT call() throws Exception {
                return rawCpc.FindConnectionPoint(adviseRiid, ppCp);
            }
        });
        COMUtils.checkRC(hr);
        final ConnectionPoint rawCp = new ConnectionPoint(ppCp.getValue());
        return rawCp;
    }
    
    public IComEventCallbackCookie advise(final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
        try {
            final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final ConnectionPoint rawCp = this.fetchRawConnectionPoint(iid);
            final IDispatchCallback rawListener = new CallbackProxy(this.factory, comEventCallbackInterface, comEventCallbackListener);
            comEventCallbackListener.setDispatchCallbackListener(rawListener);
            final WinDef.DWORDByReference pdwCookie = new WinDef.DWORDByReference();
            final WinNT.HRESULT hr = this.factory.getComThread().execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return rawCp.Advise(rawListener, pdwCookie);
                }
            });
            final int n = rawCp.Release();
            COMUtils.checkRC(hr);
            return new ComEventCallbackCookie(pdwCookie.getValue());
        }
        catch (Exception e) {
            throw new COMException("Error occured in advise when trying to connect the listener " + comEventCallbackListener, e);
        }
    }
    
    public void unadvise(final Class<?> comEventCallbackInterface, final IComEventCallbackCookie cookie) {
        try {
            final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("unadvise: Interface must define a value for iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final ConnectionPoint rawCp = this.fetchRawConnectionPoint(iid);
            final WinNT.HRESULT hr = this.factory.getComThread().execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return rawCp.Unadvise(((ComEventCallbackCookie)cookie).getValue());
                }
            });
            rawCp.Release();
            COMUtils.checkRC(hr);
        }
        catch (Exception e) {
            throw new COMException("Error occured in unadvise when trying to disconnect the listener from " + this, e);
        }
    }
    
    @Override
    public <T> void setProperty(final String name, final T value) {
        final Variant.VARIANT v = Convert.toVariant(value);
        final WinNT.HRESULT hr = this.oleMethod(4, null, this.getRawDispatch(), name, v);
        COMUtils.checkRC(hr);
    }
    
    @Override
    public <T> T getProperty(final Class<T> returnType, final String name, final Object... args) {
        Variant.VARIANT[] vargs;
        if (null == args) {
            vargs = new Variant.VARIANT[0];
        }
        else {
            vargs = new Variant.VARIANT[args.length];
        }
        for (int i = 0; i < vargs.length; ++i) {
            vargs[i] = Convert.toVariant(args[i]);
        }
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        final WinNT.HRESULT hr = this.oleMethod(2, result, this.getRawDispatch(), name, vargs);
        COMUtils.checkRC(hr);
        final Object jobj = Convert.toJavaObject(result);
        if (IComEnum.class.isAssignableFrom(returnType)) {
            return Convert.toComEnum(returnType, jobj);
        }
        if (jobj instanceof com.sun.jna.platform.win32.COM.IDispatch) {
            final com.sun.jna.platform.win32.COM.IDispatch d = (com.sun.jna.platform.win32.COM.IDispatch)jobj;
            final T t = this.factory.createProxy(returnType, d);
            final int n = d.Release();
            return t;
        }
        return (T)jobj;
    }
    
    @Override
    public <T> T invokeMethod(final Class<T> returnType, final String name, final Object... args) {
        Variant.VARIANT[] vargs;
        if (null == args) {
            vargs = new Variant.VARIANT[0];
        }
        else {
            vargs = new Variant.VARIANT[args.length];
        }
        for (int i = 0; i < vargs.length; ++i) {
            vargs[i] = Convert.toVariant(args[i]);
        }
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        final WinNT.HRESULT hr = this.oleMethod(1, result, this.getRawDispatch(), name, vargs);
        COMUtils.checkRC(hr);
        final Object jobj = Convert.toJavaObject(result);
        if (IComEnum.class.isAssignableFrom(returnType)) {
            return Convert.toComEnum(returnType, jobj);
        }
        if (jobj instanceof com.sun.jna.platform.win32.COM.IDispatch) {
            final com.sun.jna.platform.win32.COM.IDispatch d = (com.sun.jna.platform.win32.COM.IDispatch)jobj;
            final T t = this.factory.createProxy(returnType, d);
            final int n = d.Release();
            return t;
        }
        return (T)jobj;
    }
    
    @Override
    public <T> T queryInterface(final Class<T> comInterface) throws COMException {
        try {
            final ComInterface comInterfaceAnnotation = comInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("queryInterface: Interface must define a value for iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final PointerByReference ppvObject = new PointerByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return ProxyObject.this.getRawDispatch().QueryInterface(new Guid.GUID.ByValue(iid), ppvObject);
                }
            });
            if (WinNT.S_OK.equals(hr)) {
                final Dispatch dispatch = new Dispatch(ppvObject.getValue());
                final T t = this.factory.createProxy(comInterface, dispatch);
                final int n = dispatch.Release();
                return t;
            }
            final String formatMessageFromHR = Kernel32Util.formatMessage(hr);
            throw new COMException("queryInterface: " + formatMessageFromHR);
        }
        catch (Exception e) {
            throw new COMException("Error occured when trying to query for interface " + comInterface.getName(), e);
        }
    }
    
    Guid.IID getIID(final ComInterface annotation) {
        final String iidStr = annotation.iid();
        if (null != iidStr && !iidStr.isEmpty()) {
            return new Guid.IID(iidStr);
        }
        throw new COMException("ComInterface must define a value for iid");
    }
    
    private String getAccessorName(final Method method, final ComProperty prop) {
        if (!prop.name().isEmpty()) {
            return prop.name();
        }
        final String methName = method.getName();
        if (methName.startsWith("get")) {
            return methName.replaceFirst("get", "");
        }
        throw new RuntimeException("Property Accessor name must start with 'get', or set the anotation 'name' value");
    }
    
    private String getMutatorName(final Method method, final ComProperty prop) {
        if (!prop.name().isEmpty()) {
            return prop.name();
        }
        final String methName = method.getName();
        if (methName.startsWith("set")) {
            return methName.replaceFirst("set", "");
        }
        throw new RuntimeException("Property Mutator name must start with 'set', or set the anotation 'name' value");
    }
    
    private String getMethodName(final Method method, final ComMethod meth) {
        if (meth.name().isEmpty()) {
            final String methName = method.getName();
            return methName;
        }
        return meth.name();
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, (Variant.VARIANT[])null);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, (Variant.VARIANT[])null);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name, final Variant.VARIANT[] pArgs) throws COMException {
        try {
            if (pDisp == null) {
                throw new COMException("pDisp (IDispatch) parameter is null!");
            }
            final WString[] ptName = { new WString(name) };
            final OaIdl.DISPIDByReference pdispID = new OaIdl.DISPIDByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    final WinNT.HRESULT hr = pDisp.GetIDsOfNames(new Guid.GUID.ByValue(Guid.IID_NULL), ptName, 1, ProxyObject.LOCALE_USER_DEFAULT, pdispID);
                    return hr;
                }
            });
            COMUtils.checkRC(hr);
            return this.oleMethod(nType, pvResult, pDisp, pdispID.getValue(), pArgs);
        }
        catch (InterruptedException e) {
            throw new COMException(e);
        }
        catch (ExecutionException e2) {
            throw new COMException(e2);
        }
        catch (TimeoutException e3) {
            throw new COMException(e3);
        }
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT[] pArgs) throws COMException {
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
        try {
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return pDisp.Invoke(dispId, new Guid.GUID.ByValue(Guid.IID_NULL), ProxyObject.LOCALE_SYSTEM_DEFAULT, new WinDef.WORD((long)nType), dp, pvResult, pExcepInfo, puArgErr);
                }
            });
            COMUtils.checkRC(hr, pExcepInfo, puArgErr);
            return hr;
        }
        catch (InterruptedException e) {
            throw new COMException(e);
        }
        catch (ExecutionException e2) {
            throw new COMException(e2);
        }
        catch (TimeoutException e3) {
            throw new COMException(e3);
        }
    }
    
    static {
        LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
        LOCALE_SYSTEM_DEFAULT = Kernel32.INSTANCE.GetSystemDefaultLCID();
    }
}
