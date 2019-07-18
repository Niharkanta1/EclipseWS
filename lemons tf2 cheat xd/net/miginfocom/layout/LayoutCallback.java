// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

public abstract class LayoutCallback
{
    public UnitValue[] getPosition(final ComponentWrapper componentWrapper) {
        return null;
    }
    
    public BoundSize[] getSize(final ComponentWrapper componentWrapper) {
        return null;
    }
    
    public void correctBounds(final ComponentWrapper componentWrapper) {
    }
}
