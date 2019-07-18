// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.keyboard.event;

import java.util.EventListener;

public interface GlobalKeyListener extends EventListener
{
    void keyPressed(final GlobalKeyEvent p0);
    
    void keyReleased(final GlobalKeyEvent p0);
}
