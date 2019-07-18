// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import javax.swing.border.Border;
import java.awt.Color;
import com.jgoodies.forms.layout.ConstantSize;
import javax.swing.Action;
import java.awt.Component;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.common.base.Preconditions;
import javax.swing.JComponent;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.ColumnSpec;

public final class ButtonBarBuilder extends AbstractButtonPanelBuilder
{
    private static final ColumnSpec[] COL_SPECS;
    private static final RowSpec[] ROW_SPECS;
    
    public ButtonBarBuilder() {
        this(new JPanel(null));
    }
    
    public ButtonBarBuilder(final JPanel panel) {
        super(new FormLayout(ButtonBarBuilder.COL_SPECS, ButtonBarBuilder.ROW_SPECS), panel);
    }
    
    public static ButtonBarBuilder create() {
        return new ButtonBarBuilder();
    }
    
    public ButtonBarBuilder addButton(final JComponent button) {
        Preconditions.checkNotNull(button, "The button to add must not be null.");
        this.getLayout().appendColumn(FormSpecs.BUTTON_COLSPEC);
        this.add(button);
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addButton(final JComponent... buttons) {
        super.addButton(buttons);
        return this;
    }
    
    public ButtonBarBuilder addButton(final Action... actions) {
        super.addButton(actions);
        return this;
    }
    
    public ButtonBarBuilder addFixed(final JComponent component) {
        this.getLayout().appendColumn(FormSpecs.PREF_COLSPEC);
        this.add(component);
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addGrowing(final JComponent component) {
        this.getLayout().appendColumn(FormSpecs.GROWING_BUTTON_COLSPEC);
        this.add(component);
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addGlue() {
        this.appendGlueColumn();
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addRelatedGap() {
        this.appendRelatedComponentsGapColumn();
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addUnrelatedGap() {
        this.appendUnrelatedComponentsGapColumn();
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder addStrut(final ConstantSize width) {
        this.getLayout().appendColumn(ColumnSpec.createGap(width));
        this.nextColumn();
        return this;
    }
    
    public ButtonBarBuilder background(final Color background) {
        super.background(background);
        return this;
    }
    
    public ButtonBarBuilder border(final Border border) {
        super.border(border);
        return this;
    }
    
    public ButtonBarBuilder opaque(final boolean b) {
        super.opaque(b);
        return this;
    }
    
    static {
        COL_SPECS = new ColumnSpec[0];
        ROW_SPECS = new RowSpec[] { RowSpec.decode("center:pref") };
    }
}
