// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.memory;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Memory
{
    public List<Offset> offsets;
    
    public Memory() {
        this.offsets = new ArrayList<Offset>();
        this.addOffsets();
    }
    
    public void addOffsets() {
    }
    
    public Offset getOffset(final String name) {
        for (final Offset o : this.getOffsets()) {
            if (o.getName().equalsIgnoreCase(name)) {
                return o;
            }
        }
        return null;
    }
    
    public List<Offset> getOffsets() {
        return this.offsets;
    }
    
    public void setOffsets(final List<Offset> offsets) {
        this.offsets = offsets;
    }
}
