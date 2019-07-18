// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.collect;

public interface ObservableList2<E> extends ObservableList<E>
{
    void fireContentsChanged(final int p0);
    
    void fireContentsChanged(final int p0, final int p1);
}
