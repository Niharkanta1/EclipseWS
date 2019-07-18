// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.ShortByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.Unknown;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import java.util.Date;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.Pointer;
import com.sun.jna.Union;

public interface Variant
{
    public static final int VT_EMPTY = 0;
    public static final int VT_NULL = 1;
    public static final int VT_I2 = 2;
    public static final int VT_I4 = 3;
    public static final int VT_R4 = 4;
    public static final int VT_R8 = 5;
    public static final int VT_CY = 6;
    public static final int VT_DATE = 7;
    public static final int VT_BSTR = 8;
    public static final int VT_DISPATCH = 9;
    public static final int VT_ERROR = 10;
    public static final int VT_BOOL = 11;
    public static final int VT_VARIANT = 12;
    public static final int VT_UNKNOWN = 13;
    public static final int VT_DECIMAL = 14;
    public static final int VT_I1 = 16;
    public static final int VT_UI1 = 17;
    public static final int VT_UI2 = 18;
    public static final int VT_UI4 = 19;
    public static final int VT_I8 = 20;
    public static final int VT_UI8 = 21;
    public static final int VT_INT = 22;
    public static final int VT_UINT = 23;
    public static final int VT_VOID = 24;
    public static final int VT_HRESULT = 25;
    public static final int VT_PTR = 26;
    public static final int VT_SAFEARRAY = 27;
    public static final int VT_CARRAY = 28;
    public static final int VT_USERDEFINED = 29;
    public static final int VT_LPSTR = 30;
    public static final int VT_LPWSTR = 31;
    public static final int VT_RECORD = 36;
    public static final int VT_INT_PTR = 37;
    public static final int VT_UINT_PTR = 38;
    public static final int VT_FILETIME = 64;
    public static final int VT_BLOB = 65;
    public static final int VT_STREAM = 66;
    public static final int VT_STORAGE = 67;
    public static final int VT_STREAMED_OBJECT = 68;
    public static final int VT_STORED_OBJECT = 69;
    public static final int VT_BLOB_OBJECT = 70;
    public static final int VT_CF = 71;
    public static final int VT_CLSID = 72;
    public static final int VT_VERSIONED_STREAM = 73;
    public static final int VT_BSTR_BLOB = 4095;
    public static final int VT_VECTOR = 4096;
    public static final int VT_ARRAY = 8192;
    public static final int VT_BYREF = 16384;
    public static final int VT_RESERVED = 32768;
    public static final int VT_ILLEGAL = 65535;
    public static final int VT_ILLEGALMASKED = 4095;
    public static final int VT_TYPEMASK = 4095;
    public static final OaIdl.VARIANT_BOOL VARIANT_TRUE = new OaIdl.VARIANT_BOOL(65535L);
    public static final OaIdl.VARIANT_BOOL VARIANT_FALSE = new OaIdl.VARIANT_BOOL(0L);
    public static final long COM_DAYS_ADJUSTMENT = 25569L;
    public static final long MICRO_SECONDS_PER_DAY = 86400000L;
    
    public static class VARIANT extends Union
    {
        public _VARIANT _variant;
        public OaIdl.DECIMAL decVal;
        
        public VARIANT() {
            this.setType("_variant");
            this.read();
        }
        
        public VARIANT(final Pointer pointer) {
            super(pointer);
            this.setType("_variant");
            this.read();
        }
        
        public VARIANT(final WTypes.BSTR value) {
            this();
            this.setValue(8, value);
        }
        
        public VARIANT(final WTypes.BSTRByReference value) {
            this();
            this.setValue(16392, value);
        }
        
        public VARIANT(final OaIdl.VARIANT_BOOL value) {
            this();
            this.setValue(11, new WinDef.BOOL((long)value.intValue()));
        }
        
        public VARIANT(final WinDef.BOOL value) {
            this();
            this.setValue(11, value);
        }
        
        public VARIANT(final WinDef.LONG value) {
            this();
            this.setValue(3, value);
        }
        
        public VARIANT(final WinDef.SHORT value) {
            this();
            this.setValue(2, value);
        }
        
