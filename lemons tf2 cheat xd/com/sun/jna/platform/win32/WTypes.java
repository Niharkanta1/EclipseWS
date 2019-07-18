// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.ptr.ByReference;
import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public interface WTypes
{
    public static final int CLSCTX_INPROC_SERVER = 1;
    public static final int CLSCTX_INPROC_HANDLER = 2;
    public static final int CLSCTX_LOCAL_SERVER = 4;
    public static final int CLSCTX_INPROC_SERVER16 = 8;
    public static final int CLSCTX_REMOTE_SERVER = 16;
    public static final int CLSCTX_INPROC_HANDLER16 = 32;
    public static final int CLSCTX_RESERVED1 = 64;
    public static final int CLSCTX_RESERVED2 = 128;
    public static final int CLSCTX_RESERVED3 = 256;
    public static final int CLSCTX_RESERVED4 = 512;
    public static final int CLSCTX_NO_CODE_DOWNLOAD = 1024;
    public static final int CLSCTX_RESERVED5 = 2048;
    public static final int CLSCTX_NO_CUSTOM_MARSHAL = 4096;
    public static final int CLSCTX_ENABLE_CODE_DOWNLOAD = 8192;
    public static final int CLSCTX_NO_FAILURE_LOG = 16384;
    public static final int CLSCTX_DISABLE_AAA = 32768;
    public static final int CLSCTX_ENABLE_AAA = 65536;
    public static final int CLSCTX_FROM_DEFAULT_CONTEXT = 131072;
    public static final int CLSCTX_ACTIVATE_32_BIT_SERVER = 262144;
    public static final int CLSCTX_ACTIVATE_64_BIT_SERVER = 524288;
    public static final int CLSCTX_ENABLE_CLOAKING = 1048576;
    public static final int CLSCTX_APPCONTAINER = 4194304;
    public static final int CLSCTX_ACTIVATE_AAA_AS_IU = 8388608;
    public static final int CLSCTX_PS_DLL = Integer.MIN_VALUE;
    public static final int CLSCTX_SERVER = 21;
    public static final int CLSCTX_ALL = 7;
    
    public static class BSTR extends PointerType
    {
        public BSTR() {
            super(new Memory(Pointer.SIZE));
        }
        
        public BSTR(final Pointer pointer) {
            super(pointer);
        }
        
        public BSTR(final String value) {
            super(new Memory((value.length() + 1L) * Native.WCHAR_SIZE));
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.getPointer().setWideString(0L, value);
        }
        
        public String getValue() {
            final Pointer pointer = this.getPointer();
            String str = null;
            if (pointer != null) {
                str = pointer.getWideString(0L);
            }
            return str;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
        
        public static class ByReference extends BSTR implements Structure.ByReference
        {
        }
    }
    
    public static class BSTRByReference extends ByReference
    {
        public BSTRByReference() {
            super(Pointer.SIZE);
        }
        
        public BSTRByReference(final BSTR value) {
            this();
            this.setValue(value);
        }
        
        public void setValue(final BSTR value) {
            this.getPointer().setPointer(0L, value.getPointer());
        }
        
        public BSTR getValue() {
            return new BSTR(this.getPointer().getPointer(0L));
        }
        
        public String getString() {
            return this.getValue().getValue();
        }
    }
    
    public static class LPSTR extends PointerType
    {
        public LPSTR() {
            super(Pointer.NULL);
        }
        
        public LPSTR(final Pointer pointer) {
            super(pointer);
        }
        
        public LPSTR(final String value) {
            this(new Memory((value.length() + 1L) * Native.WCHAR_SIZE));
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.getPointer().setWideString(0L, value);
        }
        
        public String getValue() {
            final Pointer pointer = this.getPointer();
            String str = null;
            if (pointer != null) {
                str = pointer.getWideString(0L);
            }
            return str;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
        
        public static class ByReference extends BSTR implements Structure.ByReference
        {
        }
    }
    
    public static class LPWSTR extends PointerType
    {
        public LPWSTR() {
            super(Pointer.NULL);
        }
        
        public LPWSTR(final Pointer pointer) {
            super(pointer);
        }
        
        public LPWSTR(final String value) {
            this(new Memory((value.length() + 1L) * Native.WCHAR_SIZE));
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.getPointer().setWideString(0L, value);
        }
        
        public String getValue() {
            final Pointer pointer = this.getPointer();
            String str = null;
            if (pointer != null) {
                str = pointer.getWideString(0L);
            }
            return str;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
        
        public static class ByReference extends BSTR implements Structure.ByReference
        {
        }
    }
    
    public static class LPOLESTR extends PointerType
    {
        public LPOLESTR() {
            super(Pointer.NULL);
        }
        
        public LPOLESTR(final Pointer pointer) {
            super(pointer);
        }
        
        public LPOLESTR(final String value) {
            super(new Memory((value.length() + 1L) * Native.WCHAR_SIZE));
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.getPointer().setWideString(0L, value);
        }
        
        public String getValue() {
            final Pointer pointer = this.getPointer();
            String str = null;
            if (pointer != null) {
                str = pointer.getWideString(0L);
            }
            return str;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
        
        public static class ByReference extends BSTR implements Structure.ByReference
        {
        }
    }
    
    public static class VARTYPE extends WinDef.USHORT
    {
        public VARTYPE() {
            this(0);
        }
        
        public VARTYPE(final int value) {
            super((long)value);
        }
    }
}
