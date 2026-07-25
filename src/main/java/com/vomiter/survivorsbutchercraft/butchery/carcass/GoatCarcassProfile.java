package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import net.minecraft.world.level.material.MapColor;

public final class GoatCarcassProfile extends DefaultMammalCarcassProfile {

    @Override
    public MeatType getMeatType() {
        return MeatType.CHEVON;
    }

    @Override
    public MapColor mapColor() {
        return MapColor.COLOR_LIGHT_GRAY;
    }

    @Override
    public Carcass carcass(){
        return Carcass.GOAT;
    }

}
