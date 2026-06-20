package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.butchery.ButcherHelpers;
import com.vomiter.survivorsbutchercraft.mixin.MeatHookBlockEntityAccessor;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

public class MeatHookBucketAdapter{

    private final MeatHookBlockEntity meatHook;

    private final int MIN_BUCKET_CAPACITY = 1000;

    public MeatHookBucketAdapter(MeatHookBlockEntity meatHook) {
        this.meatHook = meatHook;
    }

    private boolean shouldReturnItem = false;

    private void setupStage(HookRecipe recipe, int stage){
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            acc.sbtfc$setupStage(recipe, stage);
        }
    }

    private boolean isFinalStage(HookRecipe recipe){
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            return acc.sbtfc$isFinalStage(recipe);
        }
        return false;
    }

    private Optional<HookRecipe> matchRecipe(){
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            return acc.sbtfc$matchRecipe();
        }
        return Optional.empty();
    }

    private void progressRecipe(HookRecipe recipe){
        if (this.isFinalStage(recipe)) {
            meatHook.finishRecipe();
        } else {
            this.setupStage(recipe, meatHook.stage + 1);
        }
    }

    private void handleAfterFluidTransfer(ItemStack old, ItemStack newItem, Player p){
        //only calc at server side
        if(!(p.level() instanceof ServerLevel serverLevel)) return;
        ItemStack itemInQuestion;
        if(newItem.isEmpty()) itemInQuestion = old;
        else itemInQuestion = newItem;
        Optional.ofNullable(Helpers.getCapability(itemInQuestion, Capabilities.FLUID_ITEM)).ifPresent(
                itemFluidAfter -> {
                    //if it's full, drop the bucket
                    if(ButcherHelpers.hasEnoughCapacity(itemFluidAfter, MIN_BUCKET_CAPACITY)){
                        var center = meatHook.getBlockPos().getCenter().add(0, -2, 0);
                        var itemEntity = new ItemEntity(serverLevel, center.x(), center.y(), center.z(), itemInQuestion.split(1), 0, 0, 0);
                        serverLevel.addFreshEntity(itemEntity);
                    }
                    //if it's not full, but it's a split item, add the item back.
                    else if(shouldReturnItem) {
                        p.addItem(itemInQuestion);
                        shouldReturnItem = false;
                    }
                }
        );
    }

    public void acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir){
        matchRecipe().ifPresent(recipe -> {if(recipe.getRecipeToolsIn().get(meatHook.stage).tool.test(Items.BUCKET.getDefaultInstance())){
            if(butcheringTool.getItem() instanceof FluidContainerItem){
                ItemStack containerItem;
                shouldReturnItem = false;
                if(Helpers.getCapability(butcheringTool, Capabilities.FLUID_ITEM) == null){
                    containerItem = butcheringTool.split(1);
                    shouldReturnItem = true;
                }
                else containerItem = butcheringTool;
                var blood = ButcherHelpers.createBloodTank(MIN_BUCKET_CAPACITY);
                Optional.ofNullable(Helpers.getCapability(containerItem, Capabilities.FLUID_ITEM)).ifPresent(
                        itemFluid -> ButcherHelpers.handleFluid(itemFluid, MIN_BUCKET_CAPACITY, p, blood, () -> {
                            ButcherHelpers.transferFluid(p, containerItem, itemFluid, blood, (o, n) -> handleAfterFluidTransfer(o, n, p));
                            ButcherHelpers.applyEffects(p);
                            progressRecipe(recipe);
                            meatHook.updateInventory();
                            cir.setReturnValue(InteractionResult.SUCCESS);
                        }));
            }
        }});

    }

}
