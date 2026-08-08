package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.armor.BCArmorMaterial;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ButchercraftItems.class)
public class BCMaterialMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/registries/DeferredRegister$Items;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/neoforged/neoforge/registries/DeferredItem;"))
    private static DeferredItem<? extends Item> sb$buffArmor(DeferredRegister.Items instance, String name, Supplier<? extends Item> sup, Operation<DeferredItem<? extends Item>> original){
        return original.call(instance, name, sup);
    }
}
