package com.vomiter.survivorsbutchercraft.data.loot;

import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.MeatHookStage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Locale;

public final class MeatHookLootHelper {

    private MeatHookLootHelper() {}

    /* =========================
     *  Folder constants
     * ========================= */

    public static final String ROOT = "meat_hook";
    public static final String SUPPORT = "support";
    public static final String TRIVIAL = "trivial";


    /* =========================
     *  Stage naming (single source of truth)
     * ========================= */

    /**
     * stage_action：目前採用 enum name 的小寫（HOOK/SKIN/... -> hook/skin/...）
     * 之後若要 action/state mapping，只改這個 method 就好。
     */
    public static String stageAction(MeatHookStage stage) {
        return stage.name().toLowerCase(Locale.ROOT);
    }

    /* =========================
     *  Paths (namespace-free)
     * ========================= */

    /** meat_hook/<carcass>/<stage_action> */
    public static String mainPath(Carcass carcass, MeatHookStage stage) {
        return ROOT + "/" + carcass.serializedName() + "/" + stageAction(stage);
    }

    /** meat_hook/<carcass>/support/<stage_action> */
    public static String supportPath(Carcass carcass, MeatHookStage stage, String rank) {
        return ROOT + "/" + carcass.serializedName() + "/" + rank + "/" + stageAction(stage);
    }

    /* =========================
     *  ResourceLocations (namespaced)
     * ========================= */

    public static ResourceLocation mainTable(Carcass carcass, MeatHookStage stage) {
        return Helpers.id(mainPath(carcass, stage));
    }

    public static ResourceLocation supportTable(Carcass carcass, MeatHookStage stage) {
        return supportTable(carcass, stage, SUPPORT);
    }

    public static ResourceLocation trivialTable(Carcass carcass, MeatHookStage stage) {
        return supportTable(carcass, stage, TRIVIAL);
    }

    private static ResourceLocation supportTable(Carcass carcass, MeatHookStage stage, String rank) {
        return Helpers.id(supportPath(carcass, stage, rank));
    }

    /* =========================
     *  Stage relation helpers
     * ========================= */

    public static boolean isFirstStage(MeatHookStage stage) {
        return stage == MeatHookStage.HOOK;
    }

    public static boolean hasPrevious(MeatHookStage stage) {
        return !isFirstStage(stage);
    }

    /** 上一階段的「狀態字串」（hooked/skinned/...），用來決定當下顯示的 carcass 模型 */
    public static String previousStateName(MeatHookStage stage) {
        return stage.previousStep();
    }

    /**
     * state 名稱：hooked/skinned/gutted/...
     */
    public static String postStateName(MeatHookStage stage) {
        return stage.pp;
    }


    /* =========================
     *  Pool reference helpers
     * ========================= */

    public static LootPool.Builder includeSupport(MeatHookStage stage, Carcass carcass) {
        return includeSupport0(stage, carcass, SUPPORT, ConstantValue.exactly(1));
    }

    public static LootPool.Builder includeTrivial(MeatHookStage stage, Carcass carcass) {
        return includeSupport0(stage, carcass, TRIVIAL, carcass.trivialRolls(stage));
    }

    private static LootPool.Builder includeSupport0(MeatHookStage stage, Carcass carcass, String rank, NumberProvider rolls) {
        ResourceLocation table = supportTable(carcass, stage, rank);
        return LootPool.lootPool()
                .setRolls(rolls)
                .add(LootTableReference.lootTableReference(table));
    }

}
