package com.overwolf;

import java.io.IOException;

import org.jire.arrowhead.Process;
import org.jire.arrowhead.ProcessBy;

import com.overwolf.unative.CUser32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public final class Engine {

	private static Process process;
	private static final int TARGET_TPS = 200;
	private long tps_sleep = (long) ((1f / TARGET_TPS) * 1000);
	private long last_tick = 0;
	public static long tick = 0;
	public static int isInGame = 0;
	public static String[] cmdargs;

	public void init(String[] args) throws InterruptedException, IOException {
		String processName = "notepad.exe";		
		waitUntilFound("process", () -> (process = ProcessBy.processByName(processName)) != null);
		RECT rect = new WinDef.RECT();
		HWND hwd = CUser32.FindWindowA(null, "Untitled - Notepad");
		CUser32.GetClientRect(hwd, rect);
		int gameWidth = rect.right - rect.left;
		int gameHeight = rect.bottom - rect.top;
		
		System.out.println(process.getId()+" "+gameHeight+" "+gameWidth);
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
