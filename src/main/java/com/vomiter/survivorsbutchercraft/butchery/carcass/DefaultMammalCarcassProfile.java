package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public abstract class DefaultMammalCarcassProfile implements ICarcassProfile {

    public Carcass carcass(){
        return null;
    }

    public Item iconicToolFor(MeatHookStage stage){
        return switch (stage){
            case SKIN -> ButchercraftItems.SKINNING_KNIFE.get();
            case DISEMBOWEL -> ButchercraftItems.BONE_SAW.get();
            case BISECT -> ButchercraftItems.GUT_KNIFE.get();
            case BUTCHER -> ButchercraftItems.BUTCHER_KNIFE.get();
            default -> Items.BARRIER;
        };
    }


    @Override
    public boolean hasHide() {
        return true;
    }

    @Override
    public List<DropSpec> dropsFor(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(DropSpec.of(new ItemStack(SBItems.HIDES.get(carcass()).get())));
            case DISEMBOWEL -> List.of(
                    DropSpec.of(new ItemStack(SBItems.HEADS.get(carcass()).get()))
            );
            case BISECT -> List.of(
                    DropSpec.of(new ItemStack(ButchercraftItems.HEART.get())),
                    DropSpec.of(new ItemStack(ButchercraftItems.LIVER.get())),
                    DropSpec.of(new ItemStack(ButchercraftItems.KIDNEY.get(), 2))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<DropSpec> dropsForSupport(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            default ->
                    List.of();
        };
    }

}
