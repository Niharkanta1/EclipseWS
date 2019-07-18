// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.mouse.event;

import java.util.EventListener;

public interface GlobalMouseListener extends EventListener
{
    void mousePressed(final GlobalMouseEvent p0);
    
    void mouseReleased(final GlobalMouseEvent p0);
    
    void mouseMoved(final GlobalMouseEvent p0);
    
    void mouseWheel(final GlobalMouseEvent p0);
}
