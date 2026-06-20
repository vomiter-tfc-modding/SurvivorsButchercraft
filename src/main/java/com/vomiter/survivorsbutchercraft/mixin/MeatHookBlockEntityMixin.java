package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.adapter.MeatHookBucketAdapter;
import com.vomiter.survivorsbutchercraft.butchery.convert.ConvertResultManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public abstract class MeatHookBlockEntityMixin extends BlockEntity {
    public MeatHookBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir) {
        var adapter = new MeatHookBucketAdapter((MeatHookBlockEntity) (Object) this);
        adapter.acceptFluidHandler(p, butcheringTool, cir);
    }

    @WrapOperation(
            method = "butcher",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;" +
                            "hurtAndBreak(" +
                            "ILnet/minecraft/world/entity/LivingEntity;" +
                            "Ljava/util/function/Consumer;" +
                            ")V",
                    remap = true)
    )
    private void sbtfc$addExtraDamage(ItemStack instance, int amount, LivingEntity living, Consumer<LivingEntity> consumer, Operation<Void> original){
        int extra = ConvertResultManager.INSTANCE.getExtraDamage(instance);
        original.call(instance, extra + amount, living, consumer);
    }

    @WrapOperation(
            method = "dropLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;" +
                            "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)" +
                            "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
                    remap = true
            )
    )
    private ObjectArrayList<ItemStack> sbtfc$convertLoot(LootTable instance, LootParams params, Operation<ObjectArrayList<ItemStack>> original){
        if(getLevel() == null) return original.call(instance, params);
        var tool = Optional.ofNullable(params.getParamOrNull(LootContextParams.TOOL)).orElse(ItemStack.EMPTY);
        var random = getLevel().random;
        var originalList = original.call(instance, params);
        ObjectArrayList<ItemStack> newList = ObjectArrayList.of();
        originalList.forEach(originalItemStack -> {
            var singleInput = originalItemStack.copyWithCount(1);
            var matched = ConvertResultManager.INSTANCE.findMatchingResults(tool, singleInput);
            if(matched.isEmpty()){
                newList.add(originalItemStack.copy());
                return;
            }
            for (int i = 0; i < originalItemStack.getCount(); i++) {
                ConvertResultManager.INSTANCE.rollFrom(matched, random).ifPresent(convert -> {
                    if(convert.keep()){
                        Helpers.addOrMerge(newList, singleInput.copy());
                    } else {
                        Helpers.addOrMerge(newList, convert.to().copy());
                    }
                });
            }
        });
        return newList;
    }

}
