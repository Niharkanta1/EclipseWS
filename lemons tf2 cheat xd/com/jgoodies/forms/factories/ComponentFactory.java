// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.factories;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Action;

public interface ComponentFactory
{
    JButton createButton(final Action p0);
    
    JLabel createLabel(final String p0);
    
    JLabel createReadOnlyLabel(final String p0);
    
    JLabel createTitle(final String p0);
    
    JLabel createHeaderLabel(final String p0);
    
    JComponent createSeparator(final String p0, final int p1);
}
