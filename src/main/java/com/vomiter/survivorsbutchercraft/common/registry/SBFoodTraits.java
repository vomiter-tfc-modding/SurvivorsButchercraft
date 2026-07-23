package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;

public final class SBFoodTraits {
    public static final String KEY_PREFIX = "foodtrait." + SurvivorsButchercraft.MODID + ".";

    private static boolean BOOTSTRAPPED = false;

    private static ResourceLocation id(String path) {
        return Helpers.id(path);
    }
    private static String translationKey(String path){return KEY_PREFIX + path;}
    private static FoodTrait create(String path, float decay){return FoodTrait.register(id(path), new FoodTrait(decay, translationKey(path)));
    }

    public static FoodTrait PRESERVED;

    public static void bootstrap() {
        if (BOOTSTRAPPED) return;
        BOOTSTRAPPED = true;

        PRESERVED = create("preserved", 0);
    }

    private SBFoodTraits() {}
}