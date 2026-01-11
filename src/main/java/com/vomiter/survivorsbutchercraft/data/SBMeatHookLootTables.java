package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.ButchercraftItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;

public class SBMeatHookLootTables implements LootTableSubProvider {
    protected List<ItemStack> CONSTANT_ORGANS = List.of(
            new ItemStack(ButchercraftItems.HEART.get()),
            new ItemStack(ButchercraftItems.LUNG.get(), 2),
            new ItemStack(ButchercraftItems.LIVER.get()),
            new ItemStack(ButchercraftItems.KIDNEY.get(), 2)
    );

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> bc) {

    }
}
