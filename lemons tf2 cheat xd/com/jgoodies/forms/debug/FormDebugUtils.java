// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.debug;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Component;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;

public final class FormDebugUtils
{
    private FormDebugUtils() {
    }
    
    public static void dumpAll(final Container container) {
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        final FormLayout layout = (FormLayout)container.getLayout();
        dumpColumnSpecs(layout);
        dumpRowSpecs(layout);
        System.out.println();
        dumpColumnGroups(layout);
        dumpRowGroups(layout);
        System.out.println();
        dumpConstraints(container);
        dumpGridBounds(container);
    }
    
    public static void dumpColumnSpecs(final FormLayout layout) {
        System.out.print("COLUMN SPECS:");
        for (int col = 1; col <= layout.getColumnCount(); ++col) {
            final ColumnSpec colSpec = layout.getColumnSpec(col);
            System.out.print(colSpec.toShortString());
            if (col < layout.getColumnCount()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    
    public static void dumpRowSpecs(final FormLayout layout) {
        System.out.print("ROW SPECS:   ");
        for (int row = 1; row <= layout.getRowCount(); ++row) {
            final RowSpec rowSpec = layout.getRowSpec(row);
            System.out.print(rowSpec.toShortString());
            if (row < layout.getRowCount()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    
    public static void dumpColumnGroups(final FormLayout layout) {
        dumpGroups("COLUMN GROUPS: ", layout.getColumnGroups());
    }
    
    public static void dumpRowGroups(final FormLayout layout) {
        dumpGroups("ROW GROUPS:    ", layout.getRowGroups());
    }
    
    public static void dumpGridBounds(final Container container) {
        System.out.println("GRID BOUNDS");
        dumpGridBounds(getLayoutInfo(container));
    }
    
    public static void dumpGridBounds(final FormLayout.LayoutInfo layoutInfo) {
        System.out.print("COLUMN ORIGINS: ");
        for (final int columnOrigin : layoutInfo.columnOrigins) {
            System.out.print(columnOrigin + " ");
        }
        System.out.println();
        System.out.print("ROW ORIGINS:    ");
        for (final int rowOrigin : layoutInfo.rowOrigins) {
            System.out.print(rowOrigin + " ");
        }
        System.out.println();
    }
    
    public static void dumpConstraints(final Container container) {
        System.out.println("COMPONENT CONSTRAINTS");
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        final FormLayout layout = (FormLayout)container.getLayout();
        for (int childCount = container.getComponentCount(), i = 0; i < childCount; ++i) {
            final Component child = container.getComponent(i);
            final CellConstraints cc = layout.getConstraints(child);
            final String ccString = (cc == null) ? "no constraints" : cc.toShortString(layout);
            System.out.print(ccString);
            System.out.print("; ");
            final String childType = child.getClass().getName();
            System.out.print(childType);
            if (child instanceof JLabel) {
                final JLabel label = (JLabel)child;
                System.out.print("      \"" + label.getText() + "\"");
            }
            if (child.getName() != null) {
                System.out.print("; name=");
                System.out.print(child.getName());
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private static void dumpGroups(final String title, final int[][] allGroups) {
        System.out.print(title + " {");
        for (int group = 0; group < allGroups.length; ++group) {
            final int[] groupIndices = allGroups[group];
            System.out.print(" {");
            for (int i = 0; i < groupIndices.length; ++i) {
                System.out.print(groupIndices[i]);
                if (i < groupIndices.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("} ");
            if (group < allGroups.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    
    public static FormLayout.LayoutInfo getLayoutInfo(final Container container) {
        Preconditions.checkNotNull(container, "The container must not be null.");
        Preconditions.checkArgument(container.getLayout() instanceof FormLayout, "The container must use an instance of FormLayout.");
        final FormLayout layout = (FormLayout)container.getLayout();
        return layout.getLayoutInfo(container);
    }
}
