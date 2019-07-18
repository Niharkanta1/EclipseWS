// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinNT;

public abstract class COMUtils
{
    public static final int S_OK = 0;
    public static final int S_FALSE = 1;
    public static final int E_UNEXPECTED = -2147418113;
    
    public static boolean SUCCEEDED(final WinNT.HRESULT hr) {
        return SUCCEEDED(hr.intValue());
    }
    
    public static boolean SUCCEEDED(final int hr) {
        return hr >= 0;
    }
    
    public static boolean FAILED(final WinNT.HRESULT hr) {
        return FAILED(hr.intValue());
    }
    
    public static boolean FAILED(final int hr) {
        return hr < 0;
    }
    
    public static void checkRC(final WinNT.HRESULT hr) {
        checkRC(hr, null, null);
    }
    
    public static void checkRC(final WinNT.HRESULT hr, final OaIdl.EXCEPINFO pExcepInfo, final IntByReference puArgErr) {
        if (FAILED(hr)) {
            final String formatMessageFromHR = Kernel32Util.formatMessage(hr);
            throw new COMException(formatMessageFromHR, pExcepInfo, puArgErr);
        }
    }
    
    public static ArrayList<COMInfo> getAllCOMInfoOnSystem() {
        WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
        WinReg.HKEYByReference phkResult2 = new WinReg.HKEYByReference();
        final ArrayList<COMInfo> comInfos = new ArrayList<COMInfo>();
        try {
            phkResult = Advapi32Util.registryGetKey(WinReg.HKEY_CLASSES_ROOT, "CLSID", 131097);
            final Advapi32Util.InfoKey infoKey = Advapi32Util.registryQueryInfoKey(phkResult.getValue(), 131097);
            for (int i = 0; i < infoKey.lpcSubKeys.getValue(); ++i) {
                final Advapi32Util.EnumKey enumKey = Advapi32Util.registryRegEnumKey(phkResult.getValue(), i);
                final String subKey = Native.toString(enumKey.lpName);
                final COMInfo comInfo = new COMInfo(subKey);
                phkResult2 = Advapi32Util.registryGetKey(phkResult.getValue(), subKey, 131097);
                final Advapi32Util.InfoKey infoKey2 = Advapi32Util.registryQueryInfoKey(phkResult2.getValue(), 131097);
                for (int y = 0; y < infoKey2.lpcSubKeys.getValue(); ++y) {
                    final Advapi32Util.EnumKey enumKey2 = Advapi32Util.registryRegEnumKey(phkResult2.getValue(), y);
                    final String subKey2 = Native.toString(enumKey2.lpName);
                    if (subKey2.equals("InprocHandler32")) {
                        comInfo.inprocHandler32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("InprocServer32")) {
                        comInfo.inprocServer32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("LocalServer32")) {
                        comInfo.localServer32 = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("ProgID")) {
                        comInfo.progID = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                    else if (subKey2.equals("TypeLib")) {
                        comInfo.typeLib = (String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null);
                    }
                }
                Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
                comInfos.add(comInfo);
            }
        }
        finally {
            Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
            Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
        }
        return comInfos;
    }
    
    public static class COMInfo
    {
        public String clsid;
        public String inprocHandler32;
        public String inprocServer32;
        public String localServer32;
        public String progID;
        public String typeLib;
        
        public COMInfo() {
        }
        
        public COMInfo(final String clsid) {
            this.clsid = clsid;
        }
    }
}
