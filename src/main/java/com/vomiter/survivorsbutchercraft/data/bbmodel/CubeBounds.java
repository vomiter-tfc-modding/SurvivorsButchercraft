package com.vomiter.survivorsbutchercraft.data.bbmodel;

import net.minecraft.client.model.geom.ModelPart;
import org.joml.Matrix4f;

public final class CubeBounds {

    public Bounds computeBounds(ModelPart.Cube cube) {
        // 1.20.1: Cube has public min/max fields in model units
        return new Bounds(
                new Vec3(cube.minX, cube.minY, cube.minZ),
                new Vec3(cube.maxX, cube.maxY, cube.maxZ)
        );
    }

    public Vec3 extractTranslationModelUnits(Matrix4f pose) {
        // ModelPart.translateAndRotate uses translate(x/16,y/16,z/16)
        float tx = pose.m30() * 16.0f;
        float ty = pose.m31() * 16.0f;
        float tz = pose.m32() * 16.0f;
        return new Vec3(tx, ty, tz);
    }
}