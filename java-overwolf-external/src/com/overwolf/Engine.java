package com.overwolf;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jire.arrowhead.Process;
import org.jire.arrowhead.ProcessBy;

import com.overwolf.natives.CUser32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
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
	public static int gameWidth;
	public static int gameHeight;
	public static int gameX;
	public static int gameY;
	
	public static int lastGameWidth;
	public static int lastGameHeight;
	public static int lastGameX;
	public static int lastGameY;
	public static GLWindow glwindow;
	
	public void init(String[] args) throws InterruptedException, IOException {
		if (DrawUtils.enableOverlay) {
			glwindow = setupWindow(new JOGL2Renderer());
			isOverlayOpen = true;
		}
		String processName = "notepad.exe";		
		waitUntilFound("process", () -> (process = ProcessBy.processByName(processName)) != null);
		RECT rect = new WinDef.RECT();
		HWND hwd = CUser32.FindWindowA(null, "Untitled - Notepad");
		Runnable helloRunnable = new Runnable() {
		    public void run() {
				CUser32.GetWindowRect(hwd, rect);
				gameWidth = rect.right - rect.left;
				gameHeight = rect.bottom - rect.top;
				gameX = rect.left + (((rect.right - rect.left) - gameWidth) / 2);
				gameY = rect.top + ((rect.bottom - rect.top) - gameHeight);
				
				if (isOverlayOpen && (lastGameX != gameX || lastGameY != gameY)){
					glwindow.setPosition(gameX, gameY);
					System.out.println("screen position updated...");
				}
				
				if (isOverlayOpen && (lastGameWidth != gameWidth || lastGameHeight != gameHeight)) {
					glwindow.setSize(gameWidth, gameHeight);
					System.out.println("screen size updated...");
				}
				
				lastGameWidth = gameWidth;
				lastGameHeight = gameHeight;
				lastGameX = gameX;
				lastGameY = gameY;
		    }
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 1, TimeUnit.SECONDS);
		
		Client.theClient.startClient();
		System.out.println(process.getId()+" "+gameHeight+" "+gameWidth);
		
		while (Client.theClient.isRunning) {
			try {
				Client.theClient.eventHandler.onPreLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			
			last_tick = System.nanoTime();

			try {
				Client.theClient.eventHandler.onLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			
			if (tps_sleep > 0)
				Thread.sleep(tps_sleep);

			double adjust = ((1f / TARGET_TPS) * 1e9) / (System.nanoTime() - last_tick);
			tps_sleep *= adjust;
			if (tps_sleep > ((1f / TARGET_TPS) * 1000))
				tps_sleep = (long) ((1f / TARGET_TPS) * 1000l);
			if (tps_sleep < 1)
				tps_sleep = 1;

			tick++;
		}
		Client.theClient.shutdownClient();
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

	private GLWindow setupWindow(GLEventListener renderer) {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setBackgroundOpaque(false);
		GLWindow window = GLWindow.create(caps);
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
		window.setSize(gameWidth, gameHeight);
		window.setPosition(gameX, gameY);
		window.setTitle("Overwolf");
		window.setVisible(true);
		animator.start();
		
		System.out.println("started animator");
		return window;
	}
}
