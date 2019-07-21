package com.charlatano.settings

import com.sun.jna.platform.win32.WinNT

var MAX_ENTITIES = 4096
var CLEANUP_TIME = 10_000
var GARBAGE_COLLECT_ON_MAP_START = true
var PROCESS_NAME = "notepad.exe"
var WINDOW_NAME = "Untitled - Notepad"
var PROCESS_ACCESS_FLAGS = WinNT.PROCESS_QUERY_INFORMATION or WinNT.PROCESS_VM_READ