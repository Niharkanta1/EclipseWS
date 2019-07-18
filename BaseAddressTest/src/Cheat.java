import java.util.Arrays;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.*;

public class Cheat
{
	static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
    static User32     user32 = (User32)   Native.loadLibrary("user32"  , User32.class);
    public static void main(String[] args)
    {
        int pid = getProcessId("Notepad"); // get our process ID
        Pointer readprocess = openProcess(0x0010, pid); // open the process ID with read priviledges.
       
        int size = 4; // we want to read 4 bytes
        Memory read = readMemory(readprocess,0x00AB0C62,size); // read 4 bytes of memory starting at the address 0x00AB0C62.
        System.out.println(read.getInt(0)); // print out the value!
    }
   
    public static int getProcessId(String window)
    {
        IntByReference pid = new IntByReference(0);
        user32.GetWindowThreadProcessId(user32.FindWindowA(null,window), pid);
       
        return pid.getValue();
    }
   
    public static Pointer openProcess(int permissions, int pid)
    {
        Pointer process = kernel32.OpenProcess(permissions,true, pid);
        return process;
    }
   
    public static Memory readMemory(Pointer process, int address, int bytesToRead)
    {
        IntByReference read = new IntByReference(0);
        Memory output = new Memory(bytesToRead);
        kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);
        return output;
    }
    
    public int getBaseAddress(Pointer pointer) {
        try {
                Pointer hProcess = pointer;
                List<Module> hModules = PsapiHandler.getInstance().EnumProcessModules(hProcess);

                for(Module m: hModules){
                        if(m.getFileName().contains(exeName)){
                                misc.log(m.getFileName() + ": 0x" + Long.toHexString(Pointer.nativeValue(m.getEntryPoint())));
                                return Integer.valueOf("" + Pointer.nativeValue(m.getLpBaseOfDll()));
                        }
                }
        } catch (Exception e) {  e.printStackTrace(); }
        return -1;
}
}