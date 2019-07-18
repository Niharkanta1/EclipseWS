// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.ComponentFactory;

public class FormsSetup
{
    private static final String DEBUG_TOOL_TIPS_ENABLED_KEY = "FormsSetup.debugToolTipsEnabled";
    private static ComponentFactory componentFactoryDefault;
    private static boolean labelForFeatureEnabledDefault;
    private static boolean opaqueDefault;
    private static boolean debugToolTipsEnabled;
    
    private FormsSetup() {
    }
    
    public static ComponentFactory getComponentFactoryDefault() {
        if (FormsSetup.componentFactoryDefault == null) {
            FormsSetup.componentFactoryDefault = new DefaultComponentFactory();
        }
        return FormsSetup.componentFactoryDefault;
    }
    
    public static void setComponentFactoryDefault(final ComponentFactory factory) {
        FormsSetup.componentFactoryDefault = factory;
    }
    
    public static boolean getLabelForFeatureEnabledDefault() {
        return FormsSetup.labelForFeatureEnabledDefault;
    }
    
    public static void setLabelForFeatureEnabledDefault(final boolean b) {
        FormsSetup.labelForFeatureEnabledDefault = b;
    }
    
    public static boolean getOpaqueDefault() {
        return FormsSetup.opaqueDefault;
    }
    
    public static void setOpaqueDefault(final boolean b) {
        FormsSetup.opaqueDefault = b;
    }
    
    public static boolean getDebugToolTipsEnabledDefault() {
        return FormsSetup.debugToolTipsEnabled;
    }
    
    public static void setDebugToolTipsEnabled(final boolean b) {
        FormsSetup.debugToolTipsEnabled = b;
    }
    
    private static boolean getDebugToolTipSystemProperty() {
        try {
            final String value = System.getProperty("FormsSetup.debugToolTipsEnabled");
            return "true".equalsIgnoreCase(value);
        }
        catch (SecurityException e) {
            return false;
        }
    }
    
    static {
        FormsSetup.labelForFeatureEnabledDefault = true;
        FormsSetup.opaqueDefault = false;
        FormsSetup.debugToolTipsEnabled = getDebugToolTipSystemProperty();
    }
}
