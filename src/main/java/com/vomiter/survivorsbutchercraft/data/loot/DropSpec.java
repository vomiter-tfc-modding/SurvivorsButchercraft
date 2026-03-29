package com.vomiter.survivorsbutchercraft.data.loot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record DropSpec(
        LootItem.Builder<?> entry,
        List<LootItemFunction.Builder> functions
) {
    // ---- factories ----

    public static DropSpec of(Item item) {
        return new DropSpec(LootItem.lootTableItem(item), new ArrayList<>());
    }

    public static DropSpec of(Item item, int min, int max) {
        return of(item).withCount(min, max);
    }

    /** 可以「塞 ItemStack」：count + (optional) NBT */
    public static DropSpec of(ItemStack stack) {
        DropSpec spec = of(stack.getItem()).withCount(stack.getCount());

        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && !tag.isEmpty()) {
                spec = spec.with(SetNbtFunction.setTag(tag.copy()));
            }
        }

        return spec;
    }

    // ---- fluent modifiers ----

    public DropSpec with(LootItemFunction.Builder fn) {
        ArrayList<LootItemFunction.Builder> list = new ArrayList<>(this.functions);
        list.add(fn);
        return new DropSpec(this.entry, list);
    }

    public DropSpec withAll(LootItemFunction.Builder... fns) {
        ArrayList<LootItemFunction.Builder> list = new ArrayList<>(this.functions);
        list.addAll(Arrays.asList(fns));
        return new DropSpec(this.entry, list);
    }

    public DropSpec withCount(int count) {
        return with(SetItemCountFunction.setCount(ConstantValue.exactly((float) count)));
    }

    public DropSpec withCount(int min, int max) {
        return with(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
    }

    // ---- build helpers ----

    /** 把 entry + functions 套到 LootPool.Builder（等價於 LootPool.lootPool().add(entry).apply(fn1).apply(fn2)...） */
    public LootPool.@NotNull Builder toPool() {
        LootPool.Builder pool = LootPool.lootPool().add(entry);
        for (LootItemFunction.Builder fn : functions) {
            pool.apply(fn);
        }
        return pool;
    }

    public LootItem.@NotNull Builder<?> toEntry() {
        LootItem.Builder<?> e = entry;
        for (LootItemFunction.Builder fn : functions) {
            e.apply(fn);
        }
        return e;
    }
}
