package com.buttercake.scripts.esp

import com.charlatano.settings.CROSSHAIR
import com.charlatano.settings.ENABLE_ESP
import com.charlatano.settings.BOX_ESP
import com.buttercake.scripts.esp.croshair
import com.buttercake.scripts.esp.boxEsp

fun esp() {
	if (!ENABLE_ESP) return
	
	if(CROSSHAIR) croshair()
	//if(BOX_ESP) boxEsp()
	 
}