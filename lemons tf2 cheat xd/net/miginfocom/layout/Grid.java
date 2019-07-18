// 
// Decompiled by Procyon v0.5.36
// 

package net.miginfocom.layout;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class Grid
{
    public static final boolean TEST_GAPS = true;
    private static final Float[] GROW_100;
    private static final DimConstraint DOCK_DIM_CONSTRAINT;
    private static final int MAX_GRID = 30000;
    private static final int MAX_DOCK_GRID = 32767;
    private static final ResizeConstraint GAP_RC_CONST;
    private static final ResizeConstraint GAP_RC_CONST_PUSH;
    private final LC lc;
    private final ContainerWrapper container;
    private final LinkedHashMap<Integer, Cell> grid;
    private HashMap<Integer, BoundSize> wrapGapMap;
    private final TreeSet<Integer> rowIndexes;
    private final TreeSet<Integer> colIndexes;
    private final AC rowConstr;
    private final AC colConstr;
    private FlowSizeSpec colFlowSpecs;
    private FlowSizeSpec rowFlowSpecs;
    private final ArrayList<LinkedDimGroup>[] colGroupLists;
    private final ArrayList<LinkedDimGroup>[] rowGroupLists;
    private int[] width;
    private int[] height;
    private ArrayList<int[]> debugRects;
    private HashMap<String, Boolean> linkTargetIDs;
    private final int dockOffY;
    private final int dockOffX;
    private final Float[] pushXs;
    private final Float[] pushYs;
    private final ArrayList<LayoutCallback> callbackList;
    private static WeakHashMap[] PARENT_ROWCOL_SIZES_MAP;
    private static WeakHashMap<Object, LinkedHashMap<Integer, Cell>> PARENT_GRIDPOS_MAP;
    
    public Grid(final ContainerWrapper container, final LC lc, final AC rowConstr, final AC colConstr, final Map<ComponentWrapper, CC> map, final ArrayList<LayoutCallback> callbackList) {
        this.grid = new LinkedHashMap<Integer, Cell>();
        this.wrapGapMap = null;
        this.rowIndexes = new TreeSet<Integer>();
        this.colIndexes = new TreeSet<Integer>();
        this.colFlowSpecs = null;
        this.rowFlowSpecs = null;
        this.width = null;
        this.height = null;
        this.debugRects = null;
        this.linkTargetIDs = null;
        this.lc = lc;
        this.rowConstr = rowConstr;
        this.colConstr = colConstr;
        this.container = container;
        this.callbackList = callbackList;
        final int n = (lc.getWrapAfter() != 0) ? lc.getWrapAfter() : (lc.isFlowX() ? colConstr : rowConstr).getConstaints().length;
        final ComponentWrapper[] components = container.getComponents();
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        int n2 = 0;
        final int[] array = new int[2];
        final ArrayList<int[]> list = new ArrayList<int[]>(2);
        final DimConstraint[] constaints = (lc.isFlowX() ? rowConstr : colConstr).getConstaints();
        int n3 = 0;
        int n4 = 0;
        int[] array2 = null;
        LinkHandler.clearTemporaryBounds(container.getLayout());
        int i = 0;
        while (i < components.length) {
            final ComponentWrapper componentWrapper = components[i];
            final CC cc = getCC(componentWrapper, map);
            this.addLinkIDs(cc);
            final int n5 = componentWrapper.isVisible() ? -1 : ((cc.getHideMode() != -1) ? cc.getHideMode() : lc.getHideMode());
            if (n5 == 3) {
                this.setLinkedBounds(componentWrapper, cc, componentWrapper.getX(), componentWrapper.getY(), componentWrapper.getWidth(), componentWrapper.getHeight(), cc.isExternal());
                ++i;
            }
            else {
                if (cc.getHorizontal().getSizeGroup() != null) {
                    ++n3;
                }
                if (cc.getVertical().getSizeGroup() != null) {
                    ++n4;
                }
                UnitValue[] array3 = this.getPos(componentWrapper, cc);
                BoundSize[] array4 = this.getCallbackSize(componentWrapper);
                if (array3 != null || cc.isExternal()) {
                    final CompWrap compWrap = new CompWrap(componentWrapper, cc, n5, array3, array4);
                    final Cell cell = this.grid.get(null);
                    if (cell == null) {
                        this.grid.put(null, new Cell(compWrap));
                    }
                    else {
                        cell.compWraps.add(compWrap);
                    }
                    if (!cc.isBoundsInGrid() || cc.isExternal()) {
                        this.setLinkedBounds(componentWrapper, cc, componentWrapper.getX(), componentWrapper.getY(), componentWrapper.getWidth(), componentWrapper.getHeight(), cc.isExternal());
                        ++i;
                        continue;
                    }
                }
                if (cc.getDockSide() != -1) {
                    if (array2 == null) {
                        array2 = new int[] { -32767, -32767, 32767, 32767 };
                    }
                    this.addDockingCell(array2, cc.getDockSide(), new CompWrap(componentWrapper, cc, n5, array3, array4));
                    ++i;
                }
                else {
                    final Boolean flowX = cc.getFlowX();
                    Cell cell2 = null;
                    if (cc.isNewline()) {
                        this.wrap(array, cc.getNewlineGapSize());
                    }
                    else if (n2 != 0) {
                        this.wrap(array, null);
                    }
                    n2 = 0;
                    final boolean b4 = lc.isNoGrid() || ((DimConstraint)LayoutUtil.getIndexSafe(constaints, lc.isFlowX() ? array[1] : array[0])).isNoGrid();
                    final int cellX = cc.getCellX();
                    final int cellY = cc.getCellY();
                    if ((cellX < 0 || cellY < 0) && !b4 && cc.getSkip() == 0) {
                        while (!this.isCellFree(array[1], array[0], list)) {
                            if (Math.abs(this.increase(array, 1)) >= n) {
                                this.wrap(array, null);
                            }
                        }
                    }
                    else {
                        if (cellX >= 0 && cellY >= 0) {
                            if (cellY >= 0) {
                                array[0] = cellX;
                                array[1] = cellY;
                            }
                            else if (lc.isFlowX()) {
                                array[0] = cellX;
                            }
                            else {
                                array[1] = cellX;
                            }
                        }
                        cell2 = this.getCell(array[1], array[0]);
                    }
                    for (int j = 0; j < cc.getSkip(); ++j) {
                        do {
                            if (Math.abs(this.increase(array, 1)) >= n) {
                                this.wrap(array, null);
                            }
                        } while (!this.isCellFree(array[1], array[0], list));
                    }
                    if (cell2 == null) {
                        final int min = Math.min((b4 && lc.isFlowX()) ? 2097051 : cc.getSpanX(), 30000 - array[0]);
                        final int min2 = Math.min((b4 && !lc.isFlowX()) ? 2097051 : cc.getSpanY(), 30000 - array[1]);
                        cell2 = new Cell(min, min2, (flowX != null) ? ((boolean)flowX) : lc.isFlowX());
                        this.setCell(array[1], array[0], cell2);
                        if (min > 1 || min2 > 1) {
                            list.add(new int[] { array[0], array[1], min, min2 });
                        }
                    }
                    boolean b5 = false;
                    int n6 = b4 ? 2097051 : (cc.getSplit() - 1);
                    boolean b6 = false;
                    final boolean b7 = (lc.isFlowX() ? cc.getSpanX() : cc.getSpanY()) == 2097051;
                    while (n6 >= 0 && i < components.length) {
                        final ComponentWrapper componentWrapper2 = components[i];
                        final CC cc2 = getCC(componentWrapper2, map);
                        this.addLinkIDs(cc2);
                        final boolean visible = componentWrapper2.isVisible();
                        final int n7 = visible ? -1 : ((cc2.getHideMode() != -1) ? cc2.getHideMode() : lc.getHideMode());
                        if (cc2.isExternal() || n7 == 3) {
                            ++i;
                            ++n6;
                        }
                        else {
                            b2 |= ((visible || n7 > 1) && cc2.getPushX() != null);
                            b3 |= ((visible || n7 > 1) && cc2.getPushY() != null);
                            if (cc2 != cc) {
                                if (cc2.isNewline() || !cc2.isBoundsInGrid()) {
                                    break;
                                }
                                if (cc2.getDockSide() != -1) {
                                    break;
                                }
                                if (n6 > 0 && cc2.getSkip() > 0) {
                                    b6 = true;
                                    break;
                                }
                                array3 = this.getPos(componentWrapper2, cc2);
                                array4 = this.getCallbackSize(componentWrapper2);
                            }
                            cell2.compWraps.add(new CompWrap(componentWrapper2, cc2, n7, array3, array4));
                            Cell.access$476(cell2, (cc2.getTag() != null) ? 1 : 0);
                            b |= cell2.hasTagged;
                            if (cc2 != cc) {
                                if (cc2.getHorizontal().getSizeGroup() != null) {
                                    ++n3;
                                }
                                if (cc2.getVertical().getSizeGroup() != null) {
                                    ++n4;
                                }
                            }
                            ++i;
                            if (cc2.isWrap() || (b7 && n6 == 0)) {
                                if (cc2.isWrap()) {
                                    this.wrap(array, cc2.getWrapGapSize());
                                }
                                else {
                                    n2 = 1;
                                }
                                b5 = true;
                                break;
                            }
                        }
                        --n6;
                    }
                    if (b5 || b4) {
                        continue;
                    }
                    final int n8 = lc.isFlowX() ? cell2.spanx : cell2.spany;
                    if (Math.abs(lc.isFlowX() ? array[0] : array[1]) + n8 >= n) {
                        n2 = 1;
                    }
                    else {
                        this.increase(array, b6 ? (n8 - 1) : n8);
                    }
                }
            }
        }
        if (n3 > 0 || n4 > 0) {
            final HashMap hashMap = (n3 > 0) ? new HashMap<String, int[]>(n3) : null;
            final HashMap hashMap2 = (n4 > 0) ? new HashMap<String, int[]>(n4) : null;
            final ArrayList list2 = new ArrayList<CompWrap>(Math.max(n3, n4));
            for (final Cell cell3 : this.grid.values()) {
                for (int k = 0; k < cell3.compWraps.size(); ++k) {
                    final CompWrap compWrap2 = cell3.compWraps.get(k);
                    final String sizeGroup = compWrap2.cc.getHorizontal().getSizeGroup();
                    final String sizeGroup2 = compWrap2.cc.getVertical().getSizeGroup();
                    if (sizeGroup != null || sizeGroup2 != null) {
                        if (sizeGroup != null && hashMap != null) {
                            addToSizeGroup((HashMap<String, int[]>)hashMap, sizeGroup, compWrap2.horSizes);
                        }
                        if (sizeGroup2 != null && hashMap2 != null) {
                            addToSizeGroup((HashMap<String, int[]>)hashMap2, sizeGroup2, compWrap2.verSizes);
                        }
                        list2.add(compWrap2);
                    }
                }
            }
            for (int l = 0; l < list2.size(); ++l) {
                final CompWrap compWrap3 = list2.get(l);
                if (hashMap != null) {
                    compWrap3.setSizes(hashMap.get(compWrap3.cc.getHorizontal().getSizeGroup()), true);
                }
                if (hashMap2 != null) {
                    compWrap3.setSizes(hashMap2.get(compWrap3.cc.getVertical().getSizeGroup()), false);
                }
            }
        }
        if (n3 > 0 || n4 > 0) {
            final HashMap hashMap3 = (n3 > 0) ? new HashMap<String, int[]>(n3) : null;
            final HashMap hashMap4 = (n4 > 0) ? new HashMap<String, int[]>(n4) : null;
            final ArrayList list3 = new ArrayList<CompWrap>(Math.max(n3, n4));
            for (final Cell cell4 : this.grid.values()) {
                for (int n9 = 0; n9 < cell4.compWraps.size(); ++n9) {
                    final CompWrap compWrap4 = cell4.compWraps.get(n9);
                    final String sizeGroup3 = compWrap4.cc.getHorizontal().getSizeGroup();
                    final String sizeGroup4 = compWrap4.cc.getVertical().getSizeGroup();
                    if (sizeGroup3 != null || sizeGroup4 != null) {
                        if (sizeGroup3 != null && hashMap3 != null) {
                            addToSizeGroup((HashMap<String, int[]>)hashMap3, sizeGroup3, compWrap4.horSizes);
                        }
                        if (sizeGroup4 != null && hashMap4 != null) {
                            addToSizeGroup((HashMap<String, int[]>)hashMap4, sizeGroup4, compWrap4.verSizes);
                        }
                        list3.add(compWrap4);
                    }
                }
            }
            for (int n10 = 0; n10 < list3.size(); ++n10) {
                final CompWrap compWrap5 = list3.get(n10);
                if (hashMap3 != null) {
                    compWrap5.setSizes(hashMap3.get(compWrap5.cc.getHorizontal().getSizeGroup()), true);
                }
                if (hashMap4 != null) {
                    compWrap5.setSizes(hashMap4.get(compWrap5.cc.getVertical().getSizeGroup()), false);
                }
            }
        }
        if (b) {
            sortCellsByPlatform(this.grid.values(), container);
        }
        final boolean leftToRight = LayoutUtil.isLeftToRight(lc, container);
        for (final Cell cell5 : this.grid.values()) {
            final ArrayList access$200 = cell5.compWraps;
            for (int n11 = 0, n12 = access$200.size() - 1; n11 <= n12; ++n11) {
                final CompWrap compWrap6 = access$200.get(n11);
                final ComponentWrapper componentWrapper3 = (n11 > 0) ? access$200.get(n11 - 1).comp : null;
                final ComponentWrapper componentWrapper4 = (n11 < n12) ? access$200.get(n11 + 1).comp : null;
                compWrap6.calcGaps(componentWrapper3, (componentWrapper3 != null) ? getCC(componentWrapper3, map) : null, componentWrapper4, (componentWrapper4 != null) ? getCC(componentWrapper4, map) : null, getCC(compWrap6.comp, map).getTag(), cell5.flowx, leftToRight);
            }
        }
        this.dockOffX = getDockInsets(this.colIndexes);
        this.dockOffY = getDockInsets(this.rowIndexes);
        for (int n13 = 0; n13 < rowConstr.getCount(); ++n13) {
            this.rowIndexes.add(n13);
        }
        for (int n14 = 0; n14 < colConstr.getCount(); ++n14) {
            this.colIndexes.add(n14);
        }
        this.colGroupLists = this.divideIntoLinkedGroups(false);
        this.rowGroupLists = this.divideIntoLinkedGroups(true);
        this.pushXs = (Float[])((b2 || lc.isFillX()) ? this.getDefaultPushWeights(false) : null);
        this.pushYs = (Float[])((b3 || lc.isFillY()) ? this.getDefaultPushWeights(true) : null);
        if (LayoutUtil.isDesignTime(container)) {
            saveGrid(container, this.grid);
        }
    }
    
    private static CC getCC(final ComponentWrapper componentWrapper, final Map<ComponentWrapper, CC> map) {
        final CC cc = map.get(componentWrapper);
        return (cc != null) ? cc : new CC();
    }
    
    private void addLinkIDs(final CC cc) {
        final String[] linkTargets = cc.getLinkTargets();
        for (int i = 0; i < linkTargets.length; ++i) {
            if (this.linkTargetIDs == null) {
                this.linkTargetIDs = new HashMap<String, Boolean>();
            }
            this.linkTargetIDs.put(linkTargets[i], null);
        }
    }
    
    public void invalidateContainerSize() {
        this.colFlowSpecs = null;
    }
    
    public boolean layout(final int[] array, final UnitValue unitValue, final UnitValue unitValue2, final boolean b, final boolean b2) {
        if (b) {
            this.debugRects = new ArrayList<int[]>();
        }
        this.checkSizeCalcs();
        this.resetLinkValues(true, true);
        this.layoutInOneDim(array[2], unitValue, false, this.pushXs);
        this.layoutInOneDim(array[3], unitValue2, true, this.pushYs);
        HashMap<String, Integer> addToEndGroup = null;
        HashMap<String, Integer> addToEndGroup2 = null;
        final int componentCount = this.container.getComponentCount();
        int n = 0;
        if (componentCount > 0) {
            for (int i = 0; i < ((this.linkTargetIDs != null) ? 2 : 1); ++i) {
                int n2 = 0;
                boolean b3;
                do {
                    b3 = false;
                    final Iterator<Cell> iterator = this.grid.values().iterator();
                    while (iterator.hasNext()) {
                        final ArrayList access$200 = iterator.next().compWraps;
                        for (int j = 0; j < access$200.size(); ++j) {
                            final CompWrap compWrap = access$200.get(j);
                            if (i == 0) {
                                b3 |= this.doAbsoluteCorrections(compWrap, array);
                                if (!b3) {
                                    if (compWrap.cc.getHorizontal().getEndGroup() != null) {
                                        addToEndGroup = addToEndGroup(addToEndGroup, compWrap.cc.getHorizontal().getEndGroup(), compWrap.x + compWrap.w);
                                    }
                                    if (compWrap.cc.getVertical().getEndGroup() != null) {
                                        addToEndGroup2 = addToEndGroup(addToEndGroup2, compWrap.cc.getVertical().getEndGroup(), compWrap.y + compWrap.h);
                                    }
                                }
                                if (this.linkTargetIDs != null && (this.linkTargetIDs.containsKey("visual") || this.linkTargetIDs.containsKey("container"))) {
                                    n = 1;
                                }
                            }
                            if (this.linkTargetIDs == null || i == 1) {
                                if (compWrap.cc.getHorizontal().getEndGroup() != null) {
                                    compWrap.w = addToEndGroup.get(compWrap.cc.getHorizontal().getEndGroup()) - compWrap.x;
                                }
                                if (compWrap.cc.getVertical().getEndGroup() != null) {
                                    compWrap.h = addToEndGroup2.get(compWrap.cc.getVertical().getEndGroup()) - compWrap.y;
                                }
                                compWrap.x += array[0];
                                compWrap.y += array[1];
                                n |= (compWrap.transferBounds(b2 && n == 0) ? 1 : 0);
                                if (this.callbackList != null) {
                                    for (int k = 0; k < this.callbackList.size(); ++k) {
                                        this.callbackList.get(k).correctBounds(compWrap.comp);
                                    }
                                }
                            }
                        }
                    }
                    this.clearGroupLinkBounds();
                    if (++n2 > (componentCount << 3) + 10) {
                        System.err.println("Unstable cyclic dependency in absolute linked values!");
                        break;
                    }
                } while (b3);
            }
        }
        if (b) {
            final Iterator<Cell> iterator2 = this.grid.values().iterator();
            while (iterator2.hasNext()) {
                final ArrayList access$201 = iterator2.next().compWraps;
                for (int l = 0; l < access$201.size(); ++l) {
                    final CompWrap compWrap2 = access$201.get(l);
                    final LinkedDimGroup groupContaining = getGroupContaining(this.colGroupLists, compWrap2);
                    final LinkedDimGroup groupContaining2 = getGroupContaining(this.rowGroupLists, compWrap2);
                    if (groupContaining != null && groupContaining2 != null) {
                        this.debugRects.add(new int[] { groupContaining.lStart + array[0] - (groupContaining.fromEnd ? groupContaining.lSize : 0), groupContaining2.lStart + array[1] - (groupContaining2.fromEnd ? groupContaining2.lSize : 0), groupContaining.lSize, groupContaining2.lSize });
                    }
                }
            }
        }
        return n != 0;
    }
    
    public void paintDebug() {
        if (this.debugRects != null) {
            this.container.paintDebugOutline();
            final ArrayList<int[]> list = new ArrayList<int[]>();
            for (int i = 0; i < this.debugRects.size(); ++i) {
                final int[] array = this.debugRects.get(i);
                if (!list.contains(array)) {
                    this.container.paintDebugCell(array[0], array[1], array[2], array[3]);
                    list.add(array);
                }
            }
            final Iterator<Cell> iterator = this.grid.values().iterator();
            while (iterator.hasNext()) {
                final ArrayList access$200 = iterator.next().compWraps;
                for (int j = 0; j < access$200.size(); ++j) {
                    access$200.get(j).comp.paintDebugOutline();
                }
            }
        }
    }
    
    public ContainerWrapper getContainer() {
        return this.container;
    }
    
    public final int[] getWidth() {
        this.checkSizeCalcs();
        return this.width.clone();
    }
    
    public final int[] getHeight() {
        this.checkSizeCalcs();
        return this.height.clone();
    }
    
    private void checkSizeCalcs() {
        if (this.colFlowSpecs == null) {
            this.colFlowSpecs = this.calcRowsOrColsSizes(true);
            this.rowFlowSpecs = this.calcRowsOrColsSizes(false);
            this.width = this.getMinPrefMaxSumSize(true);
            this.height = this.getMinPrefMaxSumSize(false);
            if (this.linkTargetIDs == null) {
                this.resetLinkValues(false, true);
            }
            else {
                this.layout(new int[4], null, null, false, false);
                this.resetLinkValues(false, false);
            }
            this.adjustSizeForAbsolute(true);
            this.adjustSizeForAbsolute(false);
        }
    }
    
    private UnitValue[] getPos(final ComponentWrapper componentWrapper, final CC cc) {
        UnitValue[] position = null;
        if (this.callbackList != null) {
            for (int n = 0; n < this.callbackList.size() && position == null; position = this.callbackList.get(n).getPosition(componentWrapper), ++n) {}
        }
        final UnitValue[] pos = cc.getPos();
        if (position == null || pos == null) {
            return (position != null) ? position : pos;
        }
        for (int i = 0; i < 4; ++i) {
            final UnitValue unitValue = position[i];
            if (unitValue != null) {
                pos[i] = unitValue;
            }
        }
        return pos;
    }
    
    private BoundSize[] getCallbackSize(final ComponentWrapper componentWrapper) {
        if (this.callbackList != null) {
            for (int i = 0; i < this.callbackList.size(); ++i) {
                final BoundSize[] size = this.callbackList.get(i).getSize(componentWrapper);
                if (size != null) {
                    return size;
                }
            }
        }
        return null;
    }
    
    private static int getDockInsets(final TreeSet<Integer> set) {
        int n = 0;
        final Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext() && iterator.next() < -30000) {
            ++n;
        }
        return n;
    }
    
    private boolean setLinkedBounds(final ComponentWrapper componentWrapper, final CC cc, final int n, final int n2, final int n3, final int n4, final boolean b) {
        String substring = (cc.getId() != null) ? cc.getId() : componentWrapper.getLinkId();
        if (substring == null) {
            return false;
        }
        String substring2 = null;
        final int index = substring.indexOf(46);
        if (index != -1) {
            substring2 = substring.substring(0, index);
            substring = substring.substring(index + 1);
        }
        final Object layout = this.container.getLayout();
        boolean setBounds = false;
        if (b || (this.linkTargetIDs != null && this.linkTargetIDs.containsKey(substring))) {
            setBounds = LinkHandler.setBounds(layout, substring, n, n2, n3, n4, !b, false);
        }
        if (substring2 != null && (b || (this.linkTargetIDs != null && this.linkTargetIDs.containsKey(substring2)))) {
            if (this.linkTargetIDs == null) {
                this.linkTargetIDs = new HashMap<String, Boolean>(4);
            }
            this.linkTargetIDs.put(substring2, Boolean.TRUE);
            setBounds |= LinkHandler.setBounds(layout, substring2, n, n2, n3, n4, !b, true);
        }
        return setBounds;
    }
    
    private int increase(final int[] array, final int n) {
        int n3;
        if (this.lc.isFlowX()) {
            final int n2 = 0;
            n3 = (array[n2] += n);
        }
        else {
            final int n4 = 1;
            n3 = (array[n4] += n);
        }
        return n3;
    }
    
    private void wrap(final int[] array, final BoundSize boundSize) {
        final int flowX = this.lc.isFlowX() ? 1 : 0;
        array[0] = ((flowX != 0) ? 0 : (array[0] + 1));
        array[1] = ((flowX != 0) ? (array[1] + 1) : 0);
        if (boundSize != null) {
            if (this.wrapGapMap == null) {
                this.wrapGapMap = new HashMap<Integer, BoundSize>(8);
            }
            this.wrapGapMap.put(array[flowX], boundSize);
        }
        if (flowX != 0) {
            this.rowIndexes.add(array[1]);
        }
        else {
            this.colIndexes.add(array[0]);
        }
    }
    
    private static void sortCellsByPlatform(final Collection<Cell> collection, final ContainerWrapper containerWrapper) {
        final String buttonOrder = PlatformDefaults.getButtonOrder();
        final String lowerCase = buttonOrder.toLowerCase();
        final int convertToPixels = PlatformDefaults.convertToPixels(1.0f, "u", true, 0.0f, containerWrapper, null);
        if (convertToPixels == -87654312) {
            throw new IllegalArgumentException("'unrelated' not recognized by PlatformDefaults!");
        }
        final int[] array = { convertToPixels, convertToPixels, -2147471302 };
        final int[] array2 = { 0, 0, -2147471302 };
        for (final Cell cell : collection) {
            if (!cell.hasTagged) {
                continue;
            }
            CompWrap compWrap = null;
            int n = 0;
            int n2 = 0;
            final ArrayList list = new ArrayList<CompWrap>(cell.compWraps.size());
            for (int i = 0; i < lowerCase.length(); ++i) {
                final char char1 = lowerCase.charAt(i);
                if (char1 == '+' || char1 == '_') {
                    n = 1;
                    if (char1 == '+') {
                        n2 = 1;
                    }
                }
                else {
                    final String tagForChar = PlatformDefaults.getTagForChar(char1);
                    if (tagForChar != null) {
                        for (int j = 0; j < cell.compWraps.size(); ++j) {
                            final CompWrap compWrap2 = cell.compWraps.get(j);
                            if (tagForChar.equals(compWrap2.cc.getTag())) {
                                if (Character.isUpperCase(buttonOrder.charAt(i))) {
                                    final int pixels = PlatformDefaults.getMinimumButtonWidth().getPixels(0.0f, containerWrapper, compWrap2.comp);
                                    if (pixels > compWrap2.horSizes[0]) {
                                        compWrap2.horSizes[0] = pixels;
                                    }
                                    correctMinMax(compWrap2.horSizes);
                                }
                                list.add(compWrap2);
                                if (n != 0) {
                                    ((compWrap != null) ? compWrap : compWrap2).mergeGapSizes(array, cell.flowx, compWrap == null);
                                    if (n2 != 0) {
                                        compWrap2.forcedPushGaps = 1;
                                        n = 0;
                                        n2 = 0;
                                    }
                                }
                                if (char1 == 'u') {
                                    n = 1;
                                }
                                compWrap = compWrap2;
                            }
                        }
                    }
                }
            }
            if (list.size() > 0) {
                final CompWrap compWrap3 = list.get(list.size() - 1);
                if (n != 0) {
                    compWrap3.mergeGapSizes(array, cell.flowx, false);
                    if (n2 != 0) {
                        compWrap3.forcedPushGaps |= 0x2;
                    }
                }
                if (compWrap3.cc.getHorizontal().getGapAfter() == null) {
                    compWrap3.setGaps(array2, 3);
                }
                final CompWrap compWrap4 = list.get(0);
                if (compWrap4.cc.getHorizontal().getGapBefore() == null) {
                    compWrap4.setGaps(array2, 1);
                }
            }
            if (cell.compWraps.size() == list.size()) {
                cell.compWraps.clear();
            }
            else {
                cell.compWraps.removeAll(list);
            }
            cell.compWraps.addAll(list);
        }
    }
    
    private Float[] getDefaultPushWeights(final boolean b) {
        final ArrayList<LinkedDimGroup>[] array = b ? this.rowGroupLists : this.colGroupLists;
        Float[] grow_100 = Grid.GROW_100;
        for (int i = 0, n = 1; i < array.length; ++i, n += 2) {
            final ArrayList<LinkedDimGroup> list = array[i];
            Float n2 = null;
            for (int j = 0; j < list.size(); ++j) {
                final LinkedDimGroup linkedDimGroup = list.get(j);
                for (int k = 0; k < linkedDimGroup._compWraps.size(); ++k) {
                    final CompWrap compWrap = linkedDimGroup._compWraps.get(k);
                    final Float n3 = ((compWrap.comp.isVisible() ? -1 : ((compWrap.cc.getHideMode() != -1) ? compWrap.cc.getHideMode() : this.lc.getHideMode())) < 2) ? (b ? compWrap.cc.getPushY() : compWrap.cc.getPushX()) : null;
                    if (n2 == null || (n3 != null && n3 > n2)) {
                        n2 = n3;
                    }
                }
            }
            if (n2 != null) {
                if (grow_100 == Grid.GROW_100) {
                    grow_100 = new Float[(array.length << 1) + 1];
                }
                grow_100[n] = n2;
            }
        }
        return grow_100;
    }
    
    private void clearGroupLinkBounds() {
        if (this.linkTargetIDs == null) {
            return;
        }
        for (final Map.Entry<String, Boolean> entry : this.linkTargetIDs.entrySet()) {
            if (entry.getValue() == Boolean.TRUE) {
                LinkHandler.clearBounds(this.container.getLayout(), entry.getKey());
            }
        }
    }
    
    private void resetLinkValues(final boolean b, final boolean b2) {
        final Object layout = this.container.getLayout();
        if (b2) {
            LinkHandler.clearTemporaryBounds(layout);
        }
        final boolean b3 = !this.hasDocks();
        final int n = b ? this.lc.getWidth().constrain(this.container.getWidth(), (float)getParentSize(this.container, true), this.container) : 0;
        final int n2 = b ? this.lc.getHeight().constrain(this.container.getHeight(), (float)getParentSize(this.container, false), this.container) : 0;
        final int pixels = LayoutUtil.getInsets(this.lc, 0, b3).getPixels(0.0f, this.container, null);
        final int pixels2 = LayoutUtil.getInsets(this.lc, 1, b3).getPixels(0.0f, this.container, null);
        LinkHandler.setBounds(layout, "visual", pixels, pixels2, n - pixels - LayoutUtil.getInsets(this.lc, 2, b3).getPixels(0.0f, this.container, null), n2 - pixels2 - LayoutUtil.getInsets(this.lc, 3, b3).getPixels(0.0f, this.container, null), true, false);
        LinkHandler.setBounds(layout, "container", 0, 0, n, n2, true, false);
    }
    
    private static LinkedDimGroup getGroupContaining(final ArrayList<LinkedDimGroup>[] array, final CompWrap compWrap) {
        for (int i = 0; i < array.length; ++i) {
            final ArrayList<LinkedDimGroup> list = array[i];
            for (int j = 0; j < list.size(); ++j) {
                final ArrayList access$2500 = list.get(j)._compWraps;
                for (int k = 0; k < access$2500.size(); ++k) {
                    if (access$2500.get(k) == compWrap) {
                        return list.get(j);
                    }
                }
            }
        }
        return null;
    }
    
    private boolean doAbsoluteCorrections(final CompWrap compWrap, final int[] array) {
        boolean setLinkedBounds = false;
        final int[] absoluteDimBounds = this.getAbsoluteDimBounds(compWrap, array[2], true);
        if (absoluteDimBounds != null) {
            compWrap.setDimBounds(absoluteDimBounds[0], absoluteDimBounds[1], true);
        }
        final int[] absoluteDimBounds2 = this.getAbsoluteDimBounds(compWrap, array[3], false);
        if (absoluteDimBounds2 != null) {
            compWrap.setDimBounds(absoluteDimBounds2[0], absoluteDimBounds2[1], false);
        }
        if (this.linkTargetIDs != null) {
            setLinkedBounds = this.setLinkedBounds(compWrap.comp, compWrap.cc, compWrap.x, compWrap.y, compWrap.w, compWrap.h, false);
        }
        return setLinkedBounds;
    }
    
    private void adjustSizeForAbsolute(final boolean b) {
        final int[] array = b ? this.width : this.height;
        final Cell cell = this.grid.get(null);
        if (cell == null || cell.compWraps.size() == 0) {
            return;
        }
        final ArrayList access$200 = cell.compWraps;
        int n = 0;
        for (int i = 0, size = cell.compWraps.size(); i < size + 3; ++i) {
            boolean b2 = false;
            for (int j = 0; j < size; ++j) {
                final CompWrap compWrap = access$200.get(j);
                final int[] absoluteDimBounds = this.getAbsoluteDimBounds(compWrap, 0, b);
                final int n2 = absoluteDimBounds[0] + absoluteDimBounds[1];
                if (n < n2) {
                    n = n2;
                }
                if (this.linkTargetIDs != null) {
                    b2 |= this.setLinkedBounds(compWrap.comp, compWrap.cc, absoluteDimBounds[0], absoluteDimBounds[0], absoluteDimBounds[1], absoluteDimBounds[1], false);
                }
            }
            if (!b2) {
                break;
            }
            n = 0;
            this.clearGroupLinkBounds();
        }
        final int n3 = n + LayoutUtil.getInsets(this.lc, b ? 3 : 2, !this.hasDocks()).getPixels(0.0f, this.container, null);
        if (array[0] < n3) {
            array[0] = n3;
        }
        if (array[1] < n3) {
            array[1] = n3;
        }
    }
    
    private int[] getAbsoluteDimBounds(final CompWrap compWrap, final int n, final boolean b) {
        if (compWrap.cc.isExternal()) {
            if (b) {
                return new int[] { compWrap.comp.getX(), compWrap.comp.getWidth() };
            }
            return new int[] { compWrap.comp.getY(), compWrap.comp.getHeight() };
        }
        else {
            final int[] array = (int[])(this.lc.isVisualPadding() ? compWrap.comp.getVisualPadding() : null);
            final UnitValue[] padding = compWrap.cc.getPadding();
            if (compWrap.pos == null && array == null && padding == null) {
                return null;
            }
            int pixels = b ? compWrap.x : compWrap.y;
            int n2 = b ? compWrap.w : compWrap.h;
            if (compWrap.pos != null) {
                final UnitValue unitValue = (compWrap.pos != null) ? compWrap.pos[!b] : null;
                final UnitValue unitValue2 = (compWrap.pos != null) ? compWrap.pos[b ? 2 : 3] : null;
                final int access$2800 = compWrap.getSize(0, b);
                final int access$2801 = compWrap.getSize(2, b);
                n2 = Math.min(Math.max(compWrap.getSize(1, b), access$2800), access$2801);
                if (unitValue != null) {
                    pixels = unitValue.getPixels((unitValue.getUnit() == 12) ? ((float)n2) : ((float)n), this.container, compWrap.comp);
                    if (unitValue2 != null) {
                        n2 = Math.min(Math.max((b ? (compWrap.x + compWrap.w) : (compWrap.y + compWrap.h)) - pixels, access$2800), access$2801);
                    }
                }
                if (unitValue2 != null) {
                    if (unitValue != null) {
                        n2 = Math.min(Math.max(unitValue2.getPixels((float)n, this.container, compWrap.comp) - pixels, access$2800), access$2801);
                    }
                    else {
                        pixels = unitValue2.getPixels((float)n, this.container, compWrap.comp) - n2;
                    }
                }
            }
            if (padding != null) {
                final UnitValue unitValue3 = padding[b];
                final int n3 = (unitValue3 != null) ? unitValue3.getPixels((float)n, this.container, compWrap.comp) : 0;
                pixels += n3;
                final UnitValue unitValue4 = padding[b ? 3 : 2];
                n2 += -n3 + ((unitValue4 != null) ? unitValue4.getPixels((float)n, this.container, compWrap.comp) : 0);
            }
            if (array != null) {
                final int n4 = array[b];
                pixels += n4;
                n2 += -n4 + array[b ? 3 : 2];
            }
            return new int[] { pixels, n2 };
        }
    }
    
    private void layoutInOneDim(final int n, final UnitValue unitValue, final boolean b, final Float[] array) {
        boolean b2 = false;
        Label_0036: {
            Label_0035: {
                if (b) {
                    if (this.lc.isTopToBottom()) {
                        break Label_0035;
                    }
                }
                else if (LayoutUtil.isLeftToRight(this.lc, this.container)) {
                    break Label_0035;
                }
                b2 = true;
                break Label_0036;
            }
            b2 = false;
        }
        final boolean b3 = b2;
        final DimConstraint[] constaints = (b ? this.rowConstr : this.colConstr).getConstaints();
        final FlowSizeSpec flowSizeSpec = b ? this.rowFlowSpecs : this.colFlowSpecs;
        final ArrayList<LinkedDimGroup>[] array2 = b ? this.rowGroupLists : this.colGroupLists;
        final int[] calculateSerial = LayoutUtil.calculateSerial(flowSizeSpec.sizes, flowSizeSpec.resConstsInclGaps, array, 1, n);
        if (LayoutUtil.isDesignTime(this.container)) {
            final TreeSet<Integer> set = b ? this.rowIndexes : this.colIndexes;
            final int[] array3 = new int[set.size()];
            int n2 = 0;
            final Iterator<Integer> iterator = set.iterator();
            while (iterator.hasNext()) {
                array3[n2++] = iterator.next();
            }
            putSizesAndIndexes(this.container.getComponent(), calculateSerial, array3, b);
        }
        int n3 = (unitValue != null) ? unitValue.getPixels((float)(n - LayoutUtil.sum(calculateSerial)), this.container, null) : 0;
        if (b3) {
            n3 = n - n3;
        }
        for (int i = 0; i < array2.length; ++i) {
            final ArrayList<LinkedDimGroup> list = array2[i];
            final int n4 = i - (b ? this.dockOffY : this.dockOffX);
            final int n5 = i << 1;
            final int n6 = n5 + 1;
            final int n7 = n3 + (b3 ? (-calculateSerial[n5]) : calculateSerial[n5]);
            final DimConstraint dimConstraint = (n4 >= 0) ? constaints[(n4 >= constaints.length) ? (constaints.length - 1) : n4] : Grid.DOCK_DIM_CONSTRAINT;
            final int n8 = calculateSerial[n6];
            for (int j = 0; j < list.size(); ++j) {
                final LinkedDimGroup linkedDimGroup = list.get(j);
                int sum = n8;
                if (linkedDimGroup.span > 1) {
                    sum = LayoutUtil.sum(calculateSerial, n6, Math.min((linkedDimGroup.span << 1) - 1, calculateSerial.length - n6 - 1));
                }
                linkedDimGroup.layout(dimConstraint, n7, sum, linkedDimGroup.span);
            }
            n3 = n7 + (b3 ? (-n8) : n8);
        }
    }
    
    private static void addToSizeGroup(final HashMap<String, int[]> hashMap, final String s, final int[] array) {
        final int[] array2 = hashMap.get(s);
        if (array2 == null) {
            hashMap.put(s, new int[] { array[0], array[1], array[2] });
        }
        else {
            array2[0] = Math.max(array[0], array2[0]);
            array2[1] = Math.max(array[1], array2[1]);
            array2[2] = Math.min(array[2], array2[2]);
        }
    }
    
    private static HashMap<String, Integer> addToEndGroup(HashMap<String, Integer> hashMap, final String s, final int n) {
        if (s != null) {
            if (hashMap == null) {
                hashMap = new HashMap<String, Integer>(2);
            }
            final Integer n2 = hashMap.get(s);
            if (n2 == null || n > n2) {
                hashMap.put(s, n);
            }
        }
        return hashMap;
    }
    
    private FlowSizeSpec calcRowsOrColsSizes(final boolean b) {
        final ArrayList<LinkedDimGroup>[] array = b ? this.colGroupLists : this.rowGroupLists;
        final Float[] array2 = b ? this.pushXs : this.pushYs;
        int constrain = b ? this.container.getWidth() : this.container.getHeight();
        final BoundSize boundSize = b ? this.lc.getWidth() : this.lc.getHeight();
        if (!boundSize.isUnset()) {
            constrain = boundSize.constrain(constrain, (float)getParentSize(this.container, b), this.container);
        }
        final DimConstraint[] constaints = (b ? this.colConstr : this.rowConstr).getConstaints();
        final TreeSet<Integer> set = b ? this.colIndexes : this.rowIndexes;
        final int[][] array3 = new int[set.size()][];
        final HashMap<Object, int[]> hashMap = new HashMap<Object, int[]>(2);
        final DimConstraint[] array4 = new DimConstraint[set.size()];
        final Iterator<Integer> iterator = set.iterator();
        for (int i = 0; i < array3.length; ++i) {
            final int intValue = iterator.next();
            final int[] array5 = new int[3];
            if (intValue >= -30000 && intValue <= 30000) {
                array4[i] = constaints[(intValue >= constaints.length) ? (constaints.length - 1) : intValue];
            }
            else {
                array4[i] = Grid.DOCK_DIM_CONSTRAINT;
            }
            final ArrayList<LinkedDimGroup> list = array[i];
            final int[] array6 = { getTotalGroupsSizeParallel(list, 0, false), getTotalGroupsSizeParallel(list, 1, false), 2097051 };
            correctMinMax(array6);
            final BoundSize size = array4[i].getSize();
            for (int j = 0; j <= 2; ++j) {
                int pixels = array6[j];
                final UnitValue size2 = size.getSize(j);
                if (size2 != null) {
                    final int unit = size2.getUnit();
                    if (unit == 14) {
                        pixels = array6[1];
                    }
                    else if (unit == 13) {
                        pixels = array6[0];
                    }
                    else if (unit == 15) {
                        pixels = array6[2];
                    }
                    else {
                        pixels = size2.getPixels((float)constrain, this.container, null);
                    }
                }
                else if (intValue >= -30000 && intValue <= 30000 && pixels == 0) {
                    pixels = (LayoutUtil.isDesignTime(this.container) ? LayoutUtil.getDesignTimeEmptySize() : 0);
                }
                array5[j] = pixels;
            }
            correctMinMax(array5);
            addToSizeGroup((HashMap<String, int[]>)hashMap, array4[i].getSizeGroup(), array5);
            array3[i] = array5;
        }
        if (hashMap.size() > 0) {
            for (int k = 0; k < array3.length; ++k) {
                if (array4[k].getSizeGroup() != null) {
                    array3[k] = hashMap.get(array4[k].getSizeGroup());
                }
            }
        }
        final ResizeConstraint[] rowResizeConstraints = getRowResizeConstraints(array4);
        final boolean[] array7 = new boolean[array4.length + 1];
        final FlowSizeSpec mergeSizesGapsAndResConstrs = mergeSizesGapsAndResConstrs(rowResizeConstraints, array7, array3, this.getRowGaps(array4, constrain, b, array7));
        this.adjustMinPrefForSpanningComps(array4, array2, mergeSizesGapsAndResConstrs, array);
        return mergeSizesGapsAndResConstrs;
    }
    
    private static int getParentSize(final ComponentWrapper componentWrapper, final boolean b) {
        return (componentWrapper.getParent() != null) ? (b ? componentWrapper.getWidth() : componentWrapper.getHeight()) : 0;
    }
    
    private int[] getMinPrefMaxSumSize(final boolean b) {
        final int[][] array = b ? this.colFlowSpecs.sizes : this.rowFlowSpecs.sizes;
        final int[] array2 = new int[3];
        final BoundSize boundSize = b ? this.lc.getWidth() : this.lc.getHeight();
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != null) {
                final int[] array3 = array[i];
                for (int j = 0; j <= 2; ++j) {
                    if (boundSize.getSize(j) != null) {
                        if (i == 0) {
                            array2[j] = boundSize.getSize(j).getPixels((float)getParentSize(this.container, b), this.container, null);
                        }
                    }
                    else {
                        int n = array3[j];
                        if (n != -2147471302) {
                            if (j == 1) {
                                final int n2 = array3[2];
                                if (n2 != -2147471302 && n2 < n) {
                                    n = n2;
                                }
                                final int n3 = array3[0];
                                if (n3 > n) {
                                    n = n3;
                                }
                            }
                            final int[] array4 = array2;
                            final int n4 = j;
                            array4[n4] += n;
                        }
                        if (array3[2] == -2147471302 || array2[2] > 2097051) {
                            array2[2] = 2097051;
                        }
                    }
                }
            }
        }
        correctMinMax(array2);
        return array2;
    }
    
    private static ResizeConstraint[] getRowResizeConstraints(final DimConstraint[] array) {
        final ResizeConstraint[] array2 = new ResizeConstraint[array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = array[i].resize;
        }
        return array2;
    }
    
    private static ResizeConstraint[] getComponentResizeConstraints(final ArrayList<CompWrap> list, final boolean b) {
        final ResizeConstraint[] array = new ResizeConstraint[list.size()];
        for (int i = 0; i < array.length; ++i) {
            final CC access$700 = list.get(i).cc;
            array[i] = access$700.getDimConstraint(b).resize;
            final int dockSide = access$700.getDockSide();
            if (b) {
                if (dockSide != 0) {
                    if (dockSide != 2) {
                        continue;
                    }
                }
            }
            else if (dockSide != 1 && dockSide != 3) {
                continue;
            }
            final ResizeConstraint resizeConstraint = array[i];
            array[i] = new ResizeConstraint(resizeConstraint.shrinkPrio, resizeConstraint.shrink, resizeConstraint.growPrio, ResizeConstraint.WEIGHT_100);
        }
        return array;
    }
    
    private static boolean[] getComponentGapPush(final ArrayList<CompWrap> list, final boolean b) {
        final boolean[] array = new boolean[list.size() + 1];
        for (int i = 0; i < array.length; ++i) {
            boolean access$3300 = i > 0 && list.get(i - 1).isPushGap(b, false);
            if (!access$3300 && i < array.length - 1) {
                access$3300 = list.get(i).isPushGap(b, true);
            }
            array[i] = access$3300;
        }
        return array;
    }
    
    private int[][] getRowGaps(final DimConstraint[] array, final int n, final boolean b, final boolean[] array2) {
        BoundSize boundSize = b ? this.lc.getGridGapX() : this.lc.getGridGapY();
        if (boundSize == null) {
            boundSize = (b ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY());
        }
        final int[] pixelSizes = boundSize.getPixelSizes((float)n, this.container, null);
        final boolean b2 = !this.hasDocks();
        final UnitValue insets = LayoutUtil.getInsets(this.lc, b ? 1 : 0, b2);
        final UnitValue insets2 = LayoutUtil.getInsets(this.lc, b ? 3 : 2, b2);
        final int[][] array3 = new int[array.length + 1][];
        int i = 0;
        int n2 = 0;
        while (i < array3.length) {
            final DimConstraint dimConstraint = (i > 0) ? array[i - 1] : null;
            final DimConstraint dimConstraint2 = (i < array.length) ? array[i] : null;
            final boolean b3 = dimConstraint == Grid.DOCK_DIM_CONSTRAINT || dimConstraint == null;
            final boolean b4 = dimConstraint2 == Grid.DOCK_DIM_CONSTRAINT || dimConstraint2 == null;
            if (!b3 || !b4) {
                final BoundSize boundSize2 = (this.wrapGapMap == null || b == this.lc.isFlowX()) ? null : this.wrapGapMap.get(n2++);
                if (boundSize2 == null) {
                    final int[] array4 = (int[])((dimConstraint != null) ? dimConstraint.getRowGaps(this.container, null, n, false) : null);
                    final int[] array5 = (int[])((dimConstraint2 != null) ? dimConstraint2.getRowGaps(this.container, null, n, true) : null);
                    if (b3 && array5 == null && insets != null) {
                        final int pixels = insets.getPixels((float)n, this.container, null);
                        array3[i] = new int[] { pixels, pixels, pixels };
                    }
                    else if (b4 && array4 == null && insets != null) {
                        final int pixels2 = insets2.getPixels((float)n, this.container, null);
                        array3[i] = new int[] { pixels2, pixels2, pixels2 };
                    }
                    else {
                        array3[i] = ((array5 != array4) ? mergeSizes(array5, array4) : new int[] { pixelSizes[0], pixelSizes[1], pixelSizes[2] });
                    }
                    if ((dimConstraint != null && dimConstraint.isGapAfterPush()) || (dimConstraint2 != null && dimConstraint2.isGapBeforePush())) {
                        array2[i] = true;
                    }
                }
                else {
                    if (boundSize2.isUnset()) {
                        array3[i] = new int[] { pixelSizes[0], pixelSizes[1], pixelSizes[2] };
                    }
                    else {
                        array3[i] = boundSize2.getPixelSizes((float)n, this.container, null);
                    }
                    array2[i] = boundSize2.getGapPush();
                }
            }
            ++i;
        }
        return array3;
    }
    
    private static int[][] getGaps(final ArrayList<CompWrap> list, final boolean b) {
        final int size = list.size();
        final int[][] array = new int[size + 1][];
        array[0] = list.get(0).getGaps(b, true);
        for (int i = 0; i < size; ++i) {
            array[i + 1] = mergeSizes(list.get(i).getGaps(b, false), (int[])((i < size - 1) ? list.get(i + 1).getGaps(b, true) : null));
        }
        return array;
    }
    
    private boolean hasDocks() {
        return this.dockOffX > 0 || this.dockOffY > 0 || this.rowIndexes.last() > 30000 || this.colIndexes.last() > 30000;
    }
    
    private void adjustMinPrefForSpanningComps(final DimConstraint[] array, final Float[] array2, final FlowSizeSpec flowSizeSpec, final ArrayList<LinkedDimGroup>[] array3) {
        for (int i = array3.length - 1; i >= 0; --i) {
            final ArrayList<LinkedDimGroup> list = array3[i];
            for (int j = 0; j < list.size(); ++j) {
                final LinkedDimGroup linkedDimGroup = list.get(j);
                if (linkedDimGroup.span != 1) {
                    final int[] access$3500 = linkedDimGroup.getMinPrefMax();
                    for (int k = 0; k <= 1; ++k) {
                        final int n = access$3500[k];
                        if (n != -2147471302) {
                            int n2 = 0;
                            final int n3 = (i << 1) + 1;
                            final int n4 = Math.min(linkedDimGroup.span << 1, flowSizeSpec.sizes.length - n3) - 1;
                            for (int l = n3; l < n3 + n4; ++l) {
                                final int n5 = flowSizeSpec.sizes[l][k];
                                if (n5 != -2147471302) {
                                    n2 += n5;
                                }
                            }
                            if (n2 < n) {
                                for (int n6 = 0, access$3501 = 0; n6 < 4 && access$3501 < n; access$3501 = flowSizeSpec.expandSizes(array, array2, n, n3, n4, k, n6), ++n6) {}
                            }
                        }
                    }
                }
            }
        }
    }
    
    private ArrayList<LinkedDimGroup>[] divideIntoLinkedGroups(final boolean b) {
        boolean b2 = false;
        Label_0036: {
            Label_0035: {
                if (b) {
                    if (this.lc.isTopToBottom()) {
                        break Label_0035;
                    }
                }
                else if (LayoutUtil.isLeftToRight(this.lc, this.container)) {
                    break Label_0035;
                }
                b2 = true;
                break Label_0036;
            }
            b2 = false;
        }
        final boolean b3 = b2;
        final TreeSet<Integer> set = b ? this.rowIndexes : this.colIndexes;
        final TreeSet<Integer> set2 = b ? this.colIndexes : this.rowIndexes;
        final DimConstraint[] constaints = (b ? this.rowConstr : this.colConstr).getConstaints();
        final ArrayList[] array = new ArrayList[set.size()];
        int n = 0;
        for (final int intValue : set) {
            DimConstraint dock_DIM_CONSTRAINT;
            if (intValue >= -30000 && intValue <= 30000) {
                dock_DIM_CONSTRAINT = constaints[(intValue >= constaints.length) ? (constaints.length - 1) : intValue];
            }
            else {
                dock_DIM_CONSTRAINT = Grid.DOCK_DIM_CONSTRAINT;
            }
            final ArrayList<LinkedDimGroup> list = new ArrayList<LinkedDimGroup>(2);
            array[n++] = list;
            for (final int intValue2 : set2) {
                final Cell cell = b ? this.getCell(intValue, intValue2) : this.getCell(intValue2, intValue);
                if (cell != null) {
                    if (cell.compWraps.size() == 0) {
                        continue;
                    }
                    int convertSpanToSparseGrid = b ? cell.spany : cell.spanx;
                    if (convertSpanToSparseGrid > 1) {
                        convertSpanToSparseGrid = convertSpanToSparseGrid(intValue, convertSpanToSparseGrid, set);
                    }
                    final boolean b4 = cell.flowx == b;
                    if ((!b4 && cell.compWraps.size() > 1) || convertSpanToSparseGrid > 1) {
                        final LinkedDimGroup linkedDimGroup = new LinkedDimGroup("p," + intValue2, convertSpanToSparseGrid, (int)(b4 ? 1 : 0), !b, b3);
                        linkedDimGroup.setCompWraps(cell.compWraps);
                        list.add(linkedDimGroup);
                    }
                    else {
                        for (int i = 0; i < cell.compWraps.size(); ++i) {
                            final CompWrap compWrap = cell.compWraps.get(i);
                            final boolean b5 = b && this.lc.isTopToBottom() && dock_DIM_CONSTRAINT.getAlignOrDefault(!b) == UnitValue.BASELINE_IDENTITY;
                            final boolean b6 = b && compWrap.isBaselineAlign(b5);
                            final String s = b6 ? "baseline" : null;
                            boolean b7 = false;
                            for (int j = 0; j <= list.size() - 1; ++j) {
                                final LinkedDimGroup linkedDimGroup2 = list.get(j);
                                if (linkedDimGroup2.linkCtx == s || (s != null && s.equals(linkedDimGroup2.linkCtx))) {
                                    linkedDimGroup2.addCompWrap(compWrap);
                                    b7 = true;
                                    break;
                                }
                            }
                            if (!b7) {
                                final LinkedDimGroup linkedDimGroup3 = new LinkedDimGroup(s, 1, b6 ? 2 : 1, !b, b3);
                                linkedDimGroup3.addCompWrap(compWrap);
                                list.add(linkedDimGroup3);
                            }
                        }
                    }
                }
            }
        }
        return (ArrayList<LinkedDimGroup>[])array;
    }
    
    private static int convertSpanToSparseGrid(final int n, final int n2, final TreeSet<Integer> set) {
        final int n3 = n + n2;
        int n4 = 1;
        for (final int intValue : set) {
            if (intValue <= n) {
                continue;
            }
            if (intValue >= n3) {
                break;
            }
            ++n4;
        }
        return n4;
    }
    
    private final boolean isCellFree(final int n, final int n2, final ArrayList<int[]> list) {
        if (this.getCell(n, n2) != null) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            final int[] array = list.get(i);
            if (array[0] <= n2 && array[1] <= n && array[0] + array[2] > n2 && array[1] + array[3] > n) {
                return false;
            }
        }
        return true;
    }
    
    private Cell getCell(final int n, final int n2) {
        return this.grid.get((n << 16) + n2);
    }
    
    private void setCell(final int n, final int n2, final Cell cell) {
        if (n2 < 0 || n2 > 30000 || n < 0 || n > 30000) {
            throw new IllegalArgumentException("Cell position out of bounds. row: " + n + ", col: " + n2);
        }
        this.rowIndexes.add(n);
        this.colIndexes.add(n2);
        this.grid.put((n << 16) + n2, cell);
    }
    
    private void addDockingCell(final int[] array, final int n, final CompWrap compWrap) {
        int n2 = 1;
        int n3 = 1;
        int n7 = 0;
        int n8 = 0;
        switch (n) {
            case 0:
            case 2: {
                int n5;
                if (n == 0) {
                    final int n4 = 0;
                    array[n4] = (n5 = array[n4]) + 1;
                }
                else {
                    final int n6 = 2;
                    array[n6] = (n5 = array[n6]) - 1;
                }
                n7 = n5;
                n8 = array[1];
                n2 = array[3] - array[1] + 1;
                this.colIndexes.add(array[3]);
                break;
            }
            case 1:
            case 3: {
                int n10;
                if (n == 1) {
                    final int n9 = 1;
                    array[n9] = (n10 = array[n9]) + 1;
                }
                else {
                    final int n11 = 3;
                    array[n11] = (n10 = array[n11]) - 1;
                }
                n8 = n10;
                n7 = array[0];
                n3 = array[2] - array[0] + 1;
                this.rowIndexes.add(array[2]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Internal error 123.");
            }
        }
        this.rowIndexes.add(n7);
        this.colIndexes.add(n8);
        this.grid.put((n7 << 16) + n8, new Cell(compWrap, n2, n3, n2 > 1));
    }
    
    private static void layoutBaseline(final ContainerWrapper containerWrapper, final ArrayList<CompWrap> list, final DimConstraint dimConstraint, final int n, final int n2, final int n3, final int n4) {
        final int[] baselineAboveBelow = getBaselineAboveBelow(list, n3, true);
        final int n5 = baselineAboveBelow[0] + baselineAboveBelow[1];
        UnitValue unitValue = list.get(0).cc.getVertical().getAlign();
        if (n4 == 1 && unitValue == null) {
            unitValue = dimConstraint.getAlignOrDefault(false);
        }
        if (unitValue == UnitValue.BASELINE_IDENTITY) {
            unitValue = UnitValue.CENTER;
        }
        final int n6 = n + baselineAboveBelow[0] + ((unitValue != null) ? Math.max(0, unitValue.getPixels((float)(n2 - n5), containerWrapper, null)) : 0);
        for (int i = 0; i < list.size(); ++i) {
            final CompWrap compWrap = list.get(i);
            compWrap.y += n6;
            if (compWrap.y + compWrap.h > n + n2) {
                compWrap.h = n + n2 - compWrap.y;
            }
        }
    }
    
    private static void layoutSerial(final ContainerWrapper containerWrapper, final ArrayList<CompWrap> list, final DimConstraint dimConstraint, final int n, final int n2, final boolean b, final int n3, final boolean b2) {
        final FlowSizeSpec mergeSizesGapsAndResConstrs = mergeSizesGapsAndResConstrs(getComponentResizeConstraints(list, b), getComponentGapPush(list, b), getComponentSizes(list, b), getGaps(list, b));
        setCompWrapBounds(containerWrapper, LayoutUtil.calculateSerial(mergeSizesGapsAndResConstrs.sizes, mergeSizesGapsAndResConstrs.resConstsInclGaps, (Float[])(dimConstraint.isFill() ? Grid.GROW_100 : null), 1, n2), list, dimConstraint.getAlignOrDefault(b), n, n2, b, b2);
    }
    
    private static void setCompWrapBounds(final ContainerWrapper containerWrapper, final int[] array, final ArrayList<CompWrap> list, final UnitValue unitValue, final int n, final int n2, final boolean b, final boolean b2) {
        final int sum = LayoutUtil.sum(array);
        final UnitValue correctAlign = correctAlign(list.get(0).cc, unitValue, b, b2);
        int n3 = n;
        final int n4 = n2 - sum;
        if (n4 > 0 && correctAlign != null) {
            final int min = Math.min(n4, Math.max(0, correctAlign.getPixels((float)n4, containerWrapper, null)));
            n3 += (b2 ? (-min) : min);
        }
        int i = 0;
        int n5 = 0;
        while (i < list.size()) {
            final CompWrap compWrap = list.get(i);
            if (b2) {
                final int n6 = n3 - array[n5++];
                compWrap.setDimBounds(n6 - array[n5], array[n5], b);
                n3 = n6 - array[n5++];
            }
            else {
                final int n7 = n3 + array[n5++];
                compWrap.setDimBounds(n7, array[n5], b);
                n3 = n7 + array[n5++];
            }
            ++i;
        }
    }
    
    private static void layoutParallel(final ContainerWrapper containerWrapper, final ArrayList<CompWrap> list, final DimConstraint dimConstraint, final int n, final int n2, final boolean b, final boolean b2) {
        final int[][] array = new int[list.size()][];
        for (int i = 0; i < array.length; ++i) {
            final CompWrap compWrap = list.get(i);
            array[i] = LayoutUtil.calculateSerial(new int[][] { compWrap.getGaps(b, true), b ? compWrap.horSizes : compWrap.verSizes, compWrap.getGaps(b, false) }, new ResizeConstraint[] { compWrap.isPushGap(b, true) ? Grid.GAP_RC_CONST_PUSH : Grid.GAP_RC_CONST, compWrap.cc.getDimConstraint(b).resize, compWrap.isPushGap(b, false) ? Grid.GAP_RC_CONST_PUSH : Grid.GAP_RC_CONST }, (Float[])(dimConstraint.isFill() ? Grid.GROW_100 : null), 1, n2);
        }
        setCompWrapBounds(containerWrapper, array, list, dimConstraint.getAlignOrDefault(b), n, n2, b, b2);
    }
    
    private static void setCompWrapBounds(final ContainerWrapper containerWrapper, final int[][] array, final ArrayList<CompWrap> list, final UnitValue unitValue, final int n, final int n2, final boolean b, final boolean b2) {
        for (int i = 0; i < array.length; ++i) {
            final CompWrap compWrap = list.get(i);
            final UnitValue correctAlign = correctAlign(compWrap.cc, unitValue, b, b2);
            final int[] array2 = array[i];
            final int n3 = array2[0];
            final int n4 = array2[1];
            final int n5 = array2[2];
            int n6 = b2 ? (n - n3) : (n + n3);
            final int n7 = n2 - n4 - n3 - n5;
            if (n7 > 0 && correctAlign != null) {
                final int min = Math.min(n7, Math.max(0, correctAlign.getPixels((float)n7, containerWrapper, null)));
                n6 += (b2 ? (-min) : min);
            }
            compWrap.setDimBounds(b2 ? (n6 - n4) : n6, n4, b);
        }
    }
    
    private static UnitValue correctAlign(final CC cc, final UnitValue unitValue, final boolean b, final boolean b2) {
        UnitValue unitValue2 = (b ? cc.getHorizontal() : cc.getVertical()).getAlign();
        if (unitValue2 == null) {
            unitValue2 = unitValue;
        }
        if (unitValue2 == UnitValue.BASELINE_IDENTITY) {
            unitValue2 = UnitValue.CENTER;
        }
        if (b2) {
            if (unitValue2 == UnitValue.LEFT) {
                unitValue2 = UnitValue.RIGHT;
            }
            else if (unitValue2 == UnitValue.RIGHT) {
                unitValue2 = UnitValue.LEFT;
            }
        }
        return unitValue2;
    }
    
    private static int[] getBaselineAboveBelow(final ArrayList<CompWrap> list, final int n, final boolean b) {
        int max = -32768;
        int max2 = -32768;
        for (int i = 0; i < list.size(); ++i) {
            final CompWrap compWrap = list.get(i);
            final int access$2800 = compWrap.getSize(n, false);
            if (access$2800 >= 2097051) {
                return new int[] { 1048525, 1048525 };
            }
            final int access$2801 = compWrap.getBaseline(n);
            max = Math.max(access$2801 + compWrap.getGapBefore(n, false), max);
            max2 = Math.max(access$2800 - access$2801 + compWrap.getGapAfter(n, false), max2);
            if (b) {
                compWrap.setDimBounds(-access$2801, access$2800, false);
            }
        }
        return new int[] { max, max2 };
    }
    
    private static int getTotalSizeParallel(final ArrayList<CompWrap> list, final int n, final boolean b) {
        int n2 = (n == 2) ? 2097051 : 0;
        for (int i = 0; i < list.size(); ++i) {
            final int access$5400 = list.get(i).getSizeInclGaps(n, b);
            if (access$5400 >= 2097051) {
                return 2097051;
            }
            if (n == 2) {
                if (access$5400 >= n2) {
                    continue;
                }
            }
            else if (access$5400 <= n2) {
                continue;
            }
            n2 = access$5400;
        }
        return constrainSize(n2);
    }
    
    private static int getTotalSizeSerial(final ArrayList<CompWrap> list, final int n, final boolean b) {
        int n2 = 0;
        int i = 0;
        final int size = list.size();
        int access$5300 = 0;
        while (i < size) {
            final CompWrap compWrap = list.get(i);
            final int access$5301 = compWrap.getGapBefore(n, b);
            if (access$5301 > access$5300) {
                n2 += access$5301 - access$5300;
            }
            n2 = n2 + compWrap.getSize(n, b) + (access$5300 = compWrap.getGapAfter(n, b));
            if (n2 >= 2097051) {
                return 2097051;
            }
            ++i;
        }
        return constrainSize(n2);
    }
    
    private static int getTotalGroupsSizeParallel(final ArrayList<LinkedDimGroup> list, final int n, final boolean b) {
        int n2 = (n == 2) ? 2097051 : 0;
        for (int i = 0; i < list.size(); ++i) {
            final LinkedDimGroup linkedDimGroup = list.get(i);
            if (b || linkedDimGroup.span == 1) {
                final int n3 = linkedDimGroup.getMinPrefMax()[n];
                if (n3 >= 2097051) {
                    return 2097051;
                }
                if (n == 2) {
                    if (n3 >= n2) {
                        continue;
                    }
                }
                else if (n3 <= n2) {
                    continue;
                }
                n2 = n3;
            }
        }
        return constrainSize(n2);
    }
    
    private static int[][] getComponentSizes(final ArrayList<CompWrap> list, final boolean b) {
        final int[][] array = new int[list.size()][];
        for (int i = 0; i < array.length; ++i) {
            final CompWrap compWrap = list.get(i);
            array[i] = (b ? compWrap.horSizes : compWrap.verSizes);
        }
        return array;
    }
    
    private static FlowSizeSpec mergeSizesGapsAndResConstrs(final ResizeConstraint[] array, final boolean[] array2, final int[][] array3, final int[][] array4) {
        final int[][] array5 = new int[(array3.length << 1) + 1][];
        final ResizeConstraint[] array6 = new ResizeConstraint[array5.length];
        array5[0] = array4[0];
        for (int i = 0, n = 1; i < array3.length; ++i, n += 2) {
            array6[n] = array[i];
            array5[n] = array3[i];
            array5[n + 1] = array4[i + 1];
            if (array5[n - 1] != null) {
                array6[n - 1] = (array2[(i < array2.length) ? i : (array2.length - 1)] ? Grid.GAP_RC_CONST_PUSH : Grid.GAP_RC_CONST);
            }
            if (i == array3.length - 1 && array5[n + 1] != null) {
                array6[n + 1] = (array2[(i + 1 < array2.length) ? (i + 1) : (array2.length - 1)] ? Grid.GAP_RC_CONST_PUSH : Grid.GAP_RC_CONST);
            }
        }
        for (int j = 0; j < array5.length; ++j) {
            if (array5[j] == null) {
                array5[j] = new int[3];
            }
        }
        return new FlowSizeSpec(array5, array6);
    }
    
    private static int[] mergeSizes(final int[] array, final int[] array2) {
        if (array == null) {
            return array2;
        }
        if (array2 == null) {
            return array;
        }
        final int[] array3 = new int[array.length];
        for (int i = 0; i < array3.length; ++i) {
            array3[i] = mergeSizes(array[i], array2[i], true);
        }
        return array3;
    }
    
    private static int mergeSizes(final int n, final int n2, final boolean b) {
        if (n == -2147471302 || n == n2) {
            return n2;
        }
        if (n2 == -2147471302) {
            return n;
        }
        return (b != n > n2) ? n2 : n;
    }
    
    private static int constrainSize(final int n) {
        return (n > 0) ? ((n < 2097051) ? n : 2097051) : 0;
    }
    
    private static void correctMinMax(final int[] array) {
        if (array[0] > array[2]) {
            array[0] = array[2];
        }
        if (array[1] < array[0]) {
            array[1] = array[0];
        }
        if (array[1] > array[2]) {
            array[1] = array[2];
        }
    }
    
    private static Float[] extractSubArray(final DimConstraint[] array, final Float[] array2, final int n, final int n2) {
        if (array2 == null || array2.length < n + n2) {
            final Float[] array3 = new Float[n2];
            for (int i = n + n2 - 1; i >= 0; i -= 2) {
                if (array[i >> 1] != Grid.DOCK_DIM_CONSTRAINT) {
                    array3[i - n] = ResizeConstraint.WEIGHT_100;
                    return array3;
                }
            }
            return array3;
        }
        final Float[] array4 = new Float[n2];
        for (int j = 0; j < n2; ++j) {
            array4[j] = array2[n + j];
        }
        return array4;
    }
    
    private static synchronized void putSizesAndIndexes(final Object o, final int[] array, final int[] array2, final boolean b) {
        if (Grid.PARENT_ROWCOL_SIZES_MAP == null) {
            Grid.PARENT_ROWCOL_SIZES_MAP = new WeakHashMap[] { new WeakHashMap(4), new WeakHashMap(4) };
        }
        Grid.PARENT_ROWCOL_SIZES_MAP[!b].put(o, new int[][] { array2, array });
    }
    
    static synchronized int[][] getSizesAndIndexes(final Object o, final boolean b) {
        if (Grid.PARENT_ROWCOL_SIZES_MAP == null) {
            return null;
        }
        return Grid.PARENT_ROWCOL_SIZES_MAP[!b].get(o);
    }
    
    private static synchronized void saveGrid(final ComponentWrapper componentWrapper, final LinkedHashMap<Integer, Cell> linkedHashMap) {
        if (Grid.PARENT_GRIDPOS_MAP == null) {
            Grid.PARENT_GRIDPOS_MAP = new WeakHashMap<Object, LinkedHashMap<Integer, Cell>>();
        }
        Grid.PARENT_GRIDPOS_MAP.put(componentWrapper.getComponent(), linkedHashMap);
    }
    
    static synchronized HashMap<Object, int[]> getGridPositions(final Object o) {
        if (Grid.PARENT_GRIDPOS_MAP == null) {
            return null;
        }
        final LinkedHashMap<Integer, Cell> linkedHashMap = Grid.PARENT_GRIDPOS_MAP.get(o);
        if (linkedHashMap == null) {
            return null;
        }
        final HashMap<Object, int[]> hashMap = new HashMap<Object, int[]>();
        for (final Map.Entry<Integer, Cell> entry : linkedHashMap.entrySet()) {
            final Cell cell = entry.getValue();
            final Integer n = entry.getKey();
            if (n != null) {
                final int intValue = n;
                final int n2 = intValue & 0xFFFF;
                final int n3 = intValue >> 16;
                final Iterator iterator2 = cell.compWraps.iterator();
                while (iterator2.hasNext()) {
                    hashMap.put(iterator2.next().comp.getComponent(), new int[] { n2, n3, cell.spanx, cell.spany });
                }
            }
        }
        return hashMap;
    }
    
    static {
        GROW_100 = new Float[] { ResizeConstraint.WEIGHT_100 };
        (DOCK_DIM_CONSTRAINT = new DimConstraint()).setGrowPriority(0);
        GAP_RC_CONST = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, null);
        GAP_RC_CONST_PUSH = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, ResizeConstraint.WEIGHT_100);
        Grid.PARENT_ROWCOL_SIZES_MAP = null;
        Grid.PARENT_GRIDPOS_MAP = null;
    }
    
    private static final class FlowSizeSpec
    {
        private final int[][] sizes;
        private final ResizeConstraint[] resConstsInclGaps;
        
        private FlowSizeSpec(final int[][] sizes, final ResizeConstraint[] resConstsInclGaps) {
            this.sizes = sizes;
            this.resConstsInclGaps = resConstsInclGaps;
        }
        
        private int expandSizes(final DimConstraint[] array, final Float[] array2, final int n, final int n2, final int n3, final int n4, final int n5) {
            final ResizeConstraint[] array3 = new ResizeConstraint[n3];
            final int[][] array4 = new int[n3][];
            for (int i = 0; i < n3; ++i) {
                final int[] array5 = this.sizes[i + n2];
                array4[i] = new int[] { array5[n4], array5[1], array5[2] };
                if (n5 <= 1 && i % 2 == 0) {
                    final BoundSize size = ((DimConstraint)LayoutUtil.getIndexSafe(array, i + n2 - 1 >> 1)).getSize();
                    if (n4 == 0 && size.getMin() != null && size.getMin().getUnit() != 13) {
                        continue;
                    }
                    if (n4 == 1 && size.getPreferred() != null && size.getPreferred().getUnit() != 14) {
                        continue;
                    }
                }
                array3[i] = (ResizeConstraint)LayoutUtil.getIndexSafe(this.resConstsInclGaps, i + n2);
            }
            final int[] calculateSerial = LayoutUtil.calculateSerial(array4, array3, (Float[])((n5 == 1 || n5 == 3) ? extractSubArray(array, array2, n2, n3) : null), 1, n);
            int n6 = 0;
            for (int j = 0; j < n3; ++j) {
                final int n7 = calculateSerial[j];
                this.sizes[j + n2][n4] = n7;
                n6 += n7;
            }
            return n6;
        }
    }
    
    private static final class CompWrap
    {
        private final ComponentWrapper comp;
        private final CC cc;
        private final UnitValue[] pos;
        private int[][] gaps;
        private final int[] horSizes;
        private final int[] verSizes;
        private int x;
        private int y;
        private int w;
        private int h;
        private int forcedPushGaps;
        
        private CompWrap(final ComponentWrapper comp, final CC cc, final int n, final UnitValue[] pos, final BoundSize[] array) {
            this.horSizes = new int[3];
            this.verSizes = new int[3];
            this.x = -2147471302;
            this.y = -2147471302;
            this.w = -2147471302;
            this.h = -2147471302;
            this.forcedPushGaps = 0;
            this.comp = comp;
            this.cc = cc;
            this.pos = pos;
            if (n <= 0) {
                final BoundSize boundSize = (array != null && array[0] != null) ? array[0] : cc.getHorizontal().getSize();
                final BoundSize boundSize2 = (array != null && array[1] != null) ? array[1] : cc.getVertical().getSize();
                int width = -1;
                int height = -1;
                if (this.comp.getWidth() > 0 && this.comp.getHeight() > 0) {
                    height = this.comp.getHeight();
                    width = this.comp.getWidth();
                }
                for (int i = 0; i <= 2; ++i) {
                    this.horSizes[i] = this.getSize(boundSize, i, true, height);
                    this.verSizes[i] = this.getSize(boundSize2, i, false, (width > 0) ? width : this.horSizes[i]);
                }
                correctMinMax(this.horSizes);
                correctMinMax(this.verSizes);
            }
            if (n > 1) {
                this.gaps = new int[4][];
                for (int j = 0; j < this.gaps.length; ++j) {
                    this.gaps[j] = new int[3];
                }
            }
        }
        
        private int getSize(final BoundSize boundSize, final int n, final boolean b, final int n2) {
            if (boundSize != null && boundSize.getSize(n) != null) {
                final ContainerWrapper parent = this.comp.getParent();
                return boundSize.getSize(n).getPixels(b ? ((float)parent.getWidth()) : ((float)parent.getHeight()), parent, this.comp);
            }
            switch (n) {
                case 0: {
                    return b ? this.comp.getMinimumWidth(n2) : this.comp.getMinimumHeight(n2);
                }
                case 1: {
                    return b ? this.comp.getPreferredWidth(n2) : this.comp.getPreferredHeight(n2);
                }
                default: {
                    return b ? this.comp.getMaximumWidth(n2) : this.comp.getMaximumHeight(n2);
                }
            }
        }
        
        private void calcGaps(final ComponentWrapper componentWrapper, final CC cc, final ComponentWrapper componentWrapper2, final CC cc2, final String s, final boolean b, final boolean b2) {
            final ContainerWrapper parent = this.comp.getParent();
            final int width = parent.getWidth();
            final int height = parent.getHeight();
            final BoundSize boundSize = (componentWrapper != null) ? (b ? cc.getHorizontal() : cc.getVertical()).getGapAfter() : null;
            final BoundSize boundSize2 = (componentWrapper2 != null) ? (b ? cc2.getHorizontal() : cc2.getVertical()).getGapBefore() : null;
            this.mergeGapSizes(this.cc.getVertical().getComponentGaps(parent, this.comp, boundSize, b ? null : componentWrapper, s, height, 0, b2), false, true);
            this.mergeGapSizes(this.cc.getHorizontal().getComponentGaps(parent, this.comp, boundSize, b ? componentWrapper : null, s, width, 1, b2), true, true);
            this.mergeGapSizes(this.cc.getVertical().getComponentGaps(parent, this.comp, boundSize2, b ? null : componentWrapper2, s, height, 2, b2), false, false);
            this.mergeGapSizes(this.cc.getHorizontal().getComponentGaps(parent, this.comp, boundSize2, b ? componentWrapper2 : null, s, width, 3, b2), true, false);
        }
        
        private void setDimBounds(final int n, final int n2, final boolean b) {
            if (b) {
                this.x = n;
                this.w = n2;
            }
            else {
                this.y = n;
                this.h = n2;
            }
        }
        
        private boolean isPushGap(final boolean b, final boolean b2) {
            if (b && ((b2 ? 1 : 2) & this.forcedPushGaps) != 0x0) {
                return true;
            }
            final DimConstraint dimConstraint = this.cc.getDimConstraint(b);
            final BoundSize boundSize = b2 ? dimConstraint.getGapBefore() : dimConstraint.getGapAfter();
            return boundSize != null && boundSize.getGapPush();
        }
        
        private boolean transferBounds(final boolean b) {
            this.comp.setBounds(this.x, this.y, this.w, this.h);
            return b && this.w != this.horSizes[1] && this.cc.getVertical().getSize().getPreferred() == null && this.comp.getPreferredHeight(-1) != this.verSizes[1];
        }
        
        private void setSizes(final int[] array, final boolean b) {
            if (array == null) {
                return;
            }
            final int[] array2 = b ? this.horSizes : this.verSizes;
            array2[0] = array[0];
            array2[1] = array[1];
            array2[2] = array[2];
        }
        
        private void setGaps(final int[] array, final int n) {
            if (this.gaps == null) {
                this.gaps = new int[][] { null, null, null, null };
            }
            this.gaps[n] = array;
        }
        
        private void mergeGapSizes(final int[] array, final boolean b, final boolean b2) {
            if (this.gaps == null) {
                this.gaps = new int[][] { null, null, null, null };
            }
            if (array == null) {
                return;
            }
            final int gapIx = this.getGapIx(b, b2);
            int[] array2 = this.gaps[gapIx];
            if (array2 == null) {
                array2 = new int[] { 0, 0, 2097051 };
                this.gaps[gapIx] = array2;
            }
            array2[0] = Math.max(array[0], array2[0]);
            array2[1] = Math.max(array[1], array2[1]);
            array2[2] = Math.min(array[2], array2[2]);
        }
        
        private int getGapIx(final boolean b, final boolean b2) {
            return b ? (b2 ? 1 : 3) : (b2 ? 0 : 2);
        }
        
        private int getSizeInclGaps(final int n, final boolean b) {
            return this.filter(n, this.getGapBefore(n, b) + this.getSize(n, b) + this.getGapAfter(n, b));
        }
        
        private int getSize(final int n, final boolean b) {
            return this.filter(n, b ? this.horSizes[n] : this.verSizes[n]);
        }
        
        private int getGapBefore(final int n, final boolean b) {
            final int[] gaps = this.getGaps(b, true);
            return (gaps != null) ? this.filter(n, gaps[n]) : 0;
        }
        
        private int getGapAfter(final int n, final boolean b) {
            final int[] gaps = this.getGaps(b, false);
            return (gaps != null) ? this.filter(n, gaps[n]) : 0;
        }
        
        private int[] getGaps(final boolean b, final boolean b2) {
            return this.gaps[this.getGapIx(b, b2)];
        }
        
        private int filter(final int n, final int n2) {
            if (n2 == -2147471302) {
                return (n != 2) ? 0 : 2097051;
            }
            return constrainSize(n2);
        }
        
        private boolean isBaselineAlign(final boolean b) {
            final Float grow = this.cc.getVertical().getGrow();
            if (grow != null && grow.intValue() != 0) {
                return false;
            }
            final UnitValue align = this.cc.getVertical().getAlign();
            if (align != null) {
                if (align != UnitValue.BASELINE_IDENTITY) {
                    return false;
                }
            }
            else if (!b) {
                return false;
            }
            if (this.comp.hasBaseline()) {
                return true;
            }
            return false;
        }
        
        private int getBaseline(final int n) {
            return this.comp.getBaseline(this.getSize(n, true), this.getSize(n, false));
        }
    }
    
    private static class LinkedDimGroup
    {
        private static final int TYPE_SERIAL = 0;
        private static final int TYPE_PARALLEL = 1;
        private static final int TYPE_BASELINE = 2;
        private final String linkCtx;
        private final int span;
        private final int linkType;
        private final boolean isHor;
        private final boolean fromEnd;
        private ArrayList<CompWrap> _compWraps;
        private int[] sizes;
        private int lStart;
        private int lSize;
        
        private LinkedDimGroup(final String linkCtx, final int span, final int linkType, final boolean isHor, final boolean fromEnd) {
            this._compWraps = new ArrayList<CompWrap>(4);
            this.sizes = null;
            this.lStart = 0;
            this.lSize = 0;
            this.linkCtx = linkCtx;
            this.span = span;
            this.linkType = linkType;
            this.isHor = isHor;
            this.fromEnd = fromEnd;
        }
        
        private void addCompWrap(final CompWrap compWrap) {
            this._compWraps.add(compWrap);
            this.sizes = null;
        }
        
        private void setCompWraps(final ArrayList<CompWrap> compWraps) {
            if (this._compWraps != compWraps) {
                this._compWraps = compWraps;
                this.sizes = null;
            }
        }
        
        private void layout(final DimConstraint dimConstraint, final int lStart, final int lSize, final int n) {
            this.lStart = lStart;
            this.lSize = lSize;
            if (this._compWraps.size() == 0) {
                return;
            }
            final ContainerWrapper parent = this._compWraps.get(0).comp.getParent();
            if (this.linkType == 1) {
                layoutParallel(parent, this._compWraps, dimConstraint, lStart, lSize, this.isHor, this.fromEnd);
            }
            else if (this.linkType == 2) {
                layoutBaseline(parent, this._compWraps, dimConstraint, lStart, lSize, 1, n);
            }
            else {
                layoutSerial(parent, this._compWraps, dimConstraint, lStart, lSize, this.isHor, n, this.fromEnd);
            }
        }
        
        private int[] getMinPrefMax() {
            if (this.sizes == null && this._compWraps.size() > 0) {
                this.sizes = new int[3];
                for (int i = 0; i <= 1; ++i) {
                    if (this.linkType == 1) {
                        this.sizes[i] = getTotalSizeParallel(this._compWraps, i, this.isHor);
                    }
                    else if (this.linkType == 2) {
                        final int[] access$4700 = getBaselineAboveBelow(this._compWraps, i, false);
                        this.sizes[i] = access$4700[0] + access$4700[1];
                    }
                    else {
                        this.sizes[i] = getTotalSizeSerial(this._compWraps, i, this.isHor);
                    }
                }
                this.sizes[2] = 2097051;
            }
            return this.sizes;
        }
    }
    
    private static class Cell
    {
        private final int spanx;
        private final int spany;
        private final boolean flowx;
        private final ArrayList<CompWrap> compWraps;
        private boolean hasTagged;
        
        private Cell(final CompWrap compWrap) {
            this(compWrap, 1, 1, true);
        }
        
        private Cell(final int n, final int n2, final boolean b) {
            this(null, n, n2, b);
        }
        
        private Cell(final CompWrap compWrap, final int spanx, final int spany, final boolean flowx) {
            this.compWraps = new ArrayList<CompWrap>(1);
            this.hasTagged = false;
            if (compWrap != null) {
                this.compWraps.add(compWrap);
            }
            this.spanx = spanx;
            this.spany = spany;
            this.flowx = flowx;
        }
        
        static /* synthetic */ boolean access$476(final Cell cell, final int n) {
            return cell.hasTagged = ((byte)((cell.hasTagged ? 1 : 0) | n) != 0);
        }
    }
}
