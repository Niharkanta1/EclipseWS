// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.WString;
import com.sun.jna.Memory;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface Sspi extends StdCallLibrary
{
    public static final int MAX_TOKEN_SIZE = 12288;
    public static final int SECPKG_CRED_INBOUND = 1;
    public static final int SECPKG_CRED_OUTBOUND = 2;
    public static final int SECURITY_NATIVE_DREP = 16;
    public static final int ISC_REQ_ALLOCATE_MEMORY = 256;
    public static final int ISC_REQ_CONFIDENTIALITY = 16;
    public static final int ISC_REQ_CONNECTION = 2048;
    public static final int ISC_REQ_DELEGATE = 1;
    public static final int ISC_REQ_EXTENDED_ERROR = 16384;
    public static final int ISC_REQ_INTEGRITY = 65536;
    public static final int ISC_REQ_MUTUAL_AUTH = 2;
    public static final int ISC_REQ_REPLAY_DETECT = 4;
    public static final int ISC_REQ_SEQUENCE_DETECT = 8;
    public static final int ISC_REQ_STREAM = 32768;
    public static final int SECBUFFER_VERSION = 0;
    public static final int SECBUFFER_EMPTY = 0;
    public static final int SECBUFFER_DATA = 1;
    public static final int SECBUFFER_TOKEN = 2;
    
    public static class SecHandle extends Structure
    {
        public Pointer dwLower;
        public Pointer dwUpper;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("dwLower", "dwUpper");
        }
        
        public boolean isNull() {
            return this.dwLower == null && this.dwUpper == null;
        }
        
        public static class ByReference extends SecHandle implements Structure.ByReference
        {
        }
    }
    
    public static class PSecHandle extends Structure
    {
        public SecHandle.ByReference secHandle;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("secHandle");
        }
        
        public PSecHandle() {
        }
        
        public PSecHandle(final SecHandle h) {
            super(h.getPointer());
            this.read();
        }
        
        public static class ByReference extends PSecHandle implements Structure.ByReference
        {
        }
    }
    
    public static class CredHandle extends SecHandle
    {
    }
    
    public static class CtxtHandle extends SecHandle
    {
    }
    
    public static class SecBuffer extends Structure
    {
        public int cbBuffer;
        public int BufferType;
        public Pointer pvBuffer;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("cbBuffer", "BufferType", "pvBuffer");
        }
        
        public SecBuffer() {
            this.BufferType = 0;
        }
        
        public SecBuffer(final int type, final int size) {
            this.BufferType = 0;
            this.cbBuffer = size;
            this.pvBuffer = new Memory(size);
            this.BufferType = type;
        }
        
        public SecBuffer(final int type, final byte[] token) {
            this.BufferType = 0;
            this.cbBuffer = token.length;
            (this.pvBuffer = new Memory(token.length)).write(0L, token, 0, token.length);
            this.BufferType = type;
        }
        
        public byte[] getBytes() {
            return (byte[])((this.pvBuffer == null) ? null : this.pvBuffer.getByteArray(0L, this.cbBuffer));
        }
        
        public static class ByReference extends SecBuffer implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final int type, final int size) {
                super(type, size);
            }
            
            public ByReference(final int type, final byte[] token) {
                super(type, token);
            }
        }
    }
    
    public static class SecBufferDesc extends Structure
    {
        public int ulVersion;
        public int cBuffers;
        public SecBuffer.ByReference[] pBuffers;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("ulVersion", "cBuffers", "pBuffers");
        }
        
        public SecBufferDesc() {
            this.ulVersion = 0;
            this.cBuffers = 1;
            this.pBuffers = new SecBuffer.ByReference[] { new SecBuffer.ByReference() };
        }
        
        public SecBufferDesc(final int type, final byte[] token) {
            this.ulVersion = 0;
            this.cBuffers = 1;
            (this.pBuffers = new SecBuffer.ByReference[] { new SecBuffer.ByReference() })[0] = new SecBuffer.ByReference(type, token);
        }
        
        public SecBufferDesc(final int type, final int tokenSize) {
            this.ulVersion = 0;
            this.cBuffers = 1;
            (this.pBuffers = new SecBuffer.ByReference[] { new SecBuffer.ByReference() })[0] = new SecBuffer.ByReference(type, tokenSize);
        }
        
        public byte[] getBytes() {
            return this.pBuffers[0].getBytes();
        }
    }
    
    public static class SECURITY_INTEGER extends Structure
    {
        public int dwLower;
        public int dwUpper;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("dwLower", "dwUpper");
        }
    }
    
    public static class TimeStamp extends SECURITY_INTEGER
    {
    }
    
    public static class PSecPkgInfo extends Structure
    {
        public SecPkgInfo.ByReference pPkgInfo;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("pPkgInfo");
        }
        
        @Override
        public SecPkgInfo.ByReference[] toArray(final int size) {
            return (SecPkgInfo.ByReference[])this.pPkgInfo.toArray(size);
        }
        
        public static class ByReference extends PSecPkgInfo implements Structure.ByReference
        {
        }
    }
    
    public static class SecPkgInfo extends Structure
    {
        public int fCapabilities;
        public short wVersion;
        public short wRPCID;
        public int cbMaxToken;
        public WString Name;
        public WString Comment;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("fCapabilities", "wVersion", "wRPCID", "cbMaxToken", "Name", "Comment");
        }
        
        public SecPkgInfo() {
            this.wVersion = 1;
        }
        
        public static class ByReference extends SecPkgInfo implements Structure.ByReference
        {
        }
    }
}
