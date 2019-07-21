package com.buttercake.game

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.jire.arrowhead.Process
import org.jire.arrowhead.processByName
import com.charlatano.settings.*
import com.buttercake.utils.retry
import com.buttercake.utils.natives.CUser32
import com.buttercake.utils.every
import com.buttercake.overlay.Overlay
import com.buttercake.overlay.ButtercakeOverlay.camera
import com.buttercake.overlay.ButtercakeOverlay
import com.buttercake.utils.inBackground

object EFT {
	lateinit var eftEXE: Process
		private set
	
	var gameHeight: Int = 0
		private set
	
	var gameX: Int = 0
		private set
	
	var gameWidth: Int = 0
		private set
	
	var gameY: Int = 0
		private set
	
	fun initialize() {
		println("Initializing EFT buttercake...");
		
		retry(20) {
			eftEXE = processByName(PROCESS_NAME, PROCESS_ACCESS_FLAGS)!!
			if(eftEXE!=null) println("process found "+ eftEXE.id);
		}
		
		/*retry(128) {
			eftEXE.loadModules()
		}*/
		
		val rect = WinDef.RECT()
		val hwd = CUser32.FindWindowA(null, WINDOW_NAME)
		
		var lastWidth = 0
		var lastHeight = 0
		var lastX = 0
		var lastY = 0
		
		every(1000) {
			if (!CUser32.GetClientRect(hwd, rect)) System.exit(2)

			gameWidth = rect.right - rect.left
			gameHeight = rect.bottom - rect.top
			if (!CUser32.GetWindowRect(hwd, rect)) System.exit(3)
			gameX = rect.left + (((rect.right - rect.left) - gameWidth) / 2)
			gameY = rect.top + ((rect.bottom - rect.top) - gameHeight)
			
			if (Overlay.opened && (lastX != gameX || lastY != gameY))
				User32.INSTANCE.MoveWindow(Overlay.hwnd, gameX, gameY, gameWidth, gameHeight, false)
			
			if (Overlay.opened && ButtercakeOverlay.created && (lastWidth != gameWidth || lastHeight != gameHeight))
				camera.setToOrtho(true, gameWidth.toFloat(), gameHeight.toFloat())
			
			lastWidth = gameWidth
			lastHeight = gameHeight
			lastX = gameX
			lastY = gameY
		}
		
		every(1024, continuous = true) {
			inBackground = Pointer.nativeValue(hwd.pointer) != CUser32.GetForegroundWindow()
		}
		
		//Memory.load();
		//constructEntities()
	}
	
}