// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.internal;

import java.util.MissingResourceException;
import com.jgoodies.common.base.Strings;
import javax.swing.Icon;
import com.jgoodies.common.base.Preconditions;
import java.util.ResourceBundle;

public final class ResourceBundleAccessor implements StringAndIconResourceAccessor
{
    private final ResourceBundle bundle;
    
    public ResourceBundleAccessor(final ResourceBundle bundle) {
        this.bundle = Preconditions.checkNotNull(bundle, "The %1$s must not be null.", "resource bundle");
    }
    
    @Override
    public Icon getIcon(final String key) {
        return (Icon)this.bundle.getObject(key);
    }
    
    @Override
    public String getString(final String key, final Object... args) {
        try {
            return Strings.get(this.bundle.getString(key), args);
        }
        catch (MissingResourceException mre) {
            return key;
        }
    }
}
