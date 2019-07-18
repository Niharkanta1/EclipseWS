// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware;

import me.lemon.lemonware.utils.GlowUtil;
import lc.kra.system.mouse.event.GlobalMouseListener;
import me.lemon.lemonware.tf2.LocalPlayer;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import me.lemon.lemonware.feature.Feature;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import java.util.Collection;
import java.util.Arrays;
import me.lemon.lemonware.memory.Offset;
import me.lemon.lemonware.utils.Gui;
import me.lemon.lemonware.tf2.Entities;
import me.lemon.lemonware.feature.FeatureManager;
import me.lemon.lemonware.memory.Memory;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;

public enum Main
{
    INSTANCE("INSTANCE", 0);
    
    private NativeProcess TF2Process;
    private Module engine;
    private Module client;
    private Memory memory;
    private FeatureManager featureManager;
    private Entities entityList;
    private Gui gui;
    public long lastUpdate;
    
    private Main(final String s, final int n) {
    }
    
    public static void main(final String[] args) {
        Main.INSTANCE.lastUpdate = System.currentTimeMillis();
        System.out.println("Searching for TF2 process...");
        Main.INSTANCE.TF2Process = NativeProcess.byName("hl2.exe");
        System.out.println("Found TF2, (id: " + Main.INSTANCE.getTF2Process().id() + "), Searching for Engine DLL...");
        Main.INSTANCE.engine = Main.INSTANCE.getTF2Process().findModule("engine.dll");
        System.out.println("Found Engine DLL, (id: " + Main.INSTANCE.getEngine().address() + "), Searching for Client DLL...");
        Main.INSTANCE.client = Main.INSTANCE.getTF2Process().findModule("client.dll");
        System.out.println("Found Client DLL, (id: " + Main.INSTANCE.getClient().address() + ")");
        Main.INSTANCE.memory = new Memory() {
            @Override
            public void addOffsets() {
                System.out.println("Adding Offsets...");
                this.getOffsets().addAll(Arrays.asList(new Offset("m_dwEntityList", 12852508), new Offset("m_dwLocalPlayer", 12796432), new Offset("m_dwBoneMatrix", 1456), new Offset("m_dwViewAngles", 4805732), new Offset("m_dwGlowObject", 12577276), new Offset("m_dwViewMatrix", 5912744), new Offset("m_iTeamNum", 176), new Offset("m_bDormant", 426), new Offset("m_vecViewOffset", 252), new Offset("m_fFlags", 892), new Offset("m_iHealth", 168), new Offset("m_vecVelocity", 288), new Offset("m_vecOrigin", 868), new Offset("m_bGlowEnabled", 3517), new Offset("m_iCrosshairID", 6012), new Offset("m_iCloaked", 6584)));
            }
        };
        System.out.println("Initializing cheat features...");
        Main.INSTANCE.featureManager = new FeatureManager();
        System.out.println("Creating entity list...");
        Main.INSTANCE.entityList = new Entities();
        System.out.println("Creating GUI...");
        Main.INSTANCE.gui = new Gui();
        System.out.println("Creating a keyboard hook...");
        final GlobalKeyboardHook keyboard = new GlobalKeyboardHook(true);
        System.out.println("Adding key listeners...");
        keyboard.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(final GlobalKeyEvent event) {
                Main.INSTANCE.getFeatureManager().getFeatures().stream().filter(Feature::isEnabled).forEach(feature -> feature.onKeyPress(event));
            }
            
            @Override
            public void keyReleased(final GlobalKeyEvent event) {
                Main.INSTANCE.getFeatureManager().getFeatures().stream().filter(Feature::isEnabled).forEach(feature -> feature.onKeyRelease(event));
            }
        });
        System.out.println("Creating a mouse hook...");
        final GlobalMouseHook mouse = new GlobalMouseHook();
        System.out.println("Adding mouse listener...");
        mouse.addMouseListener(new GlobalMouseAdapter() {
            @Override
            public void mousePressed(final GlobalMouseEvent event) {
                final LocalPlayer local = Main.INSTANCE.getEntityList().getLocalPlayer();
                if (local != null && local.getHp() > 0L) {
                    Main.INSTANCE.getFeatureManager().getFeatures().stream().filter(Feature::isEnabled).forEach(feature -> feature.onMousePress(event));
                }
            }
            
            @Override
            public void mouseReleased(final GlobalMouseEvent event) {
                final LocalPlayer local = Main.INSTANCE.getEntityList().getLocalPlayer();
                if (local != null && local.getHp() > 0L) {
                    Main.INSTANCE.getFeatureManager().getFeatures().stream().filter(Feature::isEnabled).forEach(feature -> feature.onMouseRelease(event));
                }
            }
        });
        System.out.println("Cheat started successfully!");
        while (true) {
            Main.INSTANCE.getEntityList().findEntities();
            final LocalPlayer local = Main.INSTANCE.getEntityList().getLocalPlayer();
            if (local != null && local.getHp() > 0L) {
                Main.INSTANCE.getFeatureManager().getFeatures().stream().filter(Feature::isEnabled).forEach(feature -> feature.run(local));
                GlowUtil.doGlow(local);
            }
        }
    }
    
    public NativeProcess getTF2Process() {
        return this.TF2Process;
    }
    
    public Module getEngine() {
        return this.engine;
    }
    
    public Module getClient() {
        return this.client;
    }
    
    public Memory getMemory() {
        return this.memory;
    }
    
    public FeatureManager getFeatureManager() {
        return this.featureManager;
    }
    
    public Entities getEntityList() {
        return this.entityList;
    }
    
    public Gui getGui() {
        return this.gui;
    }
}
