package com.vomiter.survivorsbutchercraft.data.loot;

import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.function.BiConsumer;

public class SBButcherBlockLootTables  implements LootTableSubProvider {
    public static EnumMap<MeatType, ResourceLocation> ORDINARY2CUBED = new EnumMap<>(MeatType.class);
    public static EnumMap<MeatType, ResourceLocation> ROAST2ORDINARY = new EnumMap<>(MeatType.class);
    static {
        for (MeatType type : MeatType.values()) {
            ORDINARY2CUBED.put(type, Helpers.id("butcherblock/" + type.name().toLowerCase(Locale.ROOT) + "/ordinary_to_cubed"));
            ROAST2ORDINARY.put(type, Helpers.id("butcherblock/" + type.name().toLowerCase(Locale.ROOT) + "/roast_to_ordinary"));
        }
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        for (MeatType type : MeatType.values()) {
            var o2c = LootTable.lootTable();
            var r2o = LootTable.lootTable();
            o2c.withPool(LootPools.poolOnce(DropSpec.of(new ItemStack(MeatMap.get(type, MeatProduct.CUBED), 2))));
            r2o.withPool(LootPools.poolOnce(DropSpec.of(new ItemStack(MeatMap.get(type, MeatProduct.ORDINARY), 2))));
            biConsumer.accept(ORDINARY2CUBED.get(type), o2c);
            biConsumer.accept(ROAST2ORDINARY.get(type), r2o);
        }
    }
}
