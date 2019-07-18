// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.util.HashMap;

public final class PlatformDefaults
{
    private static int DEF_H_UNIT;
    private static int DEF_V_UNIT;
    private static InCellGapProvider GAP_PROVIDER;
    private static volatile int MOD_COUNT;
    private static final UnitValue LPX4;
    private static final UnitValue LPX6;
    private static final UnitValue LPX7;
    private static final UnitValue LPX9;
    private static final UnitValue LPX10;
    private static final UnitValue LPX11;
    private static final UnitValue LPX12;
    private static final UnitValue LPX14;
    private static final UnitValue LPX16;
    private static final UnitValue LPX18;
    private static final UnitValue LPX20;
    private static final UnitValue LPY4;
    private static final UnitValue LPY6;
    private static final UnitValue LPY7;
    private static final UnitValue LPY9;
    private static final UnitValue LPY10;
    private static final UnitValue LPY11;
    private static final UnitValue LPY12;
    private static final UnitValue LPY14;
    private static final UnitValue LPY16;
    private static final UnitValue LPY18;
    private static final UnitValue LPY20;
    public static final int WINDOWS_XP = 0;
    public static final int MAC_OSX = 1;
    public static final int GNOME = 2;
    private static int CUR_PLAF;
    private static final UnitValue[] PANEL_INS;
    private static final UnitValue[] DIALOG_INS;
    private static String BUTTON_FORMAT;
    private static final HashMap<String, UnitValue> HOR_DEFS;
    private static final HashMap<String, UnitValue> VER_DEFS;
    private static BoundSize DEF_VGAP;
    private static BoundSize DEF_HGAP;
    static BoundSize RELATED_X;
    static BoundSize RELATED_Y;
    static BoundSize UNRELATED_X;
    static BoundSize UNRELATED_Y;
    private static UnitValue BUTT_WIDTH;
    private static Float horScale;
    private static Float verScale;
    public static final int BASE_FONT_SIZE = 100;
    public static final int BASE_SCALE_FACTOR = 101;
    public static final int BASE_REAL_PIXEL = 102;
    private static int LP_BASE;
    private static Integer BASE_DPI_FORCED;
    private static int BASE_DPI;
    private static boolean dra;
    
    public static int getCurrentPlatform() {
        final String property = System.getProperty("os.name");
        if (property.startsWith("Mac OS")) {
            return 1;
        }
        if (property.startsWith("Linux")) {
            return 2;
        }
        return 0;
    }
    
    private PlatformDefaults() {
    }
    
