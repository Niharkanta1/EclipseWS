// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.io.EOFException;
import java.io.InputStream;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.beans.XMLEncoder;
import java.beans.ExceptionListener;
import java.io.OutputStream;
import java.util.TreeSet;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.Beans;
import java.util.IdentityHashMap;
import java.io.ByteArrayOutputStream;
import java.util.WeakHashMap;

public final class LayoutUtil
{
    static final int INF = 2097051;
    static final int NOT_SET = -2147471302;
    public static final int MIN = 0;
    public static final int PREF = 1;
    public static final int MAX = 2;
    private static volatile WeakHashMap<Object, String> CR_MAP;
    private static volatile WeakHashMap<Object, Boolean> DT_MAP;
    private static int eSz;
    private static int globalDebugMillis;
    private static ByteArrayOutputStream writeOutputStream;
    private static byte[] readBuf;
    private static final IdentityHashMap<Object, Object> SER_MAP;
    
    private LayoutUtil() {
    }
    
    public static String getVersion() {
        return "3.7.4";
    }
    
    public static int getGlobalDebugMillis() {
        return LayoutUtil.globalDebugMillis;
    }
    
    public static void setGlobalDebugMillis(final int globalDebugMillis) {
        LayoutUtil.globalDebugMillis = globalDebugMillis;
    }
    
    public static void setDesignTime(final ContainerWrapper containerWrapper, final boolean b) {
        if (LayoutUtil.DT_MAP == null) {
            LayoutUtil.DT_MAP = new WeakHashMap<Object, Boolean>();
        }
        LayoutUtil.DT_MAP.put((containerWrapper != null) ? containerWrapper.getComponent() : null, b);
    }
    
    public static boolean isDesignTime(ContainerWrapper containerWrapper) {
        if (LayoutUtil.DT_MAP == null) {
            return Beans.isDesignTime();
        }
        if (containerWrapper != null && !LayoutUtil.DT_MAP.containsKey(containerWrapper.getComponent())) {
            containerWrapper = null;
        }
        final Boolean b = LayoutUtil.DT_MAP.get((containerWrapper != null) ? containerWrapper.getComponent() : null);
        return b != null && b;
    }
    
    public static int getDesignTimeEmptySize() {
        return LayoutUtil.eSz;
    }
    
    public static void setDesignTimeEmptySize(final int eSz) {
        LayoutUtil.eSz = eSz;
    }
    
    static void putCCString(final Object o, final String s) {
        if (s != null && o != null && isDesignTime(null)) {
            if (LayoutUtil.CR_MAP == null) {
                LayoutUtil.CR_MAP = new WeakHashMap<Object, String>(64);
            }
            LayoutUtil.CR_MAP.put(o, s);
        }
    }
    
    static synchronized void setDelegate(final Class clazz, final PersistenceDelegate persistenceDelegate) {
        try {
            Introspector.getBeanInfo(clazz, 3).getBeanDescriptor().setValue("persistenceDelegate", persistenceDelegate);
        }
        catch (Exception ex) {}
    }
    
    static String getCCString(final Object o) {
        return (LayoutUtil.CR_MAP != null) ? LayoutUtil.CR_MAP.get(o) : null;
    }
    
    static void throwCC() {
        throw new IllegalStateException("setStoreConstraintData(true) must be set for strings to be saved.");
    }
    
