// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.factories;

import com.jgoodies.forms.layout.CellConstraints;
import java.io.Serializable;

public final class CC implements Cloneable, Serializable
{
    public static final CellConstraints.Alignment DEFAULT;
    public static final CellConstraints.Alignment FILL;
    public static final CellConstraints.Alignment LEFT;
    public static final CellConstraints.Alignment RIGHT;
    public static final CellConstraints.Alignment CENTER;
    public static final CellConstraints.Alignment TOP;
    public static final CellConstraints.Alignment BOTTOM;
    
    public static CellConstraints xy(final int col, final int row) {
        return xywh(col, row, 1, 1);
    }
    
    public static CellConstraints xy(final int col, final int row, final String encodedAlignments) {
        return xywh(col, row, 1, 1, encodedAlignments);
    }
    
    public static CellConstraints xy(final int col, final int row, final CellConstraints.Alignment colAlign, final CellConstraints.Alignment rowAlign) {
        return xywh(col, row, 1, 1, colAlign, rowAlign);
    }
    
    public static CellConstraints xyw(final int col, final int row, final int colSpan) {
        return xywh(col, row, colSpan, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public static CellConstraints xyw(final int col, final int row, final int colSpan, final String encodedAlignments) {
        return xywh(col, row, colSpan, 1, encodedAlignments);
    }
    
    public static CellConstraints xyw(final int col, final int row, final int colSpan, final CellConstraints.Alignment colAlign, final CellConstraints.Alignment rowAlign) {
        return xywh(col, row, colSpan, 1, colAlign, rowAlign);
    }
    
    public static CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan) {
        return xywh(col, row, colSpan, rowSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public static CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan, final String encodedAlignments) {
        return new CellConstraints().xywh(col, row, colSpan, rowSpan, encodedAlignments);
    }
    
    public static CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan, final CellConstraints.Alignment colAlign, final CellConstraints.Alignment rowAlign) {
        return new CellConstraints(col, row, colSpan, rowSpan, colAlign, rowAlign);
    }
    
    public static CellConstraints rc(final int row, final int col) {
        return rchw(row, col, 1, 1);
    }
    
    public static CellConstraints rc(final int row, final int col, final String encodedAlignments) {
        return rchw(row, col, 1, 1, encodedAlignments);
    }
    
    public static CellConstraints rc(final int row, final int col, final CellConstraints.Alignment rowAlign, final CellConstraints.Alignment colAlign) {
        return rchw(row, col, 1, 1, rowAlign, colAlign);
    }
    
    public static CellConstraints rcw(final int row, final int col, final int colSpan) {
        return rchw(row, col, 1, colSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public static CellConstraints rcw(final int row, final int col, final int colSpan, final String encodedAlignments) {
        return rchw(row, col, 1, colSpan, encodedAlignments);
    }
    
    public static CellConstraints rcw(final int row, final int col, final int colSpan, final CellConstraints.Alignment rowAlign, final CellConstraints.Alignment colAlign) {
        return rchw(row, col, 1, colSpan, rowAlign, colAlign);
    }
    
    public static CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan) {
        return rchw(row, col, rowSpan, colSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public static CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan, final String encodedAlignments) {
        return new CellConstraints().rchw(row, col, rowSpan, colSpan, encodedAlignments);
    }
    
    public static CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan, final CellConstraints.Alignment rowAlign, final CellConstraints.Alignment colAlign) {
        return xywh(col, row, colSpan, rowSpan, colAlign, rowAlign);
    }
    
    static {
        DEFAULT = CellConstraints.DEFAULT;
        FILL = CellConstraints.FILL;
        LEFT = CellConstraints.LEFT;
        RIGHT = CellConstraints.RIGHT;
        CENTER = CellConstraints.CENTER;
        TOP = CellConstraints.TOP;
        BOTTOM = CellConstraints.BOTTOM;
    }
}
