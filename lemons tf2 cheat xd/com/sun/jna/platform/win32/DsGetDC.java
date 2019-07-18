// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface DsGetDC extends StdCallLibrary
{
    public static final int DS_DOMAIN_IN_FOREST = 1;
    public static final int DS_DOMAIN_DIRECT_OUTBOUND = 2;
    public static final int DS_DOMAIN_TREE_ROOT = 4;
    public static final int DS_DOMAIN_PRIMARY = 8;
    public static final int DS_DOMAIN_NATIVE_MODE = 16;
    public static final int DS_DOMAIN_DIRECT_INBOUND = 32;
    public static final int DS_DOMAIN_VALID_FLAGS = 63;
    
    public static class DOMAIN_CONTROLLER_INFO extends Structure
    {
        public WString DomainControllerName;
        public WString DomainControllerAddress;
        public int DomainControllerAddressType;
        public Guid.GUID DomainGuid;
        public WString DomainName;
        public WString DnsForestName;
        public int Flags;
        public WString DcSiteName;
        public WString ClientSiteName;
        
        public DOMAIN_CONTROLLER_INFO() {
        }
        
        public DOMAIN_CONTROLLER_INFO(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("DomainControllerName", "DomainControllerAddress", "DomainControllerAddressType", "DomainGuid", "DomainName", "DnsForestName", "Flags", "DcSiteName", "ClientSiteName");
        }
        
        public static class ByReference extends DOMAIN_CONTROLLER_INFO implements Structure.ByReference
        {
        }
    }
    
    public static class PDOMAIN_CONTROLLER_INFO extends Structure
    {
        public DOMAIN_CONTROLLER_INFO.ByReference dci;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("dci");
        }
        
        public static class ByReference extends PDOMAIN_CONTROLLER_INFO implements Structure.ByReference
        {
        }
    }
    
    public static class DS_DOMAIN_TRUSTS extends Structure
    {
        public WString NetbiosDomainName;
        public WString DnsDomainName;
        public int Flags;
        public int ParentIndex;
        public int TrustType;
        public int TrustAttributes;
        public WinNT.PSID.ByReference DomainSid;
        public Guid.GUID DomainGuid;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("NetbiosDomainName", "DnsDomainName", "Flags", "ParentIndex", "TrustType", "TrustAttributes", "DomainSid", "DomainGuid");
        }
        
        public DS_DOMAIN_TRUSTS() {
        }
        
        public DS_DOMAIN_TRUSTS(final Pointer p) {
            super(p);
            this.read();
        }
        
        public static class ByReference extends DS_DOMAIN_TRUSTS implements Structure.ByReference
        {
        }
    }
}
