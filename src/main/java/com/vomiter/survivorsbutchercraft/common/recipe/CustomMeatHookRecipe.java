package com.vomiter.survivorsbutchercraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Collection;

public class CustomMeatHookRecipe extends HookRecipe {
    private final NonNullList<NonNullList<ChanceResult>> chanceResults;

    public CustomMeatHookRecipe(
            ResourceLocation idIn,
            String groupIn,
            Ingredient carcassIn,
            NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
            NonNullList<Ingredient> JEI,
            NonNullList<NonNullList<ChanceResult>> chanceResults
            ) {
        super(idIn, groupIn, carcassIn, recipeToolsIn, JEI);
        this.chanceResults = chanceResults;
    }

    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public NonNullList<ChanceResult> getResults(int stage) {
        return chanceResults.get(stage);
    }

    public static class Serializer implements RecipeSerializer<CustomMeatHookRecipe> {
        public static Serializer INSTANCE = new Serializer();

        public CustomMeatHookRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            String group = GsonHelper.getAsString(pSerializedRecipe, "group", "");
            NonNullList<AnimatedRecipeItemUse> recipeItemUses = NonNullList.create();
            pSerializedRecipe.getAsJsonArray("tools").forEach((jsonElement) -> recipeItemUses.add(AnimatedRecipeItemUse.read(jsonElement.getAsJsonObject())));
            Ingredient carcass = Ingredient.fromJson(pSerializedRecipe.get("carcass"));
            NonNullList<Ingredient> jei = itemsFromJson(pSerializedRecipe.getAsJsonArray("jei"));
            NonNullList<NonNullList<ChanceResult>> chanceResults = NonNullList.create();
            pSerializedRecipe
                    .getAsJsonArray("results")
                    .forEach(
                            jsonElement -> {
                                NonNullList<ChanceResult> results1 = NonNullList.create();
                                jsonElement.getAsJsonArray().forEach(jsonElement1 -> results1.add(ChanceResult.deserialize(jsonElement1)));
                                chanceResults.add(results1);
                            }
                    );
            return new CustomMeatHookRecipe(pRecipeId, group, carcass, recipeItemUses, jei, chanceResults);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
                nonnulllist.add(ingredient);
            }

            return nonnulllist;
        }

        public CustomMeatHookRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient carcassIn = Ingredient.fromNetwork(buffer);
            int listSize = buffer.readVarInt();
            NonNullList<AnimatedRecipeItemUse> tools = NonNullList.withSize(listSize, AnimatedRecipeItemUse.EMPTY);
            tools.replaceAll((ignored) -> AnimatedRecipeItemUse.read(buffer));
            int jeiSize = buffer.readVarInt();
            NonNullList<Ingredient> jei = NonNullList.withSize(jeiSize, Ingredient.EMPTY);
            jei.replaceAll((ignored) -> Ingredient.fromNetwork(buffer));
            int resultSize = buffer.readVarInt();
            NonNullList<NonNullList<ChanceResult>> chanceResults = NonNullList.create();
            for (int i = 0; i < resultSize; i++) {
                int resultSize1 = buffer.readVarInt();
                NonNullList<ChanceResult> chanceResults1 = NonNullList.withSize(resultSize1, ChanceResult.EMPTY);
                chanceResults1.replaceAll(ignored -> ChanceResult.read(buffer));
                chanceResults.add(chanceResults1);
            }
            return new CustomMeatHookRecipe(recipeId, group, carcassIn, tools, jei, chanceResults);
        }

        public void toNetwork(FriendlyByteBuf buffer, CustomMeatHookRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getCarcassIn().toNetwork(buffer);
            buffer.writeVarInt(recipe.getRecipeToolsIn().size());
            recipe.getRecipeToolsIn().forEach((riu) -> AnimatedRecipeItemUse.write(riu, buffer));
            buffer.writeVarInt(recipe.getDummyList().size());
            recipe.getDummyList().forEach((i) -> i.toNetwork(buffer));
            buffer.writeVarInt(recipe.chanceResults.size());
            recipe.chanceResults.forEach(chanceResults1 -> {
                buffer.writeVarInt(chanceResults1.size());
                chanceResults1.forEach(chanceResult -> chanceResult.write(buffer));
            });
        }
    }
}
