// File: com/vomiter/survivorsbutchercraft/data/bbmodel/ModelPartTraverser.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public final class ModelPartTraverser {

    @FunctionalInterface
    public interface CubeVisitor {
        void accept(String path, int cubeIndex, ModelPart.Cube cube, PoseStack.Pose pose);
    }

    public void visit(ModelPart root, CubeVisitor visitor) {
        PoseStack ps = new PoseStack();
        root.visit(ps, (pose, path, cubeIndex, cube) -> visitor.accept(path, cubeIndex, cube, pose));
    }
}