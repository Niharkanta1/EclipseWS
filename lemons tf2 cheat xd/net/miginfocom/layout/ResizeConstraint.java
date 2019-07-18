// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamException;
import java.io.Externalizable;

final class ResizeConstraint implements Externalizable
{
    static final Float WEIGHT_100;
    Float grow;
    int growPrio;
    Float shrink;
    int shrinkPrio;
    
    public ResizeConstraint() {
        this.grow = null;
        this.growPrio = 100;
        this.shrink = ResizeConstraint.WEIGHT_100;
        this.shrinkPrio = 100;
    }
    
    ResizeConstraint(final int shrinkPrio, final Float shrink, final int growPrio, final Float grow) {
        this.grow = null;
        this.growPrio = 100;
        this.shrink = ResizeConstraint.WEIGHT_100;
        this.shrinkPrio = 100;
        this.shrinkPrio = shrinkPrio;
        this.shrink = shrink;
        this.growPrio = growPrio;
        this.grow = grow;
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == ResizeConstraint.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
    
    static {
        WEIGHT_100 = new Float(100.0f);
    }
}
