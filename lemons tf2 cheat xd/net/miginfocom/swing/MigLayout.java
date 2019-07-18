// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.swing;

import java.awt.event.ActionEvent;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamException;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.layout.BoundSize;
import java.awt.Insets;
import java.awt.Window;
import java.awt.LayoutManager;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import net.miginfocom.layout.LayoutUtil;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.util.Iterator;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.PlatformDefaults;
import java.util.HashMap;
import java.util.IdentityHashMap;
import net.miginfocom.layout.LayoutCallback;
import java.util.ArrayList;
import java.awt.Dimension;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import javax.swing.Timer;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import java.awt.Component;
import java.util.Map;
import java.io.Externalizable;
import java.awt.LayoutManager2;

public final class MigLayout implements LayoutManager2, Externalizable
{
    private final Map<Component, Object> scrConstrMap;
    private Object layoutConstraints;
    private Object colConstraints;
    private Object rowConstraints;
    private transient ContainerWrapper cacheParentW;
    private final transient Map<ComponentWrapper, CC> ccMap;
    private transient Timer debugTimer;
    private transient LC lc;
    private transient AC colSpecs;
    private transient AC rowSpecs;
    private transient Grid grid;
    private transient int lastModCount;
    private transient int lastHash;
    private transient Dimension lastInvalidSize;
    private transient boolean lastWasInvalid;
    private transient Dimension lastParentSize;
    private transient ArrayList<LayoutCallback> callbackList;
    private transient boolean dirty;
    private long lastSize;
    
    public MigLayout() {
        this("", "", "");
    }
    
    public MigLayout(final String s) {
        this(s, "", "");
    }
    
    public MigLayout(final String s, final String s2) {
        this(s, s2, "");
    }
    
    public MigLayout(final String layoutConstraints, final String columnConstraints, final String rowConstraints) {
        this.scrConstrMap = new IdentityHashMap<Component, Object>(8);
        this.layoutConstraints = "";
        this.colConstraints = "";
        this.rowConstraints = "";
        this.cacheParentW = null;
        this.ccMap = new HashMap<ComponentWrapper, CC>(8);
        this.debugTimer = null;
        this.lc = null;
        this.colSpecs = null;
        this.rowSpecs = null;
        this.grid = null;
        this.lastModCount = PlatformDefaults.getModCount();
        this.lastHash = -1;
        this.lastInvalidSize = null;
        this.lastWasInvalid = false;
        this.lastParentSize = null;
        this.callbackList = null;
        this.dirty = true;
        this.lastSize = 0L;
        this.setLayoutConstraints(layoutConstraints);
        this.setColumnConstraints(columnConstraints);
        this.setRowConstraints(rowConstraints);
    }
    
    public MigLayout(final LC lc) {
        this(lc, null, null);
    }
    
    public MigLayout(final LC lc, final AC ac) {
        this(lc, ac, null);
    }
    
    public MigLayout(final LC layoutConstraints, final AC columnConstraints, final AC rowConstraints) {
        this.scrConstrMap = new IdentityHashMap<Component, Object>(8);
        this.layoutConstraints = "";
        this.colConstraints = "";
        this.rowConstraints = "";
        this.cacheParentW = null;
        this.ccMap = new HashMap<ComponentWrapper, CC>(8);
        this.debugTimer = null;
        this.lc = null;
        this.colSpecs = null;
        this.rowSpecs = null;
        this.grid = null;
        this.lastModCount = PlatformDefaults.getModCount();
        this.lastHash = -1;
        this.lastInvalidSize = null;
        this.lastWasInvalid = false;
        this.lastParentSize = null;
        this.callbackList = null;
        this.dirty = true;
        this.lastSize = 0L;
        this.setLayoutConstraints(layoutConstraints);
        this.setColumnConstraints(columnConstraints);
        this.setRowConstraints(rowConstraints);
    }
    
    public Object getLayoutConstraints() {
        return this.layoutConstraints;
    }
    
    public void setLayoutConstraints(Object prepare) {
        if (prepare == null || prepare instanceof String) {
            prepare = ConstraintParser.prepare((String)prepare);
            this.lc = ConstraintParser.parseLayoutConstraint((String)prepare);
        }
        else {
            if (!(prepare instanceof LC)) {
                throw new IllegalArgumentException("Illegal constraint type: " + prepare.getClass().toString());
            }
            this.lc = (LC)prepare;
        }
        this.layoutConstraints = prepare;
        this.dirty = true;
    }
    
