// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamException;
import java.io.Externalizable;

public final class DimConstraint implements Externalizable
{
    final ResizeConstraint resize;
    private String sizeGroup;
    private BoundSize size;
    private BoundSize gapBefore;
    private BoundSize gapAfter;
    private UnitValue align;
    private String endGroup;
    private boolean fill;
    private boolean noGrid;
    
    public DimConstraint() {
        this.resize = new ResizeConstraint();
        this.sizeGroup = null;
        this.size = BoundSize.NULL_SIZE;
        this.gapBefore = null;
        this.gapAfter = null;
        this.align = null;
        this.endGroup = null;
        this.fill = false;
        this.noGrid = false;
    }
    
    public int getGrowPriority() {
        return this.resize.growPrio;
    }
    
    public void setGrowPriority(final int growPrio) {
        this.resize.growPrio = growPrio;
    }
    
    public Float getGrow() {
        return this.resize.grow;
    }
    
    public void setGrow(final Float grow) {
        this.resize.grow = grow;
    }
    
    public int getShrinkPriority() {
        return this.resize.shrinkPrio;
    }
    
    public void setShrinkPriority(final int shrinkPrio) {
        this.resize.shrinkPrio = shrinkPrio;
    }
    
    public Float getShrink() {
        return this.resize.shrink;
    }
    
    public void setShrink(final Float shrink) {
        this.resize.shrink = shrink;
    }
    
    public UnitValue getAlignOrDefault(final boolean b) {
        if (this.align != null) {
            return this.align;
        }
        if (b) {
            return UnitValue.LEADING;
        }
        return (this.fill || !PlatformDefaults.getDefaultRowAlignmentBaseline()) ? UnitValue.CENTER : UnitValue.BASELINE_IDENTITY;
    }
    
    public UnitValue getAlign() {
        return this.align;
    }
    
    public void setAlign(final UnitValue align) {
        this.align = align;
    }
    
    public BoundSize getGapAfter() {
        return this.gapAfter;
    }
    
    public void setGapAfter(final BoundSize gapAfter) {
        this.gapAfter = gapAfter;
    }
    
    boolean hasGapAfter() {
        return this.gapAfter != null && !this.gapAfter.isUnset();
    }
    
    boolean isGapAfterPush() {
        return this.gapAfter != null && this.gapAfter.getGapPush();
    }
    
    public BoundSize getGapBefore() {
        return this.gapBefore;
    }
    
    public void setGapBefore(final BoundSize gapBefore) {
        this.gapBefore = gapBefore;
    }
    
    boolean hasGapBefore() {
        return this.gapBefore != null && !this.gapBefore.isUnset();
    }
    
    boolean isGapBeforePush() {
        return this.gapBefore != null && this.gapBefore.getGapPush();
    }
    
    public BoundSize getSize() {
        return this.size;
    }
    
    public void setSize(final BoundSize size) {
        if (size != null) {
            size.checkNotLinked();
        }
        this.size = size;
    }
    
    public String getSizeGroup() {
        return this.sizeGroup;
    }
    
    public void setSizeGroup(final String sizeGroup) {
        this.sizeGroup = sizeGroup;
    }
    
    public String getEndGroup() {
        return this.endGroup;
    }
    
    public void setEndGroup(final String endGroup) {
        this.endGroup = endGroup;
    }
    
    public boolean isFill() {
        return this.fill;
    }
    
    public void setFill(final boolean fill) {
        this.fill = fill;
    }
    
    public boolean isNoGrid() {
        return this.noGrid;
    }
    
    public void setNoGrid(final boolean noGrid) {
        this.noGrid = noGrid;
    }
    
    int[] getRowGaps(final ContainerWrapper containerWrapper, final BoundSize boundSize, final int n, final boolean b) {
        BoundSize boundSize2 = b ? this.gapBefore : this.gapAfter;
        if (boundSize2 == null || boundSize2.isUnset()) {
            boundSize2 = boundSize;
        }
        if (boundSize2 == null || boundSize2.isUnset()) {
            return null;
        }
        final int[] array = new int[3];
        for (int i = 0; i <= 2; ++i) {
            final UnitValue size = boundSize2.getSize(i);
            array[i] = ((size != null) ? size.getPixels((float)n, containerWrapper, null) : -2147471302);
        }
        return array;
    }
    
    int[] getComponentGaps(final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper, final BoundSize boundSize, final ComponentWrapper componentWrapper2, final String s, final int n, final int n2, final boolean b) {
        BoundSize defaultComponentGap = (n2 < 2) ? this.gapBefore : this.gapAfter;
        final boolean b2 = defaultComponentGap != null && defaultComponentGap.getGapPush();
        if ((defaultComponentGap == null || defaultComponentGap.isUnset()) && (boundSize == null || boundSize.isUnset()) && componentWrapper != null) {
            defaultComponentGap = PlatformDefaults.getDefaultComponentGap(componentWrapper, componentWrapper2, n2 + 1, s, b);
        }
        if (defaultComponentGap == null) {
            return (int[])(b2 ? new int[] { 0, 0, -2147471302 } : null);
        }
        final int[] array = new int[3];
        for (int i = 0; i <= 2; ++i) {
            final UnitValue size = defaultComponentGap.getSize(i);
            array[i] = ((size != null) ? size.getPixels((float)n, containerWrapper, null) : -2147471302);
        }
        return array;
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == DimConstraint.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}
