// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.utils.vector;

public class Vector3f
{
    public float x;
    public float y;
    public float z;
    
    public Vector3f() {
    }
    
    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getZ() {
        return this.z;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setZ(final float z) {
        this.z = z;
    }
}
