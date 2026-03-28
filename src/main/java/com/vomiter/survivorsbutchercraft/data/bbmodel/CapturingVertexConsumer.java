// File: com/vomiter/survivorsbutchercraft/data/bbmodel/CapturingVertexConsumer.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.Direction;

import java.util.*;

public final class CapturingVertexConsumer implements VertexConsumer {

    public static final class Vtx {
        public final float x, y, z;
        public final float u, v;
        public final float nx, ny, nz;

        public Vtx(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
            this.x = x; this.y = y; this.z = z;
            this.u = u; this.v = v;
            this.nx = nx; this.ny = ny; this.nz = nz;
        }
    }

    private static final float EPS_N = 0.01f;
    private final List<Vtx> vertices = new ArrayList<>(24);

    public List<Vtx> vertices() {
        return vertices;
    }

    public Map<Direction, List<Vtx>> groupByDirection() {
        Map<Direction, List<Vtx>> out = new EnumMap<>(Direction.class);
        for (Vtx v : vertices) {
            Direction d = normalToDirection(v.nx, v.ny, v.nz);
            if (d == null) continue;
            out.computeIfAbsent(d, k -> new ArrayList<>(4)).add(v);
        }
        return out;
    }

    private static Direction normalToDirection(float nx, float ny, float nz) {
        if (approx(nx, 0, EPS_N) && approx(ny, 1, EPS_N) && approx(nz, 0, EPS_N)) return Direction.UP;
        if (approx(nx, 0, EPS_N) && approx(ny,-1, EPS_N) && approx(nz, 0, EPS_N)) return Direction.DOWN;

        if (approx(nx, 0, EPS_N) && approx(ny, 0, EPS_N) && approx(nz,-1, EPS_N)) return Direction.NORTH;
        if (approx(nx, 0, EPS_N) && approx(ny, 0, EPS_N) && approx(nz, 1, EPS_N)) return Direction.SOUTH;

        if (approx(nx, 1, EPS_N) && approx(ny, 0, EPS_N) && approx(nz, 0, EPS_N)) return Direction.EAST;
        if (approx(nx,-1, EPS_N) && approx(ny, 0, EPS_N) && approx(nz, 0, EPS_N)) return Direction.WEST;

        // fallback for non-axis-aligned normals
        float ax = Math.abs(nx), ay = Math.abs(ny), az = Math.abs(nz);
        if (ay >= ax && ay >= az) return ny >= 0 ? Direction.UP : Direction.DOWN;
        if (az >= ax) return nz >= 0 ? Direction.SOUTH : Direction.NORTH;
        return nx >= 0 ? Direction.EAST : Direction.WEST;
    }

    private static boolean approx(float a, float b, float eps) {
        return Math.abs(a - b) <= eps;
    }

    // ---- VertexConsumer required methods ----
    @Override public VertexConsumer vertex(double x, double y, double z) { return this; }
    @Override public VertexConsumer color(int r, int g, int b, int a) { return this; }
    @Override public VertexConsumer uv(float u, float v) { return this; }
    @Override public VertexConsumer overlayCoords(int u, int v) { return this; }
    @Override public VertexConsumer uv2(int u, int v) { return this; }
    @Override public VertexConsumer normal(float x, float y, float z) { return this; }
    @Override public void endVertex() {}
    @Override public void defaultColor(int r, int g, int b, int a) {}
    @Override public void unsetDefaultColor() {}

    @Override
    public void vertex(float x, float y, float z,
                       float r, float g, float b, float a,
                       float u, float v,
                       int overlay, int light,
                       float nx, float ny, float nz) {
        vertices.add(new Vtx(x, y, z, u, v, nx, ny, nz));
    }
}