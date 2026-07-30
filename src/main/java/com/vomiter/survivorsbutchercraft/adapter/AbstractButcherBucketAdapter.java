package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.ButcherHelpers;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import com.vomiter.survivorsbutchercraft.common.recipe.IButcherRecipe;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private boolean shouldDropOriginalLoot = true;


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
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
            finishRecipe();
        } else {
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
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

    boolean handleEmptyBucket(Player p, ItemStack butcheringTool, FluidStack fluidStack, R recipe){
        AtomicBoolean shouldTackOver = new AtomicBoolean(false);
        try{
            shouldDropOriginalLoot = false;

            ItemStack containerItem;
            setShouldReturnItem(false);
            if(butcheringTool.is(Items.BUCKET)){
                var resultBucket = fluidStack.getFluid().getBucket();
                if(resultBucket != null && !resultBucket.getDefaultInstance().isEmpty()){
                    p.addItem(resultBucket.getDefaultInstance());
                    butcheringTool.shrink(1);
                    shouldTackOver.set(true);
                }
                return shouldTackOver.get();
            }
            else if(!butcheringTool.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() && butcheringTool.copyWithCount(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()){
                containerItem = butcheringTool.split(1);
                setShouldReturnItem(true);
            }
            else containerItem = butcheringTool;
            containerItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(
                    itemFluid -> ButcherHelpers.handleFluid(itemFluid, fluidStack.getAmount(), p, fluidStack, () -> {
                        itemFluid.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        ButcherHelpers.applyEffects(p);
                        updateInventory();
                        if(shouldReturnItem){
                            p.addItem(itemFluid.getContainer());
                        }
                        shouldTackOver.set(true);
                    }));
            return shouldTackOver.get();
        } finally {
            shouldDropOriginalLoot = true;
        }
    }

    public void acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir){
        matchRecipe().ifPresent(recipe -> {
            if (recipe instanceof IButcherRecipe butcherRecipe){
                var tool = butcherRecipe.getButcheringTool(getStage());
                var delegate = Arrays.stream(tool.getItems()).filter(item -> item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent())
                        .findFirst()
                        .orElse(ItemStack.EMPTY);
                boolean shouldTakeOver = false;
                if(!delegate.isEmpty()){
                    var delegateFluid = delegate.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                            .map(fluid -> fluid.getFluidInTank(0))
                            .orElse(FluidStack.EMPTY);
                    if(butcheringTool.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                            .map(fluid -> fluid.getFluidInTank(0).isFluidEqual(delegateFluid)
                                    && fluid.getFluidInTank(0).getAmount() >= delegateFluid.getAmount())
                            .orElse(false)
                    ){
                        butcheringTool.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                                .ifPresent(fluid0 -> fluid0.drain(delegateFluid, IFluidHandler.FluidAction.EXECUTE));
                        shouldTakeOver = true;
                    }
                }

                var results = butcherRecipe.getResults(getStage());
                var fluidResult = results.stream().filter(CompoundChanceResult::hasFluid)
                        .findFirst()
                        .orElse(null);
                if(fluidResult != null){
                    var fluidStack = fluidResult.getFluid();
                    if(handleEmptyBucket(p, butcheringTool, fluidStack, recipe)){
                        shouldTakeOver = true;
                    };
                }

                if(shouldTakeOver){
                    progressRecipe(recipe, p);
                    updateInventory();
                    if(butcherBlock instanceof IButcherBlock b){
                        b.sbtfcInterface$dropLoot(butcherRecipe.getButcheringToolStage(getStage()), p);
                    }
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        });

    }

}
