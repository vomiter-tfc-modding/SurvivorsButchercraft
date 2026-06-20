package com.vomiter.survivorsbutchercraft.butchery;

import com.lance5057.butchercraft.ButchercraftFluids;
import com.lance5057.butchercraft.ButchercraftMobEffects;
import com.lance5057.butchercraft.armor.ApronItem;
import com.lance5057.butchercraft.armor.BootsItem;
import com.lance5057.butchercraft.armor.GlovesItem;
import com.lance5057.butchercraft.armor.MaskItem;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.BiConsumer;

public class ButcherHelpers {

    public static void handleFluid(IFluidHandlerItem itemFluid, int mB ,Player p, FluidTank blood, Runnable execute){
        if(ButcherHelpers.hasEnoughCapacity(itemFluid, mB)){
            p.displayClientMessage(Component.literal("capacity not enough"), true);
            //display some word
        } else if(!itemFluid.isFluidValid(0, blood.getFluid())) {
            p.displayClientMessage(Component.literal("not valid item"), true);
            //display some word
        } else {
            execute.run();
        }

    }

    public static void transferFluid(Player p, ItemStack butcheringTool, IFluidHandlerItem itemFluid, FluidTank blood, BiConsumer<ItemStack, ItemStack> handlerAfter){
        FluidHelpers.transferBetweenItemAndOther(
                butcheringTool,
                itemFluid,
                blood,
                itemFluid,
                FluidHelpers.Transfer.FILL,
                p.level(),
                p.blockPosition(),
                handlerAfter::accept);
    }


    public static boolean hasEnoughCapacity(IFluidHandlerItem item, int mB){
        return item.getTankCapacity(0) - item.getFluidInTank(0).getAmount() < mB;
    }

    public static FluidTank createBloodTank(int mB){
        var blood = new FluidTank(mB);
        blood.fill(new FluidStack(ButchercraftFluids.BLOOD_FLUID.get(), mB), IFluidHandler.FluidAction.EXECUTE);
        return blood;
    }

    public static void applyEffects(Player p){
        ItemStack boots = p.getInventory().getArmor(0);
        if (boots.getItem() instanceof BootsItem) {
            boots.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.FEET));
        } else {
            p.addEffect(new MobEffectInstance(ButchercraftMobEffects.BLOODTRAIL.get(), 3600, 0, false, false, true));
        }

        ItemStack apron = p.getInventory().getArmor(1);
        if (apron.getItem() instanceof ApronItem) {
            apron.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.LEGS));
        } else {
            p.addEffect(new MobEffectInstance(ButchercraftMobEffects.BLOODY.get(), 3600, 0, false, false, true));
        }

        ItemStack gloves = p.getInventory().getArmor(2);
        if (gloves.getItem() instanceof GlovesItem) {
            gloves.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.CHEST));
        } else {
            p.addEffect(new MobEffectInstance(ButchercraftMobEffects.DIRTY.get(), 3600, 0, false, false, true));
        }

        ItemStack mask = p.getInventory().getArmor(3);
        if (mask.getItem() instanceof MaskItem) {
            mask.hurtAndBreak(1, p, (x) -> x.broadcastBreakEvent(EquipmentSlot.HEAD));
        } else {
            p.addEffect(new MobEffectInstance(ButchercraftMobEffects.STINKY.get(), 3600, 0, false, false, true));
        }
    }


}
