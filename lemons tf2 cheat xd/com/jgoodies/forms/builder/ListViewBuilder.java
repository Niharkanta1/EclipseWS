// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.builder;

import com.jgoodies.common.base.Strings;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.factories.Forms;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.JTable;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.FormsSetup;
import java.awt.FocusTraversalPolicy;
import javax.swing.border.Border;
import javax.swing.JComponent;
import com.jgoodies.forms.factories.ComponentFactory;

public final class ListViewBuilder
{
    private final ComponentFactory factory;
    private JComponent labelView;
    private JComponent filterView;
    private JComponent listView;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent detailsView;
    private Border border;
    private boolean honorsVisibility;
    private FocusTraversalPolicy focusTraversalPolicy;
    private String namePrefix;
    private String filterViewColSpec;
    private String listViewRowSpec;
    private JComponent panel;
    
    public ListViewBuilder() {
        this(FormsSetup.getComponentFactoryDefault());
    }
    
    public ListViewBuilder(final ComponentFactory factory) {
        this.honorsVisibility = true;
        this.namePrefix = "ListView";
        this.filterViewColSpec = "[100dlu, p]";
        this.listViewRowSpec = "fill:100dlu:grow";
        this.factory = factory;
    }
    
    public ListViewBuilder border(final Border border) {
        this.border = border;
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder focusTraversal(final FocusTraversalPolicy policy) {
        this.focusTraversalPolicy = policy;
        return this;
    }
    
    public ListViewBuilder honorVisibility(final boolean b) {
        this.honorsVisibility = b;
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder namePrefix(final String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }
    
    public ListViewBuilder labelView(final JComponent labelView) {
        this.setName(this.labelView = labelView, "label");
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder label(final String markedText) {
        this.labelView(this.factory.createLabel(markedText));
        return this;
    }
    
    public ListViewBuilder headerLabel(final String markedText) {
        this.labelView(this.factory.createHeaderLabel(markedText));
        return this;
    }
    
    public ListViewBuilder filterView(final JComponent filterView) {
        this.setName(this.filterView = filterView, "filter");
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder filterViewColSpec(final String colSpec) {
        Preconditions.checkNotNull(colSpec, "The filter view column specification must not be null.");
        this.filterViewColSpec = colSpec;
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder listView(final JComponent listView) {
        Preconditions.checkNotNull(listView, "The list view must not be null.");
        if (listView instanceof JTable || listView instanceof JList || listView instanceof JTree) {
            this.listView = new JScrollPane(listView);
        }
        else {
            this.listView = listView;
        }
        this.setName(listView, "listView");
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder listViewRowSpec(final String rowSpec) {
        Preconditions.checkNotNull(rowSpec, "The list view row specification must not be null.");
        this.listViewRowSpec = rowSpec;
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder listBarView(final JComponent listBarView) {
        this.setName(this.listBarView = listBarView, "listBarView");
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder listBar(final JComponent... buttons) {
        this.listBarView(Forms.buttonBar(buttons));
        return this;
    }
    
    public ListViewBuilder listExtrasView(final JComponent listExtrasView) {
        this.setName(this.listExtrasView = listExtrasView, "listExtrasView");
        this.invalidatePanel();
        return this;
    }
    
    public ListViewBuilder detailsView(final JComponent detailsView) {
        this.setName(this.detailsView = detailsView, "detailsView");
        this.invalidatePanel();
        return this;
    }
    
    public JComponent build() {
        if (this.panel == null) {
            this.panel = this.buildPanel();
        }
        return this.panel;
    }
    
    private void invalidatePanel() {
        this.panel = null;
    }
    
    private JComponent buildPanel() {
        Preconditions.checkNotNull(this.listView, "The list view must be set before #build is invoked.");
        final FormLayout layout = new FormLayout("fill:default:grow", "p, " + this.listViewRowSpec + ", p, p");
        layout.setHonorsVisibility(this.honorsVisibility);
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.border(this.border);
        if (this.focusTraversalPolicy != null) {
            builder.focusTraversal(this.focusTraversalPolicy);
        }
        if (this.labelView != null || this.filterView != null) {
            builder.add(this.buildDecoratedHeaderView(), CC.xy(1, 1));
        }
        builder.add(this.listView, CC.xy(1, 2));
        if (this.listBarView != null || this.listExtrasView != null) {
            builder.add(this.buildDecoratedListBarAndExtras(), CC.xy(1, 3));
        }
        if (this.detailsView != null) {
            builder.add(this.buildDecoratedDetailsView(), CC.xy(1, 4));
        }
        if (this.labelView instanceof JLabel) {
            final JLabel theLabelView = (JLabel)this.labelView;
            if (theLabelView.getLabelFor() == null) {
                theLabelView.setLabelFor(this.listView);
            }
        }
        return builder.build();
    }
    
    private JComponent buildDecoratedHeaderView() {
        final String columnSpec = (this.filterView != null) ? ("default:grow, 9dlu, " + this.filterViewColSpec) : "default:grow, 0, 0";
        final FormLayout layout = new FormLayout(columnSpec, "[14dlu,p], $lcg");
        final PanelBuilder builder = new PanelBuilder(layout).labelForFeatureEnabled(false);
        if (this.labelView != null) {
            builder.add(this.labelView, CC.xy(1, 1));
        }
        if (this.filterView != null) {
            builder.add(this.filterView, CC.xy(3, 1));
        }
        return builder.build();
    }
    
    private JComponent buildDecoratedListBarAndExtras() {
        final FormLayout layout = new FormLayout("left:default, 9dlu:grow, right:pref", "$rgap, p");
        layout.setHonorsVisibility(this.honorsVisibility);
        final PanelBuilder builder = new PanelBuilder(layout);
        if (this.listBarView != null) {
            builder.add(this.listBarView, CC.xy(1, 2));
        }
        if (this.listExtrasView != null) {
            builder.add(this.listExtrasView, CC.xy(3, 2));
        }
        return builder.build();
    }
    
    private JComponent buildDecoratedDetailsView() {
        final FormLayout layout = new FormLayout("fill:default:grow", "14, p");
        layout.setHonorsVisibility(this.honorsVisibility);
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.add(this.detailsView, CC.xy(1, 2));
        return builder.build();
    }
    
    private void setName(final JComponent component, final String suffix) {
        if (Strings.isNotBlank(component.getName())) {
            return;
        }
        component.setName(this.namePrefix + '.' + suffix);
    }
}
