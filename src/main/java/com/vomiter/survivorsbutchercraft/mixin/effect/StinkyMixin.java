package com.vomiter.survivorsbutchercraft.mixin.effect;

import com.lance5057.butchercraft.effects.PungentReekEffect;
import com.vomiter.survivorsabilities.core.SAAttributes;
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
        UUID ATTRIBUTE_UUID = UUID.fromString("49063323-a56b-4798-9bab-1cf3af2b3236");
        self.addAttributeModifier(SAAttributes.ANIMAL_TRUST.get(), ATTRIBUTE_UUID.toString() ,-15, AttributeModifier.Operation.ADDITION);
    }
}
