// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;

public interface PhysicalMonitorEnumerationAPI
{
    public static final int PHYSICAL_MONITOR_DESCRIPTION_SIZE = 128;
    
    public static class PHYSICAL_MONITOR extends Structure
    {
        public WinNT.HANDLE hPhysicalMonitor;
        public char[] szPhysicalMonitorDescription;
        
        public PHYSICAL_MONITOR() {
            this.szPhysicalMonitorDescription = new char[128];
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hPhysicalMonitor", "szPhysicalMonitorDescription");
        }
    }
}
