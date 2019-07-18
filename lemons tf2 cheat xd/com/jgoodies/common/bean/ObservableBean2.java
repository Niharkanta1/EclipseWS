// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.bean;

import java.beans.PropertyChangeListener;

public interface ObservableBean2 extends ObservableBean
{
    void addPropertyChangeListener(final String p0, final PropertyChangeListener p1);
    
    void removePropertyChangeListener(final String p0, final PropertyChangeListener p1);
    
    PropertyChangeListener[] getPropertyChangeListeners();
    
    PropertyChangeListener[] getPropertyChangeListeners(final String p0);
}