    public static void setPlatform(final int cur_PLAF) {
        switch (cur_PLAF) {
            case 0: {
                setRelatedGap(PlatformDefaults.LPX4, PlatformDefaults.LPY4);
                setUnrelatedGap(PlatformDefaults.LPX7, PlatformDefaults.LPY9);
                setParagraphGap(PlatformDefaults.LPX14, PlatformDefaults.LPY14);
                setIndentGap(PlatformDefaults.LPX9, PlatformDefaults.LPY9);
                setGridCellGap(PlatformDefaults.LPX4, PlatformDefaults.LPY4);
                setMinimumButtonWidth(new UnitValue(75.0f, 1, null));
                setButtonOrder("L_E+U+YNBXOCAH_R");
                setDialogInsets(PlatformDefaults.LPY11, PlatformDefaults.LPX11, PlatformDefaults.LPY11, PlatformDefaults.LPX11);
                setPanelInsets(PlatformDefaults.LPY7, PlatformDefaults.LPX7, PlatformDefaults.LPY7, PlatformDefaults.LPX7);
                break;
            }
            case 1: {
                setRelatedGap(PlatformDefaults.LPX4, PlatformDefaults.LPY4);
                setUnrelatedGap(PlatformDefaults.LPX7, PlatformDefaults.LPY9);
                setParagraphGap(PlatformDefaults.LPX14, PlatformDefaults.LPY14);
                setIndentGap(PlatformDefaults.LPX10, PlatformDefaults.LPY10);
                setGridCellGap(PlatformDefaults.LPX4, PlatformDefaults.LPY4);
                setMinimumButtonWidth(new UnitValue(68.0f, 1, null));
                setButtonOrder("L_HE+U+NYBXCOA_R");
                setDialogInsets(PlatformDefaults.LPY14, PlatformDefaults.LPX20, PlatformDefaults.LPY20, PlatformDefaults.LPX20);
                setPanelInsets(PlatformDefaults.LPY16, PlatformDefaults.LPX16, PlatformDefaults.LPY16, PlatformDefaults.LPX16);
                break;
            }
            case 2: {
                setRelatedGap(PlatformDefaults.LPX6, PlatformDefaults.LPY6);
                setUnrelatedGap(PlatformDefaults.LPX12, PlatformDefaults.LPY12);
                setParagraphGap(PlatformDefaults.LPX18, PlatformDefaults.LPY18);
                setIndentGap(PlatformDefaults.LPX12, PlatformDefaults.LPY12);
                setGridCellGap(PlatformDefaults.LPX6, PlatformDefaults.LPY6);
                setMinimumButtonWidth(new UnitValue(85.0f, 1, null));
                setButtonOrder("L_HE+UNYACBXIO_R");
                setDialogInsets(PlatformDefaults.LPY12, PlatformDefaults.LPX12, PlatformDefaults.LPY12, PlatformDefaults.LPX12);
                setPanelInsets(PlatformDefaults.LPY6, PlatformDefaults.LPX6, PlatformDefaults.LPY6, PlatformDefaults.LPX6);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown platform: " + cur_PLAF);
            }
        }
        PlatformDefaults.CUR_PLAF = cur_PLAF;
        PlatformDefaults.BASE_DPI = ((PlatformDefaults.BASE_DPI_FORCED != null) ? PlatformDefaults.BASE_DPI_FORCED : getPlatformDPI(cur_PLAF));
    }
    
