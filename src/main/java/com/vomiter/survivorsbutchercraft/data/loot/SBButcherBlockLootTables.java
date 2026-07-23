package com.vomiter.survivorsbutchercraft.data.loot;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.function.BiConsumer;

public class SBButcherBlockLootTables  implements LootTableSubProvider {
    public static EnumMap<MeatType, ResourceLocation> ORDINARY2CUBED = new EnumMap<>(MeatType.class);
    public static EnumMap<MeatType, ResourceLocation> ROAST2ORDINARY = new EnumMap<>(MeatType.class);
    public static ResourceLocation CASING = Helpers.id("butcherblock/casing");
    public static ResourceLocation BRAIN = Helpers.id("butcherblock/brain");
    public static ResourceLocation GOAT_HEAD = Helpers.id("butcherblock/goat_head");
    public static ResourceLocation EMPTY =  Helpers.id("butchercraft", "empty");

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
        var casing = LootTable.lootTable()
                .withPool(LootPools.poolOnce(DropSpec.of(ButchercraftItems.CASING.get())))
                .withPool(LootPools.poolOnce(DropSpec.of(ButchercraftItems.FAT.get()).withCount(0,2)))
                .withPool(LootPools.poolOnce(DropSpec.of(ButchercraftItems.SINEW.get()).withCount(0,2)));
        biConsumer.accept(CASING, casing);
        var brain = LootTable.lootTable()
                .withPool(LootPools.poolOnce(DropSpec.of(ButchercraftItems.BRAIN.get())))
                .withPool(LootPools.poolOnce(DropSpec.of(ButchercraftItems.EYEBALL.get().getDefaultInstance().copyWithCount(2))))
                .withPool(LootPools.poolOnce(DropSpec.of(Items.BONE).withCount(1,3)));
        biConsumer.accept(BRAIN, brain);
        brain.withPool(LootPools.poolOnce(DropSpec.of(TFCItems.GOAT_HORN.get().getDefaultInstance().copyWithCount(2))));
        biConsumer.accept(GOAT_HEAD, brain);
    }
}
