package com.overwolf.util;

import com.github.jonatino.misc.MemoryBuffer;
import com.overwolf.structs.VectorMem;

public class LocalPlayerPosition {
	
	public VectorMem lpvec = new VectorMem();
	private MemoryBuffer lpvecbuf = new MemoryBuffer(lpvec.size());
	private float pos[] = new float[3], viewoffset[] = new float[3], vieworigin[] = new float[3];
	private float pitch, yaw, origyaw;
	public int fov, defaultfov;
	private float[] viewangles = new float[3], viewpunch = new float[3], aimpunch = new float[3];

	public LocalPlayerPosition() {
		lpvec.setSource(lpvecbuf);
	}
	
	public void updateData() {
		System.out.println("updating local player data");
	}
	

	public float getX() {
		return this.pos[0];
	}

	public float getY() {
		return this.pos[1];
	}

	public float getZ() {
		return this.pos[2];
	}

	public int getFOV() {
		if (fov == 0)
			return defaultfov;
		return fov;
	}

	public float[] getViewOrigin() {
		return this.vieworigin;
	}

	public float[] getViewOffset() {
		return this.viewoffset;
	}

	public float[] getOrigin() {
		return this.pos;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float[] getViewAngles() {
		return this.viewangles;
	}
	
	public float[] getViewPunch() {
		return this.viewpunch;
	}
	
	public float[] getAimPunch() {
		return this.aimpunch;
	}

}
