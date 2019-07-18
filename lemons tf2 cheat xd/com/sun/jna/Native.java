// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.awt.GraphicsEnvironment;
import java.util.WeakHashMap;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.io.FilenameFilter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Method;
import java.io.InputStream;
import java.net.URL;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Arrays;
import java.net.URLClassLoader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.util.Map;

public final class Native implements Version
{
    public static final String DEFAULT_ENCODING = "utf8";
    public static boolean DEBUG_LOAD;
    public static boolean DEBUG_JNA_LOAD;
    static String jnidispatchPath;
    private static Map options;
    private static Map libraries;
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    static final int MAX_ALIGNMENT;
    static final int MAX_PADDING;
    private static final Object finalizer;
    static final String JNA_TMPLIB_PREFIX = "jna";
    private static Map registeredClasses;
    private static Map registeredLibraries;
    static final int CB_HAS_INITIALIZER = 1;
    private static final int CVT_UNSUPPORTED = -1;
    private static final int CVT_DEFAULT = 0;
    private static final int CVT_POINTER = 1;
    private static final int CVT_STRING = 2;
    private static final int CVT_STRUCTURE = 3;
    private static final int CVT_STRUCTURE_BYVAL = 4;
    private static final int CVT_BUFFER = 5;
    private static final int CVT_ARRAY_BYTE = 6;
    private static final int CVT_ARRAY_SHORT = 7;
    private static final int CVT_ARRAY_CHAR = 8;
    private static final int CVT_ARRAY_INT = 9;
    private static final int CVT_ARRAY_LONG = 10;
    private static final int CVT_ARRAY_FLOAT = 11;
    private static final int CVT_ARRAY_DOUBLE = 12;
    private static final int CVT_ARRAY_BOOLEAN = 13;
    private static final int CVT_BOOLEAN = 14;
    private static final int CVT_CALLBACK = 15;
    private static final int CVT_FLOAT = 16;
    private static final int CVT_NATIVE_MAPPED = 17;
    private static final int CVT_NATIVE_MAPPED_STRING = 18;
    private static final int CVT_NATIVE_MAPPED_WSTRING = 19;
    private static final int CVT_WSTRING = 20;
    private static final int CVT_INTEGER_TYPE = 21;
    private static final int CVT_POINTER_TYPE = 22;
    private static final int CVT_TYPE_MAPPER = 23;
    private static final int CVT_TYPE_MAPPER_STRING = 24;
    private static final int CVT_TYPE_MAPPER_WSTRING = 25;
    static final int CB_OPTION_DIRECT = 1;
    static final int CB_OPTION_IN_DLL = 2;
    private static Map nativeThreads;
    private static ThreadLocal nativeThreadTerminationFlag;
    
    public static float parseVersion(final String v) {
        return Float.parseFloat(v.substring(0, v.lastIndexOf(".")));
    }
    
    private static void dispose() {
        CallbackReference.disposeAll();
        Memory.disposeAll();
        NativeLibrary.disposeAll();
        unregisterAll();
        Native.jnidispatchPath = null;
        System.setProperty("jna.loaded", "false");
    }
    
    static boolean deleteLibrary(final File lib) {
        if (lib.delete()) {
            return true;
        }
        markTemporaryFile(lib);
        return false;
    }
    
    private Native() {
    }
    
    private static native void initIDs();
    
    public static synchronized native void setProtected(final boolean p0);
    
    public static synchronized native boolean isProtected();
    
    @Deprecated
    public static void setPreserveLastError(final boolean enable) {
    }
    
    @Deprecated
    public static boolean getPreserveLastError() {
        return true;
    }
    
    public static long getWindowID(final Window w) throws HeadlessException {
        return AWT.getWindowID(w);
    }
    
    public static long getComponentID(final Component c) throws HeadlessException {
        return AWT.getComponentID(c);
    }
    
    public static Pointer getWindowPointer(final Window w) throws HeadlessException {
        return new Pointer(AWT.getWindowID(w));
    }
    
    public static Pointer getComponentPointer(final Component c) throws HeadlessException {
        return new Pointer(AWT.getComponentID(c));
    }
    
    static native long getWindowHandle0(final Component p0);
    
    public static Pointer getDirectBufferPointer(final Buffer b) {
        final long peer = _getDirectBufferPointer(b);
        return (peer == 0L) ? null : new Pointer(peer);
    }
    
    private static native long _getDirectBufferPointer(final Buffer p0);
    
    public static String toString(final byte[] buf) {
        return toString(buf, getDefaultStringEncoding());
    }
    
    public static String toString(final byte[] buf, final String encoding) {
        int len = buf.length;
        for (int index = 0; index < len; ++index) {
            if (buf[index] == 0) {
                len = index;
                break;
            }
        }
        if (len == 0) {
            return "";
        }
        if (encoding != null) {
            try {
                return new String(buf, 0, len, encoding);
            }
            catch (UnsupportedEncodingException e) {
                System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
            }
        }
        System.err.println("JNA Warning: Decoding with fallback " + System.getProperty("file.encoding"));
        return new String(buf, 0, len);
    }
    
    public static String toString(final char[] buf) {
        int len = buf.length;
        for (int index = 0; index < len; ++index) {
            if (buf[index] == '\0') {
                len = index;
                break;
            }
        }
        if (len == 0) {
            return "";
        }
        return new String(buf, 0, len);
    }
    
