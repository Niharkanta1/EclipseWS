// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.swing;

import java.awt.Scrollbar;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JTable;
import java.awt.List;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.JPanel;
import java.awt.TextComponent;
import javax.swing.text.JTextComponent;
import java.awt.Choice;
import javax.swing.JComboBox;
import java.awt.Button;
import javax.swing.AbstractButton;
import java.awt.Checkbox;
import javax.swing.JToggleButton;
import java.awt.Label;
import javax.swing.JLabel;
import java.awt.TextField;
import javax.swing.JTextField;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Graphics2D;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Container;
import net.miginfocom.layout.ContainerWrapper;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import net.miginfocom.layout.PlatformDefaults;
import java.lang.reflect.Method;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.FontMetrics;
import java.util.IdentityHashMap;
import java.awt.Component;
import java.awt.Color;
import net.miginfocom.layout.ComponentWrapper;

public class SwingComponentWrapper implements ComponentWrapper
{
    private static boolean maxSet;
    private static boolean vp;
    private static final Color DB_COMP_OUTLINE;
    private final Component c;
    private int compType;
    private Boolean bl;
    private boolean prefCalled;
    private static final IdentityHashMap<FontMetrics, Point2D.Float> FM_MAP;
    private static final Font SUBST_FONT;
    private static Method BL_METHOD;
    private static Method BL_RES_METHOD;
    private static Method IMS_METHOD;
    
    public SwingComponentWrapper(final Component c) {
        this.compType = -1;
        this.bl = null;
        this.prefCalled = false;
        this.c = c;
    }
    
    public final int getBaseline(final int n, final int n2) {
        if (SwingComponentWrapper.BL_METHOD == null) {
            return -1;
        }
        try {
            return (int)SwingComponentWrapper.BL_METHOD.invoke(this.c, (n < 0) ? this.c.getWidth() : n, (n2 < 0) ? this.c.getHeight() : n2);
        }
        catch (Exception ex) {
            return -1;
        }
    }
    
    public final Object getComponent() {
        return this.c;
    }
    
    public final float getPixelUnitFactor(final boolean b) {
        switch (PlatformDefaults.getLogicalPixelBase()) {
            case 100: {
                final Font font = this.c.getFont();
                final FontMetrics fontMetrics = this.c.getFontMetrics((font != null) ? font : SwingComponentWrapper.SUBST_FONT);
                Point2D.Float float1 = SwingComponentWrapper.FM_MAP.get(fontMetrics);
                if (float1 == null) {
                    final Rectangle2D stringBounds = fontMetrics.getStringBounds("X", this.c.getGraphics());
                    float1 = new Point2D.Float((float)stringBounds.getWidth() / 6.0f, (float)stringBounds.getHeight() / 13.277344f);
                    SwingComponentWrapper.FM_MAP.put(fontMetrics, float1);
                }
                return b ? float1.x : float1.y;
            }
            case 101: {
                final Float n = b ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                if (n != null) {
                    return n;
                }
                return (b ? this.getHorizontalScreenDPI() : this.getVerticalScreenDPI()) / (float)PlatformDefaults.getDefaultDPI();
            }
            default: {
                return 1.0f;
            }
        }
    }
    
    public final int getX() {
        return this.c.getX();
    }
    
    public final int getY() {
        return this.c.getY();
    }
    
    public final int getHeight() {
        return this.c.getHeight();
    }
    
    public final int getWidth() {
        return this.c.getWidth();
    }
    
    public final int getScreenLocationX() {
        final Point point = new Point();
        SwingUtilities.convertPointToScreen(point, this.c);
        return point.x;
    }
    
    public final int getScreenLocationY() {
        final Point point = new Point();
        SwingUtilities.convertPointToScreen(point, this.c);
        return point.y;
    }
    
