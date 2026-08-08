package com.vomiter.survivorsbutchercraft.mixin.effect;

import com.lance5057.butchercraft.effects.PungentReekEffect;
import com.vomiter.survivorsabilities.core.SAAttributes;
import com.vomiter.survivorsbutchercraft.Helpers;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PungentReekEffect.class)
public class StinkyMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addAttributes(CallbackInfo ci){
        var self = (PungentReekEffect)(Object)this;
        self.addAttributeModifier(SAAttributes.ANIMAL_TRUST, Helpers.id("stinky") ,-15, AttributeModifier.Operation.ADD_VALUE);
    }
}