    public static List<String> toStringList(final char[] buf) {
        return toStringList(buf, 0, buf.length);
    }
    
    public static List<String> toStringList(final char[] buf, final int offset, final int len) {
        final List<String> list = new ArrayList<String>();
        int lastPos = offset;
        final int maxPos = offset + len;
        for (int curPos = offset; curPos < maxPos; ++curPos) {
            if (buf[curPos] == '\0') {
                if (lastPos == curPos) {
                    return list;
                }
                final String value = new String(buf, lastPos, curPos - lastPos);
                list.add(value);
                lastPos = curPos + 1;
            }
        }
        if (lastPos < maxPos) {
            final String value2 = new String(buf, lastPos, maxPos - lastPos);
            list.add(value2);
        }
        return list;
    }
    
    public static Object loadLibrary(final Class interfaceClass) {
        return loadLibrary(null, interfaceClass);
    }
    
    public static Object loadLibrary(final Class interfaceClass, final Map options) {
        return loadLibrary(null, interfaceClass, options);
    }
    
    public static Object loadLibrary(final String name, final Class interfaceClass) {
        return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
    }
    
    public static Object loadLibrary(final String name, final Class interfaceClass, final Map options) {
        final Library.Handler handler = new Library.Handler(name, interfaceClass, options);
        final ClassLoader loader = interfaceClass.getClassLoader();
        final Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
        cacheOptions(interfaceClass, options, proxy);
        return proxy;
    }
    
    private static void loadLibraryInstance(final Class cls) {
        synchronized (Native.libraries) {
            if (cls != null && !Native.libraries.containsKey(cls)) {
                try {
                    final Field[] fields = cls.getFields();
                    for (int i = 0; i < fields.length; ++i) {
                        final Field field = fields[i];
                        if (field.getType() == cls && Modifier.isStatic(field.getModifiers())) {
                            Native.libraries.put(cls, new WeakReference<Object>(field.get(null)));
                            break;
                        }
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
                }
            }
        }
    }
    
    static Class findEnclosingLibraryClass(Class cls) {
        if (cls == null) {
            return null;
        }
        synchronized (Native.libraries) {
            if (Native.options.containsKey(cls)) {
                return cls;
            }
        }
        if (Library.class.isAssignableFrom(cls)) {
            return cls;
        }
        if (Callback.class.isAssignableFrom(cls)) {
            cls = CallbackReference.findCallbackClass(cls);
        }
        final Class declaring = cls.getDeclaringClass();
        final Class fromDeclaring = findEnclosingLibraryClass(declaring);
        if (fromDeclaring != null) {
            return fromDeclaring;
        }
        return findEnclosingLibraryClass(cls.getSuperclass());
    }
    
    public static Map getLibraryOptions(final Class type) {
        synchronized (Native.libraries) {
            if (Native.options.containsKey(type)) {
                return (Map)Native.options.get(type);
            }
        }
        Class mappingClass = findEnclosingLibraryClass(type);
        if (mappingClass != null) {
            loadLibraryInstance(mappingClass);
        }
        else {
            mappingClass = type;
        }
        synchronized (Native.libraries) {
            if (Native.options.containsKey(mappingClass)) {
                final Map libraryOptions = Native.options.get(mappingClass);
                Native.options.put(type, libraryOptions);
                return libraryOptions;
            }
            Map libraryOptions = null;
            try {
                final Field field = mappingClass.getField("OPTIONS");
                field.setAccessible(true);
                libraryOptions = (Map)field.get(null);
            }
            catch (NoSuchFieldException e2) {
                libraryOptions = Collections.EMPTY_MAP;
            }
            catch (Exception e) {
                throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + mappingClass);
            }
            libraryOptions = new HashMap(libraryOptions);
            if (!libraryOptions.containsKey("type-mapper")) {
                libraryOptions.put("type-mapper", lookupField(mappingClass, "TYPE_MAPPER", TypeMapper.class));
            }
            if (!libraryOptions.containsKey("structure-alignment")) {
                libraryOptions.put("structure-alignment", lookupField(mappingClass, "STRUCTURE_ALIGNMENT", Integer.class));
            }
            if (!libraryOptions.containsKey("string-encoding")) {
                libraryOptions.put("string-encoding", lookupField(mappingClass, "STRING_ENCODING", String.class));
            }
            Native.options.put(mappingClass, libraryOptions);
            if (type != mappingClass) {
                Native.options.put(type, libraryOptions);
            }
            return libraryOptions;
        }
    }
    
