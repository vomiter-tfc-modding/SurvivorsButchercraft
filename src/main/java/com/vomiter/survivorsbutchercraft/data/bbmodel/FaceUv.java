package com.vomiter.survivorsbutchercraft.data.bbmodel;

public final class FaceUv {
    public final float u1, v1, u2, v2; // normalized 0..1
    public final int rotation;         // 0/90/180/270

    public FaceUv(float u1, float v1, float u2, float v2, int rotation) {
        this.u1 = u1; this.v1 = v1; this.u2 = u2; this.v2 = v2;
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "FaceUv{[" + u1 + "," + v1 + "]-[" + u2 + "," + v2 + "], rot=" + rotation + "}";
    }
}