// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.util.HashMap;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public final class LinkHandler
{
    public static final int X = 0;
    public static final int Y = 1;
    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;
    public static final int X2 = 4;
    public static final int Y2 = 5;
    private static final ArrayList<WeakReference<Object>> LAYOUTS;
    private static final ArrayList<HashMap<String, int[]>> VALUES;
    private static final ArrayList<HashMap<String, int[]>> VALUES_TEMP;
    
    private LinkHandler() {
    }
    
    public static synchronized Integer getValue(final Object o, final String s, final int n) {
        Integer value = null;
        int n2 = 1;
        for (int i = LinkHandler.LAYOUTS.size() - 1; i >= 0; --i) {
            final Object value2 = LinkHandler.LAYOUTS.get(i).get();
            if (value == null && value2 == o) {
                final int[] array = LinkHandler.VALUES_TEMP.get(i).get(s);
                if (n2 != 0 && array != null && array[n] != -2147471302) {
                    value = array[n];
                }
                else {
                    final int[] array2 = LinkHandler.VALUES.get(i).get(s);
                    value = ((array2 != null && array2[n] != -2147471302) ? Integer.valueOf(array2[n]) : null);
                }
                n2 = 0;
            }
            if (value2 == null) {
                LinkHandler.LAYOUTS.remove(i);
                LinkHandler.VALUES.remove(i);
                LinkHandler.VALUES_TEMP.remove(i);
            }
        }
        return value;
    }
    
    public static synchronized boolean setBounds(final Object o, final String s, final int n, final int n2, final int n3, final int n4) {
        return setBounds(o, s, n, n2, n3, n4, false, false);
    }
    
    static synchronized boolean setBounds(final Object o, final String s, final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
        int i = LinkHandler.LAYOUTS.size() - 1;
        while (i >= 0) {
            if (LinkHandler.LAYOUTS.get(i).get() == o) {
                final HashMap<String, int[]> hashMap = (b ? LinkHandler.VALUES_TEMP : LinkHandler.VALUES).get(i);
                final int[] array = hashMap.get(s);
                if (array != null && array[0] == n && array[1] == n2 && array[2] == n3 && array[3] == n4) {
                    return false;
                }
                if (array == null || !b2) {
                    hashMap.put(s, new int[] { n, n2, n3, n4, n + n3, n2 + n4 });
                    return true;
                }
                boolean b3 = false;
                if (n != -2147471302) {
                    if (array[0] == -2147471302 || n < array[0]) {
                        array[0] = n;
                        array[2] = array[4] - n;
                        b3 = true;
                    }
                    if (n3 != -2147471302) {
                        final int n5 = n + n3;
                        if (array[4] == -2147471302 || n5 > array[4]) {
                            array[4] = n5;
                            array[2] = n5 - array[0];
                            b3 = true;
                        }
                    }
                }
                if (n2 != -2147471302) {
                    if (array[1] == -2147471302 || n2 < array[1]) {
                        array[1] = n2;
                        array[3] = array[5] - n2;
                        b3 = true;
                    }
                    if (n4 != -2147471302) {
                        final int n6 = n2 + n4;
                        if (array[5] == -2147471302 || n6 > array[5]) {
                            array[5] = n6;
                            array[3] = n6 - array[1];
                            b3 = true;
                        }
                    }
                }
                return b3;
            }
            else {
                --i;
            }
        }
        LinkHandler.LAYOUTS.add(new WeakReference<Object>(o));
        final int[] array2 = { n, n2, n3, n4, n + n3, n2 + n4 };
        final HashMap<String, int[]> hashMap2 = new HashMap<String, int[]>(4);
        if (b) {
            hashMap2.put(s, array2);
        }
        LinkHandler.VALUES_TEMP.add(hashMap2);
        final HashMap<String, int[]> hashMap3 = new HashMap<String, int[]>(4);
        if (!b) {
            hashMap3.put(s, array2);
        }
        LinkHandler.VALUES.add(hashMap3);
        return true;
    }
    
    public static synchronized void clearWeakReferencesNow() {
        LinkHandler.LAYOUTS.clear();
    }
    
    public static synchronized boolean clearBounds(final Object o, final String s) {
        for (int i = LinkHandler.LAYOUTS.size() - 1; i >= 0; --i) {
            if (LinkHandler.LAYOUTS.get(i).get() == o) {
                return LinkHandler.VALUES.get(i).remove(s) != null;
            }
        }
        return false;
    }
    
    static synchronized void clearTemporaryBounds(final Object o) {
        for (int i = LinkHandler.LAYOUTS.size() - 1; i >= 0; --i) {
            if (LinkHandler.LAYOUTS.get(i).get() == o) {
                LinkHandler.VALUES_TEMP.get(i).clear();
                return;
            }
        }
    }
    
    static {
        LAYOUTS = new ArrayList<WeakReference<Object>>(4);
        VALUES = new ArrayList<HashMap<String, int[]>>(4);
        VALUES_TEMP = new ArrayList<HashMap<String, int[]>>(4);
    }
}
