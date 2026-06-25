package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.Butchercraft;
import com.lance5057.butchercraft.ButchercraftBlocks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SBRecipeFilter {
    private static List<Item> RESULT_ITEM_TO_BLOCK = new ArrayList<>();
    static {
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.BLOOD_SAUSAGE_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_BLOOD_SAUSAGE_BLOCK.get().asItem());

        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.BEEF_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_BEEF_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.CHICKEN_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_CHICKEN_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.GOAT_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_GOAT_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.PORK_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_PORK_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.MUTTON_BLOCK.get().asItem());
        RESULT_ITEM_TO_BLOCK.add(ButchercraftBlocks.COOKED_MUTTON_BLOCK.get().asItem());


    }

    public static boolean shouldBlock(ResourceLocation id, Recipe<?> recipe, RegistryAccess access){
        if(!id.getNamespace().equals(Butchercraft.MOD_ID)) return false;
        if(id.getPath().endsWith("roast")) return true;
        var result = recipe.getResultItem(access);
        if(RESULT_ITEM_TO_BLOCK.contains(result.getItem())) return true;
        var resultId = ForgeRegistries.ITEMS.getKey(result.getItem());

        return false;
    }

}
