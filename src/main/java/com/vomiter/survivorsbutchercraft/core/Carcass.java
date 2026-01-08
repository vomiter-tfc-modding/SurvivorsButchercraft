package com.vomiter.survivorsbutchercraft.core;

import net.minecraft.world.level.material.MapColor;

import java.util.Locale;

public enum Carcass {
    YAK(true, MapColor.COLOR_BLACK);

    public final boolean hasHide;
    public final MapColor mapColor;
    Carcass(){
        this.hasHide = false;
        mapColor = null;
    }

    Carcass(boolean hasHide, MapColor mapColor){
        this.hasHide = hasHide;
        this.mapColor = mapColor;
    };

    public String serializedName(){
        return name().toLowerCase(Locale.ROOT);
    }
}
