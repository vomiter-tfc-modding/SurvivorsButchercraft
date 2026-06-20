package com.vomiter.survivorsbutchercraft.common.registry;

import com.lance5057.butchercraft.items.CarcassItem;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.item.SkullLikeItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class SBItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsButchercraft.MODID);
    public static final Map<Carcass, RegistryObject<Item>> CARCASSES = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Item>> HIDES = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Item>> HEADS = new EnumMap<>(Carcass.class);

    static {
        for (Carcass carcass : Carcass.values()) {
            CARCASSES.put(carcass, ITEMS.register("carcass/" + carcass.serializedName(),
                    () -> new CarcassItem(new Item.Properties().stacksTo(1))));
            HEADS.put(carcass, ITEMS.register("head/" + carcass.serializedName(),
                    () -> new SkullLikeItem(
                            SBBlocks.HEADS.get(carcass).get(),
                            SBBlocks.WALL_HEADS.get(carcass).get(),
                            new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN)));

            if (carcass.hasHide()) {
                HIDES.put(carcass, ITEMS.register("hide/" + carcass.serializedName(),
                        () -> new BlockItem(SBBlocks.HIDE_CARPETS.get(carcass).get(), new Item.Properties())));
            }
        }
    }
}
