// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.Dispatch;
import java.util.Iterator;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.PointerByReference;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import com.sun.jna.platform.win32.COM.COMUtils;
import java.util.concurrent.Callable;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.COM.Moniker;
import com.sun.jna.platform.win32.COM.IEnumMoniker;
import com.sun.jna.platform.win32.COM.IRunningObjectTable;

public class EnumMoniker implements Iterable<IDispatch>
{
    ComThread comThread;
    Factory factory;
    IRunningObjectTable rawRot;
    IEnumMoniker raw;
    Moniker rawNext;
    
    protected EnumMoniker(final IEnumMoniker raw, final IRunningObjectTable rawRot, final Factory factory) {
        this.rawRot = rawRot;
        this.raw = raw;
        this.factory = factory;
        this.comThread = factory.getComThread();
        try {
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return EnumMoniker.this.raw.Reset();
                }
            });
            COMUtils.checkRC(hr);
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
        this.cacheNext();
    }
    
    protected void cacheNext() {
        try {
            final PointerByReference rgelt = new PointerByReference();
            final WinDef.ULONGByReference pceltFetched = new WinDef.ULONGByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return EnumMoniker.this.raw.Next(new WinDef.ULONG(1L), rgelt, pceltFetched);
                }
            });
            if (WinNT.S_OK.equals(hr) && pceltFetched.getValue().intValue() > 0) {
                this.rawNext = new Moniker(rgelt.getValue());
            }
            else {
                if (!WinNT.S_FALSE.equals(hr)) {
                    COMUtils.checkRC(hr);
                }
                this.rawNext = null;
            }
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
    
    @Override
    public Iterator<IDispatch> iterator() {
        return new Iterator<IDispatch>() {
            @Override
            public boolean hasNext() {
                return null != EnumMoniker.this.rawNext;
            }
            
            @Override
            public IDispatch next() {
                try {
                    final Moniker moniker = EnumMoniker.this.rawNext;
                    final PointerByReference ppunkObject = new PointerByReference();
                    final WinNT.HRESULT hr = EnumMoniker.this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                        @Override
                        public WinNT.HRESULT call() throws Exception {
                            return EnumMoniker.this.rawRot.GetObject(moniker.getPointer(), ppunkObject);
                        }
                    });
                    COMUtils.checkRC(hr);
                    final Dispatch dispatch = new Dispatch(ppunkObject.getValue());
                    EnumMoniker.this.cacheNext();
                    final IDispatch d = EnumMoniker.this.factory.createProxy(IDispatch.class, dispatch);
                    final int n = dispatch.Release();
                    return d;
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
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}
