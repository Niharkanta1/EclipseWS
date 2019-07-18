// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.misc;

import java.util.HashMap;
import net.openhft.hashing.LongHashFunction;
import java.util.Map;

public final class Strings
{
    private static Map<Long, String> map;
    
    public static String transform(final byte[] bytes) {
        final long hash = LongHashFunction.xx_r39().hashBytes(bytes);
        if (Strings.map.containsKey(hash)) {
            return Strings.map.get(hash);
        }
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] == 0) {
                bytes[i] = 32;
            }
        }
        final String string = new String(bytes).split(" ")[0].trim().intern();
        Strings.map.put(hash, string);
        return string;
    }
    
    public static String hex(final int value) {
        return "0x" + Integer.toHexString(value).toUpperCase();
    }
    
    static {
        Strings.map = new HashMap<Long, String>(16982);
    }
}
