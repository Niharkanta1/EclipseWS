// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinDef;
import java.util.Date;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.Guid;

public class COMLateBindingObject extends COMBindingBaseObject
{
    public COMLateBindingObject(final IDispatch iDispatch) {
        super(iDispatch);
    }
    
    public COMLateBindingObject(final Guid.CLSID clsid, final boolean useActiveInstance) {
        super(clsid, useActiveInstance);
    }
    
    public COMLateBindingObject(final String progId, final boolean useActiveInstance) throws COMException {
        super(progId, useActiveInstance);
    }
    
    protected IDispatch getAutomationProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return (IDispatch)result.getValue();
    }
    
    protected IDispatch getAutomationProperty(final String propertyName, final COMLateBindingObject comObject) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, comObject.getIDispatch(), propertyName);
        return (IDispatch)result.getValue();
    }
    
    protected IDispatch getAutomationProperty(final String propertyName, final COMLateBindingObject comObject, final Variant.VARIANT value) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, comObject.getIDispatch(), propertyName, value);
        return (IDispatch)result.getValue();
    }
    
    protected IDispatch getAutomationProperty(final String propertyName, final IDispatch iDispatch) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return (IDispatch)result.getValue();
    }
    
    protected boolean getBooleanProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return ((OaIdl.VARIANT_BOOL)result.getValue()).intValue() != 0;
    }
    
    protected Date getDateProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return result.dateValue();
    }
    
    protected int getIntProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return ((WinDef.LONG)result.getValue()).intValue();
    }
    
    protected short getShortProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return ((WinDef.SHORT)result.getValue()).shortValue();
    }
    
    protected String getStringProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, this.getIDispatch(), propertyName);
        return result.getValue().toString();
    }
    
    protected Variant.VARIANT invoke(final String methodName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName, arg);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT[] args) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName, args);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3, final Variant.VARIANT arg4) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
    }
    
    protected void invokeNoReply(final String methodName, final IDispatch dispatch) {
        this.oleMethod(1, null, dispatch, methodName);
    }
    
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName);
    }
    
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT arg) {
        this.oleMethod(1, null, dispatch, methodName, arg);
    }
    
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.oleMethod(1, null, dispatch, methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject, final Variant.VARIANT arg) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName, arg);
    }
    
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT[] args) {
        this.oleMethod(1, null, dispatch, methodName, args);
    }
    
    protected void invokeNoReply(final String methodName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName, arg);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT[] args) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, this.getIDispatch(), methodName, args);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3, final Variant.VARIANT arg4) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
    }
    
    protected void setProperty(final String propertyName, final boolean value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final Date value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final IDispatch value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final int value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final short value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final String value) {
        this.oleMethod(4, null, this.getIDispatch(), propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final IDispatch iDispatch, final Variant.VARIANT value) {
        this.oleMethod(4, null, iDispatch, propertyName, value);
    }
    
    protected void setProperty(final String propertyName, final COMLateBindingObject comObject, final Variant.VARIANT value) {
        this.oleMethod(4, null, comObject.getIDispatch(), propertyName, value);
    }
    
    public Variant.VARIANT toVariant() {
        return new Variant.VARIANT(this.getIDispatch());
    }
}
