// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.base;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public final class Objects
{
    private Objects() {
    }
    
    public static <T extends Serializable> T deepCopy(final T original) {
        if (original == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            final ObjectOutputStream oas = new ObjectOutputStream(baos);
            oas.writeObject(original);
            oas.flush();
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final ObjectInputStream ois = new ObjectInputStream(bais);
            return (T)ois.readObject();
        }
        catch (Throwable e) {
            throw new RuntimeException("Deep copy failed", e);
        }
    }
    
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }
}
