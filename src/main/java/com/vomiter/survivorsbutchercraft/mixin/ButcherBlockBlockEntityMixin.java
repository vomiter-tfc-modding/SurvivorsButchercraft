package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.adapter.ButcherBlockBucketAdapter;
import com.vomiter.survivorsbutchercraft.adapter.TFCFoodAdapter;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = ButcherBlockBlockEntity.class, remap = false)
public abstract class ButcherBlockBlockEntityMixin extends BlockEntity implements IButcherBlock {

    public int sbtfcInterface$getStage(){
        return stage;
    }

    public Ingredient sbtfcInterface$getCurTool(){
        return curTool;
    }

    public ItemStack sbtfcInterface$getInserted(){
        return getInsertedItem();
    }


    @Shadow
    public abstract ItemStack getInsertedItem();

    @Shadow
    public int stage;

    @Shadow
    private Ingredient curTool;

    public ButcherBlockBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir) {
        var adapter = new ButcherBlockBucketAdapter((ButcherBlockBlockEntity) (Object) this);
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
        original.call(instance, amount, living, consumer);
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
        originalList.removeIf(stack -> stack.is(SBTags.Items.BUTCHERY_SKIP_LOOT));
        var ideal = Optional.ofNullable(ToolAlternative.getIdealTool(curTool)).orElse(Items.AIR);
        boolean isIdeal = ToolAlternative.getIdealTool(ideal).test(tool);
        ObjectArrayList<ItemStack> newList = ObjectArrayList.of();
        originalList.forEach(originalItemStack -> {
            if(isIdeal){
                newList.add(originalItemStack);
            } else {
                var singleInput = originalItemStack.copyWithCount(1);
                for (int i = 0; i < originalItemStack.getCount(); i++) {
                    if(random.nextFloat() < 3/4f){
                        newList.add(singleInput);
                    }
                }
            }
        });
        return ObjectArrayList.wrap(
                newList.stream().map(item -> TFCFoodAdapter.copyRotten(getInsertedItem(), item))
                        .toArray(ItemStack[]::new)
        );
    }

}
