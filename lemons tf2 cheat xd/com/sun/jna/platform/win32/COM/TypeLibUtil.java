// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.OleAuto;

public class TypeLibUtil
{
    public static final OleAuto OLEAUTO;
    private ITypeLib typelib;
    private WinDef.LCID lcid;
    private String name;
    private String docString;
    private int helpContext;
    private String helpFile;
    
    public TypeLibUtil(final String clsidStr, final int wVerMajor, final int wVerMinor) {
        this.lcid = Kernel32.INSTANCE.GetUserDefaultLCID();
        final Guid.CLSID.ByReference clsid = new Guid.CLSID.ByReference();
        WinNT.HRESULT hr = Ole32.INSTANCE.CLSIDFromString(new WString(clsidStr), clsid);
        COMUtils.checkRC(hr);
        final PointerByReference pTypeLib = new PointerByReference();
        hr = OleAuto.INSTANCE.LoadRegTypeLib(clsid, wVerMajor, wVerMinor, this.lcid, pTypeLib);
        COMUtils.checkRC(hr);
        this.typelib = new TypeLib(pTypeLib.getValue());
        this.initTypeLibInfo();
    }
    
    public TypeLibUtil(final String file) {
        this.lcid = Kernel32.INSTANCE.GetUserDefaultLCID();
        final PointerByReference pTypeLib = new PointerByReference();
        final WinNT.HRESULT hr = OleAuto.INSTANCE.LoadTypeLib(new WString(file), pTypeLib);
        COMUtils.checkRC(hr);
        this.typelib = new TypeLib(pTypeLib.getValue());
        this.initTypeLibInfo();
    }
    
    private void initTypeLibInfo() {
        final TypeLibDoc documentation = this.getDocumentation(-1);
        this.name = documentation.getName();
        this.docString = documentation.getDocString();
        this.helpContext = documentation.getHelpContext();
        this.helpFile = documentation.getHelpFile();
    }
    
    public int getTypeInfoCount() {
        return this.typelib.GetTypeInfoCount().intValue();
    }
    
    public OaIdl.TYPEKIND getTypeInfoType(final int index) {
        final OaIdl.TYPEKIND.ByReference typekind = new OaIdl.TYPEKIND.ByReference();
        final WinNT.HRESULT hr = this.typelib.GetTypeInfoType(new WinDef.UINT((long)index), typekind);
        COMUtils.checkRC(hr);
        return typekind;
    }
    
    public ITypeInfo getTypeInfo(final int index) {
        final PointerByReference ppTInfo = new PointerByReference();
        final WinNT.HRESULT hr = this.typelib.GetTypeInfo(new WinDef.UINT((long)index), ppTInfo);
        COMUtils.checkRC(hr);
        return new TypeInfo(ppTInfo.getValue());
    }
    
    public TypeInfoUtil getTypeInfoUtil(final int index) {
        return new TypeInfoUtil(this.getTypeInfo(index));
    }
    
    public OaIdl.TLIBATTR getLibAttr() {
        final PointerByReference ppTLibAttr = new PointerByReference();
        final WinNT.HRESULT hr = this.typelib.GetLibAttr(ppTLibAttr);
        COMUtils.checkRC(hr);
        return new OaIdl.TLIBATTR(ppTLibAttr.getValue());
    }
    
    public TypeComp GetTypeComp() {
        final PointerByReference ppTComp = new PointerByReference();
        final WinNT.HRESULT hr = this.typelib.GetTypeComp(ppTComp);
        COMUtils.checkRC(hr);
        return new TypeComp(ppTComp.getValue());
    }
    
    public TypeLibDoc getDocumentation(final int index) {
        final WTypes.BSTRByReference pBstrName = new WTypes.BSTRByReference();
        final WTypes.BSTRByReference pBstrDocString = new WTypes.BSTRByReference();
        final WinDef.DWORDByReference pdwHelpContext = new WinDef.DWORDByReference();
        final WTypes.BSTRByReference pBstrHelpFile = new WTypes.BSTRByReference();
        final WinNT.HRESULT hr = this.typelib.GetDocumentation(index, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile);
        COMUtils.checkRC(hr);
        final TypeLibDoc typeLibDoc = new TypeLibDoc(pBstrName.getString(), pBstrDocString.getString(), pdwHelpContext.getValue().intValue(), pBstrHelpFile.getString());
        TypeLibUtil.OLEAUTO.SysFreeString(pBstrName.getValue());
        TypeLibUtil.OLEAUTO.SysFreeString(pBstrDocString.getValue());
        TypeLibUtil.OLEAUTO.SysFreeString(pBstrHelpFile.getValue());
        return typeLibDoc;
    }
    
