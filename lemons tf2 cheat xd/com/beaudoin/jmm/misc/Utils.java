// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm.misc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public final class Utils
{
    public static int exec(final String... command) {
        try {
            return Integer.parseInt(new Scanner(Runtime.getRuntime().exec(command).getInputStream()).next());
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read output from " + Arrays.toString(command));
        }
    }
}
