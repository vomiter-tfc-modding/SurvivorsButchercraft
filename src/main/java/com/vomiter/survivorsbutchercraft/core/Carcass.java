package com.vomiter.survivorsbutchercraft.core;

import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;

import java.util.List;
import java.util.Locale;

public enum Carcass implements ICarcassProfile {
    YAK(new YakCarcassProfile());

    private final ICarcassProfile profile;

    Carcass(ICarcassProfile profile) {
        this.profile = profile;
    }

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    // === delegate ===
    @Override public boolean hasHide() { return profile.hasHide(); }
    @Override public MapColor mapColor() { return profile.mapColor(); }
    @Override public List<DropSpec> dropsFor(MeatHookStage stage) {
        return profile.dropsFor(stage);
    }
    @Override public List<DropSpec> dropsForSupport(MeatHookStage stage) {
        return profile.dropsForSupport(stage);
    }
    @Override public List<DropSpec> dropsForTrivial(MeatHookStage stage) {
        return profile.dropsForTrivial(stage);
    }
    @Override
    public Item carcassItem() {
        return profile.carcassItem();
    }
}
