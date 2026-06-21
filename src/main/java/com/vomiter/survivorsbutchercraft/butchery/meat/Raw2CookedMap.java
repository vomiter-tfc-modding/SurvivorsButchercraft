package com.vomiter.survivorsbutchercraft.butchery.meat;

import com.lance5057.butchercraft.ButchercraftItems;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class Raw2CookedMap {
    private static final HashMap<Item, Item> entries = new HashMap<>();

    private Raw2CookedMap() {
    }

    static {
        // TFC ordinary meats
        put(TFCItems.FOOD.get(Food.BEEF).get(), TFCItems.FOOD.get(Food.COOKED_BEEF).get());
        put(TFCItems.FOOD.get(Food.PORK).get(), TFCItems.FOOD.get(Food.COOKED_PORK).get());
        put(TFCItems.FOOD.get(Food.MUTTON).get(), TFCItems.FOOD.get(Food.COOKED_MUTTON).get());
        put(TFCItems.FOOD.get(Food.CHEVON).get(), TFCItems.FOOD.get(Food.COOKED_CHEVON).get());

        // Beef
        put(ButchercraftItems.BEEF_SCRAPS.get(), ButchercraftItems.COOKED_BEEF_SCRAPS.get());
        put(ButchercraftItems.GROUND_BEEF.get(), ButchercraftItems.COOKED_GROUND_BEEF.get());
        put(ButchercraftItems.CUBED_BEEF.get(), ButchercraftItems.COOKED_CUBED_BEEF.get());
        put(ButchercraftItems.BEEF_RIBS.get(), ButchercraftItems.COOKED_BEEF_RIBS.get());
        put(ButchercraftItems.BEEF_ROAST.get(), ButchercraftItems.COOKED_BEEF_ROAST.get());
        put(ButchercraftItems.BEEF_STEW_MEAT.get(), ButchercraftItems.COOKED_BEEF_STEW_MEAT.get());
        put(ButchercraftItems.OXTAIL.get(), ButchercraftItems.COOKED_OXTAIL.get());
        put(ButchercraftItems.TONGUE.get(), ButchercraftItems.COOKED_TONGUE.get());

        // Pork
        put(ButchercraftItems.PORK_SCRAPS.get(), ButchercraftItems.COOKED_PORK_SCRAPS.get());
        put(ButchercraftItems.GROUND_PORK.get(), ButchercraftItems.COOKED_GROUND_PORK.get());
        put(ButchercraftItems.CUBED_PORK.get(), ButchercraftItems.COOKED_CUBED_PORK.get());
        put(ButchercraftItems.PORK_RIBS.get(), ButchercraftItems.COOKED_PORK_RIBS.get());
        put(ButchercraftItems.PORK_ROAST.get(), ButchercraftItems.COOKED_PORK_ROAST.get());
        put(ButchercraftItems.PORK_STEW_MEAT.get(), ButchercraftItems.COOKED_PORK_STEW_MEAT.get());

        // Mutton / Lamb
        put(ButchercraftItems.MUTTON_SCRAPS.get(), ButchercraftItems.COOKED_MUTTON_SCRAPS.get());
        put(ButchercraftItems.GROUND_MUTTON.get(), ButchercraftItems.COOKED_GROUND_MUTTON.get());
        put(ButchercraftItems.CUBED_MUTTON.get(), ButchercraftItems.COOKED_CUBED_MUTTON.get());
        put(ButchercraftItems.MUTTON_RIBS.get(), ButchercraftItems.COOKED_MUTTON_RIBS.get());
        put(ButchercraftItems.MUTTON_ROAST.get(), ButchercraftItems.COOKED_MUTTON_ROAST.get());
        put(ButchercraftItems.MUTTON_STEW_MEAT.get(), ButchercraftItems.COOKED_MUTTON_STEW_MEAT.get());

        // Goat / Chevon
        put(ButchercraftItems.GOAT_CHOP.get(), ButchercraftItems.COOKED_GOAT_CHOP.get());
        put(ButchercraftItems.GOAT_SCRAPS.get(), ButchercraftItems.COOKED_GOAT_SCRAPS.get());
        put(ButchercraftItems.GROUND_GOAT.get(), ButchercraftItems.COOKED_GROUND_GOAT.get());
        put(ButchercraftItems.CUBED_GOAT.get(), ButchercraftItems.COOKED_CUBED_GOAT.get());
        put(ButchercraftItems.GOAT_RIBS.get(), ButchercraftItems.COOKED_GOAT_RIBS.get());
        put(ButchercraftItems.GOAT_ROAST.get(), ButchercraftItems.COOKED_GOAT_ROAST.get());
        put(ButchercraftItems.GOAT_STEW_MEAT.get(), ButchercraftItems.COOKED_GOAT_STEW_MEAT.get());

        // Chicken
        put(ButchercraftItems.CHICKEN_BREAST.get(), ButchercraftItems.COOKED_CHICKEN_BREAST.get());
        put(ButchercraftItems.CUBED_CHICKEN.get(), ButchercraftItems.COOKED_CUBED_CHICKEN.get());
        put(ButchercraftItems.GROUND_CHICKEN.get(), ButchercraftItems.COOKED_GROUND_CHICKEN.get());
        put(ButchercraftItems.CHICKEN_LEG.get(), ButchercraftItems.COOKED_CHICKEN_LEG.get());
        put(ButchercraftItems.CHICKEN_SCRAPS.get(), ButchercraftItems.COOKED_CHICKEN_SCRAPS.get());
        put(ButchercraftItems.CHICKEN_THIGH.get(), ButchercraftItems.COOKED_CHICKEN_THIGH.get());
        put(ButchercraftItems.CHICKEN_WING.get(), ButchercraftItems.COOKED_CHICKEN_WING.get());
        put(ButchercraftItems.STEW_CHICKEN.get(), ButchercraftItems.COOKED_STEW_CHICKEN.get());
        put(ButchercraftItems.WATTLE.get(), ButchercraftItems.COOKED_WATTLE.get());

        // Rabbit
        put(ButchercraftItems.RABBIT_SADDLE.get(), ButchercraftItems.COOKED_RABBIT_SADDLE.get());
        put(ButchercraftItems.CUBED_RABBIT.get(), ButchercraftItems.COOKED_CUBED_RABBIT.get());
        put(ButchercraftItems.GROUND_RABBIT.get(), ButchercraftItems.COOKED_GROUND_RABBIT.get());
        put(ButchercraftItems.RABBIT_LEG.get(), ButchercraftItems.COOKED_RABBIT_LEG.get());
        put(ButchercraftItems.RABBIT_SCRAPS.get(), ButchercraftItems.COOKED_RABBIT_SCRAPS.get());
        put(ButchercraftItems.RABBIT_THIGH.get(), ButchercraftItems.COOKED_RABBIT_THIGH.get());
        put(ButchercraftItems.STEW_RABBIT.get(), ButchercraftItems.COOKED_STEW_RABBIT.get());

        // Organs / offal
        put(ButchercraftItems.TRIPE.get(), ButchercraftItems.COOKED_TRIPE.get());
        put(ButchercraftItems.STOMACH.get(), ButchercraftItems.COOKED_STOMACH.get());
        put(ButchercraftItems.LUNG.get(), ButchercraftItems.COOKED_LUNG.get());
        put(ButchercraftItems.LIVER.get(), ButchercraftItems.COOKED_LIVER.get());
        put(ButchercraftItems.KIDNEY.get(), ButchercraftItems.COOKED_KIDNEY.get());
        put(ButchercraftItems.HEART.get(), ButchercraftItems.COOKED_HEART.get());
        put(ButchercraftItems.BRAIN.get(), ButchercraftItems.COOKED_BRAIN.get());
        put(ButchercraftItems.EYEBALL.get(), ButchercraftItems.COOKED_EYEBALL.get());

        // Sausage
        put(ButchercraftItems.SAUSAGE.get(), ButchercraftItems.COOKED_SAUSAGE.get());
        put(ButchercraftItems.BLOOD_SAUSAGE.get(), ButchercraftItems.COOKED_BLOOD_SAUSAGE.get());
    }

    private static void put(Item raw, Item cooked) {
        entries.put(raw, cooked);
    }

    public static Map<Item, Item> entries() {
        return entries;
    }

    public static Item get(Item raw) {
        return entries.get(raw);
    }

    public static Item getOrDefault(Item raw, Item fallback) {
        return entries.getOrDefault(raw, fallback);
    }

    public static ItemStack cook(ItemStack rawStack) {
        Item cooked = entries.get(rawStack.getItem());
        if (cooked == null) {
            return rawStack.copy();
        }

        return new ItemStack(cooked, rawStack.getCount());
    }
}