        public VARIANT(final OaIdl.DATE value) {
            this();
            this.setValue(7, value);
        }
        
        public VARIANT(final byte value) {
            this(new WinDef.BYTE((long)value));
        }
        
        public VARIANT(final WinDef.BYTE value) {
            this();
            this.setValue(17, value);
        }
        
        public VARIANT(final char value) {
            this(new WinDef.CHAR(value));
        }
        
        public VARIANT(final WinDef.CHAR value) {
            this();
            this.setValue(16, value);
        }
        
        public VARIANT(final short value) {
            this();
            this.setValue(2, new WinDef.SHORT((long)value));
        }
        
        public VARIANT(final int value) {
            this();
            this.setValue(3, new WinDef.LONG((long)value));
        }
        
        public VARIANT(final long value) {
            this();
            this.setValue(20, new WinDef.LONGLONG(value));
        }
        
        public VARIANT(final float value) {
            this();
            this.setValue(4, value);
        }
        
        public VARIANT(final double value) {
            this();
            this.setValue(5, value);
        }
        
        public VARIANT(final String value) {
            this();
            final WTypes.BSTR bstrValue = OleAuto.INSTANCE.SysAllocString(value);
            this.setValue(8, bstrValue);
        }
        
        public VARIANT(final boolean value) {
            this();
            if (value) {
                this.setValue(11, new WinDef.BOOL((long)Variant.VARIANT_TRUE.intValue()));
            }
            else {
                this.setValue(11, new WinDef.BOOL((long)Variant.VARIANT_FALSE.intValue()));
            }
        }
        
        public VARIANT(final IDispatch value) {
            this();
            this.setValue(9, value);
        }
        
        public VARIANT(final Date value) {
            this();
            final OaIdl.DATE date = this.fromJavaDate(value);
            this.setValue(7, date);
        }
        
        public WTypes.VARTYPE getVarType() {
            this.read();
            return this._variant.vt;
        }
        
        public void setVarType(final short vt) {
            this._variant.vt = new WTypes.VARTYPE(vt);
        }
        
        public void setValue(final int vt, final Object value) {
            this.setValue(new WTypes.VARTYPE(vt), value);
        }
        
