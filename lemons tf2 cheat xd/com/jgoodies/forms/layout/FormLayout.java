// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JComponent;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Dimension;
import com.jgoodies.common.base.Objects;
import java.awt.Container;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.jgoodies.common.base.Preconditions;
import java.awt.Component;
import java.util.Map;
import java.util.List;
import java.io.Serializable;
import java.awt.LayoutManager2;

public final class FormLayout implements LayoutManager2, Serializable
{
    private final List<ColumnSpec> colSpecs;
    private final List<RowSpec> rowSpecs;
    private int[][] colGroupIndices;
    private int[][] rowGroupIndices;
    private final Map<Component, CellConstraints> constraintMap;
    private boolean honorsVisibility;
    private transient List<Component>[] colComponents;
    private transient List<Component>[] rowComponents;
    private final ComponentSizeCache componentSizeCache;
    private final Measure minimumWidthMeasure;
    private final Measure minimumHeightMeasure;
    private final Measure preferredWidthMeasure;
    private final Measure preferredHeightMeasure;
    
    public FormLayout() {
        this(new ColumnSpec[0], new RowSpec[0]);
    }
    
    public FormLayout(final String encodedColumnSpecs) {
        this(encodedColumnSpecs, LayoutMap.getRoot());
    }
    
    public FormLayout(final String encodedColumnSpecs, final LayoutMap layoutMap) {
        this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap), new RowSpec[0]);
    }
    
    public FormLayout(final String encodedColumnSpecs, final String encodedRowSpecs) {
        this(encodedColumnSpecs, encodedRowSpecs, LayoutMap.getRoot());
    }
    
    public FormLayout(final String encodedColumnSpecs, final String encodedRowSpecs, final LayoutMap layoutMap) {
        this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap), RowSpec.decodeSpecs(encodedRowSpecs, layoutMap));
    }
    
    public FormLayout(final ColumnSpec[] colSpecs) {
        this(colSpecs, new RowSpec[0]);
    }
    
    public FormLayout(final ColumnSpec[] colSpecs, final RowSpec[] rowSpecs) {
        this.honorsVisibility = true;
        Preconditions.checkNotNull(colSpecs, "The column specifications must not be null.");
        Preconditions.checkNotNull(rowSpecs, "The row specifications must not be null.");
        this.colSpecs = new ArrayList<ColumnSpec>(Arrays.asList(colSpecs));
        this.rowSpecs = new ArrayList<RowSpec>(Arrays.asList(rowSpecs));
        this.colGroupIndices = new int[0][];
        this.rowGroupIndices = new int[0][];
        final int initialCapacity = colSpecs.length * rowSpecs.length / 4;
        this.constraintMap = new HashMap<Component, CellConstraints>(initialCapacity);
        this.componentSizeCache = new ComponentSizeCache(initialCapacity);
        this.minimumWidthMeasure = new MinimumWidthMeasure(this.componentSizeCache);
        this.minimumHeightMeasure = new MinimumHeightMeasure(this.componentSizeCache);
        this.preferredWidthMeasure = new PreferredWidthMeasure(this.componentSizeCache);
        this.preferredHeightMeasure = new PreferredHeightMeasure(this.componentSizeCache);
    }
    
    public int getColumnCount() {
        return this.colSpecs.size();
    }
    
    public ColumnSpec getColumnSpec(final int columnIndex) {
        return this.colSpecs.get(columnIndex - 1);
    }
    
    public void setColumnSpec(final int columnIndex, final ColumnSpec columnSpec) {
        Preconditions.checkNotNull(columnSpec, "The column spec must not be null.");
        this.colSpecs.set(columnIndex - 1, columnSpec);
    }
    
    public void appendColumn(final ColumnSpec columnSpec) {
        Preconditions.checkNotNull(columnSpec, "The column spec must not be null.");
        this.colSpecs.add(columnSpec);
    }
    
    public void insertColumn(final int columnIndex, final ColumnSpec columnSpec) {
        if (columnIndex < 1 || columnIndex > this.getColumnCount()) {
            throw new IndexOutOfBoundsException("The column index " + columnIndex + "must be in the range [1, " + this.getColumnCount() + "].");
        }
        this.colSpecs.add(columnIndex - 1, columnSpec);
        this.shiftComponentsHorizontally(columnIndex, false);
        adjustGroupIndices(this.colGroupIndices, columnIndex, false);
    }
    
    public void removeColumn(final int columnIndex) {
        if (columnIndex < 1 || columnIndex > this.getColumnCount()) {
            throw new IndexOutOfBoundsException("The column index " + columnIndex + " must be in the range [1, " + this.getColumnCount() + "].");
        }
        this.colSpecs.remove(columnIndex - 1);
        this.shiftComponentsHorizontally(columnIndex, true);
        adjustGroupIndices(this.colGroupIndices, columnIndex, true);
    }
    
    public int getRowCount() {
        return this.rowSpecs.size();
    }
    
    public RowSpec getRowSpec(final int rowIndex) {
        return this.rowSpecs.get(rowIndex - 1);
    }
    
    public void setRowSpec(final int rowIndex, final RowSpec rowSpec) {
        Preconditions.checkNotNull(rowSpec, "The row spec must not be null.");
        this.rowSpecs.set(rowIndex - 1, rowSpec);
    }
    
    public void appendRow(final RowSpec rowSpec) {
        Preconditions.checkNotNull(rowSpec, "The row spec must not be null.");
        this.rowSpecs.add(rowSpec);
    }
    
    public void insertRow(final int rowIndex, final RowSpec rowSpec) {
        if (rowIndex < 1 || rowIndex > this.getRowCount()) {
            throw new IndexOutOfBoundsException("The row index " + rowIndex + " must be in the range [1, " + this.getRowCount() + "].");
        }
        this.rowSpecs.add(rowIndex - 1, rowSpec);
        this.shiftComponentsVertically(rowIndex, false);
        adjustGroupIndices(this.rowGroupIndices, rowIndex, false);
    }
    
    public void removeRow(final int rowIndex) {
        if (rowIndex < 1 || rowIndex > this.getRowCount()) {
            throw new IndexOutOfBoundsException("The row index " + rowIndex + "must be in the range [1, " + this.getRowCount() + "].");
        }
        this.rowSpecs.remove(rowIndex - 1);
        this.shiftComponentsVertically(rowIndex, true);
        adjustGroupIndices(this.rowGroupIndices, rowIndex, true);
    }
    
    private void shiftComponentsHorizontally(final int columnIndex, final boolean remove) {
        final int offset = remove ? -1 : 1;
        for (final Object element : this.constraintMap.entrySet()) {
            final Map.Entry entry = (Map.Entry)element;
            final CellConstraints constraints = entry.getValue();
            final int x1 = constraints.gridX;
            final int w = constraints.gridWidth;
            final int x2 = x1 + w - 1;
            if (x1 == columnIndex && remove) {
                throw new IllegalStateException("The removed column " + columnIndex + " must not contain component origins.\n" + "Illegal component=" + entry.getKey());
            }
            if (x1 >= columnIndex) {
                final CellConstraints cellConstraints = constraints;
                cellConstraints.gridX += offset;
            }
            else {
                if (x2 < columnIndex) {
                    continue;
                }
                final CellConstraints cellConstraints2 = constraints;
                cellConstraints2.gridWidth += offset;
            }
        }
    }
    
    private void shiftComponentsVertically(final int rowIndex, final boolean remove) {
        final int offset = remove ? -1 : 1;
        for (final Object element : this.constraintMap.entrySet()) {
            final Map.Entry entry = (Map.Entry)element;
            final CellConstraints constraints = entry.getValue();
            final int y1 = constraints.gridY;
            final int h = constraints.gridHeight;
            final int y2 = y1 + h - 1;
            if (y1 == rowIndex && remove) {
                throw new IllegalStateException("The removed row " + rowIndex + " must not contain component origins.\n" + "Illegal component=" + entry.getKey());
            }
            if (y1 >= rowIndex) {
                final CellConstraints cellConstraints = constraints;
                cellConstraints.gridY += offset;
            }
            else {
                if (y2 < rowIndex) {
                    continue;
                }
                final CellConstraints cellConstraints2 = constraints;
                cellConstraints2.gridHeight += offset;
            }
        }
    }
    
    private static void adjustGroupIndices(final int[][] allGroupIndices, final int modifiedIndex, final boolean remove) {
        final int offset = remove ? -1 : 1;
        for (final int[] groupIndices : allGroupIndices) {
            final int[] allGroupIndice = groupIndices;
            for (int i = 0; i < groupIndices.length; ++i) {
                final int index = groupIndices[i];
                if (index == modifiedIndex && remove) {
                    throw new IllegalStateException("The removed index " + modifiedIndex + " must not be grouped.");
                }
                if (index >= modifiedIndex) {
                    final int[] array = groupIndices;
                    final int n = i;
                    array[n] += offset;
                }
            }
        }
    }
    
    public CellConstraints getConstraints(final Component component) {
        return (CellConstraints)this.getConstraints0(component).clone();
    }
    
    private CellConstraints getConstraints0(final Component component) {
        Preconditions.checkNotNull(component, "The component must not be null.");
        final CellConstraints constraints = this.constraintMap.get(component);
        Preconditions.checkState(constraints != null, "The component has not been added to the container.");
        return constraints;
    }
    
    public void setConstraints(final Component component, final CellConstraints constraints) {
        Preconditions.checkNotNull(component, "The component must not be null.");
        Preconditions.checkNotNull(constraints, "The constraints must not be null.");
        constraints.ensureValidGridBounds(this.getColumnCount(), this.getRowCount());
        this.constraintMap.put(component, (CellConstraints)constraints.clone());
    }
    
    private void removeConstraints(final Component component) {
        this.constraintMap.remove(component);
        this.componentSizeCache.removeEntry(component);
    }
    
    public int[][] getColumnGroups() {
        return deepClone(this.colGroupIndices);
    }
    
    public void setColumnGroups(final int[][] groupOfIndices) {
        this.setColumnGroupsImpl(groupOfIndices, true);
    }
    
    private void setColumnGroupsImpl(final int[][] groupOfIndices, final boolean checkIndices) {
        final int maxColumn = this.getColumnCount();
        final boolean[] usedIndices = new boolean[maxColumn + 1];
        for (int group = 0; group < groupOfIndices.length; ++group) {
            final int[] indices = groupOfIndices[group];
            if (checkIndices) {
                Preconditions.checkArgument(indices.length >= 2, "Each indice group must contain at least two indices.");
            }
            for (final int colIndex : indices) {
                final int indice = colIndex;
                if (colIndex < 1 || colIndex > maxColumn) {
                    throw new IndexOutOfBoundsException("Invalid column group index " + colIndex + " in group " + (group + 1));
                }
                if (usedIndices[colIndex]) {
                    throw new IllegalArgumentException("Column index " + colIndex + " must not be used in multiple column groups.");
                }
                usedIndices[colIndex] = true;
            }
        }
        this.colGroupIndices = deepClone(groupOfIndices);
    }
    
    public void setColumnGroup(final int... indices) {
        Preconditions.checkNotNull(indices, "The %1$s must not be null.", "column group indices");
        Preconditions.checkArgument(indices.length >= 2, "You must specify at least two indices.");
        this.setColumnGroups(new int[][] { indices });
    }
    
    public void addGroupedColumn(final int columnIndex) {
        int[][] newColGroups = this.getColumnGroups();
        if (newColGroups.length == 0) {
            newColGroups = new int[][] { { columnIndex } };
        }
        else {
            final int lastGroupIndex = newColGroups.length - 1;
            final int[] lastGroup = newColGroups[lastGroupIndex];
            final int groupSize = lastGroup.length;
            final int[] newLastGroup = new int[groupSize + 1];
            System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
            newLastGroup[groupSize] = columnIndex;
            newColGroups[lastGroupIndex] = newLastGroup;
        }
        this.setColumnGroupsImpl(newColGroups, false);
    }
    
    public int[][] getRowGroups() {
        return deepClone(this.rowGroupIndices);
    }
    
    public void setRowGroups(final int[][] groupOfIndices) {
        this.setRowGroupsImpl(groupOfIndices, true);
    }
    
    private void setRowGroupsImpl(final int[][] groupOfIndices, final boolean checkIndices) {
        final int rowCount = this.getRowCount();
        final boolean[] usedIndices = new boolean[rowCount + 1];
        for (int group = 0; group < groupOfIndices.length; ++group) {
            final int[] indices = groupOfIndices[group];
            if (checkIndices) {
                Preconditions.checkArgument(indices.length >= 2, "Each indice group must contain at least two indices.");
            }
            for (final int rowIndex : indices) {
                final int indice = rowIndex;
                if (rowIndex < 1 || rowIndex > rowCount) {
                    throw new IndexOutOfBoundsException("Invalid row group index " + rowIndex + " in group " + (group + 1));
                }
                if (usedIndices[rowIndex]) {
                    throw new IllegalArgumentException("Row index " + rowIndex + " must not be used in multiple row groups.");
                }
                usedIndices[rowIndex] = true;
            }
        }
        this.rowGroupIndices = deepClone(groupOfIndices);
    }
    
    public void setRowGroup(final int... indices) {
        Preconditions.checkNotNull(indices, "The %1$s must not be null.", "row group indices");
        Preconditions.checkArgument(indices.length >= 2, "You must specify at least two indices.");
        this.setRowGroups(new int[][] { indices });
    }
    
    public void addGroupedRow(final int rowIndex) {
        int[][] newRowGroups = this.getRowGroups();
        if (newRowGroups.length == 0) {
            newRowGroups = new int[][] { { rowIndex } };
        }
        else {
            final int lastGroupIndex = newRowGroups.length - 1;
            final int[] lastGroup = newRowGroups[lastGroupIndex];
            final int groupSize = lastGroup.length;
            final int[] newLastGroup = new int[groupSize + 1];
            System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
            newLastGroup[groupSize] = rowIndex;
            newRowGroups[lastGroupIndex] = newLastGroup;
        }
        this.setRowGroupsImpl(newRowGroups, false);
    }
    
    public boolean getHonorsVisibility() {
        return this.honorsVisibility;
    }
    
    public void setHonorsVisibility(final boolean b) {
        final boolean oldHonorsVisibility = this.getHonorsVisibility();
        if (oldHonorsVisibility == b) {
            return;
        }
        this.honorsVisibility = b;
        final Set componentSet = this.constraintMap.keySet();
        if (componentSet.isEmpty()) {
            return;
        }
        final Component firstComponent = componentSet.iterator().next();
        final Container container = firstComponent.getParent();
        invalidateAndRepaint(container);
    }
    
    public void setHonorsVisibility(final Component component, final Boolean b) {
        final CellConstraints constraints = this.getConstraints0(component);
        if (Objects.equals(b, constraints.honorsVisibility)) {
            return;
        }
        constraints.honorsVisibility = b;
        invalidateAndRepaint(component.getParent());
    }
    
    @Override
    public void addLayoutComponent(final String name, final Component component) {
        throw new UnsupportedOperationException("Use #addLayoutComponent(Component, Object) instead.");
    }
    
    @Override
    public void addLayoutComponent(final Component comp, final Object constraints) {
        Preconditions.checkNotNull(constraints, "The constraints must not be null.");
        if (constraints instanceof String) {
            this.setConstraints(comp, new CellConstraints((String)constraints));
        }
        else {
            if (!(constraints instanceof CellConstraints)) {
                throw new IllegalArgumentException("Illegal constraint type " + constraints.getClass());
            }
            this.setConstraints(comp, (CellConstraints)constraints);
        }
    }
    
    @Override
    public void removeLayoutComponent(final Component comp) {
        this.removeConstraints(comp);
    }
    
    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return this.computeLayoutSize(parent, this.minimumWidthMeasure, this.minimumHeightMeasure);
    }
    
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        return this.computeLayoutSize(parent, this.preferredWidthMeasure, this.preferredHeightMeasure);
    }
    
    @Override
    public Dimension maximumLayoutSize(final Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public float getLayoutAlignmentX(final Container parent) {
        return 0.5f;
    }
    
    @Override
    public float getLayoutAlignmentY(final Container parent) {
        return 0.5f;
    }
    
    @Override
    public void invalidateLayout(final Container target) {
        this.invalidateCaches();
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        synchronized (parent.getTreeLock()) {
            this.initializeColAndRowComponentLists();
            final Dimension size = parent.getSize();
            final Insets insets = parent.getInsets();
            final int totalWidth = size.width - insets.left - insets.right;
            final int totalHeight = size.height - insets.top - insets.bottom;
            final int[] x = computeGridOrigins(parent, totalWidth, insets.left, this.colSpecs, this.colComponents, this.colGroupIndices, this.minimumWidthMeasure, this.preferredWidthMeasure);
            final int[] y = computeGridOrigins(parent, totalHeight, insets.top, this.rowSpecs, this.rowComponents, this.rowGroupIndices, this.minimumHeightMeasure, this.preferredHeightMeasure);
            this.layoutComponents(x, y);
        }
    }
    
    private void initializeColAndRowComponentLists() {
        this.colComponents = (List<Component>[])new List[this.getColumnCount()];
        for (int i = 0; i < this.getColumnCount(); ++i) {
            this.colComponents[i] = new ArrayList<Component>();
        }
        this.rowComponents = (List<Component>[])new List[this.getRowCount()];
        for (int i = 0; i < this.getRowCount(); ++i) {
            this.rowComponents[i] = new ArrayList<Component>();
        }
        for (final Object element : this.constraintMap.entrySet()) {
            final Map.Entry entry = (Map.Entry)element;
            final Component component = entry.getKey();
            final CellConstraints constraints = entry.getValue();
            if (this.takeIntoAccount(component, constraints)) {
                if (constraints.gridWidth == 1) {
                    this.colComponents[constraints.gridX - 1].add(component);
                }
                if (constraints.gridHeight != 1) {
                    continue;
                }
                this.rowComponents[constraints.gridY - 1].add(component);
            }
        }
    }
    
    private Dimension computeLayoutSize(final Container parent, final Measure defaultWidthMeasure, final Measure defaultHeightMeasure) {
        synchronized (parent.getTreeLock()) {
            this.initializeColAndRowComponentLists();
            final int[] colWidths = maximumSizes(parent, this.colSpecs, this.colComponents, this.minimumWidthMeasure, this.preferredWidthMeasure, defaultWidthMeasure);
            final int[] rowHeights = maximumSizes(parent, this.rowSpecs, this.rowComponents, this.minimumHeightMeasure, this.preferredHeightMeasure, defaultHeightMeasure);
            final int[] groupedWidths = groupedSizes(this.colGroupIndices, colWidths);
            final int[] groupedHeights = groupedSizes(this.rowGroupIndices, rowHeights);
            final int[] xOrigins = computeOrigins(groupedWidths, 0);
            final int[] yOrigins = computeOrigins(groupedHeights, 0);
            final int width1 = sum(groupedWidths);
            final int height1 = sum(groupedHeights);
            int maxWidth = width1;
            int maxHeight = height1;
            final int[] maxFixedSizeColsTable = computeMaximumFixedSpanTable(this.colSpecs);
            final int[] maxFixedSizeRowsTable = computeMaximumFixedSpanTable(this.rowSpecs);
            for (final Object element : this.constraintMap.entrySet()) {
                final Map.Entry entry = (Map.Entry)element;
                final Component component = entry.getKey();
                final CellConstraints constraints = entry.getValue();
                if (!this.takeIntoAccount(component, constraints)) {
                    continue;
                }
                if (constraints.gridWidth > 1 && constraints.gridWidth > maxFixedSizeColsTable[constraints.gridX - 1]) {
                    final int compWidth = defaultWidthMeasure.sizeOf(component);
                    final int gridX1 = constraints.gridX - 1;
                    final int gridX2 = gridX1 + constraints.gridWidth;
                    final int lead = xOrigins[gridX1];
                    final int trail = width1 - xOrigins[gridX2];
                    final int myWidth = lead + compWidth + trail;
                    if (myWidth > maxWidth) {
                        maxWidth = myWidth;
                    }
                }
                if (constraints.gridHeight <= 1 || constraints.gridHeight <= maxFixedSizeRowsTable[constraints.gridY - 1]) {
                    continue;
                }
                final int compHeight = defaultHeightMeasure.sizeOf(component);
                final int gridY1 = constraints.gridY - 1;
                final int gridY2 = gridY1 + constraints.gridHeight;
                final int lead = yOrigins[gridY1];
                final int trail = height1 - yOrigins[gridY2];
                final int myHeight = lead + compHeight + trail;
                if (myHeight <= maxHeight) {
                    continue;
                }
                maxHeight = myHeight;
            }
            final Insets insets = parent.getInsets();
            final int width2 = maxWidth + insets.left + insets.right;
            final int height2 = maxHeight + insets.top + insets.bottom;
            return new Dimension(width2, height2);
        }
    }
    
    private static int[] computeGridOrigins(final Container container, final int totalSize, final int offset, final List formSpecs, final List[] componentLists, final int[][] groupIndices, final Measure minMeasure, final Measure prefMeasure) {
        final int[] minSizes = maximumSizes(container, formSpecs, componentLists, minMeasure, prefMeasure, minMeasure);
        final int[] prefSizes = maximumSizes(container, formSpecs, componentLists, minMeasure, prefMeasure, prefMeasure);
        final int[] groupedMinSizes = groupedSizes(groupIndices, minSizes);
        final int[] groupedPrefSizes = groupedSizes(groupIndices, prefSizes);
        final int totalMinSize = sum(groupedMinSizes);
        final int totalPrefSize = sum(groupedPrefSizes);
        final int[] compressedSizes = compressedSizes(formSpecs, totalSize, totalMinSize, totalPrefSize, groupedMinSizes, prefSizes);
        final int[] groupedSizes = groupedSizes(groupIndices, compressedSizes);
        final int totalGroupedSize = sum(groupedSizes);
        final int[] sizes = distributedSizes(formSpecs, totalSize, totalGroupedSize, groupedSizes);
        return computeOrigins(sizes, offset);
    }
    
    private static int[] computeOrigins(final int[] sizes, final int offset) {
        final int count = sizes.length;
        final int[] origins = new int[count + 1];
        origins[0] = offset;
        for (int i = 1; i <= count; ++i) {
            origins[i] = origins[i - 1] + sizes[i - 1];
        }
        return origins;
    }
    
    private void layoutComponents(final int[] x, final int[] y) {
        final Rectangle cellBounds = new Rectangle();
        for (final Object element : this.constraintMap.entrySet()) {
            final Map.Entry entry = (Map.Entry)element;
            final Component component = entry.getKey();
            final CellConstraints constraints = entry.getValue();
            final int gridX = constraints.gridX - 1;
            final int gridY = constraints.gridY - 1;
            final int gridWidth = constraints.gridWidth;
            final int gridHeight = constraints.gridHeight;
            cellBounds.x = x[gridX];
            cellBounds.y = y[gridY];
            cellBounds.width = x[gridX + gridWidth] - cellBounds.x;
            cellBounds.height = y[gridY + gridHeight] - cellBounds.y;
            constraints.setBounds(component, this, cellBounds, this.minimumWidthMeasure, this.minimumHeightMeasure, this.preferredWidthMeasure, this.preferredHeightMeasure);
        }
    }
    
    private void invalidateCaches() {
        this.componentSizeCache.invalidate();
    }
    
    private static int[] maximumSizes(final Container container, final List formSpecs, final List[] componentLists, final Measure minMeasure, final Measure prefMeasure, final Measure defaultMeasure) {
        final int size = formSpecs.size();
        final int[] result = new int[size];
        for (int i = 0; i < size; ++i) {
            final FormSpec formSpec = formSpecs.get(i);
            result[i] = formSpec.maximumSize(container, componentLists[i], minMeasure, prefMeasure, defaultMeasure);
        }
        return result;
    }
    
    private static int[] compressedSizes(final List formSpecs, final int totalSize, final int totalMinSize, final int totalPrefSize, final int[] minSizes, final int[] prefSizes) {
        if (totalSize < totalMinSize) {
            return minSizes;
        }
        if (totalSize >= totalPrefSize) {
            return prefSizes;
        }
        final int count = formSpecs.size();
        final int[] sizes = new int[count];
        final double totalCompressionSpace = totalPrefSize - totalSize;
        final double maxCompressionSpace = totalPrefSize - totalMinSize;
        final double compressionFactor = totalCompressionSpace / maxCompressionSpace;
        for (int i = 0; i < count; ++i) {
            final FormSpec formSpec = formSpecs.get(i);
            sizes[i] = prefSizes[i];
            if (formSpec.getSize().compressible()) {
                final int[] array = sizes;
                final int n = i;
                array[n] -= (int)Math.round((prefSizes[i] - minSizes[i]) * compressionFactor);
            }
        }
        return sizes;
    }
    
    private static int[] groupedSizes(final int[][] groups, final int[] rawSizes) {
        if (groups == null || groups.length == 0) {
            return rawSizes;
        }
        final int[] sizes = new int[rawSizes.length];
        for (int i = 0; i < sizes.length; ++i) {
            sizes[i] = rawSizes[i];
        }
        for (final int[] groupIndices : groups) {
            int groupMaxSize = 0;
            for (final int groupIndice : groupIndices) {
                final int index = groupIndice - 1;
                groupMaxSize = Math.max(groupMaxSize, sizes[index]);
            }
            for (final int groupIndice : groupIndices) {
                final int index = groupIndice - 1;
                sizes[index] = groupMaxSize;
            }
        }
        return sizes;
    }
    
    private static int[] distributedSizes(final List formSpecs, final int totalSize, final int totalPrefSize, final int[] inputSizes) {
        final double totalFreeSpace = totalSize - totalPrefSize;
        if (totalFreeSpace < 0.0) {
            return inputSizes;
        }
        final int count = formSpecs.size();
        double totalWeight = 0.0;
        for (int i = 0; i < count; ++i) {
            final FormSpec formSpec = formSpecs.get(i);
            totalWeight += formSpec.getResizeWeight();
        }
        if (totalWeight == 0.0) {
            return inputSizes;
        }
        final int[] sizes = new int[count];
        double restSpace = totalFreeSpace;
        int roundedRestSpace = (int)totalFreeSpace;
        for (int j = 0; j < count; ++j) {
            final FormSpec formSpec2 = formSpecs.get(j);
            final double weight = formSpec2.getResizeWeight();
            if (weight == 0.0) {
                sizes[j] = inputSizes[j];
            }
            else {
                final double roundingCorrection = restSpace - roundedRestSpace;
                final double extraSpace = totalFreeSpace * weight / totalWeight;
                final double correctedExtraSpace = extraSpace - roundingCorrection;
                final int roundedExtraSpace = (int)Math.round(correctedExtraSpace);
                sizes[j] = inputSizes[j] + roundedExtraSpace;
                restSpace -= extraSpace;
                roundedRestSpace -= roundedExtraSpace;
            }
        }
        return sizes;
    }
    
    private static int[] computeMaximumFixedSpanTable(final List formSpecs) {
        final int size = formSpecs.size();
        final int[] table = new int[size];
        int maximumFixedSpan = Integer.MAX_VALUE;
        for (int i = size - 1; i >= 0; --i) {
            final FormSpec spec = formSpecs.get(i);
            if (spec.canGrow()) {
                maximumFixedSpan = 0;
            }
            if ((table[i] = maximumFixedSpan) < Integer.MAX_VALUE) {
                ++maximumFixedSpan;
            }
        }
        return table;
    }
    
    private static int sum(final int[] sizes) {
        int sum = 0;
        for (int i = sizes.length - 1; i >= 0; --i) {
            sum += sizes[i];
        }
        return sum;
    }
    
    private static void invalidateAndRepaint(final Container container) {
        if (container == null) {
            return;
        }
        if (container instanceof JComponent) {
            ((JComponent)container).revalidate();
        }
        else {
            container.invalidate();
        }
        container.repaint();
    }
    
    private boolean takeIntoAccount(final Component component, final CellConstraints cc) {
        return component.isVisible() || (cc.honorsVisibility == null && !this.getHonorsVisibility()) || Boolean.FALSE.equals(cc.honorsVisibility);
    }
    
    public LayoutInfo getLayoutInfo(final Container parent) {
        synchronized (parent.getTreeLock()) {
            this.initializeColAndRowComponentLists();
            final Dimension size = parent.getSize();
            final Insets insets = parent.getInsets();
            final int totalWidth = size.width - insets.left - insets.right;
            final int totalHeight = size.height - insets.top - insets.bottom;
            final int[] x = computeGridOrigins(parent, totalWidth, insets.left, this.colSpecs, this.colComponents, this.colGroupIndices, this.minimumWidthMeasure, this.preferredWidthMeasure);
            final int[] y = computeGridOrigins(parent, totalHeight, insets.top, this.rowSpecs, this.rowComponents, this.rowGroupIndices, this.minimumHeightMeasure, this.preferredHeightMeasure);
            return new LayoutInfo(x, y);
        }
    }
    
    private static int[][] deepClone(final int[][] array) {
        final int[][] result = new int[array.length][];
        for (int i = 0; i < result.length; ++i) {
            result[i] = array[i].clone();
        }
        return result;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.invalidateCaches();
        out.defaultWriteObject();
    }
    
    private abstract static class CachingMeasure implements Measure, Serializable
    {
        protected final ComponentSizeCache cache;
        
        private CachingMeasure(final ComponentSizeCache cache) {
            this.cache = cache;
        }
    }
    
    private static final class MinimumWidthMeasure extends CachingMeasure
    {
        private MinimumWidthMeasure(final ComponentSizeCache cache) {
            super(cache);
        }
        
        @Override
        public int sizeOf(final Component c) {
            return this.cache.getMinimumSize(c).width;
        }
    }
    
    private static final class MinimumHeightMeasure extends CachingMeasure
    {
        private MinimumHeightMeasure(final ComponentSizeCache cache) {
            super(cache);
        }
        
        @Override
        public int sizeOf(final Component c) {
            return this.cache.getMinimumSize(c).height;
        }
    }
    
    private static final class PreferredWidthMeasure extends CachingMeasure
    {
        private PreferredWidthMeasure(final ComponentSizeCache cache) {
            super(cache);
        }
        
        @Override
        public int sizeOf(final Component c) {
            return this.cache.getPreferredSize(c).width;
        }
    }
    
    private static final class PreferredHeightMeasure extends CachingMeasure
    {
        private PreferredHeightMeasure(final ComponentSizeCache cache) {
            super(cache);
        }
        
        @Override
        public int sizeOf(final Component c) {
            return this.cache.getPreferredSize(c).height;
        }
    }
    
    private static final class ComponentSizeCache implements Serializable
    {
        private final Map<Component, Dimension> minimumSizes;
        private final Map<Component, Dimension> preferredSizes;
        
        private ComponentSizeCache(final int initialCapacity) {
            this.minimumSizes = new HashMap<Component, Dimension>(initialCapacity);
            this.preferredSizes = new HashMap<Component, Dimension>(initialCapacity);
        }
        
        void invalidate() {
            this.minimumSizes.clear();
            this.preferredSizes.clear();
        }
        
        Dimension getMinimumSize(final Component component) {
            Dimension size = this.minimumSizes.get(component);
            if (size == null) {
                size = component.getMinimumSize();
                this.minimumSizes.put(component, size);
            }
            return size;
        }
        
        Dimension getPreferredSize(final Component component) {
            Dimension size = this.preferredSizes.get(component);
            if (size == null) {
                size = component.getPreferredSize();
                this.preferredSizes.put(component, size);
            }
            return size;
        }
        
        void removeEntry(final Component component) {
            this.minimumSizes.remove(component);
            this.preferredSizes.remove(component);
        }
    }
    
    public static final class LayoutInfo
    {
        public final int[] columnOrigins;
        public final int[] rowOrigins;
        
        private LayoutInfo(final int[] xOrigins, final int[] yOrigins) {
            this.columnOrigins = xOrigins;
            this.rowOrigins = yOrigins;
        }
        
        public int getX() {
            return this.columnOrigins[0];
        }
        
        public int getY() {
            return this.rowOrigins[0];
        }
        
        public int getWidth() {
            return this.columnOrigins[this.columnOrigins.length - 1] - this.columnOrigins[0];
        }
        
        public int getHeight() {
            return this.rowOrigins[this.rowOrigins.length - 1] - this.rowOrigins[0];
        }
    }
    
    public interface Measure
    {
        int sizeOf(final Component p0);
    }
}
