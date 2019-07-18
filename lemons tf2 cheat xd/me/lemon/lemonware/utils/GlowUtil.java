// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.utils;

import java.util.Iterator;
import me.lemon.lemonware.tf2.Entity;
import me.lemon.lemonware.Main;
import me.lemon.lemonware.tf2.LocalPlayer;

public class GlowUtil
{
    public static void doGlow(final LocalPlayer local) {
        for (final Entity entity : Main.INSTANCE.getEntityList().getEntities()) {
            if (entity != local && !entity.isDormant() && entity.getHp() > 0L) {
                Main.INSTANCE.getTF2Process().writeBoolean(entity.getAddress() + Main.INSTANCE.getMemory().getOffset("m_bGlowEnabled").getAddress(), Main.INSTANCE.getFeatureManager().getFeature("glow").isEnabled());
            }
        }
    }
}
