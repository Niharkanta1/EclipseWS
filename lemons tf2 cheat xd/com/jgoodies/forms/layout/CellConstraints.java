// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.Locale;
import java.awt.Rectangle;
import java.awt.Component;
import java.util.StringTokenizer;
import com.jgoodies.common.base.Preconditions;
import java.awt.Insets;
import java.io.Serializable;

public final class CellConstraints implements Cloneable, Serializable
{
    public static final Alignment DEFAULT;
    public static final Alignment FILL;
    public static final Alignment LEFT;
    public static final Alignment RIGHT;
    public static final Alignment CENTER;
    public static final Alignment TOP;
    public static final Alignment BOTTOM;
    private static final Alignment[] VALUES;
    private static final Insets EMPTY_INSETS;
    public int gridX;
    public int gridY;
    public int gridWidth;
    public int gridHeight;
    public Alignment hAlign;
    public Alignment vAlign;
    public Insets insets;
    public Boolean honorsVisibility;
    
    public CellConstraints() {
        this(1, 1);
    }
    
    public CellConstraints(final int gridX, final int gridY) {
        this(gridX, gridY, 1, 1);
    }
    
    public CellConstraints(final int gridX, final int gridY, final Alignment hAlign, final Alignment vAlign) {
        this(gridX, gridY, 1, 1, hAlign, vAlign, CellConstraints.EMPTY_INSETS);
    }
    
