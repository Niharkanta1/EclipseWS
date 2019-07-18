// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.feature;

import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import java.awt.Graphics;
import me.lemon.lemonware.tf2.LocalPlayer;

public abstract class Feature
{
    private final String name;
    private final FeatureType type;
    private boolean enabled;
    
    public Feature(final String name, final FeatureType type) {
        this.name = name;
        this.type = type;
    }
    
    public abstract void run(final LocalPlayer p0);
    
    public void draw2D(final LocalPlayer local, final Graphics g) {
    }
    
    public abstract void onKeyPress(final GlobalKeyEvent p0);
    
    public abstract void onKeyRelease(final GlobalKeyEvent p0);
    
    public void onMousePress(final GlobalMouseEvent event) {
    }
    
    public void onMouseRelease(final GlobalMouseEvent event) {
    }
    
    public String getName() {
        return this.name;
    }
    
    public FeatureType getType() {
        return this.type;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public enum FeatureType
    {
        AIMBOT("AIMBOT", 0, "aimbot"), 
        VISUAL("VISUAL", 1, "visual"), 
        OTHER("OTHER", 2, "other");
        
        private final String name;
        
        private FeatureType(final String s, final int n, final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
