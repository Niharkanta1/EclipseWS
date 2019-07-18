// 
// Decompiled by Procyon v0.5.36
// 

package com.beaudoin.jmm;

import java.io.IOException;
import com.beaudoin.jmm.process.NativeProcess;

public final class Main
{
    public static void main(final String[] args) throws IOException {
        final NativeProcess proc = NativeProcess.byName("C__Stuff.exe");
    }
}
