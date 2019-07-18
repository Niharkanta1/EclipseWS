// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.bean;

import java.beans.PropertyChangeListener;

public interface ObservableBean
{
    void addPropertyChangeListener(final PropertyChangeListener p0);
    
    void removePropertyChangeListener(final PropertyChangeListener p0);
}
