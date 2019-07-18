// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.format;

import java.text.AttributedCharacterIterator;
import java.util.TimeZone;
import java.text.NumberFormat;
import java.util.Calendar;
import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import com.jgoodies.common.base.Preconditions;
import java.text.DateFormat;

public abstract class AbstractWrappedDateFormat extends DateFormat
{
    protected final DateFormat delegate;
    
    public AbstractWrappedDateFormat(final DateFormat delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "The %1$s must not be null.", "delegate format");
    }
    
    @Override
    public abstract StringBuffer format(final Date p0, final StringBuffer p1, final FieldPosition p2);
    
    @Override
    public abstract Date parse(final String p0, final ParsePosition p1);
    
    @Override
    public Calendar getCalendar() {
        return this.delegate.getCalendar();
    }
    
    @Override
    public void setCalendar(final Calendar newCalendar) {
        this.delegate.setCalendar(newCalendar);
    }
    
    @Override
    public NumberFormat getNumberFormat() {
        return this.delegate.getNumberFormat();
    }
    
    @Override
    public void setNumberFormat(final NumberFormat newNumberFormat) {
        this.delegate.setNumberFormat(newNumberFormat);
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.delegate.getTimeZone();
    }
    
    @Override
    public void setTimeZone(final TimeZone zone) {
        this.delegate.setTimeZone(zone);
    }
    
    @Override
    public boolean isLenient() {
        return this.delegate.isLenient();
    }
    
    @Override
    public void setLenient(final boolean lenient) {
        this.delegate.setLenient(lenient);
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        return this.delegate.formatToCharacterIterator(obj);
    }
}
