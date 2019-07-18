// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.beans.Expression;
import java.beans.Encoder;
import java.beans.PersistenceDelegate;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

public final class UnitValue implements Serializable
{
    private static final HashMap<String, Integer> UNIT_MAP;
    private static final ArrayList<UnitConverter> CONVERTERS;
    public static final int STATIC = 100;
    public static final int ADD = 101;
    public static final int SUB = 102;
    public static final int MUL = 103;
    public static final int DIV = 104;
    public static final int MIN = 105;
    public static final int MAX = 106;
    public static final int MID = 107;
    public static final int PIXEL = 0;
    public static final int LPX = 1;
    public static final int LPY = 2;
    public static final int MM = 3;
    public static final int CM = 4;
    public static final int INCH = 5;
    public static final int PERCENT = 6;
    public static final int PT = 7;
    public static final int SPX = 8;
    public static final int SPY = 9;
    public static final int ALIGN = 12;
    public static final int MIN_SIZE = 13;
    public static final int PREF_SIZE = 14;
    public static final int MAX_SIZE = 15;
    public static final int BUTTON = 16;
    public static final int LINK_X = 18;
    public static final int LINK_Y = 19;
    public static final int LINK_W = 20;
    public static final int LINK_H = 21;
    public static final int LINK_X2 = 22;
    public static final int LINK_Y2 = 23;
    public static final int LINK_XPOS = 24;
    public static final int LINK_YPOS = 25;
    public static final int LOOKUP = 26;
    public static final int LABEL_ALIGN = 27;
    private static final int IDENTITY = -1;
    static final UnitValue ZERO;
    static final UnitValue TOP;
    static final UnitValue LEADING;
    static final UnitValue LEFT;
    static final UnitValue CENTER;
    static final UnitValue TRAILING;
    static final UnitValue RIGHT;
    static final UnitValue BOTTOM;
    static final UnitValue LABEL;
    static final UnitValue INF;
    static final UnitValue BASELINE_IDENTITY;
    private final transient float value;
    private final transient int unit;
    private final transient int oper;
    private final transient String unitStr;
    private transient String linkId;
    private final transient boolean isHor;
    private final transient UnitValue[] subUnits;
    private static final float[] SCALE;
    private static final long serialVersionUID = 1L;
    
    public UnitValue(final float n) {
        this(n, null, 0, true, 100, null, null, n + "px");
    }
    
    public UnitValue(final float n, final int n2, final String s) {
        this(n, null, n2, true, 100, null, null, s);
    }
    
    UnitValue(final float n, final String s, final boolean b, final int n2, final String s2) {
        this(n, s, -1, b, n2, null, null, s2);
    }
    
    UnitValue(final boolean b, final int n, final UnitValue unitValue, final UnitValue unitValue2, final String s) {
        this(0.0f, "", -1, b, n, unitValue, unitValue2, s);
        if (unitValue == null || unitValue2 == null) {
            throw new IllegalArgumentException("Sub units is null!");
        }
    }
    
    private UnitValue(final float value, final String unitStr, final int n, final boolean isHor, final int oper, final UnitValue unitValue, final UnitValue unitValue2, final String s) {
        this.linkId = null;
        if (oper < 100 || oper > 107) {
            throw new IllegalArgumentException("Unknown Operation: " + oper);
        }
        if (oper >= 101 && oper <= 107 && (unitValue == null || unitValue2 == null)) {
            throw new IllegalArgumentException(oper + " Operation may not have null sub-UnitValues.");
        }
        this.value = value;
        this.oper = oper;
        this.isHor = isHor;
        this.unit = (((this.unitStr = unitStr) != null) ? this.parseUnitString() : n);
        this.subUnits = (UnitValue[])((unitValue != null && unitValue2 != null) ? new UnitValue[] { unitValue, unitValue2 } : null);
        LayoutUtil.putCCString(this, s);
    }
    
    public final int getPixels(final float n, final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper) {
        return Math.round(this.getPixelsExact(n, containerWrapper, componentWrapper));
    }
    
