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

public final class AC implements Externalizable
{
    private final ArrayList<DimConstraint> cList;
    private transient int curIx;
    
    public AC() {
        this.cList = new ArrayList<DimConstraint>(8);
        this.curIx = 0;
        this.cList.add(new DimConstraint());
    }
    
    public final DimConstraint[] getConstaints() {
        return this.cList.toArray(new DimConstraint[this.cList.size()]);
    }
    
    public final void setConstaints(DimConstraint[] array) {
        if (array == null || array.length < 1) {
            array = new DimConstraint[] { new DimConstraint() };
        }
        this.cList.clear();
        this.cList.ensureCapacity(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.cList.add(array[i]);
        }
    }
    
    public int getCount() {
        return this.cList.size();
    }
    
    public final AC count(final int n) {
        this.makeSize(n);
        return this;
    }
    
    public final AC noGrid() {
        return this.noGrid(this.curIx);
    }
    
    public final AC noGrid(final int... array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setNoGrid(true);
        }
        return this;
    }
    
    public final AC index(final int curIx) {
        this.makeSize(curIx);
        this.curIx = curIx;
        return this;
    }
    
    public final AC fill() {
        return this.fill(this.curIx);
    }
    
    public final AC fill(final int... array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setFill(true);
        }
        return this;
    }
    
    public final AC sizeGroup() {
        return this.sizeGroup("", this.curIx);
    }
    
    public final AC sizeGroup(final String s) {
        return this.sizeGroup(s, this.curIx);
    }
    
    public final AC sizeGroup(final String sizeGroup, final int... array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setSizeGroup(sizeGroup);
        }
        return this;
    }
    
    public final AC size(final String s) {
        return this.size(s, this.curIx);
    }
    
    public final AC size(final String s, final int... array) {
        final BoundSize boundSize = ConstraintParser.parseBoundSize(s, false, true);
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setSize(boundSize);
        }
        return this;
    }
    
    public final AC gap() {
        ++this.curIx;
        return this;
    }
    
    public final AC gap(final String s) {
        return this.gap(s, this.curIx++);
    }
    
    public final AC gap(final String s, final int... array) {
        final BoundSize gapAfter = (s != null) ? ConstraintParser.parseBoundSize(s, true, true) : null;
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            if (gapAfter != null) {
                this.cList.get(n).setGapAfter(gapAfter);
            }
        }
        return this;
    }
    
    public final AC align(final String s) {
        return this.align(s, this.curIx);
    }
    
    public final AC align(final String s, final int... array) {
        UnitValue align = ConstraintParser.parseAlignKeywords(s, true);
        if (align == null) {
            align = ConstraintParser.parseAlignKeywords(s, false);
        }
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setAlign(align);
        }
        return this;
    }
    
    public final AC growPrio(final int n) {
        return this.growPrio(n, this.curIx);
    }
    
    public final AC growPrio(final int growPriority, final int... array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setGrowPriority(growPriority);
        }
        return this;
    }
    
    public final AC grow() {
        return this.grow(1.0f, this.curIx);
    }
    
    public final AC grow(final float n) {
        return this.grow(n, this.curIx);
    }
    
    public final AC grow(final float n, final int... array) {
        final Float grow = new Float(n);
        for (int i = array.length - 1; i >= 0; --i) {
            final int n2 = array[i];
            this.makeSize(n2);
            this.cList.get(n2).setGrow(grow);
        }
        return this;
    }
    
    public final AC shrinkPrio(final int n) {
        return this.shrinkPrio(n, this.curIx);
    }
    
    public final AC shrinkPrio(final int shrinkPriority, final int... array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int n = array[i];
            this.makeSize(n);
            this.cList.get(n).setShrinkPriority(shrinkPriority);
        }
        return this;
    }
    
    public final AC shrink() {
        return this.shrink(100.0f, this.curIx);
    }
    
    public final AC shrink(final float n) {
        return this.shrink(n, this.curIx);
    }
    
    public final AC shrink(final float n, final int... array) {
        final Float shrink = new Float(n);
        for (int i = array.length - 1; i >= 0; --i) {
            final int n2 = array[i];
            this.makeSize(n2);
            this.cList.get(n2).setShrink(shrink);
        }
        return this;
    }
    
    @Deprecated
    public final AC shrinkWeight(final float n) {
        return this.shrink(n);
    }
    
    @Deprecated
    public final AC shrinkWeight(final float n, final int... array) {
        return this.shrink(n, array);
    }
    
    private void makeSize(final int n) {
        if (this.cList.size() <= n) {
            this.cList.ensureCapacity(n);
            for (int i = this.cList.size(); i <= n; ++i) {
                this.cList.add(new DimConstraint());
            }
        }
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == AC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}
