// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import com.jgoodies.forms.util.LayoutStyle;

public final class FormSpecs
{
    public static final ColumnSpec MIN_COLSPEC;
    public static final ColumnSpec PREF_COLSPEC;
    public static final ColumnSpec DEFAULT_COLSPEC;
    public static final ColumnSpec GLUE_COLSPEC;
    public static final ColumnSpec LABEL_COMPONENT_GAP_COLSPEC;
    public static final ColumnSpec RELATED_GAP_COLSPEC;
    public static final ColumnSpec UNRELATED_GAP_COLSPEC;
    public static final ColumnSpec BUTTON_COLSPEC;
    public static final ColumnSpec GROWING_BUTTON_COLSPEC;
    public static final RowSpec MIN_ROWSPEC;
    public static final RowSpec PREF_ROWSPEC;
    public static final RowSpec DEFAULT_ROWSPEC;
    public static final RowSpec GLUE_ROWSPEC;
    public static final RowSpec LABEL_COMPONENT_GAP_ROWSPEC;
    public static final RowSpec RELATED_GAP_ROWSPEC;
    public static final RowSpec UNRELATED_GAP_ROWSPEC;
    public static final RowSpec NARROW_LINE_GAP_ROWSPEC;
    public static final RowSpec LINE_GAP_ROWSPEC;
    public static final RowSpec PARAGRAPH_GAP_ROWSPEC;
    public static final RowSpec BUTTON_ROWSPEC;
    
    private FormSpecs() {
    }
    
    static {
        MIN_COLSPEC = new ColumnSpec(Sizes.MINIMUM);
        PREF_COLSPEC = new ColumnSpec(Sizes.PREFERRED);
        DEFAULT_COLSPEC = new ColumnSpec(Sizes.DEFAULT);
        GLUE_COLSPEC = new ColumnSpec(ColumnSpec.DEFAULT, Sizes.ZERO, 1.0);
        LABEL_COMPONENT_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getLabelComponentPadX());
        RELATED_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX());
        UNRELATED_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getUnrelatedComponentsPadX());
        BUTTON_COLSPEC = new ColumnSpec(Sizes.bounded(Sizes.PREFERRED, LayoutStyle.getCurrent().getDefaultButtonWidth(), null));
        GROWING_BUTTON_COLSPEC = new ColumnSpec(ColumnSpec.DEFAULT, FormSpecs.BUTTON_COLSPEC.getSize(), 1.0);
        MIN_ROWSPEC = new RowSpec(Sizes.MINIMUM);
        PREF_ROWSPEC = new RowSpec(Sizes.PREFERRED);
        DEFAULT_ROWSPEC = new RowSpec(Sizes.DEFAULT);
        GLUE_ROWSPEC = new RowSpec(RowSpec.DEFAULT, Sizes.ZERO, 1.0);
        LABEL_COMPONENT_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getLabelComponentPadY());
        RELATED_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY());
        UNRELATED_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getUnrelatedComponentsPadY());
        NARROW_LINE_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getNarrowLinePad());
        LINE_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getLinePad());
        PARAGRAPH_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getParagraphPad());
        BUTTON_ROWSPEC = new RowSpec(Sizes.bounded(Sizes.PREFERRED, LayoutStyle.getCurrent().getDefaultButtonHeight(), null));
    }
}
