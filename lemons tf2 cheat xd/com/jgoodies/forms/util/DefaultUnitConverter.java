// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.awt.Component;
import com.jgoodies.common.base.Preconditions;
import java.awt.FontMetrics;
import java.awt.Font;
import java.util.logging.Logger;

public final class DefaultUnitConverter extends AbstractUnitConverter
{
    public static final String PROPERTY_AVERAGE_CHARACTER_WIDTH_TEST_STRING = "averageCharacterWidthTestString";
    public static final String PROPERTY_DEFAULT_DIALOG_FONT = "defaultDialogFont";
    public static final String OLD_AVERAGE_CHARACTER_TEST_STRING = "X";
    public static final String MODERN_AVERAGE_CHARACTER_TEST_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String BALANCED_AVERAGE_CHARACTER_TEST_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Logger LOGGER;
    private static DefaultUnitConverter instance;
    private String averageCharWidthTestString;
    private Font defaultDialogFont;
    private DialogBaseUnits cachedGlobalDialogBaseUnits;
    private DialogBaseUnits cachedDialogBaseUnits;
    private FontMetrics cachedFontMetrics;
    private Font cachedDefaultDialogFont;
    
    private DefaultUnitConverter() {
        this.averageCharWidthTestString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        this.cachedGlobalDialogBaseUnits = null;
        this.cachedDialogBaseUnits = null;
        this.cachedFontMetrics = null;
        this.cachedDefaultDialogFont = null;
    }
    
    public static DefaultUnitConverter getInstance() {
        if (DefaultUnitConverter.instance == null) {
            DefaultUnitConverter.instance = new DefaultUnitConverter();
        }
        return DefaultUnitConverter.instance;
    }
    
    public String getAverageCharacterWidthTestString() {
        return this.averageCharWidthTestString;
    }
    
    public void setAverageCharacterWidthTestString(final String newTestString) {
        Preconditions.checkNotBlank(newTestString, "The %1$s must not be null, empty, or whitespace.", "test string");
        final String oldTestString = this.averageCharWidthTestString;
        this.firePropertyChange("averageCharacterWidthTestString", oldTestString, this.averageCharWidthTestString = newTestString);
    }
    
    public Font getDefaultDialogFont() {
        return (this.defaultDialogFont != null) ? this.defaultDialogFont : this.getCachedDefaultDialogFont();
    }
    
    public void setDefaultDialogFont(final Font newFont) {
        final Font oldFont = this.defaultDialogFont;
        this.defaultDialogFont = newFont;
        this.clearCache();
        this.firePropertyChange("defaultDialogFont", oldFont, newFont);
    }
    
    @Override
    protected double getDialogBaseUnitsX(final Component component) {
        return this.getDialogBaseUnits(component).x;
    }
    
    @Override
    protected double getDialogBaseUnitsY(final Component component) {
        return this.getDialogBaseUnits(component).y;
    }
    
    private DialogBaseUnits getGlobalDialogBaseUnits() {
        if (this.cachedGlobalDialogBaseUnits == null) {
            this.cachedGlobalDialogBaseUnits = this.computeGlobalDialogBaseUnits();
        }
        return this.cachedGlobalDialogBaseUnits;
    }
    
    private DialogBaseUnits getDialogBaseUnits(final Component c) {
        FormUtils.ensureValidCache();
        if (c == null) {
            return this.getGlobalDialogBaseUnits();
        }
        final FontMetrics fm = c.getFontMetrics(this.getDefaultDialogFont());
        if (fm.equals(this.cachedFontMetrics)) {
            return this.cachedDialogBaseUnits;
        }
        final DialogBaseUnits dialogBaseUnits = this.computeDialogBaseUnits(fm);
        this.cachedFontMetrics = fm;
        return this.cachedDialogBaseUnits = dialogBaseUnits;
    }
    
    private DialogBaseUnits computeDialogBaseUnits(final FontMetrics metrics) {
        final double averageCharWidth = this.computeAverageCharWidth(metrics, this.averageCharWidthTestString);
        final int ascent = metrics.getAscent();
        final double height = (ascent > 14) ? ascent : ((double)(ascent + (15 - ascent) / 3));
        final DialogBaseUnits dialogBaseUnits = new DialogBaseUnits(averageCharWidth, height);
        if (DefaultUnitConverter.LOGGER.isLoggable(Level.CONFIG)) {
            DefaultUnitConverter.LOGGER.config("Computed dialog base units " + dialogBaseUnits + " for: " + metrics.getFont());
        }
        return dialogBaseUnits;
    }
    
    private DialogBaseUnits computeGlobalDialogBaseUnits() {
        DefaultUnitConverter.LOGGER.config("Computing global dialog base units...");
        final Font dialogFont = this.getDefaultDialogFont();
        final FontMetrics metrics = createDefaultGlobalComponent().getFontMetrics(dialogFont);
        final DialogBaseUnits globalDialogBaseUnits = this.computeDialogBaseUnits(metrics);
        return globalDialogBaseUnits;
    }
    
    private Font getCachedDefaultDialogFont() {
        FormUtils.ensureValidCache();
        if (this.cachedDefaultDialogFont == null) {
            this.cachedDefaultDialogFont = lookupDefaultDialogFont();
        }
        return this.cachedDefaultDialogFont;
    }
    
    private static Font lookupDefaultDialogFont() {
        final Font buttonFont = UIManager.getFont("Button.font");
        return (buttonFont != null) ? buttonFont : new JButton().getFont();
    }
    
    private static Component createDefaultGlobalComponent() {
        return new JPanel(null);
    }
    
    void clearCache() {
        this.cachedGlobalDialogBaseUnits = null;
        this.cachedFontMetrics = null;
        this.cachedDefaultDialogFont = null;
    }
    
    static {
        LOGGER = Logger.getLogger(DefaultUnitConverter.class.getName());
    }
    
    private static final class DialogBaseUnits
    {
        final double x;
        final double y;
        
        DialogBaseUnits(final double dialogBaseUnitsX, final double dialogBaseUnitsY) {
            this.x = dialogBaseUnitsX;
            this.y = dialogBaseUnitsY;
        }
        
        @Override
        public String toString() {
            return "DBU(x=" + this.x + "; y=" + this.y + ")";
        }
    }
}
