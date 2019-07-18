// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.List;
import java.awt.Container;

public interface Size
{
    int maximumSize(final Container p0, final List p1, final FormLayout.Measure p2, final FormLayout.Measure p3, final FormLayout.Measure p4);
    
    boolean compressible();
    
    String encode();
}
