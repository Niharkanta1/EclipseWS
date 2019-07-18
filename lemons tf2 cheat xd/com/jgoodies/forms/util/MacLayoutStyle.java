// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;

public final class MacLayoutStyle extends LayoutStyle
{
    static final MacLayoutStyle INSTANCE;
    private static final Size BUTTON_WIDTH;
    private static final Size BUTTON_HEIGHT;
    private static final ConstantSize DIALOG_MARGIN_X;
    private static final ConstantSize DIALOG_MARGIN_Y;
    private static final ConstantSize TABBED_DIALOG_MARGIN_X;
    private static final ConstantSize TABBED_DIALOG_MARGIN_Y;
    private static final ConstantSize LABEL_COMPONENT_PADX;
    private static final ConstantSize RELATED_COMPONENTS_PADX;
    private static final ConstantSize UNRELATED_COMPONENTS_PADX;
    private static final ConstantSize LABEL_COMPONENT_PADY;
    private static final ConstantSize RELATED_COMPONENTS_PADY;
    private static final ConstantSize UNRELATED_COMPONENTS_PADY;
    private static final ConstantSize NARROW_LINE_PAD;
    private static final ConstantSize LINE_PAD;
    private static final ConstantSize PARAGRAPH_PAD;
    private static final ConstantSize BUTTON_BAR_PAD;
    
    private MacLayoutStyle() {
    }
    
    @Override
    public Size getDefaultButtonWidth() {
        return MacLayoutStyle.BUTTON_WIDTH;
    }
    
    @Override
    public Size getDefaultButtonHeight() {
        return MacLayoutStyle.BUTTON_HEIGHT;
    }
    
    @Override
    public ConstantSize getDialogMarginX() {
        return MacLayoutStyle.DIALOG_MARGIN_X;
    }
    
    @Override
    public ConstantSize getDialogMarginY() {
        return MacLayoutStyle.DIALOG_MARGIN_Y;
    }
    
    @Override
    public ConstantSize getTabbedDialogMarginX() {
        return MacLayoutStyle.TABBED_DIALOG_MARGIN_X;
    }
    
    @Override
    public ConstantSize getTabbedDialogMarginY() {
        return MacLayoutStyle.TABBED_DIALOG_MARGIN_Y;
    }
    
    @Override
    public ConstantSize getLabelComponentPadX() {
        return MacLayoutStyle.LABEL_COMPONENT_PADX;
    }
    
    @Override
    public ConstantSize getLabelComponentPadY() {
        return MacLayoutStyle.LABEL_COMPONENT_PADY;
    }
    
    @Override
    public ConstantSize getRelatedComponentsPadX() {
        return MacLayoutStyle.RELATED_COMPONENTS_PADX;
    }
    
    @Override
    public ConstantSize getRelatedComponentsPadY() {
        return MacLayoutStyle.RELATED_COMPONENTS_PADY;
    }
    
    @Override
    public ConstantSize getUnrelatedComponentsPadX() {
        return MacLayoutStyle.UNRELATED_COMPONENTS_PADX;
    }
    
    @Override
    public ConstantSize getUnrelatedComponentsPadY() {
        return MacLayoutStyle.UNRELATED_COMPONENTS_PADY;
    }
    
    @Override
    public ConstantSize getNarrowLinePad() {
        return MacLayoutStyle.NARROW_LINE_PAD;
    }
    
    @Override
    public ConstantSize getLinePad() {
        return MacLayoutStyle.LINE_PAD;
    }
    
    @Override
    public ConstantSize getParagraphPad() {
        return MacLayoutStyle.PARAGRAPH_PAD;
    }
    
    @Override
    public ConstantSize getButtonBarPad() {
        return MacLayoutStyle.BUTTON_BAR_PAD;
    }
    
    static {
        INSTANCE = new MacLayoutStyle();
        BUTTON_WIDTH = Sizes.dluX(55);
        BUTTON_HEIGHT = Sizes.dluY(14);
        DIALOG_MARGIN_X = Sizes.DLUX9;
        DIALOG_MARGIN_Y = Sizes.DLUY9;
        TABBED_DIALOG_MARGIN_X = Sizes.DLUX4;
        TABBED_DIALOG_MARGIN_Y = Sizes.DLUY4;
        LABEL_COMPONENT_PADX = Sizes.DLUX1;
        RELATED_COMPONENTS_PADX = Sizes.DLUX2;
        UNRELATED_COMPONENTS_PADX = Sizes.DLUX4;
        LABEL_COMPONENT_PADY = Sizes.DLUY2;
        RELATED_COMPONENTS_PADY = Sizes.DLUY3;
        UNRELATED_COMPONENTS_PADY = Sizes.DLUY6;
        NARROW_LINE_PAD = Sizes.DLUY2;
        LINE_PAD = Sizes.DLUY3;
        PARAGRAPH_PAD = Sizes.DLUY9;
        BUTTON_BAR_PAD = Sizes.DLUY4;
    }
}
