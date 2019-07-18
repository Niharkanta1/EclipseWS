// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.memory;

public class Offset
{
    public String name;
    private int address;
    
    public Offset(final String name, final int offset) {
        this.name = name;
        this.address = offset;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getAddress() {
        return this.address;
    }
    
    public void setAddress(final int address) {
        this.address = address;
    }
}
