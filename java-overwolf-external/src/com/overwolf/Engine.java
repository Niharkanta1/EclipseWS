package com.overwolf;

import java.io.IOException;

import org.jire.arrowhead.Process;
import org.jire.arrowhead.ProcessBy;

import com.overwolf.natives.CUser32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.overwolf.JOGL2Renderer;
import com.overwolf.util.DrawUtils;

public final class Engine {

	private static Process process;
	private static final int TARGET_TPS = 200;
	private long tps_sleep = (long) ((1f / TARGET_TPS) * 1000);
	private long last_tick = 0;
	public static long tick = 0;
	public static int isInGame = 0;
	public static String[] cmdargs;
	public static boolean isOverlayOpen = false;

	public void init(String[] args) throws InterruptedException, IOException {
		if (DrawUtils.enableOverlay) {
			setupWindow(new JOGL2Renderer());
			isOverlayOpen = true;
		}
		String processName = "notepad.exe";		
		waitUntilFound("process", () -> (process = ProcessBy.processByName(processName)) != null);
		RECT rect = new WinDef.RECT();
		HWND hwd = CUser32.FindWindowA(null, "Untitled - Notepad");
		CUser32.GetClientRect(hwd, rect);
		int gameWidth = rect.right - rect.left;
		int gameHeight = rect.bottom - rect.top;
		int gameX = rect.left + (((rect.right - rect.left) - gameWidth) / 2);
		int gameY = rect.top + ((rect.bottom - rect.top) - gameHeight);
		
		/*
		 * if (isOverlayOpen && (lastX != gameX || lastY != gameY))
		 * CUser32.INSTANCE.MoveWindow(Overlay.hwnd, gameX, gameY, gameWidth,
		 * gameHeight, false);
		 */
		
		
		
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

	private void setupWindow(GLEventListener renderer) {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setBackgroundOpaque(false);
        GLCanvas canvas = new GLCanvas(caps);
        
        
		
		Window win = new Window(window.getWindowHandle());
		window.setAlwaysOnTop(true);
		window.setUndecorated(true);

		final FPSAnimator animator = new FPSAnimator(window, 60, true);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop();
						System.exit(0);
					}
				}.start();
			}
		});

		window.addGLEventListener(renderer);
		window.setSize(1600, 900);
		window.setPosition(1600, 0);
		window.setTitle("Overwolf");
		window.setVisible(true);
		// window.setAlwaysOnTop(true);
		animator.start();
		
		System.out.println("started animator");
	}
}
