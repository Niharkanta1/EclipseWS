// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import com.jgoodies.forms.util.LayoutStyle;
import java.util.Iterator;
import java.util.Locale;
import com.jgoodies.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

public final class LayoutMap
{
    private static final char VARIABLE_PREFIX_CHAR = '$';
    private static final Map<String, String> COLUMN_ALIASES;
    private static final Map<String, String> ROW_ALIASES;
    private static LayoutMap root;
    private final LayoutMap parent;
    private final Map<String, String> columnMap;
    private final Map<String, String> columnMapCache;
    private final Map<String, String> rowMap;
    private final Map<String, String> rowMapCache;
    
    public LayoutMap() {
        this(getRoot());
    }
    
    public LayoutMap(final LayoutMap parent) {
        this.parent = parent;
        this.columnMap = new HashMap<String, String>();
        this.rowMap = new HashMap<String, String>();
        this.columnMapCache = new HashMap<String, String>();
        this.rowMapCache = new HashMap<String, String>();
    }
    
    public static synchronized LayoutMap getRoot() {
        if (LayoutMap.root == null) {
            LayoutMap.root = createRoot();
        }
        return LayoutMap.root;
    }
    
    public boolean columnContainsKey(final String key) {
        final String resolvedKey = resolveColumnKey(key);
        return this.columnMap.containsKey(resolvedKey) || (this.parent != null && this.parent.columnContainsKey(resolvedKey));
    }
    
    public String columnGet(final String key) {
        final String resolvedKey = resolveColumnKey(key);
        final String cachedValue = this.columnMapCache.get(resolvedKey);
        if (cachedValue != null) {
            return cachedValue;
        }
        String value = this.columnMap.get(resolvedKey);
        if (value == null && this.parent != null) {
            value = this.parent.columnGet(resolvedKey);
        }
        if (value == null) {
            return null;
        }
        final String expandedString = this.expand(value, true);
        this.columnMapCache.put(resolvedKey, expandedString);
        return expandedString;
    }
    
    public String columnPut(final String key, final String value) {
        Preconditions.checkNotNull(value, "The column expression value must not be null.");
        final String resolvedKey = resolveColumnKey(key);
        this.columnMapCache.clear();
        return this.columnMap.put(resolvedKey, value.toLowerCase(Locale.ENGLISH));
    }
    
    public String columnPut(final String key, final ColumnSpec value) {
        return this.columnPut(key, value.encode());
    }
    
    public String columnPut(final String key, final Size value) {
        return this.columnPut(key, value.encode());
    }
    
    public String columnRemove(final String key) {
        final String resolvedKey = resolveColumnKey(key);
        this.columnMapCache.clear();
        return this.columnMap.remove(resolvedKey);
    }
    
    public boolean rowContainsKey(final String key) {
        final String resolvedKey = resolveRowKey(key);
        return this.rowMap.containsKey(resolvedKey) || (this.parent != null && this.parent.rowContainsKey(resolvedKey));
    }
    
    public String rowGet(final String key) {
        final String resolvedKey = resolveRowKey(key);
        final String cachedValue = this.rowMapCache.get(resolvedKey);
        if (cachedValue != null) {
            return cachedValue;
        }
        String value = this.rowMap.get(resolvedKey);
        if (value == null && this.parent != null) {
            value = this.parent.rowGet(resolvedKey);
        }
        if (value == null) {
            return null;
        }
        final String expandedString = this.expand(value, false);
        this.rowMapCache.put(resolvedKey, expandedString);
        return expandedString;
    }
    
    public String rowPut(final String key, final String value) {
        Preconditions.checkNotNull(value, "The row expression value must not be null.");
        final String resolvedKey = resolveRowKey(key);
        this.rowMapCache.clear();
        return this.rowMap.put(resolvedKey, value.toLowerCase(Locale.ENGLISH));
    }
    
    public String rowPut(final String key, final RowSpec value) {
        return this.rowPut(key, value.encode());
    }
    
    public String rowPut(final String key, final Size value) {
        return this.rowPut(key, value.encode());
    }
    
    public String rowRemove(final String key) {
        final String resolvedKey = resolveRowKey(key);
        this.rowMapCache.clear();
        return this.rowMap.remove(resolvedKey);
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append("\n  Column associations:");
        for (final Map.Entry entry : this.columnMap.entrySet()) {
            buffer.append("\n    ");
            buffer.append(entry.getKey());
            buffer.append("->");
            buffer.append(entry.getValue());
        }
        buffer.append("\n  Row associations:");
        for (final Map.Entry entry : this.rowMap.entrySet()) {
            buffer.append("\n    ");
            buffer.append(entry.getKey());
            buffer.append("->");
            buffer.append(entry.getValue());
        }
        return buffer.toString();
    }
    
    String expand(final String expression, final boolean horizontal) {
        int cursor = 0;
        int start = expression.indexOf(36, cursor);
        if (start == -1) {
            return expression;
        }
        final StringBuffer buffer = new StringBuffer();
        do {
            buffer.append(expression.substring(cursor, start));
            final String variableName = nextVariableName(expression, start);
            buffer.append(this.expansion(variableName, horizontal));
            cursor = start + variableName.length() + 1;
            start = expression.indexOf(36, cursor);
        } while (start != -1);
        buffer.append(expression.substring(cursor));
        return buffer.toString();
    }
    