    public Object getColumnConstraints() {
        return this.colConstraints;
    }
    
    public void setColumnConstraints(Object prepare) {
        if (prepare == null || prepare instanceof String) {
            prepare = ConstraintParser.prepare((String)prepare);
            this.colSpecs = ConstraintParser.parseColumnConstraints((String)prepare);
        }
        else {
            if (!(prepare instanceof AC)) {
                throw new IllegalArgumentException("Illegal constraint type: " + prepare.getClass().toString());
            }
            this.colSpecs = (AC)prepare;
        }
        this.colConstraints = prepare;
        this.dirty = true;
    }
    
    public Object getRowConstraints() {
        return this.rowConstraints;
    }
    
    public void setRowConstraints(Object prepare) {
        if (prepare == null || prepare instanceof String) {
            prepare = ConstraintParser.prepare((String)prepare);
            this.rowSpecs = ConstraintParser.parseRowConstraints((String)prepare);
        }
        else {
            if (!(prepare instanceof AC)) {
                throw new IllegalArgumentException("Illegal constraint type: " + prepare.getClass().toString());
            }
            this.rowSpecs = (AC)prepare;
        }
        this.rowConstraints = prepare;
        this.dirty = true;
    }
    
    public Map<Component, Object> getConstraintMap() {
        return new IdentityHashMap<Component, Object>(this.scrConstrMap);
    }
    
    public void setConstraintMap(final Map<Component, Object> map) {
        this.scrConstrMap.clear();
        this.ccMap.clear();
        for (final Map.Entry<Component, Object> entry : map.entrySet()) {
            this.setComponentConstraintsImpl(entry.getKey(), entry.getValue(), true);
        }
    }
    
    public Object getComponentConstraints(final Component component) {
        synchronized (component.getParent().getTreeLock()) {
            return this.scrConstrMap.get(component);
        }
    }
    
    public void setComponentConstraints(final Component component, final Object o) {
        this.setComponentConstraintsImpl(component, o, false);
    }
    
