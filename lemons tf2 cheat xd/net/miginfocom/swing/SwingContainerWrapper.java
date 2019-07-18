// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.swing;

import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import net.miginfocom.layout.ComponentWrapper;
import java.awt.Component;
import java.awt.Container;
import java.awt.Color;
import net.miginfocom.layout.ContainerWrapper;

public final class SwingContainerWrapper extends SwingComponentWrapper implements ContainerWrapper
{
    private static final Color DB_CELL_OUTLINE;
    
    public SwingContainerWrapper(final Container container) {
        super(container);
    }
    
    public ComponentWrapper[] getComponents() {
        final Container container = (Container)this.getComponent();
        final ComponentWrapper[] array = new ComponentWrapper[container.getComponentCount()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new SwingComponentWrapper(container.getComponent(i));
        }
        return array;
    }
    
    public int getComponentCount() {
        return ((Container)this.getComponent()).getComponentCount();
    }
    
    public Object getLayout() {
        return ((Container)this.getComponent()).getLayout();
    }
    
    public final boolean isLeftToRight() {
        return ((Container)this.getComponent()).getComponentOrientation().isLeftToRight();
    }
    
    public final void paintDebugCell(final int n, final int n2, final int n3, final int n4) {
        final Component component = (Component)this.getComponent();
        if (!component.isShowing()) {
            return;
        }
        final Graphics2D graphics2D = (Graphics2D)component.getGraphics();
        if (graphics2D == null) {
            return;
        }
        graphics2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[] { 2.0f, 3.0f }, 0.0f));
        graphics2D.setPaint(SwingContainerWrapper.DB_CELL_OUTLINE);
        graphics2D.drawRect(n, n2, n3 - 1, n4 - 1);
    }
    
    @Override
    public int getComponetType(final boolean b) {
        return 1;
    }
    
    @Override
    public int getLayoutHashCode() {
        super.getLayoutHashCode();
        if (this.isLeftToRight()) {}
        return 0;
    }
    
    static {
        DB_CELL_OUTLINE = new Color(255, 0, 0);
    }
}
