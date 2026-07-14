package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public final class YakCarcassProfile extends DefaultMammalCarcassProfile {

    @Override
    public MeatType getMeatType() {
        return MeatType.CHEVON;
    }

    @Override
    public MapColor mapColor() {
        return MapColor.COLOR_BLACK;
    }

    @Override
    public Carcass carcass(){
        return Carcass.YAK;
    }

    @Override public int bloodBucket() { return 3; }


    @Override
    public List<DropSpec> dropsFor(MeatHookStage stage) {
        switch (stage){
        }

        return super.dropsFor(stage);
    }

    @Override
    public List<DropSpec> dropsForSupport(MeatHookStage stage) {
        switch (stage){
            case BISECT -> {
                return List.of(
                        DropSpec.of(new ItemStack(ButchercraftItems.STOMACH.get(), 4)),
                        DropSpec.of(new ItemStack(ButchercraftItems.TRIPE.get(), 8))
                );
            }
            case DISEMBOWEL -> {
                return List.of(
                        DropSpec.of(ButchercraftItems.OXTAIL.get())
                );
            }
        }

        return super.dropsForSupport(stage);
    }


}
