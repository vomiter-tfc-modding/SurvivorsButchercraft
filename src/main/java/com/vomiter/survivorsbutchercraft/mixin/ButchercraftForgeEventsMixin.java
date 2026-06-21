package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.ButchercraftForgeEvents;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ButchercraftForgeEvents.class, remap = false)
public class ButchercraftForgeEventsMixin {
    @Inject(method = "giveHoodsToUndead", at = @At("HEAD"), cancellable = true)
    private static void sbtfc$cancelGiveHood(MobSpawnEvent.FinalizeSpawn event, CallbackInfo ci){
        ci.cancel();
    }
}
