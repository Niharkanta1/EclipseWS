package application;
import application.User32;  
import application.Kernel32;  
import com.sun.jna.Memory;  
import com.sun.jna.Native;  
import com.sun.jna.Pointer;  
import com.sun.jna.ptr.IntByReference;

public class ProcessHelper {
	static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);  
    static User32 user32 = (User32) Native.loadLibrary("user32", User32.class);  
    static int readRight = 0x0010;  
    static int writeRight = 0x0020;  
    //static int PROCESS_VM_OPERATION = 0x0008; 
    
    public static void startProcessHelper() {
    	//Read Memory  
        //MineSweeper = Campo Minado  
        int pid = getProcessId("Campo Minado"); // get our process ID  
        System.out.println("Pid = " + pid);  
  
  
        Pointer readprocess = openProcess(readRight, pid); // open the process ID with read priviledges.  
        Pointer writeprocess = openProcess(writeRight, pid);  
  
  
        int size = 4; // we want to read 4 bytes  
        int address = 0x004053C8;  
  
  
        //Read Memory  
        Memory read = readMemory(readprocess, address, size); // read 4 bytes of memory starting at the address 0x00AB0C62.  
        System.out.println(read.getInt(0)); // print out the value!         
        //Write Memory  
        int writeMemory = writeMemory(writeprocess, address, new short[0x22222222]);  
        System.out.println("WriteMemory :" + writeMemory);  
          
        Memory readM = readMemory(readprocess, address, size);  
        System.out.println(readM.getInt(0));  
    }  
  
  
    public static int writeMemory(Pointer process, int address, short[] data) {  
        IntByReference written = new IntByReference(0);  
  
  
        Memory toWrite = new Memory(data.length);  
  
  
        for (long i = 0; i < data.length; i++) {  
            toWrite.setShort(0, data[new Integer(Long.toString(i))]);  
        }  
  
  
        boolean b = kernel32.WriteProcessMemory(process, address, toWrite, data.length, written);  
        System.out.println("kernel32.WriteProcessMemory : " + b); // Retorna false  
        return written.getValue();  
    }
    
    public static Pointer openProcess(int permissions, int pid) {  
        Pointer process = kernel32.OpenProcess(permissions, true, pid);  
        return process;  
    }  
  
  
    public static int getProcessId(String window) {  
        IntByReference pid = new IntByReference(0);  
        user32.GetWindowThreadProcessId(user32.FindWindowA(null, window), pid);  
  
  
        return pid.getValue();  
    }  
  
  
    public static Memory readMemory(Pointer process, int address, int bytesToRead) {  
        IntByReference read = new IntByReference(0);  
        Memory output = new Memory(bytesToRead);  
  
  
        kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);  
        return output;  
    }  

}
