package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.ButchercraftFluids;
import com.lance5057.butchercraft.ButchercraftMobEffects;
import com.lance5057.butchercraft.armor.ApronItem;
import com.lance5057.butchercraft.armor.BootsItem;
import com.lance5057.butchercraft.armor.GlovesItem;
import com.lance5057.butchercraft.armor.MaskItem;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.mixin.MeatHookBlockEntityAccessor;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
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

    private void applyEffects(Player p){
        ItemStack boots = p.getInventory().getArmor(0);
        if (boots.getItem() instanceof BootsItem) {
            boots.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.FEET));
        } else {
            p.addEffect(new MobEffectInstance((MobEffect) ButchercraftMobEffects.BLOODTRAIL.get(), 3600, 0, false, false, true));
        }

        ItemStack apron = p.getInventory().getArmor(1);
        if (apron.getItem() instanceof ApronItem) {
            apron.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.LEGS));
        } else {
            p.addEffect(new MobEffectInstance((MobEffect)ButchercraftMobEffects.BLOODY.get(), 3600, 0, false, false, true));
        }

        ItemStack gloves = p.getInventory().getArmor(2);
        if (gloves.getItem() instanceof GlovesItem) {
            gloves.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.CHEST));
        } else {
            p.addEffect(new MobEffectInstance((MobEffect)ButchercraftMobEffects.DIRTY.get(), 3600, 0, false, false, true));
        }

        ItemStack mask = p.getInventory().getArmor(3);
        if (mask.getItem() instanceof MaskItem) {
            mask.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.HEAD));
        } else {
            p.addEffect(new MobEffectInstance((MobEffect)ButchercraftMobEffects.STINKY.get(), 3600, 0, false, false, true));
        }
    }

    private void progressRecipe(HookRecipe recipe){
        if (this.isFinalStage(recipe)) {
            meatHook.finishRecipe();
        } else {
            this.setupStage(recipe, meatHook.stage + 1);
        }
    }

    private void handleAfter(ItemStack old, ItemStack newItem, Player p){
        //only calc at server side
        if(!(meatHook.getLevel() instanceof ServerLevel serverLevel)) return;
        ItemStack itemInQuestion;
        if(newItem.isEmpty()) itemInQuestion = old;
        else itemInQuestion = newItem;
        Optional.ofNullable(Helpers.getCapability(itemInQuestion, Capabilities.FLUID_ITEM)).ifPresent(
                itemFluidAfter -> {
                    //if it's full, drop the bucket
                    if(itemFluidAfter.getTankCapacity(0) - itemFluidAfter.getFluidInTank(0).getAmount() < MIN_BUCKET_CAPACITY){
                        var center = meatHook.getBlockPos().getCenter().add(0, -2, 0);
                        var itemEntity = new ItemEntity(serverLevel, center.x(), center.y(), center.z(), itemInQuestion.split(1));
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

    private void transferFluid(Player p, ItemStack butcheringTool, IFluidHandlerItem itemFluid, FluidTank blood){
        FluidHelpers.transferBetweenItemAndOther(
                butcheringTool,
                itemFluid,
                blood,
                itemFluid,
                FluidHelpers.Transfer.FILL,
                p.level(),
                p.blockPosition(),
                (o, n) -> handleAfter(o, n, p));
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
                Optional.ofNullable(Helpers.getCapability(containerItem, Capabilities.FLUID_ITEM)).ifPresent(
                        itemFluid -> {
                            var blood = new FluidTank(1000);
                            blood.fill(new FluidStack(ButchercraftFluids.BLOOD_FLUID.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                            if(itemFluid.getTankCapacity(0) - itemFluid.getFluidInTank(0).getAmount() < MIN_BUCKET_CAPACITY){
                                p.displayClientMessage(Component.literal("capacity not enough"), true);
                                //display some word
                            } else if(!itemFluid.isFluidValid(0, blood.getFluid())) {
                                p.displayClientMessage(Component.literal("not valid item"), true);
                                //display some word
                            } else {
                                transferFluid(p, containerItem, itemFluid, blood);
                                progressRecipe(recipe);
                                applyEffects(p);
                                meatHook.updateInventory();
                                cir.setReturnValue(InteractionResult.SUCCESS);
                            }
                        });
            }
        }});

    }

}
