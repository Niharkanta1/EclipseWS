// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system;

import java.util.Locale;
import java.util.zip.Checksum;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.io.FileNotFoundException;
import java.io.File;

public class LibraryLoader
{
    private static final String LIBRARY_NAME = "systemhook";
    private static boolean libraryLoad;
    
    public static synchronized void loadLibrary() throws UnsatisfiedLinkError {
        if (LibraryLoader.libraryLoad) {
            return;
        }
        String libName = System.getProperty("systemhook.lib.name", System.mapLibraryName("systemhook").replaceAll("\\.jnilib$", "\\.dylib"));
        final String libPath = System.getProperty("systemhook.lib.path");
        try {
            if (libPath == null) {
                System.loadLibrary(libName);
            }
            else {
                System.load(new File(libPath, libName).getAbsolutePath());
            }
            LibraryLoader.libraryLoad = true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            libName = System.mapLibraryName("systemhook-" + getOperatingSystemName() + '-' + getOperatingSystemArchitecture()).replaceAll("\\.jnilib$", "\\.dylib");
            final String libNameExtension = libName.substring(libName.lastIndexOf(46));
            final String libResourcePath = LibraryLoader.class.getPackage().getName().replace('.', '/') + "/lib/" + libName;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                if ((inputStream = LibraryLoader.class.getClassLoader().getResourceAsStream(libResourcePath)) == null) {
                    throw new FileNotFoundException("lib: " + libName + " not found in lib directory");
                }
                final File tempFile = File.createTempFile("systemhook-", libNameExtension);
                final Checksum checksum = new CRC32();
                outputStream = new FileOutputStream(tempFile);
                final byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                    checksum.update(buffer, 0, read);
                }
                outputStream.close();
                final File libFile = new File(tempFile.getParentFile(), "systemhook+" + checksum.getValue() + libNameExtension);
                if (!libFile.exists()) {
                    tempFile.renameTo(libFile);
                }
                else {
                    tempFile.delete();
                }
                System.load(libFile.getAbsolutePath());
                LibraryLoader.libraryLoad = true;
            }
            catch (IOException e) {
                throw new UnsatisfiedLinkError(e.getMessage());
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException ex) {}
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    }
                    catch (IOException ex2) {}
                }
            }
        }
    }
    
    private static String getOperatingSystemName() {
        final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.startsWith("windows")) {
            return "windows";
        }
        if (osName.startsWith("linux")) {
            return "linux";
        }
        if (osName.startsWith("mac os")) {
            return "darwin";
        }
        if (osName.startsWith("sunos") || osName.startsWith("solaris")) {
            return "solaris";
        }
        return osName;
    }
    
    private static String getOperatingSystemArchitecture() {
        final String osArch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        if ((osArch.startsWith("i") || osArch.startsWith("x")) && osArch.endsWith("86")) {
            return "x86";
        }
        if ((osArch.equals("i86") || osArch.startsWith("amd")) && osArch.endsWith("64")) {
            return "amd64";
        }
        if (osArch.startsWith("arm")) {
            return "arm";
        }
        if (osArch.startsWith("sparc")) {
            return osArch.endsWith("64") ? "sparc64" : "sparc";
        }
        if (osArch.startsWith("ppc")) {
            return osArch.endsWith("64") ? "ppc64" : "ppc";
        }
        return osArch;
    }
}
