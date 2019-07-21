@file:JvmName("ButterCake")
package com.ButterCake

import javax.script.ScriptEngineManager
import java.io.File
import java.io.FileReader
import com.charlatano.settings.*;
import com.ButterCake.overlay.Overlay
import com.ButterCake.game.EFT
import java.util.Scanner

const val SETTINGS_DIRECTORY = "settings"

fun main(args: Array<String>){
	loadSettings()
	
	EFT.initialize()
	
	//esp()
	//flatAim()
	//pathAim()
	
	val scanner = Scanner(System.`in`)
	while (!Thread.interrupted()) {
		when (scanner.nextLine()) {
			"exit", "quit" -> System.exit(0)
			"reload" -> loadSettings()
		}
	}
}

private fun loadSettings() {	
	/*with(ScriptEngineManager().getEngineByExtension("kts")) {
		File(SETTINGS_DIRECTORY).listFiles().forEach {
			FileReader(it).use {
				val code = it.readLines().joinToString("\n")
				eval(code)
			}
		}
	}*/
	
	val needsOverlay = ENABLE_ESP and (SKELETON_ESP or BOX_ESP)
	if (!Overlay.opened && needsOverlay) Overlay.open()
}