package com.vomiter.survivorsbutchercraft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.util.loot.AnimalYieldProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(AnimalYieldProvider.class)
public class AnimalYieldProviderMixin {
    @WrapOperation(
            method = "getFloat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D")
    )
    private double sbtfc$getAttack(Player instance, Attribute attribute, Operation<Double> original){
        if(ThreadLocalFlags.dropLootForButchering.get() && !ThreadLocalFlags.carcass.get().isEmpty()) {
            return 100;
        }
        return original.call(instance, attribute);
    }

    @WrapOperation(
            method = "getFloat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootContext;" +
                            "getParamOrNull" +
                            "(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;)" +
                            "Ljava/lang/Object;",
                    ordinal = 0))
    private Object sbtfc$getAnimal(LootContext instance, LootContextParam<Object> param, Operation<Object> original){
        if(ThreadLocalFlags.dropLootForButchering.get() && !ThreadLocalFlags.carcass.get().isEmpty()){
            var carcass = ThreadLocalFlags.carcass.get();
            AtomicReference<LivingEntity> result = new AtomicReference<>();
            Optional.ofNullable(CarcassDataHelper.getId(carcass))
                    .flatMap(id -> Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.getValue(id)))
                    .ifPresent(type -> {
                        var entity = type.create(instance.getLevel());
                        if(entity instanceof TFCAnimalProperties props){
                            props.setGeneticSize(CarcassDataHelper.getGeneticSize(carcass));
                            props.setFamiliarity(CarcassDataHelper.getFamiliarity(carcass));
                            result.set(props.getEntity());
                        }
            });
            if(result.get() != null) return result.get();
        }
        return original.call(instance, param);
    }
}
