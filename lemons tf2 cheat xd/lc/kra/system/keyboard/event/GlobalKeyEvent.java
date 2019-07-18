// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.keyboard.event;

import java.util.EventObject;

public class GlobalKeyEvent extends EventObject
{
    private static final long serialVersionUID = -8194688548489965445L;
    public static final int TS_UP = 0;
    public static final int TS_DOWN = 1;
    public static final int VK_UNDEFINED = 0;
    public static final int VK_LBUTTON = 1;
    public static final int VK_RBUTTON = 2;
    public static final int VK_CANCEL = 3;
    public static final int VK_MBUTTON = 4;
    public static final int VK_XBUTTON1 = 5;
    public static final int VK_XBUTTON2 = 6;
    public static final int VK_BACK = 8;
    public static final int VK_TAB = 9;
    public static final int VK_CLEAR = 12;
    public static final int VK_RETURN = 13;
    public static final int VK_SHIFT = 16;
    public static final int VK_CONTROL = 17;
    public static final int VK_MENU = 18;
    public static final int VK_PAUSE = 19;
    public static final int VK_CAPITAL = 20;
    public static final int VK_KANA = 21;
    public static final int VK_HANGUEL = 21;
    public static final int VK_HANGUL = 21;
    public static final int VK_JUNJA = 23;
    public static final int VK_FINAL = 24;
    public static final int VK_HANJA = 25;
    public static final int VK_KANJI = 25;
    public static final int VK_ESCAPE = 27;
    public static final int VK_CONVERT = 28;
    public static final int VK_NONCONVERT = 29;
    public static final int VK_ACCEPT = 30;
    public static final int VK_MODECHANGE = 31;
    public static final int VK_SPACE = 32;
    public static final int VK_PRIOR = 33;
    public static final int VK_NEXT = 34;
    public static final int VK_END = 35;
    public static final int VK_HOME = 36;
    public static final int VK_LEFT = 37;
    public static final int VK_UP = 38;
    public static final int VK_RIGHT = 39;
    public static final int VK_DOWN = 40;
    public static final int VK_SELECT = 41;
    public static final int VK_PRINT = 42;
    public static final int VK_EXECUTE = 43;
    public static final int VK_SNAPSHOT = 44;
    public static final int VK_INSERT = 45;
    public static final int VK_DELETE = 46;
    public static final int VK_HELP = 47;
    public static final int VK_0 = 48;
    public static final int VK_1 = 49;
    public static final int VK_2 = 50;
    public static final int VK_3 = 51;
    public static final int VK_4 = 52;
    public static final int VK_5 = 53;
    public static final int VK_6 = 54;
    public static final int VK_7 = 55;
    public static final int VK_8 = 56;
    public static final int VK_9 = 57;
    public static final int VK_A = 65;
    public static final int VK_B = 66;
    public static final int VK_C = 67;
    public static final int VK_D = 68;
    public static final int VK_E = 69;
    public static final int VK_F = 70;
    public static final int VK_G = 71;
    public static final int VK_H = 72;
    public static final int VK_I = 73;
    public static final int VK_J = 74;
    public static final int VK_K = 75;
    public static final int VK_L = 76;
    public static final int VK_M = 77;
    public static final int VK_N = 78;
    public static final int VK_O = 79;
    public static final int VK_P = 80;
    public static final int VK_Q = 81;
    public static final int VK_R = 82;
    public static final int VK_S = 83;
    public static final int VK_T = 84;
    public static final int VK_U = 85;
    public static final int VK_V = 86;
    public static final int VK_W = 87;
    public static final int VK_X = 88;
    public static final int VK_Y = 89;
    public static final int VK_Z = 90;
    public static final int VK_LWIN = 91;
    public static final int VK_RWIN = 92;
    public static final int VK_APPS = 93;
    public static final int VK_SLEEP = 95;
    public static final int VK_NUMPAD0 = 96;
    public static final int VK_NUMPAD1 = 97;
    public static final int VK_NUMPAD2 = 98;
    public static final int VK_NUMPAD3 = 99;
    public static final int VK_NUMPAD4 = 100;
    public static final int VK_NUMPAD5 = 101;
    public static final int VK_NUMPAD6 = 102;
    public static final int VK_NUMPAD7 = 103;
    public static final int VK_NUMPAD8 = 104;
    public static final int VK_NUMPAD9 = 105;
    public static final int VK_MULTIPLY = 106;
    public static final int VK_ADD = 107;
    public static final int VK_SEPARATOR = 108;
    public static final int VK_SUBTRACT = 109;
    public static final int VK_DECIMAL = 110;
    public static final int VK_DIVIDE = 111;
    public static final int VK_F1 = 112;
    public static final int VK_F2 = 113;
    public static final int VK_F3 = 114;
    public static final int VK_F4 = 115;
    public static final int VK_F5 = 116;
    public static final int VK_F6 = 117;
    public static final int VK_F7 = 118;
    public static final int VK_F8 = 119;
    public static final int VK_F9 = 120;
    public static final int VK_F10 = 121;
    public static final int VK_F11 = 122;
    public static final int VK_F12 = 123;
    public static final int VK_F13 = 124;
    public static final int VK_F14 = 125;
    public static final int VK_F15 = 126;
    public static final int VK_F16 = 127;
    public static final int VK_F17 = 128;
    public static final int VK_F18 = 129;
    public static final int VK_F19 = 130;
    public static final int VK_F20 = 131;
    public static final int VK_F21 = 132;
    public static final int VK_F22 = 133;
    public static final int VK_F23 = 134;
    public static final int VK_F24 = 135;
    public static final int VK_NUMLOCK = 144;
    public static final int VK_SCROLL = 145;
    public static final int VK_LSHIFT = 160;
    public static final int VK_RSHIFT = 161;
    public static final int VK_LCONTROL = 162;
    public static final int VK_RCONTROL = 163;
    public static final int VK_LMENU = 164;
    public static final int VK_RMENU = 165;
    public static final int VK_BROWSER_BACK = 166;
    public static final int VK_BROWSER_FORWARD = 167;
    public static final int VK_BROWSER_REFRESH = 168;
    public static final int VK_BROWSER_STOP = 169;
    public static final int VK_BROWSER_SEARCH = 170;
    public static final int VK_BROWSER_FAVORITES = 171;
    public static final int VK_BROWSER_HOME = 172;
    public static final int VK_VOLUME_MUTE = 173;
    public static final int VK_VOLUME_DOWN = 174;
    public static final int VK_VOLUME_UP = 175;
    public static final int VK_MEDIA_NEXT_TRACK = 176;
    public static final int VK_MEDIA_PREV_TRACK = 177;
    public static final int VK_MEDIA_STOP = 178;
    public static final int VK_MEDIA_PLAY_PAUSE = 179;
    public static final int VK_LAUNCH_MAIL = 180;
    public static final int VK_LAUNCH_MEDIA_SELECT = 181;
    public static final int VK_LAUNCH_APP1 = 182;
    public static final int VK_LAUNCH_APP2 = 183;
    public static final int VK_OEM_1 = 186;
    public static final int VK_OEM_PLUS = 187;
    public static final int VK_OEM_COMMA = 188;
    public static final int VK_OEM_MINUS = 189;
    public static final int VK_OEM_PERIOD = 190;
    public static final int VK_OEM_2 = 191;
    public static final int VK_OEM_3 = 192;
    public static final int VK_OEM_4 = 219;
    public static final int VK_OEM_5 = 220;
    public static final int VK_OEM_6 = 221;
    public static final int VK_OEM_7 = 222;
    public static final int VK_OEM_8 = 223;
    public static final int VK_OEM_102 = 226;
    public static final int VK_PROCESSKEY = 229;
    public static final int VK_PACKET = 231;
    public static final int VK_ATTN = 246;
    public static final int VK_CRSEL = 247;
    public static final int VK_EXSEL = 248;
    public static final int VK_EREOF = 249;
    public static final int VK_PLAY = 250;
    public static final int VK_ZOOM = 251;
    public static final int VK_NONAME = 252;
    public static final int VK_PA1 = 253;
    public static final int VK_OEM_CLEAR = 254;
    private int virtualKeyCode;
    private int transitionState;
    private char keyChar;
    private boolean menuPressed;
    private boolean shiftPressed;
    private boolean controlPressed;
    private boolean extendedKey;
    private long deviceHandle;
    
