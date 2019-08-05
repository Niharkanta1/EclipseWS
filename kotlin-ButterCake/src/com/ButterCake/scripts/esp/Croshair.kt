package com.buttercake.scripts.esp

import com.badlogic.gdx.graphics.Color
import com.buttercake.overlay.ButtercakeOverlay

private val box = Box()

internal fun croshair() = ButtercakeOverlay{
	
	box.apply {
		x = 800;
		y = 450;
		w = 2;
		h = 2;
	}
	
	shapeRenderer.apply sR@ {
		begin()
		box.apply {
			this@sR.color = color
			rect(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
		}	
		end()
	}
}