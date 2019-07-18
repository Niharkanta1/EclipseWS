// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import java.awt.Component;

public interface UnitConverter
{
    int inchAsPixel(final double p0, final Component p1);
    
    int millimeterAsPixel(final double p0, final Component p1);
    
    int centimeterAsPixel(final double p0, final Component p1);
    
    int pointAsPixel(final int p0, final Component p1);
    
    int dialogUnitXAsPixel(final int p0, final Component p1);
    
    int dialogUnitYAsPixel(final int p0, final Component p1);
}
