package com.vomiter.survivorsbutchercraft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.config.TFCConfig;
import net.minecraftforge.client.model.renderable.BakedModelRenderable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = BakedModelRenderable.class, remap = false)
public class BakedModelRenderableMixin {
    private static Vector4f rgbToTint(int color) {
        return new Vector4f(
                ((color >> 16) & 0xFF) / 255.0f,
                ((color >> 8) & 0xFF) / 255.0f,
                (color & 0xFF) / 255.0f,
                1.0f
        );
    }

    @WrapOperation(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;" +
                    "Lnet/minecraft/client/renderer/MultiBufferSource;" +
                    "Lnet/minecraftforge/client/model/renderable/ITextureRenderTypeLookup;" +
                    "IIFLnet/minecraftforge/client/model/renderable/BakedModelRenderable$Context;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/renderable/BakedModelRenderable$Context;tint()Lorg/joml/Vector4f;")
    )
    private Vector4f sbtfc$rottenTint(BakedModelRenderable.Context instance, Operation<Vector4f> original){
        var carcass = ThreadLocalFlags.carcassRendering.get();
        if (!carcass.isEmpty()){
            if (Optional.ofNullable(FoodCapability.get(carcass)).map(IFood::isRotten).orElse(false)){
                return rgbToTint(TFCConfig.CLIENT.foodExpiryOverlayColor.get());
            }
        }
        return original.call(instance);
    }
}
