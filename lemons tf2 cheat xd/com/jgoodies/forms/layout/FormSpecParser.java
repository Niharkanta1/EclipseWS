// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.forms.layout;

import java.util.regex.Matcher;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.jgoodies.common.base.Preconditions;
import java.util.regex.Pattern;

public final class FormSpecParser
{
    private static final Pattern MULTIPLIER_PREFIX_PATTERN;
    private static final Pattern DIGIT_PATTERN;
    private final String source;
    private final LayoutMap layoutMap;
    
    private FormSpecParser(final String source, final String description, final LayoutMap layoutMap, final boolean horizontal) {
        Preconditions.checkNotNull(source, "The %S must not be null.", description);
        Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
        this.layoutMap = layoutMap;
        this.source = this.layoutMap.expand(source, horizontal);
    }
    
    static ColumnSpec[] parseColumnSpecs(final String encodedColumnSpecs, final LayoutMap layoutMap) {
        final FormSpecParser parser = new FormSpecParser(encodedColumnSpecs, "encoded column specifications", layoutMap, true);
        return parser.parseColumnSpecs();
    }
    
    static RowSpec[] parseRowSpecs(final String encodedRowSpecs, final LayoutMap layoutMap) {
        final FormSpecParser parser = new FormSpecParser(encodedRowSpecs, "encoded row specifications", layoutMap, false);
        return parser.parseRowSpecs();
    }
    
    private ColumnSpec[] parseColumnSpecs() {
        final List encodedColumnSpecs = this.split(this.source, 0);
        final int columnCount = encodedColumnSpecs.size();
        final ColumnSpec[] columnSpecs = new ColumnSpec[columnCount];
        for (int i = 0; i < columnCount; ++i) {
            final String encodedSpec = encodedColumnSpecs.get(i);
            columnSpecs[i] = ColumnSpec.decodeExpanded(encodedSpec);
        }
        return columnSpecs;
    }
    
    private RowSpec[] parseRowSpecs() {
        final List encodedRowSpecs = this.split(this.source, 0);
        final int rowCount = encodedRowSpecs.size();
        final RowSpec[] rowSpecs = new RowSpec[rowCount];
        for (int i = 0; i < rowCount; ++i) {
            final String encodedSpec = encodedRowSpecs.get(i);
            rowSpecs[i] = RowSpec.decodeExpanded(encodedSpec);
        }
        return rowSpecs;
    }
    
    private List<String> split(final String expression, final int offset) {
        final List<String> encodedSpecs = new ArrayList<String>();
        int parenthesisLevel = 0;
        int bracketLevel = 0;
        int quoteLevel = 0;
        final int length = expression.length();
        int specStart = 0;
        boolean lead = true;
        for (int i = 0; i < length; ++i) {
            final char c = expression.charAt(i);
            if (lead && Character.isWhitespace(c)) {
                ++specStart;
            }
            else {
                lead = false;
                if (c == ',' && parenthesisLevel == 0 && bracketLevel == 0 && quoteLevel == 0) {
                    final String token = expression.substring(specStart, i);
                    this.addSpec(encodedSpecs, token, offset + specStart);
                    specStart = i + 1;
                    lead = true;
                }
                else if (c == '(') {
                    if (bracketLevel > 0) {
                        this.fail(offset + i, "illegal '(' in [...]");
                    }
                    ++parenthesisLevel;
                }
                else if (c == ')') {
                    if (bracketLevel > 0) {
                        this.fail(offset + i, "illegal ')' in [...]");
                    }
                    if (--parenthesisLevel < 0) {
                        this.fail(offset + i, "missing '('");
                    }
                }
                else if (c == '[') {
                    if (bracketLevel > 0) {
                        this.fail(offset + i, "too many '['");
                    }
                    ++bracketLevel;
                }
                else if (c == ']') {
                    if (--bracketLevel < 0) {
                        this.fail(offset + i, "missing '['");
                    }
                }
                else if (c == '\'') {
                    if (quoteLevel == 0) {
                        ++quoteLevel;
                    }
                    else if (quoteLevel == 1) {
                        --quoteLevel;
                    }
                }
            }
        }
        if (parenthesisLevel > 0) {
            this.fail(offset + length, "missing ')'");
        }
        if (bracketLevel > 0) {
            this.fail(offset + length, "missing ']");
        }
        if (specStart < length) {
            final String token2 = expression.substring(specStart);
            this.addSpec(encodedSpecs, token2, offset + specStart);
        }
        return encodedSpecs;
    }
    
    private void addSpec(final List<String> encodedSpecs, final String expression, final int offset) {
        final String trimmedExpression = expression.trim();
        final Multiplier multiplier = this.multiplier(trimmedExpression, offset);
        if (multiplier == null) {
            encodedSpecs.add(trimmedExpression);
            return;
        }
        final List<String> subTokenList = this.split(multiplier.expression, offset + multiplier.offset);
        for (int i = 0; i < multiplier.multiplier; ++i) {
            encodedSpecs.addAll(subTokenList);
        }
    }
    
    private Multiplier multiplier(final String expression, final int offset) {
        final Matcher matcher = FormSpecParser.MULTIPLIER_PREFIX_PATTERN.matcher(expression);
        if (!matcher.find()) {
            return null;
        }
        if (matcher.start() > 0) {
            this.fail(offset + matcher.start(), "illegal multiplier position");
        }
        final Matcher digitMatcher = FormSpecParser.DIGIT_PATTERN.matcher(expression);
        if (!digitMatcher.find()) {
            return null;
        }
        final String digitStr = expression.substring(0, digitMatcher.end());
        if (digitStr.startsWith("-")) {
            this.fail(offset, "illegal negative multiplier designation");
        }
        int number = 0;
        try {
            number = Integer.parseInt(digitStr);
        }
        catch (NumberFormatException ex) {
            this.fail(offset, ex);
        }
        if (number < 0) {
            this.fail(offset, "illegal negative multiplier");
        }
        final String subexpression = expression.substring(matcher.end(), expression.length() - 1);
        return new Multiplier(number, subexpression, matcher.end());
    }
    
    public static void fail(final String source, final int index, final String description) {
        throw new FormLayoutParseException(message(source, index, description));
    }
    
    private void fail(final int index, final String description) {
        throw new FormLayoutParseException(message(this.source, index, description));
    }
    
    private void fail(final int index, final NumberFormatException cause) {
        throw new FormLayoutParseException(message(this.source, index, "Invalid multiplier"), cause);
    }
    
    private static String message(final String source, final int index, final String description) {
        final StringBuffer buffer = new StringBuffer(10);
        buffer.append('\n');
        buffer.append(source);
        buffer.append('\n');
        for (int i = 0; i < index; ++i) {
            buffer.append(' ');
        }
        buffer.append('^');
        buffer.append(description);
        final String message = buffer.toString();
        throw new FormLayoutParseException(message);
    }
    
    static {
        MULTIPLIER_PREFIX_PATTERN = Pattern.compile("-?\\d+\\s*\\*\\s*\\(");
        DIGIT_PATTERN = Pattern.compile("-?\\d+");
    }
    
    public static final class FormLayoutParseException extends RuntimeException
    {
        FormLayoutParseException(final String message) {
            super(message);
        }
        
        FormLayoutParseException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    static final class Multiplier
    {
        final int multiplier;
        final String expression;
        final int offset;
        
        Multiplier(final int multiplier, final String expression, final int offset) {
            this.multiplier = multiplier;
            this.expression = expression;
            this.offset = offset;
        }
    }
}
