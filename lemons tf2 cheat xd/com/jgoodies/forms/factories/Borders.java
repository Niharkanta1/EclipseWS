// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.factories;

import java.awt.Insets;
import java.awt.Component;
import javax.swing.border.AbstractBorder;
import com.jgoodies.forms.util.LayoutStyle;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.ConstantSize;
import javax.swing.border.Border;

public final class Borders
{
    public static final Border EMPTY;
    public static final Border DLU2;
    public static final Border DLU4;
    public static final Border DLU7;
    public static final Border DLU9;
    public static final Border DLU14;
    public static final Border DLU21;
    public static final Border BUTTON_BAR_PAD;
    public static final Border DIALOG;
    public static final Border TABBED_DIALOG;
    
    private Borders() {
    }
    
    public static Border createEmptyBorder(final ConstantSize top, final ConstantSize left, final ConstantSize bottom, final ConstantSize right) {
        return new EmptyBorder(top, left, bottom, right);
    }
    
    public static Border createEmptyBorder(final String encodedSizes) {
        final String[] token = encodedSizes.split("\\s*,\\s*");
        final int tokenCount = token.length;
        Preconditions.checkArgument(token.length == 4, "The border requires 4 sizes, but \"%s\" has %d.", encodedSizes, tokenCount);
        final ConstantSize top = Sizes.constant(token[0], false);
        final ConstantSize left = Sizes.constant(token[1], true);
        final ConstantSize bottom = Sizes.constant(token[2], false);
        final ConstantSize right = Sizes.constant(token[3], true);
        return createEmptyBorder(top, left, bottom, right);
    }
    
    static {
        EMPTY = new javax.swing.border.EmptyBorder(0, 0, 0, 0);
        DLU2 = createEmptyBorder(Sizes.DLUY2, Sizes.DLUX2, Sizes.DLUY2, Sizes.DLUX2);
        DLU4 = createEmptyBorder(Sizes.DLUY4, Sizes.DLUX4, Sizes.DLUY4, Sizes.DLUX4);
        DLU7 = createEmptyBorder(Sizes.DLUY7, Sizes.DLUX7, Sizes.DLUY7, Sizes.DLUX7);
        DLU9 = createEmptyBorder(Sizes.DLUY9, Sizes.DLUX9, Sizes.DLUY9, Sizes.DLUX9);
        DLU14 = createEmptyBorder(Sizes.DLUY14, Sizes.DLUX14, Sizes.DLUY14, Sizes.DLUX14);
        DLU21 = createEmptyBorder(Sizes.DLUY21, Sizes.DLUX21, Sizes.DLUY21, Sizes.DLUX21);
        BUTTON_BAR_PAD = createEmptyBorder(LayoutStyle.getCurrent().getButtonBarPad(), Sizes.dluX(0), Sizes.dluY(0), Sizes.dluX(0));
        DIALOG = createEmptyBorder(LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX(), LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX());
        TABBED_DIALOG = createEmptyBorder(LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX(), LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX());
    }
    
    public static final class EmptyBorder extends AbstractBorder
    {
        private final ConstantSize top;
        private final ConstantSize left;
        private final ConstantSize bottom;
        private final ConstantSize right;
        
        private EmptyBorder(final ConstantSize top, final ConstantSize left, final ConstantSize bottom, final ConstantSize right) {
            if (top == null || left == null || bottom == null || right == null) {
                throw new NullPointerException("The top, left, bottom, and right must not be null.");
            }
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }
        
        @Override
        public Insets getBorderInsets(final Component c, final Insets insets) {
            insets.top = this.top.getPixelSize(c);
            insets.left = this.left.getPixelSize(c);
            insets.bottom = this.bottom.getPixelSize(c);
            insets.right = this.right.getPixelSize(c);
            return insets;
        }
        
        @Override
        public Insets getBorderInsets(final Component c) {
            return this.getBorderInsets(c, new Insets(0, 0, 0, 0));
        }
        
        public ConstantSize top() {
            return this.top;
        }
        
        public ConstantSize left() {
            return this.left;
        }
        
        public ConstantSize bottom() {
            return this.bottom;
        }
        
        public ConstantSize right() {
            return this.right;
        }
    }
}
