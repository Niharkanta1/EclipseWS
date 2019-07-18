package com.overwolf.util;

public class MemoryUtils {
	
	public static int getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Integer.parseInt(processName.split("@")[0]);
	}
}
