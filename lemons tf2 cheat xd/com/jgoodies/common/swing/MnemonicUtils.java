// 
// Decompiled by Procyon v0.5.36
// 

package com.jgoodies.common.swing;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import com.jgoodies.common.base.Strings;
import javax.swing.JLabel;
import javax.swing.Action;
import com.jgoodies.common.base.Preconditions;
import javax.swing.AbstractButton;

public final class MnemonicUtils
{
    static final char MNEMONIC_MARKER = '&';
    
    private MnemonicUtils() {
    }
    
    public static void configure(final AbstractButton target, final String markedText) {
        Preconditions.checkNotNull(target, "The %1$s must not be null.", "target");
        configure0(target, new MnemonicText(markedText, '&'));
    }
    
    public static void configure(final Action target, final String markedText) {
        Preconditions.checkNotNull(target, "The %1$s must not be null.", "target");
        configure0(target, new MnemonicText(markedText, '&'));
    }
    
    public static void configure(final JLabel target, final String markedText) {
        Preconditions.checkNotNull(target, "The %1$s must not be null.", "target");
        configure0(target, new MnemonicText(markedText, '&'));
    }
    
    public static String plainText(final String markedText) {
        return new MnemonicText(markedText, '&').text;
    }
    
    static int mnemonic(final String markedText) {
        return new MnemonicText(markedText, '&').key;
    }
    
    static int mnemonicIndex(final String markedText) {
        return new MnemonicText(markedText, '&').index;
    }
    
    private static void configure0(final AbstractButton button, final MnemonicText mnemonicText) {
        button.setText(mnemonicText.text);
        button.setMnemonic(mnemonicText.key);
        button.setDisplayedMnemonicIndex(mnemonicText.index);
    }
    
    private static void configure0(final Action action, final MnemonicText mnemonicText) {
        final Integer keyValue = mnemonicText.key;
        final Integer indexValue = (mnemonicText.index == -1) ? null : Integer.valueOf(mnemonicText.index);
        action.putValue("Name", mnemonicText.text);
        action.putValue("MnemonicKey", keyValue);
        action.putValue("SwingDisplayedMnemonicIndexKey", indexValue);
    }
    
    private static void configure0(final JLabel label, final MnemonicText mnemonicText) {
        label.setText(mnemonicText.text);
        label.setDisplayedMnemonic(mnemonicText.key);
        label.setDisplayedMnemonicIndex(mnemonicText.index);
    }
    
    private static final class MnemonicText
    {
        String text;
        int key;
        int index;
        
        private MnemonicText(final String markedText, final char marker) {
            int i;
            if (markedText == null || markedText.length() <= 1 || (i = markedText.indexOf(marker)) == -1) {
                this.text = markedText;
                this.key = 0;
                this.index = -1;
                return;
            }
            final boolean html = Strings.startsWithIgnoreCase(markedText, "<html>");
            final StringBuilder builder = new StringBuilder();
            int begin = 0;
            int quotedMarkers = 0;
            int markerIndex = -1;
            boolean marked = false;
            char markedChar = '\0';
            final CharacterIterator sci = new StringCharacterIterator(markedText);
            do {
                builder.append(markedText.substring(begin, i));
                final char current = sci.setIndex(i);
                final char next = sci.next();
                if (html) {
                    final int entityEnd = indexOfEntityEnd(markedText, i);
                    if (entityEnd == -1) {
                        marked = true;
                        builder.append("<u>").append(next).append("</u>");
                        begin = i + 2;
                        markedChar = next;
                    }
                    else {
                        builder.append(markedText.substring(i, entityEnd));
                        begin = entityEnd;
                    }
                }
                else if (next == marker) {
                    builder.append(next);
                    begin = i + 2;
                    ++quotedMarkers;
                }
                else if (Character.isWhitespace(next)) {
                    builder.append(current).append(next);
                    begin = i + 2;
                }
                else if (next == '\uffff') {
                    builder.append(current);
                    begin = i + 2;
                }
                else {
                    builder.append(next);
                    begin = i + 2;
                    markerIndex = i - quotedMarkers;
                    marked = true;
                    markedChar = next;
                }
                i = markedText.indexOf(marker, begin);
            } while (i != -1 && !marked);
            if (begin < markedText.length()) {
                builder.append(markedText.substring(begin));
            }
            this.text = builder.toString();
            this.index = markerIndex;
            if (marked) {
                this.key = mnemonicKey(markedChar);
            }
            else {
                this.key = 0;
            }
        }
        
        private static int indexOfEntityEnd(final String htmlText, final int start) {
            final CharacterIterator sci = new StringCharacterIterator(htmlText, start);
            char c;
            do {
                c = sci.next();
                if (c == ';') {
                    return sci.getIndex();
                }
                if (!Character.isLetterOrDigit(c)) {
                    return -1;
                }
            } while (c != '\uffff');
            return -1;
        }
        
        private static int mnemonicKey(final char c) {
            int vk = c;
            if (vk >= 97 && vk <= 122) {
                vk -= 32;
            }
            return vk;
        }
    }
}
