// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import java.awt.Component;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.ComponentOrientation;
import java.awt.Container;
import com.jgoodies.forms.layout.FormLayout;

public abstract class AbstractFormBuilder extends AbstractBuilder
{
    private boolean leftToRight;
    
    public AbstractFormBuilder(final FormLayout layout, final Container container) {
        super(layout, container);
        final ComponentOrientation orientation = container.getComponentOrientation();
        this.leftToRight = (orientation.isLeftToRight() || !orientation.isHorizontal());
    }
    
    public final boolean isLeftToRight() {
        return this.leftToRight;
    }
    
    public final void setLeftToRight(final boolean b) {
        this.leftToRight = b;
    }
    
    public final int getColumn() {
        return this.currentCellConstraints.gridX;
    }
    
    public final void setColumn(final int column) {
        this.currentCellConstraints.gridX = column;
    }
    
    public final int getRow() {
        return this.currentCellConstraints.gridY;
    }
    
    public final void setRow(final int row) {
        this.currentCellConstraints.gridY = row;
    }
    
    public final void setColumnSpan(final int columnSpan) {
        this.currentCellConstraints.gridWidth = columnSpan;
    }
    
    public final void setRowSpan(final int rowSpan) {
        this.currentCellConstraints.gridHeight = rowSpan;
    }
    
    public final void setOrigin(final int column, final int row) {
        this.setColumn(column);
        this.setRow(row);
    }
    
    public final void setExtent(final int columnSpan, final int rowSpan) {
        this.setColumnSpan(columnSpan);
        this.setRowSpan(rowSpan);
    }
    
    public final void setBounds(final int column, final int row, final int columnSpan, final int rowSpan) {
        this.setColumn(column);
        this.setRow(row);
        this.setColumnSpan(columnSpan);
        this.setRowSpan(rowSpan);
    }
    
    public final void setHAlignment(final CellConstraints.Alignment alignment) {
        this.cellConstraints().hAlign = alignment;
    }
    
    public final void setVAlignment(final CellConstraints.Alignment alignment) {
        this.cellConstraints().vAlign = alignment;
    }
    
    public final void setAlignment(final CellConstraints.Alignment hAlign, final CellConstraints.Alignment vAlign) {
        this.setHAlignment(hAlign);
        this.setVAlignment(vAlign);
    }
    
    public final void nextColumn() {
        this.nextColumn(1);
    }
    
    public final void nextColumn(final int columns) {
        final CellConstraints cellConstraints = this.cellConstraints();
        cellConstraints.gridX += columns * this.getColumnIncrementSign();
    }
    
    public final void nextRow() {
        this.nextRow(1);
    }
    
    public final void nextRow(final int rows) {
        final CellConstraints cellConstraints = this.cellConstraints();
        cellConstraints.gridY += rows;
    }
    
    public final void nextLine() {
        this.nextLine(1);
    }
    
    public final void nextLine(final int lines) {
        this.nextRow(lines);
        this.setColumn(this.getLeadingColumn());
    }
    
    public final void appendColumn(final ColumnSpec columnSpec) {
        this.getLayout().appendColumn(columnSpec);
    }
    
    public final void appendColumn(final String encodedColumnSpec) {
        this.appendColumn(ColumnSpec.decode(encodedColumnSpec));
    }
    
    public final void appendGlueColumn() {
        this.appendColumn(FormSpecs.GLUE_COLSPEC);
    }
    
    public final void appendLabelComponentsGapColumn() {
        this.appendColumn(FormSpecs.LABEL_COMPONENT_GAP_COLSPEC);
    }
    
    public final void appendRelatedComponentsGapColumn() {
        this.appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
    }
    
    public final void appendUnrelatedComponentsGapColumn() {
        this.appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
    }
    
    public final void appendRow(final RowSpec rowSpec) {
        this.getLayout().appendRow(rowSpec);
    }
    
    public final void appendRow(final String encodedRowSpec) {
        this.appendRow(RowSpec.decode(encodedRowSpec));
    }
    
    public final void appendGlueRow() {
        this.appendRow(FormSpecs.GLUE_ROWSPEC);
    }
    
    public final void appendRelatedComponentsGapRow() {
        this.appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
    }
    
    public final void appendUnrelatedComponentsGapRow() {
        this.appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
    }
    
    public final void appendParagraphGapRow() {
        this.appendRow(FormSpecs.PARAGRAPH_GAP_ROWSPEC);
    }
    
    public Component add(final Component component, final CellConstraints cellConstraints) {
        this.getContainer().add(component, cellConstraints);
        return component;
    }
    
    public final Component add(final Component component, final String encodedCellConstraints) {
        this.getContainer().add(component, new CellConstraints(encodedCellConstraints));
        return component;
    }
    
    public final Component add(final Component component) {
        this.add(component, this.cellConstraints());
        return component;
    }
    
    protected final CellConstraints cellConstraints() {
        return this.currentCellConstraints;
    }
    
    protected int getLeadingColumn() {
        return this.isLeftToRight() ? 1 : this.getColumnCount();
    }
    
    protected final int getColumnIncrementSign() {
        return this.isLeftToRight() ? 1 : -1;
    }
    
    protected final CellConstraints createLeftAdjustedConstraints(final int columnSpan) {
        final int firstColumn = this.isLeftToRight() ? this.getColumn() : (this.getColumn() + 1 - columnSpan);
        return new CellConstraints(firstColumn, this.getRow(), columnSpan, this.cellConstraints().gridHeight);
    }
}
