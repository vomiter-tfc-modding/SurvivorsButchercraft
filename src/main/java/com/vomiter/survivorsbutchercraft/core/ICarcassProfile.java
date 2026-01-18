package com.vomiter.survivorsbutchercraft.core;

import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public interface ICarcassProfile {

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