    public final float getPixelsExact(final float n, final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper) {
        if (containerWrapper == null) {
            return 1.0f;
        }
        if (this.oper != 100) {
            if (this.subUnits != null && this.subUnits.length == 2) {
                final float pixelsExact = this.subUnits[0].getPixelsExact(n, containerWrapper, componentWrapper);
                final float pixelsExact2 = this.subUnits[1].getPixelsExact(n, containerWrapper, componentWrapper);
                switch (this.oper) {
                    case 101: {
                        return pixelsExact + pixelsExact2;
                    }
                    case 102: {
                        return pixelsExact - pixelsExact2;
                    }
                    case 103: {
                        return pixelsExact * pixelsExact2;
                    }
                    case 104: {
                        return pixelsExact / pixelsExact2;
                    }
                    case 105: {
                        return (pixelsExact < pixelsExact2) ? pixelsExact : pixelsExact2;
                    }
                    case 106: {
                        return (pixelsExact > pixelsExact2) ? pixelsExact : pixelsExact2;
                    }
                    case 107: {
                        return (pixelsExact + pixelsExact2) * 0.5f;
                    }
                }
            }
            throw new IllegalArgumentException("Internal: Unknown Oper: " + this.oper);
        }
        switch (this.unit) {
            case 0: {
                return this.value;
            }
            case 1:
            case 2: {
                return containerWrapper.getPixelUnitFactor(this.unit == 1) * this.value;
            }
            case 3:
            case 4:
            case 5:
            case 7: {
                float n2 = UnitValue.SCALE[this.unit - 3];
                final Float n3 = this.isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                if (n3 != null) {
                    n2 *= n3;
                }
                return (this.isHor ? containerWrapper.getHorizontalScreenDPI() : containerWrapper.getVerticalScreenDPI()) * this.value / n2;
            }
            case 6: {
                return this.value * n * 0.01f;
            }
            case 8:
            case 9: {
                return ((this.unit == 8) ? containerWrapper.getScreenWidth() : containerWrapper.getScreenHeight()) * this.value * 0.01f;
            }
            case 12: {
                final Integer value = LinkHandler.getValue(containerWrapper.getLayout(), "visual", this.isHor ? 0 : 1);
                final Integer value2 = LinkHandler.getValue(containerWrapper.getLayout(), "visual", this.isHor ? 2 : 3);
                if (value == null || value2 == null) {
                    return 0.0f;
                }
                return this.value * (Math.max(0, value2) - n) + value;
            }
            case 13: {
                if (componentWrapper == null) {
                    return 0.0f;
                }
                return this.isHor ? ((float)componentWrapper.getMinimumWidth(componentWrapper.getHeight())) : ((float)componentWrapper.getMinimumHeight(componentWrapper.getWidth()));
            }
            case 14: {
                if (componentWrapper == null) {
                    return 0.0f;
                }
                return this.isHor ? ((float)componentWrapper.getPreferredWidth(componentWrapper.getHeight())) : ((float)componentWrapper.getPreferredHeight(componentWrapper.getWidth()));
            }
            case 15: {
                if (componentWrapper == null) {
                    return 0.0f;
                }
                return this.isHor ? ((float)componentWrapper.getMaximumWidth(componentWrapper.getHeight())) : ((float)componentWrapper.getMaximumHeight(componentWrapper.getWidth()));
            }
            case 16: {
                return (float)PlatformDefaults.getMinimumButtonWidth().getPixels(n, containerWrapper, componentWrapper);
            }
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25: {
                final Integer value3 = LinkHandler.getValue(containerWrapper.getLayout(), this.getLinkTargetId(), this.unit - ((this.unit >= 24) ? 24 : 18));
                if (value3 == null) {
                    return 0.0f;
                }
                if (this.unit == 24) {
                    return (float)(containerWrapper.getScreenLocationX() + value3);
                }
                if (this.unit == 25) {
                    return (float)(containerWrapper.getScreenLocationY() + value3);
                }
                return value3;
            }
            case 26: {
                final float lookup = this.lookup(n, containerWrapper, componentWrapper);
                if (lookup != -8.7654312E7f) {
                    return lookup;
                }
                return PlatformDefaults.getLabelAlignPercentage() * n;
            }
            case 27: {
                return PlatformDefaults.getLabelAlignPercentage() * n;
            }
            default: {
                throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
            }
        }
    }
    
