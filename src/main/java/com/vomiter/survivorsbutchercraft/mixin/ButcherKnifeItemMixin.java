package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.Helpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ButcherKnifeItem.class)
public class ButcherKnifeItemMixin {
    @WrapOperation(method = "interactLivingEntity", at = @At(value = "INVOKE", target = "Lcom/lance5057/butchercraft/items/ButcherKnifeItem;killAndDrop(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/entity/Mob;)V"))
    private void sbtfc$redirectLoottable(ButcherKnifeItem instance, Player player, ResourceLocation lootTableLocation, Mob mob, Operation<Void> original){
        var mobId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
        if(mobId.getNamespace().equals("minecraft")){
            original.call(instance, player, lootTableLocation, mob);
            return;
        }
        ResourceLocation redirected = Helpers.id("butchercraft", "butcher_knife/" + mobId.getNamespace() + "/" + mobId.getPath());
        LootTable lootTable = player.getServer().getLootData().getLootTable(redirected);
        if(lootTable != LootTable.EMPTY){
            original.call(instance, player, redirected, mob);
        }
    }
}
