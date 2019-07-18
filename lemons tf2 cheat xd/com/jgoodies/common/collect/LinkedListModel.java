// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.collect;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import javax.swing.event.EventListenerList;
import java.util.LinkedList;

public class LinkedListModel<E> extends LinkedList<E> implements ObservableList2<E>
{
    private static final long serialVersionUID = 5753378113505707237L;
    private EventListenerList listenerList;
    
    public LinkedListModel() {
    }
    
    public LinkedListModel(final Collection<? extends E> c) {
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
    public final void addFirst(final E e) {
        super.addFirst(e);
        this.fireIntervalAdded(0, 0);
    }
    
    @Override
    public final void addLast(final E e) {
        final int newIndex = this.size();
        super.addLast(e);
        this.fireIntervalAdded(newIndex, newIndex);
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
        if (index == -1) {
            return false;
        }
        this.remove(index);
        return true;
    }
    
    @Override
    public final E removeFirst() {
        final E first = super.removeFirst();
        this.fireIntervalRemoved(0, 0);
        return first;
    }
    
    @Override
    public final E removeLast() {
        final int lastIndex = this.size() - 1;
        final E last = super.removeLast();
        this.fireIntervalRemoved(lastIndex, lastIndex);
        return last;
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
    public final ListIterator<E> listIterator(final int index) {
        return new ReportingListIterator(super.listIterator(index));
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
    public final E getElementAt(final int index) {
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
    
    private final class ReportingListIterator implements ListIterator<E>
    {
        private final ListIterator<E> delegate;
        private int lastReturnedIndex;
        
        ReportingListIterator(final ListIterator<E> delegate) {
            this.delegate = delegate;
            this.lastReturnedIndex = -1;
        }
        
        @Override
        public boolean hasNext() {
            return this.delegate.hasNext();
        }
        
        @Override
        public E next() {
            this.lastReturnedIndex = this.nextIndex();
            return this.delegate.next();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.delegate.hasPrevious();
        }
        
        @Override
        public E previous() {
            this.lastReturnedIndex = this.previousIndex();
            return this.delegate.previous();
        }
        
        @Override
        public int nextIndex() {
            return this.delegate.nextIndex();
        }
        
        @Override
        public int previousIndex() {
            return this.delegate.previousIndex();
        }
        
        @Override
        public void remove() {
            final int oldSize = LinkedListModel.this.size();
            this.delegate.remove();
            final int newSize = LinkedListModel.this.size();
            if (newSize < oldSize) {
                LinkedListModel.this.fireIntervalRemoved(this.lastReturnedIndex, this.lastReturnedIndex);
            }
        }
        
        @Override
        public void set(final E e) {
            this.delegate.set(e);
            LinkedListModel.this.fireContentsChanged(this.lastReturnedIndex);
        }
        
        @Override
        public void add(final E e) {
            this.delegate.add(e);
            final int newIndex = this.previousIndex();
            LinkedListModel.this.fireIntervalAdded(newIndex, newIndex);
            this.lastReturnedIndex = -1;
        }
    }
}
