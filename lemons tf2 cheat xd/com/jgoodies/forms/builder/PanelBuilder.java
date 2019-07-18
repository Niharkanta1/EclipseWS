// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import com.jgoodies.common.base.Preconditions;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import java.awt.Component;
import com.jgoodies.forms.layout.CellConstraints;
import javax.swing.JLabel;
import java.awt.FocusTraversalPolicy;
import com.jgoodies.forms.factories.Borders;
import javax.swing.border.Border;
import java.awt.Color;
import com.jgoodies.forms.FormsSetup;
import java.awt.Container;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import java.lang.ref.WeakReference;

public class PanelBuilder extends AbstractFormBuilder
{
    private static final String LABELED_BY_PROPERTY = "labeledBy";
    private boolean labelForFeatureEnabled;
    private WeakReference mostRecentlyAddedLabelReference;
    
    public PanelBuilder(final FormLayout layout) {
        this(layout, new JPanel(null));
    }
    
    public PanelBuilder(final FormLayout layout, final JPanel panel) {
        super(layout, panel);
        this.mostRecentlyAddedLabelReference = null;
        this.opaque(FormsSetup.getOpaqueDefault());
        this.labelForFeatureEnabled = FormsSetup.getLabelForFeatureEnabledDefault();
    }
    
    public final JPanel getPanel() {
        return (JPanel)this.getContainer();
    }
    
    public PanelBuilder background(final Color background) {
        this.getPanel().setBackground(background);
        this.opaque(true);
        return this;
    }
    
    public PanelBuilder border(final Border border) {
        this.getPanel().setBorder(border);
        return this;
    }
    
    public PanelBuilder border(final String emptyBorderSpec) {
        this.border(Borders.createEmptyBorder(emptyBorderSpec));
        return this;
    }
    
    public PanelBuilder opaque(final boolean b) {
        this.getPanel().setOpaque(b);
        return this;
    }
    
    public PanelBuilder focusTraversal(final FocusTraversalPolicy policy) {
        this.getPanel().setFocusTraversalPolicy(policy);
        this.getPanel().setFocusTraversalPolicyProvider(true);
        return this;
    }
    
    public PanelBuilder labelForFeatureEnabled(final boolean b) {
        this.labelForFeatureEnabled = b;
        return this;
    }
    
    @Deprecated
    public void setBorder(final Border border) {
        this.getPanel().setBorder(border);
    }
    
    @Deprecated
    public void setDefaultDialogBorder() {
        this.border(Borders.DIALOG);
    }
    
    @Deprecated
    public void setOpaque(final boolean b) {
        this.getPanel().setOpaque(b);
    }
    
    public final JPanel build() {
        return this.getPanel();
    }
    
    public final JLabel addLabel(final String textWithMnemonic) {
        return this.addLabel(textWithMnemonic, this.cellConstraints());
    }
    
    public final JLabel addLabel(final String textWithMnemonic, final CellConstraints constraints) {
        final JLabel label = this.getComponentFactory().createLabel(textWithMnemonic);
        this.add(label, constraints);
        return label;
    }
    
    public final JLabel addLabel(final String textWithMnemonic, final String encodedConstraints) {
        return this.addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }
    
    public final JLabel addLabel(final String textWithMnemonic, final CellConstraints labelConstraints, final Component component, final CellConstraints componentConstraints) {
        if (labelConstraints == componentConstraints) {
            throw new IllegalArgumentException("You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC class. See the JavaDocs for details.");
        }
        final JLabel label = this.addLabel(textWithMnemonic, labelConstraints);
        this.add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }
    
    public final JLabel addROLabel(final String textWithMnemonic) {
        return this.addROLabel(textWithMnemonic, this.cellConstraints());
    }
    
    public final JLabel addROLabel(final String textWithMnemonic, final CellConstraints constraints) {
        final JLabel label = this.getComponentFactory().createReadOnlyLabel(textWithMnemonic);
        this.add(label, constraints);
        return label;
    }
    
    public final JLabel addROLabel(final String textWithMnemonic, final String encodedConstraints) {
        return this.addROLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }
    
    public final JLabel addROLabel(final String textWithMnemonic, final CellConstraints labelConstraints, final Component component, final CellConstraints componentConstraints) {
        checkConstraints(labelConstraints, componentConstraints);
        final JLabel label = this.addROLabel(textWithMnemonic, labelConstraints);
        this.add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }
    
