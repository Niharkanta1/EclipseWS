// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.base;

public final class Preconditions
{
    private Preconditions() {
    }
    
    public static void checkArgument(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static void checkArgument(final boolean expression, final String messageFormat, final Object... messageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(messageFormat, messageArgs));
        }
    }
    
    public static <T> T checkNotNull(final T reference, final String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }
        return reference;
    }
    
    public static <T> T checkNotNull(final T reference, final String messageFormat, final Object... messageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(messageFormat, messageArgs));
        }
        return reference;
    }
    
    public static void checkState(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }
    
    public static void checkState(final boolean expression, final String messageFormat, final Object... messageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(messageFormat, messageArgs));
        }
    }
    
    public static String checkNotBlank(final String str, final String message) {
        checkNotNull(str, message);
        checkArgument(Strings.isNotBlank(str), message);
        return str;
    }
    
    public static String checkNotBlank(final String str, final String messageFormat, final Object... messageArgs) {
        checkNotNull(str, messageFormat, messageArgs);
        checkArgument(Strings.isNotBlank(str), messageFormat, messageArgs);
        return str;
    }
    
    static String format(final String messageFormat, final Object... messageArgs) {
        return String.format(messageFormat, messageArgs);
    }
}
