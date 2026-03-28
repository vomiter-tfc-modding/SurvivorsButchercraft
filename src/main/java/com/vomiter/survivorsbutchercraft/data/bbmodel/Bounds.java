// File: com/vomiter/survivorsbutchercraft/data/bbmodel/Bounds.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

public final class Bounds {
    public final Vec3 min, max;

    public Bounds(Vec3 min, Vec3 max) {
        this.min = min; this.max = max;
    }

    @Override
    public String toString() {
        return "Bounds{min=" + min + ", max=" + max + "}";
    }
}