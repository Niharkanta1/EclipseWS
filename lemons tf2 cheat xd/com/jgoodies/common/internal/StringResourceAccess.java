// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.internal;

import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jgoodies.common.base.Preconditions;

public final class StringResourceAccess
{
    public static String getResourceString(final StringResourceAccessor accessor, final String key, final Object... args) {
        Preconditions.checkNotNull(accessor, "To use the internationalization support you must provide a ResourceBundle, ResourceMap, or a StringResourceAccessor. See #resources.");
        try {
            return accessor.getString(key, args);
        }
        catch (MissingResourceException ex) {
            Logger.getLogger(StringResourceAccess.class.getName()).log(Level.WARNING, "Missing internationalized text", ex);
            return key;
        }
    }
}
