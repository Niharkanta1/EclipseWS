// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.bean;

import java.beans.PropertyVetoException;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeSupport;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public abstract class Bean implements Serializable, ObservableBean2
{
    protected transient PropertyChangeSupport changeSupport;
    private transient VetoableChangeSupport vetoSupport;
    
    @Override
    public final synchronized void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.changeSupport == null) {
            this.changeSupport = this.createPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(listener);
    }
    
    @Override
    public final synchronized void removePropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null || this.changeSupport == null) {
            return;
        }
        this.changeSupport.removePropertyChangeListener(listener);
    }
    
    @Override
    public final synchronized void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.changeSupport == null) {
            this.changeSupport = this.createPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    @Override
    public final synchronized void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (listener == null || this.changeSupport == null) {
            return;
        }
        this.changeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    public final synchronized void addVetoableChangeListener(final VetoableChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.vetoSupport == null) {
            this.vetoSupport = new VetoableChangeSupport(this);
        }
        this.vetoSupport.addVetoableChangeListener(listener);
    }
    
    public final synchronized void removeVetoableChangeListener(final VetoableChangeListener listener) {
        if (listener == null || this.vetoSupport == null) {
            return;
        }
        this.vetoSupport.removeVetoableChangeListener(listener);
    }
    
    public final synchronized void addVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.vetoSupport == null) {
            this.vetoSupport = new VetoableChangeSupport(this);
        }
        this.vetoSupport.addVetoableChangeListener(propertyName, listener);
    }
    
    public final synchronized void removeVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
        if (listener == null || this.vetoSupport == null) {
            return;
        }
        this.vetoSupport.removeVetoableChangeListener(propertyName, listener);
    }
    
    @Override
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }
    
    @Override
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners(propertyName);
    }
    
    public final synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        if (this.vetoSupport == null) {
            return new VetoableChangeListener[0];
        }
        return this.vetoSupport.getVetoableChangeListeners();
    }
    
    public final synchronized VetoableChangeListener[] getVetoableChangeListeners(final String propertyName) {
        if (this.vetoSupport == null) {
            return new VetoableChangeListener[0];
        }
        return this.vetoSupport.getVetoableChangeListeners(propertyName);
    }
    
    protected PropertyChangeSupport createPropertyChangeSupport(final Object bean) {
        return new PropertyChangeSupport(bean);
    }
    
    protected final void firePropertyChange(final PropertyChangeEvent event) {
        final PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(event);
    }
    
    protected final void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        final PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    protected final void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
        final PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    protected final void firePropertyChange(final String propertyName, final double oldValue, final double newValue) {
        this.firePropertyChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void firePropertyChange(final String propertyName, final float oldValue, final float newValue) {
        this.firePropertyChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        final PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void firePropertyChange(final String propertyName, final long oldValue, final long newValue) {
        this.firePropertyChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void fireMultiplePropertiesChanged() {
        this.firePropertyChange(null, null, null);
    }
    
    protected final void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue, final Object newValue) {
        final PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }
    
    protected final void fireIndexedPropertyChange(final String propertyName, final int index, final int oldValue, final int newValue) {
        if (oldValue == newValue) {
            return;
        }
        this.fireIndexedPropertyChange(propertyName, index, oldValue, (Object)newValue);
    }
    
    protected final void fireIndexedPropertyChange(final String propertyName, final int index, final boolean oldValue, final boolean newValue) {
        if (oldValue == newValue) {
            return;
        }
        this.fireIndexedPropertyChange(propertyName, index, oldValue, (Object)newValue);
    }
    
    protected final void fireVetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
        final VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(event);
    }
    
    protected final void fireVetoableChange(final String propertyName, final Object oldValue, final Object newValue) throws PropertyVetoException {
        final VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }
    
    protected final void fireVetoableChange(final String propertyName, final boolean oldValue, final boolean newValue) throws PropertyVetoException {
        final VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }
    
    protected final void fireVetoableChange(final String propertyName, final double oldValue, final double newValue) throws PropertyVetoException {
        this.fireVetoableChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void fireVetoableChange(final String propertyName, final int oldValue, final int newValue) throws PropertyVetoException {
        final VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void fireVetoableChange(final String propertyName, final float oldValue, final float newValue) throws PropertyVetoException {
        this.fireVetoableChange(propertyName, oldValue, (Object)newValue);
    }
    
    protected final void fireVetoableChange(final String propertyName, final long oldValue, final long newValue) throws PropertyVetoException {
        this.fireVetoableChange(propertyName, oldValue, (Object)newValue);
    }
}
