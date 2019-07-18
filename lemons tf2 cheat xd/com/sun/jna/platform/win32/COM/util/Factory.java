// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import com.sun.jna.platform.win32.COM.IDispatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import com.sun.jna.platform.win32.COM.RunningObjectTable;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Ole32;
import java.util.concurrent.Callable;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.util.WeakHashMap;

public class Factory
{
    ComThread comThread;
    WeakHashMap<ProxyObject, Integer> registeredObjects;
    
    public Factory() {
        this(new ComThread("Default Factory COM Thread", 5000L, new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
            }
        }));
    }
    
    public Factory(final ComThread comThread) {
        this.comThread = comThread;
        this.registeredObjects = new WeakHashMap<ProxyObject, Integer>();
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.disposeAll();
        }
        finally {
            super.finalize();
        }
    }
    
    public ComThread getComThread() {
        return this.comThread;
    }
    
    public IRunningObjectTable getRunningObjectTable() {
        try {
            final PointerByReference rotPtr = new PointerByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return Ole32.INSTANCE.GetRunningObjectTable(new WinDef.DWORD(0L), rotPtr);
                }
            });
            COMUtils.checkRC(hr);
            final RunningObjectTable raw = new RunningObjectTable(rotPtr.getValue());
            final IRunningObjectTable rot = new com.sun.jna.platform.win32.COM.util.RunningObjectTable(raw, this);
            return rot;
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e2) {
            throw new RuntimeException(e2);
        }
        catch (TimeoutException e3) {
            throw new RuntimeException(e3);
        }
    }
    
    public <T> T createProxy(final Class<T> comInterface, final IDispatch dispatch) {
        final ProxyObject jop = new ProxyObject(comInterface, dispatch, this);
        final Object proxy = Proxy.newProxyInstance(comInterface.getClassLoader(), new Class[] { comInterface }, jop);
        final T result = comInterface.cast(proxy);
        return result;
    }
    
     <T> T createProxy(final Class<T> comInterface, final long unknownId, final IDispatch dispatch) {
        final ProxyObject jop = new ProxyObject(comInterface, unknownId, dispatch, this);
        final Object proxy = Proxy.newProxyInstance(comInterface.getClassLoader(), new Class[] { comInterface }, jop);
        final T result = comInterface.cast(proxy);
        return result;
    }
    
    public <T> T createObject(final Class<T> comInterface) {
        try {
            final ComObject comObectAnnotation = comInterface.getAnnotation(ComObject.class);
            if (null == comObectAnnotation) {
                throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
            }
            final Guid.GUID guid = this.discoverClsId(comObectAnnotation);
            final PointerByReference ptrDisp = new PointerByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return Ole32.INSTANCE.CoCreateInstance(guid, null, 21, IDispatch.IID_IDISPATCH, ptrDisp);
                }
            });
            COMUtils.checkRC(hr);
            final Dispatch d = new Dispatch(ptrDisp.getValue());
            final T t = this.createProxy(comInterface, d);
            final int n = d.Release();
            return t;
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e2) {
            throw new RuntimeException(e2);
        }
        catch (TimeoutException e3) {
            throw new RuntimeException(e3);
        }
    }
    
    public <T> T fetchObject(final Class<T> comInterface) {
        try {
            final ComObject comObectAnnotation = comInterface.getAnnotation(ComObject.class);
            if (null == comObectAnnotation) {
                throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
            }
            final Guid.GUID guid = this.discoverClsId(comObectAnnotation);
            final PointerByReference ptrDisp = new PointerByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return OleAuto.INSTANCE.GetActiveObject(guid, null, ptrDisp);
                }
            });
            COMUtils.checkRC(hr);
            final Dispatch d = new Dispatch(ptrDisp.getValue());
            final T t = this.createProxy(comInterface, d);
            d.Release();
            return t;
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e2) {
            throw new RuntimeException(e2);
        }
        catch (TimeoutException e3) {
            throw new RuntimeException(e3);
        }
    }
    
    Guid.GUID discoverClsId(final ComObject annotation) {
        try {
            final String clsIdStr = annotation.clsId();
            final String progIdStr = annotation.progId();
            if (null != clsIdStr && !clsIdStr.isEmpty()) {
                return new Guid.CLSID(clsIdStr);
            }
            if (null != progIdStr && !progIdStr.isEmpty()) {
                final Guid.CLSID.ByReference rclsid = new Guid.CLSID.ByReference();
                final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                    @Override
                    public WinNT.HRESULT call() throws Exception {
                        return Ole32.INSTANCE.CLSIDFromProgID(progIdStr, rclsid);
                    }
                });
                COMUtils.checkRC(hr);
                return rclsid;
            }
            throw new COMException("ComObject must define a value for either clsId or progId");
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e2) {
            throw new RuntimeException(e2);
        }
        catch (TimeoutException e3) {
            throw new RuntimeException(e3);
        }
    }
    
    public void register(final ProxyObject proxyObject) {
        synchronized (this.registeredObjects) {
            if (this.registeredObjects.containsKey(proxyObject)) {
                final int r = this.registeredObjects.get(proxyObject);
                this.registeredObjects.put(proxyObject, r + 1);
            }
            else {
                this.registeredObjects.put(proxyObject, 1);
            }
        }
    }
    
    public void unregister(final ProxyObject proxyObject, final int d) {
        synchronized (this.registeredObjects) {
            if (!this.registeredObjects.containsKey(proxyObject)) {
                throw new RuntimeException("Tried to dispose a ProxyObject that is not registered");
            }
            final int r = this.registeredObjects.get(proxyObject);
            if (r > 1) {
                this.registeredObjects.put(proxyObject, r - d);
            }
            else {
                this.registeredObjects.remove(proxyObject);
            }
        }
    }
    
    public void disposeAll() {
        synchronized (this.registeredObjects) {
            final Set<ProxyObject> s = new HashSet<ProxyObject>(this.registeredObjects.keySet());
            for (final ProxyObject proxyObject : s) {
                final int r = this.registeredObjects.get(proxyObject);
                proxyObject.dispose(r);
            }
            this.registeredObjects.clear();
        }
    }
}
