package com.overwolf.modules;

import com.overwolf.Engine;
import com.overwolf.offsets.Offsets;
import com.overwolf.util.DrawUtils;

public class CrosshairDot extends Module {

	private final int sx = (int) (Engine.glwindow.getHeight() * 0.5f);
	private final int sy = (int) (Engine.glwindow.getWidth() * 0.5f);

	@Override
	public void onUIRender() {
		/*
		 * if (Offsets.m_dwLocalPlayer == 0 || !this.isToggled()) return;
		 */
		DrawUtils.setColor(0, 0, 0, 150);
		DrawUtils.fillRectanglew(sx - 2, sy - 2, 20, 20);
		DrawUtils.setColor(0x00FFFFFF);
		DrawUtils.fillRectanglew(sx - 1, sy - 1,10, 10);
	}

}
