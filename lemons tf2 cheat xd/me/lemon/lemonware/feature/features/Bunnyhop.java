// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.feature.features;

import lc.kra.system.keyboard.event.GlobalKeyEvent;
import me.lemon.lemonware.tf2.LocalPlayer;
import java.awt.AWTException;
import java.awt.Robot;
import me.lemon.lemonware.feature.Feature;

public class Bunnyhop extends Feature
{
    private boolean ignoreKey;
    private boolean spaceDown;
    private long lastHop;
    private Robot robot;
    
    public Bunnyhop() {
        super("bunnyhop", FeatureType.OTHER);
        try {
            this.robot = new Robot();
        }
        catch (AWTException ex) {}
    }
    
    @Override
    public void run(final LocalPlayer local) {
        if (this.spaceDown && local.isOnGround() && System.currentTimeMillis() - this.lastHop >= 50L) {
            this.lastHop = System.currentTimeMillis();
            this.ignoreKey = true;
            this.robot.keyPress(32);
            this.robot.keyRelease(32);
        }
    }
    
    @Override
    public void onKeyPress(final GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == 32 && !this.ignoreKey) {
            this.lastHop = System.currentTimeMillis() + 100L;
            this.spaceDown = true;
        }
    }
    
    @Override
    public void onKeyRelease(final GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == 32 && !this.ignoreKey) {
            this.spaceDown = false;
        }
        else {
            this.ignoreKey = false;
        }
    }
}
