package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public interface ICarcassProfile {

    Item carcassItem();
    MeatType getMeatType();

    default int bloodBucket(){
        return 1;
    }

    default int workCountFor(MeatHookStage stage){
        return switch (stage){
            case SKIN, BUTCHER, BISECT, DISEMBOWEL -> 12;
            default -> 1;
        };
    }

    default Ingredient toolFor(MeatHookStage stage){
        return switch (stage){
            case SKIN -> Ingredient.of(SBTags.Items.SKINNING_TOOLS);
            case DISEMBOWEL -> Ingredient.of(SBTags.Items.BEHEADING_TOOLS);
            case BISECT -> Ingredient.of(SBTags.Items.GUTTING_TOOLS);
            case BUTCHER -> Ingredient.of(SBTags.Items.BUTCHERING_TOOLS);
            default -> Ingredient.EMPTY;
        };
    }

    default boolean hasHide() {
        return false;
    }

    default MapColor mapColor() {
        return MapColor.NONE;
    }

    /** 每個 cut group 的掉落定義 */
    default List<DropSpec> dropsFor(MeatHookStage stage) {
        return List.of();
    }

    default List<DropSpec> dropsForSupport(MeatHookStage stage) {
        return List.of();
    }

    default List<DropSpec> dropsForTrivial(MeatHookStage stage) {
        return List.of();
    }

    default NumberProvider trivialRolls(MeatHookStage stage){
        return ConstantValue.exactly(1);
    }

    /** 給之後 alpha → beta 換掉用 */
    default boolean isPlaceholder() {
        return true;
    }
}