    static int[] calculateSerial(final int[][] array, final ResizeConstraint[] array2, final Float[] array3, final int n, final int n2) {
        final float[] array4 = new float[array.length];
        float n3 = 0.0f;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != null) {
                float n4 = (array[i][n] != -2147471302) ? ((float)array[i][n]) : 0.0f;
                final int brokenBoundary = getBrokenBoundary(n4, array[i][0], array[i][2]);
                if (brokenBoundary != -2147471302) {
                    n4 = (float)brokenBoundary;
                }
                n3 += n4;
                array4[i] = n4;
            }
        }
        final int round = Math.round(n3);
        if (round != n2 && array2 != null) {
            final boolean b = round < n2;
            final TreeSet<Integer> set = new TreeSet<Integer>();
            for (int j = 0; j < array.length; ++j) {
                final ResizeConstraint resizeConstraint = (ResizeConstraint)getIndexSafe(array2, j);
                if (resizeConstraint != null) {
                    set.add(b ? resizeConstraint.growPrio : resizeConstraint.shrinkPrio);
                }
            }
            final Integer[] array5 = set.toArray(new Integer[set.size()]);
            for (int k = 0; k <= ((b && array3 != null) ? 1 : 0); ++k) {
                for (int l = array5.length - 1; l >= 0; --l) {
                    final int intValue = array5[l];
                    float n5 = 0.0f;
                    final Float[] array6 = new Float[array.length];
                    for (int n6 = 0; n6 < array.length; ++n6) {
                        if (array[n6] != null) {
                            final ResizeConstraint resizeConstraint2 = (ResizeConstraint)getIndexSafe(array2, n6);
                            if (resizeConstraint2 != null && intValue == (b ? resizeConstraint2.growPrio : resizeConstraint2.shrinkPrio)) {
                                if (b) {
                                    array6[n6] = ((k == 0 || resizeConstraint2.grow != null) ? resizeConstraint2.grow : array3[(n6 < array3.length) ? n6 : (array3.length - 1)]);
                                }
                                else {
                                    array6[n6] = resizeConstraint2.shrink;
                                }
                                if (array6[n6] != null) {
                                    n5 += array6[n6];
                                }
                            }
                        }
                    }
                    if (n5 > 0.0f) {
                        boolean b2;
                        do {
                            final float n7 = n2 - n3;
                            b2 = false;
                            float n8 = 0.0f;
                            for (int n9 = 0; n9 < array.length && n5 > 1.0E-4f; ++n9) {
                                final Float n10 = array6[n9];
                                if (n10 != null) {
                                    float n11 = n7 * n10 / n5;
                                    float n12 = array4[n9] + n11;
                                    if (array[n9] != null) {
                                        final int brokenBoundary2 = getBrokenBoundary(n12, array[n9][0], array[n9][2]);
                                        if (brokenBoundary2 != -2147471302) {
                                            array6[n9] = null;
                                            b2 = true;
                                            n8 += n10;
                                            n12 = (float)brokenBoundary2;
                                            n11 = n12 - array4[n9];
                                        }
                                    }
                                    array4[n9] = n12;
                                    n3 += n11;
                                }
                            }
                            n5 -= n8;
                        } while (b2);
                    }
                }
            }
        }
        return roundSizes(array4);
    }
    
    static Object getIndexSafe(final Object[] array, final int n) {
        return (array != null) ? array[(n < array.length) ? n : (array.length - 1)] : null;
    }
    
    private static int getBrokenBoundary(final float n, final int n2, final int n3) {
        if (n2 != -2147471302) {
            if (n < n2) {
                return n2;
            }
        }
        else if (n < 0.0f) {
            return 0;
        }
        if (n3 != -2147471302 && n > n3) {
            return n3;
        }
        return -2147471302;
    }
    
    static int sum(final int[] array, final int n, final int n2) {
        int n3 = 0;
        for (int i = n; i < n + n2; ++i) {
            n3 += array[i];
        }
        return n3;
    }
    
    static int sum(final int[] array) {
        return sum(array, 0, array.length);
    }
    
    public static int getSizeSafe(final int[] array, final int n) {
        if (array == null || array[n] == -2147471302) {
            return (n == 2) ? 2097051 : 0;
        }
        return array[n];
    }
    
    static BoundSize derive(final BoundSize boundSize, final UnitValue unitValue, final UnitValue unitValue2, final UnitValue unitValue3) {
        if (boundSize == null || boundSize.isUnset()) {
            return new BoundSize(unitValue, unitValue2, unitValue3, null);
        }
        return new BoundSize((unitValue != null) ? unitValue : boundSize.getMin(), (unitValue2 != null) ? unitValue2 : boundSize.getPreferred(), (unitValue3 != null) ? unitValue3 : boundSize.getMax(), boundSize.getGapPush(), null);
    }
    
    public static boolean isLeftToRight(final LC lc, final ContainerWrapper containerWrapper) {
        if (lc != null && lc.getLeftToRight() != null) {
            return lc.getLeftToRight();
        }
        return containerWrapper == null || containerWrapper.isLeftToRight();
    }
    
    static int[] roundSizes(final float[] array) {
        final int[] array2 = new int[array.length];
        float n = 0.0f;
        for (int i = 0; i < array2.length; ++i) {
            final int n2 = (int)(n + 0.5f);
            n += array[i];
            array2[i] = (int)(n + 0.5f) - n2;
        }
        return array2;
    }
    
    static boolean equals(final Object o, final Object o2) {
        return o == o2 || (o != null && o2 != null && o.equals(o2));
    }
    
    static UnitValue getInsets(final LC lc, final int n, final boolean b) {
        final UnitValue[] insets = lc.getInsets();
        return (insets != null && insets[n] != null) ? insets[n] : (b ? PlatformDefaults.getPanelInsets(n) : UnitValue.ZERO);
    }
    
    static void writeXMLObject(final OutputStream outputStream, final Object o, final ExceptionListener exceptionListener) {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(LayoutUtil.class.getClassLoader());
        final XMLEncoder xmlEncoder = new XMLEncoder(outputStream);
        if (exceptionListener != null) {
            xmlEncoder.setExceptionListener(exceptionListener);
        }
        xmlEncoder.writeObject(o);
        xmlEncoder.close();
        Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
    
    public static synchronized void writeAsXML(final ObjectOutput objectOutput, final Object o) throws IOException {
        if (LayoutUtil.writeOutputStream == null) {
            LayoutUtil.writeOutputStream = new ByteArrayOutputStream(16384);
        }
        LayoutUtil.writeOutputStream.reset();
        writeXMLObject(LayoutUtil.writeOutputStream, o, new ExceptionListener() {
            public void exceptionThrown(final Exception ex) {
                ex.printStackTrace();
            }
        });
        final byte[] byteArray = LayoutUtil.writeOutputStream.toByteArray();
        objectOutput.writeInt(byteArray.length);
        objectOutput.write(byteArray);
    }
    
    public static synchronized Object readAsXML(final ObjectInput objectInput) throws IOException {
        if (LayoutUtil.readBuf == null) {
            LayoutUtil.readBuf = new byte[16384];
        }
        final Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = null;
        try {
            contextClassLoader = currentThread.getContextClassLoader();
            currentThread.setContextClassLoader(LayoutUtil.class.getClassLoader());
        }
        catch (SecurityException ex) {}
        Object object = null;
        try {
            final int int1 = objectInput.readInt();
            if (int1 > LayoutUtil.readBuf.length) {
                LayoutUtil.readBuf = new byte[int1];
            }
            objectInput.readFully(LayoutUtil.readBuf, 0, int1);
            object = new XMLDecoder(new ByteArrayInputStream(LayoutUtil.readBuf, 0, int1)).readObject();
        }
        catch (EOFException ex2) {}
        if (contextClassLoader != null) {
            currentThread.setContextClassLoader(contextClassLoader);
        }
        return object;
    }
    
    public static void setSerializedObject(final Object o, final Object o2) {
        synchronized (LayoutUtil.SER_MAP) {
            LayoutUtil.SER_MAP.put(o, o2);
        }
    }
    
    public static Object getSerializedObject(final Object o) {
        synchronized (LayoutUtil.SER_MAP) {
            return LayoutUtil.SER_MAP.remove(o);
        }
    }
    
    static {
        LayoutUtil.CR_MAP = null;
        LayoutUtil.DT_MAP = null;
        LayoutUtil.eSz = 0;
        LayoutUtil.globalDebugMillis = 0;
        LayoutUtil.writeOutputStream = null;
        LayoutUtil.readBuf = null;
        SER_MAP = new IdentityHashMap<Object, Object>(2);
    }
}
