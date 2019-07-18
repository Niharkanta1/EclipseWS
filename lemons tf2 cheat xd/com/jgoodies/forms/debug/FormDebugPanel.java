// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.debug;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class FormDebugPanel extends JPanel
{
    public static boolean paintRowsDefault;
    private static final Color DEFAULT_GRID_COLOR;
    private boolean paintInBackground;
    private boolean paintDiagonals;
    private boolean paintRows;
    private Color gridColor;
    
    public FormDebugPanel() {
        this(null);
    }
    
    public FormDebugPanel(final FormLayout layout) {
        this(layout, false, false);
    }
    
    public FormDebugPanel(final boolean paintInBackground, final boolean paintDiagonals) {
        this(null, paintInBackground, paintDiagonals);
    }
    
    public FormDebugPanel(final FormLayout layout, final boolean paintInBackground, final boolean paintDiagonals) {
        super(layout);
        this.paintRows = FormDebugPanel.paintRowsDefault;
        this.gridColor = FormDebugPanel.DEFAULT_GRID_COLOR;
        this.setPaintInBackground(paintInBackground);
        this.setPaintDiagonals(paintDiagonals);
        this.setGridColor(FormDebugPanel.DEFAULT_GRID_COLOR);
    }
    
    public void setPaintInBackground(final boolean b) {
        this.paintInBackground = b;
    }
    
    public void setPaintDiagonals(final boolean b) {
        this.paintDiagonals = b;
    }
    
    public void setPaintRows(final boolean b) {
        this.paintRows = b;
    }
    
    public void setGridColor(final Color color) {
        this.gridColor = color;
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.paintInBackground) {
            this.paintGrid(g);
        }
    }
    
    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        if (!this.paintInBackground) {
            this.paintGrid(g);
        }
    }
    
    private void paintGrid(final Graphics g) {
        if (!(this.getLayout() instanceof FormLayout)) {
            return;
        }
        final FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
        final int left = layoutInfo.getX();
        final int top = layoutInfo.getY();
        final int width = layoutInfo.getWidth();
        final int height = layoutInfo.getHeight();
        g.setColor(this.gridColor);
        for (int last = layoutInfo.columnOrigins.length - 1, col = 0; col <= last; ++col) {
            final boolean firstOrLast = col == 0 || col == last;
            final int x = layoutInfo.columnOrigins[col];
            final int start = firstOrLast ? 0 : top;
            for (int stop = firstOrLast ? this.getHeight() : (top + height), i = start; i < stop; i += 5) {
                final int length = Math.min(3, stop - i);
                g.fillRect(x, i, 1, length);
            }
        }
        for (int last = layoutInfo.rowOrigins.length - 1, row = 0; row <= last; ++row) {
            final boolean firstOrLast = row == 0 || row == last;
            final int y = layoutInfo.rowOrigins[row];
            final int start = firstOrLast ? 0 : left;
            final int stop = firstOrLast ? this.getWidth() : (left + width);
            if (firstOrLast || this.paintRows) {
                for (int i = start; i < stop; i += 5) {
                    final int length = Math.min(3, stop - i);
                    g.fillRect(i, y, length, 1);
                }
            }
        }
        if (this.paintDiagonals) {
            g.drawLine(left, top, left + width, top + height);
            g.drawLine(left, top + height, left + width, top);
        }
    }
    
    static {
        FormDebugPanel.paintRowsDefault = true;
        DEFAULT_GRID_COLOR = Color.red;
    }
}
