// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.List;
import java.awt.Container;
import java.util.Locale;
import com.jgoodies.common.base.Preconditions;
import java.util.regex.Pattern;
import java.io.Serializable;

public abstract class FormSpec implements Serializable
{
    static final DefaultAlignment LEFT_ALIGN;
    static final DefaultAlignment RIGHT_ALIGN;
    static final DefaultAlignment TOP_ALIGN;
    static final DefaultAlignment BOTTOM_ALIGN;
    static final DefaultAlignment CENTER_ALIGN;
    static final DefaultAlignment FILL_ALIGN;
    static final DefaultAlignment NO_ALIGN;
    private static final DefaultAlignment[] VALUES;
    public static final double NO_GROW = 0.0;
    public static final double DEFAULT_GROW = 1.0;
    private static final Pattern TOKEN_SEPARATOR_PATTERN;
    private static final Pattern BOUNDS_SEPARATOR_PATTERN;
    private DefaultAlignment defaultAlignment;
    private boolean defaultAlignmentExplicitlySet;
    private Size size;
    private double resizeWeight;
    
    protected FormSpec(final DefaultAlignment defaultAlignment, final Size size, final double resizeWeight) {
        Preconditions.checkNotNull(size, "The size must not be null.");
        Preconditions.checkArgument(resizeWeight >= 0.0, "The resize weight must be non-negative.");
        this.defaultAlignment = defaultAlignment;
        this.size = size;
        this.resizeWeight = resizeWeight;
    }
    
    protected FormSpec(final DefaultAlignment defaultAlignment, final String encodedDescription) {
        this(defaultAlignment, Sizes.DEFAULT, 0.0);
        this.parseAndInitValues(encodedDescription.toLowerCase(Locale.ENGLISH));
    }
    
    public final DefaultAlignment getDefaultAlignment() {
        return this.defaultAlignment;
    }
    
    public final boolean getDefaultAlignmentExplictlySet() {
        return this.defaultAlignmentExplicitlySet;
    }
    
    public final Size getSize() {
        return this.size;
    }
    
    public final double getResizeWeight() {
        return this.resizeWeight;
    }
    
    final boolean canGrow() {
        return this.getResizeWeight() != 0.0;
    }
    
    abstract boolean isHorizontal();
    
    void setDefaultAlignment(final DefaultAlignment defaultAlignment) {
        this.defaultAlignment = defaultAlignment;
        this.defaultAlignmentExplicitlySet = true;
    }
    
    void setSize(final Size size) {
        this.size = size;
    }
    
    void setResizeWeight(final double resizeWeight) {
        this.resizeWeight = resizeWeight;
    }
    
    private void parseAndInitValues(final String encodedDescription) {
        Preconditions.checkNotBlank(encodedDescription, "The encoded form specification must not be null, empty or whitespace.");
        final String[] token = FormSpec.TOKEN_SEPARATOR_PATTERN.split(encodedDescription);
        Preconditions.checkArgument(token.length > 0, "The form spec must not be empty.");
        int nextIndex = 0;
        String next = token[nextIndex++];
        final DefaultAlignment alignment = valueOf(next, this.isHorizontal());
        if (alignment != null) {
            this.setDefaultAlignment(alignment);
            Preconditions.checkArgument(token.length > 1, "The form spec must provide a size.");
            next = token[nextIndex++];
        }
        this.setSize(this.parseSize(next));
        if (nextIndex < token.length) {
            this.setResizeWeight(parseResizeWeight(token[nextIndex]));
        }
    }
    
    private Size parseSize(final String token) {
        if (token.startsWith("[") && token.endsWith("]")) {
            return this.parseBoundedSize(token);
        }
        if (token.startsWith("max(") && token.endsWith(")")) {
            return this.parseOldBoundedSize(token, false);
        }
        if (token.startsWith("min(") && token.endsWith(")")) {
            return this.parseOldBoundedSize(token, true);
        }
        return this.parseAtomicSize(token);
    }
    
