package com.vomiter.survivorsbutchercraft.util;

import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class CarcassDataHelper {
    public static final String ROOT = "sb_carcass";
    public static final String TFC_ANIMAL = "tfc_animal";
    public static final String ENTITY_ID = "entity_id";

    // 這幾個是 render / debug 常用快取欄位
    public static final String MALE = "male";
    public static final String OLD = "old";
    public static final String GENETIC_SIZE = "genetic_size";
    public static final String FAMILIARITY = "familiarity";

    private CarcassDataHelper() {
    }

    public static CompoundTag getOrCreateRoot(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            tag.put(ROOT, new CompoundTag());
        }
        return tag.getCompound(ROOT);
    }

    public static CompoundTag getRoot(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            return null;
        }
        return tag.getCompound(ROOT);
    }

    public static boolean hasData(ItemStack stack) {
        return getRoot(stack) != null;
    }

    public static void writeFromTFCAnimal(ItemStack stack, TFCAnimalProperties props, ResourceLocation entityId) {
        CompoundTag root = getOrCreateRoot(stack);

        // 1. 保留完整 TFC common animal data
        CompoundTag tfcAnimal = new CompoundTag();
        props.saveCommonAnimalData(tfcAnimal);
        root.put(TFC_ANIMAL, tfcAnimal);
        root.putString(ENTITY_ID, entityId.toString());

        // 2. 額外做 render / 判斷方便用的 mirror 欄位
        root.putBoolean(MALE, props.isMale());
        root.putBoolean(OLD, props.getAgeType() == TFCAnimalProperties.Age.OLD);
        root.putInt(GENETIC_SIZE, props.getGeneticSize());
        root.putFloat(FAMILIARITY, props.getFamiliarity());
    }

    public static ResourceLocation getId(ItemStack stack){
        CompoundTag root = getRoot(stack);
        if(root == null) return null;
        return ResourceLocation.tryParse(root.get(ENTITY_ID).getAsString());
    }

    public static boolean isMale(ItemStack stack) {
        CompoundTag root = getRoot(stack);
        return root != null && root.getBoolean(MALE);
    }

    public static boolean isOld(ItemStack stack) {
        CompoundTag root = getRoot(stack);
        return root != null && root.getBoolean(OLD);
    }

    public static int getGeneticSize(ItemStack stack) {
        CompoundTag root = getRoot(stack);
        return root != null ? root.getInt(GENETIC_SIZE) : 0;
    }

    public static float getFamiliarity(ItemStack stack) {
        CompoundTag root = getRoot(stack);
        return root != null ? root.getFloat(FAMILIARITY) : 0f;
    }
}