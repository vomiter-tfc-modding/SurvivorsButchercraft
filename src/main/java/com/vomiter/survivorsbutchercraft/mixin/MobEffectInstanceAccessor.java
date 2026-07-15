package com.vomiter.survivorsbutchercraft.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {
    @Invoker("tickDownDuration")
    int sb$tickDownDuration();
}