    private static Object lookupField(final Class mappingClass, final String fieldName, final Class resultClass) {
        try {
            final Field field = mappingClass.getField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        }
        catch (NoSuchFieldException e2) {
            return null;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " must be a public field of type " + resultClass.getName() + " (" + e + "): " + mappingClass);
        }
    }
    
    public static TypeMapper getTypeMapper(final Class cls) {
        return getLibraryOptions(cls).get("type-mapper");
    }
    
    public static String getStringEncoding(final Class cls) {
        final String encoding = getLibraryOptions(cls).get("string-encoding");
        return (encoding != null) ? encoding : getDefaultStringEncoding();
    }
    
    public static String getDefaultStringEncoding() {
        return System.getProperty("jna.encoding", "utf8");
    }
    
    public static int getStructureAlignment(final Class cls) {
        final Integer alignment = getLibraryOptions(cls).get("structure-alignment");
        return (alignment == null) ? 0 : alignment;
    }
    
    static byte[] getBytes(final String s) {
        return getBytes(s, getDefaultStringEncoding());
    }
    
    static byte[] getBytes(final String s, final String encoding) {
        if (encoding != null) {
            try {
                return s.getBytes(encoding);
            }
            catch (UnsupportedEncodingException e) {
                System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
            }
        }
        System.err.println("JNA Warning: Encoding with fallback " + System.getProperty("file.encoding"));
        return s.getBytes();
    }
    
    public static byte[] toByteArray(final String s) {
        return toByteArray(s, getDefaultStringEncoding());
    }
    
    public static byte[] toByteArray(final String s, final String encoding) {
        final byte[] bytes = getBytes(s, encoding);
        final byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }
    
    public static char[] toCharArray(final String s) {
        final char[] chars = s.toCharArray();
        final char[] buf = new char[chars.length + 1];
        System.arraycopy(chars, 0, buf, 0, chars.length);
        return buf;
    }
    
    private static void loadNativeDispatchLibrary() {
        if (!Boolean.getBoolean("jna.nounpack")) {
            try {
                removeTemporaryFiles();
            }
            catch (IOException e) {
                System.err.println("JNA Warning: IOException removing temporary files: " + e.getMessage());
            }
        }
        final String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
        final String bootPath = System.getProperty("jna.boot.library.path");
        if (bootPath != null) {
            final StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
            while (dirs.hasMoreTokens()) {
                final String dir = dirs.nextToken();
                final File file = new File(new File(dir), System.mapLibraryName(libName).replace(".dylib", ".jnilib"));
                String path = file.getAbsolutePath();
                if (Native.DEBUG_JNA_LOAD) {
                    System.out.println("Looking in " + path);
                }
                if (file.exists()) {
                    try {
                        if (Native.DEBUG_JNA_LOAD) {
                            System.out.println("Trying " + path);
                        }
                        System.setProperty("jnidispatch.path", path);
                        System.load(path);
                        Native.jnidispatchPath = path;
                        if (Native.DEBUG_JNA_LOAD) {
                            System.out.println("Found jnidispatch at " + path);
                        }
                        return;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                }
                if (Platform.isMac()) {
                    String orig;
                    String ext;
                    if (path.endsWith("dylib")) {
                        orig = "dylib";
                        ext = "jnilib";
                    }
                    else {
                        orig = "jnilib";
                        ext = "dylib";
                    }
                    path = path.substring(0, path.lastIndexOf(orig)) + ext;
                    if (Native.DEBUG_JNA_LOAD) {
                        System.out.println("Looking in " + path);
                    }
                    if (!new File(path).exists()) {
                        continue;
                    }
                    try {
                        if (Native.DEBUG_JNA_LOAD) {
                            System.out.println("Trying " + path);
                        }
                        System.setProperty("jnidispatch.path", path);
                        System.load(path);
                        Native.jnidispatchPath = path;
                        if (Native.DEBUG_JNA_LOAD) {
                            System.out.println("Found jnidispatch at " + path);
                        }
                        return;
                    }
                    catch (UnsatisfiedLinkError ex) {
                        System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
                    }
                }
            }
        }
        if (!Boolean.getBoolean("jna.nosys")) {
            try {
                if (Native.DEBUG_JNA_LOAD) {
                    System.out.println("Trying (via loadLibrary) " + libName);
                }
                System.loadLibrary(libName);
                if (Native.DEBUG_JNA_LOAD) {
                    System.out.println("Found jnidispatch on system path");
                }
                return;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError2) {}
        }
        if (!Boolean.getBoolean("jna.noclasspath")) {
            loadNativeDispatchLibraryFromClasspath();
            return;
        }
        throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
    }
    
    private static void loadNativeDispatchLibraryFromClasspath() {
        try {
            final String libName = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
            final File lib = extractFromResourcePath(libName, Native.class.getClassLoader());
            if (lib == null && lib == null) {
                throw new UnsatisfiedLinkError("Could not find JNA native support");
            }
            if (Native.DEBUG_JNA_LOAD) {
                System.out.println("Trying " + lib.getAbsolutePath());
            }
            System.setProperty("jnidispatch.path", lib.getAbsolutePath());
            System.load(lib.getAbsolutePath());
            Native.jnidispatchPath = lib.getAbsolutePath();
            if (Native.DEBUG_JNA_LOAD) {
                System.out.println("Found jnidispatch at " + Native.jnidispatchPath);
            }
            if (isUnpacked(lib) && !Boolean.getBoolean("jnidispatch.preserve")) {
                deleteLibrary(lib);
            }
        }
        catch (IOException e) {
            throw new UnsatisfiedLinkError(e.getMessage());
        }
    }
    
    static boolean isUnpacked(final File file) {
        return file.getName().startsWith("jna");
    }
    
    public static File extractFromResourcePath(final String name) throws IOException {
        return extractFromResourcePath(name, null);
    }
    
    public static File extractFromResourcePath(final String name, ClassLoader loader) throws IOException {
        final boolean DEBUG = Native.DEBUG_LOAD || (Native.DEBUG_JNA_LOAD && name.indexOf("jnidispatch") != -1);
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = Native.class.getClassLoader();
            }
        }
        if (DEBUG) {
            System.out.println("Looking in classpath from " + loader + " for " + name);
        }
        final String libname = name.startsWith("/") ? name : NativeLibrary.mapSharedLibraryName(name);
        String resourcePath = name.startsWith("/") ? name : (Platform.RESOURCE_PREFIX + "/" + libname);
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }
        URL url = loader.getResource(resourcePath);
        if (url == null && resourcePath.startsWith(Platform.RESOURCE_PREFIX)) {
            url = loader.getResource(libname);
        }
        if (url == null) {
            String path = System.getProperty("java.class.path");
            if (loader instanceof URLClassLoader) {
                path = Arrays.asList(((URLClassLoader)loader).getURLs()).toString();
            }
            throw new IOException("Native library (" + resourcePath + ") not found in resource path (" + path + ")");
        }
        if (DEBUG) {
            System.out.println("Found library resource at " + url);
        }
        File lib = null;
        if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                lib = new File(new URI(url.toString()));
            }
            catch (URISyntaxException e2) {
                lib = new File(url.getPath());
            }
            if (DEBUG) {
                System.out.println("Looking in " + lib.getAbsolutePath());
            }
            if (!lib.exists()) {
                throw new IOException("File URL " + url + " could not be properly decoded");
            }
        }
        else if (!Boolean.getBoolean("jna.nounpack")) {
            final InputStream is = loader.getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IOException("Can't obtain InputStream for " + resourcePath);
            }
            FileOutputStream fos = null;
            try {
                final File dir = getTempDir();
                lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
                if (!Boolean.getBoolean("jnidispatch.preserve")) {
                    lib.deleteOnExit();
                }
                fos = new FileOutputStream(lib);
                final byte[] buf = new byte[1024];
                int count;
                while ((count = is.read(buf, 0, buf.length)) > 0) {
                    fos.write(buf, 0, count);
                }
            }
            catch (IOException e) {
                throw new IOException("Failed to create temporary file for " + name + " library: " + e.getMessage());
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException ex) {}
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException ex2) {}
                }
            }
        }
        return lib;
    }
    
    private static native int sizeof(final int p0);
    
    private static native String getNativeVersion();
    
    private static native String getAPIChecksum();
    
    public static native int getLastError();
    
    public static native void setLastError(final int p0);
    
    public static Library synchronizedLibrary(final Library library) {
        final Class cls = library.getClass();
        if (!Proxy.isProxyClass(cls)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        final InvocationHandler ih = Proxy.getInvocationHandler(library);
        if (!(ih instanceof Library.Handler)) {
            throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
        }
        final Library.Handler handler = (Library.Handler)ih;
        final InvocationHandler newHandler = new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                synchronized (handler.getNativeLibrary()) {
                    return handler.invoke(library, method, args);
                }
            }
        };
        return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
    }
    
    public static String getWebStartLibraryPath(final String libName) {
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            final ClassLoader cl = Native.class.getClassLoader();
            final Method m = AccessController.doPrivileged((PrivilegedAction<Method>)new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        final Method m = ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
                        m.setAccessible(true);
                        return m;
                    }
                    catch (Exception e) {
                        return null;
                    }
                }
            });
            final String libpath = (String)m.invoke(cl, libName);
            if (libpath != null) {
                return new File(libpath).getParent();
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    static void markTemporaryFile(final File file) {
        try {
            final File marker = new File(file.getParentFile(), file.getName() + ".x");
            marker.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static File getTempDir() throws IOException {
        final String prop = System.getProperty("jna.tmpdir");
        File jnatmp;
        if (prop != null) {
            jnatmp = new File(prop);
            jnatmp.mkdirs();
        }
        else {
            final File tmp = new File(System.getProperty("java.io.tmpdir"));
            jnatmp = new File(tmp, "jna-" + System.getProperty("user.name").hashCode());
            jnatmp.mkdirs();
            if (!jnatmp.exists() || !jnatmp.canWrite()) {
                jnatmp = tmp;
            }
        }
        if (!jnatmp.exists()) {
            throw new IOException("JNA temporary directory '" + jnatmp + "' does not exist");
        }
        if (!jnatmp.canWrite()) {
            throw new IOException("JNA temporary directory '" + jnatmp + "' is not writable");
        }
        return jnatmp;
    }
    
    static void removeTemporaryFiles() throws IOException {
        final File dir = getTempDir();
        final FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".x") && name.startsWith("jna");
            }
        };
        final File[] files = dir.listFiles(filter);
        for (int i = 0; files != null && i < files.length; ++i) {
            final File marker = files[i];
            String name = marker.getName();
            name = name.substring(0, name.length() - 2);
            final File target = new File(marker.getParentFile(), name);
            if (!target.exists() || target.delete()) {
                marker.delete();
            }
        }
    }
    
    public static int getNativeSize(final Class type, final Object value) {
        if (type.isArray()) {
            final int len = Array.getLength(value);
            if (len > 0) {
                final Object o = Array.get(value, 0);
                return len * getNativeSize(type.getComponentType(), o);
            }
            throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
        }
        else {
            if (Structure.class.isAssignableFrom(type) && !Structure.ByReference.class.isAssignableFrom(type)) {
                return Structure.size(type, (Structure)value);
            }
            try {
                return getNativeSize(type);
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e.getMessage());
            }
        }
    }
    
    public static int getNativeSize(Class cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return 4;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return 1;
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return 2;
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return Native.WCHAR_SIZE;
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return 4;
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return 8;
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return 4;
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return 8;
        }
        if (Structure.class.isAssignableFrom(cls)) {
            if (Structure.ByValue.class.isAssignableFrom(cls)) {
                return Structure.size(cls);
            }
            return Native.POINTER_SIZE;
        }
        else {
            if (Pointer.class.isAssignableFrom(cls) || (Platform.HAS_BUFFERS && Buffers.isBuffer(cls)) || Callback.class.isAssignableFrom(cls) || String.class == cls || WString.class == cls) {
                return Native.POINTER_SIZE;
            }
            throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
        }
    }
    
    public static boolean isSupportedNativeType(final Class cls) {
        if (Structure.class.isAssignableFrom(cls)) {
            return true;
        }
        try {
            return getNativeSize(cls) != 0;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static void setCallbackExceptionHandler(final Callback.UncaughtExceptionHandler eh) {
        Native.callbackExceptionHandler = ((eh == null) ? Native.DEFAULT_HANDLER : eh);
    }
    
    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        return Native.callbackExceptionHandler;
    }
    
    public static void register(final String libName) {
        register(findDirectMappedClass(getCallingClass()), libName);
    }
    
    public static void register(final NativeLibrary lib) {
        register(findDirectMappedClass(getCallingClass()), lib);
    }
    
    static Class findDirectMappedClass(final Class cls) {
        final Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if ((methods[i].getModifiers() & 0x100) != 0x0) {
                return cls;
            }
        }
        final int idx = cls.getName().lastIndexOf("$");
        if (idx != -1) {
            final String name = cls.getName().substring(0, idx);
            try {
                return findDirectMappedClass(Class.forName(name, true, cls.getClassLoader()));
            }
            catch (ClassNotFoundException ex) {}
        }
        throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
    }
    
    static Class getCallingClass() {
        final Class[] context = new SecurityManager() {
            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (context == null) {
            throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
        }
        if (context.length < 4) {
            throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        return context[3];
    }
    
    public static void setCallbackThreadInitializer(final Callback cb, final CallbackThreadInitializer initializer) {
        CallbackReference.setCallbackThreadInitializer(cb, initializer);
    }
    
    private static void unregisterAll() {
        synchronized (Native.registeredClasses) {
            final Iterator i = Native.registeredClasses.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry e = i.next();
                unregister(e.getKey(), e.getValue());
                i.remove();
            }
        }
    }
    
    public static void unregister() {
        unregister(findDirectMappedClass(getCallingClass()));
    }
    
    public static void unregister(final Class cls) {
        synchronized (Native.registeredClasses) {
            if (Native.registeredClasses.containsKey(cls)) {
                unregister(cls, Native.registeredClasses.get(cls));
                Native.registeredClasses.remove(cls);
                Native.registeredLibraries.remove(cls);
            }
        }
    }
    
    public static boolean registered(final Class cls) {
        synchronized (Native.registeredClasses) {
            return Native.registeredClasses.containsKey(cls);
        }
    }
    
    private static native void unregister(final Class p0, final long[] p1);
    
    private static String getSignature(final Class cls) {
        if (cls.isArray()) {
            return "[" + getSignature(cls.getComponentType());
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return "V";
            }
            if (cls == Boolean.TYPE) {
                return "Z";
            }
            if (cls == Byte.TYPE) {
                return "B";
            }
            if (cls == Short.TYPE) {
                return "S";
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return "F";
            }
            if (cls == Double.TYPE) {
                return "D";
            }
        }
        return "L" + replace(".", "/", cls.getName()) + ";";
    }
    
    static String replace(final String s1, final String s2, String str) {
        final StringBuilder buf = new StringBuilder();
        while (true) {
            final int idx = str.indexOf(s1);
            if (idx == -1) {
                break;
            }
            buf.append(str.substring(0, idx));
            buf.append(s2);
            str = str.substring(idx + s1.length());
        }
        buf.append(str);
        return buf.toString();
    }
    
    private static int getConversion(Class type, final TypeMapper mapper) {
        if (type == Boolean.class) {
            type = Boolean.TYPE;
        }
        else if (type == Byte.class) {
            type = Byte.TYPE;
        }
        else if (type == Short.class) {
            type = Short.TYPE;
        }
        else if (type == Character.class) {
            type = Character.TYPE;
        }
        else if (type == Integer.class) {
            type = Integer.TYPE;
        }
        else if (type == Long.class) {
            type = Long.TYPE;
        }
        else if (type == Float.class) {
            type = Float.TYPE;
        }
        else if (type == Double.class) {
            type = Double.TYPE;
        }
        else if (type == Void.class) {
            type = Void.TYPE;
        }
        if (mapper != null) {
            final FromNativeConverter fromNative = mapper.getFromNativeConverter((Class)type);
            final ToNativeConverter toNative = mapper.getToNativeConverter((Class)type);
            if (fromNative != null) {
                final Class nativeType = fromNative.nativeType();
                if (nativeType == String.class) {
                    return 24;
                }
                if (nativeType == WString.class) {
                    return 25;
                }
                return 23;
            }
            else if (toNative != null) {
                final Class nativeType = toNative.nativeType();
                if (nativeType == String.class) {
                    return 24;
                }
                if (nativeType == WString.class) {
                    return 25;
                }
                return 23;
            }
        }
        if (Pointer.class.isAssignableFrom((Class<?>)type)) {
            return 1;
        }
        if (String.class == type) {
            return 2;
        }
        if (WString.class.isAssignableFrom((Class<?>)type)) {
            return 20;
        }
        if (Platform.HAS_BUFFERS && Buffers.isBuffer((Class)type)) {
            return 5;
        }
        if (Structure.class.isAssignableFrom((Class<?>)type)) {
            if (Structure.ByValue.class.isAssignableFrom((Class<?>)type)) {
                return 4;
            }
            return 3;
        }
        else {
            if (((Class)type).isArray()) {
                switch (((Class)type).getName().charAt(1)) {
                    case 'Z': {
                        return 13;
                    }
                    case 'B': {
                        return 6;
                    }
                    case 'S': {
                        return 7;
                    }
                    case 'C': {
                        return 8;
                    }
                    case 'I': {
                        return 9;
                    }
                    case 'J': {
                        return 10;
                    }
                    case 'F': {
                        return 11;
                    }
                    case 'D': {
                        return 12;
                    }
                }
            }
            if (((Class)type).isPrimitive()) {
                return (type == Boolean.TYPE) ? 14 : 0;
            }
            if (Callback.class.isAssignableFrom((Class<?>)type)) {
                return 15;
            }
            if (IntegerType.class.isAssignableFrom((Class<?>)type)) {
                return 21;
            }
            if (PointerType.class.isAssignableFrom((Class<?>)type)) {
                return 22;
            }
            if (!NativeMapped.class.isAssignableFrom((Class<?>)type)) {
                return -1;
            }
            final Class nativeType2 = NativeMappedConverter.getInstance((Class)type).nativeType();
            if (nativeType2 == String.class) {
                return 18;
            }
            if (nativeType2 == WString.class) {
                return 19;
            }
            return 17;
        }
    }
    
    public static void register(final Class cls, final String libName) {
        final Map options = new HashMap();
        options.put("classloader", cls.getClassLoader());
        register(cls, NativeLibrary.getInstance(libName, options));
    }
    
    public static void register(final Class cls, final NativeLibrary lib) {
        final Method[] methods = cls.getDeclaredMethods();
        final List mlist = new ArrayList();
        final TypeMapper mapper = lib.getOptions().get("type-mapper");
        cacheOptions(cls, lib.getOptions(), null);
        for (int i = 0; i < methods.length; ++i) {
            if ((methods[i].getModifiers() & 0x100) != 0x0) {
                mlist.add(methods[i]);
            }
        }
        final long[] handles = new long[mlist.size()];
        for (int j = 0; j < handles.length; ++j) {
            final Method method = mlist.get(j);
            String sig = "(";
            final Class rclass = method.getReturnType();
            final Class[] ptypes = method.getParameterTypes();
            final long[] atypes = new long[ptypes.length];
            final long[] closure_atypes = new long[ptypes.length];
            final int[] cvt = new int[ptypes.length];
            final ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
            FromNativeConverter fromNative = null;
            final int rcvt = getConversion(rclass, mapper);
            boolean throwLastError = false;
            long closure_rtype = 0L;
            long rtype = 0L;
            switch (rcvt) {
                case -1: {
                    throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
                }
                case 23:
                case 24:
                case 25: {
                    fromNative = mapper.getFromNativeConverter(rclass);
                    closure_rtype = Structure.FFIType.get(rclass.isPrimitive() ? rclass : Pointer.class).peer;
                    rtype = Structure.FFIType.get(fromNative.nativeType()).peer;
                    break;
                }
                case 17:
                case 18:
                case 19:
                case 21:
                case 22: {
                    closure_rtype = Structure.FFIType.get(Pointer.class).peer;
                    rtype = Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType()).peer;
                    break;
                }
                case 3: {
                    rtype = (closure_rtype = Structure.FFIType.get(Pointer.class).peer);
                    break;
                }
                case 4: {
                    closure_rtype = Structure.FFIType.get(Pointer.class).peer;
                    rtype = Structure.FFIType.get(rclass).peer;
                    break;
                }
                default: {
                    rtype = (closure_rtype = Structure.FFIType.get(rclass).peer);
                    break;
                }
            }
            for (int t = 0; t < ptypes.length; ++t) {
                Class type = ptypes[t];
                sig += getSignature(type);
                cvt[t] = getConversion(type, mapper);
                if (cvt[t] == -1) {
                    throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
                }
                if (cvt[t] == 17 || cvt[t] == 18 || cvt[t] == 19 || cvt[t] == 21) {
                    type = NativeMappedConverter.getInstance(type).nativeType();
                }
                else if (cvt[t] == 23 || cvt[t] == 24 || cvt[t] == 25) {
                    toNative[t] = mapper.getToNativeConverter(type);
                }
                switch (cvt[t]) {
                    case 4:
                    case 17:
                    case 18:
                    case 19:
                    case 21:
                    case 22: {
                        atypes[t] = Structure.FFIType.get(type).peer;
                        closure_atypes[t] = Structure.FFIType.get(Pointer.class).peer;
                        break;
                    }
                    case 23:
                    case 24:
                    case 25: {
                        closure_atypes[t] = Structure.FFIType.get(type.isPrimitive() ? type : Pointer.class).peer;
                        atypes[t] = Structure.FFIType.get(toNative[t].nativeType()).peer;
                        break;
                    }
                    case 0: {
                        closure_atypes[t] = (atypes[t] = Structure.FFIType.get(type).peer);
                        break;
                    }
                    default: {
                        closure_atypes[t] = (atypes[t] = Structure.FFIType.get(Pointer.class).peer);
                        break;
                    }
                }
            }
            sig += ")";
            sig += getSignature(rclass);
            final Class[] etypes = method.getExceptionTypes();
            for (int e = 0; e < etypes.length; ++e) {
                if (LastErrorException.class.isAssignableFrom(etypes[e])) {
                    throwLastError = true;
                    break;
                }
            }
            final Function f = lib.getFunction(method.getName(), method);
            try {
                handles[j] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f.peer, f.getCallingConvention(), throwLastError, toNative, fromNative, f.encoding);
            }
            catch (NoSuchMethodError e2) {
                throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
            }
        }
        synchronized (Native.registeredClasses) {
            Native.registeredClasses.put(cls, handles);
            Native.registeredLibraries.put(cls, lib);
        }
    }
    
    private static void cacheOptions(final Class cls, Map libOptions, final Object proxy) {
        libOptions = new HashMap(libOptions);
        synchronized (Native.libraries) {
            Native.options.put(cls, libOptions);
            if (proxy != null) {
                Native.libraries.put(cls, new WeakReference<Object>(proxy));
            }
            if (!cls.isInterface() && Library.class.isAssignableFrom(cls)) {
                final Class[] ifaces = cls.getInterfaces();
                for (int i = 0; i < ifaces.length; ++i) {
                    if (Library.class.isAssignableFrom(ifaces[i])) {
                        cacheOptions(ifaces[i], libOptions, proxy);
                        break;
                    }
                }
            }
        }
    }
    
    private static native long registerMethod(final Class p0, final String p1, final String p2, final int[] p3, final long[] p4, final long[] p5, final int p6, final long p7, final long p8, final Class p9, final long p10, final int p11, final boolean p12, final ToNativeConverter[] p13, final FromNativeConverter p14, final String p15);
    
    private static NativeMapped fromNative(final Class cls, final Object value) {
        return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
    }
    
    private static Class nativeType(final Class cls) {
        return NativeMappedConverter.getInstance(cls).nativeType();
    }
    
    private static Object toNative(final ToNativeConverter cvt, final Object o) {
        return cvt.toNative(o, new ToNativeContext());
    }
    
    private static Object fromNative(final FromNativeConverter cvt, final Object o, final Class cls) {
        return cvt.fromNative(o, new FromNativeContext(cls));
    }
    
    public static native long ffi_prep_cif(final int p0, final int p1, final long p2, final long p3);
    
    public static native void ffi_call(final long p0, final long p1, final long p2, final long p3);
    
    public static native long ffi_prep_closure(final long p0, final ffi_callback p1);
    
    public static native void ffi_free_closure(final long p0);
    
    static native int initialize_ffi_type(final long p0);
    
    public static void main(final String[] args) {
        final String DEFAULT_TITLE = "Java Native Access (JNA)";
        final String DEFAULT_VERSION = "4.2.2";
        final String DEFAULT_BUILD = "4.2.2 (package information missing)";
        final Package pkg = Native.class.getPackage();
        String title = (pkg != null) ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
        if (title == null) {
            title = "Java Native Access (JNA)";
        }
        String version = (pkg != null) ? pkg.getSpecificationVersion() : "4.2.2";
        if (version == null) {
            version = "4.2.2";
        }
        title = title + " API Version " + version;
        System.out.println(title);
        version = ((pkg != null) ? pkg.getImplementationVersion() : "4.2.2 (package information missing)");
        if (version == null) {
            version = "4.2.2 (package information missing)";
        }
        System.out.println("Version: " + version);
        System.out.println(" Native: " + getNativeVersion() + " (" + getAPIChecksum() + ")");
        System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
    }
    
    static synchronized native void freeNativeCallback(final long p0);
    
    static synchronized native long createNativeCallback(final Callback p0, final Method p1, final Class[] p2, final Class p3, final int p4, final int p5, final String p6);
    
    static native int invokeInt(final long p0, final int p1, final Object[] p2);
    
    static native long invokeLong(final long p0, final int p1, final Object[] p2);
    
    static native void invokeVoid(final long p0, final int p1, final Object[] p2);
    
    static native float invokeFloat(final long p0, final int p1, final Object[] p2);
    
    static native double invokeDouble(final long p0, final int p1, final Object[] p2);
    
    static native long invokePointer(final long p0, final int p1, final Object[] p2);
    
    private static native void invokeStructure(final long p0, final int p1, final Object[] p2, final long p3, final long p4);
    
    static Structure invokeStructure(final long fp, final int callFlags, final Object[] args, final Structure s) {
        invokeStructure(fp, callFlags, args, s.getPointer().peer, s.getTypeInfo().peer);
        return s;
    }
    
    static native Object invokeObject(final long p0, final int p1, final Object[] p2);
    
    static long open(final String name) {
        return open(name, -1);
    }
    
    static native long open(final String p0, final int p1);
    
    static native void close(final long p0);
    
    static native long findSymbol(final long p0, final String p1);
    
    static native long indexOf(final long p0, final byte p1);
    
    static native void read(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final short[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final char[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final int[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final long[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final float[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final double[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final short[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final char[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final int[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final long[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final float[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final double[] p1, final int p2, final int p3);
    
    static native byte getByte(final long p0);
    
    static native char getChar(final long p0);
    
    static native short getShort(final long p0);
    
    static native int getInt(final long p0);
    
    static native long getLong(final long p0);
    
    static native float getFloat(final long p0);
    
    static native double getDouble(final long p0);
    
    static Pointer getPointer(final long addr) {
        final long peer = _getPointer(addr);
        return (peer == 0L) ? null : new Pointer(peer);
    }
    
    private static native long _getPointer(final long p0);
    
    static native String getWideString(final long p0);
    
    static String getString(final long addr) {
        return getString(addr, getDefaultStringEncoding());
    }
    
    static String getString(final long addr, final String encoding) {
        final byte[] data = getStringBytes(addr);
        if (encoding != null) {
            try {
                return new String(data, encoding);
            }
            catch (UnsupportedEncodingException ex) {}
        }
        return new String(data);
    }
    
    static native byte[] getStringBytes(final long p0);
    
    static native void setMemory(final long p0, final long p1, final byte p2);
    
    static native void setByte(final long p0, final byte p1);
    
    static native void setShort(final long p0, final short p1);
    
    static native void setChar(final long p0, final char p1);
    
    static native void setInt(final long p0, final int p1);
    
    static native void setLong(final long p0, final long p1);
    
    static native void setFloat(final long p0, final float p1);
    
    static native void setDouble(final long p0, final double p1);
    
    static native void setPointer(final long p0, final long p1);
    
    static native void setWideString(final long p0, final String p1);
    
    public static native long malloc(final long p0);
    
    public static native void free(final long p0);
    
    public static native ByteBuffer getDirectByteBuffer(final long p0, final long p1);
    
    public static void detach(final boolean detach) {
        final Thread thread = Thread.currentThread();
        if (detach) {
            Native.nativeThreads.remove(thread);
            final Pointer p = Native.nativeThreadTerminationFlag.get();
            setDetachState(true, 0L);
        }
        else if (!Native.nativeThreads.containsKey(thread)) {
            final Pointer p = Native.nativeThreadTerminationFlag.get();
            Native.nativeThreads.put(thread, p);
            setDetachState(false, p.peer);
        }
    }
    
    static Pointer getTerminationFlag(final Thread t) {
        return Native.nativeThreads.get(t);
    }
    
    private static native void setDetachState(final boolean p0, final long p1);
    
    static {
        Native.DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
        Native.DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
        Native.jnidispatchPath = null;
        Native.options = new WeakHashMap();
        Native.libraries = new WeakHashMap();
        DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Callback c, final Throwable e) {
                System.err.println("JNA: Callback " + c + " threw the following exception:");
                e.printStackTrace();
            }
        };
        Native.callbackExceptionHandler = Native.DEFAULT_HANDLER;
        loadNativeDispatchLibrary();
        POINTER_SIZE = sizeof(0);
        LONG_SIZE = sizeof(1);
        WCHAR_SIZE = sizeof(2);
        SIZE_T_SIZE = sizeof(3);
        initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            setProtected(true);
        }
        final float version = parseVersion(getNativeVersion());
        if (version != parseVersion("4.0.1")) {
            final String LS = System.getProperty("line.separator");
            throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system" + LS + ((Native.jnidispatchPath != null) ? ("(at " + Native.jnidispatchPath + ")") : System.getProperty("java.library.path")) + "." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
        }
        MAX_ALIGNMENT = ((Platform.isSPARC() || Platform.isWindows() || (Platform.isLinux() && (Platform.isARM() || Platform.isPPC())) || Platform.isAIX() || Platform.isAndroid()) ? 8 : Native.LONG_SIZE);
        MAX_PADDING = ((Platform.isMac() && Platform.isPPC()) ? 8 : Native.MAX_ALIGNMENT);
        System.setProperty("jna.loaded", "true");
        finalizer = new Object() {
            @Override
            protected void finalize() {
                dispose();
            }
        };
        Native.registeredClasses = new WeakHashMap();
        Native.registeredLibraries = new WeakHashMap();
        Native.nativeThreads = Collections.synchronizedMap(new WeakHashMap<Object, Object>());
        Native.nativeThreadTerminationFlag = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                final Memory m = new Memory(4L);
                m.clear();
                return m;
            }
        };
    }
    
    private static class Buffers
    {
        static boolean isBuffer(final Class cls) {
            return Buffer.class.isAssignableFrom(cls);
        }
    }
    
    private static class AWT
    {
        static long getWindowID(final Window w) throws HeadlessException {
            return getComponentID(w);
        }
        
        static long getComponentID(final Object o) throws HeadlessException {
            if (GraphicsEnvironment.isHeadless()) {
                throw new HeadlessException("No native windows when headless");
            }
            final Component c = (Component)o;
            if (c.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight");
            }
            if (!c.isDisplayable()) {
                throw new IllegalStateException("Component must be displayable");
            }
            if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && !c.isVisible()) {
                throw new IllegalStateException("Component must be visible");
            }
            return Native.getWindowHandle0(c);
        }
    }
    
    public interface ffi_callback
    {
        void invoke(final long p0, final long p1, final long p2);
    }
}
