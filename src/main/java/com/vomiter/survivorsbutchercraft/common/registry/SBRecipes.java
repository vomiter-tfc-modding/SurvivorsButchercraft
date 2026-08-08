package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomMeatHookRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SBRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
            .create(BuiltInRegistries.RECIPE_TYPE, SurvivorsButchercraft.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
            .create(BuiltInRegistries.RECIPE_SERIALIZER, SurvivorsButchercraft.MODID);

    private static <R extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<R>> register(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<R>() {
            public String toString() {
                return name;
            }
        });
    }

    public static DeferredHolder<RecipeType<?>, RecipeType<CustomButcherBlockRecipe>> CUSTOM_BUTCHER_BLOCK
            = register("custom_butcher_block");
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CustomButcherBlockRecipe>> CUSTOM_BUTCHER_BLOCK_SERIALIZER
            = RECIPE_SERIALIZERS.register("custom_butcher_block", () -> CustomButcherBlockRecipe.Serializer.INSTANCE);
    public static DeferredHolder<RecipeType<?>, RecipeType<CustomMeatHookRecipe>> CUSTOM_MEAT_HOOK
            = register("custom_meat_hook");
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CustomMeatHookRecipe>> CUSTOM_MEAT_HOOK_SERIALIZER
            = RECIPE_SERIALIZERS.register("custom_meat_hook", () -> CustomMeatHookRecipe.Serializer.INSTANCE);

}
