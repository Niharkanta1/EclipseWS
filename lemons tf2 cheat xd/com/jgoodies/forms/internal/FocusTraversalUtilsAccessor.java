// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.internal;

import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractButton;
import java.lang.reflect.Method;

public final class FocusTraversalUtilsAccessor
{
    private static final String FOCUS_TRAVERSAL_UTILS_NAME = "com.jgoodies.jsdl.common.focus.FocusTraversalUtils";
    private static Method groupMethod;
    
    private FocusTraversalUtilsAccessor() {
    }
    
    public static void tryToBuildAFocusGroup(final AbstractButton... buttons) {
        if (FocusTraversalUtilsAccessor.groupMethod == null) {
            return;
        }
        try {
            FocusTraversalUtilsAccessor.groupMethod.invoke(null, buttons);
        }
        catch (IllegalAccessException e) {}
        catch (InvocationTargetException ex) {}
    }
    
    private static Method getGroupMethod() {
        try {
            final Class<?> clazz = Class.forName("com.jgoodies.jsdl.common.focus.FocusTraversalUtils");
            return clazz.getMethod("group", AbstractButton[].class);
        }
        catch (ClassNotFoundException e) {}
        catch (SecurityException e2) {}
        catch (NoSuchMethodException ex) {}
        return null;
    }
    
    static {
        FocusTraversalUtilsAccessor.groupMethod = null;
        FocusTraversalUtilsAccessor.groupMethod = getGroupMethod();
    }
}
