// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.feature.features;

import java.util.Iterator;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import me.lemon.lemonware.tf2.Entity;
import me.lemon.lemonware.Main;
import me.lemon.lemonware.utils.EnumBone;
import me.lemon.lemonware.tf2.LocalPlayer;
import java.awt.AWTException;
import me.lemon.lemonware.utils.vector.Vector3f;
import java.awt.Robot;
import me.lemon.lemonware.feature.Feature;

public class Aimbot extends Feature
{
    public boolean doAim;
    public boolean setupClick;
    public boolean snapBack;
    public Robot r;
    public Vector3f oldAngles;
    public int ticks;
    
    public Aimbot() {
        super("aimbot", FeatureType.AIMBOT);
        try {
            this.r = new Robot();
        }
        catch (AWTException ex) {}
    }
    
    @Override
    public void run(final LocalPlayer local) {
        ++this.ticks;
        if (this.snapBack && this.ticks > 30) {
            local.setViewAngles(this.oldAngles);
            this.snapBack = false;
        }
        else if (!this.snapBack && this.ticks > 30) {
            this.ticks = 0;
        }
        if (this.setupClick) {
            this.oldAngles = local.getViewAngles();
            final Entity target = this.getClosestToCrosshair(local);
            if (target != null) {
                int bone = ((EnumBone)Main.INSTANCE.getGui().bone.getSelectedItem()).getBoneID();
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(target.getAddress() + 5452L) == 4L) {
                    bone = 16;
                }
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(target.getAddress() + 5452L) == 9L) {
                    bone = 8;
                }
                final Vector3f angles = this.getRotations(local, target, local.getBonePos(target, bone));
                local.setViewAngles(angles);
                this.snapBack = true;
                this.ticks = 0;
            }
            this.setupClick = false;
        }
        if (this.doAim) {
            final Entity target = this.getClosestToCrosshair(local);
            if (target != null) {
                int bone = ((EnumBone)Main.INSTANCE.getGui().bone.getSelectedItem()).getBoneID();
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(target.getAddress() + 5452L) == 4L) {
                    bone = 16;
                }
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(target.getAddress() + 5452L) == 9L) {
                    bone = 8;
                }
                final Vector3f angles = this.getRotations(local, target, local.getBonePos(target, bone));
                local.setViewAngles(angles);
                if (local.isCrosshairOverPlayer() && Main.INSTANCE.getGui().autoShoot.isSelected()) {
                    this.r.mousePress(16);
                    this.r.mouseRelease(16);
                }
            }
        }
    }
    
    @Override
    public void onKeyPress(final GlobalKeyEvent event) {
        if (Main.INSTANCE.getGui().aimkey.getSelectedItem() != "Mouse1" && event.getKeyChar() == ((String)Main.INSTANCE.getGui().aimkey.getSelectedItem()).toLowerCase().charAt(0)) {
            this.doAim = true;
        }
    }
    
    @Override
    public void onKeyRelease(final GlobalKeyEvent event) {
        if (Main.INSTANCE.getGui().aimkey.getSelectedItem() != "Mouse1" && event.getKeyChar() == ((String)Main.INSTANCE.getGui().aimkey.getSelectedItem()).toLowerCase().charAt(0)) {
            this.doAim = false;
        }
    }
    
    @Override
    public void onMousePress(final GlobalMouseEvent event) {
        if (Main.INSTANCE.getGui().aimkey.getSelectedItem() == "Mouse1" && Main.INSTANCE.getEntityList().getLocalPlayer() != null && event.getButton() == 1) {
            this.setupClick = true;
        }
    }
    
    public Entity getClosestToCrosshair(final LocalPlayer local) {
        Entity entityaaa = null;
        float angle = (float)Main.INSTANCE.getGui().fov.getValue();
        for (final Entity entity : Main.INSTANCE.getEntityList().getEntities()) {
            if (entity != local && entity.getTeam() != local.getTeam()) {
                int bone = ((EnumBone)Main.INSTANCE.getGui().bone.getSelectedItem()).getBoneID();
                final String lol = "this is a super gay fix for demoman and engineer head bone ids being different than everyone else";
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(entity.getAddress() + 5452L) == 4L) {
                    bone = 16;
                }
                if (bone == 6 && Main.INSTANCE.getTF2Process().readUnsignedInt(entity.getAddress() + 5452L) == 9L) {
                    bone = 8;
                }
                final Vector3f rots = this.getRotations(local, entity, local.getBonePos(entity, bone));
                final float yawDifference = this.angleDifference(local.getViewAngles().y, rots.y);
                final float pitchDifference = this.angleDifference(local.getViewAngles().x, rots.x);
                if (yawDifference > angle && pitchDifference > angle) {
                    continue;
                }
                final float angleDif = (yawDifference + pitchDifference) / 2.0f;
                if (angleDif > angle) {
                    continue;
                }
                angle = yawDifference;
                entityaaa = entity;
            }
        }
        return entityaaa;
    }
    
    public float angleDifference(final float ang1, final float ang2) {
        return Math.abs(((ang1 - ang2 + 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f);
    }
    
    public float angleDifferenceNonAbs(final float ang1, final float ang2) {
        return ((ang1 - ang2 + 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f;
    }
    
    public Vector3f normalizeRot(final Vector3f angle) {
        if (angle.x > 89.0f && angle.y <= 180.0f) {
            angle.x = 89.0f;
        }
        if (angle.y > 180.0f) {
            angle.y -= 360.0f;
        }
        if (angle.x < -89.0f) {
            angle.x = -89.0f;
        }
        if (angle.y > 180.0f) {
            angle.y -= 360.0f;
        }
        if (angle.y < -180.0f) {
            angle.y += 360.0f;
        }
        if (angle.z != 0.0f) {
            angle.z = 0.0f;
        }
        return angle;
    }
    
    public Vector3f getRotations(final LocalPlayer player, final Entity e, final Vector3f bonePos) {
        final Vector3f angles = new Vector3f();
        final float x = player.getPos().x - (bonePos.x + 0.02f * e.getVelocity().x);
        final float y = player.getPos().z + player.getViewOffsets().z - (bonePos.z + 0.02f * e.getVelocity().z) - 3.0f;
        final float z = player.getPos().y - (bonePos.y + 0.02f * e.getVelocity().y);
        final double dist = Math.sqrt(x * x + z * z);
        angles.y = (float)(Math.atan(y / dist) * 57.29577951308232);
        angles.x = (float)(Math.atan(z / x) * 57.29577951308232);
        angles.x += ((x >= 0.0f) ? 180 : 0);
        return angles;
    }
}
