// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.format;

import java.text.ParsePosition;
import java.text.ParseException;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.base.Objects;
import java.text.FieldPosition;
import com.jgoodies.common.base.Preconditions;
import java.text.NumberFormat;

public final class EmptyNumberFormat extends NumberFormat
{
    private final NumberFormat delegate;
    private final Number emptyValue;
    
    public EmptyNumberFormat(final NumberFormat delegate) {
        this(delegate, null);
    }
    
    public EmptyNumberFormat(final NumberFormat delegate, final int emptyValue) {
        this(delegate, (Number)emptyValue);
    }
    
    public EmptyNumberFormat(final NumberFormat delegate, final Number emptyValue) {
        this.delegate = Preconditions.checkNotNull(delegate, "The %1$s must not be null.", "delegate format");
        this.emptyValue = emptyValue;
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        return Objects.equals(obj, this.emptyValue) ? toAppendTo : this.delegate.format(obj, toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.delegate.format(number, toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final long number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.delegate.format(number, toAppendTo, pos);
    }
    
    @Override
    public Object parseObject(final String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parseObject(source);
    }
    
    @Override
    public Number parse(final String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parse(source);
    }
    
    @Override
    public Number parse(final String source, final ParsePosition pos) {
        return this.delegate.parse(source, pos);
    }
}