    public IsName IsName(final String nameBuf, final int hashVal) {
        final WTypes.LPOLESTR szNameBuf = new WTypes.LPOLESTR(nameBuf);
        final WinDef.ULONG lHashVal = new WinDef.ULONG((long)hashVal);
        final WinDef.BOOLByReference pfName = new WinDef.BOOLByReference();
        final WinNT.HRESULT hr = this.typelib.IsName(szNameBuf, lHashVal, pfName);
        COMUtils.checkRC(hr);
        return new IsName(szNameBuf.getValue(), pfName.getValue().booleanValue());
    }
    
    public FindName FindName(final String name, final int hashVal, short found) {
        final WTypes.BSTRByReference szNameBuf = new WTypes.BSTRByReference(OleAuto.INSTANCE.SysAllocString(name));
        final WinDef.ULONG lHashVal = new WinDef.ULONG((long)hashVal);
        final WinDef.USHORTByReference pcFound = new WinDef.USHORTByReference(found);
        WinNT.HRESULT hr = this.typelib.FindName(szNameBuf, lHashVal, null, null, pcFound);
        COMUtils.checkRC(hr);
        found = pcFound.getValue().shortValue();
        final ITypeInfo[] ppTInfo = new ITypeInfo[found];
        final OaIdl.MEMBERID[] rgMemId = new OaIdl.MEMBERID[found];
        hr = this.typelib.FindName(szNameBuf, lHashVal, ppTInfo, rgMemId, pcFound);
        COMUtils.checkRC(hr);
        final FindName findName = new FindName(szNameBuf.getString(), ppTInfo, rgMemId, found);
        TypeLibUtil.OLEAUTO.SysFreeString(szNameBuf.getValue());
        return findName;
    }
    
    public void ReleaseTLibAttr(final OaIdl.TLIBATTR pTLibAttr) {
        this.typelib.ReleaseTLibAttr(pTLibAttr);
    }
    
    public WinDef.LCID getLcid() {
        return this.lcid;
    }
    
    public ITypeLib getTypelib() {
        return this.typelib;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDocString() {
        return this.docString;
    }
    
    public long getHelpContext() {
        return this.helpContext;
    }
    
    public String getHelpFile() {
        return this.helpFile;
    }
    
    static {
        OLEAUTO = OleAuto.INSTANCE;
    }
    
    public static class TypeLibDoc
    {
        private String name;
        private String docString;
        private int helpContext;
        private String helpFile;
        
        public TypeLibDoc(final String name, final String docString, final int helpContext, final String helpFile) {
            this.name = name;
            this.docString = docString;
            this.helpContext = helpContext;
            this.helpFile = helpFile;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDocString() {
            return this.docString;
        }
        
        public int getHelpContext() {
            return this.helpContext;
        }
        
        public String getHelpFile() {
            return this.helpFile;
        }
    }
    
    public static class IsName
    {
        private String nameBuf;
        private boolean name;
        
        public IsName(final String nameBuf, final boolean name) {
            this.nameBuf = nameBuf;
            this.name = name;
        }
        
        public String getNameBuf() {
            return this.nameBuf;
        }
        
        public boolean isName() {
            return this.name;
        }
    }
    
    public static class FindName
    {
        private String nameBuf;
        private ITypeInfo[] pTInfo;
        private OaIdl.MEMBERID[] rgMemId;
        private short pcFound;
        
        public FindName(final String nameBuf, final ITypeInfo[] pTInfo, final OaIdl.MEMBERID[] rgMemId, final short pcFound) {
            this.nameBuf = nameBuf;
            this.pTInfo = pTInfo;
            this.rgMemId = rgMemId;
            this.pcFound = pcFound;
        }
        
        public String getNameBuf() {
            return this.nameBuf;
        }
        
        public ITypeInfo[] getTInfo() {
            return this.pTInfo;
        }
        
        public OaIdl.MEMBERID[] getMemId() {
            return this.rgMemId;
        }
        
        public short getFound() {
            return this.pcFound;
        }
    }
}
