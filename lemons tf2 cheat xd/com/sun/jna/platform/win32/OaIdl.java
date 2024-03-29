// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.COM.TypeComp;
import com.sun.jna.NativeLong;
import com.sun.jna.Union;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.IntegerType;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface OaIdl
{
    public static final DISPID DISPID_COLLECT = new DISPID(-8);
    public static final DISPID DISPID_CONSTRUCTOR = new DISPID(-6);
    public static final DISPID DISPID_DESTRUCTOR = new DISPID(-7);
    public static final DISPID DISPID_EVALUATE = new DISPID(-5);
    public static final DISPID DISPID_NEWENUM = new DISPID(-4);
    public static final DISPID DISPID_PROPERTYPUT = new DISPID(-3);
    public static final DISPID DISPID_UNKNOWN = new DISPID(-1);
    public static final DISPID DISPID_VALUE = new DISPID(0);
    public static final MEMBERID MEMBERID_NIL = new MEMBERID(OaIdl.DISPID_UNKNOWN.intValue());
    public static final int FADF_AUTO = 1;
    public static final int FADF_STATIC = 2;
    public static final int FADF_EMBEDDED = 4;
    public static final int FADF_FIXEDSIZE = 16;
    public static final int FADF_RECORD = 32;
    public static final int FADF_HAVEIID = 64;
    public static final int FADF_HAVEVARTYPE = 128;
    public static final int FADF_BSTR = 256;
    public static final int FADF_UNKNOWN = 512;
    public static final int FADF_DISPATCH = 1024;
    public static final int FADF_VARIANT = 2048;
    public static final int FADF_RESERVED = 61448;
    
    public static class EXCEPINFO extends Structure
    {
        public WinDef.WORD wCode;
        public WinDef.WORD wReserved;
        public WTypes.BSTR bstrSource;
        public WTypes.BSTR bstrDescription;
        public WTypes.BSTR bstrHelpFile;
        public WinDef.DWORD dwHelpContext;
        public WinDef.PVOID pvReserved;
        public ByReference pfnDeferredFillIn;
        public WinDef.SCODE scode;
        
        public EXCEPINFO() {
        }
        
        public EXCEPINFO(final Pointer p) {
            super(p);
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("wCode", "wReserved", "bstrSource", "bstrDescription", "bstrHelpFile", "dwHelpContext", "pvReserved", "pfnDeferredFillIn", "scode");
        }
        
        public static class ByReference extends EXCEPINFO implements Structure.ByReference
        {
        }
    }
    
    public static class VARIANT_BOOL extends IntegerType
    {
        public static final int SIZE = 2;
        
        public VARIANT_BOOL() {
            this(0L);
        }
        
        public VARIANT_BOOL(final long value) {
            super(2, value);
        }
    }
    
    public static class _VARIANT_BOOL extends VARIANT_BOOL
    {
        public _VARIANT_BOOL() {
            this(0L);
        }
        
        public _VARIANT_BOOL(final long value) {
            super(value);
        }
    }
    
    public static class VARIANT_BOOLByReference extends ByReference
    {
        public VARIANT_BOOLByReference() {
            this(new VARIANT_BOOL(0L));
        }
        
        public VARIANT_BOOLByReference(final VARIANT_BOOL value) {
            super(2);
            this.setValue(value);
        }
        
        public void setValue(final VARIANT_BOOL value) {
            this.getPointer().setShort(0L, value.shortValue());
        }
        
        public VARIANT_BOOL getValue() {
            return new VARIANT_BOOL((long)this.getPointer().getShort(0L));
        }
    }
    
    public static class _VARIANT_BOOLByReference extends ByReference
    {
        public _VARIANT_BOOLByReference() {
            this(new VARIANT_BOOL(0L));
        }
        
        public _VARIANT_BOOLByReference(final VARIANT_BOOL value) {
            super(2);
            this.setValue(value);
        }
        
        public void setValue(final VARIANT_BOOL value) {
            this.getPointer().setShort(0L, value.shortValue());
        }
        
        public VARIANT_BOOL getValue() {
            return new VARIANT_BOOL((long)this.getPointer().getShort(0L));
        }
    }
    
    public static class DATE extends Structure
    {
        public double date;
        
        public DATE() {
        }
        
        public DATE(final double date) {
            this.date = date;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("date");
        }
        
        public static class ByReference extends DATE implements Structure.ByReference
        {
        }
    }
    
    public static class DISPID extends WinDef.LONG
    {
        public DISPID() {
            this(0);
        }
        
        public DISPID(final int value) {
            super((long)value);
        }
    }
    
    public static class DISPIDByReference extends ByReference
    {
        public DISPIDByReference() {
            this(new DISPID(0));
        }
        
        public DISPIDByReference(final DISPID value) {
            super(DISPID.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final DISPID value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public DISPID getValue() {
            return new DISPID(this.getPointer().getInt(0L));
        }
    }
    
    public static class MEMBERID extends DISPID
    {
        public MEMBERID() {
            this(0);
        }
        
        public MEMBERID(final int value) {
            super(value);
        }
    }
    
    public static class MEMBERIDByReference extends ByReference
    {
        public MEMBERIDByReference() {
            this(new MEMBERID(0));
        }
        
        public MEMBERIDByReference(final MEMBERID value) {
            super(MEMBERID.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final MEMBERID value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public MEMBERID getValue() {
            return new MEMBERID(this.getPointer().getInt(0L));
        }
    }
    
    public static class TYPEKIND extends Structure
    {
        public int value;
        public static final int TKIND_ENUM = 0;
        public static final int TKIND_RECORD = 1;
        public static final int TKIND_MODULE = 2;
        public static final int TKIND_INTERFACE = 3;
        public static final int TKIND_DISPATCH = 4;
        public static final int TKIND_COCLASS = 5;
        public static final int TKIND_ALIAS = 6;
        public static final int TKIND_UNION = 7;
        public static final int TKIND_MAX = 8;
        
        public TYPEKIND() {
        }
        
        public TYPEKIND(final int value) {
            this.value = value;
        }
        
        public TYPEKIND(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends TYPEKIND implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final int value) {
                super(value);
            }
            
            public ByReference(final TYPEKIND typekind) {
                super(typekind.getPointer());
                this.value = typekind.value;
            }
        }
    }
    
    public static class DESCKIND extends Structure
    {
        public int value;
        public static final int DESCKIND_NONE = 0;
        public static final int DESCKIND_FUNCDESC = 1;
        public static final int DESCKIND_VARDESC = 2;
        public static final int DESCKIND_TYPECOMP = 3;
        public static final int DESCKIND_IMPLICITAPPOBJ = 4;
        public static final int DESCKIND_MAX = 5;
        
        public DESCKIND() {
        }
        
        public DESCKIND(final int value) {
            this.value = value;
        }
        
        public DESCKIND(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends DESCKIND implements Structure.ByReference
        {
        }
    }
    
    public static class SAFEARRAY extends Structure
    {
        public WinDef.USHORT cDims;
        public WinDef.USHORT fFeatures;
        public WinDef.ULONG cbElements;
        public WinDef.ULONG cLocks;
        public WinDef.PVOID pvData;
        public SAFEARRAYBOUND[] rgsabound;
        
        public SAFEARRAY() {
            this.rgsabound = new SAFEARRAYBOUND[] { new SAFEARRAYBOUND() };
        }
        
        public SAFEARRAY(final Pointer pointer) {
            super(pointer);
            this.rgsabound = new SAFEARRAYBOUND[] { new SAFEARRAYBOUND() };
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("cDims", "fFeatures", "cbElements", "cLocks", "pvData", "rgsabound");
        }
        
        public static class ByReference extends SAFEARRAY implements Structure.ByReference
        {
        }
    }
    
    public static class SAFEARRAYBOUND extends Structure
    {
        public WinDef.ULONG cElements;
        public WinDef.LONG lLbound;
        
        public SAFEARRAYBOUND() {
        }
        
        public SAFEARRAYBOUND(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        public SAFEARRAYBOUND(final int cElements, final int lLbound) {
            this.cElements = new WinDef.ULONG((long)cElements);
            this.lLbound = new WinDef.LONG((long)lLbound);
            this.write();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("cElements", "lLbound");
        }
        
        public static class ByReference extends SAFEARRAYBOUND implements Structure.ByReference
        {
        }
    }
    
    public static class CURRENCY extends Union
    {
        public _CURRENCY currency;
        public WinDef.LONGLONG int64;
        
        public CURRENCY() {
        }
        
        public CURRENCY(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        public static class ByReference extends CURRENCY implements Structure.ByReference
        {
        }
        
        public static class _CURRENCY extends Structure
        {
            public WinDef.ULONG Lo;
            public WinDef.LONG Hi;
            
            public _CURRENCY() {
            }
            
            public _CURRENCY(final Pointer pointer) {
                super(pointer);
                this.read();
            }
            
            @Override
            protected List getFieldOrder() {
                return Arrays.asList("Lo", "Hi");
            }
        }
    }
    
    public static class DECIMAL extends Structure
    {
        public short wReserved;
        public _DECIMAL1 decimal1;
        public NativeLong Hi32;
        public _DECIMAL2 decimal2;
        
        public DECIMAL() {
        }
        
        public DECIMAL(final Pointer pointer) {
            super(pointer);
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("wReserved", "decimal1", "Hi32", "decimal2");
        }
        
        public static class ByReference extends DECIMAL implements Structure.ByReference
        {
        }
        
        public static class _DECIMAL1 extends Union
        {
            public WinDef.USHORT signscale;
            public _DECIMAL1_DECIMAL decimal1_DECIMAL;
            
            public _DECIMAL1() {
                this.setType("signscale");
            }
            
            public _DECIMAL1(final Pointer pointer) {
                super(pointer);
                this.setType("signscale");
                this.read();
            }
            
            public static class _DECIMAL1_DECIMAL extends Structure
            {
                public WinDef.BYTE scale;
                public WinDef.BYTE sign;
                
                public _DECIMAL1_DECIMAL() {
                }
                
                public _DECIMAL1_DECIMAL(final Pointer pointer) {
                    super(pointer);
                }
                
                @Override
                protected List getFieldOrder() {
                    return Arrays.asList("scale", "sign");
                }
            }
        }
        
        public static class _DECIMAL2 extends Union
        {
            public WinDef.ULONGLONG Lo64;
            public _DECIMAL2_DECIMAL decimal2_DECIMAL;
            
            public _DECIMAL2() {
                this.setType("Lo64");
            }
            
            public _DECIMAL2(final Pointer pointer) {
                super(pointer);
                this.setType("Lo64");
                this.read();
            }
            
            public static class _DECIMAL2_DECIMAL extends Structure
            {
                public WinDef.BYTE Lo32;
                public WinDef.BYTE Mid32;
                
                public _DECIMAL2_DECIMAL() {
                }
                
                public _DECIMAL2_DECIMAL(final Pointer pointer) {
                    super(pointer);
                }
                
                @Override
                protected List getFieldOrder() {
                    return Arrays.asList("Lo32", "Mid32");
                }
            }
        }
    }
    
    public static class SYSKIND extends Structure
    {
        public int value;
        public static final int SYS_WIN16 = 0;
        public static final int SYS_WIN32 = 1;
        public static final int SYS_MAC = 2;
        public static final int SYS_WIN64 = 3;
        
        public SYSKIND() {
        }
        
        public SYSKIND(final int value) {
            this.value = value;
        }
        
        public SYSKIND(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends SYSKIND implements Structure.ByReference
        {
        }
    }
    
    public static class LIBFLAGS extends Structure
    {
        public int value;
        public static final int LIBFLAG_FRESTRICTED = 1;
        public static final int LIBFLAG_FCONTROL = 2;
        public static final int LIBFLAG_FHIDDEN = 4;
        public static final int LIBFLAG_FHASDISKIMAGE = 8;
        
        public LIBFLAGS() {
        }
        
        public LIBFLAGS(final int value) {
            this.value = value;
        }
        
        public LIBFLAGS(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends LIBFLAGS implements Structure.ByReference
        {
        }
    }
    
    public static class TLIBATTR extends Structure
    {
        public Guid.GUID guid;
        public WinDef.LCID lcid;
        public SYSKIND syskind;
        public WinDef.WORD wMajorVerNum;
        public WinDef.WORD wMinorVerNum;
        public WinDef.WORD wLibFlags;
        
        public TLIBATTR() {
        }
        
        public TLIBATTR(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("guid", "lcid", "syskind", "wMajorVerNum", "wMinorVerNum", "wLibFlags");
        }
        
        public static class ByReference extends TLIBATTR implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer pointer) {
                super(pointer);
                this.read();
            }
        }
    }
    
    public static class BINDPTR extends Union
    {
        public FUNCDESC lpfuncdesc;
        public VARDESC lpvardesc;
        public TypeComp lptcomp;
        
        public BINDPTR() {
        }
        
        public BINDPTR(final VARDESC lpvardesc) {
            this.lpvardesc = lpvardesc;
            this.setType(VARDESC.class);
        }
        
        public BINDPTR(final TypeComp lptcomp) {
            this.lptcomp = lptcomp;
            this.setType(TypeComp.class);
        }
        
        public BINDPTR(final FUNCDESC lpfuncdesc) {
            this.lpfuncdesc = lpfuncdesc;
            this.setType(FUNCDESC.class);
        }
        
        public static class ByReference extends BINDPTR implements Structure.ByReference
        {
        }
    }
    
    public static class FUNCDESC extends Structure
    {
        public MEMBERID memid;
        public ScodeArg.ByReference lprgscode;
        public ElemDescArg.ByReference lprgelemdescParam;
        public FUNCKIND funckind;
        public INVOKEKIND invkind;
        public CALLCONV callconv;
        public WinDef.SHORT cParams;
        public WinDef.SHORT cParamsOpt;
        public WinDef.SHORT oVft;
        public WinDef.SHORT cScodes;
        public ELEMDESC elemdescFunc;
        public WinDef.WORD wFuncFlags;
        
        public FUNCDESC() {
        }
        
        public FUNCDESC(final Pointer pointer) {
            super(pointer);
            this.read();
            if (this.cParams.shortValue() > 1) {
                this.lprgelemdescParam.elemDescArg = new ELEMDESC[this.cParams.shortValue()];
                this.lprgelemdescParam.read();
            }
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("memid", "lprgscode", "lprgelemdescParam", "funckind", "invkind", "callconv", "cParams", "cParamsOpt", "oVft", "cScodes", "elemdescFunc", "wFuncFlags");
        }
        
        public static class ByReference extends FUNCDESC implements Structure.ByReference
        {
        }
    }
    
    public static class ElemDescArg extends Structure
    {
        public ELEMDESC[] elemDescArg;
        
        public ElemDescArg() {
            this.elemDescArg = new ELEMDESC[] { new ELEMDESC() };
        }
        
        public ElemDescArg(final Pointer pointer) {
            super(pointer);
            this.elemDescArg = new ELEMDESC[] { new ELEMDESC() };
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("elemDescArg");
        }
        
        public static class ByReference extends ElemDescArg implements Structure.ByReference
        {
        }
    }
    
    public static class ScodeArg extends Structure
    {
        public WinDef.SCODE[] scodeArg;
        
        public ScodeArg() {
            this.scodeArg = new WinDef.SCODE[] { new WinDef.SCODE() };
        }
        
        public ScodeArg(final Pointer pointer) {
            super(pointer);
            this.scodeArg = new WinDef.SCODE[] { new WinDef.SCODE() };
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("scodeArg");
        }
        
        public static class ByReference extends ScodeArg implements Structure.ByReference
        {
        }
    }
    
    public static class VARDESC extends Structure
    {
        public MEMBERID memid;
        public WTypes.LPOLESTR lpstrSchema;
        public _VARDESC _vardesc;
        public ELEMDESC elemdescVar;
        public WinDef.WORD wVarFlags;
        public VARKIND varkind;
        
        public VARDESC() {
        }
        
        public VARDESC(final Pointer pointer) {
            super(pointer);
            this._vardesc.setType("lpvarValue");
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("memid", "lpstrSchema", "_vardesc", "elemdescVar", "wVarFlags", "varkind");
        }
        
        public static class ByReference extends VARDESC implements Structure.ByReference
        {
        }
        
        public static class _VARDESC extends Union
        {
            public NativeLong oInst;
            public Variant.VARIANT.ByReference lpvarValue;
            
            public _VARDESC() {
                this.setType("lpvarValue");
                this.read();
            }
            
            public _VARDESC(final Pointer pointer) {
                super(pointer);
                this.setType("lpvarValue");
                this.read();
            }
            
            public _VARDESC(final Variant.VARIANT.ByReference lpvarValue) {
                this.lpvarValue = lpvarValue;
                this.setType("lpvarValue");
            }
            
            public _VARDESC(final NativeLong oInst) {
                this.oInst = oInst;
                this.setType("oInst");
            }
            
            public static class ByReference extends _VARDESC implements Structure.ByReference
            {
            }
        }
    }
    
    public static class ELEMDESC extends Structure
    {
        public TYPEDESC tdesc;
        public _ELEMDESC _elemdesc;
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("tdesc", "_elemdesc");
        }
        
        public ELEMDESC() {
        }
        
        public ELEMDESC(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        public static class ByReference extends ELEMDESC implements Structure.ByReference
        {
        }
        
        public static class _ELEMDESC extends Union
        {
            public IDLDESC idldesc;
            public PARAMDESC paramdesc;
            
            public _ELEMDESC() {
            }
            
            public _ELEMDESC(final Pointer pointer) {
                super(pointer);
                this.setType("paramdesc");
                this.read();
            }
            
            public _ELEMDESC(final PARAMDESC paramdesc) {
                this.paramdesc = paramdesc;
                this.setType("paramdesc");
            }
            
            public _ELEMDESC(final IDLDESC idldesc) {
                this.idldesc = idldesc;
                this.setType("idldesc");
            }
            
            public static class ByReference extends _ELEMDESC implements Structure.ByReference
            {
            }
        }
    }
    
    public static class FUNCKIND extends Structure
    {
        public static final int FUNC_VIRTUAL = 0;
        public static final int FUNC_PUREVIRTUAL = 1;
        public static final int FUNC_NONVIRTUAL = 2;
        public static final int FUNC_STATIC = 3;
        public static final int FUNC_DISPATCH = 4;
        public int value;
        
        public FUNCKIND() {
        }
        
        public FUNCKIND(final int value) {
            this.value = value;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends FUNCKIND implements Structure.ByReference
        {
        }
    }
    
    public static class INVOKEKIND extends Structure
    {
        public static final INVOKEKIND INVOKE_FUNC;
        public static final INVOKEKIND INVOKE_PROPERTYGET;
        public static final INVOKEKIND INVOKE_PROPERTYPUT;
        public static final INVOKEKIND INVOKE_PROPERTYPUTREF;
        public int value;
        
        public INVOKEKIND() {
        }
        
        public INVOKEKIND(final int value) {
            this.value = value;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        static {
            INVOKE_FUNC = new INVOKEKIND(1);
            INVOKE_PROPERTYGET = new INVOKEKIND(2);
            INVOKE_PROPERTYPUT = new INVOKEKIND(4);
            INVOKE_PROPERTYPUTREF = new INVOKEKIND(8);
        }
        
        public static class ByReference extends INVOKEKIND implements Structure.ByReference
        {
        }
    }
    
    public static class CALLCONV extends Structure
    {
        public static final int CC_FASTCALL = 0;
        public static final int CC_CDECL = 1;
        public static final int CC_MSCPASCAL = 2;
        public static final int CC_PASCAL = 2;
        public static final int CC_MACPASCAL = 3;
        public static final int CC_STDCALL = 4;
        public static final int CC_FPFASTCALL = 5;
        public static final int CC_SYSCALL = 6;
        public static final int CC_MPWCDECL = 7;
        public static final int CC_MPWPASCAL = 8;
        public static final int CC_MAX = 9;
        public int value;
        
        public CALLCONV() {
        }
        
        public CALLCONV(final int value) {
            this.value = value;
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends CALLCONV implements Structure.ByReference
        {
        }
    }
    
    public static class VARKIND extends Structure
    {
        public static final int VAR_PERINSTANCE = 0;
        public static final int VAR_STATIC = 1;
        public static final int VAR_CONST = 2;
        public static final int VAR_DISPATCH = 3;
        public int value;
        
        public VARKIND() {
        }
        
        public VARKIND(final int value) {
            this.value = value;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("value");
        }
        
        public static class ByReference extends VARKIND implements Structure.ByReference
        {
        }
    }
    
    public static class TYPEDESC extends Structure
    {
        public _TYPEDESC _typedesc;
        public WTypes.VARTYPE vt;
        
        public TYPEDESC() {
            this.read();
        }
        
        public TYPEDESC(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        public TYPEDESC(final _TYPEDESC _typedesc, final WTypes.VARTYPE vt) {
            this._typedesc = _typedesc;
            this.vt = vt;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("_typedesc", "vt");
        }
        
        public static class ByReference extends TYPEDESC implements Structure.ByReference
        {
        }
        
        public static class _TYPEDESC extends Union
        {
            public TYPEDESC.ByReference lptdesc;
            public ARRAYDESC.ByReference lpadesc;
            public HREFTYPE hreftype;
            
            public _TYPEDESC() {
                this.setType("hreftype");
                this.read();
            }
            
            public _TYPEDESC(final Pointer pointer) {
                super(pointer);
                this.setType("hreftype");
                this.read();
            }
            
            public TYPEDESC.ByReference getLptdesc() {
                this.setType("lptdesc");
                this.read();
                return this.lptdesc;
            }
            
            public ARRAYDESC.ByReference getLpadesc() {
                this.setType("lpadesc");
                this.read();
                return this.lpadesc;
            }
            
            public HREFTYPE getHreftype() {
                this.setType("hreftype");
                this.read();
                return this.hreftype;
            }
        }
    }
    
    public static class IDLDESC extends Structure
    {
        public BaseTSD.ULONG_PTR dwReserved;
        public WinDef.USHORT wIDLFlags;
        
        public IDLDESC() {
        }
        
        public IDLDESC(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        public IDLDESC(final BaseTSD.ULONG_PTR dwReserved, final WinDef.USHORT wIDLFlags) {
            this.dwReserved = dwReserved;
            this.wIDLFlags = wIDLFlags;
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("dwReserved", "wIDLFlags");
        }
        
        public static class ByReference extends IDLDESC implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final IDLDESC idldesc) {
                super(idldesc.dwReserved, idldesc.wIDLFlags);
            }
        }
    }
    
    public static class ARRAYDESC extends Structure
    {
        public TYPEDESC tdescElem;
        public short cDims;
        public SAFEARRAYBOUND[] rgbounds;
        
        public ARRAYDESC() {
            this.rgbounds = new SAFEARRAYBOUND[] { new SAFEARRAYBOUND() };
        }
        
        public ARRAYDESC(final Pointer pointer) {
            super(pointer);
            this.rgbounds = new SAFEARRAYBOUND[] { new SAFEARRAYBOUND() };
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("tdescElem", "cDims", "rgbounds");
        }
        
        public ARRAYDESC(final TYPEDESC tdescElem, final short cDims, final SAFEARRAYBOUND[] rgbounds) {
            this.rgbounds = new SAFEARRAYBOUND[] { new SAFEARRAYBOUND() };
            this.tdescElem = tdescElem;
            this.cDims = cDims;
            if (rgbounds.length != this.rgbounds.length) {
                throw new IllegalArgumentException("Wrong array size !");
            }
            this.rgbounds = rgbounds;
        }
        
        public static class ByReference extends ARRAYDESC implements Structure.ByReference
        {
        }
    }
    
    public static class PARAMDESC extends Structure
    {
        public Pointer pparamdescex;
        public WinDef.USHORT wParamFlags;
        
        public PARAMDESC() {
        }
        
        public PARAMDESC(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("pparamdescex", "wParamFlags");
        }
        
        public static class ByReference extends PARAMDESC implements Structure.ByReference
        {
        }
    }
    
    public static class PARAMDESCEX extends Structure
    {
        public WinDef.ULONG cBytes;
        public Variant.VariantArg varDefaultValue;
        
        public PARAMDESCEX() {
        }
        
        public PARAMDESCEX(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("cBytes", "varDefaultValue");
        }
        
        public static class ByReference extends PARAMDESCEX implements Structure.ByReference
        {
        }
    }
    
    public static class HREFTYPE extends WinDef.DWORD
    {
        public HREFTYPE() {
        }
        
        public HREFTYPE(final long value) {
            super(value);
        }
    }
    
    public static class HREFTYPEByReference extends WinDef.DWORDByReference
    {
        public HREFTYPEByReference() {
            this(new HREFTYPE(0L));
        }
        
        public HREFTYPEByReference(final WinDef.DWORD value) {
            super(value);
        }
        
        public void setValue(final HREFTYPE value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        @Override
        public HREFTYPE getValue() {
            return new HREFTYPE((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class TYPEATTR extends Structure
    {
        public Guid.GUID guid;
        public WinDef.LCID lcid;
        public WinDef.DWORD dwReserved;
        public MEMBERID memidConstructor;
        public MEMBERID memidDestructor;
        public WTypes.LPOLESTR lpstrSchema;
        public WinDef.ULONG cbSizeInstance;
        public TYPEKIND typekind;
        public WinDef.WORD cFuncs;
        public WinDef.WORD cVars;
        public WinDef.WORD cImplTypes;
        public WinDef.WORD cbSizeVft;
        public WinDef.WORD cbAlignment;
        public WinDef.WORD wTypeFlags;
        public WinDef.WORD wMajorVerNum;
        public WinDef.WORD wMinorVerNum;
        public TYPEDESC tdescAlias;
        public IDLDESC idldescType;
        
        public TYPEATTR() {
        }
        
        public TYPEATTR(final Pointer pointer) {
            super(pointer);
            this.read();
        }
        
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("guid", "lcid", "dwReserved", "memidConstructor", "memidDestructor", "lpstrSchema", "cbSizeInstance", "typekind", "cFuncs", "cVars", "cImplTypes", "cbSizeVft", "cbAlignment", "wTypeFlags", "wMajorVerNum", "wMinorVerNum", "tdescAlias", "idldescType");
        }
        
        public static class ByReference extends TYPEATTR implements Structure.ByReference
        {
        }
    }
}
