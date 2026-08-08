package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SBFoodTraits {
    public static final String KEY_PREFIX = "foodtrait." + SurvivorsButchercraft.MODID + ".";

    public static final DeferredRegister<FoodTrait> TRAITS = DeferredRegister.create(FoodTraits.KEY, SurvivorsButchercraft.MODID);;


    private static ResourceLocation id(String path) {
        return Helpers.id(path);
    }
    private static String translationKey(String path){return KEY_PREFIX + path;}
    private static DeferredHolder<FoodTrait, FoodTrait> create(String path, double decay){return TRAITS.register(path, () -> new FoodTrait(() -> decay, translationKey(path)));
    }

    public static DeferredHolder<FoodTrait, FoodTrait> PRESERVED = create("preserved", 0);;
    private SBFoodTraits() {}
}