package com.vomiter.survivorsbutchercraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.data.builders.recipes.ButcherBlockRecipeBuilder;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.vomiter.survivorsbutchercraft.common.registry.SBRecipes;
import com.vomiter.survivorsbutchercraft.data.loot.SBButcherBlockLootTables;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public class CustomButcherRecipeBuilder {
    private ResourceLocation idIn;
    private String groupIn = "";
    private Ingredient carcass = Ingredient.EMPTY;

    private final NonNullList<AnimatedRecipeItemUse> recipeToolsIn =
            NonNullList.create();

    private final NonNullList<Ingredient> dummyList =
            NonNullList.create();

    private final NonNullList<NonNullList<ChanceResult>> chanceResults =
            NonNullList.create();

    public static CustomButcherRecipeBuilder builder() {
        return new CustomButcherRecipeBuilder();
    }

    public CustomButcherRecipeBuilder id(ResourceLocation id) {
        this.idIn = Objects.requireNonNull(id, "Recipe id cannot be null");
        return this;
    }

    public CustomButcherRecipeBuilder group(String group) {
        this.groupIn = Objects.requireNonNull(group, "Recipe group cannot be null");
        return this;
    }

    public CustomButcherRecipeBuilder carcass(Ingredient carcass) {
        this.carcass = Objects.requireNonNull(carcass, "Carcass ingredient cannot be null");
        return this;
    }

    public CustomButcherRecipeBuilder tool(AnimatedRecipeItemUse tool) {
        this.recipeToolsIn.add(
                Objects.requireNonNull(tool, "Recipe tool cannot be null")
        );
        return this;
    }

    public CustomButcherRecipeBuilder tool(Ingredient tool, int uses, boolean damage, ResourceLocation table, BlacklistedModel... model) {
        this.tool(new AnimatedRecipeItemUse(uses, tool, 1, damage, table, model));
        return this;
    }

    public CustomButcherRecipeBuilder tool(Ingredient tool, int uses, boolean damage, BlacklistedModel... model) {
        this.tool(new AnimatedRecipeItemUse(uses, tool, 1, damage, SBButcherBlockLootTables.EMPTY, model));
        return this;
    }


    public CustomButcherRecipeBuilder tools(
            Collection<? extends AnimatedRecipeItemUse> tools
    ) {
        Objects.requireNonNull(tools, "Recipe tools cannot be null");
        tools.forEach(this::tool);
        return this;
    }

    public CustomButcherRecipeBuilder jei(Ingredient ingredient) {
        this.dummyList.add(
                Objects.requireNonNull(ingredient, "JEI ingredient cannot be null")
        );
        return this;
    }

    public CustomButcherRecipeBuilder jei(
            Collection<? extends Ingredient> ingredients
    ) {
        Objects.requireNonNull(ingredients, "JEI ingredients cannot be null");
        ingredients.forEach(this::jei);
        return this;
    }

    /**
     * 新增一個 results 外層階段。
     *
     * 產生：
     * "results": [
     *   [ resultA, resultB ],
     *   [ resultC ]
     * ]
     */
    public CustomButcherRecipeBuilder resultStage(
            Collection<? extends ChanceResult> results
    ) {
        Objects.requireNonNull(results, "Chance result stage cannot be null");

        if (results.isEmpty()) {
            throw new IllegalArgumentException(
                    "Chance result stage cannot be empty"
            );
        }

        NonNullList<ChanceResult> stage = NonNullList.create();

        for (ChanceResult result : results) {
            stage.add(
                    Objects.requireNonNull(
                            result,
                            "Chance result cannot be null"
                    )
            );
        }

        this.chanceResults.add(stage);
        return this;
    }

    public CustomButcherRecipeBuilder resultStage(ChanceResult... results) {
        Objects.requireNonNull(results, "Chance results cannot be null");
        return resultStage(Arrays.asList(results));
    }

    public void saveHook(Consumer<FinishedRecipe> output) {
        validate();

        output.accept(new HookResult(
                idIn,
                groupIn,
                carcass,
                copyTools(recipeToolsIn),
                copyIngredients(dummyList),
                copyChanceResults(chanceResults)
        ));
    }

    public void saveHook(
            Consumer<FinishedRecipe> output,
            ResourceLocation id
    ) {
        id(id);
        saveHook(output);
    }

    public void saveButcherBlock(Consumer<FinishedRecipe> output) {
        validate();

        output.accept(new ButcherResult(
                idIn,
                groupIn,
                carcass,
                copyTools(recipeToolsIn),
                copyIngredients(dummyList),
                copyChanceResults(chanceResults)
        ));
    }

    public void saveButcherBlock(
            Consumer<FinishedRecipe> output,
            ResourceLocation id
    ) {
        id(id);
        saveButcherBlock(output);
    }

    private void validate() {
        if (idIn == null) {
            throw new IllegalStateException(
                    "Custom butcher recipe has no id"
            );
        }

        if (carcass == Ingredient.EMPTY) {
            throw new IllegalStateException(
                    "Custom butcher recipe " + idIn + " has no carcass"
            );
        }

        if (recipeToolsIn.isEmpty()) {
            throw new IllegalStateException(
                    "Custom butcher recipe " + idIn + " has no tools"
            );
        }

        if (chanceResults.isEmpty()) {
            throw new IllegalStateException(
                    "Custom butcher recipe " + idIn + " has no result stages"
            );
        }
    }

    private static NonNullList<AnimatedRecipeItemUse> copyTools(
            NonNullList<AnimatedRecipeItemUse> source
    ) {
        NonNullList<AnimatedRecipeItemUse> copy = NonNullList.create();
        copy.addAll(source);
        return copy;
    }

    private static NonNullList<Ingredient> copyIngredients(
            NonNullList<Ingredient> source
    ) {
        NonNullList<Ingredient> copy = NonNullList.create();
        copy.addAll(source);
        return copy;
    }

    private static NonNullList<NonNullList<ChanceResult>> copyChanceResults(
            NonNullList<NonNullList<ChanceResult>> source
    ) {
        NonNullList<NonNullList<ChanceResult>> copy = NonNullList.create();

        for (NonNullList<ChanceResult> stage : source) {
            NonNullList<ChanceResult> stageCopy = NonNullList.create();
            stageCopy.addAll(stage);
            copy.add(stageCopy);
        }

        return copy;
    }

    public CustomButcherRecipeBuilder carcass(@NotNull Item item) {
        carcass(Ingredient.of(item));
        return this;
    }

    public abstract static class AbstractResult implements FinishedRecipe {
        private final ResourceLocation idIn;
        private final String groupIn;
        private final Ingredient carcass;
        private final NonNullList<AnimatedRecipeItemUse> recipeToolsIn;
        private final NonNullList<Ingredient> dummyList;
        private final NonNullList<NonNullList<ChanceResult>> chanceResults;

        protected AbstractResult(
                ResourceLocation idIn,
                String groupIn,
                Ingredient carcass,
                NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
                NonNullList<Ingredient> dummyList,
                NonNullList<NonNullList<ChanceResult>> chanceResults
        ) {
            this.idIn = idIn;
            this.groupIn = groupIn;
            this.carcass = carcass;
            this.recipeToolsIn = recipeToolsIn;
            this.dummyList = dummyList;
            this.chanceResults = chanceResults;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!groupIn.isEmpty()) {
                json.addProperty("group", groupIn);
            }

            json.add("carcass", carcass.toJson());

            JsonArray toolsJson = new JsonArray();

            for (AnimatedRecipeItemUse tool : recipeToolsIn) {
                toolsJson.add(AnimatedRecipeItemUse.addProperty(tool));
            }

            json.add("tools", toolsJson);

            JsonArray jeiJson = new JsonArray();

            for (Ingredient ingredient : dummyList) {
                jeiJson.add(ingredient.toJson());
            }

            json.add("jei", jeiJson);

            JsonArray resultsJson = new JsonArray();

            for (NonNullList<ChanceResult> stage : chanceResults) {
                JsonArray stageJson = new JsonArray();

                for (ChanceResult result : stage) {
                    stageJson.add(result.serialize());
                }

                resultsJson.add(stageJson);
            }

            json.add("results", resultsJson);
        }

        @Override
        public ResourceLocation getId() {
            return idIn;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }

    public static class HookResult extends AbstractResult {
        protected HookResult(
                ResourceLocation idIn,
                String groupIn,
                Ingredient carcass,
                NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
                NonNullList<Ingredient> dummyList,
                NonNullList<NonNullList<ChanceResult>> chanceResults
        ) {
            super(
                    idIn,
                    groupIn,
                    carcass,
                    recipeToolsIn,
                    dummyList,
                    chanceResults
            );
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SBRecipes.CUSTOM_MEAT_HOOK_SERIALIZER.get();
        }
    }

    public static class ButcherResult extends AbstractResult {
        protected ButcherResult(
                ResourceLocation idIn,
                String groupIn,
                Ingredient carcass,
                NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
                NonNullList<Ingredient> dummyList,
                NonNullList<NonNullList<ChanceResult>> chanceResults
        ) {
            super(
                    idIn,
                    groupIn,
                    carcass,
                    recipeToolsIn,
                    dummyList,
                    chanceResults
            );
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SBRecipes.CUSTOM_BUTCHER_BLOCK_SERIALIZER.get();
        }
    }
}