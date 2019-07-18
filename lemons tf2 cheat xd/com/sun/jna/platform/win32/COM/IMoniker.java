// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.Pointer;

public interface IMoniker extends IPersistStream
{
    void BindToObject();
    
    void BindToStorage();
    
    void Reduce();
    
    void ComposeWith();
    
    void Enum();
    
    void IsEqual();
    
    void Hash();
    
    void IsRunning();
    
    void GetTimeOfLastChange();
    
    void Inverse();
    
    void CommonPrefixWith();
    
    WinNT.HRESULT GetDisplayName(final Pointer p0, final Pointer p1, final WTypes.BSTRByReference p2);
    
    void ParseDisplayName();
    
    void IsSystemMoniker();
    
    void RelativePathTo();
}