    public GlobalKeyEvent(final Object source, final int virtualKeyCode, final int transitionState, final char keyChar, final boolean menuPressed, final boolean shiftPressed, final boolean controlPressed, final boolean extendedKey, final long deviceHandle) {
        super(source);
        this.virtualKeyCode = virtualKeyCode;
        this.transitionState = transitionState;
        this.keyChar = keyChar;
        this.menuPressed = menuPressed;
        this.shiftPressed = shiftPressed;
        this.controlPressed = controlPressed;
        this.extendedKey = extendedKey;
        this.deviceHandle = deviceHandle;
    }
    
    public int getVirtualKeyCode() {
        return this.virtualKeyCode;
    }
    
    public char getKeyChar() {
        return this.keyChar;
    }
    
    public int getTransitionState() {
        return this.transitionState;
    }
    
    public boolean isMenuPressed() {
        return this.menuPressed;
    }
    
    public boolean isShiftPressed() {
        return this.shiftPressed;
    }
    
    public boolean isControlPressed() {
        return this.controlPressed;
    }
    
    public boolean isExtendedKey() {
        return this.extendedKey;
    }
    
    public long getDeviceHandle() {
        return this.deviceHandle;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder().append(this.virtualKeyCode);
        builder.append(" [").append((this.transitionState == 1) ? "down" : "up");
        if (this.menuPressed) {
            builder.append(",menu");
        }
        if (this.shiftPressed) {
            builder.append(",shift");
        }
        if (this.controlPressed) {
            builder.append(",control");
        }
        if (this.extendedKey) {
            builder.append(",extended");
        }
        return builder.append(']').toString();
    }
}
