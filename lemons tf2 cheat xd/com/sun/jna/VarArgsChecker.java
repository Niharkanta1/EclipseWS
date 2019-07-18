// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.Method;

abstract class VarArgsChecker
{
    private VarArgsChecker() {
    }
    
    static VarArgsChecker create() {
        try {
            final Method isVarArgsMethod = Method.class.getMethod("isVarArgs", (Class<?>[])new Class[0]);
            if (isVarArgsMethod != null) {
                return new RealVarArgsChecker();
            }
            return new NoVarArgsChecker();
        }
        catch (NoSuchMethodException e) {
            return new NoVarArgsChecker();
        }
        catch (SecurityException e2) {
            return new NoVarArgsChecker();
        }
    }
    
    abstract boolean isVarArgs(final Method p0);
    
    private static final class RealVarArgsChecker extends VarArgsChecker
    {
        private RealVarArgsChecker() {
            super(null);
        }
        
        @Override
        boolean isVarArgs(final Method m) {
            return m.isVarArgs();
        }
    }
    
    private static final class NoVarArgsChecker extends VarArgsChecker
    {
        private NoVarArgsChecker() {
            super(null);
        }
        
        @Override
        boolean isVarArgs(final Method m) {
            return false;
        }
    }
}
