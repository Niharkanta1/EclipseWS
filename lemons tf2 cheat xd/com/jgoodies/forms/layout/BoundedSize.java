// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.List;
import java.awt.Container;
import com.jgoodies.common.base.Preconditions;
import java.io.Serializable;

public final class BoundedSize implements Size, Serializable
{
    private final Size basis;
    private final Size lowerBound;
    private final Size upperBound;
    
    public BoundedSize(final Size basis, final Size lowerBound, final Size upperBound) {
        this.basis = Preconditions.checkNotNull(basis, "The basis must not be null.");
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        if (lowerBound == null && upperBound == null) {
            throw new IllegalArgumentException("A bounded size must have a non-null lower or upper bound.");
        }
    }
    
    public Size getBasis() {
        return this.basis;
    }
    
    public Size getLowerBound() {
        return this.lowerBound;
    }
    
    public Size getUpperBound() {
        return this.upperBound;
    }
    
    @Override
    public int maximumSize(final Container container, final List components, final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure, final FormLayout.Measure defaultMeasure) {
        int size = this.basis.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
        if (this.lowerBound != null) {
            size = Math.max(size, this.lowerBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
        }
        if (this.upperBound != null) {
            size = Math.min(size, this.upperBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
        }
        return size;
    }
    
    @Override
    public boolean compressible() {
        return this.getBasis().compressible();
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BoundedSize)) {
            return false;
        }
        final BoundedSize size = (BoundedSize)object;
        return this.basis.equals(size.basis) && ((this.lowerBound == null && size.lowerBound == null) || (this.lowerBound != null && this.lowerBound.equals(size.lowerBound))) && ((this.upperBound == null && size.upperBound == null) || (this.upperBound != null && this.upperBound.equals(size.upperBound)));
    }
    
    @Override
    public int hashCode() {
        int hashValue = this.basis.hashCode();
        if (this.lowerBound != null) {
            hashValue = hashValue * 37 + this.lowerBound.hashCode();
        }
        if (this.upperBound != null) {
            hashValue = hashValue * 37 + this.upperBound.hashCode();
        }
        return hashValue;
    }
    
    @Override
    public String toString() {
        return this.encode();
    }
    
    @Override
    public String encode() {
        final StringBuffer buffer = new StringBuffer("[");
        if (this.lowerBound != null) {
            buffer.append(this.lowerBound.encode());
            buffer.append(',');
        }
        buffer.append(this.basis.encode());
        if (this.upperBound != null) {
            buffer.append(',');
            buffer.append(this.upperBound.encode());
        }
        buffer.append(']');
        return buffer.toString();
    }
}
