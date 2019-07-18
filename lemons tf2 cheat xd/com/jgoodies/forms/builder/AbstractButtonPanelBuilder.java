// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import javax.swing.JButton;
import javax.swing.Action;
import com.jgoodies.common.base.Preconditions;
import javax.swing.JComponent;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.CellConstraints;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import javax.swing.AbstractButton;
import java.util.ArrayList;
import java.awt.ComponentOrientation;
import java.awt.Container;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;

public abstract class AbstractButtonPanelBuilder extends AbstractBuilder
{
    private boolean leftToRight;
    protected boolean focusGrouped;
    
    protected AbstractButtonPanelBuilder(final FormLayout layout, final JPanel container) {
        super(layout, container);
        this.opaque(this.focusGrouped = false);
        final ComponentOrientation orientation = container.getComponentOrientation();
        this.leftToRight = (orientation.isLeftToRight() || !orientation.isHorizontal());
    }
    
    public JPanel getPanel() {
        return this.build();
    }
    
    public JPanel build() {
        if (!this.focusGrouped) {
            final List<AbstractButton> buttons = new ArrayList<AbstractButton>();
            for (final Component component : this.getContainer().getComponents()) {
                if (component instanceof AbstractButton) {
                    buttons.add((AbstractButton)component);
                }
            }
            FocusTraversalUtilsAccessor.tryToBuildAFocusGroup((AbstractButton[])buttons.toArray(new AbstractButton[0]));
            this.focusGrouped = true;
        }
        return (JPanel)this.getContainer();
    }
    
    protected AbstractButtonPanelBuilder background(final Color background) {
        this.getPanel().setBackground(background);
        this.opaque(true);
        return this;
    }
    
    protected AbstractButtonPanelBuilder border(final Border border) {
        this.getPanel().setBorder(border);
        return this;
    }
    
    protected AbstractButtonPanelBuilder opaque(final boolean b) {
        this.getPanel().setOpaque(b);
        return this;
    }
    
    public void setBackground(final Color background) {
        this.getPanel().setBackground(background);
        this.opaque(true);
    }
    
    public void setBorder(final Border border) {
        this.getPanel().setBorder(border);
    }
    
    public void setOpaque(final boolean b) {
        this.getPanel().setOpaque(b);
    }
    
    public final boolean isLeftToRight() {
        return this.leftToRight;
    }
    
    public final void setLeftToRight(final boolean b) {
        this.leftToRight = b;
    }
    
    protected final void nextColumn() {
        this.nextColumn(1);
    }
    
    private void nextColumn(final int columns) {
        final CellConstraints currentCellConstraints = this.currentCellConstraints;
        currentCellConstraints.gridX += columns * this.getColumnIncrementSign();
    }
    
    protected final int getColumn() {
        return this.currentCellConstraints.gridX;
    }
    
    protected final int getRow() {
        return this.currentCellConstraints.gridY;
    }
    
    protected final void nextRow() {
        this.nextRow(1);
    }
    
    private void nextRow(final int rows) {
        final CellConstraints currentCellConstraints = this.currentCellConstraints;
        currentCellConstraints.gridY += rows;
    }
    
    protected final void appendColumn(final ColumnSpec columnSpec) {
        this.getLayout().appendColumn(columnSpec);
    }
    
    protected final void appendGlueColumn() {
        this.appendColumn(FormSpecs.GLUE_COLSPEC);
    }
    
    protected final void appendRelatedComponentsGapColumn() {
        this.appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
    }
    
    protected final void appendUnrelatedComponentsGapColumn() {
        this.appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
    }
    
    protected final void appendRow(final RowSpec rowSpec) {
        this.getLayout().appendRow(rowSpec);
    }
    
    protected final void appendGlueRow() {
        this.appendRow(FormSpecs.GLUE_ROWSPEC);
    }
    
    protected final void appendRelatedComponentsGapRow() {
        this.appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
    }
    
    protected final void appendUnrelatedComponentsGapRow() {
        this.appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
    }
    
    protected Component add(final Component component) {
        this.getContainer().add(component, this.currentCellConstraints);
        this.focusGrouped = false;
        return component;
    }
    
    protected abstract AbstractButtonPanelBuilder addButton(final JComponent p0);
    
    protected AbstractButtonPanelBuilder addButton(final JComponent... buttons) {
        Preconditions.checkNotNull(buttons, "The button array must not be null.");
        Preconditions.checkArgument(buttons.length > 0, "The button array must not be empty.");
        boolean needsGap = false;
        for (final JComponent button : buttons) {
            if (button == null) {
                this.addUnrelatedGap();
                needsGap = false;
            }
            else {
                if (needsGap) {
                    this.addRelatedGap();
                }
                this.addButton(button);
                needsGap = true;
            }
        }
        return this;
    }
    
    protected AbstractButtonPanelBuilder addButton(final Action... actions) {
        Preconditions.checkNotNull(actions, "The Action array must not be null.");
        final int length = actions.length;
        Preconditions.checkArgument(length > 0, "The Action array must not be empty.");
        final JButton[] buttons = new JButton[length];
        for (int i = 0; i < length; ++i) {
            final Action action = actions[i];
            buttons[i] = ((action == null) ? null : this.createButton(action));
        }
        return this.addButton((JComponent[])buttons);
    }
    
    protected abstract AbstractButtonPanelBuilder addRelatedGap();
    
    protected abstract AbstractButtonPanelBuilder addUnrelatedGap();
    
    protected JButton createButton(final Action action) {
        return this.getComponentFactory().createButton(action);
    }
    
    private int getColumnIncrementSign() {
        return this.isLeftToRight() ? 1 : -1;
    }
}
