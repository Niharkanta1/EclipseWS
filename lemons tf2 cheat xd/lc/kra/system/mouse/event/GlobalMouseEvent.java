// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.mouse.event;

import java.util.EventObject;

public class GlobalMouseEvent extends EventObject
{
    private static final long serialVersionUID = -8194688548489965445L;
    public static final int TS_UP = 0;
    public static final int TS_DOWN = 1;
    public static final int TS_MOVE = 2;
    public static final int TS_WHEEL = 3;
    public static final int BUTTON_NO = 0;
    public static final int BUTTON_LEFT = 1;
    public static final int BUTTON_RIGHT = 2;
    public static final int BUTTON_MIDDLE = 16;
    public static final int WHEEL_DELTA = 120;
    private int transitionState;
    private int button;
    private int buttons;
    private int x;
    private int y;
    private int delta;
    private long deviceHandle;
    
    public GlobalMouseEvent(final Object source, final int transitionState, final int button, final int buttons, final int x, final int y, final int delta, final long deviceHandle) {
        super(source);
        this.transitionState = transitionState;
        this.button = button;
        this.buttons = buttons;
        this.x = x;
        this.y = y;
        this.delta = delta;
        this.deviceHandle = deviceHandle;
    }
    
    public int getTransitionState() {
        return this.transitionState;
    }
    
    public int getButton() {
        return this.button;
    }
    
    public int getButtons() {
        return this.buttons;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getDelta() {
        return this.delta;
    }
    
    public long getDeviceHandle() {
        return this.deviceHandle;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder().append(this.x).append(',').append(this.y);
        if (this.buttons != 0 || this.transitionState == 3) {
            builder.append(" [");
            if ((this.buttons & 0x1) != 0x0) {
                builder.append("left,");
            }
            if ((this.buttons & 0x2) != 0x0) {
                builder.append("right,");
            }
            if ((this.buttons & 0x10) != 0x0) {
                builder.append("middle,");
            }
            if (this.transitionState == 3) {
                builder.append("delta ").append(this.delta).append(',');
            }
            return builder.deleteCharAt(builder.length() - 1).append(']').toString();
        }
        return builder.toString();
    }
}
