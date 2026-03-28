// File: .../CubeUvExtractor.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class CubeUvExtractor {
    private final UvRotationInferer rotationInferer = new UvRotationInferer();

    // 取一個「公開可取得」的 identity Pose（不需要 new PoseStack.Pose）
    private static final PoseStack.Pose IDENTITY_POSE = new PoseStack().last();

    public Map<Direction, FaceUv> captureFaces(ModelPart.Cube cube, PoseStack.Pose ignoredPose, int texW, int texH) {
        CapturingVertexConsumer cap = new CapturingVertexConsumer();

        // 用 identity pose compile，避免骨架旋轉讓 normal 不再軸對齊
        cube.compile(IDENTITY_POSE, cap, 0, 0, 1, 1, 1, 1);

        Map<Direction, List<CapturingVertexConsumer.Vtx>> byDir = cap.groupByDirection();

        Map<Direction, FaceUv> out = new EnumMap<>(Direction.class);
        for (var e : byDir.entrySet()) {
            Direction dir = e.getKey();
            List<CapturingVertexConsumer.Vtx> verts = e.getValue();
            if (verts.size() < 4) continue;

            List<CapturingVertexConsumer.Vtx> quad = verts.subList(0, 4);

            float minU = Float.POSITIVE_INFINITY, minV = Float.POSITIVE_INFINITY;
            float maxU = Float.NEGATIVE_INFINITY, maxV = Float.NEGATIVE_INFINITY;
            for (var v : quad) {
                minU = Math.min(minU, v.u);
                minV = Math.min(minV, v.v);
                maxU = Math.max(maxU, v.u);
                maxV = Math.max(maxV, v.v);
            }

            FaceUv face = rotationInferer.inferFaceUv(dir, quad);
            out.put(dir, face);
        }

        return out;
    }
}