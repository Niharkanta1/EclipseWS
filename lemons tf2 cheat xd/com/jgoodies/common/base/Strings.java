// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.base;

public class Strings
{
    public static final String NO_ELLIPSIS_STRING = "...";
    public static final String ELLIPSIS_STRING = "\u2026";
    
    protected Strings() {
    }
    
    public static boolean isBlank(final String str) {
        final int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = length - 1; i >= 0; --i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(final String str) {
        final int length;
        if (str == null || (length = str.length()) == 0) {
            return false;
        }
        for (int i = length - 1; i >= 0; --i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }
    
    public static boolean isNotEmpty(final String str) {
        return str != null && str.length() > 0;
    }
    
    public static boolean isTrimmed(final String str) {
        final int length;
        return str == null || (length = str.length()) == 0 || (!Character.isWhitespace(str.charAt(0)) && !Character.isWhitespace(str.charAt(length - 1)));
    }
    
    public static boolean startsWithIgnoreCase(final String str, final String prefix) {
        if (str == null) {
            return prefix == null;
        }
        return prefix != null && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }
    
    public static String abbreviateCenter(final String str, final int maxLength) {
        if (str == null) {
            return null;
        }
        final int length = str.length();
        if (length <= maxLength) {
            return str;
        }
        final int headLength = maxLength / 2;
        final int tailLength = maxLength - headLength - 1;
        final String head = str.substring(0, headLength);
        final String tail = str.substring(length - tailLength, length);
        return head + "\u2026" + tail;
    }
    
    public static String get(final String str, final Object... args) {
        return (args == null || args.length == 0) ? str : String.format(str, args);
    }
}
