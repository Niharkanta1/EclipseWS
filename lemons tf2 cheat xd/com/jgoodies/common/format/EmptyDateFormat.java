// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.format;

import com.jgoodies.common.base.Strings;
import java.text.ParsePosition;
import com.jgoodies.common.base.Objects;
import java.text.FieldPosition;
import java.text.DateFormat;
import java.util.Date;

public final class EmptyDateFormat extends AbstractWrappedDateFormat
{
    private final Date emptyValue;
    
    public EmptyDateFormat(final DateFormat delegate) {
        this(delegate, null);
    }
    
    public EmptyDateFormat(final DateFormat delegate, final Date emptyValue) {
        super(delegate);
        this.emptyValue = emptyValue;
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition pos) {
        return Objects.equals(date, this.emptyValue) ? toAppendTo : this.delegate.format(date, toAppendTo, pos);
    }
    
    @Override
    public Date parse(final String source, final ParsePosition pos) {
        if (Strings.isBlank(source)) {
            pos.setIndex(1);
            return this.emptyValue;
        }
        return this.delegate.parse(source, pos);
    }
}
