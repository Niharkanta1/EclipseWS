// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.List;
import java.awt.Container;
import java.awt.Component;
import com.jgoodies.common.base.Preconditions;
import java.io.Serializable;

public final class ConstantSize implements Size, Serializable
{
    public static final Unit PIXEL;
    public static final Unit POINT;
    public static final Unit DIALOG_UNITS_X;
    public static final Unit DIALOG_UNITS_Y;
    public static final Unit MILLIMETER;
    public static final Unit CENTIMETER;
    public static final Unit INCH;
    public static final Unit PX;
    public static final Unit PT;
    public static final Unit DLUX;
    public static final Unit DLUY;
    public static final Unit MM;
    public static final Unit CM;
    public static final Unit IN;
    private static final Unit[] VALUES;
    private final double value;
    private final Unit unit;
    
    public ConstantSize(final int value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }
    
    public ConstantSize(final double value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }
    
    static ConstantSize valueOf(final String encodedValueAndUnit, final boolean horizontal) {
        final String[] split = splitValueAndUnit(encodedValueAndUnit);
        final String encodedValue = split[0];
        final String encodedUnit = split[1];
        final Unit unit = Unit.valueOf(encodedUnit, horizontal);
        final double value = Double.parseDouble(encodedValue);
        if (unit.requiresIntegers) {
            Preconditions.checkArgument(value == (int)value, "%s value %s must be an integer.", unit, encodedValue);
        }
        return new ConstantSize(value, unit);
    }
    
    static ConstantSize dluX(final int value) {
        return new ConstantSize(value, ConstantSize.DLUX);
    }
    
    static ConstantSize dluY(final int value) {
        return new ConstantSize(value, ConstantSize.DLUY);
    }
    
    public double getValue() {
        return this.value;
    }
    
    public Unit getUnit() {
        return this.unit;
    }
    
    public int getPixelSize(final Component component) {
        if (this.unit == ConstantSize.PIXEL) {
            return this.intValue();
        }
        if (this.unit == ConstantSize.POINT) {
            return Sizes.pointAsPixel(this.intValue(), component);
        }
        if (this.unit == ConstantSize.INCH) {
            return Sizes.inchAsPixel(this.value, component);
        }
        if (this.unit == ConstantSize.MILLIMETER) {
            return Sizes.millimeterAsPixel(this.value, component);
        }
        if (this.unit == ConstantSize.CENTIMETER) {
            return Sizes.centimeterAsPixel(this.value, component);
        }
        if (this.unit == ConstantSize.DIALOG_UNITS_X) {
            return Sizes.dialogUnitXAsPixel(this.intValue(), component);
        }
        if (this.unit == ConstantSize.DIALOG_UNITS_Y) {
            return Sizes.dialogUnitYAsPixel(this.intValue(), component);
        }
        throw new IllegalStateException("Invalid unit " + this.unit);
    }
    
    @Override
    public int maximumSize(final Container container, final List components, final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure, final FormLayout.Measure defaultMeasure) {
        return this.getPixelSize(container);
    }
    
    @Override
    public boolean compressible() {
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstantSize)) {
            return false;
        }
        final ConstantSize size = (ConstantSize)o;
        return this.value == size.value && this.unit == size.unit;
    }
    
    @Override
    public int hashCode() {
        return new Double(this.value).hashCode() + 37 * this.unit.hashCode();
    }
    
    @Override
    public String toString() {
        return (this.value == this.intValue()) ? (Integer.toString(this.intValue()) + this.unit.abbreviation()) : (Double.toString(this.value) + this.unit.abbreviation());
    }
    
    @Override
    public String encode() {
        return (this.value == this.intValue()) ? (Integer.toString(this.intValue()) + this.unit.encode()) : (Double.toString(this.value) + this.unit.encode());
    }
    
    private int intValue() {
        return (int)Math.round(this.value);
    }
    
    private static String[] splitValueAndUnit(final String encodedValueAndUnit) {
        final String[] result = new String[2];
        int firstLetterIndex;
        for (int len = firstLetterIndex = encodedValueAndUnit.length(); firstLetterIndex > 0 && Character.isLetter(encodedValueAndUnit.charAt(firstLetterIndex - 1)); --firstLetterIndex) {}
        result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
        result[1] = encodedValueAndUnit.substring(firstLetterIndex);
        return result;
    }
    
    static {
        PIXEL = new Unit("Pixel", "px", (String)null, true);
        POINT = new Unit("Point", "pt", (String)null, true);
        DIALOG_UNITS_X = new Unit("Dialog units X", "dluX", "dlu", true);
        DIALOG_UNITS_Y = new Unit("Dialog units Y", "dluY", "dlu", true);
        MILLIMETER = new Unit("Millimeter", "mm", (String)null, false);
        CENTIMETER = new Unit("Centimeter", "cm", (String)null, false);
        INCH = new Unit("Inch", "in", (String)null, false);
        PX = ConstantSize.PIXEL;
        PT = ConstantSize.POINT;
        DLUX = ConstantSize.DIALOG_UNITS_X;
        DLUY = ConstantSize.DIALOG_UNITS_Y;
        MM = ConstantSize.MILLIMETER;
        CM = ConstantSize.CENTIMETER;
        IN = ConstantSize.INCH;
        VALUES = new Unit[] { ConstantSize.PIXEL, ConstantSize.POINT, ConstantSize.DIALOG_UNITS_X, ConstantSize.DIALOG_UNITS_Y, ConstantSize.MILLIMETER, ConstantSize.CENTIMETER, ConstantSize.INCH };
    }
    
    public static final class Unit implements Serializable
    {
        private final transient String name;
        private final transient String abbreviation;
        private final transient String parseAbbreviation;
        final transient boolean requiresIntegers;
        private static int nextOrdinal;
        private final int ordinal;
        
        private Unit(final String name, final String abbreviation, final String parseAbbreviation, final boolean requiresIntegers) {
            this.ordinal = Unit.nextOrdinal++;
            this.name = name;
            this.abbreviation = abbreviation;
            this.parseAbbreviation = parseAbbreviation;
            this.requiresIntegers = requiresIntegers;
        }
        
        static Unit valueOf(final String name, final boolean horizontal) {
            if (name.length() == 0) {
                final Unit defaultUnit = Sizes.getDefaultUnit();
                if (defaultUnit != null) {
                    return defaultUnit;
                }
                return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
            }
            else {
                if (name.equals("px")) {
                    return ConstantSize.PIXEL;
                }
                if (name.equals("dlu")) {
                    return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
                }
                if (name.equals("pt")) {
                    return ConstantSize.POINT;
                }
                if (name.equals("in")) {
                    return ConstantSize.INCH;
                }
                if (name.equals("mm")) {
                    return ConstantSize.MILLIMETER;
                }
                if (name.equals("cm")) {
                    return ConstantSize.CENTIMETER;
                }
                throw new IllegalArgumentException("Invalid unit name '" + name + "'. Must be one of: " + "px, dlu, pt, mm, cm, in");
            }
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public String encode() {
            return (this.parseAbbreviation != null) ? this.parseAbbreviation : this.abbreviation;
        }
        
        public String abbreviation() {
            return this.abbreviation;
        }
        
        private Object readResolve() {
            return ConstantSize.VALUES[this.ordinal];
        }
        
        static {
            Unit.nextOrdinal = 0;
        }
    }
}
