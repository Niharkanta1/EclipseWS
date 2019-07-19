import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT.HANDLE;

/*
 * http://msdn.microsoft.com/en-us/library/ms684229(VS.85).aspx
 */

public class LPMODULEINFO extends Structure {
            public HANDLE lpBaseOfDll;
            public int  SizeOfImage;
            public HANDLE EntryPoint;
            
			@Override
			protected List getFieldOrder() {
				return Arrays.asList(new String[] { "lpBaseOfDll", "SizeOfImage", "EntryPoint"});
			}
}