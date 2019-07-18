// 
// Decompiled by Procyon v0.5.36
// 

package lc.kra.system.mouse;

import java.util.Map;
import java.util.Iterator;
import lc.kra.system.LibraryLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import lc.kra.system.mouse.event.GlobalMouseListener;
import java.util.List;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import java.util.concurrent.BlockingQueue;

public class GlobalMouseHook
{
    private static final int STATUS_SUCCESS = 0;
    private NativeMouseHook mouseHook;
    private BlockingQueue<GlobalMouseEvent> inputBuffer;
    private int buttons;
    private List<GlobalMouseListener> listeners;
    private Thread eventDispatcher;
    
    public GlobalMouseHook() throws UnsatisfiedLinkError {
        this(false);
    }
    
    public GlobalMouseHook(final boolean raw) throws UnsatisfiedLinkError {
        this.inputBuffer = new LinkedBlockingQueue<GlobalMouseEvent>();
        this.buttons = 0;
        this.listeners = new CopyOnWriteArrayList<GlobalMouseListener>();
        this.eventDispatcher = new Thread() {
            {
                this.setName("Global Mouse Hook Dispatcher");
                this.setDaemon(true);
            }
            
            @Override
            public void run() {
                try {
                    while (GlobalMouseHook.this.isAlive()) {
                        final GlobalMouseEvent event = GlobalMouseHook.this.inputBuffer.take();
                        switch (event.getTransitionState()) {
                            case 0: {
                                GlobalMouseHook.this.mouseReleased(event);
                                continue;
                            }
                            case 1: {
                                GlobalMouseHook.this.mousePressed(event);
                                continue;
                            }
                            case 2: {
                                GlobalMouseHook.this.mouseMoved(event);
                                continue;
                            }
                            case 3: {
                                GlobalMouseHook.this.mouseWheel(event);
                                continue;
                            }
                        }
                    }
                }
                catch (InterruptedException ex) {}
            }
        };
        LibraryLoader.loadLibrary();
        this.mouseHook = new NativeMouseHook(raw) {
            @Override
            public void handleMouse(final int transitionState, final int button, final int x, final int y, final int delta, final long deviceHandle) {
                GlobalMouseHook.this.inputBuffer.add(new GlobalMouseEvent(this, transitionState, button, GlobalMouseHook.this.buttons ^= button, x, y, delta, deviceHandle));
            }
        };
        this.eventDispatcher.start();
    }
    
    public void addMouseListener(final GlobalMouseListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeMouseListener(final GlobalMouseListener listener) {
        this.listeners.remove(listener);
    }
    
    private void mousePressed(final GlobalMouseEvent event) {
        for (final GlobalMouseListener listener : this.listeners) {
            listener.mousePressed(event);
        }
    }
    
    private void mouseReleased(final GlobalMouseEvent event) {
        for (final GlobalMouseListener listener : this.listeners) {
            listener.mouseReleased(event);
        }
    }
    
    private void mouseMoved(final GlobalMouseEvent event) {
        for (final GlobalMouseListener listener : this.listeners) {
            listener.mouseMoved(event);
        }
    }
    
    private void mouseWheel(final GlobalMouseEvent event) {
        for (final GlobalMouseListener listener : this.listeners) {
            listener.mouseWheel(event);
        }
    }
    
    public boolean isAlive() {
        return this.mouseHook != null && this.mouseHook.isAlive();
    }
    
    public void shutdownHook() {
        if (this.isAlive()) {
            this.mouseHook.unregisterHook();
            try {
                this.mouseHook.join();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static Map<Long, String> listMice() throws UnsatisfiedLinkError {
        LibraryLoader.loadLibrary();
        return NativeMouseHook.listDevices();
    }
    
    private abstract static class NativeMouseHook extends Thread
    {
        private int status;
        private boolean raw;
        
        public NativeMouseHook(final boolean raw) {
            super("Global Mouse Hook Thread");
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
                    throw new RuntimeException("Low-level mouse hook failed (" + this.status + ")");
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
        
        public abstract void handleMouse(final int p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    }
}