    private static int getPlatformDPI(final int n) {
        switch (n) {
            case 0:
            case 2: {
                return 96;
            }
            case 1: {
                try {
                    return (System.getProperty("java.version").compareTo("1.6") < 0) ? 72 : 96;
                }
                catch (Throwable t) {
                    return 72;
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unknown platform: " + n);
    }
    
    public static int getPlatform() {
        return PlatformDefaults.CUR_PLAF;
    }
    
    public static int getDefaultDPI() {
        return PlatformDefaults.BASE_DPI;
    }
    
    public static void setDefaultDPI(final Integer base_DPI_FORCED) {
        PlatformDefaults.BASE_DPI = ((base_DPI_FORCED != null) ? base_DPI_FORCED : getPlatformDPI(PlatformDefaults.CUR_PLAF));
        PlatformDefaults.BASE_DPI_FORCED = base_DPI_FORCED;
    }
    
    public static Float getHorizontalScaleFactor() {
        return PlatformDefaults.horScale;
    }
    
    public static void setHorizontalScaleFactor(final Float horScale) {
        if (!LayoutUtil.equals(PlatformDefaults.horScale, horScale)) {
            PlatformDefaults.horScale = horScale;
            ++PlatformDefaults.MOD_COUNT;
        }
    }
    
    public static Float getVerticalScaleFactor() {
        return PlatformDefaults.verScale;
    }
    
    public static void setVerticalScaleFactor(final Float verScale) {
        if (!LayoutUtil.equals(PlatformDefaults.verScale, verScale)) {
            PlatformDefaults.verScale = verScale;
            ++PlatformDefaults.MOD_COUNT;
        }
    }
    
    public static int getLogicalPixelBase() {
        return PlatformDefaults.LP_BASE;
    }
    
    public static void setLogicalPixelBase(final int lp_BASE) {
        if (PlatformDefaults.LP_BASE != lp_BASE) {
            if (lp_BASE < 100 || lp_BASE > 101) {
                throw new IllegalArgumentException("Unrecognized base: " + lp_BASE);
            }
            PlatformDefaults.LP_BASE = lp_BASE;
            ++PlatformDefaults.MOD_COUNT;
        }
    }
    
    public static void setRelatedGap(final UnitValue unitValue, final UnitValue unitValue2) {
        setUnitValue(new String[] { "r", "rel", "related" }, unitValue, unitValue2);
        PlatformDefaults.RELATED_X = new BoundSize(unitValue, unitValue, null, "rel:rel");
        PlatformDefaults.RELATED_Y = new BoundSize(unitValue2, unitValue2, null, "rel:rel");
    }
    
    public static void setUnrelatedGap(final UnitValue unitValue, final UnitValue unitValue2) {
        setUnitValue(new String[] { "u", "unrel", "unrelated" }, unitValue, unitValue2);
        PlatformDefaults.UNRELATED_X = new BoundSize(unitValue, unitValue, null, "unrel:unrel");
        PlatformDefaults.UNRELATED_Y = new BoundSize(unitValue2, unitValue2, null, "unrel:unrel");
    }
    
    public static void setParagraphGap(final UnitValue unitValue, final UnitValue unitValue2) {
        setUnitValue(new String[] { "p", "para", "paragraph" }, unitValue, unitValue2);
    }
    
    public static void setIndentGap(final UnitValue unitValue, final UnitValue unitValue2) {
        setUnitValue(new String[] { "i", "ind", "indent" }, unitValue, unitValue2);
    }
    
    public static void setGridCellGap(final UnitValue unitValue, final UnitValue unitValue2) {
        if (unitValue != null) {
            PlatformDefaults.DEF_HGAP = new BoundSize(unitValue, unitValue, null, null);
        }
        if (unitValue2 != null) {
            PlatformDefaults.DEF_VGAP = new BoundSize(unitValue2, unitValue2, null, null);
        }
        ++PlatformDefaults.MOD_COUNT;
    }
    
    public static void setMinimumButtonWidth(final UnitValue butt_WIDTH) {
        PlatformDefaults.BUTT_WIDTH = butt_WIDTH;
        ++PlatformDefaults.MOD_COUNT;
    }
    
    public static UnitValue getMinimumButtonWidth() {
        return PlatformDefaults.BUTT_WIDTH;
    }
    
    public static UnitValue getUnitValueX(final String s) {
        return PlatformDefaults.HOR_DEFS.get(s);
    }
    
    public static UnitValue getUnitValueY(final String s) {
        return PlatformDefaults.VER_DEFS.get(s);
    }
    
    public static final void setUnitValue(final String[] array, final UnitValue unitValue, final UnitValue unitValue2) {
        for (int i = 0; i < array.length; ++i) {
            final String trim = array[i].toLowerCase().trim();
            if (unitValue != null) {
                PlatformDefaults.HOR_DEFS.put(trim, unitValue);
            }
            if (unitValue2 != null) {
                PlatformDefaults.VER_DEFS.put(trim, unitValue2);
            }
        }
        ++PlatformDefaults.MOD_COUNT;
    }
    
    static final int convertToPixels(final float n, final String s, final boolean b, final float n2, final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper) {
        final UnitValue unitValue = (b ? PlatformDefaults.HOR_DEFS : PlatformDefaults.VER_DEFS).get(s);
        return (unitValue != null) ? Math.round(n * unitValue.getPixels(n2, containerWrapper, componentWrapper)) : -87654312;
    }
    
    public static final String getButtonOrder() {
        return PlatformDefaults.BUTTON_FORMAT;
    }
    
    public static final void setButtonOrder(final String button_FORMAT) {
        PlatformDefaults.BUTTON_FORMAT = button_FORMAT;
        ++PlatformDefaults.MOD_COUNT;
    }
    
    static final String getTagForChar(final char c) {
        switch (c) {
            case 'o': {
                return "ok";
            }
            case 'c': {
                return "cancel";
            }
            case 'h': {
                return "help";
            }
            case 'e': {
                return "help2";
            }
            case 'y': {
                return "yes";
            }
            case 'n': {
                return "no";
            }
            case 'a': {
                return "apply";
            }
            case 'x': {
                return "next";
            }
            case 'b': {
                return "back";
            }
            case 'i': {
                return "finish";
            }
            case 'l': {
                return "left";
            }
            case 'r': {
                return "right";
            }
            case 'u': {
                return "other";
            }
            default: {
                return null;
            }
        }
    }
    
    public static BoundSize getGridGapX() {
        return PlatformDefaults.DEF_HGAP;
    }
    
    public static BoundSize getGridGapY() {
        return PlatformDefaults.DEF_VGAP;
    }
    
    public static UnitValue getDialogInsets(final int n) {
        return PlatformDefaults.DIALOG_INS[n];
    }
    
    public static void setDialogInsets(final UnitValue unitValue, final UnitValue unitValue2, final UnitValue unitValue3, final UnitValue unitValue4) {
        if (unitValue != null) {
            PlatformDefaults.DIALOG_INS[0] = unitValue;
        }
        if (unitValue2 != null) {
            PlatformDefaults.DIALOG_INS[1] = unitValue2;
        }
        if (unitValue3 != null) {
            PlatformDefaults.DIALOG_INS[2] = unitValue3;
        }
        if (unitValue4 != null) {
            PlatformDefaults.DIALOG_INS[3] = unitValue4;
        }
        ++PlatformDefaults.MOD_COUNT;
    }
    
    public static UnitValue getPanelInsets(final int n) {
        return PlatformDefaults.PANEL_INS[n];
    }
    
    public static void setPanelInsets(final UnitValue unitValue, final UnitValue unitValue2, final UnitValue unitValue3, final UnitValue unitValue4) {
        if (unitValue != null) {
            PlatformDefaults.PANEL_INS[0] = unitValue;
        }
        if (unitValue2 != null) {
            PlatformDefaults.PANEL_INS[1] = unitValue2;
        }
        if (unitValue3 != null) {
            PlatformDefaults.PANEL_INS[2] = unitValue3;
        }
        if (unitValue4 != null) {
            PlatformDefaults.PANEL_INS[3] = unitValue4;
        }
        ++PlatformDefaults.MOD_COUNT;
    }
    
    public static float getLabelAlignPercentage() {
        return (PlatformDefaults.CUR_PLAF == 1) ? 1.0f : 0.0f;
    }
    
    static BoundSize getDefaultComponentGap(final ComponentWrapper componentWrapper, final ComponentWrapper componentWrapper2, final int n, final String s, final boolean b) {
        if (PlatformDefaults.GAP_PROVIDER != null) {
            return PlatformDefaults.GAP_PROVIDER.getDefaultGap(componentWrapper, componentWrapper2, n, s, b);
        }
        if (componentWrapper2 == null) {
            return null;
        }
        return (n == 2 || n == 4) ? PlatformDefaults.RELATED_X : PlatformDefaults.RELATED_Y;
    }
    
    public static InCellGapProvider getGapProvider() {
        return PlatformDefaults.GAP_PROVIDER;
    }
    
    public static void setGapProvider(final InCellGapProvider gap_PROVIDER) {
        PlatformDefaults.GAP_PROVIDER = gap_PROVIDER;
    }
    
    public static int getModCount() {
        return PlatformDefaults.MOD_COUNT;
    }
    
    public void invalidate() {
        ++PlatformDefaults.MOD_COUNT;
    }
    
    public static final int getDefaultHorizontalUnit() {
        return PlatformDefaults.DEF_H_UNIT;
    }
    
    public static final void setDefaultHorizontalUnit(final int def_H_UNIT) {
        if (def_H_UNIT < 0 || def_H_UNIT > 27) {
            throw new IllegalArgumentException("Illegal Unit: " + def_H_UNIT);
        }
        if (PlatformDefaults.DEF_H_UNIT != def_H_UNIT) {
            PlatformDefaults.DEF_H_UNIT = def_H_UNIT;
            ++PlatformDefaults.MOD_COUNT;
        }
    }
    
    public static final int getDefaultVerticalUnit() {
        return PlatformDefaults.DEF_V_UNIT;
    }
    
    public static final void setDefaultVerticalUnit(final int def_V_UNIT) {
        if (def_V_UNIT < 0 || def_V_UNIT > 27) {
            throw new IllegalArgumentException("Illegal Unit: " + def_V_UNIT);
        }
        if (PlatformDefaults.DEF_V_UNIT != def_V_UNIT) {
            PlatformDefaults.DEF_V_UNIT = def_V_UNIT;
            ++PlatformDefaults.MOD_COUNT;
        }
    }
    
    public static boolean getDefaultRowAlignmentBaseline() {
        return PlatformDefaults.dra;
    }
    
    public static void setDefaultRowAlignmentBaseline(final boolean dra) {
        PlatformDefaults.dra = dra;
    }
    
    static {
        PlatformDefaults.DEF_H_UNIT = 1;
        PlatformDefaults.DEF_V_UNIT = 2;
        PlatformDefaults.GAP_PROVIDER = null;
        PlatformDefaults.MOD_COUNT = 0;
        LPX4 = new UnitValue(4.0f, 1, null);
        LPX6 = new UnitValue(6.0f, 1, null);
        LPX7 = new UnitValue(7.0f, 1, null);
        LPX9 = new UnitValue(9.0f, 1, null);
        LPX10 = new UnitValue(10.0f, 1, null);
        LPX11 = new UnitValue(11.0f, 1, null);
        LPX12 = new UnitValue(12.0f, 1, null);
        LPX14 = new UnitValue(14.0f, 1, null);
        LPX16 = new UnitValue(16.0f, 1, null);
        LPX18 = new UnitValue(18.0f, 1, null);
        LPX20 = new UnitValue(20.0f, 1, null);
        LPY4 = new UnitValue(4.0f, 2, null);
        LPY6 = new UnitValue(6.0f, 2, null);
        LPY7 = new UnitValue(7.0f, 2, null);
        LPY9 = new UnitValue(9.0f, 2, null);
        LPY10 = new UnitValue(10.0f, 2, null);
        LPY11 = new UnitValue(11.0f, 2, null);
        LPY12 = new UnitValue(12.0f, 2, null);
        LPY14 = new UnitValue(14.0f, 2, null);
        LPY16 = new UnitValue(16.0f, 2, null);
        LPY18 = new UnitValue(18.0f, 2, null);
        LPY20 = new UnitValue(20.0f, 2, null);
        PlatformDefaults.CUR_PLAF = 0;
        PANEL_INS = new UnitValue[4];
        DIALOG_INS = new UnitValue[4];
        PlatformDefaults.BUTTON_FORMAT = null;
        HOR_DEFS = new HashMap<String, UnitValue>(32);
        VER_DEFS = new HashMap<String, UnitValue>(32);
        PlatformDefaults.DEF_VGAP = null;
        PlatformDefaults.DEF_HGAP = null;
        PlatformDefaults.RELATED_X = null;
        PlatformDefaults.RELATED_Y = null;
        PlatformDefaults.UNRELATED_X = null;
        PlatformDefaults.UNRELATED_Y = null;
        PlatformDefaults.BUTT_WIDTH = null;
        PlatformDefaults.horScale = null;
        PlatformDefaults.verScale = null;
        PlatformDefaults.LP_BASE = 101;
        PlatformDefaults.BASE_DPI_FORCED = null;
        PlatformDefaults.BASE_DPI = 96;
        PlatformDefaults.dra = true;
        setPlatform(getCurrentPlatform());
        PlatformDefaults.MOD_COUNT = 0;
    }
}
