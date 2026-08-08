package com.vomiter.survivorsbutchercraft.mixin.client;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.client.rendering.RenderUtil;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimationFloatTransform;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.client.HookTransformReloadListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.renderable.IRenderable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderUtil.class, remap = false)
public class RenderUtilMixin {
    @Unique
    private static ResourceLocation sb$id(String namespace, String path){return ResourceLocation.fromNamespaceAndPath(namespace, path);}
    @Unique
    private static ResourceLocation sb$id(String path){return sb$id(SurvivorsButchercraft.MODID, path);}

    @Unique
    private static void sb$applyCenteredScale(PoseStack poseStack, HookTransformReloadListener.TransformDef def) {
        poseStack.translate(0.5f, 0.0f, 0.5f);
        poseStack.scale(def.sx(), def.sy(), def.sz());
        poseStack.translate(-0.5f, 0.0f, -0.5f);
        poseStack.translate(def.tx(), def.ty(), def.tz());
    }

    @Redirect(
            method = "loadModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/client/rendering/RenderUtil;blockModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/neoforged/neoforge/client/model/renderable/IRenderable;Lcom/lance5057/butchercraft/client/rendering/animation/floats/AnimationFloatTransform;F)V"
            )
    )
    private static void redirect_blockModel(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, IRenderable<ModelData> bm, AnimationFloatTransform transform, float timer, @Local(argsOnly = true, name = "arg4") BlacklistedModel model, @Local(argsOnly = true, name = "arg5") float loadModelTimer
    ) {
        HookTransformReloadListener.TransformDef def = HookTransformReloadListener.get(model.rc());
        if (def == null) {
            if(model.rc().getPath().endsWith("_female_parts")) def = HookTransformReloadListener.get(
                    Helpers.id(model.rc().getNamespace(), model.rc().getPath().replace("_female_parts", ""))
            );
            if(model.rc().getPath().endsWith("_male_parts")) def = HookTransformReloadListener.get(
                    Helpers.id(model.rc().getNamespace(), model.rc().getPath().replace("_male_parts", ""))
            );
        }
        if (def != null) {
            poseStack.pushPose();
            sb$applyCenteredScale(poseStack, def);
            RenderUtil.blockModel(poseStack, buffer, packedLight, packedOverlay, bm, transform, timer);
            poseStack.popPose();
        } else {
            RenderUtil.blockModel(poseStack, buffer, packedLight, packedOverlay, bm, transform, timer);
        }
    }

    @Redirect(
            method = "loadModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/client/rendering/RenderUtil;itemModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/world/item/Item;Lcom/lance5057/butchercraft/client/rendering/animation/floats/AnimationFloatTransform;F)V"
            )
    )
    private static void redirect_itemModel(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay,
            Item item,
            AnimationFloatTransform transform,
            float timer,
            @Local(argsOnly = true, name = "arg4") BlacklistedModel model,
            @Local(argsOnly = true, name = "arg5") float loadModelTimer
    ) {
        HookTransformReloadListener.TransformDef def = HookTransformReloadListener.get(model.rc());
        if (def != null) {
            poseStack.pushPose();
            sb$applyCenteredScale(poseStack, def);
            RenderUtil.itemModel(poseStack, buffer, packedLight, packedOverlay, item, transform, timer);
            poseStack.popPose();
        } else {
            RenderUtil.itemModel(poseStack, buffer, packedLight, packedOverlay, item, transform, timer);
        }
    }

}
