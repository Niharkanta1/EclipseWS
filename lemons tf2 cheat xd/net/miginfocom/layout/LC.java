// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamException;
import java.io.Externalizable;

public final class LC implements Externalizable
{
    private int wrapAfter;
    private Boolean leftToRight;
    private UnitValue[] insets;
    private UnitValue alignX;
    private UnitValue alignY;
    private BoundSize gridGapX;
    private BoundSize gridGapY;
    private BoundSize width;
    private BoundSize height;
    private BoundSize packW;
    private BoundSize packH;
    private float pwAlign;
    private float phAlign;
    private int debugMillis;
    private int hideMode;
    private boolean noCache;
    private boolean flowX;
    private boolean fillX;
    private boolean fillY;
    private boolean topToBottom;
    private boolean noGrid;
    private boolean visualPadding;
    
    public LC() {
        this.wrapAfter = 2097051;
        this.leftToRight = null;
        this.insets = null;
        this.alignX = null;
        this.alignY = null;
        this.gridGapX = null;
        this.gridGapY = null;
        this.width = BoundSize.NULL_SIZE;
        this.height = BoundSize.NULL_SIZE;
        this.packW = BoundSize.NULL_SIZE;
        this.packH = BoundSize.NULL_SIZE;
        this.pwAlign = 0.5f;
        this.phAlign = 1.0f;
        this.debugMillis = 0;
        this.hideMode = 0;
        this.noCache = false;
        this.flowX = true;
        this.fillX = false;
        this.fillY = false;
        this.topToBottom = true;
        this.noGrid = false;
        this.visualPadding = true;
    }
    
    public boolean isNoCache() {
        return this.noCache;
    }
    
    public void setNoCache(final boolean noCache) {
        this.noCache = noCache;
    }
    
    public final UnitValue getAlignX() {
        return this.alignX;
    }
    
    public final void setAlignX(final UnitValue alignX) {
        this.alignX = alignX;
    }
    
    public final UnitValue getAlignY() {
        return this.alignY;
    }
    
    public final void setAlignY(final UnitValue alignY) {
        this.alignY = alignY;
    }
    
    public final int getDebugMillis() {
        return this.debugMillis;
    }
    
    public final void setDebugMillis(final int debugMillis) {
        this.debugMillis = debugMillis;
    }
    
    public final boolean isFillX() {
        return this.fillX;
    }
    
    public final void setFillX(final boolean fillX) {
        this.fillX = fillX;
    }
    
    public final boolean isFillY() {
        return this.fillY;
    }
    
    public final void setFillY(final boolean fillY) {
        this.fillY = fillY;
    }
    
    public final boolean isFlowX() {
        return this.flowX;
    }
    
    public final void setFlowX(final boolean flowX) {
        this.flowX = flowX;
    }
    
    public final BoundSize getGridGapX() {
        return this.gridGapX;
    }
    
    public final void setGridGapX(final BoundSize gridGapX) {
        this.gridGapX = gridGapX;
    }
    
    public final BoundSize getGridGapY() {
        return this.gridGapY;
    }
    
    public final void setGridGapY(final BoundSize gridGapY) {
        this.gridGapY = gridGapY;
    }
    
    public final int getHideMode() {
        return this.hideMode;
    }
    
