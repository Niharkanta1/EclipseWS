// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;

public class UnknownListener extends Structure
{
    public UnknownVTable.ByReference vtbl;
    
    public UnknownListener(final IUnknownCallback callback) {
        this.vtbl = this.constructVTable();
        this.initVTable(callback);
        super.write();
    }
    
    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("vtbl");
    }
    
    protected UnknownVTable.ByReference constructVTable() {
        return new UnknownVTable.ByReference();
    }
    
    protected void initVTable(final IUnknownCallback callback) {
        this.vtbl.QueryInterfaceCallback = new UnknownVTable.QueryInterfaceCallback() {
            @Override
            public WinNT.HRESULT invoke(final Pointer thisPointer, final Guid.GUID.ByValue refid, final PointerByReference ppvObject) {
                return callback.QueryInterface(refid, ppvObject);
            }
        };
        this.vtbl.AddRefCallback = new UnknownVTable.AddRefCallback() {
            @Override
            public int invoke(final Pointer thisPointer) {
                return callback.AddRef();
            }
        };
        this.vtbl.ReleaseCallback = new UnknownVTable.ReleaseCallback() {
            @Override
            public int invoke(final Pointer thisPointer) {
                return callback.Release();
            }
        };
    }
}
