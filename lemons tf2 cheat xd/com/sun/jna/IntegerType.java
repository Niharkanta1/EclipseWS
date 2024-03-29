// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

public abstract class IntegerType extends Number implements NativeMapped
{
    private int size;
    private Number number;
    private boolean unsigned;
    private long value;
    
    public IntegerType(final int size) {
        this(size, 0L, false);
    }
    
    public IntegerType(final int size, final boolean unsigned) {
        this(size, 0L, unsigned);
    }
    
    public IntegerType(final int size, final long value) {
        this(size, value, false);
    }
    
    public IntegerType(final int size, final long value, final boolean unsigned) {
        this.size = size;
        this.unsigned = unsigned;
        this.setValue(value);
    }
    
    public void setValue(final long value) {
        long truncated = value;
        this.value = value;
        switch (this.size) {
            case 1: {
                if (this.unsigned) {
                    this.value = (value & 0xFFL);
                }
                truncated = (byte)value;
                this.number = new Byte((byte)value);
                break;
            }
            case 2: {
                if (this.unsigned) {
                    this.value = (value & 0xFFFFL);
                }
                truncated = (short)value;
                this.number = new Short((short)value);
                break;
            }
            case 4: {
                if (this.unsigned) {
                    this.value = (value & 0xFFFFFFFFL);
                }
                truncated = (int)value;
                this.number = new Integer((int)value);
                break;
            }
            case 8: {
                this.number = new Long(value);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported size: " + this.size);
            }
        }
        if (this.size < 8) {
            final long mask = ~((1L << this.size * 8) - 1L);
            if ((value < 0L && truncated != value) || (value >= 0L && (mask & value) != 0x0L)) {
                throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(value) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + Long.toHexString(mask));
            }
        }
    }
    
    @Override
    public Object toNative() {
        return this.number;
    }
    
    @Override
    public Object fromNative(final Object nativeValue, final FromNativeContext context) {
        final long value = (nativeValue == null) ? 0L : ((Number)nativeValue).longValue();
        try {
            final IntegerType number = (IntegerType)this.getClass().newInstance();
            number.setValue(value);
            return number;
        }
        catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't instantiate " + this.getClass());
        }
        catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
        }
    }
    
    @Override
    public Class nativeType() {
        return this.number.getClass();
    }
    
    @Override
    public int intValue() {
        return (int)this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        return this.number.floatValue();
    }
    
    @Override
    public double doubleValue() {
        return this.number.doubleValue();
    }
    
    @Override
    public boolean equals(final Object rhs) {
        return rhs instanceof IntegerType && this.number.equals(((IntegerType)rhs).number);
    }
    
    @Override
    public String toString() {
        return this.number.toString();
    }
    
    @Override
    public int hashCode() {
        return this.number.hashCode();
    }
    
    public static <T extends IntegerType> int compare(final T v1, final T v2) {
        if (v1 == v2) {
            return 0;
        }
        if (v1 == null) {
            return 1;
        }
        if (v2 == null) {
            return -1;
        }
        return compare(v1.longValue(), v2.longValue());
    }
    
    public static int compare(final IntegerType v1, final long v2) {
        if (v1 == null) {
            return 1;
        }
        return compare(v1.longValue(), v2);
    }
    
    public static final int compare(final long v1, final long v2) {
        if (v1 == v2) {
            return 0;
        }
        if (v1 < v2) {
            return -1;
        }
        return 1;
    }
}
