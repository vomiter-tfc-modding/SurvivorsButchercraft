package com.vomiter.survivorsbutchercraft.mixin.effect;

import com.lance5057.butchercraft.effects.BloodTrailEffect;
import com.vomiter.survivorsabilities.core.SAAttributes;
import com.vomiter.survivorsbutchercraft.Helpers;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BloodTrailEffect.class)
public class BloodTrailMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addAttributes(CallbackInfo ci){
        var self = (BloodTrailEffect)(Object)this;
        self.addAttributeModifier(
                SAAttributes.BLOOD_SCENT,
                Helpers.id("blood_scent"),
                10,
                AttributeModifier.Operation.ADD_VALUE);
    }
}
