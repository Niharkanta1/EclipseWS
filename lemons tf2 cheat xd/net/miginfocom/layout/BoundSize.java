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
import java.io.Serializable;

public class BoundSize implements Serializable
{
    public static final BoundSize NULL_SIZE;
    public static final BoundSize ZERO_PIXEL;
    private final transient UnitValue min;
    private final transient UnitValue pref;
    private final transient UnitValue max;
    private final transient boolean gapPush;
    private static final long serialVersionUID = 1L;
    
    public BoundSize(final UnitValue unitValue, final String s) {
        this(unitValue, unitValue, unitValue, s);
    }
    
    public BoundSize(final UnitValue unitValue, final UnitValue unitValue2, final UnitValue unitValue3, final String s) {
        this(unitValue, unitValue2, unitValue3, false, s);
    }
    
    public BoundSize(final UnitValue min, final UnitValue pref, final UnitValue max, final boolean gapPush, final String s) {
        this.min = min;
        this.pref = pref;
        this.max = max;
        this.gapPush = gapPush;
        LayoutUtil.putCCString(this, s);
    }
    
    public final UnitValue getMin() {
        return this.min;
    }
    
    public final UnitValue getPreferred() {
        return this.pref;
    }
    
    public final UnitValue getMax() {
        return this.max;
    }
    
    public boolean getGapPush() {
        return this.gapPush;
    }
    
    public boolean isUnset() {
        return this == BoundSize.ZERO_PIXEL || (this.pref == null && this.min == null && this.max == null && !this.gapPush);
    }
    
    public int constrain(int n, final float n2, final ContainerWrapper containerWrapper) {
        if (this.max != null) {
            n = Math.min(n, this.max.getPixels(n2, containerWrapper, containerWrapper));
        }
        if (this.min != null) {
            n = Math.max(n, this.min.getPixels(n2, containerWrapper, containerWrapper));
        }
        return n;
    }
    
    final UnitValue getSize(final int n) {
        switch (n) {
            case 0: {
                return this.min;
            }
            case 1: {
                return this.pref;
            }
            case 2: {
                return this.max;
            }
            default: {
                throw new IllegalArgumentException("Unknown size: " + n);
            }
        }
    }
    
    final int[] getPixelSizes(final float n, final ContainerWrapper containerWrapper, final ComponentWrapper componentWrapper) {
        return new int[] { (this.min != null) ? this.min.getPixels(n, containerWrapper, componentWrapper) : 0, (this.pref != null) ? this.pref.getPixels(n, containerWrapper, componentWrapper) : 0, (this.max != null) ? this.max.getPixels(n, containerWrapper, componentWrapper) : 2097051 };
    }
    
    String getConstraintString() {
        final String ccString = LayoutUtil.getCCString(this);
        if (ccString != null) {
            return ccString;
        }
        if (this.min == this.pref && this.pref == this.max) {
            return (this.min != null) ? (this.min.getConstraintString() + "!") : "null";
        }
        final StringBuilder sb = new StringBuilder(16);
        if (this.min != null) {
            sb.append(this.min.getConstraintString()).append(':');
        }
        if (this.pref != null) {
            if (this.min == null && this.max != null) {
                sb.append(":");
            }
            sb.append(this.pref.getConstraintString());
        }
        else if (this.min != null) {
            sb.append('n');
        }
        if (this.max != null) {
            sb.append((sb.length() == 0) ? "::" : ":").append(this.max.getConstraintString());
        }
        if (this.gapPush) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append("push");
        }
        return sb.toString();
    }
    
    void checkNotLinked() {
        if ((this.min != null && this.min.isLinkedDeep()) || (this.pref != null && this.pref.isLinkedDeep()) || (this.max != null && this.max.isLinkedDeep())) {
            throw new IllegalArgumentException("Size may not contain links");
        }
    }
    
    protected Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        if (this.getClass() == BoundSize.class) {
            LayoutUtil.writeAsXML(objectOutputStream, this);
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInputStream));
    }
    
    static {
        NULL_SIZE = new BoundSize(null, null);
        ZERO_PIXEL = new BoundSize(UnitValue.ZERO, "0px");
        LayoutUtil.setDelegate(BoundSize.class, new PersistenceDelegate() {
            @Override
            protected Expression instantiate(final Object o, final Encoder encoder) {
                final BoundSize boundSize = (BoundSize)o;
                return new Expression(o, BoundSize.class, "new", new Object[] { boundSize.getMin(), boundSize.getPreferred(), boundSize.getMax(), boundSize.getGapPush(), boundSize.getConstraintString() });
            }
        });
    }
}
