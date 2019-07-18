// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.Iterator;
import java.util.List;
import java.awt.Container;
import java.io.Serializable;
import com.jgoodies.forms.util.DefaultUnitConverter;
import java.awt.Component;
import java.util.Locale;
import com.jgoodies.forms.util.UnitConverter;

public final class Sizes
{
    public static final ConstantSize ZERO;
    public static final ConstantSize DLUX1;
    public static final ConstantSize DLUX2;
    public static final ConstantSize DLUX3;
    public static final ConstantSize DLUX4;
    public static final ConstantSize DLUX5;
    public static final ConstantSize DLUX6;
    public static final ConstantSize DLUX7;
    public static final ConstantSize DLUX8;
    public static final ConstantSize DLUX9;
    public static final ConstantSize DLUX11;
    public static final ConstantSize DLUX14;
    public static final ConstantSize DLUX21;
    public static final ConstantSize DLUY1;
    public static final ConstantSize DLUY2;
    public static final ConstantSize DLUY3;
    public static final ConstantSize DLUY4;
    public static final ConstantSize DLUY5;
    public static final ConstantSize DLUY6;
    public static final ConstantSize DLUY7;
    public static final ConstantSize DLUY8;
    public static final ConstantSize DLUY9;
    public static final ConstantSize DLUY11;
    public static final ConstantSize DLUY14;
    public static final ConstantSize DLUY21;
    public static final ComponentSize MINIMUM;
    public static final ComponentSize PREFERRED;
    public static final ComponentSize DEFAULT;
    private static final ComponentSize[] VALUES;
    private static UnitConverter unitConverter;
    private static ConstantSize.Unit defaultUnit;
    
    private Sizes() {
    }
    
    public static ConstantSize constant(final String encodedValueAndUnit, final boolean horizontal) {
        final String lowerCase = encodedValueAndUnit.toLowerCase(Locale.ENGLISH);
        final String trimmed = lowerCase.trim();
        return ConstantSize.valueOf(trimmed, horizontal);
    }
    
    public static ConstantSize dluX(final int value) {
        return ConstantSize.dluX(value);
    }
    
    public static ConstantSize dluY(final int value) {
        return ConstantSize.dluY(value);
    }
    
    public static ConstantSize pixel(final int value) {
        return new ConstantSize(value, ConstantSize.PIXEL);
    }
    
    public static Size bounded(final Size basis, final Size lowerBound, final Size upperBound) {
        return new BoundedSize(basis, lowerBound, upperBound);
    }
    
    public static int inchAsPixel(final double in, final Component component) {
        return (in == 0.0) ? 0 : getUnitConverter().inchAsPixel(in, component);
    }
    
    public static int millimeterAsPixel(final double mm, final Component component) {
        return (mm == 0.0) ? 0 : getUnitConverter().millimeterAsPixel(mm, component);
    }
    
    public static int centimeterAsPixel(final double cm, final Component component) {
        return (cm == 0.0) ? 0 : getUnitConverter().centimeterAsPixel(cm, component);
    }
    
    public static int pointAsPixel(final int pt, final Component component) {
        return (pt == 0) ? 0 : getUnitConverter().pointAsPixel(pt, component);
    }
    
    public static int dialogUnitXAsPixel(final int dluX, final Component component) {
        return (dluX == 0) ? 0 : getUnitConverter().dialogUnitXAsPixel(dluX, component);
    }
    
    public static int dialogUnitYAsPixel(final int dluY, final Component component) {
        return (dluY == 0) ? 0 : getUnitConverter().dialogUnitYAsPixel(dluY, component);
    }
    
    public static UnitConverter getUnitConverter() {
        if (Sizes.unitConverter == null) {
            Sizes.unitConverter = DefaultUnitConverter.getInstance();
        }
        return Sizes.unitConverter;
    }
    
    public static void setUnitConverter(final UnitConverter newUnitConverter) {
        Sizes.unitConverter = newUnitConverter;
    }
    
    public static ConstantSize.Unit getDefaultUnit() {
        return Sizes.defaultUnit;
    }
    
    public static void setDefaultUnit(final ConstantSize.Unit unit) {
        if (unit == ConstantSize.DLUX || unit == ConstantSize.DLUY) {
            throw new IllegalArgumentException("The unit must not be DLUX or DLUY. To use DLU as default unit, invoke this method with null.");
        }
        Sizes.defaultUnit = unit;
    }
    
    static {
        ZERO = pixel(0);
        DLUX1 = dluX(1);
        DLUX2 = dluX(2);
        DLUX3 = dluX(3);
        DLUX4 = dluX(4);
        DLUX5 = dluX(5);
        DLUX6 = dluX(6);
        DLUX7 = dluX(7);
        DLUX8 = dluX(8);
        DLUX9 = dluX(9);
        DLUX11 = dluX(11);
        DLUX14 = dluX(14);
        DLUX21 = dluX(21);
        DLUY1 = dluY(1);
        DLUY2 = dluY(2);
        DLUY3 = dluY(3);
        DLUY4 = dluY(4);
        DLUY5 = dluY(5);
        DLUY6 = dluY(6);
        DLUY7 = dluY(7);
        DLUY8 = dluY(8);
        DLUY9 = dluY(9);
        DLUY11 = dluY(11);
        DLUY14 = dluY(14);
        DLUY21 = dluY(21);
        MINIMUM = new ComponentSize("minimum");
        PREFERRED = new ComponentSize("preferred");
        DEFAULT = new ComponentSize("default");
        VALUES = new ComponentSize[] { Sizes.MINIMUM, Sizes.PREFERRED, Sizes.DEFAULT };
        Sizes.defaultUnit = ConstantSize.PIXEL;
    }
    
    static final class ComponentSize implements Size, Serializable
    {
        private final transient String name;
        private static int nextOrdinal;
        private final int ordinal;
        
        private ComponentSize(final String name) {
            this.ordinal = ComponentSize.nextOrdinal++;
            this.name = name;
        }
        
        static ComponentSize valueOf(final String str) {
            if (str.equals("m") || str.equals("min")) {
                return Sizes.MINIMUM;
            }
            if (str.equals("p") || str.equals("pref")) {
                return Sizes.PREFERRED;
            }
            if (str.equals("d") || str.equals("default")) {
                return Sizes.DEFAULT;
            }
            return null;
        }
        
        @Override
        public int maximumSize(final Container container, final List components, final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure, final FormLayout.Measure defaultMeasure) {
            final FormLayout.Measure measure = (this == Sizes.MINIMUM) ? minMeasure : ((this == Sizes.PREFERRED) ? prefMeasure : defaultMeasure);
            int maximum = 0;
            for (final Component c : components) {
                maximum = Math.max(maximum, measure.sizeOf(c));
            }
            return maximum;
        }
        
        @Override
        public boolean compressible() {
            return this == Sizes.DEFAULT;
        }
        
        @Override
        public String toString() {
            return this.encode();
        }
        
        @Override
        public String encode() {
            return this.name.substring(0, 1);
        }
        
        private Object readResolve() {
            return Sizes.VALUES[this.ordinal];
        }
        
        static {
            ComponentSize.nextOrdinal = 0;
        }
    }
}
