// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.util.Iterator;
import com.sun.jna.platform.win32.COM.COMException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import com.sun.jna.platform.win32.COM.IEnumMoniker;
import com.sun.jna.platform.win32.COM.EnumMoniker;
import com.sun.jna.platform.win32.COM.COMUtils;
import java.util.concurrent.Callable;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class RunningObjectTable implements IRunningObjectTable
{
    Factory factory;
    ComThread comThread;
    com.sun.jna.platform.win32.COM.RunningObjectTable raw;
    
    protected RunningObjectTable(final com.sun.jna.platform.win32.COM.RunningObjectTable raw, final Factory factory) {
        this.raw = raw;
        this.factory = factory;
        this.comThread = factory.getComThread();
    }
    
    @Override
    public Iterable<IDispatch> enumRunning() {
        try {
            final PointerByReference ppenumMoniker = new PointerByReference();
            final WinNT.HRESULT hr = this.comThread.execute((Callable<WinNT.HRESULT>)new Callable<WinNT.HRESULT>() {
                @Override
                public WinNT.HRESULT call() throws Exception {
                    return RunningObjectTable.this.raw.EnumRunning(ppenumMoniker);
                }
            });
            COMUtils.checkRC(hr);
            final EnumMoniker raw = new EnumMoniker(ppenumMoniker.getValue());
            return new com.sun.jna.platform.win32.COM.util.EnumMoniker(raw, this.raw, this.factory);
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
    public <T> List<T> getActiveObjectsByInterface(final Class<T> comInterface) {
        final List<T> result = new ArrayList<T>();
        for (final IDispatch obj : this.enumRunning()) {
            try {
                final T dobj = obj.queryInterface(comInterface);
                result.add(dobj);
            }
            catch (COMException ex) {}
        }
        return result;
    }
}
