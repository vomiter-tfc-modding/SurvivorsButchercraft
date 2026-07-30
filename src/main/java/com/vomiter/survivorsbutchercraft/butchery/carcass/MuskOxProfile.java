package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public final class MuskOxProfile extends DefaultMammalCarcassProfile {

    @Override
    public MeatType getMeatType() {
        return MeatType.MUTTON;
    }

    @Override
    public MapColor mapColor() {
        return MapColor.COLOR_BROWN;
    }

    @Override
    public Carcass carcass(){
        return Carcass.MUSK_OX;
    }

    @Override public int bloodBucket() { return 3; }


    @Override
    public List<CompoundChanceResult> dropsFor(MeatHookStage stage) {
        switch (stage){
        }

        return super.dropsFor(stage);
    }

    @Override
    public List<CompoundChanceResult> dropsForSupport(MeatHookStage stage) {
        switch (stage){
            case BISECT -> {
                return List.of(
                        new CompoundChanceResult(new ItemStack(ButchercraftItems.STOMACH.get(), 4)),
                        new CompoundChanceResult(new ItemStack(ButchercraftItems.TRIPE.get(), 8))
                );
            }
            case DISEMBOWEL -> {
                return List.of(
                        new CompoundChanceResult(ButchercraftItems.OXTAIL.get())
                );
            }
        }

        return super.dropsForSupport(stage);
    }


}
