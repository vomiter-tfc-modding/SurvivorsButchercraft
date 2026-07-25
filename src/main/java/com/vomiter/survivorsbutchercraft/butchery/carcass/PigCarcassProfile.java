package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import net.minecraft.world.level.material.MapColor;

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