    private float lookup(final float n, final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper) {
        for (int i = UnitValue.CONVERTERS.size() - 1; i >= 0; --i) {
            final float n2 = (float)UnitValue.CONVERTERS.get(i).convertToPixels(this.value, this.unitStr, this.isHor, n, containerWrapper, componentWrapper);
            if (n2 != -8.7654312E7f) {
                return n2;
            }
        }
        return (float)PlatformDefaults.convertToPixels(this.value, this.unitStr, this.isHor, n, containerWrapper, componentWrapper);
    }
    
    private int parseUnitString() {
        if (this.unitStr.length() == 0) {
            return this.isHor ? PlatformDefaults.getDefaultHorizontalUnit() : PlatformDefaults.getDefaultVerticalUnit();
        }
        final Integer n = UnitValue.UNIT_MAP.get(this.unitStr);
        if (n != null) {
            return n;
        }
        if (this.unitStr.equals("lp")) {
            return this.isHor ? 1 : 2;
        }
        if (this.unitStr.equals("sp")) {
            return this.isHor ? 8 : 9;
        }
        if (this.lookup(0.0f, null, null) != -8.7654312E7f) {
            return 26;
        }
        final int index = this.unitStr.indexOf(46);
        if (index != -1) {
            this.linkId = this.unitStr.substring(0, index);
            final String substring = this.unitStr.substring(index + 1);
            if (substring.equals("x")) {
                return 18;
            }
            if (substring.equals("y")) {
                return 19;
            }
            if (substring.equals("w") || substring.equals("width")) {
                return 20;
            }
            if (substring.equals("h") || substring.equals("height")) {
                return 21;
            }
            if (substring.equals("x2")) {
                return 22;
            }
            if (substring.equals("y2")) {
                return 23;
            }
            if (substring.equals("xpos")) {
                return 24;
            }
            if (substring.equals("ypos")) {
                return 25;
            }
        }
        throw new IllegalArgumentException("Unknown keyword: " + this.unitStr);
    }
    
    final boolean isLinked() {
        return this.linkId != null;
    }
    
    final boolean isLinkedDeep() {
        if (this.subUnits == null) {
            return this.linkId != null;
        }
        for (int i = 0; i < this.subUnits.length; ++i) {
            if (this.subUnits[i].isLinkedDeep()) {
                return true;
            }
        }
        return false;
    }
    
    final String getLinkTargetId() {
        return this.linkId;
    }
    
    final UnitValue getSubUnitValue(final int n) {
        return this.subUnits[n];
    }
    
    final int getSubUnitCount() {
        return (this.subUnits != null) ? this.subUnits.length : 0;
    }
    
    public final UnitValue[] getSubUnits() {
        return (UnitValue[])((this.subUnits != null) ? ((UnitValue[])this.subUnits.clone()) : null);
    }
    
    public final int getUnit() {
        return this.unit;
    }
    
    public final String getUnitString() {
        return this.unitStr;
    }
    
    public final int getOperation() {
        return this.oper;
    }
    
    public final float getValue() {
        return this.value;
    }
    
    public final boolean isHorizontal() {
        return this.isHor;
    }
    
    @Override
    public final String toString() {
        return this.getClass().getName() + ". Value=" + this.value + ", unit=" + this.unit + ", unitString: " + this.unitStr + ", oper=" + this.oper + ", isHor: " + this.isHor;
    }
    
    public final String getConstraintString() {
        return LayoutUtil.getCCString(this);
    }
    
    @Override
    public final int hashCode() {
        return (int)(this.value * 12345.0f) + (this.oper >>> 5) + this.unit >>> 17;
    }
    
