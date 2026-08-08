package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ButcherKnifeItem.class)
public class ButcherKnifeItemMixin {
    @WrapOperation(method = "interactLivingEntity",
            at = @At(value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/items/ButcherKnifeItem;killAndDrop(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Mob;)V",
                    remap = false
            ))
    private void sbtfc$redirectKillDrop(ButcherKnifeItem instance, Player player, ResourceKey<LootTable> lootTableLocation, Mob mob, Operation<Void> original){
        var mobId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
        if(mobId.getNamespace().equals("minecraft")){
            original.call(instance, player, lootTableLocation, mob);
            return;
        }
        ResourceLocation redirected = Helpers.id("butchercraft", "butcher_knife/" + mobId.getNamespace() + "/" + mobId.getPath());
        LootTable lootTable = player.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, redirected));
        if(lootTable != LootTable.EMPTY){
            try{
                ThreadLocalFlags.dropCarcass.set(true);
                original.call(instance, player, ResourceKey.create(Registries.LOOT_TABLE, redirected), mob);
            } finally {
                ThreadLocalFlags.dropCarcass.remove();
            }
        }
    }

    @WrapOperation(method = "interactLivingEntity",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;",
                    remap = true
            ))
    private LootTable sbtfc$redirectLoottable(
            ReloadableServerRegistries.Holder instance, ResourceKey<LootTable> lootTableKey, Operation<LootTable> original, @Local(argsOnly = true) LivingEntity entity
    )
    {
        var mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if(mobId.getNamespace().equals("minecraft")){
            return original.call(instance, lootTableKey);
        }
        ResourceLocation redirected = Helpers.id("butchercraft", "butcher_knife/" + mobId.getNamespace() + "/" + mobId.getPath());
        return instance.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, redirected));
    }

}
