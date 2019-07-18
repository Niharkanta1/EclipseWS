// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import javax.swing.border.Border;
import java.awt.Color;
import com.jgoodies.forms.layout.Size;
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

public final class ButtonStackBuilder extends AbstractButtonPanelBuilder
{
    private static final ColumnSpec[] COL_SPECS;
    private static final RowSpec[] ROW_SPECS;
    
    public ButtonStackBuilder() {
        this(new JPanel(null));
    }
    
    public ButtonStackBuilder(final JPanel panel) {
        super(new FormLayout(ButtonStackBuilder.COL_SPECS, ButtonStackBuilder.ROW_SPECS), panel);
    }
    
    public static ButtonStackBuilder create() {
        return new ButtonStackBuilder();
    }
    
    public ButtonStackBuilder addButton(final JComponent button) {
        Preconditions.checkNotNull(button, "The button must not be null.");
        this.getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
        this.add(button);
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder addButton(final JComponent... buttons) {
        super.addButton(buttons);
        return this;
    }
    
    public ButtonStackBuilder addButton(final Action... actions) {
        super.addButton(actions);
        return this;
    }
    
    public ButtonStackBuilder addFixed(final JComponent component) {
        this.getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
        this.add(component);
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder addGlue() {
        this.appendGlueRow();
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder addRelatedGap() {
        this.appendRelatedComponentsGapRow();
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder addUnrelatedGap() {
        this.appendUnrelatedComponentsGapRow();
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder addStrut(final ConstantSize size) {
        this.getLayout().appendRow(new RowSpec(RowSpec.TOP, size, 0.0));
        this.nextRow();
        return this;
    }
    
    public ButtonStackBuilder background(final Color background) {
        super.background(background);
        return this;
    }
    
    public ButtonStackBuilder border(final Border border) {
        super.border(border);
        return this;
    }
    
    public ButtonStackBuilder opaque(final boolean b) {
        super.opaque(b);
        return this;
    }
    
    static {
        COL_SPECS = new ColumnSpec[] { FormSpecs.BUTTON_COLSPEC };
        ROW_SPECS = new RowSpec[0];
    }
}
