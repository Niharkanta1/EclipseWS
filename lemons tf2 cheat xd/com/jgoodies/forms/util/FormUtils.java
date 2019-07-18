// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.util;

import javax.swing.UIManager;
import com.jgoodies.common.base.SystemUtils;
import javax.swing.LookAndFeel;

public final class FormUtils
{
    private static LookAndFeel cachedLookAndFeel;
    private static Boolean cachedIsLafAqua;
    
    private FormUtils() {
    }
    
    public static boolean isLafAqua() {
        ensureValidCache();
        if (FormUtils.cachedIsLafAqua == null) {
            FormUtils.cachedIsLafAqua = SystemUtils.isLafAqua();
        }
        return FormUtils.cachedIsLafAqua;
    }
    
    public static void clearLookAndFeelBasedCaches() {
        FormUtils.cachedIsLafAqua = null;
        DefaultUnitConverter.getInstance().clearCache();
    }
    
    static void ensureValidCache() {
        final LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel();
        if (currentLookAndFeel != FormUtils.cachedLookAndFeel) {
            clearLookAndFeelBasedCaches();
            FormUtils.cachedLookAndFeel = currentLookAndFeel;
        }
    }
}