    private Size parseBoundedSize(final String token) {
        final String content = token.substring(1, token.length() - 1);
        final String[] subtoken = FormSpec.BOUNDS_SEPARATOR_PATTERN.split(content);
        Size basis = null;
        Size lower = null;
        Size upper = null;
        if (subtoken.length == 2) {
            final Size size1 = this.parseAtomicSize(subtoken[0]);
            final Size size2 = this.parseAtomicSize(subtoken[1]);
            if (isConstant(size1)) {
                if (isConstant(size2)) {
                    lower = size1;
                    basis = size2;
                    upper = size2;
                }
                else {
                    lower = size1;
                    basis = size2;
                }
            }
            else {
                basis = size1;
                upper = size2;
            }
        }
        else if (subtoken.length == 3) {
            lower = this.parseAtomicSize(subtoken[0]);
            basis = this.parseAtomicSize(subtoken[1]);
            upper = this.parseAtomicSize(subtoken[2]);
        }
        if ((lower == null || isConstant(lower)) && (upper == null || isConstant(upper))) {
            return new BoundedSize(basis, lower, upper);
        }
        throw new IllegalArgumentException("Illegal bounded size '" + token + "'. Must be one of:" + "\n[<constant size>,<logical size>]                 // lower bound" + "\n[<logical size>,<constant size>]                 // upper bound" + "\n[<constant size>,<logical size>,<constant size>] // lower and upper bound." + "\nExamples:" + "\n[50dlu,pref]                                     // lower bound" + "\n[pref,200dlu]                                    // upper bound" + "\n[50dlu,pref,200dlu]                              // lower and upper bound.");
    }
    
    private Size parseOldBoundedSize(final String token, final boolean setMax) {
        final int semicolonIndex = token.indexOf(59);
        final String sizeToken1 = token.substring(4, semicolonIndex);
        final String sizeToken2 = token.substring(semicolonIndex + 1, token.length() - 1);
        final Size size1 = this.parseAtomicSize(sizeToken1);
        final Size size2 = this.parseAtomicSize(sizeToken2);
        if (isConstant(size1)) {
            if (size2 instanceof Sizes.ComponentSize) {
                return new BoundedSize(size2, setMax ? null : size1, setMax ? size1 : null);
            }
            throw new IllegalArgumentException("Bounded sizes must not be both constants.");
        }
        else {
            if (isConstant(size2)) {
                return new BoundedSize(size1, setMax ? null : size2, setMax ? size2 : null);
            }
            throw new IllegalArgumentException("Bounded sizes must not be both logical.");
        }
    }
    
    private Size parseAtomicSize(final String token) {
        final String trimmedToken = token.trim();
        if (trimmedToken.startsWith("'") && trimmedToken.endsWith("'")) {
            final int length = trimmedToken.length();
            if (length < 2) {
                throw new IllegalArgumentException("Missing closing \"'\" for prototype.");
            }
            return new PrototypeSize(trimmedToken.substring(1, length - 1));
        }
        else {
            final Sizes.ComponentSize componentSize = Sizes.ComponentSize.valueOf(trimmedToken);
            if (componentSize != null) {
                return componentSize;
            }
            return ConstantSize.valueOf(trimmedToken, this.isHorizontal());
        }
    }
    
