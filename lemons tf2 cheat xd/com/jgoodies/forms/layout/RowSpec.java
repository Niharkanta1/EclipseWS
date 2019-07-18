// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.HashMap;
import java.util.Locale;
import com.jgoodies.common.base.Preconditions;
import java.util.Map;

public final class RowSpec extends FormSpec
{
    public static final DefaultAlignment TOP;
    public static final DefaultAlignment CENTER;
    public static final DefaultAlignment BOTTOM;
    public static final DefaultAlignment FILL;
    public static final DefaultAlignment DEFAULT;
    private static final Map<String, RowSpec> CACHE;
    
    public RowSpec(final DefaultAlignment defaultAlignment, final Size size, final double resizeWeight) {
        super(defaultAlignment, size, resizeWeight);
    }
    
    public RowSpec(final Size size) {
        super(RowSpec.DEFAULT, size, 0.0);
    }
    
    private RowSpec(final String encodedDescription) {
        super(RowSpec.DEFAULT, encodedDescription);
    }
    
    public static RowSpec createGap(final ConstantSize gapHeight) {
        return new RowSpec(RowSpec.DEFAULT, gapHeight, 0.0);
    }
    
    public static RowSpec decode(final String encodedRowSpec) {
        return decode(encodedRowSpec, LayoutMap.getRoot());
    }
    
    public static RowSpec decode(final String encodedRowSpec, final LayoutMap layoutMap) {
        Preconditions.checkNotBlank(encodedRowSpec, "The encoded row specification must not be null, empty or whitespace.");
        Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
        final String trimmed = encodedRowSpec.trim();
        final String lower = trimmed.toLowerCase(Locale.ENGLISH);
        return decodeExpanded(layoutMap.expand(lower, false));
    }
    
    static RowSpec decodeExpanded(final String expandedTrimmedLowerCaseSpec) {
        RowSpec spec = RowSpec.CACHE.get(expandedTrimmedLowerCaseSpec);
        if (spec == null) {
            spec = new RowSpec(expandedTrimmedLowerCaseSpec);
            RowSpec.CACHE.put(expandedTrimmedLowerCaseSpec, spec);
        }
        return spec;
    }
    
    public static RowSpec[] decodeSpecs(final String encodedRowSpecs) {
        return decodeSpecs(encodedRowSpecs, LayoutMap.getRoot());
    }
    
    public static RowSpec[] decodeSpecs(final String encodedRowSpecs, final LayoutMap layoutMap) {
        return FormSpecParser.parseRowSpecs(encodedRowSpecs, layoutMap);
    }
    
    protected boolean isHorizontal() {
        return false;
    }
    
    static {
        TOP = FormSpec.TOP_ALIGN;
        CENTER = FormSpec.CENTER_ALIGN;
        BOTTOM = FormSpec.BOTTOM_ALIGN;
        FILL = FormSpec.FILL_ALIGN;
        DEFAULT = RowSpec.CENTER;
        CACHE = new HashMap<String, RowSpec>();
    }
}
