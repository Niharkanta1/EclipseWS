// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public final class ConstraintParser
{
    private ConstraintParser() {
    }
    
    public static LC parseLayoutConstraint(final String s) {
        final LC lc = new LC();
        if (s.length() == 0) {
            return lc;
        }
        final String[] trimmedTokens = toTrimmedTokens(s, ',');
        for (int i = 0; i < trimmedTokens.length; ++i) {
            final String s2 = trimmedTokens[i];
            if (s2 != null) {
                final int length = s2.length();
                if (length == 3 || length == 11) {
                    if (s2.equals("ltr") || s2.equals("rtl") || s2.equals("lefttoright") || s2.equals("righttoleft")) {
                        lc.setLeftToRight((s2.charAt(0) == 'l') ? Boolean.TRUE : Boolean.FALSE);
                        trimmedTokens[i] = null;
                    }
                    if (s2.equals("ttb") || s2.equals("btt") || s2.equals("toptobottom") || s2.equals("bottomtotop")) {
                        lc.setTopToBottom(s2.charAt(0) == 't');
                        trimmedTokens[i] = null;
                    }
                }
            }
        }
        for (int j = 0; j < trimmedTokens.length; ++j) {
            final String s3 = trimmedTokens[j];
            if (s3 != null) {
                if (s3.length() != 0) {
                    try {
                        final char char1 = s3.charAt(0);
                        if (char1 == 'w' || char1 == 'h') {
                            final int startsWithLenient = startsWithLenient(s3, "wrap", -1, true);
                            if (startsWithLenient > -1) {
                                final String trim = s3.substring(startsWithLenient).trim();
                                lc.setWrapAfter((trim.length() != 0) ? Integer.parseInt(trim) : 0);
                                continue;
                            }
                            final boolean b = char1 == 'w';
                            if (b && (s3.startsWith("w ") || s3.startsWith("width "))) {
                                lc.setWidth(parseBoundSize(s3.substring((s3.charAt(1) == ' ') ? 2 : 6).trim(), false, true));
                                continue;
                            }
                            if (!b && (s3.startsWith("h ") || s3.startsWith("height "))) {
                                lc.setHeight(parseBoundSize(s3.substring((s3.charAt(1) == ' ') ? 2 : 7).trim(), false, false));
                                continue;
                            }
                            if (s3.length() > 5) {
                                final String trim2 = s3.substring(5).trim();
                                if (s3.startsWith("wmin ")) {
                                    lc.minWidth(trim2);
                                    continue;
                                }
                                if (s3.startsWith("wmax ")) {
                                    lc.maxWidth(trim2);
                                    continue;
                                }
                                if (s3.startsWith("hmin ")) {
                                    lc.minHeight(trim2);
                                    continue;
                                }
                                if (s3.startsWith("hmax ")) {
                                    lc.maxHeight(trim2);
                                    continue;
                                }
                            }
                            if (s3.startsWith("hidemode ")) {
                                lc.setHideMode(Integer.parseInt(s3.substring(9)));
                                continue;
                            }
                        }
                        if (char1 == 'g') {
                            if (s3.startsWith("gapx ")) {
                                lc.setGridGapX(parseBoundSize(s3.substring(5).trim(), true, true));
                                continue;
                            }
                            if (s3.startsWith("gapy ")) {
                                lc.setGridGapY(parseBoundSize(s3.substring(5).trim(), true, false));
                                continue;
                            }
                            if (s3.startsWith("gap ")) {
                                final String[] trimmedTokens2 = toTrimmedTokens(s3.substring(4).trim(), ' ');
                                lc.setGridGapX(parseBoundSize(trimmedTokens2[0], true, true));
                                lc.setGridGapY((trimmedTokens2.length > 1) ? parseBoundSize(trimmedTokens2[1], true, false) : lc.getGridGapX());
                                continue;
                            }
                        }
                        if (char1 == 'd') {
                            final int startsWithLenient2 = startsWithLenient(s3, "debug", 5, true);
                            if (startsWithLenient2 > -1) {
                                final String trim3 = s3.substring(startsWithLenient2).trim();
                                lc.setDebugMillis((trim3.length() > 0) ? Integer.parseInt(trim3) : 1000);
                                continue;
                            }
                        }
                        if (char1 == 'n') {
                            if (s3.equals("nogrid")) {
                                lc.setNoGrid(true);
                                continue;
                            }
                            if (s3.equals("nocache")) {
                                lc.setNoCache(true);
                                continue;
                            }
                            if (s3.equals("novisualpadding")) {
                                lc.setVisualPadding(false);
                                continue;
                            }
                        }
                        if (char1 == 'f') {
                            if (s3.equals("fill") || s3.equals("fillx") || s3.equals("filly")) {
                                lc.setFillX(s3.length() == 4 || s3.charAt(4) == 'x');
                                lc.setFillY(s3.length() == 4 || s3.charAt(4) == 'y');
                                continue;
                            }
                            if (s3.equals("flowy")) {
                                lc.setFlowX(false);
                                continue;
                            }
                            if (s3.equals("flowx")) {
                                lc.setFlowX(true);
                                continue;
                            }
                        }
                        if (char1 == 'i') {
                            final int startsWithLenient3 = startsWithLenient(s3, "insets", 3, true);
                            if (startsWithLenient3 > -1) {
                                final String trim4 = s3.substring(startsWithLenient3).trim();
                                final UnitValue[] insets = parseInsets(trim4, true);
                                LayoutUtil.putCCString(insets, trim4);
                                lc.setInsets(insets);
                                continue;
                            }
                        }
                        if (char1 == 'a') {
                            final int startsWithLenient4 = startsWithLenient(s3, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
                            if (startsWithLenient4 > -1) {
                                final UnitValue unitValueOrAlign = parseUnitValueOrAlign(s3.substring(startsWithLenient4).trim(), false, null);
                                if (unitValueOrAlign == UnitValue.BASELINE_IDENTITY) {
                                    throw new IllegalArgumentException("'baseline' can not be used to align the whole component group.");
                                }
                                lc.setAlignY(unitValueOrAlign);
                                continue;
                            }
                            else {
                                final int startsWithLenient5 = startsWithLenient(s3, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
                                if (startsWithLenient5 > -1) {
                                    lc.setAlignX(parseUnitValueOrAlign(s3.substring(startsWithLenient5).trim(), true, null));
                                    continue;
                                }
                                final int startsWithLenient6 = startsWithLenient(s3, "align", 2, true);
                                if (startsWithLenient6 > -1) {
                                    final String[] trimmedTokens3 = toTrimmedTokens(s3.substring(startsWithLenient6).trim(), ' ');
                                    lc.setAlignX(parseUnitValueOrAlign(trimmedTokens3[0], true, null));
                                    lc.setAlignY((trimmedTokens3.length > 1) ? parseUnitValueOrAlign(trimmedTokens3[1], false, null) : lc.getAlignX());
                                    continue;
                                }
                            }
                        }
                        if (char1 == 'p') {
                            if (s3.startsWith("packalign ")) {
                                final String[] trimmedTokens4 = toTrimmedTokens(s3.substring(10).trim(), ' ');
                                lc.setPackWidthAlign((trimmedTokens4[0].length() > 0) ? Float.parseFloat(trimmedTokens4[0]) : 0.5f);
                                if (trimmedTokens4.length > 1) {
                                    lc.setPackHeightAlign(Float.parseFloat(trimmedTokens4[1]));
                                }
                                continue;
                            }
                            if (s3.startsWith("pack ") || s3.equals("pack")) {
                                final String trim5 = s3.substring(4).trim();
                                final String[] trimmedTokens5 = toTrimmedTokens((trim5.length() > 0) ? trim5 : "pref pref", ' ');
                                lc.setPackWidth(parseBoundSize(trimmedTokens5[0], false, true));
                                if (trimmedTokens5.length > 1) {
                                    lc.setPackHeight(parseBoundSize(trimmedTokens5[1], false, false));
                                }
                                continue;
                            }
                        }
                        if (lc.getAlignX() == null) {
                            final UnitValue alignKeywords = parseAlignKeywords(s3, true);
                            if (alignKeywords != null) {
                                lc.setAlignX(alignKeywords);
                                continue;
                            }
                        }
                        final UnitValue alignKeywords2 = parseAlignKeywords(s3, false);
                        if (alignKeywords2 == null) {
                            throw new IllegalArgumentException("Unknown Constraint: '" + s3 + "'\n");
                        }
                        lc.setAlignY(alignKeywords2);
                    }
                    catch (Exception ex) {
                        throw new IllegalArgumentException("Illegal Constraint: '" + s3 + "'\n" + ex.getMessage());
                    }
                }
            }
        }
        return lc;
    }
    
    public static AC parseRowConstraints(final String s) {
        return parseAxisConstraint(s, false);
    }
    
    public static AC parseColumnConstraints(final String s) {
        return parseAxisConstraint(s, true);
    }
    
    private static AC parseAxisConstraint(String s, final boolean b) {
        s = s.trim();
        if (s.length() == 0) {
            return new AC();
        }
        s = s.toLowerCase();
        final ArrayList<String> rowColAndGapsTrimmed = getRowColAndGapsTrimmed(s);
        final BoundSize[] array = new BoundSize[(rowColAndGapsTrimmed.size() >> 1) + 1];
        for (int i = 0, size = rowColAndGapsTrimmed.size(), n = 0; i < size; i += 2, ++n) {
            array[n] = parseBoundSize(rowColAndGapsTrimmed.get(i), true, b);
        }
        final DimConstraint[] constaints = new DimConstraint[rowColAndGapsTrimmed.size() >> 1];
        for (int j = 0, n2 = 0; j < constaints.length; ++j, ++n2) {
            if (n2 >= array.length - 1) {
                n2 = array.length - 2;
            }
            constaints[j] = parseDimConstraint(rowColAndGapsTrimmed.get((j << 1) + 1), array[n2], array[n2 + 1], b);
        }
        final AC ac = new AC();
        ac.setConstaints(constaints);
        return ac;
    }
    
    private static DimConstraint parseDimConstraint(final String s, final BoundSize gapBefore, final BoundSize gapAfter, final boolean b) {
        final DimConstraint dimConstraint = new DimConstraint();
        dimConstraint.setGapBefore(gapBefore);
        dimConstraint.setGapAfter(gapAfter);
        final String[] trimmedTokens = toTrimmedTokens(s, ',');
        for (int i = 0; i < trimmedTokens.length; ++i) {
            final String s2 = trimmedTokens[i];
            try {
                if (s2.length() != 0) {
                    if (s2.equals("fill")) {
                        dimConstraint.setFill(true);
                    }
                    else if (s2.equals("nogrid")) {
                        dimConstraint.setNoGrid(true);
                    }
                    else {
                        final char char1 = s2.charAt(0);
                        if (char1 == 's') {
                            final int startsWithLenient = startsWithLenient(s2, new String[] { "sizegroup", "sg" }, new int[] { 5, 2 }, true);
                            if (startsWithLenient > -1) {
                                dimConstraint.setSizeGroup(s2.substring(startsWithLenient).trim());
                                continue;
                            }
                            final int startsWithLenient2 = startsWithLenient(s2, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
                            if (startsWithLenient2 > -1) {
                                dimConstraint.setShrinkPriority(Integer.parseInt(s2.substring(startsWithLenient2).trim()));
                                continue;
                            }
                            final int startsWithLenient3 = startsWithLenient(s2, "shrink", 6, true);
                            if (startsWithLenient3 > -1) {
                                dimConstraint.setShrink(parseFloat(s2.substring(startsWithLenient3).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                        }
                        if (char1 == 'g') {
                            final int startsWithLenient4 = startsWithLenient(s2, new String[] { "growpriority", "gp" }, new int[] { 5, 2 }, true);
                            if (startsWithLenient4 > -1) {
                                dimConstraint.setGrowPriority(Integer.parseInt(s2.substring(startsWithLenient4).trim()));
                                continue;
                            }
                            final int startsWithLenient5 = startsWithLenient(s2, "grow", 4, true);
                            if (startsWithLenient5 > -1) {
                                dimConstraint.setGrow(parseFloat(s2.substring(startsWithLenient5).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                        }
                        if (char1 == 'a') {
                            final int startsWithLenient6 = startsWithLenient(s2, "align", 2, true);
                            if (startsWithLenient6 > -1) {
                                dimConstraint.setAlign(parseUnitValueOrAlign(s2.substring(startsWithLenient6).trim(), b, null));
                                continue;
                            }
                        }
                        final UnitValue alignKeywords = parseAlignKeywords(s2, b);
                        if (alignKeywords != null) {
                            dimConstraint.setAlign(alignKeywords);
                        }
                        else {
                            dimConstraint.setSize(parseBoundSize(s2, false, b));
                        }
                    }
                }
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Illegal contraint: '" + s2 + "'\n" + ex.getMessage());
            }
        }
        return dimConstraint;
    }
    
    public static Map<ComponentWrapper, CC> parseComponentConstraints(final Map<ComponentWrapper, String> map) {
        final HashMap<ComponentWrapper, CC> hashMap = new HashMap<ComponentWrapper, CC>();
        for (final Map.Entry<ComponentWrapper, String> entry : map.entrySet()) {
            hashMap.put(entry.getKey(), parseComponentConstraint(entry.getValue()));
        }
        return hashMap;
    }
    
    public static CC parseComponentConstraint(final String s) {
        final CC cc = new CC();
        if (s.length() == 0) {
            return cc;
        }
        final String[] trimmedTokens = toTrimmedTokens(s, ',');
        for (int i = 0; i < trimmedTokens.length; ++i) {
            final String s2 = trimmedTokens[i];
            try {
                if (s2.length() != 0) {
                    final char char1 = s2.charAt(0);
                    if (char1 == 'n') {
                        if (s2.equals("north")) {
                            cc.setDockSide(0);
                            continue;
                        }
                        if (s2.equals("newline")) {
                            cc.setNewline(true);
                            continue;
                        }
                        if (s2.startsWith("newline ")) {
                            cc.setNewlineGapSize(parseBoundSize(s2.substring(7).trim(), true, true));
                            continue;
                        }
                    }
                    if (char1 == 'f' && (s2.equals("flowy") || s2.equals("flowx"))) {
                        cc.setFlowX((s2.charAt(4) == 'x') ? Boolean.TRUE : Boolean.FALSE);
                    }
                    else {
                        if (char1 == 's') {
                            final int startsWithLenient = startsWithLenient(s2, "skip", 4, true);
                            if (startsWithLenient > -1) {
                                final String trim = s2.substring(startsWithLenient).trim();
                                cc.setSkip((trim.length() != 0) ? Integer.parseInt(trim) : 1);
                                continue;
                            }
                            final int startsWithLenient2 = startsWithLenient(s2, "split", 5, true);
                            if (startsWithLenient2 > -1) {
                                final String trim2 = s2.substring(startsWithLenient2).trim();
                                cc.setSplit((trim2.length() > 0) ? Integer.parseInt(trim2) : 2097051);
                                continue;
                            }
                            if (s2.equals("south")) {
                                cc.setDockSide(2);
                                continue;
                            }
                            final int startsWithLenient3 = startsWithLenient(s2, new String[] { "spany", "sy" }, new int[] { 5, 2 }, true);
                            if (startsWithLenient3 > -1) {
                                cc.setSpanY(parseSpan(s2.substring(startsWithLenient3).trim()));
                                continue;
                            }
                            final int startsWithLenient4 = startsWithLenient(s2, new String[] { "spanx", "sx" }, new int[] { 5, 2 }, true);
                            if (startsWithLenient4 > -1) {
                                cc.setSpanX(parseSpan(s2.substring(startsWithLenient4).trim()));
                                continue;
                            }
                            final int startsWithLenient5 = startsWithLenient(s2, "span", 4, true);
                            if (startsWithLenient5 > -1) {
                                final String[] trimmedTokens2 = toTrimmedTokens(s2.substring(startsWithLenient5).trim(), ' ');
                                cc.setSpanX((trimmedTokens2[0].length() > 0) ? Integer.parseInt(trimmedTokens2[0]) : 2097051);
                                cc.setSpanY((trimmedTokens2.length > 1) ? Integer.parseInt(trimmedTokens2[1]) : 1);
                                continue;
                            }
                            final int startsWithLenient6 = startsWithLenient(s2, "shrinkx", 7, true);
                            if (startsWithLenient6 > -1) {
                                cc.getHorizontal().setShrink(parseFloat(s2.substring(startsWithLenient6).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                            final int startsWithLenient7 = startsWithLenient(s2, "shrinky", 7, true);
                            if (startsWithLenient7 > -1) {
                                cc.getVertical().setShrink(parseFloat(s2.substring(startsWithLenient7).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                            final int startsWithLenient8 = startsWithLenient(s2, "shrink", 6, false);
                            if (startsWithLenient8 > -1) {
                                final String[] trimmedTokens3 = toTrimmedTokens(s2.substring(startsWithLenient8).trim(), ' ');
                                cc.getHorizontal().setShrink(parseFloat(s2.substring(startsWithLenient8).trim(), ResizeConstraint.WEIGHT_100));
                                if (trimmedTokens3.length > 1) {
                                    cc.getVertical().setShrink(parseFloat(s2.substring(startsWithLenient8).trim(), ResizeConstraint.WEIGHT_100));
                                }
                                continue;
                            }
                            final int startsWithLenient9 = startsWithLenient(s2, new String[] { "shrinkprio", "shp" }, new int[] { 10, 3 }, true);
                            if (startsWithLenient9 > -1) {
                                final String trim3 = s2.substring(startsWithLenient9).trim();
                                if (trim3.startsWith("x") || trim3.startsWith("y")) {
                                    (trim3.startsWith("x") ? cc.getHorizontal() : cc.getVertical()).setShrinkPriority(Integer.parseInt(trim3.substring(2)));
                                }
                                else {
                                    final String[] trimmedTokens4 = toTrimmedTokens(trim3, ' ');
                                    cc.getHorizontal().setShrinkPriority(Integer.parseInt(trimmedTokens4[0]));
                                    if (trimmedTokens4.length > 1) {
                                        cc.getVertical().setShrinkPriority(Integer.parseInt(trimmedTokens4[1]));
                                    }
                                }
                                continue;
                            }
                            final int startsWithLenient10 = startsWithLenient(s2, new String[] { "sizegroupx", "sizegroupy", "sgx", "sgy" }, new int[] { 9, 9, 2, 2 }, true);
                            if (startsWithLenient10 > -1) {
                                final String trim4 = s2.substring(startsWithLenient10).trim();
                                final char char2 = s2.charAt(startsWithLenient10 - 1);
                                if (char2 != 'y') {
                                    cc.getHorizontal().setSizeGroup(trim4);
                                }
                                if (char2 != 'x') {
                                    cc.getVertical().setSizeGroup(trim4);
                                }
                                continue;
                            }
                        }
                        if (char1 == 'g') {
                            final int startsWithLenient11 = startsWithLenient(s2, "growx", 5, true);
                            if (startsWithLenient11 > -1) {
                                cc.getHorizontal().setGrow(parseFloat(s2.substring(startsWithLenient11).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                            final int startsWithLenient12 = startsWithLenient(s2, "growy", 5, true);
                            if (startsWithLenient12 > -1) {
                                cc.getVertical().setGrow(parseFloat(s2.substring(startsWithLenient12).trim(), ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                            final int startsWithLenient13 = startsWithLenient(s2, "grow", 4, false);
                            if (startsWithLenient13 > -1) {
                                final String[] trimmedTokens5 = toTrimmedTokens(s2.substring(startsWithLenient13).trim(), ' ');
                                cc.getHorizontal().setGrow(parseFloat(trimmedTokens5[0], ResizeConstraint.WEIGHT_100));
                                cc.getVertical().setGrow(parseFloat((trimmedTokens5.length > 1) ? trimmedTokens5[1] : "", ResizeConstraint.WEIGHT_100));
                                continue;
                            }
                            final int startsWithLenient14 = startsWithLenient(s2, new String[] { "growprio", "gp" }, new int[] { 8, 2 }, true);
                            if (startsWithLenient14 > -1) {
                                final String trim5 = s2.substring(startsWithLenient14).trim();
                                final char c = (trim5.length() > 0) ? trim5.charAt(0) : ' ';
                                if (c == 'x' || c == 'y') {
                                    ((c == 'x') ? cc.getHorizontal() : cc.getVertical()).setGrowPriority(Integer.parseInt(trim5.substring(2)));
                                }
                                else {
                                    final String[] trimmedTokens6 = toTrimmedTokens(trim5, ' ');
                                    cc.getHorizontal().setGrowPriority(Integer.parseInt(trimmedTokens6[0]));
                                    if (trimmedTokens6.length > 1) {
                                        cc.getVertical().setGrowPriority(Integer.parseInt(trimmedTokens6[1]));
                                    }
                                }
                                continue;
                            }
                            if (s2.startsWith("gap")) {
                                final BoundSize[] gaps = parseGaps(s2);
                                if (gaps[0] != null) {
                                    cc.getVertical().setGapBefore(gaps[0]);
                                }
                                if (gaps[1] != null) {
                                    cc.getHorizontal().setGapBefore(gaps[1]);
                                }
                                if (gaps[2] != null) {
                                    cc.getVertical().setGapAfter(gaps[2]);
                                }
                                if (gaps[3] != null) {
                                    cc.getHorizontal().setGapAfter(gaps[3]);
                                }
                                continue;
                            }
                        }
                        if (char1 == 'a') {
                            final int startsWithLenient15 = startsWithLenient(s2, new String[] { "aligny", "ay" }, new int[] { 6, 2 }, true);
                            if (startsWithLenient15 > -1) {
                                cc.getVertical().setAlign(parseUnitValueOrAlign(s2.substring(startsWithLenient15).trim(), false, null));
                                continue;
                            }
                            final int startsWithLenient16 = startsWithLenient(s2, new String[] { "alignx", "ax" }, new int[] { 6, 2 }, true);
                            if (startsWithLenient16 > -1) {
                                cc.getHorizontal().setAlign(parseUnitValueOrAlign(s2.substring(startsWithLenient16).trim(), true, null));
                                continue;
                            }
                            final int startsWithLenient17 = startsWithLenient(s2, "align", 2, true);
                            if (startsWithLenient17 > -1) {
                                final String[] trimmedTokens7 = toTrimmedTokens(s2.substring(startsWithLenient17).trim(), ' ');
                                cc.getHorizontal().setAlign(parseUnitValueOrAlign(trimmedTokens7[0], true, null));
                                if (trimmedTokens7.length > 1) {
                                    cc.getVertical().setAlign(parseUnitValueOrAlign(trimmedTokens7[1], false, null));
                                }
                                continue;
                            }
                        }
                        if ((char1 == 'x' || char1 == 'y') && s2.length() > 2) {
                            final char char3 = s2.charAt(1);
                            if (char3 == ' ' || (char3 == '2' && s2.charAt(2) == ' ')) {
                                if (cc.getPos() == null) {
                                    cc.setPos(new UnitValue[4]);
                                }
                                else if (!cc.isBoundsInGrid()) {
                                    throw new IllegalArgumentException("Cannot combine 'position' with 'x/y/x2/y2' keywords.");
                                }
                                final int n = ((char1 != 'x') ? 1 : 0) + ((char3 == '2') ? 2 : 0);
                                final UnitValue[] pos = cc.getPos();
                                pos[n] = parseUnitValue(s2.substring(2).trim(), null, char1 == 'x');
                                cc.setPos(pos);
                                cc.setBoundsInGrid(true);
                                continue;
                            }
                        }
                        if (char1 == 'c') {
                            final int startsWithLenient18 = startsWithLenient(s2, "cell", 4, true);
                            if (startsWithLenient18 > -1) {
                                final String[] trimmedTokens8 = toTrimmedTokens(s2.substring(startsWithLenient18).trim(), ' ');
                                if (trimmedTokens8.length < 2) {
                                    throw new IllegalArgumentException("At least two integers must follow " + s2);
                                }
                                cc.setCellX(Integer.parseInt(trimmedTokens8[0]));
                                cc.setCellY(Integer.parseInt(trimmedTokens8[1]));
                                if (trimmedTokens8.length > 2) {
                                    cc.setSpanX(Integer.parseInt(trimmedTokens8[2]));
                                }
                                if (trimmedTokens8.length > 3) {
                                    cc.setSpanY(Integer.parseInt(trimmedTokens8[3]));
                                }
                                continue;
                            }
                        }
                        if (char1 == 'p') {
                            final int startsWithLenient19 = startsWithLenient(s2, "pos", 3, true);
                            if (startsWithLenient19 > -1) {
                                if (cc.getPos() != null && cc.isBoundsInGrid()) {
                                    throw new IllegalArgumentException("Can not combine 'pos' with 'x/y/x2/y2' keywords.");
                                }
                                final String[] trimmedTokens9 = toTrimmedTokens(s2.substring(startsWithLenient19).trim(), ' ');
                                final UnitValue[] pos2 = new UnitValue[4];
                                for (int j = 0; j < trimmedTokens9.length; ++j) {
                                    pos2[j] = parseUnitValue(trimmedTokens9[j], null, j % 2 == 0);
                                }
                                if ((pos2[0] == null && pos2[2] == null) || (pos2[1] == null && pos2[3] == null)) {
                                    throw new IllegalArgumentException("Both x and x2 or y and y2 can not be null!");
                                }
                                cc.setPos(pos2);
                                cc.setBoundsInGrid(false);
                                continue;
                            }
                            else {
                                final int startsWithLenient20 = startsWithLenient(s2, "pad", 3, true);
                                if (startsWithLenient20 > -1) {
                                    final UnitValue[] insets = parseInsets(s2.substring(startsWithLenient20).trim(), false);
                                    cc.setPadding(new UnitValue[] { insets[0], (insets.length > 1) ? insets[1] : null, (insets.length > 2) ? insets[2] : null, (insets.length > 3) ? insets[3] : null });
                                    continue;
                                }
                                final int startsWithLenient21 = startsWithLenient(s2, "pushx", 5, true);
                                if (startsWithLenient21 > -1) {
                                    cc.setPushX(parseFloat(s2.substring(startsWithLenient21).trim(), ResizeConstraint.WEIGHT_100));
                                    continue;
                                }
                                final int startsWithLenient22 = startsWithLenient(s2, "pushy", 5, true);
                                if (startsWithLenient22 > -1) {
                                    cc.setPushY(parseFloat(s2.substring(startsWithLenient22).trim(), ResizeConstraint.WEIGHT_100));
                                    continue;
                                }
                                final int startsWithLenient23 = startsWithLenient(s2, "push", 4, false);
                                if (startsWithLenient23 > -1) {
                                    final String[] trimmedTokens10 = toTrimmedTokens(s2.substring(startsWithLenient23).trim(), ' ');
                                    cc.setPushX(parseFloat(trimmedTokens10[0], ResizeConstraint.WEIGHT_100));
                                    cc.setPushY(parseFloat((trimmedTokens10.length > 1) ? trimmedTokens10[1] : "", ResizeConstraint.WEIGHT_100));
                                    continue;
                                }
                            }
                        }
                        if (char1 == 't') {
                            final int startsWithLenient24 = startsWithLenient(s2, "tag", 3, true);
                            if (startsWithLenient24 > -1) {
                                cc.setTag(s2.substring(startsWithLenient24).trim());
                                continue;
                            }
                        }
                        if (char1 == 'w' || char1 == 'h') {
                            if (s2.equals("wrap")) {
                                cc.setWrap(true);
                                continue;
                            }
                            if (s2.startsWith("wrap ")) {
                                cc.setWrapGapSize(parseBoundSize(s2.substring(5).trim(), true, true));
                                continue;
                            }
                            final boolean b = char1 == 'w';
                            if (b && (s2.startsWith("w ") || s2.startsWith("width "))) {
                                cc.getHorizontal().setSize(parseBoundSize(s2.substring((s2.charAt(1) == ' ') ? 2 : 6).trim(), false, true));
                                continue;
                            }
                            if (!b && (s2.startsWith("h ") || s2.startsWith("height "))) {
                                cc.getVertical().setSize(parseBoundSize(s2.substring((s2.charAt(1) == ' ') ? 2 : 7).trim(), false, false));
                                continue;
                            }
                            if (s2.startsWith("wmin ") || s2.startsWith("wmax ") || s2.startsWith("hmin ") || s2.startsWith("hmax ")) {
                                final String trim6 = s2.substring(5).trim();
                                if (trim6.length() > 0) {
                                    final UnitValue unitValue = parseUnitValue(trim6, null, b);
                                    final boolean b2 = s2.charAt(3) == 'n';
                                    final DimConstraint dimConstraint = b ? cc.getHorizontal() : cc.getVertical();
                                    dimConstraint.setSize(new BoundSize(b2 ? unitValue : dimConstraint.getSize().getMin(), dimConstraint.getSize().getPreferred(), b2 ? dimConstraint.getSize().getMax() : unitValue, trim6));
                                    continue;
                                }
                            }
                            if (s2.equals("west")) {
                                cc.setDockSide(1);
                                continue;
                            }
                            if (s2.startsWith("hidemode ")) {
                                cc.setHideMode(Integer.parseInt(s2.substring(9)));
                                continue;
                            }
                        }
                        if (char1 == 'i' && s2.startsWith("id ")) {
                            cc.setId(s2.substring(3).trim());
                            final int index = cc.getId().indexOf(46);
                            if (index == 0 || index == cc.getId().length() - 1) {
                                throw new IllegalArgumentException("Dot must not be first or last!");
                            }
                        }
                        else {
                            if (char1 == 'e') {
                                if (s2.equals("east")) {
                                    cc.setDockSide(3);
                                    continue;
                                }
                                if (s2.equals("external")) {
                                    cc.setExternal(true);
                                    continue;
                                }
                                final int startsWithLenient25 = startsWithLenient(s2, new String[] { "endgroupx", "endgroupy", "egx", "egy" }, new int[] { -1, -1, -1, -1 }, true);
                                if (startsWithLenient25 > -1) {
                                    ((s2.charAt(startsWithLenient25 - 1) == 'x') ? cc.getHorizontal() : cc.getVertical()).setEndGroup(s2.substring(startsWithLenient25).trim());
                                    continue;
                                }
                            }
                            if (char1 == 'd') {
                                if (s2.equals("dock north")) {
                                    cc.setDockSide(0);
                                    continue;
                                }
                                if (s2.equals("dock west")) {
                                    cc.setDockSide(1);
                                    continue;
                                }
                                if (s2.equals("dock south")) {
                                    cc.setDockSide(2);
                                    continue;
                                }
                                if (s2.equals("dock east")) {
                                    cc.setDockSide(3);
                                    continue;
                                }
                                if (s2.equals("dock center")) {
                                    cc.getHorizontal().setGrow(new Float(100.0f));
                                    cc.getVertical().setGrow(new Float(100.0f));
                                    cc.setPushX(new Float(100.0f));
                                    cc.setPushY(new Float(100.0f));
                                    continue;
                                }
                            }
                            final UnitValue alignKeywords = parseAlignKeywords(s2, true);
                            if (alignKeywords != null) {
                                cc.getHorizontal().setAlign(alignKeywords);
                            }
                            else {
                                final UnitValue alignKeywords2 = parseAlignKeywords(s2, false);
                                if (alignKeywords2 == null) {
                                    throw new IllegalArgumentException("Unknown keyword.");
                                }
                                cc.getVertical().setAlign(alignKeywords2);
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Illegal Constraint: '" + s2 + "'\n" + ex.getMessage());
            }
        }
        return cc;
    }
    
    public static UnitValue[] parseInsets(final String s, final boolean b) {
        if (s.length() != 0 && !s.equals("dialog") && !s.equals("panel")) {
            final String[] trimmedTokens = toTrimmedTokens(s, ' ');
            final UnitValue[] array = new UnitValue[4];
            for (int i = 0; i < 4; ++i) {
                final UnitValue unitValue = parseUnitValue(trimmedTokens[(i < trimmedTokens.length) ? i : (trimmedTokens.length - 1)], UnitValue.ZERO, i % 2 == 1);
                array[i] = ((unitValue != null) ? unitValue : PlatformDefaults.getPanelInsets(i));
            }
            return array;
        }
        if (!b) {
            throw new IllegalAccessError("Insets now allowed: " + s + "\n");
        }
        final boolean startsWith = s.startsWith("p");
        final UnitValue[] array2 = new UnitValue[4];
        for (int j = 0; j < 4; ++j) {
            array2[j] = (startsWith ? PlatformDefaults.getPanelInsets(j) : PlatformDefaults.getDialogInsets(j));
        }
        return array2;
    }
    
    private static BoundSize[] parseGaps(String s) {
        final BoundSize[] array = new BoundSize[4];
        final int startsWithLenient = startsWithLenient(s, "gaptop", -1, true);
        if (startsWithLenient > -1) {
            s = s.substring(startsWithLenient).trim();
            array[0] = parseBoundSize(s, true, false);
            return array;
        }
        final int startsWithLenient2 = startsWithLenient(s, "gapleft", -1, true);
        if (startsWithLenient2 > -1) {
            s = s.substring(startsWithLenient2).trim();
            array[1] = parseBoundSize(s, true, true);
            return array;
        }
        final int startsWithLenient3 = startsWithLenient(s, "gapbottom", -1, true);
        if (startsWithLenient3 > -1) {
            s = s.substring(startsWithLenient3).trim();
            array[2] = parseBoundSize(s, true, false);
            return array;
        }
        final int startsWithLenient4 = startsWithLenient(s, "gapright", -1, true);
        if (startsWithLenient4 > -1) {
            s = s.substring(startsWithLenient4).trim();
            array[3] = parseBoundSize(s, true, true);
            return array;
        }
        final int startsWithLenient5 = startsWithLenient(s, "gapbefore", -1, true);
        if (startsWithLenient5 > -1) {
            s = s.substring(startsWithLenient5).trim();
            array[1] = parseBoundSize(s, true, true);
            return array;
        }
        final int startsWithLenient6 = startsWithLenient(s, "gapafter", -1, true);
        if (startsWithLenient6 > -1) {
            s = s.substring(startsWithLenient6).trim();
            array[3] = parseBoundSize(s, true, true);
            return array;
        }
        final int startsWithLenient7 = startsWithLenient(s, new String[] { "gapx", "gapy" }, null, true);
        if (startsWithLenient7 > -1) {
            final int n = (s.charAt(3) == 'x') ? 1 : 0;
            final String[] trimmedTokens = toTrimmedTokens(s.substring(startsWithLenient7).trim(), ' ');
            array[n] = parseBoundSize(trimmedTokens[0], true, (boolean)(n != 0));
            if (trimmedTokens.length > 1) {
                array[(n != 0) ? 3 : 2] = parseBoundSize(trimmedTokens[1], true, (boolean)(n == 0));
            }
            return array;
        }
        final int startsWithLenient8 = startsWithLenient(s, "gap ", 1, true);
        if (startsWithLenient8 > -1) {
            final String[] trimmedTokens2 = toTrimmedTokens(s.substring(startsWithLenient8).trim(), ' ');
            array[1] = parseBoundSize(trimmedTokens2[0], true, true);
            if (trimmedTokens2.length > 1) {
                array[3] = parseBoundSize(trimmedTokens2[1], true, false);
                if (trimmedTokens2.length > 2) {
                    array[0] = parseBoundSize(trimmedTokens2[2], true, true);
                    if (trimmedTokens2.length > 3) {
                        array[2] = parseBoundSize(trimmedTokens2[3], true, false);
                    }
                }
            }
            return array;
        }
        throw new IllegalArgumentException("Unknown Gap part: '" + s + "'");
    }
    
    private static int parseSpan(final String s) {
        return (s.length() > 0) ? Integer.parseInt(s) : 2097051;
    }
    
    private static Float parseFloat(final String s, final Float n) {
        return (s.length() > 0) ? new Float(Float.parseFloat(s)) : n;
    }
    
    public static BoundSize parseBoundSize(String substring, final boolean b, final boolean b2) {
        if (substring.length() == 0 || substring.equals("null") || substring.equals("n")) {
            return null;
        }
        final String s = substring;
        boolean b3 = false;
        if (substring.endsWith("push")) {
            b3 = true;
            substring = substring.substring(0, substring.length() - (substring.endsWith(":push") ? 5 : 4));
            if (substring.length() == 0) {
                return new BoundSize(null, null, null, true, s);
            }
        }
        final String[] trimmedTokens = toTrimmedTokens(substring, ':');
        String substring2 = trimmedTokens[0];
        if (trimmedTokens.length == 1) {
            final boolean endsWith = substring2.endsWith("!");
            if (endsWith) {
                substring2 = substring2.substring(0, substring2.length() - 1);
            }
            final UnitValue unitValue = parseUnitValue(substring2, null, b2);
            return new BoundSize((b || endsWith) ? unitValue : null, unitValue, endsWith ? unitValue : null, b3, s);
        }
        if (trimmedTokens.length == 2) {
            return new BoundSize(parseUnitValue(substring2, null, b2), parseUnitValue(trimmedTokens[1], null, b2), null, b3, s);
        }
        if (trimmedTokens.length == 3) {
            return new BoundSize(parseUnitValue(substring2, null, b2), parseUnitValue(trimmedTokens[1], null, b2), parseUnitValue(trimmedTokens[2], null, b2), b3, s);
        }
        throw new IllegalArgumentException("Min:Preferred:Max size section must contain 0, 1 or 2 colons. '" + s + "'");
    }
    
    public static UnitValue parseUnitValueOrAlign(final String s, final boolean b, final UnitValue unitValue) {
        if (s.length() == 0) {
            return unitValue;
        }
        final UnitValue alignKeywords = parseAlignKeywords(s, b);
        if (alignKeywords != null) {
            return alignKeywords;
        }
        return parseUnitValue(s, unitValue, b);
    }
    
    public static UnitValue parseUnitValue(final String s, final boolean b) {
        return parseUnitValue(s, null, b);
    }
    
    private static UnitValue parseUnitValue(String substring, final UnitValue unitValue, final boolean b) {
        if (substring == null || substring.length() == 0) {
            return unitValue;
        }
        final String s = substring;
        final char char1 = substring.charAt(0);
        if (char1 == '(' && substring.charAt(substring.length() - 1) == ')') {
            substring = substring.substring(1, substring.length() - 1);
        }
        if (char1 == 'n' && (substring.equals("null") || substring.equals("n"))) {
            return null;
        }
        if (char1 == 'i' && substring.equals("inf")) {
            return UnitValue.INF;
        }
        final int oper = getOper(substring);
        final boolean b2 = oper == 101 || oper == 102 || oper == 103 || oper == 104;
        if (oper != 100) {
            String[] array;
            if (!b2) {
                final String trim = substring.substring(4, substring.length() - 1).trim();
                array = toTrimmedTokens(trim, ',');
                if (array.length == 1) {
                    return parseUnitValue(trim, null, b);
                }
            }
            else {
                char c;
                if (oper == 101) {
                    c = '+';
                }
                else if (oper == 102) {
                    c = '-';
                }
                else if (oper == 103) {
                    c = '*';
                }
                else {
                    c = '/';
                }
                array = toTrimmedTokens(substring, c);
                if (array.length > 2) {
                    final String s2 = array[array.length - 1];
                    array = new String[] { substring.substring(0, substring.length() - s2.length() - 1), s2 };
                }
            }
            if (array.length != 2) {
                throw new IllegalArgumentException("Malformed UnitValue: '" + substring + "'");
            }
            final UnitValue unitValue2 = parseUnitValue(array[0], null, b);
            final UnitValue unitValue3 = parseUnitValue(array[1], null, b);
            if (unitValue2 == null || unitValue3 == null) {
                throw new IllegalArgumentException("Malformed UnitValue. Must be two sub-values: '" + substring + "'");
            }
            return new UnitValue(b, oper, unitValue2, unitValue3, s);
        }
        else {
            try {
                final String[] numTextParts = getNumTextParts(substring);
                return new UnitValue((numTextParts[0].length() > 0) ? Float.parseFloat(numTextParts[0]) : 1.0f, numTextParts[1], b, oper, s);
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Malformed UnitValue: '" + substring + "'");
            }
        }
    }
    
    static UnitValue parseAlignKeywords(final String s, final boolean b) {
        if (startsWithLenient(s, "center", 1, false) != -1) {
            return UnitValue.CENTER;
        }
        if (b) {
            if (startsWithLenient(s, "left", 1, false) != -1) {
                return UnitValue.LEFT;
            }
            if (startsWithLenient(s, "right", 1, false) != -1) {
                return UnitValue.RIGHT;
            }
            if (startsWithLenient(s, "leading", 4, false) != -1) {
                return UnitValue.LEADING;
            }
            if (startsWithLenient(s, "trailing", 5, false) != -1) {
                return UnitValue.TRAILING;
            }
            if (startsWithLenient(s, "label", 5, false) != -1) {
                return UnitValue.LABEL;
            }
        }
        else {
            if (startsWithLenient(s, "baseline", 4, false) != -1) {
                return UnitValue.BASELINE_IDENTITY;
            }
            if (startsWithLenient(s, "top", 1, false) != -1) {
                return UnitValue.TOP;
            }
            if (startsWithLenient(s, "bottom", 1, false) != -1) {
                return UnitValue.BOTTOM;
            }
        }
        return null;
    }
    
    private static String[] getNumTextParts(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 == ' ') {
                throw new IllegalArgumentException("Space in UnitValue: '" + s + "'");
            }
            if ((char1 < '0' || char1 > '9') && char1 != '.' && char1 != '-') {
                return new String[] { s.substring(0, i).trim(), s.substring(i).trim() };
            }
        }
        return new String[] { s, "" };
    }
    
    private static int getOper(final String s) {
        final int length = s.length();
        if (length < 3) {
            return 100;
        }
        if (length > 5 && s.charAt(3) == '(' && s.charAt(length - 1) == ')') {
            if (s.startsWith("min(")) {
                return 105;
            }
            if (s.startsWith("max(")) {
                return 106;
            }
            if (s.startsWith("mid(")) {
                return 107;
            }
        }
        for (int i = 0; i < 2; ++i) {
            int j = length - 1;
            int n = 0;
            while (j > 0) {
                final char char1 = s.charAt(j);
                if (char1 == ')') {
                    ++n;
                }
                else if (char1 == '(') {
                    --n;
                }
                else if (n == 0) {
                    if (i == 0) {
                        if (char1 == '+') {
                            return 101;
                        }
                        if (char1 == '-') {
                            return 102;
                        }
                    }
                    else {
                        if (char1 == '*') {
                            return 103;
                        }
                        if (char1 == '/') {
                            return 104;
                        }
                    }
                }
                --j;
            }
        }
        return 100;
    }
    
    private static int startsWithLenient(final String s, final String[] array, final int[] array2, final boolean b) {
        for (int i = 0; i < array.length; ++i) {
            final int startsWithLenient = startsWithLenient(s, array[i], (array2 != null) ? array2[i] : -1, b);
            if (startsWithLenient > -1) {
                return startsWithLenient;
            }
        }
        return -1;
    }
    
    private static int startsWithLenient(final String s, final String s2, int length, final boolean b) {
        if (s.charAt(0) != s2.charAt(0)) {
            return -1;
        }
        if (length == -1) {
            length = s2.length();
        }
        final int length2 = s.length();
        if (length2 < length) {
            return -1;
        }
        final int length3 = s2.length();
        int n = 0;
        for (int i = 0; i < length3; ++i) {
            while (n < length2 && (s.charAt(n) == ' ' || s.charAt(n) == '_')) {
                ++n;
            }
            if (n >= length2 || s.charAt(n) != s2.charAt(i)) {
                return (i >= length && (b || n >= length2) && (n >= length2 || s.charAt(n - 1) == ' ')) ? n : -1;
            }
            ++n;
        }
        return (n >= length2 || b || s.charAt(n) == ' ') ? n : -1;
    }
    
    private static String[] toTrimmedTokens(final String s, final char c) {
        int n = 0;
        final int length = s.length();
        final boolean b = c == ' ';
        int n2 = 0;
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '(') {
                ++n2;
            }
            else if (char1 == ')') {
                --n2;
            }
            else if (n2 == 0 && char1 == c) {
                ++n;
                while (b && i < length - 1 && s.charAt(i + 1) == ' ') {
                    ++i;
                }
            }
            if (n2 < 0) {
                throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'");
            }
        }
        if (n2 != 0) {
            throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'");
        }
        if (n == 0) {
            return new String[] { s.trim() };
        }
        final String[] array = new String[n + 1];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        for (int j = 0; j < length; ++j) {
            final char char2 = s.charAt(j);
            if (char2 == '(') {
                ++n5;
            }
            else if (char2 == ')') {
                --n5;
            }
            else if (n5 == 0 && char2 == c) {
                array[n4++] = s.substring(n3, j).trim();
                n3 = j + 1;
                while (b && j < length - 1 && s.charAt(j + 1) == ' ') {
                    ++j;
                }
            }
        }
        array[n4++] = s.substring(n3, length).trim();
        return array;
    }
    
    private static final ArrayList<String> getRowColAndGapsTrimmed(String replaceAll) {
        if (replaceAll.indexOf(124) != -1) {
            replaceAll = replaceAll.replaceAll("\\|", "][");
        }
        final ArrayList<String> list = new ArrayList<String>(Math.max(replaceAll.length() >> 3, 3));
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        for (int i = 0; i < replaceAll.length(); ++i) {
            final char char1 = replaceAll.charAt(i);
            if (char1 == '[') {
                ++n;
            }
            else {
                if (char1 != ']') {
                    continue;
                }
                ++n2;
            }
            if (n != n2 && n - 1 != n2) {
                break;
            }
            list.add(replaceAll.substring(n3, i).trim());
            n3 = i + 1;
        }
        if (n != n2) {
            throw new IllegalArgumentException("'[' and ']' mismatch in row/column format string: " + replaceAll);
        }
        if (n == 0) {
            list.add("");
            list.add(replaceAll);
            list.add("");
        }
        else if (list.size() % 2 == 0) {
            list.add(replaceAll.substring(n3, replaceAll.length()));
        }
        return list;
    }
    
    public static final String prepare(final String s) {
        return (s != null) ? s.trim().toLowerCase() : "";
    }
}
