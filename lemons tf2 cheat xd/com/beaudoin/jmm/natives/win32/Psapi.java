// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.natives.win32;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import java.util.HashMap;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.beaudoin.jmm.process.NativeProcess;
import com.beaudoin.jmm.misc.Strings;
import com.beaudoin.jmm.process.impl.win32.Win32Process;
import com.beaudoin.jmm.process.Module;
import java.util.Map;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinDef;

public final class Psapi
{
    private static PsapiStdCall psapi;
    private static final WinDef.HMODULE[] lphModules;
    private static final IntByReference lpcbNeededs;
    private static final PsapiStdCall.LPMODULEINFO moduleInfo;
    private static final Map<String, Module> modules;
    private static byte[] lpImageFileName;
    
    public static Map<String, Module> getModules(final Win32Process process) {
        Psapi.modules.clear();
        EnumProcessModulesEx(process.pointer(), Psapi.lphModules, Psapi.lphModules.length, Psapi.lpcbNeededs, 3);
        for (int i = 0; i < Psapi.lpcbNeededs.getValue() / 4; ++i) {
            final WinDef.HMODULE hModule = Psapi.lphModules[i];
            if (GetModuleInformation(process.pointer(), hModule, Psapi.moduleInfo, Psapi.moduleInfo.size()) && Psapi.moduleInfo.lpBaseOfDll != null) {
                final String moduleName = Strings.transform(GetModuleBaseNameA(process.pointer(), hModule));
                Psapi.modules.put(moduleName, new Module(process, moduleName, hModule.getPointer(), Psapi.moduleInfo.SizeOfImage));
            }
        }
        return Psapi.modules;
    }
    
    public static native boolean GetModuleInformation(final Pointer p0, final WinDef.HMODULE p1, final PsapiStdCall.LPMODULEINFO p2, final int p3);
    
    public static native int GetModuleBaseNameA(final Pointer p0, final WinDef.HMODULE p1, final byte[] p2, final int p3);
    
    private static byte[] GetModuleBaseNameA(final Pointer hProcess, final WinDef.HMODULE hModule) {
        GetModuleBaseNameA(hProcess, hModule, Psapi.lpImageFileName, Psapi.lpImageFileName.length);
        return Psapi.lpImageFileName;
    }
    
    private static boolean EnumProcessModulesEx(final Pointer hProcess, final WinDef.HMODULE[] lphModule, final int cb, final IntByReference lpcbNeededs, final int flags) {
        return Psapi.psapi.EnumProcessModulesEx(hProcess, lphModule, cb, lpcbNeededs, flags);
    }
    
    static {
        Psapi.psapi = (PsapiStdCall)Native.loadLibrary("Psapi", PsapiStdCall.class);
        Native.register(NativeLibrary.getInstance("Psapi"));
        lphModules = new WinDef.HMODULE[1024];
        lpcbNeededs = new IntByReference();
        moduleInfo = new PsapiStdCall.LPMODULEINFO();
        modules = new HashMap<String, Module>();
        Psapi.lpImageFileName = new byte[128];
    }
    
    private interface PsapiStdCall extends StdCallLibrary
    {
        boolean EnumProcessModulesEx(final Pointer p0, final WinDef.HMODULE[] p1, final int p2, final IntByReference p3, final int p4);
        
        public static class LPMODULEINFO extends Structure
        {
            public WinNT.HANDLE lpBaseOfDll;
            public int SizeOfImage;
            public WinNT.HANDLE EntryPoint;
            
            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("lpBaseOfDll", "SizeOfImage", "EntryPoint");
            }
        }
    }
}