    private static double parseResizeWeight(final String token) {
        if (token.equals("g") || token.equals("grow")) {
            return 1.0;
        }
        if (token.equals("n") || token.equals("nogrow") || token.equals("none")) {
            return 0.0;
        }
        if ((token.startsWith("grow(") || token.startsWith("g(")) && token.endsWith(")")) {
            final int leftParen = token.indexOf(40);
            final int rightParen = token.indexOf(41);
            final String substring = token.substring(leftParen + 1, rightParen);
            return Double.parseDouble(substring);
        }
        throw new IllegalArgumentException("The resize argument '" + token + "' is invalid. " + " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
    }
    
    private static boolean isConstant(final Size aSize) {
        return aSize instanceof ConstantSize || aSize instanceof PrototypeSize;
    }
    
    @Override
    public final String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment);
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0) {
            buffer.append("noGrow");
        }
        else if (this.resizeWeight == 1.0) {
            buffer.append("grow");
        }
        else {
            buffer.append("grow(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }
    
    public final String toShortString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment.abbreviation());
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0) {
            buffer.append("n");
        }
        else if (this.resizeWeight == 1.0) {
            buffer.append("g");
        }
        else {
            buffer.append("g(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }
    
    public final String encode() {
        final StringBuffer buffer = new StringBuffer();
        final DefaultAlignment alignmentDefault = this.isHorizontal() ? ColumnSpec.DEFAULT : RowSpec.DEFAULT;
        if (!alignmentDefault.equals(this.defaultAlignment)) {
            buffer.append(this.defaultAlignment.abbreviation());
            buffer.append(":");
        }
        buffer.append(this.size.encode());
        if (this.resizeWeight != 0.0) {
            if (this.resizeWeight == 1.0) {
                buffer.append(':');
                buffer.append("g");
            }
            else {
                buffer.append(':');
                buffer.append("g(");
                buffer.append(this.resizeWeight);
                buffer.append(')');
            }
        }
        return buffer.toString();
    }
    
    final int maximumSize(final Container container, final List components, final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure, final FormLayout.Measure defaultMeasure) {
        return this.size.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
    }
    
    static {
        LEFT_ALIGN = new DefaultAlignment("left");
        RIGHT_ALIGN = new DefaultAlignment("right");
        TOP_ALIGN = new DefaultAlignment("top");
        BOTTOM_ALIGN = new DefaultAlignment("bottom");
        CENTER_ALIGN = new DefaultAlignment("center");
        FILL_ALIGN = new DefaultAlignment("fill");
        NO_ALIGN = new DefaultAlignment("none");
        VALUES = new DefaultAlignment[] { FormSpec.LEFT_ALIGN, FormSpec.RIGHT_ALIGN, FormSpec.TOP_ALIGN, FormSpec.BOTTOM_ALIGN, FormSpec.CENTER_ALIGN, FormSpec.FILL_ALIGN, FormSpec.NO_ALIGN };
        TOKEN_SEPARATOR_PATTERN = Pattern.compile(":");
        BOUNDS_SEPARATOR_PATTERN = Pattern.compile("\\s*,\\s*");
    }
    
    public static final class DefaultAlignment implements Serializable
    {
        private final transient String name;
        private static int nextOrdinal;
        private final int ordinal;
        
        private DefaultAlignment(final String name) {
            this.ordinal = DefaultAlignment.nextOrdinal++;
            this.name = name;
        }
        
        private static DefaultAlignment valueOf(final String str, final boolean isHorizontal) {
            if (str.equals("f") || str.equals("fill")) {
                return FormSpec.FILL_ALIGN;
            }
            if (str.equals("c") || str.equals("center")) {
                return FormSpec.CENTER_ALIGN;
            }
            if (isHorizontal) {
                if (str.equals("r") || str.equals("right")) {
                    return FormSpec.RIGHT_ALIGN;
                }
                if (str.equals("l") || str.equals("left")) {
                    return FormSpec.LEFT_ALIGN;
                }
                if (str.equals("none")) {
                    return FormSpec.NO_ALIGN;
                }
                return null;
            }
            else {
                if (str.equals("t") || str.equals("top")) {
                    return FormSpec.TOP_ALIGN;
                }
                if (str.equals("b") || str.equals("bottom")) {
                    return FormSpec.BOTTOM_ALIGN;
                }
                return null;
            }
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public char abbreviation() {
            return this.name.charAt(0);
        }
        
        private Object readResolve() {
            return FormSpec.VALUES[this.ordinal];
        }
        
        static {
            DefaultAlignment.nextOrdinal = 0;
        }
    }
}