    public CellConstraints(final int gridX, final int gridY, final int gridWidth, final int gridHeight) {
        this(gridX, gridY, gridWidth, gridHeight, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public CellConstraints(final int gridX, final int gridY, final int gridWidth, final int gridHeight, final Alignment hAlign, final Alignment vAlign) {
        this(gridX, gridY, gridWidth, gridHeight, hAlign, vAlign, CellConstraints.EMPTY_INSETS);
    }
    
    public CellConstraints(final int gridX, final int gridY, final int gridWidth, final int gridHeight, final Alignment hAlign, final Alignment vAlign, final Insets insets) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        this.insets = insets;
        if (gridX <= 0) {
            throw new IndexOutOfBoundsException("The grid x must be a positive number.");
        }
        if (gridY <= 0) {
            throw new IndexOutOfBoundsException("The grid y must be a positive number.");
        }
        if (gridWidth <= 0) {
            throw new IndexOutOfBoundsException("The grid width must be a positive number.");
        }
        if (gridHeight <= 0) {
            throw new IndexOutOfBoundsException("The grid height must be a positive number.");
        }
        Preconditions.checkNotNull(hAlign, "The horizontal alignment must not be null.");
        Preconditions.checkNotNull(vAlign, "The vertical alignment must not be null.");
        ensureValidOrientations(hAlign, vAlign);
    }
    
    public CellConstraints(final String encodedConstraints) {
        this();
        this.initFromConstraints(encodedConstraints);
    }
    
    public CellConstraints translate(final int dx, final int dy) {
        return new CellConstraints(this.gridX + dx, this.gridY + dy, this.gridWidth, this.gridHeight, this.hAlign, this.vAlign, this.insets);
    }
    
    public CellConstraints xy(final int col, final int row) {
        return this.xywh(col, row, 1, 1);
    }
    
    public CellConstraints xy(final int col, final int row, final String encodedAlignments) {
        return this.xywh(col, row, 1, 1, encodedAlignments);
    }
    
    public CellConstraints xy(final int col, final int row, final Alignment colAlign, final Alignment rowAlign) {
        return this.xywh(col, row, 1, 1, colAlign, rowAlign);
    }
    
    public CellConstraints xyw(final int col, final int row, final int colSpan) {
        return this.xywh(col, row, colSpan, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public CellConstraints xyw(final int col, final int row, final int colSpan, final String encodedAlignments) {
        return this.xywh(col, row, colSpan, 1, encodedAlignments);
    }
    
    public CellConstraints xyw(final int col, final int row, final int colSpan, final Alignment colAlign, final Alignment rowAlign) {
        return this.xywh(col, row, colSpan, 1, colAlign, rowAlign);
    }
    
    public CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan) {
        return this.xywh(col, row, colSpan, rowSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan, final String encodedAlignments) {
        final CellConstraints result = this.xywh(col, row, colSpan, rowSpan);
        result.setAlignments(encodedAlignments, true);
        return result;
    }
    
    public CellConstraints xywh(final int col, final int row, final int colSpan, final int rowSpan, final Alignment colAlign, final Alignment rowAlign) {
        this.gridX = col;
        this.gridY = row;
        this.gridWidth = colSpan;
        this.gridHeight = rowSpan;
        this.hAlign = colAlign;
        this.vAlign = rowAlign;
        ensureValidOrientations(this.hAlign, this.vAlign);
        return this;
    }
    
    public CellConstraints rc(final int row, final int col) {
        return this.rchw(row, col, 1, 1);
    }
    
    public CellConstraints rc(final int row, final int col, final String encodedAlignments) {
        return this.rchw(row, col, 1, 1, encodedAlignments);
    }
    
    public CellConstraints rc(final int row, final int col, final Alignment rowAlign, final Alignment colAlign) {
        return this.rchw(row, col, 1, 1, rowAlign, colAlign);
    }
    
    public CellConstraints rcw(final int row, final int col, final int colSpan) {
        return this.rchw(row, col, 1, colSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public CellConstraints rcw(final int row, final int col, final int colSpan, final String encodedAlignments) {
        return this.rchw(row, col, 1, colSpan, encodedAlignments);
    }
    
    public CellConstraints rcw(final int row, final int col, final int colSpan, final Alignment rowAlign, final Alignment colAlign) {
        return this.rchw(row, col, 1, colSpan, rowAlign, colAlign);
    }
    
    public CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan) {
        return this.rchw(row, col, rowSpan, colSpan, CellConstraints.DEFAULT, CellConstraints.DEFAULT);
    }
    
    public CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan, final String encodedAlignments) {
        final CellConstraints result = this.rchw(row, col, rowSpan, colSpan);
        result.setAlignments(encodedAlignments, false);
        return result;
    }
    
    public CellConstraints rchw(final int row, final int col, final int rowSpan, final int colSpan, final Alignment rowAlign, final Alignment colAlign) {
        return this.xywh(col, row, colSpan, rowSpan, colAlign, rowAlign);
    }
    
    private void initFromConstraints(final String encodedConstraints) {
        final StringTokenizer tokenizer = new StringTokenizer(encodedConstraints, " ,");
        final int argCount = tokenizer.countTokens();
        Preconditions.checkArgument(argCount == 2 || argCount == 4 || argCount == 6, "You must provide 2, 4 or 6 arguments.");
        Integer nextInt = decodeInt(tokenizer.nextToken());
        Preconditions.checkArgument(nextInt != null, "First cell constraint element must be a number.");
        this.gridX = nextInt;
        Preconditions.checkArgument(this.gridX > 0, "The grid x must be a positive number.");
        nextInt = decodeInt(tokenizer.nextToken());
        Preconditions.checkArgument(nextInt != null, "Second cell constraint element must be a number.");
        this.gridY = nextInt;
        Preconditions.checkArgument(this.gridY > 0, "The grid y must be a positive number.");
        if (!tokenizer.hasMoreTokens()) {
            return;
        }
        String token = tokenizer.nextToken();
        nextInt = decodeInt(token);
        if (nextInt != null) {
            this.gridWidth = nextInt;
            if (this.gridWidth <= 0) {
                throw new IndexOutOfBoundsException("The grid width must be a positive number.");
            }
            nextInt = decodeInt(tokenizer.nextToken());
            if (nextInt == null) {
                throw new IllegalArgumentException("Fourth cell constraint element must be like third.");
            }
            this.gridHeight = nextInt;
            if (this.gridHeight <= 0) {
                throw new IndexOutOfBoundsException("The grid height must be a positive number.");
            }
            if (!tokenizer.hasMoreTokens()) {
                return;
            }
            token = tokenizer.nextToken();
        }
        this.hAlign = decodeAlignment(token);
        this.vAlign = decodeAlignment(tokenizer.nextToken());
        ensureValidOrientations(this.hAlign, this.vAlign);
    }
    
    private void setAlignments(final String encodedAlignments, final boolean horizontalThenVertical) {
        final StringTokenizer tokenizer = new StringTokenizer(encodedAlignments, " ,");
        final Alignment first = decodeAlignment(tokenizer.nextToken());
        final Alignment second = decodeAlignment(tokenizer.nextToken());
        this.hAlign = (horizontalThenVertical ? first : second);
        this.vAlign = (horizontalThenVertical ? second : first);
        ensureValidOrientations(this.hAlign, this.vAlign);
    }
    
    private static Integer decodeInt(final String token) {
        try {
            return Integer.decode(token);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    private static Alignment decodeAlignment(final String encodedAlignment) {
        return Alignment.valueOf(encodedAlignment);
    }
    
    void ensureValidGridBounds(final int colCount, final int rowCount) {
        if (this.gridX <= 0) {
            throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be positive.");
        }
        if (this.gridX > colCount) {
            throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be less than or equal to " + colCount + ".");
        }
        if (this.gridX + this.gridWidth - 1 > colCount) {
            throw new IndexOutOfBoundsException("The grid width " + this.gridWidth + " must be less than or equal to " + (colCount - this.gridX + 1) + ".");
        }
        if (this.gridY <= 0) {
            throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be positive.");
        }
        if (this.gridY > rowCount) {
            throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be less than or equal to " + rowCount + ".");
        }
        if (this.gridY + this.gridHeight - 1 > rowCount) {
            throw new IndexOutOfBoundsException("The grid height " + this.gridHeight + " must be less than or equal to " + (rowCount - this.gridY + 1) + ".");
        }
    }
    
    private static void ensureValidOrientations(final Alignment horizontalAlignment, final Alignment verticalAlignment) {
        if (!horizontalAlignment.isHorizontal()) {
            throw new IllegalArgumentException("The horizontal alignment must be one of: left, center, right, fill, default.");
        }
        if (!verticalAlignment.isVertical()) {
            throw new IllegalArgumentException("The vertical alignment must be one of: top, center, bottom, fill, default.");
        }
    }
    
    void setBounds(final Component c, final FormLayout layout, final Rectangle cellBounds, final FormLayout.Measure minWidthMeasure, final FormLayout.Measure minHeightMeasure, final FormLayout.Measure prefWidthMeasure, final FormLayout.Measure prefHeightMeasure) {
        final ColumnSpec colSpec = (this.gridWidth == 1) ? layout.getColumnSpec(this.gridX) : null;
        final RowSpec rowSpec = (this.gridHeight == 1) ? layout.getRowSpec(this.gridY) : null;
        final Alignment concreteHAlign = concreteAlignment(this.hAlign, colSpec);
        final Alignment concreteVAlign = concreteAlignment(this.vAlign, rowSpec);
        final Insets concreteInsets = (this.insets != null) ? this.insets : CellConstraints.EMPTY_INSETS;
        final int cellX = cellBounds.x + concreteInsets.left;
        final int cellY = cellBounds.y + concreteInsets.top;
        final int cellW = cellBounds.width - concreteInsets.left - concreteInsets.right;
        final int cellH = cellBounds.height - concreteInsets.top - concreteInsets.bottom;
        final int compW = componentSize(c, colSpec, cellW, minWidthMeasure, prefWidthMeasure);
        final int compH = componentSize(c, rowSpec, cellH, minHeightMeasure, prefHeightMeasure);
        final int x = origin(concreteHAlign, cellX, cellW, compW);
        final int y = origin(concreteVAlign, cellY, cellH, compH);
        final int w = extent(concreteHAlign, cellW, compW);
        final int h = extent(concreteVAlign, cellH, compH);
        c.setBounds(x, y, w, h);
    }
    
    private static Alignment concreteAlignment(final Alignment cellAlignment, final FormSpec formSpec) {
        return (formSpec == null) ? ((cellAlignment == CellConstraints.DEFAULT) ? CellConstraints.FILL : cellAlignment) : usedAlignment(cellAlignment, formSpec);
    }
    
    private static Alignment usedAlignment(final Alignment cellAlignment, final FormSpec formSpec) {
        if (cellAlignment != CellConstraints.DEFAULT) {
            return cellAlignment;
        }
        final FormSpec.DefaultAlignment defaultAlignment = formSpec.getDefaultAlignment();
        if (defaultAlignment == FormSpec.FILL_ALIGN) {
            return CellConstraints.FILL;
        }
        if (defaultAlignment == ColumnSpec.LEFT) {
            return CellConstraints.LEFT;
        }
        if (defaultAlignment == FormSpec.CENTER_ALIGN) {
            return CellConstraints.CENTER;
        }
        if (defaultAlignment == ColumnSpec.RIGHT) {
            return CellConstraints.RIGHT;
        }
        if (defaultAlignment == RowSpec.TOP) {
            return CellConstraints.TOP;
        }
        return CellConstraints.BOTTOM;
    }
    
    private static int componentSize(final Component component, final FormSpec formSpec, final int cellSize, final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure) {
        if (formSpec == null) {
            return prefMeasure.sizeOf(component);
        }
        if (formSpec.getSize() == Sizes.MINIMUM) {
            return minMeasure.sizeOf(component);
        }
        if (formSpec.getSize() == Sizes.PREFERRED) {
            return prefMeasure.sizeOf(component);
        }
        return Math.min(cellSize, prefMeasure.sizeOf(component));
    }
    
    private static int origin(final Alignment alignment, final int cellOrigin, final int cellSize, final int componentSize) {
        if (alignment == CellConstraints.RIGHT || alignment == CellConstraints.BOTTOM) {
            return cellOrigin + cellSize - componentSize;
        }
        if (alignment == CellConstraints.CENTER) {
            return cellOrigin + (cellSize - componentSize) / 2;
        }
        return cellOrigin;
    }
    
    private static int extent(final Alignment alignment, final int cellSize, final int componentSize) {
        return (alignment == CellConstraints.FILL) ? cellSize : componentSize;
    }
    
    public Object clone() {
        try {
            final CellConstraints c = (CellConstraints)super.clone();
            c.insets = (Insets)this.insets.clone();
            return c;
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("CellConstraints");
        buffer.append("[x=");
        buffer.append(this.gridX);
        buffer.append("; y=");
        buffer.append(this.gridY);
        buffer.append("; w=");
        buffer.append(this.gridWidth);
        buffer.append("; h=");
        buffer.append(this.gridHeight);
        buffer.append("; hAlign=");
        buffer.append(this.hAlign);
        buffer.append("; vAlign=");
        buffer.append(this.vAlign);
        if (!CellConstraints.EMPTY_INSETS.equals(this.insets)) {
            buffer.append("; insets=");
            buffer.append(this.insets);
        }
        buffer.append("; honorsVisibility=");
        buffer.append(this.honorsVisibility);
        buffer.append(']');
        return buffer.toString();
    }
    
    public String toShortString() {
        return this.toShortString(null);
    }
    
    public String toShortString(final FormLayout layout) {
        final StringBuffer buffer = new StringBuffer("(");
        buffer.append(formatInt(this.gridX));
        buffer.append(", ");
        buffer.append(formatInt(this.gridY));
        buffer.append(", ");
        buffer.append(formatInt(this.gridWidth));
        buffer.append(", ");
        buffer.append(formatInt(this.gridHeight));
        buffer.append(", \"");
        buffer.append(this.hAlign.abbreviation());
        if (this.hAlign == CellConstraints.DEFAULT && layout != null) {
            buffer.append('=');
            final ColumnSpec colSpec = (this.gridWidth == 1) ? layout.getColumnSpec(this.gridX) : null;
            buffer.append(concreteAlignment(this.hAlign, colSpec).abbreviation());
        }
        buffer.append(", ");
        buffer.append(this.vAlign.abbreviation());
        if (this.vAlign == CellConstraints.DEFAULT && layout != null) {
            buffer.append('=');
            final RowSpec rowSpec = (this.gridHeight == 1) ? layout.getRowSpec(this.gridY) : null;
            buffer.append(concreteAlignment(this.vAlign, rowSpec).abbreviation());
        }
        buffer.append("\"");
        if (!CellConstraints.EMPTY_INSETS.equals(this.insets)) {
            buffer.append(", ");
            buffer.append(this.insets);
        }
        if (this.honorsVisibility != null) {
            buffer.append(this.honorsVisibility ? "honors visibility" : "ignores visibility");
        }
        buffer.append(')');
        return buffer.toString();
    }
    
    private static String formatInt(final int number) {
        final String str = Integer.toString(number);
        return (number < 10) ? (" " + str) : str;
    }
    
    static {
        DEFAULT = new Alignment("default", 2);
        FILL = new Alignment("fill", 2);
        LEFT = new Alignment("left", 0);
        RIGHT = new Alignment("right", 0);
        CENTER = new Alignment("center", 2);
        TOP = new Alignment("top", 1);
        BOTTOM = new Alignment("bottom", 1);
        VALUES = new Alignment[] { CellConstraints.DEFAULT, CellConstraints.FILL, CellConstraints.LEFT, CellConstraints.RIGHT, CellConstraints.CENTER, CellConstraints.TOP, CellConstraints.BOTTOM };
        EMPTY_INSETS = new Insets(0, 0, 0, 0);
    }
    
    public static final class Alignment implements Serializable
    {
        private static final int HORIZONTAL = 0;
        private static final int VERTICAL = 1;
        private static final int BOTH = 2;
        private final transient String name;
        private final transient int orientation;
        private static int nextOrdinal;
        private final int ordinal;
        
        private Alignment(final String name, final int orientation) {
            this.ordinal = Alignment.nextOrdinal++;
            this.name = name;
            this.orientation = orientation;
        }
        
        static Alignment valueOf(final String nameOrAbbreviation) {
            final String str = nameOrAbbreviation.toLowerCase(Locale.ENGLISH);
            if (str.equals("d") || str.equals("default")) {
                return CellConstraints.DEFAULT;
            }
            if (str.equals("f") || str.equals("fill")) {
                return CellConstraints.FILL;
            }
            if (str.equals("c") || str.equals("center")) {
                return CellConstraints.CENTER;
            }
            if (str.equals("l") || str.equals("left")) {
                return CellConstraints.LEFT;
            }
            if (str.equals("r") || str.equals("right")) {
                return CellConstraints.RIGHT;
            }
            if (str.equals("t") || str.equals("top")) {
                return CellConstraints.TOP;
            }
            if (str.equals("b") || str.equals("bottom")) {
                return CellConstraints.BOTTOM;
            }
            throw new IllegalArgumentException("Invalid alignment " + nameOrAbbreviation + ". Must be one of: left, center, right, top, bottom, " + "fill, default, l, c, r, t, b, f, d.");
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public char abbreviation() {
            return this.name.charAt(0);
        }
        
        private boolean isHorizontal() {
            return this.orientation != 1;
        }
        
        private boolean isVertical() {
            return this.orientation != 0;
        }
        
        private Object readResolve() {
            return CellConstraints.VALUES[this.ordinal];
        }
        
        static {
            Alignment.nextOrdinal = 0;
        }
    }
}
