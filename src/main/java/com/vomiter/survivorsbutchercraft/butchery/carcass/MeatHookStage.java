package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

public enum MeatHookStage {
    HOOK("hooked"),
    SKIN("skinned"),
    DISEMBOWEL("gutted"),
    BISECT("bisected"),
    BUTCHER("butchered");

    public final String pp;
    public String pre;

    public String previousStep(){
        if(pre != null) return pre;
        String previousStep;
        int i = Arrays.stream(values()).toList().indexOf(this);
        if(i == 0) previousStep = "";
        else previousStep = Arrays.stream(values()).toList().get(i-1).pp;
        SurvivorsButchercraft.LOGGER.info(name() + ":" + previousStep);
        pre = previousStep;
        return previousStep;
    };

    public Item iconicTool(){
        return switch (this){
            case SKIN -> ButchercraftItems.SKINNING_KNIFE.get();
            case DISEMBOWEL -> ButchercraftItems.BONE_SAW.get();
            case BISECT -> ButchercraftItems.GUT_KNIFE.get();
            case BUTCHER -> ButchercraftItems.BUTCHER_KNIFE.get();
            default -> Items.BARRIER;
        };
    }

    public Ingredient acceptableTools(){
        return switch (this){
            case SKIN -> Ingredient.of(SBTags.Items.SKINNING_TOOLS);
            case DISEMBOWEL -> Ingredient.of(SBTags.Items.BEHEADING_TOOLS);
            case BISECT -> Ingredient.of(SBTags.Items.GUTTING_TOOLS);
            case BUTCHER -> Ingredient.of(SBTags.Items.BUTCHERING_TOOLS);
            default -> Ingredient.EMPTY;
        };
    }


    MeatHookStage(String pp){
        this.pp = pp;
    }
}