    public final void setHideMode(final int hideMode) {
        if (hideMode < 0 || hideMode > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + hideMode);
        }
        this.hideMode = hideMode;
    }
    
    public final UnitValue[] getInsets() {
        return (UnitValue[])((this.insets != null) ? new UnitValue[] { this.insets[0], this.insets[1], this.insets[2], this.insets[3] } : null);
    }
    
    public final void setInsets(final UnitValue[] array) {
        this.insets = (UnitValue[])((array != null) ? new UnitValue[] { array[0], array[1], array[2], array[3] } : null);
    }
    
    public final Boolean getLeftToRight() {
        return this.leftToRight;
    }
    
    public final void setLeftToRight(final Boolean leftToRight) {
        this.leftToRight = leftToRight;
    }
    
    public final boolean isNoGrid() {
        return this.noGrid;
    }
    
    public final void setNoGrid(final boolean noGrid) {
        this.noGrid = noGrid;
    }
    
    public final boolean isTopToBottom() {
        return this.topToBottom;
    }
    
    public final void setTopToBottom(final boolean topToBottom) {
        this.topToBottom = topToBottom;
    }
    
    public final boolean isVisualPadding() {
        return this.visualPadding;
    }
    
    public final void setVisualPadding(final boolean visualPadding) {
        this.visualPadding = visualPadding;
    }
    
    public final int getWrapAfter() {
        return this.wrapAfter;
    }
    
    public final void setWrapAfter(final int wrapAfter) {
        this.wrapAfter = wrapAfter;
    }
    
    public final BoundSize getPackWidth() {
        return this.packW;
    }
    
    public final void setPackWidth(final BoundSize boundSize) {
        this.packW = ((boundSize != null) ? boundSize : BoundSize.NULL_SIZE);
    }
    
    public final BoundSize getPackHeight() {
        return this.packH;
    }
    
    public final void setPackHeight(final BoundSize boundSize) {
        this.packH = ((boundSize != null) ? boundSize : BoundSize.NULL_SIZE);
    }
    
    public final float getPackHeightAlign() {
        return this.phAlign;
    }
    
    public final void setPackHeightAlign(final float n) {
        this.phAlign = Math.max(0.0f, Math.min(1.0f, n));
    }
    
    public final float getPackWidthAlign() {
        return this.pwAlign;
    }
    
    public final void setPackWidthAlign(final float n) {
        this.pwAlign = Math.max(0.0f, Math.min(1.0f, n));
    }
    
    public final BoundSize getWidth() {
        return this.width;
    }
    
    public final void setWidth(final BoundSize boundSize) {
        this.width = ((boundSize != null) ? boundSize : BoundSize.NULL_SIZE);
    }
    
    public final BoundSize getHeight() {
        return this.height;
    }
    
    public final void setHeight(final BoundSize boundSize) {
        this.height = ((boundSize != null) ? boundSize : BoundSize.NULL_SIZE);
    }
    
    public final LC pack() {
        return this.pack("pref", "pref");
    }
    
    public final LC pack(final String s, final String s2) {
        this.setPackWidth((s != null) ? ConstraintParser.parseBoundSize(s, false, false) : BoundSize.NULL_SIZE);
        this.setPackHeight((s2 != null) ? ConstraintParser.parseBoundSize(s2, false, false) : BoundSize.NULL_SIZE);
        return this;
    }
    
    public final LC packAlign(final float packWidthAlign, final float packHeightAlign) {
        this.setPackWidthAlign(packWidthAlign);
        this.setPackHeightAlign(packHeightAlign);
        return this;
    }
    
    public final LC wrap() {
        this.setWrapAfter(0);
        return this;
    }
    
    public final LC wrapAfter(final int wrapAfter) {
        this.setWrapAfter(wrapAfter);
        return this;
    }
    
    public final LC noCache() {
        this.setNoCache(true);
        return this;
    }
    
    public final LC flowY() {
        this.setFlowX(false);
        return this;
    }
    
    public final LC flowX() {
        this.setFlowX(true);
        return this;
    }
    
    public final LC fill() {
        this.setFillX(true);
        this.setFillY(true);
        return this;
    }
    
    public final LC fillX() {
        this.setFillX(true);
        return this;
    }
    
    public final LC fillY() {
        this.setFillY(true);
        return this;
    }
    
    public final LC leftToRight(final boolean b) {
        this.setLeftToRight(b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public final LC rightToLeft() {
        this.setLeftToRight(Boolean.FALSE);
        return this;
    }
    
    public final LC bottomToTop() {
        this.setTopToBottom(false);
        return this;
    }
    
    public final LC topToBottom() {
        this.setTopToBottom(true);
        return this;
    }
    
    public final LC noGrid() {
        this.setNoGrid(true);
        return this;
    }
    
    public final LC noVisualPadding() {
        this.setVisualPadding(false);
        return this;
    }
    
    public final LC insetsAll(final String s) {
        final UnitValue unitValue = ConstraintParser.parseUnitValue(s, true);
        final UnitValue unitValue2 = ConstraintParser.parseUnitValue(s, false);
        this.insets = new UnitValue[] { unitValue2, unitValue, unitValue2, unitValue };
        return this;
    }
    
    public final LC insets(final String s) {
        this.insets = ConstraintParser.parseInsets(s, true);
        return this;
    }
    
    public final LC insets(final String s, final String s2, final String s3, final String s4) {
        this.insets = new UnitValue[] { ConstraintParser.parseUnitValue(s, false), ConstraintParser.parseUnitValue(s2, true), ConstraintParser.parseUnitValue(s3, false), ConstraintParser.parseUnitValue(s4, true) };
        return this;
    }
    
    public final LC alignX(final String s) {
        this.setAlignX(ConstraintParser.parseUnitValueOrAlign(s, true, null));
        return this;
    }
    
    public final LC alignY(final String s) {
        this.setAlignY(ConstraintParser.parseUnitValueOrAlign(s, false, null));
        return this;
    }
    
    public final LC align(final String s, final String s2) {
        if (s != null) {
            this.alignX(s);
        }
        if (s2 != null) {
            this.alignY(s2);
        }
        return this;
    }
    
    public final LC gridGapX(final String s) {
        this.setGridGapX(ConstraintParser.parseBoundSize(s, true, true));
        return this;
    }
    
    public final LC gridGapY(final String s) {
        this.setGridGapY(ConstraintParser.parseBoundSize(s, true, false));
        return this;
    }
    
    public final LC gridGap(final String s, final String s2) {
        if (s != null) {
            this.gridGapX(s);
        }
        if (s2 != null) {
            this.gridGapY(s2);
        }
        return this;
    }
    
    public final LC debug(final int debugMillis) {
        this.setDebugMillis(debugMillis);
        return this;
    }
    
    public final LC hideMode(final int hideMode) {
        this.setHideMode(hideMode);
        return this;
    }
    
    public final LC minWidth(final String s) {
        this.setWidth(LayoutUtil.derive(this.getWidth(), ConstraintParser.parseUnitValue(s, true), null, null));
        return this;
    }
    
    public final LC width(final String s) {
        this.setWidth(ConstraintParser.parseBoundSize(s, false, true));
        return this;
    }
    
    public final LC maxWidth(final String s) {
        this.setWidth(LayoutUtil.derive(this.getWidth(), null, null, ConstraintParser.parseUnitValue(s, true)));
        return this;
    }
    
    public final LC minHeight(final String s) {
        this.setHeight(LayoutUtil.derive(this.getHeight(), ConstraintParser.parseUnitValue(s, false), null, null));
        return this;
    }
    
    public final LC height(final String s) {
        this.setHeight(ConstraintParser.parseBoundSize(s, false, false));
        return this;
    }
    
    public final LC maxHeight(final String s) {
        this.setHeight(LayoutUtil.derive(this.getHeight(), null, null, ConstraintParser.parseUnitValue(s, false)));
        return this;
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == LC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}
