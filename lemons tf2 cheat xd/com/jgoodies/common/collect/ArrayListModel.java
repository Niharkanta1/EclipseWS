// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.collect;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Iterator;
import java.util.Collection;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;

public class ArrayListModel<E> extends ArrayList<E> implements ObservableList2<E>
{
    private static final long serialVersionUID = -6165677201152015546L;
    private EventListenerList listenerList;
    
    public ArrayListModel() {
        this(10);
    }
    
    public ArrayListModel(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public ArrayListModel(final Collection<? extends E> c) {
        super(c);
    }
    
    @Override
    public final void add(final int index, final E element) {
        super.add(index, element);
        this.fireIntervalAdded(index, index);
    }
    
    @Override
    public final boolean add(final E e) {
        final int newIndex = this.size();
        super.add(e);
        this.fireIntervalAdded(newIndex, newIndex);
        return true;
    }
    
    @Override
    public final boolean addAll(final int index, final Collection<? extends E> c) {
        final boolean changed = super.addAll(index, c);
        if (changed) {
            final int lastIndex = index + c.size() - 1;
            this.fireIntervalAdded(index, lastIndex);
        }
        return changed;
    }
    
    @Override
    public final boolean addAll(final Collection<? extends E> c) {
        final int firstIndex = this.size();
        final boolean changed = super.addAll(c);
        if (changed) {
            final int lastIndex = firstIndex + c.size() - 1;
            this.fireIntervalAdded(firstIndex, lastIndex);
        }
        return changed;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean modified = false;
        final Iterator<?> e = this.iterator();
        while (e.hasNext()) {
            if (c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        boolean modified = false;
        final Iterator<E> e = this.iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public final void clear() {
        if (this.isEmpty()) {
            return;
        }
        final int oldLastIndex = this.size() - 1;
        super.clear();
        this.fireIntervalRemoved(0, oldLastIndex);
    }
    
    @Override
    public final E remove(final int index) {
        final E removedElement = super.remove(index);
        this.fireIntervalRemoved(index, index);
        return removedElement;
    }
    
    @Override
    public final boolean remove(final Object o) {
        final int index = this.indexOf(o);
        final boolean contained = index != -1;
        if (contained) {
            this.remove(index);
        }
        return contained;
    }
    
    @Override
    protected final void removeRange(final int fromIndex, final int toIndex) {
        super.removeRange(fromIndex, toIndex);
        this.fireIntervalRemoved(fromIndex, toIndex - 1);
    }
    
    @Override
    public final E set(final int index, final E element) {
        final E previousElement = super.set(index, element);
        this.fireContentsChanged(index, index);
        return previousElement;
    }
    
    @Override
    public final void addListDataListener(final ListDataListener l) {
        this.getEventListenerList().add(ListDataListener.class, l);
    }
    
    @Override
    public final void removeListDataListener(final ListDataListener l) {
        this.getEventListenerList().remove(ListDataListener.class, l);
    }
    
    @Override
    public final Object getElementAt(final int index) {
        return this.get(index);
    }
    
    @Override
    public final int getSize() {
        return this.size();
    }
    
    @Override
    public final void fireContentsChanged(final int index) {
        this.fireContentsChanged(index, index);
    }
    
    @Override
    public final void fireContentsChanged(final int index0, final int index1) {
        final Object[] listeners = this.getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 0, index0, index1);
                }
                ((ListDataListener)listeners[i + 1]).contentsChanged(e);
            }
        }
    }
    
    public final ListDataListener[] getListDataListeners() {
        return this.getEventListenerList().getListeners(ListDataListener.class);
    }
    
    private void fireIntervalAdded(final int index0, final int index1) {
        final Object[] listeners = this.getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 1, index0, index1);
                }
                ((ListDataListener)listeners[i + 1]).intervalAdded(e);
            }
        }
    }
    
    private void fireIntervalRemoved(final int index0, final int index1) {
        final Object[] listeners = this.getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 2, index0, index1);
                }
                ((ListDataListener)listeners[i + 1]).intervalRemoved(e);
            }
        }
    }
    
    private EventListenerList getEventListenerList() {
        if (this.listenerList == null) {
            this.listenerList = new EventListenerList();
        }
        return this.listenerList;
    }
}
