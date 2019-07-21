package com.buttercake.overlay

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.buttercake.game.EFT.gameWidth
import com.buttercake.game.EFT.gameHeight
import com.buttercake.game.EFT.gameX
import com.buttercake.game.EFT.gameY
import com.buttercake.utils.randLong
import com.charlatano.settings.*
import com.buttercake.overlay.transparency.TransparencyApplier
import com.buttercake.overlay.transparency.win10.Win10TransparencyApplier

object Overlay {
	@Volatile var opened = false
	
	lateinit var hwnd: WinDef.HWND
	
	fun open() = LwjglApplicationConfiguration().apply {
		width = gameWidth
		height = gameHeight
		title = randLong(Long.MAX_VALUE).toString()
		x = gameX
		y = gameY
		resizable = false
		fullscreen = false
		vSyncEnabled = OPENGL_VSYNC
		if (OPENGL_MSAA_SAMPLES > 0)
			samples = OPENGL_MSAA_SAMPLES
		
		foregroundFPS = OPENGL_FPS
		backgroundFPS = OPENGL_FPS
		
		LwjglApplication(ButtercakeOverlay, this)
		
		do {
			val hwnd = User32.INSTANCE.FindWindow(null, title)
			if (hwnd != null) {
				Overlay.hwnd = hwnd
				break
			}
			Thread.sleep(64) // decreased so it won't go black as long
		} while (!Thread.interrupted())
		
		// sets up window to be fullscreen, click-through, etc.
		WindowCorrector.setupWindow(hwnd)
		
		
		// sets up the full transparency of the Window (only Windows 7 and 10 can do this)
		val transparencyApplier: TransparencyApplier = Win10TransparencyApplier
		
		//OR  Win7TransparencyApplier // will only work on Windows 7 or early Windows 10 builds		
		transparencyApplier.applyTransparency(hwnd)
		
		opened = true
		println("Overlay initiated....")
	}
	
	init {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
	}
	
}