// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.format;

import java.text.ParsePosition;
import com.jgoodies.common.display.Displayable;
import java.text.FieldPosition;
import java.text.Format;

public final class DisplayableFormat extends Format
{
    public static final DisplayableFormat INSTANCE;
    
    private DisplayableFormat() {
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (obj == null) {
            return toAppendTo;
        }
        if (!(obj instanceof Displayable)) {
            throw new ClassCastException("The object to format must implement the Displayable interface.");
        }
        toAppendTo.append(((Displayable)obj).getDisplayString());
        return toAppendTo;
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        throw new UnsupportedOperationException("The DisplayableFormat cannot parse.");
    }
    
    static {
        INSTANCE = new DisplayableFormat();
    }
}
