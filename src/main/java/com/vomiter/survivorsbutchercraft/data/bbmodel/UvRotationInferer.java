package com.vomiter.survivorsbutchercraft.data.bbmodel;

import net.minecraft.core.Direction;

import java.util.List;

public final class UvRotationInferer {

    public FaceUv inferFaceUv(Direction dir, List<CapturingVertexConsumer.Vtx> quad) {
        CornerProjector proj = CornerProjector.forDirection(dir);

        CapturingVertexConsumer.Vtx tl = null, tr = null, br = null, bl = null;

        for (var v : quad) {
            float sx = proj.sx(v);
            float sy = proj.sy(v);

            if (tl == null) { tl = tr = br = bl = v; continue; }

            if (sx < proj.sx(tl) || (approx(sx, proj.sx(tl)) && sy < proj.sy(tl))) tl = v; // TL
            if (sx > proj.sx(br) || (approx(sx, proj.sx(br)) && sy > proj.sy(br))) br = v; // BR
            if (sx > proj.sx(tr) || (approx(sx, proj.sx(tr)) && sy < proj.sy(tr))) tr = v; // TR
            if (sx < proj.sx(bl) || (approx(sx, proj.sx(bl)) && sy > proj.sy(bl))) bl = v; // BL
        }

        if (tl == null || tr == null || br == null || bl == null) {
            return new FaceUv(0, 0, 0, 0, 0);
        }

        // uv bounds（仍然用 bounds 當作候選角）
        float u0 = min4(tl.u, tr.u, br.u, bl.u);
        float u1 = max4(tl.u, tr.u, br.u, bl.u);
        float v0 = min4(tl.v, tr.v, br.v, bl.v);
        float v1 = max4(tl.v, tr.v, br.v, bl.v);

        int[] rots = {0, 90, 180, 270};

        // 16 組枚舉：rotation * flipU * flipV
        for (int rot : rots) {
            for (boolean flipU : new boolean[]{false, true}) {
                for (boolean flipV : new boolean[]{false, true}) {
                    float ua = flipU ? u1 : u0;
                    float ub = flipU ? u0 : u1;
                    float va = flipV ? v1 : v0;
                    float vb = flipV ? v0 : v1;

                    if (matchesRotation(tl, tr, br, bl, ua, va, ub, vb, rot)) {
                        // 注意：ua/va/ub/vb 可能是反向（用來表達鏡像）
                        return new FaceUv(ua, va, ub, vb, rot);
                    }
                }
            }
        }

        // fallback：保守回傳不翻轉、0 度
        return new FaceUv(u0, v0, u1, v1, 0);
    }

    private static boolean matchesRotation(CapturingVertexConsumer.Vtx tl,
                                           CapturingVertexConsumer.Vtx tr,
                                           CapturingVertexConsumer.Vtx br,
                                           CapturingVertexConsumer.Vtx bl,
                                           float ua, float va, float ub, float vb,
                                           int rot) {
        // rotation 0:   TL=(ua,va) TR=(ub,va) BR=(ub,vb) BL=(ua,vb)
        // rotation 90:  TL=(ub,va) TR=(ub,vb) BR=(ua,vb) BL=(ua,va)
        // rotation 180: TL=(ub,vb) TR=(ua,vb) BR=(ua,va) BL=(ub,va)
        // rotation 270: TL=(ua,vb) TR=(ua,va) BR=(ub,va) BL=(ub,vb)
        return switch (rot) {
            case 0 ->  matches(tl, ua, va) && matches(tr, ub, va) && matches(br, ub, vb) && matches(bl, ua, vb);
            case 90 -> matches(tl, ub, va) && matches(tr, ub, vb) && matches(br, ua, vb) && matches(bl, ua, va);
            case 180 -> matches(tl, ub, vb) && matches(tr, ua, vb) && matches(br, ua, va) && matches(bl, ub, va);
            case 270 -> matches(tl, ua, vb) && matches(tr, ua, va) && matches(br, ub, va) && matches(bl, ub, vb);
            default -> false;
        };
    }

    private static boolean matches(CapturingVertexConsumer.Vtx v, float u, float vv) {
        return approx(v.u, u) && approx(v.v, vv);
    }

    private static boolean approx(float a, float b) {
        return Math.abs(a - b) <= 1e-3f;
    }

    private static float min4(float a, float b, float c, float d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    private static float max4(float a, float b, float c, float d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    private static final class CornerProjector {
        interface F { float get(CapturingVertexConsumer.Vtx v); }
        private final F sx, sy;

        private CornerProjector(F sx, F sy) {
            this.sx = sx; this.sy = sy;
        }

        float sx(CapturingVertexConsumer.Vtx v) { return sx.get(v); }
        float sy(CapturingVertexConsumer.Vtx v) { return sy.get(v); }

        static CornerProjector forDirection(Direction dir) {
            // NOTE: +Y is DOWN in entity model coordinates, so "up" in screen space is -y.
            return switch (dir) {
                case NORTH -> new CornerProjector(v -> v.x,  v -> -v.y);
                case SOUTH -> new CornerProjector(v -> -v.x, v -> -v.y);
                case EAST  -> new CornerProjector(v -> v.z,  v -> -v.y);
                case WEST  -> new CornerProjector(v -> -v.z, v -> -v.y);
                case UP    -> new CornerProjector(v -> v.x,  v -> -v.z);
                case DOWN  -> new CornerProjector(v -> v.x,  v ->  v.z);
            };
        }
    }
}