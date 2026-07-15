package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.armor.BCArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BCArmorMaterial.class)
public class BCMaterialMixin {
    @Shadow
    @Final
    public static BCArmorMaterial WOOL;

    @Inject(method = "getDurabilityForType", at = @At("RETURN"), cancellable = true)
    private void sb$buffArmor(ArmorItem.Type type, CallbackInfoReturnable<Integer> cir){
        var self = (BCArmorMaterial)(Object)this;
        if(self.equals(WOOL)) cir.setReturnValue(cir.getReturnValue() * 8);
    }
}
