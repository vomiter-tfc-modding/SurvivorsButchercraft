package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.vomiter.survivorsbutchercraft.butchery.ButcherHelpers;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.util.Helpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractButcherBucketAdapter<R extends Recipe<?>> {

    private final BlockEntity butcherBlock;

    private final int MIN_BUCKET_CAPACITY = 1000;

    abstract int getStage();
    abstract void setStage(int i);
    abstract void finishRecipe();
    abstract void updateInventory();
    abstract void dropLoot(AnimatedRecipeItemUse tool, Player player);

    public AbstractButcherBucketAdapter(BlockEntity butcherBlock) {
        this.butcherBlock = butcherBlock;
    }

    private boolean shouldReturnItem = false;

    boolean isShouldReturnItem() {
        return shouldReturnItem;
    }

    void setShouldReturnItem(boolean shouldReturnItem) {
        this.shouldReturnItem = shouldReturnItem;
    }

    abstract void setupStage(R recipe, int stage);

    abstract boolean isFinalStage(R recipe);

    abstract Optional<R> matchRecipe();

    void progressRecipe(R recipe, Player player){
        if (this.isFinalStage(recipe)) {
            dropLoot(getTool(recipe, getStage()), player);
            finishRecipe();
        } else {
            dropLoot(getTool(recipe, getStage()), player);
            setStage(getStage() + 1);
        }
    }

    abstract AnimatedRecipeItemUse getTool(R recipe, int stage);

    void handleAfterFluidTransfer(ItemStack old, ItemStack newItem, Player p){
        //only calc at server side
        if(!(p.level() instanceof ServerLevel serverLevel)) return;
        ItemStack itemInQuestion;
        if(newItem.isEmpty()) itemInQuestion = old;
        else itemInQuestion = newItem;
        Optional.ofNullable(Helpers.getCapability(itemInQuestion, Capabilities.FLUID_ITEM)).ifPresent(
                itemFluidAfter -> {
                    //if it's full, drop the bucket
                    if(ButcherHelpers.hasEnoughCapacity(itemFluidAfter, MIN_BUCKET_CAPACITY)){
                        var center = butcherBlock.getBlockPos().getCenter().add(0, -2, 0);
                        var itemEntity = new ItemEntity(serverLevel, center.x(), center.y(), center.z(), itemInQuestion.split(1), 0, 0, 0);
                        serverLevel.addFreshEntity(itemEntity);
                    }
                    //if it's not full, but it's a split item, add the item back.
                    else if(isShouldReturnItem()) {
                        p.addItem(itemInQuestion);
                        setShouldReturnItem(false);
                    }
                }
        );
    }

    void handleEmptyBucket(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir, R recipe){
        if(butcheringTool.getItem() instanceof FluidContainerItem){
            ItemStack containerItem;
            setShouldReturnItem(false);
            if(Helpers.getCapability(butcheringTool, Capabilities.FLUID_ITEM) == null){
                containerItem = butcheringTool.split(1);
                setShouldReturnItem(true);
            }
            else containerItem = butcheringTool;
            var blood = ButcherHelpers.createBloodTank(MIN_BUCKET_CAPACITY);
            Optional.ofNullable(Helpers.getCapability(containerItem, Capabilities.FLUID_ITEM)).ifPresent(
                    itemFluid -> ButcherHelpers.handleFluid(itemFluid, MIN_BUCKET_CAPACITY, p, blood, () -> {
                        ButcherHelpers.drainBlood(p, containerItem, itemFluid, blood, (o, n) -> handleAfterFluidTransfer(o, n, p));
                        ButcherHelpers.applyEffects(p);
                        progressRecipe(recipe, p);
                        updateInventory();
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }));
        }
    }

    void handleFluidBucket(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir, R recipe){
        if(butcheringTool.getItem() instanceof FluidContainerItem){
            ItemStack containerItem;
            setShouldReturnItem(false);
            if(Helpers.getCapability(butcheringTool, Capabilities.FLUID_ITEM) == null){
                containerItem = butcheringTool.split(1);
                setShouldReturnItem(true);
            }
            else containerItem = butcheringTool;
            var emptyTank = new FluidTank(MIN_BUCKET_CAPACITY);
            Optional.ofNullable(Helpers.getCapability(containerItem, Capabilities.FLUID_ITEM)).ifPresent(
                    itemFluid -> {
                        itemFluid.drain(MIN_BUCKET_CAPACITY, IFluidHandler.FluidAction.EXECUTE);
                        ButcherHelpers.applyFluid(p, containerItem, itemFluid, emptyTank, (o, n) -> handleAfterFluidTransfer(o, n, p));
                        ButcherHelpers.applyEffects(p);
                        progressRecipe(recipe, p);
                        updateInventory();
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }
            );
        }
    }

    public void acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir){
        matchRecipe().ifPresent(recipe -> {
            var tool = getTool(recipe, getStage()).tool;
            var delegateTool = tool.getItems()[0];
            var delegateFluidHandler = delegateTool.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            var butcheringToolFluidHandler = butcheringTool.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if(tool.test(Items.BUCKET.getDefaultInstance())){
                handleEmptyBucket(p, butcheringTool, cir, recipe);
            }
            else if(
                    delegateFluidHandler.isPresent()
                    && butcheringToolFluidHandler.isPresent()
            ){
                if(delegateFluidHandler.resolve().get().getFluidInTank(0).getFluid()
                        .isSame(butcheringToolFluidHandler.resolve().get().getFluidInTank(0).getFluid())
                        && butcheringToolFluidHandler.resolve().get().getFluidInTank(0).getAmount() >= MIN_BUCKET_CAPACITY
                ){
                    handleFluidBucket(p, butcheringTool, cir, recipe);
                }
            }
        });

    }

}
