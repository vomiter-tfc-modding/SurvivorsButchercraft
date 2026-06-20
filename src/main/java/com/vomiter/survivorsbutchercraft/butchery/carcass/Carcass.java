package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
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

    @Override public Item carcassItem() { return profile.carcassItem(); }
    @Override public int bloodBucket() { return profile.bloodBucket(); }
    @Override public int workCountFor(MeatHookStage stage) { return profile.workCountFor(stage); }
    @Override public Ingredient toolFor(MeatHookStage stage) { return profile.toolFor(stage); }

    @Override public boolean hasHide() { return profile.hasHide(); }
    @Override public MapColor mapColor() { return profile.mapColor(); }
    @Override public List<DropSpec> dropsFor(MeatHookStage stage) { return profile.dropsFor(stage); }
    @Override public List<DropSpec> dropsForSupport(MeatHookStage stage) { return profile.dropsForSupport(stage); }
    @Override public List<DropSpec> dropsForTrivial(MeatHookStage stage) { return profile.dropsForTrivial(stage); }
}