    public static synchronized void addGlobalUnitConverter(final UnitConverter unitConverter) {
        if (unitConverter == null) {
            throw new NullPointerException();
        }
        UnitValue.CONVERTERS.add(unitConverter);
    }
    
    public static synchronized boolean removeGlobalUnitConverter(final UnitConverter unitConverter) {
        return UnitValue.CONVERTERS.remove(unitConverter);
    }
    
    public static synchronized UnitConverter[] getGlobalUnitConverters() {
        return UnitValue.CONVERTERS.toArray(new UnitConverter[UnitValue.CONVERTERS.size()]);
    }
    
    @Deprecated
    public static int getDefaultUnit() {
        return PlatformDefaults.getDefaultHorizontalUnit();
    }
    
    @Deprecated
    public static void setDefaultUnit(final int n) {
        PlatformDefaults.setDefaultHorizontalUnit(n);
        PlatformDefaults.setDefaultVerticalUnit(n);
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        if (this.getClass() == UnitValue.class) {
            LayoutUtil.writeAsXML(objectOutputStream, this);
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInputStream));
    }
    
    static {
        UNIT_MAP = new HashMap<String, Integer>(32);
        CONVERTERS = new ArrayList<UnitConverter>();
        UnitValue.UNIT_MAP.put("px", 0);
        UnitValue.UNIT_MAP.put("lpx", 1);
        UnitValue.UNIT_MAP.put("lpy", 2);
        UnitValue.UNIT_MAP.put("%", 6);
        UnitValue.UNIT_MAP.put("cm", 4);
        UnitValue.UNIT_MAP.put("in", 5);
        UnitValue.UNIT_MAP.put("spx", 8);
        UnitValue.UNIT_MAP.put("spy", 9);
        UnitValue.UNIT_MAP.put("al", 12);
        UnitValue.UNIT_MAP.put("mm", 3);
        UnitValue.UNIT_MAP.put("pt", 7);
        UnitValue.UNIT_MAP.put("min", 13);
        UnitValue.UNIT_MAP.put("minimum", 13);
        UnitValue.UNIT_MAP.put("p", 14);
        UnitValue.UNIT_MAP.put("pref", 14);
        UnitValue.UNIT_MAP.put("max", 15);
        UnitValue.UNIT_MAP.put("maximum", 15);
        UnitValue.UNIT_MAP.put("button", 16);
        UnitValue.UNIT_MAP.put("label", 27);
        ZERO = new UnitValue(0.0f, null, 0, true, 100, null, null, "0px");
        TOP = new UnitValue(0.0f, null, 6, false, 100, null, null, "top");
        LEADING = new UnitValue(0.0f, null, 6, true, 100, null, null, "leading");
        LEFT = new UnitValue(0.0f, null, 6, true, 100, null, null, "left");
        CENTER = new UnitValue(50.0f, null, 6, true, 100, null, null, "center");
        TRAILING = new UnitValue(100.0f, null, 6, true, 100, null, null, "trailing");
        RIGHT = new UnitValue(100.0f, null, 6, true, 100, null, null, "right");
        BOTTOM = new UnitValue(100.0f, null, 6, false, 100, null, null, "bottom");
        LABEL = new UnitValue(0.0f, null, 27, false, 100, null, null, "label");
        INF = new UnitValue(2097051.0f, null, 0, true, 100, null, null, "inf");
        BASELINE_IDENTITY = new UnitValue(0.0f, null, -1, false, 100, null, null, "baseline");
        SCALE = new float[] { 25.4f, 2.54f, 1.0f, 0.0f, 72.0f };
        LayoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate() {
            @Override
            protected Expression instantiate(final Object o, final Encoder encoder) {
                final UnitValue unitValue = (UnitValue)o;
                if (unitValue.getConstraintString() == null) {
                    throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");
                }
                return new Expression(o, ConstraintParser.class, "parseUnitValueOrAlign", new Object[] { unitValue.getConstraintString(), unitValue.isHorizontal() ? Boolean.TRUE : Boolean.FALSE, null });
            }
        });
    }
}
