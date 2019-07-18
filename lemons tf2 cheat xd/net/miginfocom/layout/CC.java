// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.io.Externalizable;

public final class CC implements Externalizable
{
    private static final BoundSize DEF_GAP;
    static final String[] DOCK_SIDES;
    private int dock;
    private UnitValue[] pos;
    private UnitValue[] padding;
    private Boolean flowX;
    private int skip;
    private int split;
    private int spanX;
    private int spanY;
    private int cellX;
    private int cellY;
    private String tag;
    private String id;
    private int hideMode;
    private DimConstraint hor;
    private DimConstraint ver;
    private BoundSize newline;
    private BoundSize wrap;
    private boolean boundsInGrid;
    private boolean external;
    private Float pushX;
    private Float pushY;
    private static final String[] EMPTY_ARR;
    private transient String[] linkTargets;
    
    public CC() {
        this.dock = -1;
        this.pos = null;
        this.padding = null;
        this.flowX = null;
        this.skip = 0;
        this.split = 1;
        this.spanX = 1;
        this.spanY = 1;
        this.cellX = -1;
        this.cellY = 0;
        this.tag = null;
        this.id = null;
        this.hideMode = -1;
        this.hor = new DimConstraint();
        this.ver = new DimConstraint();
        this.newline = null;
        this.wrap = null;
        this.boundsInGrid = true;
        this.external = false;
        this.pushX = null;
        this.pushY = null;
        this.linkTargets = null;
    }
    
    String[] getLinkTargets() {
        if (this.linkTargets == null) {
            final ArrayList<String> list = new ArrayList<String>(2);
            if (this.pos != null) {
                for (int i = 0; i < this.pos.length; ++i) {
                    this.addLinkTargetIDs(list, this.pos[i]);
                }
            }
            this.linkTargets = ((list.size() == 0) ? CC.EMPTY_ARR : list.toArray(new String[list.size()]));
        }
        return this.linkTargets;
    }
    
    private void addLinkTargetIDs(final ArrayList<String> list, final UnitValue unitValue) {
        if (unitValue != null) {
            final String linkTargetId = unitValue.getLinkTargetId();
            if (linkTargetId != null) {
                list.add(linkTargetId);
            }
            else {
                for (int i = unitValue.getSubUnitCount() - 1; i >= 0; --i) {
                    final UnitValue subUnitValue = unitValue.getSubUnitValue(i);
                    if (subUnitValue.isLinkedDeep()) {
                        this.addLinkTargetIDs(list, subUnitValue);
                    }
                }
            }
        }
    }
    
    public final CC endGroupX(final String endGroup) {
        this.hor.setEndGroup(endGroup);
        return this;
    }
    
    public final CC sizeGroupX(final String sizeGroup) {
        this.hor.setSizeGroup(sizeGroup);
        return this;
    }
    