    private static String nextVariableName(final String expression, final int start) {
        final int length = expression.length();
        if (length <= start) {
            FormSpecParser.fail(expression, start, "Missing variable name after variable char '$'.");
        }
        if (expression.charAt(start + 1) == '{') {
            final int end = expression.indexOf(125, start + 1);
            if (end == -1) {
                FormSpecParser.fail(expression, start, "Missing closing brace '}' for variable.");
            }
            return expression.substring(start + 1, end + 1);
        }
        int end;
        for (end = start + 1; end < length && Character.isUnicodeIdentifierPart(expression.charAt(end)); ++end) {}
        return expression.substring(start + 1, end);
    }
    
    private String expansion(final String variableName, final boolean horizontal) {
        final String key = stripBraces(variableName);
        final String expansion = horizontal ? this.columnGet(key) : this.rowGet(key);
        if (expansion == null) {
            final String orientation = horizontal ? "column" : "row";
            throw new IllegalArgumentException("Unknown " + orientation + " layout variable \"" + key + "\"");
        }
        return expansion;
    }
    
    private static String stripBraces(final String variableName) {
        return (variableName.charAt(0) == '{') ? variableName.substring(1, variableName.length() - 1) : variableName;
    }
    
    private static String resolveColumnKey(final String key) {
        Preconditions.checkNotNull(key, "The column key must not be null.");
        final String lowercaseKey = key.toLowerCase(Locale.ENGLISH);
        final String defaultKey = LayoutMap.COLUMN_ALIASES.get(lowercaseKey);
        return (defaultKey == null) ? lowercaseKey : defaultKey;
    }
    
    private static String resolveRowKey(final String key) {
        Preconditions.checkNotNull(key, "The row key must not be null.");
        final String lowercaseKey = key.toLowerCase(Locale.ENGLISH);
        final String defaultKey = LayoutMap.ROW_ALIASES.get(lowercaseKey);
        return (defaultKey == null) ? lowercaseKey : defaultKey;
    }
    
    private static LayoutMap createRoot() {
        final LayoutMap map = new LayoutMap(null);
        map.columnPut("label-component-gap", new String[] { "lcg", "lcgap" }, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC);
        map.columnPut("related-gap", new String[] { "rg", "rgap" }, FormSpecs.RELATED_GAP_COLSPEC);
        map.columnPut("unrelated-gap", new String[] { "ug", "ugap" }, FormSpecs.UNRELATED_GAP_COLSPEC);
        map.columnPut("button", new String[] { "b" }, FormSpecs.BUTTON_COLSPEC);
        map.columnPut("growing-button", new String[] { "gb" }, FormSpecs.GROWING_BUTTON_COLSPEC);
        map.columnPut("dialog-margin", new String[] { "dm", "dmargin" }, ColumnSpec.createGap(LayoutStyle.getCurrent().getDialogMarginX()));
        map.columnPut("tabbed-dialog-margin", new String[] { "tdm", "tdmargin" }, ColumnSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginX()));
        map.columnPut("glue", FormSpecs.GLUE_COLSPEC.toShortString());
        map.rowPut("label-component-gap", new String[] { "lcg", "lcgap" }, FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC);
        map.rowPut("related-gap", new String[] { "rg", "rgap" }, FormSpecs.RELATED_GAP_ROWSPEC);
        map.rowPut("unrelated-gap", new String[] { "ug", "ugap" }, FormSpecs.UNRELATED_GAP_ROWSPEC);
        map.rowPut("narrow-line-gap", new String[] { "nlg", "nlgap" }, FormSpecs.NARROW_LINE_GAP_ROWSPEC);
        map.rowPut("line-gap", new String[] { "lg", "lgap" }, FormSpecs.LINE_GAP_ROWSPEC);
        map.rowPut("paragraph-gap", new String[] { "pg", "pgap" }, FormSpecs.PARAGRAPH_GAP_ROWSPEC);
        map.rowPut("dialog-margin", new String[] { "dm", "dmargin" }, RowSpec.createGap(LayoutStyle.getCurrent().getDialogMarginY()));
        map.rowPut("tabbed-dialog-margin", new String[] { "tdm", "tdmargin" }, RowSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginY()));
        map.rowPut("button", new String[] { "b" }, FormSpecs.BUTTON_ROWSPEC);
        map.rowPut("glue", FormSpecs.GLUE_ROWSPEC);
        return map;
    }
    
    private void columnPut(final String key, final String[] aliases, final ColumnSpec value) {
        ensureLowerCase(key);
        this.columnPut(key, value);
        for (final String aliase : aliases) {
            ensureLowerCase(aliase);
            LayoutMap.COLUMN_ALIASES.put(aliase, key);
        }
    }
    
    private void rowPut(final String key, final String[] aliases, final RowSpec value) {
        ensureLowerCase(key);
        this.rowPut(key, value);
        for (final String aliase : aliases) {
            ensureLowerCase(aliase);
            LayoutMap.ROW_ALIASES.put(aliase, key);
        }
    }
    
    private static void ensureLowerCase(final String str) {
        final String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (!lowerCase.equals(str)) {
            throw new IllegalArgumentException("The string \"" + str + "\" should be lower case.");
        }
    }
    
    static {
        COLUMN_ALIASES = new HashMap<String, String>();
        ROW_ALIASES = new HashMap<String, String>();
        LayoutMap.root = null;
    }
}
