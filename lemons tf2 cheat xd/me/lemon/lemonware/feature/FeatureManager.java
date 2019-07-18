// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.feature;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import me.lemon.lemonware.feature.features.Aimbot;
import me.lemon.lemonware.feature.features.GlowESP;
import me.lemon.lemonware.feature.features.Bunnyhop;
import java.util.ArrayList;
import java.util.List;

public class FeatureManager
{
    private List<Feature> features;
    
    public FeatureManager() {
        (this.features = new ArrayList<Feature>()).addAll(Arrays.asList(new Bunnyhop(), new GlowESP(), new Aimbot()));
    }
    
    public Feature getFeature(final String name) {
        for (final Feature f : this.getFeatures()) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
    
    public List<Feature> getFeatures() {
        return this.features;
    }
}
