// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Component;
import com.jgoodies.forms.layout.ConstantSize;
import javax.swing.border.Border;
import java.awt.Color;
import com.jgoodies.forms.layout.FormSpecs;
import java.util.ResourceBundle;
import com.jgoodies.common.internal.StringResourceAccessor;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public final class DefaultFormBuilder extends I15dPanelBuilder
{
    private RowSpec defaultRowSpec;
    private RowSpec lineGapSpec;
    private RowSpec paragraphGapSpec;
    private int leadingColumnOffset;
    private boolean rowGroupingEnabled;
    
    public DefaultFormBuilder(final FormLayout layout) {
        this(layout, new JPanel(null));
    }
    
    public DefaultFormBuilder(final FormLayout layout, final JPanel container) {
        this(layout, (StringResourceAccessor)null, container);
    }
    
    public DefaultFormBuilder(final FormLayout layout, final ResourceBundle bundle) {
        super(layout, bundle);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }
    
    public DefaultFormBuilder(final FormLayout layout, final ResourceBundle bundle, final JPanel container) {
        super(layout, bundle, container);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }
    
    public DefaultFormBuilder(final FormLayout layout, final StringResourceAccessor localizer) {
        super(layout, localizer);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }
    
    public DefaultFormBuilder(final FormLayout layout, final StringResourceAccessor localizer, final JPanel container) {
        super(layout, localizer, container);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }
    
    @Override
    public DefaultFormBuilder background(final Color background) {
        super.background(background);
        return this;
    }
    
    @Override
    public DefaultFormBuilder border(final Border border) {
        super.border(border);
        return this;
    }
    
    @Override
    public DefaultFormBuilder border(final String emptyBorderSpec) {
        super.border(emptyBorderSpec);
        return this;
    }
    
    @Override
    public DefaultFormBuilder opaque(final boolean b) {
        super.opaque(b);
        return this;
    }
    
    public DefaultFormBuilder defaultRowSpec(final RowSpec defaultRowSpec) {
        this.defaultRowSpec = defaultRowSpec;
        return this;
    }
    
    public DefaultFormBuilder lineGapSize(final ConstantSize lineGapSize) {
        final RowSpec rowSpec = RowSpec.createGap(lineGapSize);
        this.lineGapSpec = rowSpec;
        return this;
    }
    
    public DefaultFormBuilder paragraphGapSize(final ConstantSize paragraphGapSize) {
        final RowSpec rowSpec = RowSpec.createGap(paragraphGapSize);
        this.paragraphGapSpec = rowSpec;
        return this;
    }
    
    public DefaultFormBuilder leadingColumnOffset(final int columnOffset) {
        this.leadingColumnOffset = columnOffset;
        return this;
    }
    
    public DefaultFormBuilder rowGroupingEnabled(final boolean enabled) {
        this.rowGroupingEnabled = enabled;
        return this;
    }
    
    @Deprecated
    public RowSpec getDefaultRowSpec() {
        return this.defaultRowSpec;
    }
    
    @Deprecated
    public void setDefaultRowSpec(final RowSpec defaultRowSpec) {
        this.defaultRowSpec = defaultRowSpec;
    }
    
    @Deprecated
    public RowSpec getLineGapSpec() {
        return this.lineGapSpec;
    }
    
    @Deprecated
    public void setLineGapSize(final ConstantSize lineGapSize) {
        final RowSpec rowSpec = RowSpec.createGap(lineGapSize);
        this.lineGapSpec = rowSpec;
    }
    
    @Deprecated
    public void setParagraphGapSize(final ConstantSize paragraphGapSize) {
        final RowSpec rowSpec = RowSpec.createGap(paragraphGapSize);
        this.paragraphGapSpec = rowSpec;
    }
    
    @Deprecated
    public int getLeadingColumnOffset() {
        return this.leadingColumnOffset;
    }
    
    @Deprecated
    public void setLeadingColumnOffset(final int columnOffset) {
        this.leadingColumnOffset = columnOffset;
    }
    
    @Deprecated
    public boolean isRowGroupingEnabled() {
        return this.rowGroupingEnabled;
    }
    
    @Deprecated
    public void setRowGroupingEnabled(final boolean enabled) {
        this.rowGroupingEnabled = enabled;
    }
    
    public final void appendLineGapRow() {
        this.appendRow(this.lineGapSpec);
    }
    
    public void append(final Component component) {
        this.append(component, 1);
    }
    
    public void append(final Component component, final int columnSpan) {
        this.ensureCursorColumnInGrid();
        this.ensureHasGapRow(this.lineGapSpec);
        this.ensureHasComponentLine();
        this.add(component, this.createLeftAdjustedConstraints(columnSpan));
        this.nextColumn(columnSpan + 1);
    }
    
    public void append(final Component c1, final Component c2) {
        this.append(c1);
        this.append(c2);
    }
    
    public void append(final Component c1, final Component c2, final Component c3) {
        this.append(c1);
        this.append(c2);
        this.append(c3);
    }
    
    public JLabel append(final String textWithMnemonic) {
        final JLabel label = this.getComponentFactory().createLabel(textWithMnemonic);
        this.append(label);
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component component) {
        return this.append(textWithMnemonic, component, 1);
    }
    
    public JLabel append(final String textWithMnemonic, final Component c, final boolean nextLine) {
        final JLabel label = this.append(textWithMnemonic, c);
        if (nextLine) {
            this.nextLine();
        }
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component c, final int columnSpan) {
        final JLabel label = this.append(textWithMnemonic);
        label.setLabelFor(c);
        this.append(c, columnSpan);
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component c1, final Component c2) {
        final JLabel label = this.append(textWithMnemonic, c1);
        this.append(c2);
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component c1, final Component c2, final int colSpan) {
        final JLabel label = this.append(textWithMnemonic, c1);
        this.append(c2, colSpan);
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component c1, final Component c2, final Component c3) {
        final JLabel label = this.append(textWithMnemonic, c1, c2);
        this.append(c3);
        return label;
    }
    
    public JLabel append(final String textWithMnemonic, final Component c1, final Component c2, final Component c3, final Component c4) {
        final JLabel label = this.append(textWithMnemonic, c1, c2, c3);
        this.append(c4);
        return label;
    }
    
    public JLabel appendI15d(final String resourceKey) {
        return this.append(this.getResourceString(resourceKey));
    }
    
    public JLabel appendI15d(final String resourceKey, final Component component) {
        return this.append(this.getResourceString(resourceKey), component, 1);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component component, final boolean nextLine) {
        return this.append(this.getResourceString(resourceKey), component, nextLine);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component c, final int columnSpan) {
        return this.append(this.getResourceString(resourceKey), c, columnSpan);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component c1, final Component c2) {
        return this.append(this.getResourceString(resourceKey), c1, c2);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component c1, final Component c2, final int colSpan) {
        return this.append(this.getResourceString(resourceKey), c1, c2, colSpan);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component c1, final Component c2, final Component c3) {
        return this.append(this.getResourceString(resourceKey), c1, c2, c3);
    }
    
    public JLabel appendI15d(final String resourceKey, final Component c1, final Component c2, final Component c3, final Component c4) {
        return this.append(this.getResourceString(resourceKey), c1, c2, c3, c4);
    }
    
    public JLabel appendTitle(final String textWithMnemonic) {
        final JLabel titleLabel = this.getComponentFactory().createTitle(textWithMnemonic);
        this.append(titleLabel);
        return titleLabel;
    }
    
    public JLabel appendI15dTitle(final String resourceKey) {
        return this.appendTitle(this.getResourceString(resourceKey));
    }
    
    public JComponent appendSeparator() {
        return this.appendSeparator("");
    }
    
    public JComponent appendSeparator(final String text) {
        this.ensureCursorColumnInGrid();
        this.ensureHasGapRow(this.paragraphGapSpec);
        this.ensureHasComponentLine();
        this.setColumn(super.getLeadingColumn());
        final int columnSpan = this.getColumnCount();
        this.setColumnSpan(this.getColumnCount());
        final JComponent titledSeparator = this.addSeparator(text);
        this.setColumnSpan(1);
        this.nextColumn(columnSpan);
        return titledSeparator;
    }
    
    public JComponent appendI15dSeparator(final String resourceKey) {
        return this.appendSeparator(this.getResourceString(resourceKey));
    }
    
    @Override
    protected int getLeadingColumn() {
        final int column = super.getLeadingColumn();
        return column + this.leadingColumnOffset * this.getColumnIncrementSign();
    }
    
    private void ensureCursorColumnInGrid() {
        if ((this.isLeftToRight() && this.getColumn() > this.getColumnCount()) || (!this.isLeftToRight() && this.getColumn() < 1)) {
            this.nextLine();
        }
    }
    
    private void ensureHasGapRow(final RowSpec gapRowSpec) {
        if (this.getRow() == 1 || this.getRow() <= this.getRowCount()) {
            return;
        }
        if (this.getRow() <= this.getRowCount()) {
            final RowSpec rowSpec = this.getCursorRowSpec();
            if (rowSpec == gapRowSpec) {
                return;
            }
        }
        this.appendRow(gapRowSpec);
        this.nextLine();
    }
    
    private void ensureHasComponentLine() {
        if (this.getRow() <= this.getRowCount()) {
            return;
        }
        this.appendRow(this.defaultRowSpec);
        if (this.rowGroupingEnabled) {
            this.getLayout().addGroupedRow(this.getRow());
        }
    }
    
    private RowSpec getCursorRowSpec() {
        return this.getLayout().getRowSpec(this.getRow());
    }
}
