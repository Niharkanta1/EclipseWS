// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.keyboard;

import java.util.Map;
import java.util.Iterator;
import lc.kra.system.LibraryLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import java.util.List;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import java.util.concurrent.BlockingQueue;

public class GlobalKeyboardHook
{
    private static final int STATUS_SUCCESS = 0;
    private NativeKeyboardHook keyboardHook;
    private BlockingQueue<GlobalKeyEvent> inputBuffer;
    private boolean menuPressed;
    private boolean shiftPressed;
    private boolean controlPressed;
    private boolean extendedKey;
    private List<GlobalKeyListener> listeners;
    private Thread eventDispatcher;
    
    public GlobalKeyboardHook() throws UnsatisfiedLinkError {
        this(false);
    }
    
    public GlobalKeyboardHook(final boolean raw) throws UnsatisfiedLinkError {
        this.inputBuffer = new LinkedBlockingQueue<GlobalKeyEvent>();
        this.listeners = new CopyOnWriteArrayList<GlobalKeyListener>();
        this.eventDispatcher = new Thread() {
            {
                this.setName("Global Keyboard Hook Dispatcher");
                this.setDaemon(true);
            }
            
            @Override
            public void run() {
                try {
                    while (GlobalKeyboardHook.this.isAlive()) {
                        final GlobalKeyEvent event = GlobalKeyboardHook.this.inputBuffer.take();
                        if (event.getTransitionState() == 1) {
                            GlobalKeyboardHook.this.keyPressed(event);
                        }
                        else {
                            GlobalKeyboardHook.this.keyReleased(event);
                        }
                    }
                }
                catch (InterruptedException ex) {}
            }
        };
        LibraryLoader.loadLibrary();
        this.keyboardHook = new NativeKeyboardHook(raw) {
            @Override
            public void handleKey(final int virtualKeyCode, final int transitionState, final char keyChar, final long deviceHandle) {
                GlobalKeyboardHook.this.switchControlKeys(virtualKeyCode, transitionState);
                GlobalKeyboardHook.this.inputBuffer.add(new GlobalKeyEvent(this, virtualKeyCode, transitionState, keyChar, GlobalKeyboardHook.this.menuPressed, GlobalKeyboardHook.this.shiftPressed, GlobalKeyboardHook.this.controlPressed, GlobalKeyboardHook.this.extendedKey, deviceHandle));
            }
        };
        this.eventDispatcher.start();
    }
    
    public void addKeyListener(final GlobalKeyListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeKeyListener(final GlobalKeyListener listener) {
        this.listeners.remove(listener);
    }
    
    private void keyPressed(final GlobalKeyEvent event) {
        for (final GlobalKeyListener listener : this.listeners) {
            listener.keyPressed(event);
        }
    }
    
    private void keyReleased(final GlobalKeyEvent event) {
        for (final GlobalKeyListener listener : this.listeners) {
            listener.keyReleased(event);
        }
    }
    
    public boolean isAlive() {
        return this.keyboardHook != null && this.keyboardHook.isAlive();
    }
    
    public void shutdownHook() {
        if (this.isAlive()) {
            this.keyboardHook.unregisterHook();
            try {
                this.keyboardHook.join();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static Map<Long, String> listKeyboards() throws UnsatisfiedLinkError {
        LibraryLoader.loadLibrary();
        return NativeKeyboardHook.listDevices();
    }
    
    private void switchControlKeys(final int virtualKeyCode, final int transitionState) {
        final boolean downTransition = transitionState == 1;
        switch (virtualKeyCode) {
            case 92: {
                this.extendedKey = downTransition;
                break;
            }
            case 165: {
                this.extendedKey = downTransition;
            }
            case 18:
            case 164: {
                this.menuPressed = downTransition;
                break;
            }
            case 161: {
                this.extendedKey = downTransition;
            }
            case 16:
            case 160: {
                this.shiftPressed = downTransition;
                break;
            }
            case 163: {
                this.extendedKey = downTransition;
            }
            case 17:
            case 162: {
                this.controlPressed = downTransition;
                break;
            }
        }
    }
    
    private abstract static class NativeKeyboardHook extends Thread
    {
        private int status;
        private boolean raw;
        
        public NativeKeyboardHook(final boolean raw) {
            super("Global Keyboard Hook Thread");
            this.setDaemon(false);
            this.setPriority(10);
            synchronized (this) {
                this.raw = raw;
                try {
                    this.start();
                    this.wait();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (this.status != 0) {
                    throw new RuntimeException("Low-level keyboard hook failed (" + this.status + ")");
                }
            }
        }
        
        @Override
        public void run() {
            this.status = this.registerHook(this.raw);
            synchronized (this) {
                this.notifyAll();
            }
        }
        
        public final native int registerHook(final boolean p0);
        
        public final native void unregisterHook();
        
        public static final native Map<Long, String> listDevices();
        
        public abstract void handleKey(final int p0, final int p1, final char p2, final long p3);
    }
}
