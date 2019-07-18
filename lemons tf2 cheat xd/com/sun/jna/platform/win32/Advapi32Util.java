// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.io.IOException;
import com.sun.jna.ptr.ByteByReference;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import com.sun.jna.WString;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import com.sun.jna.ptr.LongByReference;
import java.util.ArrayList;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public abstract class Advapi32Util
{
    public static String getUserName() {
        char[] buffer = new char[128];
        final IntByReference len = new IntByReference(buffer.length);
        boolean result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
        if (!result) {
            switch (Kernel32.INSTANCE.GetLastError()) {
                case 122: {
                    buffer = new char[len.getValue()];
                    result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
                    break;
                }
                default: {
                    throw new Win32Exception(Native.getLastError());
                }
            }
        }
        if (!result) {
            throw new Win32Exception(Native.getLastError());
        }
        return Native.toString(buffer);
    }
    
    public static Account getAccountByName(final String accountName) {
        return getAccountByName(null, accountName);
    }
    
    public static Account getAccountByName(final String systemName, final String accountName) {
        final IntByReference pSid = new IntByReference(0);
        final IntByReference cchDomainName = new IntByReference(0);
        final PointerByReference peUse = new PointerByReference();
        if (Advapi32.INSTANCE.LookupAccountName(systemName, accountName, null, pSid, null, cchDomainName, peUse)) {
            throw new RuntimeException("LookupAccountNameW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        final int rc = Kernel32.INSTANCE.GetLastError();
        if (pSid.getValue() == 0 || rc != 122) {
            throw new Win32Exception(rc);
        }
        final Memory sidMemory = new Memory(pSid.getValue());
        final WinNT.PSID result = new WinNT.PSID(sidMemory);
        final char[] referencedDomainName = new char[cchDomainName.getValue() + 1];
        if (!Advapi32.INSTANCE.LookupAccountName(systemName, accountName, result, pSid, referencedDomainName, cchDomainName, peUse)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final Account account = new Account();
        account.accountType = peUse.getPointer().getInt(0L);
        account.name = accountName;
        final String[] accountNamePartsBs = accountName.split("\\\\", 2);
        final String[] accountNamePartsAt = accountName.split("@", 2);
        if (accountNamePartsBs.length == 2) {
            account.name = accountNamePartsBs[1];
        }
        else if (accountNamePartsAt.length == 2) {
            account.name = accountNamePartsAt[0];
        }
        else {
            account.name = accountName;
        }
        if (cchDomainName.getValue() > 0) {
            account.domain = Native.toString(referencedDomainName);
            account.fqn = account.domain + "\\" + account.name;
        }
        else {
            account.fqn = account.name;
        }
        account.sid = result.getBytes();
        account.sidString = convertSidToStringSid(new WinNT.PSID(account.sid));
        return account;
    }
    
    public static Account getAccountBySid(final WinNT.PSID sid) {
        return getAccountBySid(null, sid);
    }
    
    public static Account getAccountBySid(final String systemName, final WinNT.PSID sid) {
        final IntByReference cchName = new IntByReference();
        final IntByReference cchDomainName = new IntByReference();
        final PointerByReference peUse = new PointerByReference();
        if (Advapi32.INSTANCE.LookupAccountSid(null, sid, null, cchName, null, cchDomainName, peUse)) {
            throw new RuntimeException("LookupAccountSidW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        final int rc = Kernel32.INSTANCE.GetLastError();
        if (cchName.getValue() == 0 || rc != 122) {
            throw new Win32Exception(rc);
        }
        final char[] domainName = new char[cchDomainName.getValue()];
        final char[] name = new char[cchName.getValue()];
        if (!Advapi32.INSTANCE.LookupAccountSid(null, sid, name, cchName, domainName, cchDomainName, peUse)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final Account account = new Account();
        account.accountType = peUse.getPointer().getInt(0L);
        account.name = Native.toString(name);
        if (cchDomainName.getValue() > 0) {
            account.domain = Native.toString(domainName);
            account.fqn = account.domain + "\\" + account.name;
        }
        else {
            account.fqn = account.name;
        }
        account.sid = sid.getBytes();
        account.sidString = convertSidToStringSid(sid);
        return account;
    }
    
    public static String convertSidToStringSid(final WinNT.PSID sid) {
        final PointerByReference stringSid = new PointerByReference();
        if (!Advapi32.INSTANCE.ConvertSidToStringSid(sid, stringSid)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final String result = stringSid.getValue().getWideString(0L);
        Kernel32.INSTANCE.LocalFree(stringSid.getValue());
        return result;
    }
    
    public static byte[] convertStringSidToSid(final String sidString) {
        final WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
        if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return pSID.getValue().getBytes();
    }
    
    public static boolean isWellKnownSid(final String sidString, final int wellKnownSidType) {
        final WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
        if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Advapi32.INSTANCE.IsWellKnownSid(pSID.getValue(), wellKnownSidType);
    }
    
    public static boolean isWellKnownSid(final byte[] sidBytes, final int wellKnownSidType) {
        final WinNT.PSID pSID = new WinNT.PSID(sidBytes);
        return Advapi32.INSTANCE.IsWellKnownSid(pSID, wellKnownSidType);
    }
    
    public static Account getAccountBySid(final String sidString) {
        return getAccountBySid(null, sidString);
    }
    
    public static Account getAccountBySid(final String systemName, final String sidString) {
        return getAccountBySid(systemName, new WinNT.PSID(convertStringSidToSid(sidString)));
    }
    
    public static Account[] getTokenGroups(final WinNT.HANDLE hToken) {
        final IntByReference tokenInformationLength = new IntByReference();
        if (Advapi32.INSTANCE.GetTokenInformation(hToken, 2, null, 0, tokenInformationLength)) {
            throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        final int rc = Kernel32.INSTANCE.GetLastError();
        if (rc != 122) {
            throw new Win32Exception(rc);
        }
        final WinNT.TOKEN_GROUPS groups = new WinNT.TOKEN_GROUPS(tokenInformationLength.getValue());
        if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 2, groups, tokenInformationLength.getValue(), tokenInformationLength)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final ArrayList<Account> userGroups = new ArrayList<Account>();
        for (final WinNT.SID_AND_ATTRIBUTES sidAndAttribute : groups.getGroups()) {
            Account group = null;
            try {
                group = getAccountBySid(sidAndAttribute.Sid);
            }
            catch (Exception e) {
                group = new Account();
                group.sid = sidAndAttribute.Sid.getBytes();
                group.sidString = convertSidToStringSid(sidAndAttribute.Sid);
                group.name = group.sidString;
                group.fqn = group.sidString;
                group.accountType = 2;
            }
            userGroups.add(group);
        }
        return userGroups.toArray(new Account[0]);
    }
    
    public static Account getTokenAccount(final WinNT.HANDLE hToken) {
        final IntByReference tokenInformationLength = new IntByReference();
        if (Advapi32.INSTANCE.GetTokenInformation(hToken, 1, null, 0, tokenInformationLength)) {
            throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        final int rc = Kernel32.INSTANCE.GetLastError();
        if (rc != 122) {
            throw new Win32Exception(rc);
        }
        final WinNT.TOKEN_USER user = new WinNT.TOKEN_USER(tokenInformationLength.getValue());
        if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 1, user, tokenInformationLength.getValue(), tokenInformationLength)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return getAccountBySid(user.User.Sid);
    }
    
    public static Account[] getCurrentUserGroups() {
        final WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
        try {
            final WinNT.HANDLE threadHandle = Kernel32.INSTANCE.GetCurrentThread();
            if (!Advapi32.INSTANCE.OpenThreadToken(threadHandle, 10, true, phToken)) {
                if (1008 != Kernel32.INSTANCE.GetLastError()) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                final WinNT.HANDLE processHandle = Kernel32.INSTANCE.GetCurrentProcess();
                if (!Advapi32.INSTANCE.OpenProcessToken(processHandle, 10, phToken)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
            }
            return getTokenGroups(phToken.getValue());
        }
        finally {
            if (phToken.getValue() != WinBase.INVALID_HANDLE_VALUE && !Kernel32.INSTANCE.CloseHandle(phToken.getValue())) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }
    
    public static boolean registryKeyExists(final WinReg.HKEY root, final String key) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        final int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        switch (rc) {
            case 0: {
                Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
                return true;
            }
            case 2: {
                return false;
            }
            default: {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static boolean registryValueExists(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        try {
            switch (rc) {
                case 0: {
                    final IntByReference lpcbData = new IntByReference();
                    final IntByReference lpType = new IntByReference();
                    rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
                    switch (rc) {
                        case 0:
                        case 122:
                        case 234: {
                            return true;
                        }
                        case 2: {
                            return false;
                        }
                        default: {
                            throw new Win32Exception(rc);
                        }
                    }
                    break;
                }
                case 2: {
                    return false;
                }
                default: {
                    throw new Win32Exception(rc);
                }
            }
        }
        finally {
            if (phkKey.getValue() != WinBase.INVALID_HANDLE_VALUE) {
                rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
                if (rc != 0) {
                    throw new Win32Exception(rc);
                }
            }
        }
    }
    
    public static String registryGetStringValue(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetStringValue(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static String registryGetStringValue(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 1 && lpType.getValue() != 2) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ or REG_EXPAND_SZ");
        }
        final char[] data = new char[lpcbData.getValue()];
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        return Native.toString(data);
    }
    
    public static String registryGetExpandableStringValue(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetExpandableStringValue(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static String registryGetExpandableStringValue(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 2) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
        }
        final char[] data = new char[lpcbData.getValue()];
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        return Native.toString(data);
    }
    
    public static String[] registryGetStringArray(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetStringArray(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static String[] registryGetStringArray(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 7) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
        }
        final Memory data = new Memory(lpcbData.getValue());
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        final ArrayList<String> result = new ArrayList<String>();
        int offset = 0;
        while (offset < data.size()) {
            final String s = data.getWideString(offset);
            offset += s.length() * Native.WCHAR_SIZE;
            offset += Native.WCHAR_SIZE;
            if (s.length() == 0 && offset == data.size()) {
                continue;
            }
            result.add(s);
        }
        return result.toArray(new String[0]);
    }
    
    public static byte[] registryGetBinaryValue(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetBinaryValue(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static byte[] registryGetBinaryValue(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 3) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_BINARY");
        }
        final byte[] data = new byte[lpcbData.getValue()];
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        return data;
    }
    
    public static int registryGetIntValue(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetIntValue(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static int registryGetIntValue(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 4) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_DWORD");
        }
        final IntByReference data = new IntByReference();
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        return data.getValue();
    }
    
    public static long registryGetLongValue(final WinReg.HKEY root, final String key, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetLongValue(phkKey.getValue(), value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static long registryGetLongValue(final WinReg.HKEY hKey, final String value) {
        final IntByReference lpcbData = new IntByReference();
        final IntByReference lpType = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != 11) {
            throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_QWORD");
        }
        final LongByReference data = new LongByReference();
        rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        return data.getValue();
    }
    
    public static Object registryGetValue(final WinReg.HKEY hkKey, final String subKey, final String lpValueName) {
        Object result = null;
        final IntByReference lpType = new IntByReference();
        final byte[] lpData = new byte[16383];
        final IntByReference lpcbData = new IntByReference(16383);
        final int rc = Advapi32.INSTANCE.RegGetValue(hkKey, subKey, lpValueName, 65535, lpType, lpData, lpcbData);
        if (lpType.getValue() == 0) {
            return null;
        }
        if (rc != 0 && rc != 122) {
            throw new Win32Exception(rc);
        }
        final Memory byteData = new Memory(lpcbData.getValue());
        byteData.write(0L, lpData, 0, lpcbData.getValue());
        if (lpType.getValue() == 4) {
            result = new Integer(byteData.getInt(0L));
        }
        else if (lpType.getValue() == 11) {
            result = new Long(byteData.getLong(0L));
        }
        else if (lpType.getValue() == 3) {
            result = byteData.getByteArray(0L, lpcbData.getValue());
        }
        else if (lpType.getValue() == 1 || lpType.getValue() == 2) {
            result = byteData.getWideString(0L);
        }
        return result;
    }
    
    public static boolean registryCreateKey(final WinReg.HKEY hKey, final String keyName) {
        final WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
        final IntByReference lpdwDisposition = new IntByReference();
        int rc = Advapi32.INSTANCE.RegCreateKeyEx(hKey, keyName, 0, null, 0, 131097, null, phkResult, lpdwDisposition);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        rc = Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        return 1 == lpdwDisposition.getValue();
    }
    
    public static boolean registryCreateKey(final WinReg.HKEY root, final String parentPath, final String keyName) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, parentPath, 0, 4, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryCreateKey(phkKey.getValue(), keyName);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetIntValue(final WinReg.HKEY hKey, final String name, final int value) {
        final byte[] data = { (byte)(value & 0xFF), (byte)(value >> 8 & 0xFF), (byte)(value >> 16 & 0xFF), (byte)(value >> 24 & 0xFF) };
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 4, data, 4);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetIntValue(final WinReg.HKEY root, final String keyPath, final String name, final int value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetIntValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetLongValue(final WinReg.HKEY hKey, final String name, final long value) {
        final byte[] data = { (byte)(value & 0xFFL), (byte)(value >> 8 & 0xFFL), (byte)(value >> 16 & 0xFFL), (byte)(value >> 24 & 0xFFL), (byte)(value >> 32 & 0xFFL), (byte)(value >> 40 & 0xFFL), (byte)(value >> 48 & 0xFFL), (byte)(value >> 56 & 0xFFL) };
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 11, data, 8);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetLongValue(final WinReg.HKEY root, final String keyPath, final String name, final long value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetLongValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetStringValue(final WinReg.HKEY hKey, final String name, final String value) {
        final char[] data = Native.toCharArray(value);
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 1, data, data.length * Native.WCHAR_SIZE);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetStringValue(final WinReg.HKEY root, final String keyPath, final String name, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetStringValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetExpandableStringValue(final WinReg.HKEY hKey, final String name, final String value) {
        final char[] data = Native.toCharArray(value);
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 2, data, data.length * Native.WCHAR_SIZE);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetExpandableStringValue(final WinReg.HKEY root, final String keyPath, final String name, final String value) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetExpandableStringValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetStringArray(final WinReg.HKEY hKey, final String name, final String[] arr) {
        int size = 0;
        for (final String s : arr) {
            size += s.length() * Native.WCHAR_SIZE;
            size += Native.WCHAR_SIZE;
        }
        size += Native.WCHAR_SIZE;
        int offset = 0;
        final Memory data = new Memory(size);
        for (final String s2 : arr) {
            data.setWideString(offset, s2);
            offset += s2.length() * Native.WCHAR_SIZE;
            offset += Native.WCHAR_SIZE;
        }
        for (int i = 0; i < Native.WCHAR_SIZE; ++i) {
            data.setByte(offset++, (byte)0);
        }
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 7, data.getByteArray(0L, size), size);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetStringArray(final WinReg.HKEY root, final String keyPath, final String name, final String[] arr) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetStringArray(phkKey.getValue(), name, arr);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registrySetBinaryValue(final WinReg.HKEY hKey, final String name, final byte[] data) {
        final int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 3, data, data.length);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registrySetBinaryValue(final WinReg.HKEY root, final String keyPath, final String name, final byte[] data) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetBinaryValue(phkKey.getValue(), name, data);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registryDeleteKey(final WinReg.HKEY hKey, final String keyName) {
        final int rc = Advapi32.INSTANCE.RegDeleteKey(hKey, keyName);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registryDeleteKey(final WinReg.HKEY root, final String keyPath, final String keyName) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registryDeleteKey(phkKey.getValue(), keyName);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static void registryDeleteValue(final WinReg.HKEY hKey, final String valueName) {
        final int rc = Advapi32.INSTANCE.RegDeleteValue(hKey, valueName);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static void registryDeleteValue(final WinReg.HKEY root, final String keyPath, final String valueName) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            registryDeleteValue(phkKey.getValue(), valueName);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static String[] registryGetKeys(final WinReg.HKEY hKey) {
        final IntByReference lpcSubKeys = new IntByReference();
        final IntByReference lpcMaxSubKeyLen = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, lpcSubKeys, lpcMaxSubKeyLen, null, null, null, null, null, null);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        final ArrayList<String> keys = new ArrayList<String>(lpcSubKeys.getValue());
        final char[] name = new char[lpcMaxSubKeyLen.getValue() + 1];
        for (int i = 0; i < lpcSubKeys.getValue(); ++i) {
            final IntByReference lpcchValueName = new IntByReference(lpcMaxSubKeyLen.getValue() + 1);
            rc = Advapi32.INSTANCE.RegEnumKeyEx(hKey, i, name, lpcchValueName, null, null, null, null);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            keys.add(Native.toString(name));
        }
        return keys.toArray(new String[0]);
    }
    
    public static String[] registryGetKeys(final WinReg.HKEY root, final String keyPath) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetKeys(phkKey.getValue());
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static WinReg.HKEYByReference registryGetKey(final WinReg.HKEY root, final String keyPath, final int samDesired) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        final int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, samDesired, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        return phkKey;
    }
    
    public static void registryCloseKey(final WinReg.HKEY hKey) {
        final int rc = Advapi32.INSTANCE.RegCloseKey(hKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }
    
    public static TreeMap<String, Object> registryGetValues(final WinReg.HKEY hKey) {
        final IntByReference lpcValues = new IntByReference();
        final IntByReference lpcMaxValueNameLen = new IntByReference();
        final IntByReference lpcMaxValueLen = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, null, null, null, lpcValues, lpcMaxValueNameLen, lpcMaxValueLen, null, null);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        final TreeMap<String, Object> keyValues = new TreeMap<String, Object>();
        final char[] name = new char[lpcMaxValueNameLen.getValue() + 1];
        final byte[] data = new byte[lpcMaxValueLen.getValue()];
        for (int i = 0; i < lpcValues.getValue(); ++i) {
            final IntByReference lpcchValueName = new IntByReference(lpcMaxValueNameLen.getValue() + 1);
            final IntByReference lpcbData = new IntByReference(lpcMaxValueLen.getValue());
            final IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegEnumValue(hKey, i, name, lpcchValueName, null, lpType, data, lpcbData);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            final String nameString = Native.toString(name);
            if (lpcbData.getValue() == 0) {
                switch (lpType.getValue()) {
                    case 3: {
                        keyValues.put(nameString, new byte[0]);
                        break;
                    }
                    case 1:
                    case 2: {
                        keyValues.put(nameString, new char[0]);
                        break;
                    }
                    case 7: {
                        keyValues.put(nameString, new String[0]);
                        break;
                    }
                    case 0: {
                        keyValues.put(nameString, null);
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unsupported empty type: " + lpType.getValue());
                    }
                }
            }
            else {
                final Memory byteData = new Memory(lpcbData.getValue());
                byteData.write(0L, data, 0, lpcbData.getValue());
                switch (lpType.getValue()) {
                    case 11: {
                        keyValues.put(nameString, byteData.getLong(0L));
                        break;
                    }
                    case 4: {
                        keyValues.put(nameString, byteData.getInt(0L));
                        break;
                    }
                    case 1:
                    case 2: {
                        keyValues.put(nameString, byteData.getWideString(0L));
                        break;
                    }
                    case 3: {
                        keyValues.put(nameString, byteData.getByteArray(0L, lpcbData.getValue()));
                        break;
                    }
                    case 7: {
                        final Memory stringData = new Memory(lpcbData.getValue());
                        stringData.write(0L, data, 0, lpcbData.getValue());
                        final ArrayList<String> result = new ArrayList<String>();
                        int offset = 0;
                        while (offset < stringData.size()) {
                            final String s = stringData.getWideString(offset);
                            offset += s.length() * Native.WCHAR_SIZE;
                            offset += Native.WCHAR_SIZE;
                            if (s.length() == 0 && offset == stringData.size()) {
                                continue;
                            }
                            result.add(s);
                        }
                        keyValues.put(nameString, result.toArray(new String[0]));
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unsupported type: " + lpType.getValue());
                    }
                }
            }
        }
        return keyValues;
    }
    
    public static TreeMap<String, Object> registryGetValues(final WinReg.HKEY root, final String keyPath) {
        final WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetValues(phkKey.getValue());
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static InfoKey registryQueryInfoKey(final WinReg.HKEY hKey, final int lpcbSecurityDescriptor) {
        final InfoKey infoKey = new InfoKey(hKey, lpcbSecurityDescriptor);
        final int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, infoKey.lpClass, infoKey.lpcClass, null, infoKey.lpcSubKeys, infoKey.lpcMaxSubKeyLen, infoKey.lpcMaxClassLen, infoKey.lpcValues, infoKey.lpcMaxValueNameLen, infoKey.lpcMaxValueLen, infoKey.lpcbSecurityDescriptor, infoKey.lpftLastWriteTime);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        return infoKey;
    }
    
    public static EnumKey registryRegEnumKey(final WinReg.HKEY hKey, final int dwIndex) {
        final EnumKey enumKey = new EnumKey(hKey, dwIndex);
        final int rc = Advapi32.INSTANCE.RegEnumKeyEx(hKey, enumKey.dwIndex, enumKey.lpName, enumKey.lpcName, null, enumKey.lpClass, enumKey.lpcbClass, enumKey.lpftLastWriteTime);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        return enumKey;
    }
    
    public static String getEnvironmentBlock(final Map<String, String> environment) {
        final StringBuilder out = new StringBuilder(environment.size() * 32);
        for (final Map.Entry<String, String> entry : environment.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            if (value != null) {
                out.append(key).append("=").append(value).append('\0');
            }
        }
        return out.append('\0').toString();
    }
    
    public static WinNT.ACCESS_ACEStructure[] getFileSecurity(final String fileName, final boolean compact) {
        final int infoType = 4;
        int nLength = 1024;
        boolean repeat = false;
        Memory memory = null;
        do {
            repeat = false;
            memory = new Memory(nLength);
            final IntByReference lpnSize = new IntByReference();
            final boolean succeded = Advapi32.INSTANCE.GetFileSecurity(new WString(fileName), infoType, memory, nLength, lpnSize);
            if (!succeded) {
                final int lastError = Kernel32.INSTANCE.GetLastError();
                memory.clear();
                if (122 != lastError) {
                    throw new Win32Exception(lastError);
                }
            }
            final int lengthNeeded = lpnSize.getValue();
            if (nLength < lengthNeeded) {
                repeat = true;
                nLength = lengthNeeded;
                memory.clear();
            }
        } while (repeat);
        final WinNT.SECURITY_DESCRIPTOR_RELATIVE sdr = new WinNT.SECURITY_DESCRIPTOR_RELATIVE(memory);
        memory.clear();
        final WinNT.ACL dacl = sdr.getDiscretionaryACL();
        final WinNT.ACCESS_ACEStructure[] aceStructures = dacl.getACEStructures();
        if (compact) {
            final Map<String, WinNT.ACCESS_ACEStructure> aceMap = new HashMap<String, WinNT.ACCESS_ACEStructure>();
            for (final WinNT.ACCESS_ACEStructure aceStructure : aceStructures) {
                final boolean inherted = (aceStructure.AceFlags & 0x1F) != 0x0;
                final String key = aceStructure.getSidString() + "/" + inherted + "/" + aceStructure.getClass().getName();
                final WinNT.ACCESS_ACEStructure aceStructure2 = aceMap.get(key);
                if (aceStructure2 != null) {
                    int accessMask = aceStructure2.Mask;
                    accessMask |= aceStructure.Mask;
                    aceStructure2.Mask = accessMask;
                }
                else {
                    aceMap.put(key, aceStructure);
                }
            }
            return aceMap.values().toArray(new WinNT.ACCESS_ACEStructure[aceMap.size()]);
        }
        return aceStructures;
    }
    
    private static Memory getSecurityDescriptorForFile(final String absoluteFilePath) {
        final int infoType = 7;
        final IntByReference lpnSize = new IntByReference();
        boolean succeeded = Advapi32.INSTANCE.GetFileSecurity(new WString(absoluteFilePath), 7, null, 0, lpnSize);
        if (!succeeded) {
            final int lastError = Kernel32.INSTANCE.GetLastError();
            if (122 != lastError) {
                throw new Win32Exception(lastError);
            }
        }
        final int nLength = lpnSize.getValue();
        final Memory securityDescriptorMemoryPointer = new Memory(nLength);
        succeeded = Advapi32.INSTANCE.GetFileSecurity(new WString(absoluteFilePath), 7, securityDescriptorMemoryPointer, nLength, lpnSize);
        if (!succeeded) {
            securityDescriptorMemoryPointer.clear();
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return securityDescriptorMemoryPointer;
    }
    
    public static Memory getSecurityDescriptorForObject(final String absoluteObjectPath, final int objectType, final boolean getSACL) {
        final int infoType = 0x7 | (getSACL ? 8 : 0);
        final PointerByReference ppSecurityDescriptor = new PointerByReference();
        final int lastError = Advapi32.INSTANCE.GetNamedSecurityInfo(absoluteObjectPath, objectType, infoType, null, null, null, null, ppSecurityDescriptor);
        if (lastError != 0) {
            throw new Win32Exception(lastError);
        }
        final int nLength = Advapi32.INSTANCE.GetSecurityDescriptorLength(ppSecurityDescriptor.getValue());
        final Memory memory = new Memory(nLength);
        memory.write(0L, ppSecurityDescriptor.getValue().getByteArray(0L, nLength), 0, nLength);
        Kernel32.INSTANCE.LocalFree(ppSecurityDescriptor.getValue());
        return memory;
    }
    
    public static void setSecurityDescriptorForObject(final String absoluteObjectPath, final int objectType, final WinNT.SECURITY_DESCRIPTOR_RELATIVE securityDescriptor, final boolean setOwner, final boolean setGroup, final boolean setDACL, final boolean setSACL, final boolean setDACLProtectedStatus, final boolean setSACLProtectedStatus) {
        final WinNT.PSID psidOwner = securityDescriptor.getOwner();
        final WinNT.PSID psidGroup = securityDescriptor.getGroup();
        final WinNT.ACL dacl = securityDescriptor.getDiscretionaryACL();
        final WinNT.ACL sacl = securityDescriptor.getSystemACL();
        int infoType = 0;
        if (setOwner) {
            if (psidOwner == null) {
                throw new IllegalArgumentException("SECURITY_DESCRIPTOR_RELATIVE does not contain owner");
            }
            if (!Advapi32.INSTANCE.IsValidSid(psidOwner)) {
                throw new IllegalArgumentException("Owner PSID is invalid");
            }
            infoType |= 0x1;
        }
        if (setGroup) {
            if (psidGroup == null) {
                throw new IllegalArgumentException("SECURITY_DESCRIPTOR_RELATIVE does not contain group");
            }
            if (!Advapi32.INSTANCE.IsValidSid(psidGroup)) {
                throw new IllegalArgumentException("Group PSID is invalid");
            }
            infoType |= 0x2;
        }
        if (setDACL) {
            if (dacl == null) {
                throw new IllegalArgumentException("SECURITY_DESCRIPTOR_RELATIVE does not contain DACL");
            }
            if (!Advapi32.INSTANCE.IsValidAcl(dacl.getPointer())) {
                throw new IllegalArgumentException("DACL is invalid");
            }
            infoType |= 0x4;
        }
        if (setSACL) {
            if (sacl == null) {
                throw new IllegalArgumentException("SECURITY_DESCRIPTOR_RELATIVE does not contain SACL");
            }
            if (!Advapi32.INSTANCE.IsValidAcl(sacl.getPointer())) {
                throw new IllegalArgumentException("SACL is invalid");
            }
            infoType |= 0x8;
        }
        if (setDACLProtectedStatus) {
            if ((securityDescriptor.Control & 0x1000) != 0x0) {
                infoType |= Integer.MIN_VALUE;
            }
            else if ((securityDescriptor.Control & 0x1000) == 0x0) {
                infoType |= 0x20000000;
            }
        }
        if (setSACLProtectedStatus) {
            if ((securityDescriptor.Control & 0x2000) != 0x0) {
                infoType |= 0x40000000;
            }
            else if ((securityDescriptor.Control & 0x2000) == 0x0) {
                infoType |= 0x10000000;
            }
        }
        final int lastError = Advapi32.INSTANCE.SetNamedSecurityInfo(absoluteObjectPath, objectType, infoType, setOwner ? psidOwner.getPointer() : null, setGroup ? psidGroup.getPointer() : null, setDACL ? dacl.getPointer() : null, setSACL ? sacl.getPointer() : null);
        if (lastError != 0) {
            throw new Win32Exception(lastError);
        }
    }
    
    public static boolean accessCheck(final File file, final AccessCheckPermission permissionToCheck) {
        boolean hasAccess = false;
        final Memory securityDescriptorMemoryPointer = getSecurityDescriptorForFile(file.getAbsolutePath().replaceAll("/", "\\"));
        WinNT.HANDLEByReference openedAccessToken = null;
        final WinNT.HANDLEByReference duplicatedToken = new WinNT.HANDLEByReference();
        try {
            openedAccessToken = new WinNT.HANDLEByReference();
            final int desireAccess = 131086;
            if (!Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 131086, openedAccessToken)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            if (!Advapi32.INSTANCE.DuplicateToken(openedAccessToken.getValue(), 2, duplicatedToken)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            final WinNT.GENERIC_MAPPING mapping = new WinNT.GENERIC_MAPPING();
            mapping.genericRead = new WinDef.DWORD(1179785L);
            mapping.genericWrite = new WinDef.DWORD(1179926L);
            mapping.genericExecute = new WinDef.DWORD(1179808L);
            mapping.genericAll = new WinDef.DWORD(2032127L);
            final WinDef.DWORDByReference rights = new WinDef.DWORDByReference(new WinDef.DWORD((long)permissionToCheck.getCode()));
            Advapi32.INSTANCE.MapGenericMask(rights, mapping);
            final WinNT.PRIVILEGE_SET privileges = new WinNT.PRIVILEGE_SET(1);
            privileges.PrivilegeCount = new WinDef.DWORD(0L);
            final WinDef.DWORDByReference privilegeLength = new WinDef.DWORDByReference(new WinDef.DWORD((long)privileges.size()));
            final WinDef.DWORDByReference grantedAccess = new WinDef.DWORDByReference();
            final WinDef.BOOLByReference result = new WinDef.BOOLByReference();
            if (!Advapi32.INSTANCE.AccessCheck(securityDescriptorMemoryPointer, duplicatedToken.getValue(), rights.getValue(), mapping, privileges, privilegeLength, grantedAccess, result)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            hasAccess = result.getValue().booleanValue();
        }
        finally {
            if (openedAccessToken != null && openedAccessToken.getValue() != null) {
                Kernel32.INSTANCE.CloseHandle(openedAccessToken.getValue());
            }
            if (duplicatedToken != null && duplicatedToken.getValue() != null) {
                Kernel32.INSTANCE.CloseHandle(duplicatedToken.getValue());
            }
            if (securityDescriptorMemoryPointer != null) {
                securityDescriptorMemoryPointer.clear();
            }
        }
        return hasAccess;
    }
    
    public static WinNT.SECURITY_DESCRIPTOR_RELATIVE getFileSecurityDescriptor(final File file, final boolean getSACL) {
        WinNT.SECURITY_DESCRIPTOR_RELATIVE sdr = null;
        final Memory securityDesc = getSecurityDescriptorForObject(file.getAbsolutePath().replaceAll("/", "\\"), 1, getSACL);
        sdr = new WinNT.SECURITY_DESCRIPTOR_RELATIVE(securityDesc);
        return sdr;
    }
    
    public static void setFileSecurityDescriptor(final File file, final WinNT.SECURITY_DESCRIPTOR_RELATIVE securityDescriptor, final boolean setOwner, final boolean setGroup, final boolean setDACL, final boolean setSACL, final boolean setDACLProtectedStatus, final boolean setSACLProtectedStatus) {
        setSecurityDescriptorForObject(file.getAbsolutePath().replaceAll("/", "\\"), 1, securityDescriptor, setOwner, setGroup, setDACL, setSACL, setDACLProtectedStatus, setSACLProtectedStatus);
    }
    
    public static void encryptFile(final File file) {
        final WString lpFileName = new WString(file.getAbsolutePath());
        if (!Advapi32.INSTANCE.EncryptFile(lpFileName)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static void decryptFile(final File file) {
        final WString lpFileName = new WString(file.getAbsolutePath());
        if (!Advapi32.INSTANCE.DecryptFile(lpFileName, new WinDef.DWORD(0L))) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static int fileEncryptionStatus(final File file) {
        final WinDef.DWORDByReference status = new WinDef.DWORDByReference();
        final WString lpFileName = new WString(file.getAbsolutePath());
        if (!Advapi32.INSTANCE.FileEncryptionStatus(lpFileName, status)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return status.getValue().intValue();
    }
    
    public static void disableEncryption(final File directory, final boolean disable) {
        final WString dirPath = new WString(directory.getAbsolutePath());
        if (!Advapi32.INSTANCE.EncryptionDisable(dirPath, disable)) {
            throw new Win32Exception(Native.getLastError());
        }
    }
    
    public static void backupEncryptedFile(final File src, final File destDir) {
        if (!destDir.isDirectory()) {
            throw new IllegalArgumentException("destDir must be a directory.");
        }
        final WinDef.ULONG readFlag = new WinDef.ULONG(0L);
        final WinDef.ULONG writeFlag = new WinDef.ULONG(1L);
        if (src.isDirectory()) {
            writeFlag.setValue(3L);
        }
        final WString srcFileName = new WString(src.getAbsolutePath());
        PointerByReference pvContext = new PointerByReference();
        if (Advapi32.INSTANCE.OpenEncryptedFileRaw(srcFileName, readFlag, pvContext) != 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final WinBase.FE_EXPORT_FUNC pfExportCallback = new WinBase.FE_EXPORT_FUNC() {
            @Override
            public WinDef.DWORD callback(final ByteByReference pbData, final Pointer pvCallbackContext, final WinDef.ULONG ulLength) {
                final byte[] arr = pbData.getPointer().getByteArray(0L, ulLength.intValue());
                try {
                    outputStream.write(arr);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new WinDef.DWORD(0L);
            }
        };
        if (Advapi32.INSTANCE.ReadEncryptedFileRaw(pfExportCallback, null, pvContext.getValue()) != 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        try {
            outputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Advapi32.INSTANCE.CloseEncryptedFileRaw(pvContext.getValue());
        final WString destFileName = new WString(destDir.getAbsolutePath() + File.separator + src.getName());
        pvContext = new PointerByReference();
        if (Advapi32.INSTANCE.OpenEncryptedFileRaw(destFileName, writeFlag, pvContext) != 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final IntByReference elementsReadWrapper = new IntByReference(0);
        final WinBase.FE_IMPORT_FUNC pfImportCallback = new WinBase.FE_IMPORT_FUNC() {
            @Override
            public WinDef.DWORD callback(final ByteByReference pbData, final Pointer pvCallbackContext, final WinDef.ULONGByReference ulLength) {
                final int elementsRead = elementsReadWrapper.getValue();
                final int remainingElements = outputStream.size() - elementsRead;
                final int length = Math.min(remainingElements, ulLength.getValue().intValue());
                pbData.getPointer().write(0L, outputStream.toByteArray(), elementsRead, length);
                elementsReadWrapper.setValue(elementsRead + length);
                ulLength.setValue(new WinDef.ULONG((long)length));
                return new WinDef.DWORD(0L);
            }
        };
        if (Advapi32.INSTANCE.WriteEncryptedFileRaw(pfImportCallback, null, pvContext.getValue()) != 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Advapi32.INSTANCE.CloseEncryptedFileRaw(pvContext.getValue());
    }
    
    public static class Account
    {
        public String name;
        public String domain;
        public byte[] sid;
        public String sidString;
        public int accountType;
        public String fqn;
    }
    
    public static class InfoKey
    {
        public WinReg.HKEY hKey;
        public char[] lpClass;
        public IntByReference lpcClass;
        public IntByReference lpcSubKeys;
        public IntByReference lpcMaxSubKeyLen;
        public IntByReference lpcMaxClassLen;
        public IntByReference lpcValues;
        public IntByReference lpcMaxValueNameLen;
        public IntByReference lpcMaxValueLen;
        public IntByReference lpcbSecurityDescriptor;
        public WinBase.FILETIME lpftLastWriteTime;
        
        public InfoKey() {
            this.lpClass = new char[260];
            this.lpcClass = new IntByReference(260);
            this.lpcSubKeys = new IntByReference();
            this.lpcMaxSubKeyLen = new IntByReference();
            this.lpcMaxClassLen = new IntByReference();
            this.lpcValues = new IntByReference();
            this.lpcMaxValueNameLen = new IntByReference();
            this.lpcMaxValueLen = new IntByReference();
            this.lpcbSecurityDescriptor = new IntByReference();
            this.lpftLastWriteTime = new WinBase.FILETIME();
        }
        
        public InfoKey(final WinReg.HKEY hKey, final int securityDescriptor) {
            this.lpClass = new char[260];
            this.lpcClass = new IntByReference(260);
            this.lpcSubKeys = new IntByReference();
            this.lpcMaxSubKeyLen = new IntByReference();
            this.lpcMaxClassLen = new IntByReference();
            this.lpcValues = new IntByReference();
            this.lpcMaxValueNameLen = new IntByReference();
            this.lpcMaxValueLen = new IntByReference();
            this.lpcbSecurityDescriptor = new IntByReference();
            this.lpftLastWriteTime = new WinBase.FILETIME();
            this.hKey = hKey;
            this.lpcbSecurityDescriptor = new IntByReference(securityDescriptor);
        }
    }
    
    public static class EnumKey
    {
        public WinReg.HKEY hKey;
        public int dwIndex;
        public char[] lpName;
        public IntByReference lpcName;
        public char[] lpClass;
        public IntByReference lpcbClass;
        public WinBase.FILETIME lpftLastWriteTime;
        
        public EnumKey() {
            this.dwIndex = 0;
            this.lpName = new char[255];
            this.lpcName = new IntByReference(255);
            this.lpClass = new char[255];
            this.lpcbClass = new IntByReference(255);
            this.lpftLastWriteTime = new WinBase.FILETIME();
        }
        
        public EnumKey(final WinReg.HKEY hKey, final int dwIndex) {
            this.dwIndex = 0;
            this.lpName = new char[255];
            this.lpcName = new IntByReference(255);
            this.lpClass = new char[255];
            this.lpcbClass = new IntByReference(255);
            this.lpftLastWriteTime = new WinBase.FILETIME();
            this.hKey = hKey;
            this.dwIndex = dwIndex;
        }
    }
    
    public enum EventLogType
    {
        Error, 
        Warning, 
        Informational, 
        AuditSuccess, 
        AuditFailure;
    }
    
    public static class EventLogRecord
    {
        private WinNT.EVENTLOGRECORD _record;
        private String _source;
        private byte[] _data;
        private String[] _strings;
        
        public WinNT.EVENTLOGRECORD getRecord() {
            return this._record;
        }
        
        public int getEventId() {
            return this._record.EventID.intValue();
        }
        
        public String getSource() {
            return this._source;
        }
        
        public int getStatusCode() {
            return this._record.EventID.intValue() & 0xFFFF;
        }
        
        public int getRecordNumber() {
            return this._record.RecordNumber.intValue();
        }
        
        public int getLength() {
            return this._record.Length.intValue();
        }
        
        public String[] getStrings() {
            return this._strings;
        }
        
        public EventLogType getType() {
            switch (this._record.EventType.intValue()) {
                case 0:
                case 4: {
                    return EventLogType.Informational;
                }
                case 16: {
                    return EventLogType.AuditFailure;
                }
                case 8: {
                    return EventLogType.AuditSuccess;
                }
                case 1: {
                    return EventLogType.Error;
                }
                case 2: {
                    return EventLogType.Warning;
                }
                default: {
                    throw new RuntimeException("Invalid type: " + this._record.EventType.intValue());
                }
            }
        }
        
        public byte[] getData() {
            return this._data;
        }
        
        public EventLogRecord(final Pointer pevlr) {
            this._record = null;
            this._record = new WinNT.EVENTLOGRECORD(pevlr);
            this._source = pevlr.getWideString(this._record.size());
            if (this._record.DataLength.intValue() > 0) {
                this._data = pevlr.getByteArray(this._record.DataOffset.intValue(), this._record.DataLength.intValue());
            }
            if (this._record.NumStrings.intValue() > 0) {
                final ArrayList<String> strings = new ArrayList<String>();
                int count = this._record.NumStrings.intValue();
                long offset = this._record.StringOffset.intValue();
                while (count > 0) {
                    final String s = pevlr.getWideString(offset);
                    strings.add(s);
                    offset += s.length() * Native.WCHAR_SIZE;
                    offset += Native.WCHAR_SIZE;
                    --count;
                }
                this._strings = strings.toArray(new String[0]);
            }
        }
    }
    
    public static class EventLogIterator implements Iterable<EventLogRecord>, Iterator<EventLogRecord>
    {
        private WinNT.HANDLE _h;
        private Memory _buffer;
        private boolean _done;
        private int _dwRead;
        private Pointer _pevlr;
        private int _flags;
        
        public EventLogIterator(final String sourceName) {
            this(null, sourceName, 4);
        }
        
        public EventLogIterator(final String serverName, final String sourceName, final int flags) {
            this._h = null;
            this._buffer = new Memory(65536L);
            this._done = false;
            this._dwRead = 0;
            this._pevlr = null;
            this._flags = 4;
            this._flags = flags;
            this._h = Advapi32.INSTANCE.OpenEventLog(serverName, sourceName);
            if (this._h == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
        
        private boolean read() {
            if (this._done || this._dwRead > 0) {
                return false;
            }
            final IntByReference pnBytesRead = new IntByReference();
            final IntByReference pnMinNumberOfBytesNeeded = new IntByReference();
            if (!Advapi32.INSTANCE.ReadEventLog(this._h, 0x1 | this._flags, 0, this._buffer, (int)this._buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded)) {
                final int rc = Kernel32.INSTANCE.GetLastError();
                if (rc == 122) {
                    this._buffer = new Memory(pnMinNumberOfBytesNeeded.getValue());
                    if (!Advapi32.INSTANCE.ReadEventLog(this._h, 0x1 | this._flags, 0, this._buffer, (int)this._buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded)) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                }
                else {
                    this.close();
                    if (rc != 38) {
                        throw new Win32Exception(rc);
                    }
                    return false;
                }
            }
            this._dwRead = pnBytesRead.getValue();
            this._pevlr = this._buffer;
            return true;
        }
        
        public void close() {
            this._done = true;
            if (this._h != null) {
                if (!Advapi32.INSTANCE.CloseEventLog(this._h)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                this._h = null;
            }
        }
        
        @Override
        public Iterator<EventLogRecord> iterator() {
            return this;
        }
        
        @Override
        public boolean hasNext() {
            this.read();
            return !this._done;
        }
        
        @Override
        public EventLogRecord next() {
            this.read();
            final EventLogRecord record = new EventLogRecord(this._pevlr);
            this._dwRead -= record.getLength();
            this._pevlr = this._pevlr.share(record.getLength());
            return record;
        }
        
        @Override
        public void remove() {
        }
    }
    
    public enum AccessCheckPermission
    {
        READ(Integer.MIN_VALUE), 
        WRITE(1073741824), 
        EXECUTE(536870912);
        
        final int code;
        
        private AccessCheckPermission(final int code) {
            this.code = code;
        }
        
        public int getCode() {
            return this.code;
        }
    }
}
