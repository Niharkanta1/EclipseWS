// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.utils;

public enum EnumBone
{
    HEAD("HEAD", 0, 6), 
    PELVIS("PELVIS", 1, 0);
    
    int bone;
    
    private EnumBone(final String s, final int n, final int boneid) {
        this.bone = boneid;
    }
    
    public int getBoneID() {
        return this.bone;
    }
}
