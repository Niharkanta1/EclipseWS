import com.charlatano.settings.*
import  com.sun.jna.platform.win32.WinNT;

MAX_ENTITIES = 4096
CLEANUP_TIME = 10_000
GARBAGE_COLLECT_ON_MAP_START = true
PROCESS_NAME ="notepad.exe"
WINDOW_NAME = "Untitled - Notepad"
PROCESS_ACCESS_FLAGS = WinNT.PROCESS_QUERY_INFORMATION or WinNT.PROCESS_VM_READ