// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.format;

import java.text.AttributedCharacterIterator;
import java.text.ParsePosition;
import java.text.ParseException;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.base.Objects;
import java.text.FieldPosition;
import com.jgoodies.common.base.Preconditions;
import java.text.Format;

public class EmptyFormat extends Format
{
    private final Format delegate;
    private final Object emptyValue;
    
    public EmptyFormat(final Format delegate) {
        this(delegate, null);
    }
    
    public EmptyFormat(final Format delegate, final Object emptyValue) {
        this.delegate = Preconditions.checkNotNull(delegate, "The %1$s must not be null.", "delegate format");
        this.emptyValue = emptyValue;
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        return Objects.equals(obj, this.emptyValue) ? toAppendTo : this.delegate.format(obj, toAppendTo, pos);
    }
    
    @Override
    public Object parseObject(final String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parseObject(source);
    }
    
    @Override
    public final Object parseObject(final String source, final ParsePosition pos) {
        return this.delegate.parseObject(source, pos);
    }
    
    @Override
    public final AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        return this.delegate.formatToCharacterIterator(obj);
    }
}