        public void setValue(final WTypes.VARTYPE vt, final Object value) {
            switch (vt.intValue()) {
                case 17: {
                    this._variant.__variant.writeField("bVal", value);
                    break;
                }
                case 2: {
                    this._variant.__variant.writeField("iVal", value);
                    break;
                }
                case 3: {
                    this._variant.__variant.writeField("lVal", value);
                    break;
                }
                case 20: {
                    this._variant.__variant.writeField("llVal", value);
                    break;
                }
                case 4: {
                    this._variant.__variant.writeField("fltVal", value);
                    break;
                }
                case 5: {
                    this._variant.__variant.writeField("dblVal", value);
                    break;
                }
                case 11: {
                    this._variant.__variant.writeField("boolVal", value);
                    break;
                }
                case 10: {
                    this._variant.__variant.writeField("scode", value);
                    break;
                }
                case 6: {
                    this._variant.__variant.writeField("cyVal", value);
                    break;
                }
                case 7: {
                    this._variant.__variant.writeField("date", value);
                    break;
                }
                case 8: {
                    this._variant.__variant.writeField("bstrVal", value);
                    break;
                }
                case 13: {
                    this._variant.__variant.writeField("punkVal", value);
                    break;
                }
                case 9: {
                    this._variant.__variant.writeField("pdispVal", value);
                    break;
                }
                case 27: {
                    this._variant.__variant.writeField("parray", value);
                    break;
                }
                case 8192: {
                    this._variant.__variant.writeField("parray", value);
                    break;
                }
                case 16401: {
                    this._variant.__variant.writeField("pbVal", value);
                    break;
                }
                case 16386: {
                    this._variant.__variant.writeField("piVal", value);
                    break;
                }
                case 16387: {
                    this._variant.__variant.writeField("plVal", value);
                    break;
                }
                case 16404: {
                    this._variant.__variant.writeField("pllVal", value);
                    break;
                }
                case 16388: {
                    this._variant.__variant.writeField("pfltVal", value);
                    break;
                }
                case 16389: {
                    this._variant.__variant.writeField("pdblVal", value);
                    break;
                }
                case 16395: {
                    this._variant.__variant.writeField("pboolVal", value);
                    break;
                }
                case 16394: {
                    this._variant.__variant.writeField("pscode", value);
                    break;
                }
                case 16390: {
                    this._variant.__variant.writeField("pcyVal", value);
                    break;
                }
                case 16391: {
                    this._variant.__variant.writeField("pdate", value);
                    break;
                }
                case 16392: {
                    this._variant.__variant.writeField("pbstrVal", value);
                    break;
                }
                case 16397: {
                    this._variant.__variant.writeField("ppunkVal", value);
                    break;
                }
                case 16393: {
                    this._variant.__variant.writeField("ppdispVal", value);
                    break;
                }
                case 24576: {
                    this._variant.__variant.writeField("pparray", value);
                    break;
                }
                case 16396: {
                    this._variant.__variant.writeField("pvarVal", value);
                    break;
                }
                case 16384: {
                    this._variant.__variant.writeField("byref", value);
                    break;
                }
                case 16: {
                    this._variant.__variant.writeField("cVal", value);
                    break;
                }
                case 18: {
                    this._variant.__variant.writeField("uiVal", value);
                    break;
                }
                case 19: {
                    this._variant.__variant.writeField("ulVal", value);
                    break;
                }
                case 21: {
                    this._variant.__variant.writeField("ullVal", value);
                    break;
                }
                case 22: {
                    this._variant.__variant.writeField("intVal", value);
                    break;
                }
                case 23: {
                    this._variant.__variant.writeField("uintVal", value);
                    break;
                }
                case 16398: {
                    this._variant.__variant.writeField("pdecVal", value);
                    break;
                }
                case 16400: {
                    this._variant.__variant.writeField("pcVal", value);
                    break;
                }
                case 16402: {
                    this._variant.__variant.writeField("puiVal", value);
                    break;
                }
                case 16403: {
                    this._variant.__variant.writeField("pulVal", value);
                    break;
                }
                case 16405: {
                    this._variant.__variant.writeField("pullVal", value);
                    break;
                }
                case 16406: {
                    this._variant.__variant.writeField("pintVal", value);
                    break;
                }
                case 16407: {
                    this._variant.__variant.writeField("puintVal", value);
                    break;
                }
                case 36: {
                    this._variant.__variant.writeField("pvRecord", value);
                    break;
                }
            }
            this._variant.writeField("vt", vt);
            this.write();
        }
        
