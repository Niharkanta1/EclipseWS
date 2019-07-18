// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import com.sun.jna.platform.win32.WTypes;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Date;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Variant;

public class Convert
{
    public static Variant.VARIANT toVariant(final Object value) {
        if (value instanceof Boolean) {
            return new Variant.VARIANT((boolean)value);
        }
        if (value instanceof Long) {
            return new Variant.VARIANT(new WinDef.LONG((long)value));
        }
        if (value instanceof Integer) {
            return new Variant.VARIANT((int)value);
        }
        if (value instanceof Short) {
            return new Variant.VARIANT(new WinDef.SHORT((long)(short)value));
        }
        if (value instanceof Float) {
            return new Variant.VARIANT((float)value);
        }
        if (value instanceof Double) {
            return new Variant.VARIANT((double)value);
        }
        if (value instanceof String) {
            return new Variant.VARIANT((String)value);
        }
        if (value instanceof Date) {
            return new Variant.VARIANT((Date)value);
        }
        if (value instanceof Proxy) {
            final InvocationHandler ih = Proxy.getInvocationHandler(value);
            final ProxyObject pobj = (ProxyObject)ih;
            return new Variant.VARIANT(pobj.getRawDispatch());
        }
        if (value instanceof IComEnum) {
            final IComEnum enm = (IComEnum)value;
            return new Variant.VARIANT(new WinDef.LONG(enm.getValue()));
        }
        return null;
    }
    
    public static Object toJavaObject(final Variant.VARIANT value) {
        if (null == value) {
            return null;
        }
        final Object vobj = value.getValue();
        if (vobj instanceof WinDef.BOOL) {
            return ((WinDef.BOOL)vobj).booleanValue();
        }
        if (vobj instanceof WinDef.LONG) {
            return ((WinDef.LONG)vobj).longValue();
        }
        if (vobj instanceof WinDef.SHORT) {
            return ((WinDef.SHORT)vobj).shortValue();
        }
        if (vobj instanceof WinDef.UINT) {
            return ((WinDef.UINT)vobj).intValue();
        }
        if (vobj instanceof WinDef.WORD) {
            return ((WinDef.WORD)vobj).intValue();
        }
        if (vobj instanceof WTypes.BSTR) {
            return ((WTypes.BSTR)vobj).getValue();
        }
        return vobj;
    }
    
    public static <T extends IComEnum> T toComEnum(final Class<T> enumType, final Object value) {
        try {
            final Method m = enumType.getMethod("values", (Class<?>[])new Class[0]);
            final IComEnum[] array;
            final T[] values = (T[])(array = (IComEnum[])m.invoke(null, new Object[0]));
            for (final T t : array) {
                if (value.equals(t.getValue())) {
                    return t;
                }
            }
        }
        catch (NoSuchMethodException e) {}
        catch (IllegalAccessException e2) {}
        catch (IllegalArgumentException e3) {}
        catch (InvocationTargetException ex) {}
        return null;
    }
}
