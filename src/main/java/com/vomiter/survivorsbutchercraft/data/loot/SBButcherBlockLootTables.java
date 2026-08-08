package com.vomiter.survivorsbutchercraft.data.loot;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.function.BiConsumer;

public class SBButcherBlockLootTables implements LootTableSubProvider {
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
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
    }
}
