// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Structure;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.Library;

public interface SystemB extends Library
{
    public static final SystemB INSTANCE = (SystemB)Native.loadLibrary("System", SystemB.class);
    public static final int HOST_LOAD_INFO = 1;
    public static final int HOST_VM_INFO = 2;
    public static final int HOST_CPU_LOAD_INFO = 3;
    public static final int HOST_VM_INFO64 = 4;
    public static final int CPU_STATE_MAX = 4;
    public static final int CPU_STATE_USER = 0;
    public static final int CPU_STATE_SYSTEM = 1;
    public static final int CPU_STATE_IDLE = 2;
    public static final int CPU_STATE_NICE = 3;
    public static final int PROCESSOR_BASIC_INFO = 1;
    public static final int PROCESSOR_CPU_LOAD_INFO = 2;
    public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
    public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
    
    int mach_host_self();
    
    int host_page_size(final int p0, final LongByReference p1);
    
    int host_statistics(final int p0, final int p1, final Structure p2, final IntByReference p3);
    
    int host_statistics64(final int p0, final int p1, final Structure p2, final IntByReference p3);
    
    int sysctl(final int[] p0, final int p1, final Pointer p2, final IntByReference p3, final Pointer p4, final int p5);
    
    int sysctlbyname(final String p0, final Pointer p1, final IntByReference p2, final Pointer p3, final int p4);
    
    int sysctlnametomib(final String p0, final Pointer p1, final IntByReference p2);
    
    int host_processor_info(final int p0, final int p1, final IntByReference p2, final PointerByReference p3, final IntByReference p4);
    
    public static class HostCpuLoadInfo extends Structure
    {
        public int[] cpu_ticks;
        
        public HostCpuLoadInfo() {
            this.cpu_ticks = new int[4];
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("cpu_ticks");
        }
    }
    
    public static class HostLoadInfo extends Structure
    {
        public int[] avenrun;
        public int[] mach_factor;
        
        public HostLoadInfo() {
            this.avenrun = new int[3];
            this.mach_factor = new int[3];
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("avenrun", "mach_factor");
        }
    }
    
    public static class VMStatistics extends Structure
    {
        public int free_count;
        public int active_count;
        public int inactive_count;
        public int wire_count;
        public int zero_fill_count;
        public int reactivations;
        public int pageins;
        public int pageouts;
        public int faults;
        public int cow_faults;
        public int lookups;
        public int hits;
        public int purgeable_count;
        public int purges;
        public int speculative_count;
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("free_count", "active_count", "inactive_count", "wire_count", "zero_fill_count", "reactivations", "pageins", "pageouts", "faults", "cow_faults", "lookups", "hits", "purgeable_count", "purges", "speculative_count");
        }
    }
    
    public static class VMStatistics64 extends Structure
    {
        public int free_count;
        public int active_count;
        public int inactive_count;
        public int wire_count;
        public long zero_fill_count;
        public long reactivations;
        public long pageins;
        public long pageouts;
        public long faults;
        public long cow_faults;
        public long lookups;
        public long hits;
        public long purges;
        public int purgeable_count;
        public int speculative_count;
        public long decompressions;
        public long compressions;
        public long swapins;
        public long swapouts;
        public int compressor_page_count;
        public int throttled_count;
        public int external_page_count;
        public int internal_page_count;
        public long total_uncompressed_pages_in_compressor;
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("free_count", "active_count", "inactive_count", "wire_count", "zero_fill_count", "reactivations", "pageins", "pageouts", "faults", "cow_faults", "lookups", "hits", "purges", "purgeable_count", "speculative_count", "decompressions", "compressions", "swapins", "swapouts", "compressor_page_count", "throttled_count", "external_page_count", "internal_page_count", "total_uncompressed_pages_in_compressor");
        }
    }
}