    public final JLabel addTitle(final String textWithMnemonic) {
        return this.addTitle(textWithMnemonic, this.cellConstraints());
    }
    
    public final JLabel addTitle(final String textWithMnemonic, final CellConstraints constraints) {
        final JLabel titleLabel = this.getComponentFactory().createTitle(textWithMnemonic);
        this.add(titleLabel, constraints);
        return titleLabel;
    }
    
    public final JLabel addTitle(final String textWithMnemonic, final String encodedConstraints) {
        return this.addTitle(textWithMnemonic, new CellConstraints(encodedConstraints));
    }
    
    public final JComponent addSeparator(final String textWithMnemonic) {
        return this.addSeparator(textWithMnemonic, this.getLayout().getColumnCount());
    }
    
    public final JComponent addSeparator(final String textWithMnemonic, final CellConstraints constraints) {
        final int titleAlignment = this.isLeftToRight() ? 2 : 4;
        final JComponent titledSeparator = this.getComponentFactory().createSeparator(textWithMnemonic, titleAlignment);
        this.add(titledSeparator, constraints);
        return titledSeparator;
    }
    
    public final JComponent addSeparator(final String textWithMnemonic, final String encodedConstraints) {
        return this.addSeparator(textWithMnemonic, new CellConstraints(encodedConstraints));
    }
    
    public final JComponent addSeparator(final String textWithMnemonic, final int columnSpan) {
        return this.addSeparator(textWithMnemonic, this.createLeftAdjustedConstraints(columnSpan));
    }
    
    public final JLabel add(final JLabel label, final CellConstraints labelConstraints, final Component component, final CellConstraints componentConstraints) {
        checkConstraints(labelConstraints, componentConstraints);
        this.add(label, labelConstraints);
        this.add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }
    
    @Override
    public Component add(final Component component, final CellConstraints cellConstraints) {
        final Component result = super.add(component, cellConstraints);
        this.manageLabelsAndComponents(component);
        return result;
    }
    
    private void manageLabelsAndComponents(final Component c) {
        if (!this.labelForFeatureEnabled) {
            return;
        }
        if (c instanceof JLabel) {
            final JLabel label = (JLabel)c;
            if (label.getLabelFor() == null) {
                this.setMostRecentlyAddedLabel(label);
            }
            else {
                this.clearMostRecentlyAddedLabel();
            }
            return;
        }
        final JLabel mostRecentlyAddedLabel = this.getMostRecentlyAddedLabel();
        if (mostRecentlyAddedLabel != null && this.isLabelForApplicable(mostRecentlyAddedLabel, c)) {
            this.setLabelFor(mostRecentlyAddedLabel, c);
            this.clearMostRecentlyAddedLabel();
        }
    }
    
    protected boolean isLabelForApplicable(final JLabel label, final Component component) {
        if (label.getLabelFor() != null) {
            return false;
        }
        if (!component.isFocusable()) {
            return false;
        }
        if (!(component instanceof JComponent)) {
            return true;
        }
        final JComponent c = (JComponent)component;
        return c.getClientProperty("labeledBy") == null;
    }
    
    protected void setLabelFor(final JLabel label, final Component component) {
        Component labeledComponent;
        if (component instanceof JScrollPane) {
            final JScrollPane scrollPane = (JScrollPane)component;
            labeledComponent = scrollPane.getViewport().getView();
        }
        else {
            labeledComponent = component;
        }
        label.setLabelFor(labeledComponent);
    }
    
    private JLabel getMostRecentlyAddedLabel() {
        if (this.mostRecentlyAddedLabelReference == null) {
            return null;
        }
        final JLabel label = (JLabel)this.mostRecentlyAddedLabelReference.get();
        if (label == null) {
            return null;
        }
        return label;
    }
    
    private void setMostRecentlyAddedLabel(final JLabel label) {
        this.mostRecentlyAddedLabelReference = new WeakReference((T)label);
    }
    
    private void clearMostRecentlyAddedLabel() {
        this.mostRecentlyAddedLabelReference = null;
    }
    
    private static void checkConstraints(final CellConstraints c1, final CellConstraints c2) {
        Preconditions.checkArgument(c1 != c2, "You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC factory. See the JavaDocs for details.");
    }
}
