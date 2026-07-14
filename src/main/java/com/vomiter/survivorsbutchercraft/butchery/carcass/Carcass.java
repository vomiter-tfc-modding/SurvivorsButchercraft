package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.MapColor;

import java.util.List;
import java.util.Locale;

public enum Carcass implements ICarcassProfile {
    YAK(new YakCarcassProfile()),
    MUSK_OX(new MuskOxProfile()),
    PIG(new PigCarcassProfile()),
    GOAT(new GoatCarcassProfile()),
    SHEEP(new SheepCarcassProfile()),
    COW(new CowCarcassProfile());

    private final ICarcassProfile profile;
    public ICarcassProfile getProfile(){
        return profile;
    }

    Carcass(ICarcassProfile profile) {
        this.profile = profile;
    }

    public static Carcass getCarcassFromItem(Item item){
        for (Carcass carcass : Carcass.values()) {
            if(item.equals(carcass.carcassItem())) return carcass;
        }
        return null;
    }

    public boolean hasMaleHead(){
        return List.of(MUSK_OX, PIG, SHEEP).contains(this);
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