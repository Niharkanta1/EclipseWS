// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.base;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class SystemUtils
{
    protected static final String OS_NAME;
    protected static final String OS_VERSION;
    protected static final String JAVA_VERSION;
    public static final boolean IS_OS_LINUX;
    public static final boolean IS_OS_MAC;
    public static final boolean IS_OS_SOLARIS;
    public static final boolean IS_OS_WINDOWS;
    public static final boolean IS_OS_WINDOWS_98;
    public static final boolean IS_OS_WINDOWS_ME;
    public static final boolean IS_OS_WINDOWS_2000;
    public static final boolean IS_OS_WINDOWS_XP;
    public static final boolean IS_OS_WINDOWS_XP_64_BIT_OR_SERVER_2003;
    public static final boolean IS_OS_WINDOWS_VISTA;
    public static final boolean IS_OS_WINDOWS_7;
    public static final boolean IS_OS_WINDOWS_8;
    public static final boolean IS_OS_WINDOWS_6_OR_LATER;
    public static final boolean IS_JAVA_6;
    public static final boolean IS_JAVA_7;
    public static final boolean IS_JAVA_7_OR_LATER;
    public static final boolean IS_JAVA_8;
    public static final boolean IS_JAVA_8_OR_LATER;
    public static final boolean HAS_MODERN_RASTERIZER;
    public static final boolean IS_LAF_WINDOWS_XP_ENABLED;
    public static final boolean IS_LOW_RESOLUTION;
    private static final String AWT_UTILITIES_CLASS_NAME = "com.sun.awt.AWTUtilities";
    
    public static boolean isLafAqua() {
        return UIManager.getLookAndFeel().getID().equals("Aqua");
    }
    
    protected SystemUtils() {
    }
    
    protected static String getSystemProperty(final String key) {
        try {
            return System.getProperty(key);
        }
        catch (SecurityException e) {
            Logger.getLogger(SystemUtils.class.getName()).warning("Can't access the System property " + key + ".");
            return "";
        }
    }
    
    protected static boolean startsWith(final String str, final String prefix) {
        return str != null && str.startsWith(prefix);
    }
    
    private static boolean hasModernRasterizer() {
        try {
            Class.forName("com.sun.awt.AWTUtilities");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    private static boolean isWindowsXPLafEnabled() {
        return SystemUtils.IS_OS_WINDOWS && Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive")) && getSystemProperty("swing.noxp") == null;
    }
    
    private static boolean isLowResolution() {
        try {
            return Toolkit.getDefaultToolkit().getScreenResolution() < 120;
        }
        catch (HeadlessException e) {
            return true;
        }
    }
    
    static {
        OS_NAME = getSystemProperty("os.name");
        OS_VERSION = getSystemProperty("os.version");
        JAVA_VERSION = getSystemProperty("java.version");
        IS_OS_LINUX = (startsWith(SystemUtils.OS_NAME, "Linux") || startsWith(SystemUtils.OS_NAME, "LINUX"));
        IS_OS_MAC = startsWith(SystemUtils.OS_NAME, "Mac OS");
        IS_OS_SOLARIS = startsWith(SystemUtils.OS_NAME, "Solaris");
        IS_OS_WINDOWS = startsWith(SystemUtils.OS_NAME, "Windows");
        IS_OS_WINDOWS_98 = (startsWith(SystemUtils.OS_NAME, "Windows 9") && startsWith(SystemUtils.OS_VERSION, "4.1"));
        IS_OS_WINDOWS_ME = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "4.9"));
        IS_OS_WINDOWS_2000 = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "5.0"));
        IS_OS_WINDOWS_XP = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "5.1"));
        IS_OS_WINDOWS_XP_64_BIT_OR_SERVER_2003 = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "5.2"));
        IS_OS_WINDOWS_VISTA = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "6.0"));
        IS_OS_WINDOWS_7 = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "6.1"));
        IS_OS_WINDOWS_8 = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "6.2"));
        IS_OS_WINDOWS_6_OR_LATER = (startsWith(SystemUtils.OS_NAME, "Windows") && startsWith(SystemUtils.OS_VERSION, "6."));
        IS_JAVA_6 = startsWith(SystemUtils.JAVA_VERSION, "1.6");
        IS_JAVA_7 = startsWith(SystemUtils.JAVA_VERSION, "1.7");
        IS_JAVA_7_OR_LATER = !SystemUtils.IS_JAVA_6;
        IS_JAVA_8 = startsWith(SystemUtils.JAVA_VERSION, "1.8");
        IS_JAVA_8_OR_LATER = (!SystemUtils.IS_JAVA_6 && !SystemUtils.IS_JAVA_7);
        HAS_MODERN_RASTERIZER = hasModernRasterizer();
        IS_LAF_WINDOWS_XP_ENABLED = isWindowsXPLafEnabled();
        IS_LOW_RESOLUTION = isLowResolution();
    }
}
