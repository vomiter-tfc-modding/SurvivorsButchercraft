package com.vomiter.survivorsbutchercraft.butchery.meat;

import com.lance5057.butchercraft.ButchercraftItems;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public final class MeatMap {
    public static final EnumMap<MeatType, EnumMap<MeatProduct, Item>> entries = new EnumMap<>(MeatType.class);

    private MeatMap() {
    }

    static {
        EnumMap<MeatProduct, Item> beefs = new EnumMap<>(MeatProduct.class);
        beefs.put(MeatProduct.ORDINARY, TFCItems.FOOD.get(Food.BEEF).get());
        beefs.put(MeatProduct.SCRAP, ButchercraftItems.BEEF_SCRAPS.get());
        beefs.put(MeatProduct.CUBED, ButchercraftItems.CUBED_BEEF.get());
        beefs.put(MeatProduct.GROUND, ButchercraftItems.GROUND_BEEF.get());
        beefs.put(MeatProduct.ROAST, ButchercraftItems.BEEF_ROAST.get());
        beefs.put(MeatProduct.RIB, ButchercraftItems.BEEF_RIBS.get());
        beefs.put(MeatProduct.STEW_MEAT, ButchercraftItems.BEEF_STEW_MEAT.get());
        beefs.put(MeatProduct.TAIL, ButchercraftItems.OXTAIL.get());
        entries.put(MeatType.BEEF, beefs);

        EnumMap<MeatProduct, Item> porks = new EnumMap<>(MeatProduct.class);
        porks.put(MeatProduct.ORDINARY, TFCItems.FOOD.get(Food.PORK).get());
        porks.put(MeatProduct.SCRAP, ButchercraftItems.PORK_SCRAPS.get());
        porks.put(MeatProduct.CUBED, ButchercraftItems.CUBED_PORK.get());
        porks.put(MeatProduct.GROUND, ButchercraftItems.GROUND_PORK.get());
        porks.put(MeatProduct.ROAST, ButchercraftItems.PORK_ROAST.get());
        porks.put(MeatProduct.RIB, ButchercraftItems.PORK_RIBS.get());
        porks.put(MeatProduct.STEW_MEAT, ButchercraftItems.PORK_STEW_MEAT.get());
        entries.put(MeatType.PORK, porks);

        EnumMap<MeatProduct, Item> muttons = new EnumMap<>(MeatProduct.class);
        muttons.put(MeatProduct.ORDINARY, TFCItems.FOOD.get(Food.MUTTON).get());
        muttons.put(MeatProduct.SCRAP, ButchercraftItems.MUTTON_SCRAPS.get());
        muttons.put(MeatProduct.CUBED, ButchercraftItems.CUBED_MUTTON.get());
        muttons.put(MeatProduct.GROUND, ButchercraftItems.GROUND_MUTTON.get());
        muttons.put(MeatProduct.ROAST, ButchercraftItems.MUTTON_ROAST.get());
        muttons.put(MeatProduct.RIB, ButchercraftItems.MUTTON_RIBS.get());
        muttons.put(MeatProduct.STEW_MEAT, ButchercraftItems.MUTTON_STEW_MEAT.get());
        entries.put(MeatType.MUTTON, muttons);

        EnumMap<MeatProduct, Item> chevons = new EnumMap<>(MeatProduct.class);
        chevons.put(MeatProduct.ORDINARY, TFCItems.FOOD.get(Food.CHEVON).get());
        chevons.put(MeatProduct.SCRAP, ButchercraftItems.GOAT_SCRAPS.get());
        chevons.put(MeatProduct.CUBED, ButchercraftItems.CUBED_GOAT.get());
        chevons.put(MeatProduct.GROUND, ButchercraftItems.GROUND_GOAT.get());
        chevons.put(MeatProduct.ROAST, ButchercraftItems.GOAT_ROAST.get());
        chevons.put(MeatProduct.RIB, ButchercraftItems.GOAT_RIBS.get());
        chevons.put(MeatProduct.STEW_MEAT, ButchercraftItems.GOAT_STEW_MEAT.get());
        entries.put(MeatType.CHEVON, chevons);
    }

    public static Item get(MeatType type, MeatProduct product) {
        Map<MeatProduct, Item> products = entries.get(type);
        if (products == null) {
            return null;
        }
        return products.get(product);
    }
}