    private void setComponentConstraintsImpl(final Component component, final Object o, final boolean b) {
        final Container parent = component.getParent();
        final Object o2;
        synchronized (o2 = ((parent != null) ? parent.getTreeLock() : new Object())) {
            if (!b && !this.scrConstrMap.containsKey(component)) {
                throw new IllegalArgumentException("Component must already be added to parent!");
            }
            final SwingComponentWrapper swingComponentWrapper = new SwingComponentWrapper(component);
            if (o == null || o instanceof String) {
                final String prepare = ConstraintParser.prepare((String)o);
                this.scrConstrMap.put(component, o);
                this.ccMap.put(swingComponentWrapper, ConstraintParser.parseComponentConstraint(prepare));
            }
            else {
                if (!(o instanceof CC)) {
                    throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + o.getClass().toString());
                }
                this.scrConstrMap.put(component, o);
                this.ccMap.put(swingComponentWrapper, (CC)o);
            }
            this.dirty = true;
        }
    }
    
    public boolean isManagingComponent(final Component component) {
        return this.scrConstrMap.containsKey(component);
    }
    
    public void addLayoutCallback(final LayoutCallback layoutCallback) {
        if (layoutCallback == null) {
            throw new NullPointerException();
        }
        if (this.callbackList == null) {
            this.callbackList = new ArrayList<LayoutCallback>(1);
        }
        this.callbackList.add(layoutCallback);
    }
    
    public void removeLayoutCallback(final LayoutCallback layoutCallback) {
        if (this.callbackList != null) {
            this.callbackList.remove(layoutCallback);
        }
    }
    
    private void setDebug(final ComponentWrapper componentWrapper, final boolean b) {
        if (b && (this.debugTimer == null || this.debugTimer.getDelay() != this.getDebugMillis())) {
            if (this.debugTimer != null) {
                this.debugTimer.stop();
            }
            final ContainerWrapper parent = componentWrapper.getParent();
            final Component component = (parent != null) ? ((Component)parent.getComponent()) : null;
            this.debugTimer = new Timer(this.getDebugMillis(), new MyDebugRepaintListener());
            if (component != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        final Container parent = component.getParent();
                        if (parent != null) {
                            if (parent instanceof JComponent) {
                                ((JComponent)parent).revalidate();
                            }
                            else {
                                component.invalidate();
                                parent.validate();
                            }
                        }
                    }
                });
            }
            this.debugTimer.setInitialDelay(100);
            this.debugTimer.start();
        }
        else if (!b && this.debugTimer != null) {
            this.debugTimer.stop();
            this.debugTimer = null;
        }
    }
    
    private boolean getDebug() {
        return this.debugTimer != null;
    }
    
    private int getDebugMillis() {
        final int globalDebugMillis = LayoutUtil.getGlobalDebugMillis();
        return (globalDebugMillis > 0) ? globalDebugMillis : this.lc.getDebugMillis();
    }
    
    private void checkCache(final Container container) {
        if (container == null) {
            return;
        }
        if (this.dirty) {
            this.grid = null;
        }
        final int modCount = PlatformDefaults.getModCount();
        if (this.lastModCount != modCount) {
            this.grid = null;
            this.lastModCount = modCount;
        }
        if (!container.isValid()) {
            if (!this.lastWasInvalid) {
                this.lastWasInvalid = true;
                int lastHash = 0;
                boolean b = false;
                for (final ComponentWrapper componentWrapper : this.ccMap.keySet()) {
                    final Object component = componentWrapper.getComponent();
                    if (component instanceof JTextArea || component instanceof JEditorPane) {
                        b = true;
                    }
                    lastHash += componentWrapper.getLayoutHashCode();
                }
                if (b) {
                    this.resetLastInvalidOnParent(container);
                }
                if (lastHash != this.lastHash) {
                    this.grid = null;
                    this.lastHash = lastHash;
                }
                final Dimension size = container.getSize();
                if (this.lastInvalidSize == null || !this.lastInvalidSize.equals(size)) {
                    if (this.grid != null) {
                        this.grid.invalidateContainerSize();
                    }
                    this.lastInvalidSize = size;
                }
            }
        }
        else {
            this.lastWasInvalid = false;
        }
        final ContainerWrapper checkParent = this.checkParent(container);
        this.setDebug(checkParent, this.getDebugMillis() > 0);
        if (this.grid == null) {
            this.grid = new Grid(checkParent, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList);
        }
        this.dirty = false;
    }
    
    private void resetLastInvalidOnParent(Container parent) {
        while (parent != null) {
            final LayoutManager layout = parent.getLayout();
            if (layout instanceof MigLayout) {
                ((MigLayout)layout).lastWasInvalid = false;
            }
            parent = parent.getParent();
        }
    }
    
    private ContainerWrapper checkParent(final Container container) {
        if (container == null) {
            return null;
        }
        if (this.cacheParentW == null || this.cacheParentW.getComponent() != container) {
            this.cacheParentW = new SwingContainerWrapper(container);
        }
        return this.cacheParentW;
    }
    
    public void layoutContainer(final Container container) {
        synchronized (container.getTreeLock()) {
            this.checkCache(container);
            final Insets insets = container.getInsets();
            final int[] array = { insets.left, insets.top, container.getWidth() - insets.left - insets.right, container.getHeight() - insets.top - insets.bottom };
            if (this.grid.layout(array, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), true)) {
                this.grid = null;
                this.checkCache(container);
                this.grid.layout(array, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), false);
            }
            final long lastSize = this.grid.getHeight()[1] + ((long)this.grid.getWidth()[1] << 32);
            if (this.lastSize != lastSize) {
                this.lastSize = lastSize;
                final ContainerWrapper checkParent = this.checkParent(container);
                final Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)checkParent.getComponent());
                if (window != null) {
                    if (window.isVisible()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                MigLayout.this.adjustWindowSize(checkParent);
                            }
                        });
                    }
                    else {
                        this.adjustWindowSize(checkParent);
                    }
                }
            }
            this.lastInvalidSize = null;
        }
    }
    
    private void adjustWindowSize(final ContainerWrapper containerWrapper) {
        final BoundSize packWidth = this.lc.getPackWidth();
        final BoundSize packHeight = this.lc.getPackHeight();
        if (packWidth == null && packHeight == null) {
            return;
        }
        final Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)containerWrapper.getComponent());
        if (window == null) {
            return;
        }
        final Dimension preferredSize = window.getPreferredSize();
        final int constrain = this.constrain(this.checkParent(window), window.getWidth(), preferredSize.width, packWidth);
        final int constrain2 = this.constrain(this.checkParent(window), window.getHeight(), preferredSize.height, packHeight);
        window.setBounds(Math.round(window.getX() - (constrain - window.getWidth()) * (1.0f - this.lc.getPackWidthAlign())), Math.round(window.getY() - (constrain2 - window.getHeight()) * (1.0f - this.lc.getPackHeightAlign())), constrain, constrain2);
    }
    
    private int constrain(final ContainerWrapper containerWrapper, final int n, final int n2, final BoundSize boundSize) {
        if (boundSize == null) {
            return n;
        }
        int pixels = n;
        final UnitValue preferred = boundSize.getPreferred();
        if (preferred != null) {
            pixels = preferred.getPixels((float)n2, containerWrapper, containerWrapper);
        }
        final int constrain = boundSize.constrain(pixels, (float)n2, containerWrapper);
        return boundSize.getGapPush() ? Math.max(n, constrain) : constrain;
    }
    
    public Dimension minimumLayoutSize(final Container container) {
        synchronized (container.getTreeLock()) {
            return this.getSizeImpl(container, 0);
        }
    }
    
    public Dimension preferredLayoutSize(final Container container) {
        synchronized (container.getTreeLock()) {
            if (this.lastParentSize == null || !container.getSize().equals(this.lastParentSize)) {
                final Iterator<ComponentWrapper> iterator = this.ccMap.keySet().iterator();
                while (iterator.hasNext()) {
                    final Component component = (Component)iterator.next().getComponent();
                    if (component instanceof JTextArea || component instanceof JEditorPane || (component instanceof JComponent && Boolean.TRUE.equals(((JComponent)component).getClientProperty("migLayout.dynamicAspectRatio")))) {
                        this.layoutContainer(container);
                        break;
                    }
                }
            }
            this.lastParentSize = container.getSize();
            return this.getSizeImpl(container, 1);
        }
    }
    
    public Dimension maximumLayoutSize(final Container container) {
        return new Dimension(32767, 32767);
    }
    
    private Dimension getSizeImpl(final Container container, final int n) {
        this.checkCache(container);
        final Insets insets = container.getInsets();
        return new Dimension(LayoutUtil.getSizeSafe((int[])((this.grid != null) ? this.grid.getWidth() : null), n) + insets.left + insets.right, LayoutUtil.getSizeSafe((int[])((this.grid != null) ? this.grid.getHeight() : null), n) + insets.top + insets.bottom);
    }
    
    public float getLayoutAlignmentX(final Container container) {
        return (this.lc != null && this.lc.getAlignX() != null) ? ((float)this.lc.getAlignX().getPixels(1.0f, this.checkParent(container), null)) : 0.0f;
    }
    
    public float getLayoutAlignmentY(final Container container) {
        return (this.lc != null && this.lc.getAlignY() != null) ? ((float)this.lc.getAlignY().getPixels(1.0f, this.checkParent(container), null)) : 0.0f;
    }
    
    public void addLayoutComponent(final String s, final Component component) {
        this.addLayoutComponent(component, s);
    }
    
    public void addLayoutComponent(final Component component, final Object o) {
        synchronized (component.getParent().getTreeLock()) {
            this.setComponentConstraintsImpl(component, o, true);
        }
    }
    
    public void removeLayoutComponent(final Component component) {
        synchronized (component.getParent().getTreeLock()) {
            this.scrConstrMap.remove(component);
            this.ccMap.remove(new SwingComponentWrapper(component));
        }
    }
    
    public void invalidateLayout(final Container container) {
        this.dirty = true;
    }
    
    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }
    
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }
    
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == MigLayout.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
    
    private class MyDebugRepaintListener implements ActionListener
    {
        public void actionPerformed(final ActionEvent actionEvent) {
            if (MigLayout.this.grid != null && ((Component)MigLayout.this.grid.getContainer().getComponent()).isShowing()) {
                MigLayout.this.grid.paintDebug();
                return;
            }
            MigLayout.this.debugTimer.stop();
            MigLayout.this.debugTimer = null;
        }
    }
}
