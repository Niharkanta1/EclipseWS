// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.tf2;

import me.lemon.lemonware.utils.vector.Vector3f;
import me.lemon.lemonware.Main;

public class LocalPlayer extends Entity
{
    private long crosshair;
    
    public LocalPlayer(final long adress, final long index) {
        super(adress, index);
    }
    
    @Override
    public void update() {
        super.update();
        this.crosshair = Main.INSTANCE.getTF2Process().readUnsignedInt(this.getAddress() + Main.INSTANCE.getMemory().getOffset("m_iCrosshairID").getAddress());
    }
    
    public Vector3f normalizeAngle(final Vector3f angle) {
        if (angle.y > 89.0f && angle.x <= 180.0f) {
            angle.y = 89.0f;
        }
        if (angle.x > 180.0f) {
            angle.x -= 360.0f;
        }
        if (angle.y < -89.0f) {
            angle.y = -89.0f;
        }
        if (angle.x > 180.0f) {
            angle.x -= 360.0f;
        }
        if (angle.x < -180.0f) {
            angle.x += 360.0f;
        }
        if (angle.z != 0.0f) {
            angle.z = 0.0f;
        }
        return angle;
    }
    
    public boolean isCrosshairOverPlayer() {
        return this.crosshair > 0L && this.crosshair < 64L;
    }
    
    public Vector3f setViewAngles(final Vector3f vec) {
        this.normalizeAngle(vec);
        Main.INSTANCE.getEngine().writeFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress(), vec.getY());
        Main.INSTANCE.getEngine().writeFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress() + 4, vec.getX());
        return vec;
    }
    
    public Vector3f setViewAnglesNoClamp(final Vector3f vec) {
        Main.INSTANCE.getEngine().writeFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress(), vec.getY());
        Main.INSTANCE.getEngine().writeFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress() + 4, vec.getX());
        Main.INSTANCE.getEngine().writeFloat(Main.INSTANCE.getMemory().getOffset("m_dwViewAngles").getAddress() + 8, vec.getZ());
        return vec;
    }
    
    public long getCrosshair() {
        return this.crosshair;
    }
}