        public Object getValue() {
            this.read();
            switch (this.getVarType().intValue()) {
                case 2: {
                    return this._variant.__variant.readField("iVal");
                }
                case 3: {
                    return this._variant.__variant.readField("lVal");
                }
                case 20: {
                    return this._variant.__variant.readField("llVal");
                }
                case 4: {
                    return this._variant.__variant.readField("fltVal");
                }
                case 5: {
                    return this._variant.__variant.readField("dblVal");
                }
                case 11: {
                    return this._variant.__variant.readField("boolVal");
                }
                case 10: {
                    return this._variant.__variant.readField("scode");
                }
                case 6: {
                    return this._variant.__variant.readField("cyVal");
                }
                case 7: {
                    return this._variant.__variant.readField("date");
                }
                case 8: {
                    return this._variant.__variant.readField("bstrVal");
                }
                case 13: {
                    return this._variant.__variant.readField("punkVal");
                }
                case 9: {
                    return this._variant.__variant.readField("pdispVal");
                }
                case 27: {
                    return this._variant.__variant.readField("parray");
                }
                case 8192: {
                    return this._variant.__variant.readField("parray");
                }
                case 16401: {
                    return this._variant.__variant.readField("pbVal");
                }
                case 16386: {
                    return this._variant.__variant.readField("piVal");
                }
                case 16387: {
                    return this._variant.__variant.readField("plVal");
                }
                case 16404: {
                    return this._variant.__variant.readField("pllVal");
                }
                case 16388: {
                    return this._variant.__variant.readField("pfltVal");
                }
                case 16389: {
                    return this._variant.__variant.readField("pdblVal");
                }
                case 16395: {
                    return this._variant.__variant.readField("pboolVal");
                }
                case 16394: {
                    return this._variant.__variant.readField("pscode");
                }
                case 16390: {
                    return this._variant.__variant.readField("pcyVal");
                }
                case 16391: {
                    return this._variant.__variant.readField("pdate");
                }
                case 16392: {
                    return this._variant.__variant.readField("pbstrVal");
                }
                case 16397: {
                    return this._variant.__variant.readField("ppunkVal");
                }
                case 16393: {
                    return this._variant.__variant.readField("ppdispVal");
                }
                case 24576: {
                    return this._variant.__variant.readField("pparray");
                }
                case 16396: {
                    return this._variant.__variant.readField("pvarVal");
                }
                case 16384: {
                    return this._variant.__variant.readField("byref");
                }
                case 16: {
                    return this._variant.__variant.readField("cVal");
                }
                case 18: {
                    return this._variant.__variant.readField("uiVal");
                }
                case 19: {
                    return this._variant.__variant.readField("ulVal");
                }
                case 21: {
                    return this._variant.__variant.readField("ullVal");
                }
                case 22: {
                    return this._variant.__variant.readField("intVal");
                }
                case 23: {
                    return this._variant.__variant.readField("uintVal");
                }
                case 16398: {
                    return this._variant.__variant.readField("pdecVal");
                }
                case 16400: {
                    return this._variant.__variant.readField("pcVal");
                }
                case 16402: {
                    return this._variant.__variant.readField("puiVal");
                }
                case 16403: {
                    return this._variant.__variant.readField("pulVal");
                }
                case 16405: {
                    return this._variant.__variant.readField("pullVal");
                }
                case 16406: {
                    return this._variant.__variant.readField("pintVal");
                }
                case 16407: {
                    return this._variant.__variant.readField("puintVal");
                }
                case 36: {
                    return this._variant.__variant.readField("pvRecord");
                }
                default: {
                    return null;
                }
            }
        }
        
        public int shortValue() {
            return (short)this.getValue();
        }
        
        public int intValue() {
            return (int)this.getValue();
        }
        
        public long longValue() {
            return (long)this.getValue();
        }
        
        public float floatValue() {
            return (float)this.getValue();
        }
        
        public double doubleValue() {
            return (double)this.getValue();
        }
        
        public String stringValue() {
            final WTypes.BSTR bstr = (WTypes.BSTR)this.getValue();
            return bstr.getValue();
        }
        
        public boolean booleanValue() {
            return (boolean)this.getValue();
        }
        
        public Date dateValue() {
            final OaIdl.DATE varDate = (OaIdl.DATE)this.getValue();
            return this.toJavaDate(varDate);
        }
        
        protected Date toJavaDate(final OaIdl.DATE varDate) {
            final double doubleDate = varDate.date;
            final long longDate = (long)doubleDate;
            final double doubleTime = doubleDate - longDate;
            final long longTime = (long)doubleTime * 86400000L;
            return new Date((longDate - 25569L) * 86400000L + longTime);
        }
        
        protected OaIdl.DATE fromJavaDate(final Date javaDate) {
            final long longTime = javaDate.getTime() % 86400000L;
            final long longDate = (javaDate.getTime() - longTime) / 86400000L + 25569L;
            final float floatTime = longTime / 8.64E7f;
            final float floatDateTime = floatTime + longDate;
            return new OaIdl.DATE(floatDateTime);
        }
        
        public static class ByReference extends VARIANT implements Structure.ByReference
        {
            public ByReference(final VARIANT variant) {
                this.setValue(variant.getVarType(), variant.getValue());
            }
            
            public ByReference(final Pointer variant) {
                super(variant);
            }
            
            public ByReference() {
            }
        }
        
        public static class ByValue extends VARIANT implements Structure.ByValue
        {
            public ByValue(final VARIANT variant) {
                this.setValue(variant.getVarType(), variant.getValue());
            }
            