    public final CC minWidth(final String s) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), ConstraintParser.parseUnitValue(s, true), null, null));
        return this;
    }
    
    public final CC width(final String s) {
        this.hor.setSize(ConstraintParser.parseBoundSize(s, false, true));
        return this;
    }
    
    public final CC maxWidth(final String s) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), null, null, ConstraintParser.parseUnitValue(s, true)));
        return this;
    }
    
    public final CC gapX(final String s, final String s2) {
        if (s != null) {
            this.hor.setGapBefore(ConstraintParser.parseBoundSize(s, true, true));
        }
        if (s2 != null) {
            this.hor.setGapAfter(ConstraintParser.parseBoundSize(s2, true, true));
        }
        return this;
    }
    
    public final CC alignX(final String s) {
        this.hor.setAlign(ConstraintParser.parseUnitValueOrAlign(s, true, null));
        return this;
    }
    
    public final CC growPrioX(final int growPriority) {
        this.hor.setGrowPriority(growPriority);
        return this;
    }
    
    public final CC growPrio(final int... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.growPrioY(array[1]);
            }
            case 1: {
                this.growPrioX(array[0]);
                return this;
            }
        }
    }
    
    public final CC growX() {
        this.hor.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }
    
    public final CC growX(final float n) {
        this.hor.setGrow(new Float(n));
        return this;
    }
    
    public final CC grow(final float... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.growY(array[1]);
            }
            case 1: {
                this.growX(array[0]);
                return this;
            }
        }
    }
    
    public final CC shrinkPrioX(final int shrinkPriority) {
        this.hor.setShrinkPriority(shrinkPriority);
        return this;
    }
    
    public final CC shrinkPrio(final int... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.shrinkPrioY(array[1]);
            }
            case 1: {
                this.shrinkPrioX(array[0]);
                return this;
            }
        }
    }
    
    public final CC shrinkX(final float n) {
        this.hor.setShrink(new Float(n));
        return this;
    }
    
    public final CC shrink(final float... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.shrinkY(array[1]);
            }
            case 1: {
                this.shrinkX(array[0]);
                return this;
            }
        }
    }
    
    public final CC endGroupY(final String endGroup) {
        this.ver.setEndGroup(endGroup);
        return this;
    }
    
    public final CC endGroup(final String... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.endGroupY(array[1]);
            }
            case 1: {
                this.endGroupX(array[0]);
                return this;
            }
        }
    }
    
    public final CC sizeGroupY(final String sizeGroup) {
        this.ver.setSizeGroup(sizeGroup);
        return this;
    }
    
    public final CC sizeGroup(final String... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 2: {
                this.sizeGroupY(array[1]);
            }
            case 1: {
                this.sizeGroupX(array[0]);
                return this;
            }
        }
    }
    
    public final CC minHeight(final String s) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), ConstraintParser.parseUnitValue(s, false), null, null));
        return this;
    }
    
    public final CC height(final String s) {
        this.ver.setSize(ConstraintParser.parseBoundSize(s, false, false));
        return this;
    }
    
    public final CC maxHeight(final String s) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), null, null, ConstraintParser.parseUnitValue(s, false)));
        return this;
    }
    
    public final CC gapY(final String s, final String s2) {
        if (s != null) {
            this.ver.setGapBefore(ConstraintParser.parseBoundSize(s, true, false));
        }
        if (s2 != null) {
            this.ver.setGapAfter(ConstraintParser.parseBoundSize(s2, true, false));
        }
        return this;
    }
    
    public final CC alignY(final String s) {
        this.ver.setAlign(ConstraintParser.parseUnitValueOrAlign(s, false, null));
        return this;
    }
    
    public final CC growPrioY(final int growPriority) {
        this.ver.setGrowPriority(growPriority);
        return this;
    }
    
    public final CC growY() {
        this.ver.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }
    
    public final CC growY(final Float grow) {
        this.ver.setGrow(grow);
        return this;
    }
    
    public final CC shrinkPrioY(final int shrinkPriority) {
        this.ver.setShrinkPriority(shrinkPriority);
        return this;
    }
    
    public final CC shrinkY(final float n) {
        this.ver.setShrink(new Float(n));
        return this;
    }
    
    public final CC hideMode(final int hideMode) {
        this.setHideMode(hideMode);
        return this;
    }
    
    public final CC id(final String id) {
        this.setId(id);
        return this;
    }
    
    public final CC tag(final String tag) {
        this.setTag(tag);
        return this;
    }
    
    public final CC cell(final int... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 4: {
                this.setSpanY(array[3]);
            }
            case 3: {
                this.setSpanX(array[2]);
            }
            case 2: {
                this.setCellY(array[1]);
            }
            case 1: {
                this.setCellX(array[0]);
                return this;
            }
        }
    }
    
    public final CC span(final int... array) {
        if (array == null || array.length == 0) {
            this.setSpanX(2097051);
            this.setSpanY(1);
        }
        else if (array.length == 1) {
            this.setSpanX(array[0]);
            this.setSpanY(1);
        }
        else {
            this.setSpanX(array[0]);
            this.setSpanY(array[1]);
        }
        return this;
    }
    
    public final CC gap(final String... array) {
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + array.length);
            }
            case 4: {
                this.gapBottom(array[3]);
            }
            case 3: {
                this.gapTop(array[2]);
            }
            case 2: {
                this.gapRight(array[1]);
            }
            case 1: {
                this.gapLeft(array[0]);
                return this;
            }
        }
    }
    
    public final CC gapBefore(final String s) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(s, true, true));
        return this;
    }
    
    public final CC gapAfter(final String s) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(s, true, true));
        return this;
    }
    
    public final CC gapTop(final String s) {
        this.ver.setGapBefore(ConstraintParser.parseBoundSize(s, true, false));
        return this;
    }
    
    public final CC gapLeft(final String s) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(s, true, true));
        return this;
    }
    
    public final CC gapBottom(final String s) {
        this.ver.setGapAfter(ConstraintParser.parseBoundSize(s, true, false));
        return this;
    }
    
    public final CC gapRight(final String s) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(s, true, true));
        return this;
    }
    
    public final CC spanY() {
        return this.spanY(2097051);
    }
    
    public final CC spanY(final int spanY) {
        this.setSpanY(spanY);
        return this;
    }
    
    public final CC spanX() {
        return this.spanX(2097051);
    }
    
    public final CC spanX(final int spanX) {
        this.setSpanX(spanX);
        return this;
    }
    
    public final CC push() {
        return this.pushX().pushY();
    }
    
    public final CC push(final Float n, final Float n2) {
        return this.pushX(n).pushY(n2);
    }
    
    public final CC pushY() {
        return this.pushY(ResizeConstraint.WEIGHT_100);
    }
    
    public final CC pushY(final Float pushY) {
        this.setPushY(pushY);
        return this;
    }
    
    public final CC pushX() {
        return this.pushX(ResizeConstraint.WEIGHT_100);
    }
    
    public final CC pushX(final Float pushX) {
        this.setPushX(pushX);
        return this;
    }
    
    public final CC split(final int split) {
        this.setSplit(split);
        return this;
    }
    
    public final CC split() {
        this.setSplit(2097051);
        return this;
    }
    
    public final CC skip(final int skip) {
        this.setSkip(skip);
        return this;
    }
    
    public final CC skip() {
        this.setSkip(1);
        return this;
    }
    
    public final CC external() {
        this.setExternal(true);
        return this;
    }
    
    public final CC flowX() {
        this.setFlowX(Boolean.TRUE);
        return this;
    }
    
    public final CC flowY() {
        this.setFlowX(Boolean.FALSE);
        return this;
    }
    
    public final CC grow() {
        this.growX();
        this.growY();
        return this;
    }
    
    public final CC newline() {
        this.setNewline(true);
        return this;
    }
    
    public final CC newline(final String s) {
        final BoundSize boundSize = ConstraintParser.parseBoundSize(s, true, this.flowX != null && !this.flowX);
        if (boundSize != null) {
            this.setNewlineGapSize(boundSize);
        }
        else {
            this.setNewline(true);
        }
        return this;
    }
    
    public final CC wrap() {
        this.setWrap(true);
        return this;
    }
    
    public final CC wrap(final String s) {
        final BoundSize boundSize = ConstraintParser.parseBoundSize(s, true, this.flowX != null && !this.flowX);
        if (boundSize != null) {
            this.setWrapGapSize(boundSize);
        }
        else {
            this.setWrap(true);
        }
        return this;
    }
    
    public final CC dockNorth() {
        this.setDockSide(0);
        return this;
    }
    
    public final CC dockWest() {
        this.setDockSide(1);
        return this;
    }
    
    public final CC dockSouth() {
        this.setDockSide(2);
        return this;
    }
    
    public final CC dockEast() {
        this.setDockSide(3);
        return this;
    }
    
    public final CC x(final String s) {
        return this.corrPos(s, 0);
    }
    
    public final CC y(final String s) {
        return this.corrPos(s, 1);
    }
    
    public final CC x2(final String s) {
        return this.corrPos(s, 2);
    }
    
    public final CC y2(final String s) {
        return this.corrPos(s, 3);
    }
    
    private final CC corrPos(final String s, final int n) {
        UnitValue[] pos = this.getPos();
        if (pos == null) {
            pos = new UnitValue[4];
        }
        pos[n] = ConstraintParser.parseUnitValue(s, n % 2 == 0);
        this.setPos(pos);
        this.setBoundsInGrid(true);
        return this;
    }
    
    public final CC pos(final String s, final String s2) {
        UnitValue[] pos = this.getPos();
        if (pos == null) {
            pos = new UnitValue[4];
        }
        pos[0] = ConstraintParser.parseUnitValue(s, true);
        pos[1] = ConstraintParser.parseUnitValue(s2, false);
        this.setPos(pos);
        this.setBoundsInGrid(false);
        return this;
    }
    
    public final CC pos(final String s, final String s2, final String s3, final String s4) {
        this.setPos(new UnitValue[] { ConstraintParser.parseUnitValue(s, true), ConstraintParser.parseUnitValue(s2, false), ConstraintParser.parseUnitValue(s3, true), ConstraintParser.parseUnitValue(s4, false) });
        this.setBoundsInGrid(false);
        return this;
    }
    
    public final CC pad(final int n, final int n2, final int n3, final int n4) {
        this.setPadding(new UnitValue[] { new UnitValue((float)n), new UnitValue((float)n2), new UnitValue((float)n3), new UnitValue((float)n4) });
        return this;
    }
    
    public final CC pad(final String s) {
        this.setPadding((UnitValue[])((s != null) ? ConstraintParser.parseInsets(s, false) : null));
        return this;
    }
    
    public DimConstraint getHorizontal() {
        return this.hor;
    }
    
    public void setHorizontal(final DimConstraint dimConstraint) {
        this.hor = ((dimConstraint != null) ? dimConstraint : new DimConstraint());
    }
    
    public DimConstraint getVertical() {
        return this.ver;
    }
    
    public void setVertical(final DimConstraint dimConstraint) {
        this.ver = ((dimConstraint != null) ? dimConstraint : new DimConstraint());
    }
    
    public DimConstraint getDimConstraint(final boolean b) {
        return b ? this.hor : this.ver;
    }
    
    public UnitValue[] getPos() {
        return (UnitValue[])((this.pos != null) ? new UnitValue[] { this.pos[0], this.pos[1], this.pos[2], this.pos[3] } : null);
    }
    
    public void setPos(final UnitValue[] array) {
        this.pos = (UnitValue[])((array != null) ? new UnitValue[] { array[0], array[1], array[2], array[3] } : null);
        this.linkTargets = null;
    }
    
    public boolean isBoundsInGrid() {
        return this.boundsInGrid;
    }
    
    void setBoundsInGrid(final boolean boundsInGrid) {
        this.boundsInGrid = boundsInGrid;
    }
    
    public int getCellX() {
        return this.cellX;
    }
    
    public void setCellX(final int cellX) {
        this.cellX = cellX;
    }
    
    public int getCellY() {
        return (this.cellX < 0) ? -1 : this.cellY;
    }
    
    public void setCellY(final int n) {
        if (n < 0) {
            this.cellX = -1;
        }
        this.cellY = ((n < 0) ? 0 : n);
    }
    
    public int getDockSide() {
        return this.dock;
    }
    
    public void setDockSide(final int dock) {
        if (dock < -1 || dock > 3) {
            throw new IllegalArgumentException("Illegal dock side: " + dock);
        }
        this.dock = dock;
    }
    
    public boolean isExternal() {
        return this.external;
    }
    
    public void setExternal(final boolean external) {
        this.external = external;
    }
    
    public Boolean getFlowX() {
        return this.flowX;
    }
    
    public void setFlowX(final Boolean flowX) {
        this.flowX = flowX;
    }
    
    public int getHideMode() {
        return this.hideMode;
    }
    
    public void setHideMode(final int hideMode) {
        if (hideMode < -1 || hideMode > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + hideMode);
        }
        this.hideMode = hideMode;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public UnitValue[] getPadding() {
        return (UnitValue[])((this.padding != null) ? new UnitValue[] { this.padding[0], this.padding[1], this.padding[2], this.padding[3] } : null);
    }
    
    public void setPadding(final UnitValue[] array) {
        this.padding = (UnitValue[])((array != null) ? new UnitValue[] { array[0], array[1], array[2], array[3] } : null);
    }
    
    public int getSkip() {
        return this.skip;
    }
    
    public void setSkip(final int skip) {
        this.skip = skip;
    }
    
    public int getSpanX() {
        return this.spanX;
    }
    
    public void setSpanX(final int spanX) {
        this.spanX = spanX;
    }
    
    public int getSpanY() {
        return this.spanY;
    }
    
    public void setSpanY(final int spanY) {
        this.spanY = spanY;
    }
    
    public Float getPushX() {
        return this.pushX;
    }
    
    public void setPushX(final Float pushX) {
        this.pushX = pushX;
    }
    
    public Float getPushY() {
        return this.pushY;
    }
    
    public void setPushY(final Float pushY) {
        this.pushY = pushY;
    }
    
    public int getSplit() {
        return this.split;
    }
    
    public void setSplit(final int split) {
        this.split = split;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public void setTag(final String tag) {
        this.tag = tag;
    }
    
    public boolean isWrap() {
        return this.wrap != null;
    }
    
    public void setWrap(final boolean b) {
        this.wrap = (b ? ((this.wrap == null) ? CC.DEF_GAP : this.wrap) : null);
    }
    
    public BoundSize getWrapGapSize() {
        return (this.wrap == CC.DEF_GAP) ? null : this.wrap;
    }
    
    public void setWrapGapSize(final BoundSize boundSize) {
        this.wrap = ((boundSize == null) ? ((this.wrap != null) ? CC.DEF_GAP : null) : boundSize);
    }
    
    public boolean isNewline() {
        return this.newline != null;
    }
    
    public void setNewline(final boolean b) {
        this.newline = (b ? ((this.newline == null) ? CC.DEF_GAP : this.newline) : null);
    }
    
    public BoundSize getNewlineGapSize() {
        return (this.newline == CC.DEF_GAP) ? null : this.newline;
    }
    
    public void setNewlineGapSize(final BoundSize boundSize) {
        this.newline = ((boundSize == null) ? ((this.newline != null) ? CC.DEF_GAP : null) : boundSize);
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == CC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
    
    static {
        DEF_GAP = BoundSize.NULL_SIZE;
        DOCK_SIDES = new String[] { "north", "west", "south", "east" };
        EMPTY_ARR = new String[0];
    }
}
