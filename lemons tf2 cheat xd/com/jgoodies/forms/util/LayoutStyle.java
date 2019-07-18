// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;
import com.jgoodies.common.base.SystemUtils;

public abstract class LayoutStyle
{
    private static LayoutStyle current;
    
    private static LayoutStyle initialLayoutStyle() {
        return SystemUtils.IS_OS_MAC ? MacLayoutStyle.INSTANCE : WindowsLayoutStyle.INSTANCE;
    }
    
    public static LayoutStyle getCurrent() {
        return LayoutStyle.current;
    }
    
    public static void setCurrent(final LayoutStyle newLayoutStyle) {
        LayoutStyle.current = newLayoutStyle;
    }
    
    public abstract Size getDefaultButtonWidth();
    
    public abstract Size getDefaultButtonHeight();
    
    public abstract ConstantSize getDialogMarginX();
    
    public abstract ConstantSize getDialogMarginY();
    
    public abstract ConstantSize getTabbedDialogMarginX();
    
    public abstract ConstantSize getTabbedDialogMarginY();
    
    public abstract ConstantSize getLabelComponentPadX();
    
    public abstract ConstantSize getLabelComponentPadY();
    
    public abstract ConstantSize getRelatedComponentsPadX();
    
    public abstract ConstantSize getRelatedComponentsPadY();
    
    public abstract ConstantSize getUnrelatedComponentsPadX();
    
    public abstract ConstantSize getUnrelatedComponentsPadY();
    
    public abstract ConstantSize getNarrowLinePad();
    
    public abstract ConstantSize getLinePad();
    
    public abstract ConstantSize getParagraphPad();
    
    public abstract ConstantSize getButtonBarPad();
    
    static {
        LayoutStyle.current = initialLayoutStyle();
    }
}