            public ByValue(final Pointer variant) {
                super(variant);
            }
            
            public ByValue() {
            }
        }
        
        public static class _VARIANT extends Structure
        {
            public WTypes.VARTYPE vt;
            public short wReserved1;
            public short wReserved2;
            public short wReserved3;
            public __VARIANT __variant;
            
            public _VARIANT() {
            }
            
            public _VARIANT(final Pointer pointer) {
                super(pointer);
                this.read();
            }
            
            @Override
            protected List getFieldOrder() {
                return Arrays.asList("vt", "wReserved1", "wReserved2", "wReserved3", "__variant");
            }
            
            public static class __VARIANT extends Union
            {
                public WinDef.LONGLONG llVal;
                public WinDef.LONG lVal;
                public WinDef.BYTE bVal;
                public WinDef.SHORT iVal;
                public Float fltVal;
                public Double dblVal;
                public WinDef.BOOL boolVal;
                public WinDef.SCODE scode;
                public OaIdl.CURRENCY cyVal;
                public OaIdl.DATE date;
                public WTypes.BSTR bstrVal;
                public Unknown punkVal;
                public Dispatch pdispVal;
                public OaIdl.SAFEARRAY.ByReference parray;
                public ByteByReference pbVal;
                public ShortByReference piVal;
                public WinDef.LONGByReference plVal;
                public WinDef.LONGLONGByReference pllVal;
                public FloatByReference pfltVal;
                public DoubleByReference pdblVal;
                public OaIdl.VARIANT_BOOLByReference pboolVal;
                public OaIdl._VARIANT_BOOLByReference pbool;
                public WinDef.SCODEByReference pscode;
                public OaIdl.CURRENCY.ByReference pcyVal;
                public OaIdl.DATE.ByReference pdate;
                public WTypes.BSTR.ByReference pbstrVal;
                public Unknown.ByReference ppunkVal;
                public Dispatch.ByReference ppdispVal;
                public OaIdl.SAFEARRAY.ByReference pparray;
                public VARIANT.ByReference pvarVal;
                public WinDef.PVOID byref;
                public WinDef.CHAR cVal;
                public WinDef.USHORT uiVal;
                public WinDef.ULONG ulVal;
                public WinDef.ULONGLONG ullVal;
                public Integer intVal;
                public WinDef.UINT uintVal;
                public OaIdl.DECIMAL.ByReference pdecVal;
                public WinDef.CHARByReference pcVal;
                public WinDef.USHORTByReference puiVal;
                public WinDef.ULONGByReference pulVal;
                public WinDef.ULONGLONGByReference pullVal;
                public IntByReference pintVal;
                public WinDef.UINTByReference puintVal;
                public BRECORD pvRecord;
                
                public __VARIANT() {
                    this.read();
                }
                
                public __VARIANT(final Pointer pointer) {
                    super(pointer);
                    this.read();
                }
                
                public static class BRECORD extends Structure
                {
                    public WinDef.PVOID pvRecord;
                    public Pointer pRecInfo;
                    
                    public BRECORD() {
                    }
                    
                    public BRECORD(final Pointer pointer) {
                        super(pointer);
                    }
                    
                    @Override
                    protected List getFieldOrder() {
                        return Arrays.asList("pvRecord", "pRecInfo");
                    }
                    
                    public static class ByReference extends BRECORD implements Structure.ByReference
                    {
                    }
                }
            }
        }
    }
    
    public static class VariantArg extends Structure
    {
        public VARIANT[] variantArg;
        
        public VariantArg() {
            this.variantArg = new VARIANT[1];
        }
        
        public VariantArg(final Pointer pointer) {
            super(pointer);
            this.variantArg = new VARIANT[1];
        }
        
        public VariantArg(final VARIANT[] variantArg) {
            this.variantArg = new VARIANT[1];
            this.variantArg = variantArg;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("variantArg");
        }
        
        public void setArraySize(final int size) {
            this.variantArg = new VARIANT[size];
            this.read();
        }
        
        public static class ByReference extends VariantArg implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final VARIANT[] variantArg) {
                this.variantArg = variantArg;
            }
        }
    }
}
