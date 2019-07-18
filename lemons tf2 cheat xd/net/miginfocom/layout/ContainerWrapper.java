// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

public interface ContainerWrapper extends ComponentWrapper
{
    ComponentWrapper[] getComponents();
    
    int getComponentCount();
    
    Object getLayout();
    
    boolean isLeftToRight();
    
    void paintDebugCell(final int p0, final int p1, final int p2, final int p3);
}
