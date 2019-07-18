// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.factories;

import java.awt.FontMetrics;
import com.jgoodies.forms.layout.Sizes;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Color;
import javax.accessibility.AccessibleContext;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import com.jgoodies.forms.util.FormUtils;
import com.jgoodies.common.base.Preconditions;
import javax.swing.JSeparator;
import com.jgoodies.common.base.Strings;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.Action;
import com.jgoodies.common.swing.MnemonicUtils;
import javax.swing.JLabel;

public class DefaultComponentFactory implements ComponentFactory
{
    private static final DefaultComponentFactory INSTANCE;
    
    public static DefaultComponentFactory getInstance() {
        return DefaultComponentFactory.INSTANCE;
    }
    
    @Override
    public JLabel createLabel(final String textWithMnemonic) {
        final JLabel label = new FormsLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }
    
    @Override
    public JLabel createReadOnlyLabel(final String textWithMnemonic) {
        final JLabel label = new ReadOnlyLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }
    
    @Override
    public JButton createButton(final Action action) {
        return new JButton(action);
    }
    
    @Override
    public JLabel createTitle(final String textWithMnemonic) {
        final JLabel label = new TitleLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        label.setVerticalAlignment(0);
        return label;
    }
    
    @Override
    public JLabel createHeaderLabel(final String markedText) {
        return this.createTitle(markedText);
    }
    
    public JComponent createSeparator(final String textWithMnemonic) {
        return this.createSeparator(textWithMnemonic, 2);
    }
    
    @Override
    public JComponent createSeparator(final String textWithMnemonic, final int alignment) {
        if (Strings.isBlank(textWithMnemonic)) {
            return new JSeparator();
        }
        final JLabel title = this.createTitle(textWithMnemonic);
        title.setHorizontalAlignment(alignment);
        return this.createSeparator(title);
    }
    
    public JComponent createSeparator(final JLabel label) {
        Preconditions.checkNotNull(label, "The label must not be null.");
        final int horizontalAlignment = label.getHorizontalAlignment();
        Preconditions.checkArgument(horizontalAlignment == 2 || horizontalAlignment == 0 || horizontalAlignment == 4, "The label's horizontal alignment must be one of: LEFT, CENTER, RIGHT.");
        final JPanel panel = new JPanel(new TitledSeparatorLayout(!FormUtils.isLafAqua()));
        panel.setOpaque(false);
        panel.add(label);
        panel.add(new JSeparator());
        if (horizontalAlignment == 0) {
            panel.add(new JSeparator());
        }
        return panel;
    }
    
    static {
        INSTANCE = new DefaultComponentFactory();
    }
    
    private static class FormsLabel extends JLabel
    {
        @Override
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new AccessibleFormsLabel();
            }
            return this.accessibleContext;
        }
        
        private final class AccessibleFormsLabel extends AccessibleJLabel
        {
            @Override
            public String getAccessibleName() {
                if (this.accessibleName != null) {
                    return this.accessibleName;
                }
                final String text = FormsLabel.this.getText();
                if (text == null) {
                    return super.getAccessibleName();
                }
                return text.endsWith(":") ? text.substring(0, text.length() - 1) : text;
            }
        }
    }
    
    private static final class ReadOnlyLabel extends FormsLabel
    {
        private static final String[] UIMANAGER_KEYS;
        
        @Override
        public void updateUI() {
            super.updateUI();
            this.setForeground(getDisabledForeground());
        }
        
        private static Color getDisabledForeground() {
            for (final String key : ReadOnlyLabel.UIMANAGER_KEYS) {
                final Color foreground = UIManager.getColor(key);
                if (foreground != null) {
                    return foreground;
                }
            }
            return null;
        }
        
        static {
            UIMANAGER_KEYS = new String[] { "Label.disabledForeground", "Label.disabledText", "Label[Disabled].textForeground", "textInactiveText" };
        }
    }
    
    private static final class TitleLabel extends FormsLabel
    {
        @Override
        public void updateUI() {
            super.updateUI();
            final Color foreground = getTitleColor();
            if (foreground != null) {
                this.setForeground(foreground);
            }
            this.setFont(getTitleFont());
        }
        
        private static Color getTitleColor() {
            return UIManager.getColor("TitledBorder.titleColor");
        }
        
        private static Font getTitleFont() {
            return FormUtils.isLafAqua() ? UIManager.getFont("Label.font").deriveFont(1) : UIManager.getFont("TitledBorder.font");
        }
    }
    
    private static final class TitledSeparatorLayout implements LayoutManager
    {
        private final boolean centerSeparators;
        
        private TitledSeparatorLayout(final boolean centerSeparators) {
            this.centerSeparators = centerSeparators;
        }
        
        @Override
        public void addLayoutComponent(final String name, final Component comp) {
        }
        
        @Override
        public void removeLayoutComponent(final Component comp) {
        }
        
        @Override
        public Dimension minimumLayoutSize(final Container parent) {
            return this.preferredLayoutSize(parent);
        }
        
        @Override
        public Dimension preferredLayoutSize(final Container parent) {
            final Component label = getLabel(parent);
            final Dimension labelSize = label.getPreferredSize();
            final Insets insets = parent.getInsets();
            final int width = labelSize.width + insets.left + insets.right;
            final int height = labelSize.height + insets.top + insets.bottom;
            return new Dimension(width, height);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            synchronized (parent.getTreeLock()) {
                final Dimension size = parent.getSize();
                final Insets insets = parent.getInsets();
                final int width = size.width - insets.left - insets.right;
                final JLabel label = getLabel(parent);
                final Dimension labelSize = label.getPreferredSize();
                final int labelWidth = labelSize.width;
                final int labelHeight = labelSize.height;
                final Component separator1 = parent.getComponent(1);
                final int separatorHeight = separator1.getPreferredSize().height;
                final FontMetrics metrics = label.getFontMetrics(label.getFont());
                final int ascent = metrics.getMaxAscent();
                final int hGapDlu = this.centerSeparators ? 3 : 1;
                final int hGap = Sizes.dialogUnitXAsPixel(hGapDlu, label);
                final int vOffset = this.centerSeparators ? (1 + (labelHeight - separatorHeight) / 2) : (ascent - separatorHeight / 2);
                final int alignment = label.getHorizontalAlignment();
                final int y = insets.top;
                if (alignment == 2) {
                    int x = insets.left;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    x += labelWidth;
                    x += hGap;
                    final int separatorWidth = size.width - insets.right - x;
                    separator1.setBounds(x, y + vOffset, separatorWidth, separatorHeight);
                }
                else if (alignment == 4) {
                    int x = insets.left + width - labelWidth;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    x -= hGap;
                    final int separatorWidth = --x - insets.left;
                    separator1.setBounds(insets.left, y + vOffset, separatorWidth, separatorHeight);
                }
                else {
                    final int xOffset = (width - labelWidth - 2 * hGap) / 2;
                    int x2 = insets.left;
                    separator1.setBounds(x2, y + vOffset, xOffset - 1, separatorHeight);
                    x2 += xOffset;
                    x2 += hGap;
                    label.setBounds(x2, y, labelWidth, labelHeight);
                    x2 += labelWidth;
                    x2 += hGap;
                    final Component separator2 = parent.getComponent(2);
                    final int separatorWidth2 = size.width - insets.right - x2;
                    separator2.setBounds(x2, y + vOffset, separatorWidth2, separatorHeight);
                }
            }
        }
        
        private static JLabel getLabel(final Container parent) {
            return (JLabel)parent.getComponent(0);
        }
    }
}
