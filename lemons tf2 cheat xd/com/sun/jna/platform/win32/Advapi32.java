// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.WString;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Advapi32 extends StdCallLibrary
{
    public static final Advapi32 INSTANCE = (Advapi32)Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);
    public static final int MAX_KEY_LENGTH = 255;
    public static final int MAX_VALUE_NAME = 16383;
    public static final int RRF_RT_ANY = 65535;
    public static final int RRF_RT_DWORD = 24;
    public static final int RRF_RT_QWORD = 72;
    public static final int RRF_RT_REG_BINARY = 8;
    public static final int RRF_RT_REG_DWORD = 16;
    public static final int RRF_RT_REG_EXPAND_SZ = 4;
    public static final int RRF_RT_REG_MULTI_SZ = 32;
    public static final int RRF_RT_REG_NONE = 1;
    public static final int RRF_RT_REG_QWORD = 64;
    public static final int RRF_RT_REG_SZ = 2;
    
    boolean GetUserNameW(final char[] p0, final IntByReference p1);
    
    boolean LookupAccountName(final String p0, final String p1, final WinNT.PSID p2, final IntByReference p3, final char[] p4, final IntByReference p5, final PointerByReference p6);
    
    boolean LookupAccountSid(final String p0, final WinNT.PSID p1, final char[] p2, final IntByReference p3, final char[] p4, final IntByReference p5, final PointerByReference p6);
    
    boolean ConvertSidToStringSid(final WinNT.PSID p0, final PointerByReference p1);
    
    boolean ConvertStringSidToSid(final String p0, final WinNT.PSIDByReference p1);
    
    int GetLengthSid(final WinNT.PSID p0);
    
    boolean IsValidSid(final WinNT.PSID p0);
    
    boolean IsWellKnownSid(final WinNT.PSID p0, final int p1);
    
    boolean CreateWellKnownSid(final int p0, final WinNT.PSID p1, final WinNT.PSID p2, final IntByReference p3);
    
    boolean LogonUser(final String p0, final String p1, final String p2, final int p3, final int p4, final WinNT.HANDLEByReference p5);
    
    boolean OpenThreadToken(final WinNT.HANDLE p0, final int p1, final boolean p2, final WinNT.HANDLEByReference p3);
    
    boolean SetThreadToken(final WinNT.HANDLEByReference p0, final WinNT.HANDLE p1);
    
    boolean OpenProcessToken(final WinNT.HANDLE p0, final int p1, final WinNT.HANDLEByReference p2);
    
    boolean DuplicateToken(final WinNT.HANDLE p0, final int p1, final WinNT.HANDLEByReference p2);
    
    boolean DuplicateTokenEx(final WinNT.HANDLE p0, final int p1, final WinBase.SECURITY_ATTRIBUTES p2, final int p3, final int p4, final WinNT.HANDLEByReference p5);
    
    boolean GetTokenInformation(final WinNT.HANDLE p0, final int p1, final Structure p2, final int p3, final IntByReference p4);
    
    boolean ImpersonateLoggedOnUser(final WinNT.HANDLE p0);
    
    boolean ImpersonateSelf(final int p0);
    
    boolean RevertToSelf();
    
    int RegOpenKeyEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final WinReg.HKEYByReference p4);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final char[] p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final byte[] p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final IntByReference p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final LongByReference p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final Pointer p4, final IntByReference p5);
    
    int RegCloseKey(final WinReg.HKEY p0);
    
    int RegDeleteValue(final WinReg.HKEY p0, final String p1);
    
    int RegSetValueEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final char[] p4, final int p5);
    
    int RegSetValueEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final byte[] p4, final int p5);
    
    int RegCreateKeyEx(final WinReg.HKEY p0, final String p1, final int p2, final String p3, final int p4, final int p5, final WinBase.SECURITY_ATTRIBUTES p6, final WinReg.HKEYByReference p7, final IntByReference p8);
    
    int RegDeleteKey(final WinReg.HKEY p0, final String p1);
    
    int RegEnumKeyEx(final WinReg.HKEY p0, final int p1, final char[] p2, final IntByReference p3, final IntByReference p4, final char[] p5, final IntByReference p6, final WinBase.FILETIME p7);
    
    int RegEnumValue(final WinReg.HKEY p0, final int p1, final char[] p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final byte[] p6, final IntByReference p7);
    
    int RegQueryInfoKey(final WinReg.HKEY p0, final char[] p1, final IntByReference p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final IntByReference p6, final IntByReference p7, final IntByReference p8, final IntByReference p9, final IntByReference p10, final WinBase.FILETIME p11);
    
    int RegGetValue(final WinReg.HKEY p0, final String p1, final String p2, final int p3, final IntByReference p4, final byte[] p5, final IntByReference p6);
    
    WinNT.HANDLE RegisterEventSource(final String p0, final String p1);
    
    boolean DeregisterEventSource(final WinNT.HANDLE p0);
    
    WinNT.HANDLE OpenEventLog(final String p0, final String p1);
    
    boolean CloseEventLog(final WinNT.HANDLE p0);
    
    boolean GetNumberOfEventLogRecords(final WinNT.HANDLE p0, final IntByReference p1);
    
    boolean ReportEvent(final WinNT.HANDLE p0, final int p1, final int p2, final int p3, final WinNT.PSID p4, final int p5, final int p6, final String[] p7, final Pointer p8);
    
    boolean ClearEventLog(final WinNT.HANDLE p0, final String p1);
    
    boolean BackupEventLog(final WinNT.HANDLE p0, final String p1);
    
    WinNT.HANDLE OpenBackupEventLog(final String p0, final String p1);
    
    boolean ReadEventLog(final WinNT.HANDLE p0, final int p1, final int p2, final Pointer p3, final int p4, final IntByReference p5, final IntByReference p6);
    
    boolean GetOldestEventLogRecord(final WinNT.HANDLE p0, final IntByReference p1);
    
    boolean QueryServiceStatusEx(final Winsvc.SC_HANDLE p0, final int p1, final Winsvc.SERVICE_STATUS_PROCESS p2, final int p3, final IntByReference p4);
    
    boolean ControlService(final Winsvc.SC_HANDLE p0, final int p1, final Winsvc.SERVICE_STATUS p2);
    
    boolean StartService(final Winsvc.SC_HANDLE p0, final int p1, final String[] p2);
    
    boolean CloseServiceHandle(final Winsvc.SC_HANDLE p0);
    
    Winsvc.SC_HANDLE OpenService(final Winsvc.SC_HANDLE p0, final String p1, final int p2);
    
    Winsvc.SC_HANDLE OpenSCManager(final String p0, final String p1, final int p2);
    
    boolean CreateProcessAsUser(final WinNT.HANDLE p0, final String p1, final String p2, final WinBase.SECURITY_ATTRIBUTES p3, final WinBase.SECURITY_ATTRIBUTES p4, final boolean p5, final int p6, final String p7, final String p8, final WinBase.STARTUPINFO p9, final WinBase.PROCESS_INFORMATION p10);
    
    boolean AdjustTokenPrivileges(final WinNT.HANDLE p0, final boolean p1, final WinNT.TOKEN_PRIVILEGES p2, final int p3, final WinNT.TOKEN_PRIVILEGES p4, final IntByReference p5);
    
    boolean LookupPrivilegeName(final String p0, final WinNT.LUID p1, final char[] p2, final IntByReference p3);
    
    boolean LookupPrivilegeValue(final String p0, final String p1, final WinNT.LUID p2);
    
    boolean GetFileSecurity(final WString p0, final int p1, final Pointer p2, final int p3, final IntByReference p4);
    
    int GetNamedSecurityInfo(final String p0, final int p1, final int p2, final PointerByReference p3, final PointerByReference p4, final PointerByReference p5, final PointerByReference p6, final PointerByReference p7);
    
    int SetNamedSecurityInfo(final String p0, final int p1, final int p2, final Pointer p3, final Pointer p4, final Pointer p5, final Pointer p6);
    
    int GetSecurityDescriptorLength(final Pointer p0);
    
    boolean IsValidSecurityDescriptor(final Pointer p0);
    
    boolean IsValidAcl(final Pointer p0);
    
    void MapGenericMask(final WinDef.DWORDByReference p0, final WinNT.GENERIC_MAPPING p1);
    
    boolean AccessCheck(final Pointer p0, final WinNT.HANDLE p1, final WinDef.DWORD p2, final WinNT.GENERIC_MAPPING p3, final WinNT.PRIVILEGE_SET p4, final WinDef.DWORDByReference p5, final WinDef.DWORDByReference p6, final WinDef.BOOLByReference p7);
    
    boolean EncryptFile(final WString p0);
    
    boolean DecryptFile(final WString p0, final WinDef.DWORD p1);
    
    boolean FileEncryptionStatus(final WString p0, final WinDef.DWORDByReference p1);
    
    boolean EncryptionDisable(final WString p0, final boolean p1);
    
    int OpenEncryptedFileRaw(final WString p0, final WinDef.ULONG p1, final PointerByReference p2);
    
    int ReadEncryptedFileRaw(final WinBase.FE_EXPORT_FUNC p0, final Pointer p1, final Pointer p2);
    
    int WriteEncryptedFileRaw(final WinBase.FE_IMPORT_FUNC p0, final Pointer p1, final Pointer p2);
    
    void CloseEncryptedFileRaw(final Pointer p0);
}
