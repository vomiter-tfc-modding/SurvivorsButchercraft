package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomMeatHookRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
            .create(ForgeRegistries.RECIPE_TYPES, SurvivorsButchercraft.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, SurvivorsButchercraft.MODID);

    private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<R>() {
            public String toString() {
                return name;
            }
        });
    }

    public static RegistryObject<RecipeType<CustomButcherBlockRecipe>> CUSTOM_BUTCHER_BLOCK
            = register("custom_butcher_block");
    public static RegistryObject<RecipeSerializer<CustomButcherBlockRecipe>> CUSTOM_BUTCHER_BLOCK_SERIALIZER
            = RECIPE_SERIALIZERS.register("custom_butcher_block", () -> CustomButcherBlockRecipe.Serializer.INSTANCE);
    public static RegistryObject<RecipeType<CustomButcherBlockRecipe>> CUSTOM_MEAT_HOOK
            = register("custom_meat_hook");
    public static RegistryObject<RecipeSerializer<CustomMeatHookRecipe>> CUSTOM_MEAT_HOOK_SERIALIZER
            = RECIPE_SERIALIZERS.register("custom_meat_hook", () -> CustomMeatHookRecipe.Serializer.INSTANCE);

}
