package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.survivorsbutchercraft.Helpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ButcherKnifeItem.class)
public class ButcherKnifeItemMixin {
    @WrapOperation(method = "interactLivingEntity",
            at = @At(value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/items/ButcherKnifeItem;" +
                            "killAndDrop(Lnet/minecraft/world/entity/player/Player;" +
                            "Lnet/minecraft/resources/ResourceLocation;" +
                            "Lnet/minecraft/world/entity/Mob;)V",
                    remap = false
            ))
    private void sbtfc$redirectKillDrop(ButcherKnifeItem instance, Player player, ResourceLocation lootTableLocation, Mob mob, Operation<Void> original){
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

    @WrapOperation(method = "interactLivingEntity",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootDataManager;" +
                            "getLootTable(Lnet/minecraft/resources/ResourceLocation;)" +
                            "Lnet/minecraft/world/level/storage/loot/LootTable;",
                    remap = true
            ))
    private LootTable sbtfc$redirectLoottable(
            LootDataManager instance,
            ResourceLocation resourceLocation,
            Operation<LootTable> original,
            @Local(argsOnly = true) LivingEntity entity
    )
    {
        var mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if(mobId.getNamespace().equals("minecraft")){
            return original.call(instance, resourceLocation);
        }
        ResourceLocation redirected = Helpers.id("butchercraft", "butcher_knife/" + mobId.getNamespace() + "/" + mobId.getPath());
        return instance.getLootTable(redirected);
    }

}
