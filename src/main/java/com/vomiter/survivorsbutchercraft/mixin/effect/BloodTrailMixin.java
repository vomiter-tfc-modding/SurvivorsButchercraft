package com.vomiter.survivorsbutchercraft.mixin.effect;

import com.lance5057.butchercraft.effects.BloodTrailEffect;
import com.vomiter.survivorsabilities.core.SAAttributes;
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
        UUID BLOOD_SCENT_UUID = UUID.fromString("48bf8551-67a1-4c13-82d2-52578e3f12de");
        self.addAttributeModifier(SAAttributes.BLOOD_SCENT.get(), BLOOD_SCENT_UUID.toString() ,10, AttributeModifier.Operation.ADDITION);
    }
}
