package com.overwolf;

import java.io.IOException;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;
import com.overwolf.util.MemoryUtils;

public final class Engine {

	private static Process process, localprocess = Processes.byId(MemoryUtils.getPID());
	private static final int TARGET_TPS = 200;
	private long tps_sleep = (long) ((1f / TARGET_TPS) * 1000);
	private long last_tick = 0;
	
	public static MemoryBuffer entlistbuffer = new MemoryBuffer(Long.BYTES * 4 * 65);
	public static long tick = 0;
	public static int isInGame = 0;
	public static String[] cmdargs;

	public void init(String[] args) throws InterruptedException, IOException {
		System.out.println(localprocess.id());
		String processName = "notepad.exe";		
		waitUntilFound("process", () -> (process = Processes.byName(processName)) != null);
		System.out.println(process.id());
	}
	
	private void waitUntilFound(String message, Clause clause) {
		while (!clause.get())
			try {
				Thread.sleep(1000);
				System.out.print(".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	@FunctionalInterface
	private interface Clause {
		boolean get();
	}

}
