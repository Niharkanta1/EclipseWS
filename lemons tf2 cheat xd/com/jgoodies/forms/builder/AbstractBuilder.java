// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import com.jgoodies.forms.FormsSetup;
import java.awt.LayoutManager;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;

public abstract class AbstractBuilder
{
    private final Container container;
    private final FormLayout layout;
    protected final CellConstraints currentCellConstraints;
    private ComponentFactory componentFactory;
    
    protected AbstractBuilder(final FormLayout layout, final Container container) {
        this.layout = Preconditions.checkNotNull(layout, "The layout must not be null.");
        this.container = Preconditions.checkNotNull(container, "The layout container must not be null.");
        container.setLayout(layout);
        this.currentCellConstraints = new CellConstraints();
    }
    
    public final Container getContainer() {
        return this.container;
    }
    
    public final FormLayout getLayout() {
        return this.layout;
    }
    
    public final int getColumnCount() {
        return this.getLayout().getColumnCount();
    }
    
    public final int getRowCount() {
        return this.getLayout().getRowCount();
    }
    
    public final ComponentFactory getComponentFactory() {
        if (this.componentFactory == null) {
            this.componentFactory = this.createComponentFactory();
        }
        return this.componentFactory;
    }
    
    public final void setComponentFactory(final ComponentFactory newFactory) {
        this.componentFactory = newFactory;
    }
    
    protected ComponentFactory createComponentFactory() {
        return FormsSetup.getComponentFactoryDefault();
    }
}
