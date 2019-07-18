// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import com.jgoodies.common.internal.StringResourceAccess;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.FocusTraversalPolicy;
import javax.swing.border.Border;
import java.awt.Color;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.common.internal.ResourceBundleAccessor;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import java.util.ResourceBundle;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.common.internal.StringResourceAccessor;

public class I15dPanelBuilder extends PanelBuilder
{
    private final StringResourceAccessor resources;
    private boolean debugToolTipsEnabled;
    
    public I15dPanelBuilder(final FormLayout layout, final ResourceBundle bundle) {
        this(layout, bundle, new JPanel(null));
    }
    
    public I15dPanelBuilder(final FormLayout layout, final ResourceBundle bundle, final JPanel container) {
        this(layout, new ResourceBundleAccessor(bundle), container);
    }
    
    public I15dPanelBuilder(final FormLayout layout, final StringResourceAccessor localizer) {
        this(layout, localizer, new JPanel(null));
    }
    
    public I15dPanelBuilder(final FormLayout layout, final StringResourceAccessor localizer, final JPanel container) {
        super(layout, container);
        this.debugToolTipsEnabled = FormsSetup.getDebugToolTipsEnabledDefault();
        this.resources = localizer;
    }
    
    @Override
    public I15dPanelBuilder background(final Color background) {
        super.background(background);
        return this;
    }
    
    @Override
    public I15dPanelBuilder border(final Border border) {
        super.border(border);
        return this;
    }
    
    @Override
    public I15dPanelBuilder border(final String emptyBorderSpec) {
        super.border(emptyBorderSpec);
        return this;
    }
    
    @Override
    public I15dPanelBuilder opaque(final boolean b) {
        super.opaque(b);
        return this;
    }
    
    @Override
    public I15dPanelBuilder focusTraversal(final FocusTraversalPolicy policy) {
        super.focusTraversal(policy);
        return this;
    }
    
    public I15dPanelBuilder debugToolTipsEnabled(final boolean b) {
        this.debugToolTipsEnabled = b;
        return this;
    }
    
    public final JLabel addI15dLabel(final String resourceKey, final CellConstraints constraints) {
        final JLabel label = this.addLabel(this.getResourceString(resourceKey), constraints);
        if (this.isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }
    
    public final JLabel addI15dLabel(final String resourceKey, final String encodedConstraints) {
        return this.addI15dLabel(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    public final JLabel addI15dLabel(final String resourceKey, final CellConstraints labelConstraints, final Component component, final CellConstraints componentConstraints) {
        final JLabel label = this.addLabel(this.getResourceString(resourceKey), labelConstraints, component, componentConstraints);
        if (this.isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }
    
    public final JLabel addI15dROLabel(final String resourceKey, final CellConstraints constraints) {
        final JLabel label = this.addROLabel(this.getResourceString(resourceKey), constraints);
        if (this.isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }
    
    public final JLabel addI15dROLabel(final String resourceKey, final String encodedConstraints) {
        return this.addI15dROLabel(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    public final JLabel addI15dROLabel(final String resourceKey, final CellConstraints labelConstraints, final Component component, final CellConstraints componentConstraints) {
        final JLabel label = this.addROLabel(this.getResourceString(resourceKey), labelConstraints, component, componentConstraints);
        if (this.isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }
    
    public final JComponent addI15dSeparator(final String resourceKey, final CellConstraints constraints) {
        final JComponent component = this.addSeparator(this.getResourceString(resourceKey), constraints);
        if (this.isDebugToolTipsEnabled()) {
            component.setToolTipText(resourceKey);
        }
        return component;
    }
    
    public final JComponent addI15dSeparator(final String resourceKey, final String encodedConstraints) {
        return this.addI15dSeparator(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    public final JLabel addI15dTitle(final String resourceKey, final CellConstraints constraints) {
        final JLabel label = this.addTitle(this.getResourceString(resourceKey), constraints);
        if (this.isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }
    
    public final JLabel addI15dTitle(final String resourceKey, final String encodedConstraints) {
        return this.addI15dTitle(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    protected final boolean isDebugToolTipsEnabled() {
        return this.debugToolTipsEnabled;
    }
    
    protected final String getResourceString(final String key) {
        return StringResourceAccess.getResourceString(this.resources, key, new Object[0]);
    }
}
