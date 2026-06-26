package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public final class PigCarcassProfile extends DefaultMammalCarcassProfile {

    @Override
    public MeatType getMeatType() {
        return MeatType.PORK;
    }

    @Override
    public MapColor mapColor() {
        return MapColor.COLOR_PINK;
    }

    @Override
    public Carcass carcass(){
        return Carcass.PIG;
    }

}
