package com.overwolf.natives;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.*;

public final class CUser32 {
	   public static final CUser32 INSTANCE;

	   public static final native boolean GetClientRect(HWND var0, RECT var1);

	   public static final native int GetCursorPos(POINT var0);

	   public static final native HWND FindWindowA(String var0, String var1);

	   public static final native long GetForegroundWindow();

	   public static final native boolean GetWindowRect(HWND var0, RECT var1);

	   public static final native void mouse_event(int var0, int var1, int var2, int var3, long var4);

	   private CUser32() {
	   }

	   static {
	      CUser32 var0 = new CUser32();
	      INSTANCE = var0;
	      Native.register("user32");
	   }
	}
