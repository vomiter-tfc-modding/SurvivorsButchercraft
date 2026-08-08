package com.vomiter.survivorsbutchercraft.compat.jei;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.integration.jei.categories.ButcherBlockRecipeCategory;
import com.lance5057.butchercraft.integration.jei.categories.GrinderRecipeCategory;
import com.lance5057.butchercraft.integration.jei.categories.MeatHookRecipeCategory;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import com.vomiter.survivorsbutchercraft.common.registry.SBRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SurvivorsButchercraft.MODID, "main");

    public ResourceLocation getPluginUid() {
        return ID;
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new CustomButcherBlockRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CustomMeatHookRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        registry.addRecipes(
                CustomMeatHookRecipeCategory.TYPE,
                Minecraft.getInstance().level.getRecipeManager()
                        .getAllRecipesFor(SBRecipes.CUSTOM_MEAT_HOOK.get())
                        .stream().map(RecipeHolder::value).toList()
        );

        registry.addRecipes(
                CustomButcherBlockRecipeCategory.TYPE,
                Minecraft.getInstance().level.getRecipeManager()
                        .getAllRecipesFor(SBRecipes.CUSTOM_BUTCHER_BLOCK.get())
                        .stream().map(RecipeHolder::value).toList()
        );

    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        SBBlocks.MEAT_HOOKS.values().forEach(blockBlockDeferredHolder -> {
            registry.addRecipeCatalyst(
                    blockBlockDeferredHolder.get(),
                    CustomMeatHookRecipeCategory.TYPE);
        });
        registry.addRecipeCatalyst(ButchercraftItems.BUTCHER_BLOCK_BLOCK_ITEM.get(), CustomButcherBlockRecipeCategory.TYPE);
    }
}
