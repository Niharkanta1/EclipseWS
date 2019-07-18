// 
// Decompiled by Procyon v0.5.36
// 

package lombok.delombok.ant;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.BuildException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import lombok.Lombok;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Reference;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.types.Path;
import java.io.File;
import org.apache.tools.ant.Task;

class Tasks
{
    public static class Format
    {
        private String value;
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final Format other = (Format)obj;
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
            }
            else if (!this.value.equals(other.value)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "FormatOption [value=" + this.value + "]";
        }
        
        public String getValue() {
            return this.value;
        }
        
        public void setValue(final String value) {
            this.value = value;
        }
    }
    
    public static class Delombok extends Task
    {
        private File fromDir;
        private File toDir;
        private Path classpath;
        private Path sourcepath;
        private boolean verbose;
        private String encoding;
        private Path path;
        private List<Format> formatOptions;
        private static ClassLoader shadowLoader;
        
        public Delombok() {
            this.formatOptions = new ArrayList<Format>();
        }
        
        public void setClasspath(final Path classpath) {
            if (this.classpath == null) {
                this.classpath = classpath;
            }
            else {
                this.classpath.append(classpath);
            }
        }
        
        public Path createClasspath() {
            if (this.classpath == null) {
                this.classpath = new Path(this.getProject());
            }
            return this.classpath.createPath();
        }
        
        public void setClasspathRef(final Reference r) {
            this.createClasspath().setRefid(r);
        }
        
        public void setSourcepath(final Path sourcepath) {
            if (this.sourcepath == null) {
                this.sourcepath = sourcepath;
            }
            else {
                this.sourcepath.append(sourcepath);
            }
        }
        
        public Path createSourcepath() {
            if (this.sourcepath == null) {
                this.sourcepath = new Path(this.getProject());
            }
            return this.sourcepath.createPath();
        }
        
        public void setSourcepathRef(final Reference r) {
            this.createSourcepath().setRefid(r);
        }
        
        public void setFrom(final File dir) {
            this.fromDir = dir;
        }
        
        public void setTo(final File dir) {
            this.toDir = dir;
        }
        
        public void setVerbose(final boolean verbose) {
            this.verbose = verbose;
        }
        
        public void setEncoding(final String encoding) {
            this.encoding = encoding;
        }
        
        public void addFileset(final FileSet set) {
            if (this.path == null) {
                this.path = new Path(this.getProject());
            }
            this.path.add((ResourceCollection)set);
        }
        
        public Format createFormat() {
            return new Format();
        }
        
        public void addFormat(final Format format) {
            this.formatOptions.add(format);
        }
        
        public static Class<?> shadowLoadClass(final String name) {
            try {
                if (Delombok.shadowLoader == null) {
                    try {
                        Class.forName("lombok.core.LombokNode");
                        Delombok.shadowLoader = Delombok.class.getClassLoader();
                    }
                    catch (ClassNotFoundException e2) {
                        final Class<?> launcherMain = Class.forName("lombok.launch.Main");
                        final Method m = launcherMain.getDeclaredMethod("createShadowClassLoader", (Class<?>[])new Class[0]);
                        m.setAccessible(true);
                        Delombok.shadowLoader = (ClassLoader)m.invoke(null, new Object[0]);
                    }
                }
                return Class.forName(name, true, Delombok.shadowLoader);
            }
            catch (Exception e) {
                throw Lombok.sneakyThrow(e);
            }
        }
        
        public void execute() throws BuildException {
            final Location loc = this.getLocation();
            try {
                final Object instance = shadowLoadClass("lombok.delombok.ant.DelombokTaskImpl").newInstance();
                for (final Field selfField : this.getClass().getDeclaredFields()) {
                    if (!selfField.isSynthetic()) {
                        if (!Modifier.isStatic(selfField.getModifiers())) {
                            final Field otherField = instance.getClass().getDeclaredField(selfField.getName());
                            otherField.setAccessible(true);
                            selfField.setAccessible(true);
                            if (selfField.getName().equals("formatOptions")) {
                                final List<String> rep = new ArrayList<String>();
                                for (final Format f : this.formatOptions) {
                                    if (f.getValue() == null) {
                                        throw new BuildException("'value' property required for <format>");
                                    }
                                    rep.add(f.getValue());
                                }
                                otherField.set(instance, rep);
                            }
                            else {
                                otherField.set(instance, selfField.get(this));
                            }
                        }
                    }
                }
                final Method m = instance.getClass().getMethod("execute", Location.class);
                m.setAccessible(true);
                m.invoke(instance, loc);
            }
            catch (InvocationTargetException e) {
                throw Lombok.sneakyThrow(e.getCause());
            }
            catch (Exception e2) {
                throw Lombok.sneakyThrow(e2);
            }
        }
    }
}
