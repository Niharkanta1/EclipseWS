// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import java.awt.Toolkit;
import java.awt.FontMetrics;
import java.awt.Component;
import com.jgoodies.common.bean.Bean;

public abstract class AbstractUnitConverter extends Bean implements UnitConverter
{
    private static final int DTP_RESOLUTION = 72;
    private static int defaultScreenResolution;
    
    @Override
    public int inchAsPixel(final double in, final Component component) {
        return inchAsPixel(in, this.getScreenResolution(component));
    }
    
    @Override
    public int millimeterAsPixel(final double mm, final Component component) {
        return millimeterAsPixel(mm, this.getScreenResolution(component));
    }
    
    @Override
    public int centimeterAsPixel(final double cm, final Component component) {
        return centimeterAsPixel(cm, this.getScreenResolution(component));
    }
    
    @Override
    public int pointAsPixel(final int pt, final Component component) {
        return pointAsPixel(pt, this.getScreenResolution(component));
    }
    
    @Override
    public int dialogUnitXAsPixel(final int dluX, final Component c) {
        return this.dialogUnitXAsPixel(dluX, this.getDialogBaseUnitsX(c));
    }
    
    @Override
    public int dialogUnitYAsPixel(final int dluY, final Component c) {
        return this.dialogUnitYAsPixel(dluY, this.getDialogBaseUnitsY(c));
    }
    
    protected abstract double getDialogBaseUnitsX(final Component p0);
    
    protected abstract double getDialogBaseUnitsY(final Component p0);
    
    protected static final int inchAsPixel(final double in, final int dpi) {
        return (int)Math.round(dpi * in);
    }
    
    protected static final int millimeterAsPixel(final double mm, final int dpi) {
        return (int)Math.round(dpi * mm * 10.0 / 254.0);
    }
    
    protected static final int centimeterAsPixel(final double cm, final int dpi) {
        return (int)Math.round(dpi * cm * 100.0 / 254.0);
    }
    
    protected static final int pointAsPixel(final double pt, final int dpi) {
        return (int)Math.round(dpi * pt / 72.0);
    }
    
    protected int dialogUnitXAsPixel(final int dluX, final double dialogBaseUnitsX) {
        return (int)Math.round(dluX * dialogBaseUnitsX / 4.0);
    }
    
    protected int dialogUnitYAsPixel(final int dluY, final double dialogBaseUnitsY) {
        return (int)Math.round(dluY * dialogBaseUnitsY / 8.0);
    }
    
    protected double computeAverageCharWidth(final FontMetrics metrics, final String testString) {
        final int width = metrics.stringWidth(testString);
        final double average = width / (double)testString.length();
        return average;
    }
    
    protected int getScreenResolution(final Component c) {
        if (c == null) {
            return this.getDefaultScreenResolution();
        }
        final Toolkit toolkit = c.getToolkit();
        return (toolkit != null) ? toolkit.getScreenResolution() : this.getDefaultScreenResolution();
    }
    
    protected int getDefaultScreenResolution() {
        if (AbstractUnitConverter.defaultScreenResolution == -1) {
            AbstractUnitConverter.defaultScreenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
        }
        return AbstractUnitConverter.defaultScreenResolution;
    }
    
    static {
        AbstractUnitConverter.defaultScreenResolution = -1;
    }
}
