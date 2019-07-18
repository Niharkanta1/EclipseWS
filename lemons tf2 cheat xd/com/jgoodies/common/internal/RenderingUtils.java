// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.internal;

import java.awt.print.PrinterGraphics;
import java.awt.PrintGraphics;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.util.Iterator;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.awt.FontMetrics;
import java.util.Map;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.awt.Graphics;
import javax.swing.JComponent;
import java.lang.reflect.Method;

public final class RenderingUtils
{
    private static final String PROP_DESKTOPHINTS = "awt.font.desktophints";
    private static final String SWING_UTILITIES2_NAME = "sun.swing.SwingUtilities2";
    private static Method drawStringMethod;
    private static Method drawStringUnderlineCharAtMethod;
    private static Method getFontMetricsMethod;
    
    private RenderingUtils() {
    }
    
    public static void drawString(final JComponent c, final Graphics g, final String text, final int x, final int y) {
        if (RenderingUtils.drawStringMethod != null) {
            try {
                RenderingUtils.drawStringMethod.invoke(null, c, g, text, x, y);
                return;
            }
            catch (IllegalArgumentException e) {}
            catch (IllegalAccessException e2) {}
            catch (InvocationTargetException ex) {}
        }
        final Graphics2D g2 = (Graphics2D)g;
        final Map<?, ?> oldRenderingHints = (Map<?, ?>)installDesktopHints(g2);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, -1, x, y);
        if (oldRenderingHints != null) {
            g2.addRenderingHints(oldRenderingHints);
        }
    }
    
    public static void drawStringUnderlineCharAt(final JComponent c, final Graphics g, final String text, final int underlinedIndex, final int x, final int y) {
        if (RenderingUtils.drawStringUnderlineCharAtMethod != null) {
            try {
                RenderingUtils.drawStringUnderlineCharAtMethod.invoke(null, c, g, text, new Integer(underlinedIndex), new Integer(x), new Integer(y));
                return;
            }
            catch (IllegalArgumentException e) {}
            catch (IllegalAccessException e2) {}
            catch (InvocationTargetException ex) {}
        }
        final Graphics2D g2 = (Graphics2D)g;
        final Map oldRenderingHints = installDesktopHints(g2);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, underlinedIndex, x, y);
        if (oldRenderingHints != null) {
            g2.addRenderingHints(oldRenderingHints);
        }
    }
    
    public static FontMetrics getFontMetrics(final JComponent c, final Graphics g) {
        if (RenderingUtils.getFontMetricsMethod != null) {
            try {
                return (FontMetrics)RenderingUtils.getFontMetricsMethod.invoke(null, c, g);
            }
            catch (IllegalArgumentException e) {}
            catch (IllegalAccessException e2) {}
            catch (InvocationTargetException ex) {}
        }
        return c.getFontMetrics(g.getFont());
    }
    
    private static Method getMethodDrawString() {
        try {
            final Class<?> clazz = Class.forName("sun.swing.SwingUtilities2");
            return clazz.getMethod("drawString", JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE);
        }
        catch (ClassNotFoundException e) {}
        catch (SecurityException e2) {}
        catch (NoSuchMethodException ex) {}
        return null;
    }
    
    private static Method getMethodDrawStringUnderlineCharAt() {
        try {
            final Class clazz = Class.forName("sun.swing.SwingUtilities2");
            return clazz.getMethod("drawStringUnderlineCharAt", JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (ClassNotFoundException e) {}
        catch (SecurityException e2) {}
        catch (NoSuchMethodException ex) {}
        return null;
    }
    
    private static Method getMethodGetFontMetrics() {
        try {
            final Class clazz = Class.forName("sun.swing.SwingUtilities2");
            return clazz.getMethod("getFontMetrics", JComponent.class, Graphics.class);
        }
        catch (ClassNotFoundException e) {}
        catch (SecurityException e2) {}
        catch (NoSuchMethodException ex) {}
        return null;
    }
    
    private static Map installDesktopHints(final Graphics2D g2) {
        Map oldRenderingHints = null;
        final Map desktopHints = desktopHints(g2);
        if (desktopHints != null && !desktopHints.isEmpty()) {
            oldRenderingHints = new HashMap(desktopHints.size());
            for (final RenderingHints.Key key : desktopHints.keySet()) {
                oldRenderingHints.put(key, g2.getRenderingHint(key));
            }
            g2.addRenderingHints(desktopHints);
        }
        return oldRenderingHints;
    }
    
    private static Map desktopHints(final Graphics2D g2) {
        if (isPrinting(g2)) {
            return null;
        }
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final GraphicsDevice device = g2.getDeviceConfiguration().getDevice();
        Map desktopHints = (Map)toolkit.getDesktopProperty("awt.font.desktophints." + device.getIDstring());
        if (desktopHints == null) {
            desktopHints = (Map)toolkit.getDesktopProperty("awt.font.desktophints");
        }
        if (desktopHints != null) {
            final Object aaHint = desktopHints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
            if (aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                desktopHints = null;
            }
        }
        return desktopHints;
    }
    
    private static boolean isPrinting(final Graphics g) {
        return g instanceof PrintGraphics || g instanceof PrinterGraphics;
    }
    
    static {
        RenderingUtils.drawStringMethod = null;
        RenderingUtils.drawStringUnderlineCharAtMethod = null;
        RenderingUtils.getFontMetricsMethod = null;
        RenderingUtils.drawStringMethod = getMethodDrawString();
        RenderingUtils.drawStringUnderlineCharAtMethod = getMethodDrawStringUnderlineCharAt();
        RenderingUtils.getFontMetricsMethod = getMethodGetFontMetrics();
    }
}
