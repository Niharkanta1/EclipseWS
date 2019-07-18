// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.factories;

import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import javax.swing.border.Border;
import java.awt.Component;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.common.base.Preconditions;
import javax.swing.JComponent;

public final class Forms
{
    private Forms() {
    }
    
    public static JComponent single(final String columnSpec, final String rowSpec, final JComponent component) {
        Preconditions.checkNotBlank(columnSpec, "The %1$s must not be null, empty, or whitespace.", "column specification");
        Preconditions.checkNotBlank(rowSpec, "The %1$s must not be null, empty, or whitespace.", "row specification");
        Preconditions.checkNotNull(component, "The %1$s must not be null.", "component");
        final FormLayout layout = new FormLayout(columnSpec, rowSpec);
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.add(component, CC.xy(1, 1));
        return builder.build();
    }
    
    public static JComponent centered(final JComponent component) {
        return single("center:pref:grow", "c:p:g", component);
    }
    
    public static JComponent border(final Border border, final JComponent component) {
        final JComponent container = single("fill:pref", "f:p", component);
        container.setBorder(border);
        return container;
    }
    
    public static JComponent border(final String emptyBorderSpec, final JComponent component) {
        return border(Borders.createEmptyBorder(emptyBorderSpec), component);
    }
    
    public static JComponent horizontal(final String gapColSpec, final JComponent... components) {
        Preconditions.checkNotBlank(gapColSpec, "The %1$s must not be null, empty, or whitespace.", "gap column specification");
        Preconditions.checkNotNull(components, "The %1$s must not be null.", "component array");
        Preconditions.checkArgument(components.length > 1, "You must provide more than one component.");
        final FormLayout layout = new FormLayout(components.length - 1 + "*(pref, " + gapColSpec + "), pref", "p");
        final PanelBuilder builder = new PanelBuilder(layout);
        int column = 1;
        for (final JComponent component : components) {
            builder.add(component, CC.xy(column, 1));
            column += 2;
        }
        return builder.build();
    }
    
    public static JComponent vertical(final String gapRowSpec, final JComponent... components) {
        Preconditions.checkNotBlank(gapRowSpec, "The %1$s must not be null, empty, or whitespace.", "gap row specification");
        Preconditions.checkNotNull(components, "The %1$s must not be null.", "component array");
        Preconditions.checkArgument(components.length > 1, "You must provide more than one component.");
        final FormLayout layout = new FormLayout("pref", components.length - 1 + "*(p, " + gapRowSpec + "), p");
        final PanelBuilder builder = new PanelBuilder(layout);
        int row = 1;
        for (final JComponent component : components) {
            builder.add(component, CC.xy(1, row));
            row += 2;
        }
        return builder.build();
    }
    
    public static JComponent buttonBar(final JComponent... buttons) {
        return ButtonBarBuilder.create().addButton(buttons).build();
    }
    
    public static JComponent buttonStack(final JComponent... buttons) {
        return ButtonStackBuilder.create().addButton(buttons).build();
    }
    
    public static JComponent checkBoxBar(final JCheckBox... checkBoxes) {
        return buildGroupedButtonBar((AbstractButton[])checkBoxes);
    }
    
    public static JComponent checkBoxStack(final JCheckBox... checkBoxes) {
        return buildGroupedButtonStack((AbstractButton[])checkBoxes);
    }
    
    public static JComponent radioButtonBar(final JRadioButton... radioButtons) {
        return buildGroupedButtonBar((AbstractButton[])radioButtons);
    }
    
    public static JComponent radioButtonStack(final JRadioButton... radioButtons) {
        return buildGroupedButtonStack((AbstractButton[])radioButtons);
    }
    
    private static JComponent buildGroupedButtonBar(final AbstractButton... buttons) {
        Preconditions.checkArgument(buttons.length > 1, "You must provide more than one button.");
        final FormLayout layout = new FormLayout(String.format("pref, %s*($rgap, pref)", buttons.length - 1), "p");
        final PanelBuilder builder = new PanelBuilder(layout);
        int column = 1;
        for (final AbstractButton button : buttons) {
            builder.add(button, CC.xy(column, 1));
            column += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }
    
    private static JComponent buildGroupedButtonStack(final AbstractButton... buttons) {
        Preconditions.checkArgument(buttons.length > 1, "You must provide more than one button.");
        final FormLayout layout = new FormLayout("pref", String.format("p, %s*(0, p)", buttons.length - 1));
        final PanelBuilder builder = new PanelBuilder(layout);
        int row = 1;
        for (final AbstractButton button : buttons) {
            builder.add(button, CC.xy(1, row));
            row += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }
}
