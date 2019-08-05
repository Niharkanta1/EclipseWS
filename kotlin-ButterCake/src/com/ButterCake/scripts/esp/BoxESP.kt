package com.buttercake.scripts.esp

import com.buttercake.utils.Vector
import com.buttercake.overlay.ButtercakeOverlay
import com.charlatano.settings.BOX_ESP

private val vHead = Vector()
private val vFeet = Vector()

private val vTop = Vector(0.0, 0.0, 0.0)
private val vBot = Vector(0.0, 0.0, 0.0)

private val boxes = Array(128) { Box() }

private var currentIdx = 0

internal fun boxEsp() = ButtercakeOverlay {
	if (!BOX_ESP) return@ButtercakeOverlay

}