    public final int getMinimumHeight(final int n) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().height;
    }
    
    public final int getMinimumWidth(final int n) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().width;
    }
    
    public final int getPreferredHeight(final int n) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && n != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), n, 1);
        }
        return this.c.getPreferredSize().height;
    }
    
    public final int getPreferredWidth(final int n) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && n != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), 1, n);
        }
        return this.c.getPreferredSize().width;
    }
    
    public final int getMaximumHeight(final int n) {
        if (!this.isMaxSet(this.c)) {
            return 32767;
        }
        return this.c.getMaximumSize().height;
    }
    
    public final int getMaximumWidth(final int n) {
        if (!this.isMaxSet(this.c)) {
            return 32767;
        }
        return this.c.getMaximumSize().width;
    }
    
    private boolean isMaxSet(final Component component) {
        if (SwingComponentWrapper.IMS_METHOD != null) {
            try {
                return (boolean)SwingComponentWrapper.IMS_METHOD.invoke(component, (Object[])null);
            }
            catch (Exception ex) {
                SwingComponentWrapper.IMS_METHOD = null;
            }
        }
        return isMaxSizeSetOn1_4();
    }
    
    public final ContainerWrapper getParent() {
        final Container parent = this.c.getParent();
        return (parent != null) ? new SwingContainerWrapper(parent) : null;
    }
    
    public final int getHorizontalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }
    
    public final int getVerticalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }
    
    public final int getScreenWidth() {
        try {
            return this.c.getToolkit().getScreenSize().width;
        }
        catch (HeadlessException ex) {
            return 1024;
        }
    }
    
    public final int getScreenHeight() {
        try {
            return this.c.getToolkit().getScreenSize().height;
        }
        catch (HeadlessException ex) {
            return 768;
        }
    }
    
    public final boolean hasBaseline() {
        if (this.bl == null) {
            try {
                if (SwingComponentWrapper.BL_RES_METHOD == null || SwingComponentWrapper.BL_RES_METHOD.invoke(this.c, new Object[0]).toString().equals("OTHER")) {
                    this.bl = Boolean.FALSE;
                }
                else {
                    final Dimension minimumSize = this.c.getMinimumSize();
                    this.bl = (this.getBaseline(minimumSize.width, minimumSize.height) > -1);
                }
            }
            catch (Throwable t) {
                this.bl = Boolean.FALSE;
            }
        }
        return this.bl;
    }
    
    public final String getLinkId() {
        return this.c.getName();
    }
    
    public final void setBounds(final int n, final int n2, final int n3, final int n4) {
        this.c.setBounds(n, n2, n3, n4);
    }
    
    public boolean isVisible() {
        return this.c.isVisible();
    }
    
    public final int[] getVisualPadding() {
        if (SwingComponentWrapper.vp && this.c instanceof JTabbedPane && UIManager.getLookAndFeel().getClass().getName().endsWith("WindowsLookAndFeel")) {
            return new int[] { -1, 0, 2, 2 };
        }
        return null;
    }
    
    public static boolean isMaxSizeSetOn1_4() {
        return SwingComponentWrapper.maxSet;
    }
    
    public static void setMaxSizeSetOn1_4(final boolean maxSet) {
        SwingComponentWrapper.maxSet = maxSet;
    }
    
    public static boolean isVisualPaddingEnabled() {
        return SwingComponentWrapper.vp;
    }
    
    public static void setVisualPaddingEnabled(final boolean vp) {
        SwingComponentWrapper.vp = vp;
    }
    
    public final void paintDebugOutline() {
        if (!this.c.isShowing()) {
            return;
        }
        final Graphics2D graphics2D = (Graphics2D)this.c.getGraphics();
        if (graphics2D == null) {
            return;
        }
        graphics2D.setPaint(SwingComponentWrapper.DB_COMP_OUTLINE);
        graphics2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[] { 2.0f, 4.0f }, 0.0f));
        graphics2D.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    }
    
    public int getComponetType(final boolean b) {
        if (this.compType == -1) {
            this.compType = this.checkType(b);
        }
        return this.compType;
    }
    
    public int getLayoutHashCode() {
        final Dimension maximumSize = this.c.getMaximumSize();
        final int n = maximumSize.width + (maximumSize.height << 5);
        final Dimension preferredSize = this.c.getPreferredSize();
        final int n2 = n + ((preferredSize.width << 10) + (preferredSize.height << 15));
        final Dimension minimumSize = this.c.getMinimumSize();
        int n3 = n2 + ((minimumSize.width << 20) + (minimumSize.height << 25));
        if (this.c.isVisible()) {
            n3 += 1324511;
        }
        final String linkId = this.getLinkId();
        if (linkId != null) {
            n3 += linkId.hashCode();
        }
        return n3;
    }
    
    private int checkType(final boolean b) {
        Component component = this.c;
        if (b) {
            if (component instanceof JScrollPane) {
                component = ((JScrollPane)component).getViewport().getView();
            }
            else if (component instanceof ScrollPane) {
                component = ((ScrollPane)component).getComponent(0);
            }
        }
        if (component instanceof JTextField || component instanceof TextField) {
            return 3;
        }
        if (component instanceof JLabel || component instanceof Label) {
            return 2;
        }
        if (component instanceof JToggleButton || component instanceof Checkbox) {
            return 16;
        }
        if (component instanceof AbstractButton || component instanceof Button) {
            return 5;
        }
        if (component instanceof JComboBox || component instanceof Choice) {
            return 2;
        }
        if (component instanceof JTextComponent || component instanceof TextComponent) {
            return 4;
        }
        if (component instanceof JPanel || component instanceof Canvas) {
            return 10;
        }
        if (component instanceof JList || component instanceof List) {
            return 6;
        }
        if (component instanceof JTable) {
            return 7;
        }
        if (component instanceof JSeparator) {
            return 18;
        }
        if (component instanceof JSpinner) {
            return 13;
        }
        if (component instanceof JProgressBar) {
            return 14;
        }
        if (component instanceof JSlider) {
            return 12;
        }
        if (component instanceof JScrollPane) {
            return 8;
        }
        if (component instanceof JScrollBar || component instanceof Scrollbar) {
            return 17;
        }
        if (component instanceof Container) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public final int hashCode() {
        return this.getComponent().hashCode();
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof ComponentWrapper && this.getComponent().equals(((ComponentWrapper)o).getComponent());
    }
    
    static {
        SwingComponentWrapper.maxSet = false;
        SwingComponentWrapper.vp = true;
        DB_COMP_OUTLINE = new Color(0, 0, 200);
        FM_MAP = new IdentityHashMap<FontMetrics, Point2D.Float>(4);
        SUBST_FONT = new Font("sansserif", 0, 11);
        SwingComponentWrapper.BL_METHOD = null;
        SwingComponentWrapper.BL_RES_METHOD = null;
        try {
            SwingComponentWrapper.BL_METHOD = Component.class.getDeclaredMethod("getBaseline", Integer.TYPE, Integer.TYPE);
            SwingComponentWrapper.BL_RES_METHOD = Component.class.getDeclaredMethod("getBaselineResizeBehavior", (Class<?>[])new Class[0]);
        }
        catch (Throwable t) {}
        SwingComponentWrapper.IMS_METHOD = null;
        try {
            SwingComponentWrapper.IMS_METHOD = Component.class.getDeclaredMethod("isMaximumSizeSet", (Class<?>[])null);
        }
        catch (Throwable t2) {}
    }
}
