package com.vomiter.survivorsbutchercraft.data.bbmodel;

public final class Vec3 {
    public final float x, y, z;

    public Vec3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vec3 add(Vec3 o) {
        return new Vec3(x + o.x, y + o.y, z + o.z);
    }

    @Override
    public String toString() {
        return "Vec3{" + x + "," + y + "," + z + "}";
    }
}