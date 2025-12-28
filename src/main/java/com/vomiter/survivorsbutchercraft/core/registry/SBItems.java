package com.vomiter.survivorsbutchercraft.core.registry;

import com.lance5057.butchercraft.items.CarcassItem;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class SBItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsButchercraft.MODID);
    public static final Map<Carcass, RegistryObject<Item>> CARCASSES = new EnumMap<>(Carcass.class);
    static {
        for (Carcass carcass : Carcass.values()) {
            CARCASSES.put(carcass, ITEMS.register(carcass.name().toLowerCase(Locale.ROOT) + "_carcass", () -> new CarcassItem(new Item.Properties().stacksTo(1))));
        }
    }
}
