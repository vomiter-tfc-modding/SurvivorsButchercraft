package com.vomiter.survivorsbutchercraft.common.recipe;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.vomiter.survivorsbutchercraft.data.loot.SBButcherBlockLootTables;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomButcherRecipeBuilder {
    private ResourceLocation idIn;
    private String groupIn = "";
    private Ingredient carcass = Ingredient.EMPTY;

    private final NonNullList<AnimatedRecipeItemUse> recipeToolsIn =
            NonNullList.create();

    private final NonNullList<Ingredient> dummyList =
            NonNullList.create();

    private final NonNullList<NonNullList<CompoundChanceResult>> chanceResults =
            NonNullList.create();

    public static CustomButcherRecipeBuilder builder() {
        return new CustomButcherRecipeBuilder();
    }

    public CustomButcherRecipeBuilder id(ResourceLocation id) {
        this.idIn = Objects.requireNonNull(
                id,
                "Recipe id cannot be null"
        );

        return this;
    }

    public CustomButcherRecipeBuilder group(String group) {
        this.groupIn = Objects.requireNonNull(
                group,
                "Recipe group cannot be null"
        );

        return this;
    }

    public CustomButcherRecipeBuilder carcass(Ingredient carcass) {
        this.carcass = Objects.requireNonNull(
                carcass,
                "Carcass ingredient cannot be null"
        );

        return this;
    }

    public CustomButcherRecipeBuilder carcass(@NotNull Item item) {
        return carcass(Ingredient.of(item));
    }

    public CustomButcherRecipeBuilder tool(
            AnimatedRecipeItemUse tool
    ) {
        this.recipeToolsIn.add(
                Objects.requireNonNull(
                        tool,
                        "Recipe tool cannot be null"
                )
        );

        return this;
    }

    public CustomButcherRecipeBuilder tool(
            Ingredient tool,
            int uses,
            boolean damage,
            ResourceLocation table,
            BlacklistedModel... models
    ) {
        return tool(
                new AnimatedRecipeItemUse(
                        uses,
                        tool,
                        1,
                        damage,
                        table,
                        List.of(),
                        List.of(models)
                )
        );
    }

    public CustomButcherRecipeBuilder tool(
            Ingredient tool,
            int uses,
            boolean damage,
            BlacklistedModel... models
    ) {
        return tool(
                tool,
                uses,
                damage,
                SBButcherBlockLootTables.EMPTY,
                models
        );
    }

    public CustomButcherRecipeBuilder tools(
            Collection<? extends AnimatedRecipeItemUse> tools
    ) {
        Objects.requireNonNull(
                tools,
                "Recipe tools cannot be null"
        );

        tools.forEach(this::tool);
        return this;
    }

    public CustomButcherRecipeBuilder jei(
            Ingredient ingredient
    ) {
        this.dummyList.add(
                Objects.requireNonNull(
                        ingredient,
                        "JEI ingredient cannot be null"
                )
        );

        return this;
    }

    public CustomButcherRecipeBuilder jei(
            Collection<? extends Ingredient> ingredients
    ) {
        Objects.requireNonNull(
                ingredients,
                "JEI ingredients cannot be null"
        );

        ingredients.forEach(this::jei);
        return this;
    }

    /**
     * 新增一個 results 外層階段。
     *
     * 對應：
     *
     * "results": [
     *   [resultA, resultB],
     *   [resultC]
     * ]
     */
    public CustomButcherRecipeBuilder resultStage(
            Collection<? extends CompoundChanceResult> results
    ) {
        Objects.requireNonNull(
                results,
                "Chance result stage cannot be null"
        );

        if (results.isEmpty()) {
            throw new IllegalArgumentException(
                    "Chance result stage cannot be empty"
            );
        }

        NonNullList<CompoundChanceResult> stage =
                NonNullList.create();

        for (CompoundChanceResult result : results) {
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

    public CustomButcherRecipeBuilder resultStage(
            CompoundChanceResult... results
    ) {
        Objects.requireNonNull(
                results,
                "Chance results cannot be null"
        );

        return resultStage(Arrays.asList(results));
    }

    public void saveButcherBlock(RecipeOutput output) {
        validate();

        output.accept(
                idIn,
                new CustomButcherBlockRecipe(
                        groupIn,
                        carcass,
                        copyTools(recipeToolsIn),
                        copyIngredients(dummyList),
                        copyChanceResults(chanceResults)
                ),
                null
        );
    }

    public void saveButcherBlock(
            RecipeOutput output,
            ResourceLocation id
    ) {
        id(id);
        saveButcherBlock(output);
    }

    public void saveHook(RecipeOutput output) {
        validate();

        output.accept(
                idIn,
                new CustomMeatHookRecipe(
                        groupIn,
                        carcass,
                        copyTools(recipeToolsIn),
                        copyIngredients(dummyList),
                        copyChanceResults(chanceResults)
                ),
                null
        );
    }

    public void saveHook(
            RecipeOutput output,
            ResourceLocation id
    ) {
        id(id);
        saveHook(output);
    }

    private void validate() {
        if (idIn == null) {
            throw new IllegalStateException(
                    "Custom butcher recipe has no id"
            );
        }

        if (carcass == Ingredient.EMPTY) {
            throw new IllegalStateException(
                    "Custom butcher recipe "
                            + idIn
                            + " has no carcass"
            );
        }

        if (recipeToolsIn.isEmpty()) {
            throw new IllegalStateException(
                    "Custom butcher recipe "
                            + idIn
                            + " has no tools"
            );
        }

        if (chanceResults.isEmpty()) {
            throw new IllegalStateException(
                    "Custom butcher recipe "
                            + idIn
                            + " has no result stages"
            );
        }

        if (recipeToolsIn.size() != chanceResults.size()) {
            throw new IllegalStateException(
                    "Custom butcher recipe "
                            + idIn
                            + " has "
                            + recipeToolsIn.size()
                            + " tool stages but "
                            + chanceResults.size()
                            + " result stages"
            );
        }
    }

    private static NonNullList<AnimatedRecipeItemUse> copyTools(
            NonNullList<AnimatedRecipeItemUse> source
    ) {
        NonNullList<AnimatedRecipeItemUse> copy =
                NonNullList.create();

        copy.addAll(source);
        return copy;
    }

    private static NonNullList<Ingredient> copyIngredients(
            NonNullList<Ingredient> source
    ) {
        NonNullList<Ingredient> copy =
                NonNullList.create();

        copy.addAll(source);
        return copy;
    }

    private static NonNullList<NonNullList<CompoundChanceResult>>
    copyChanceResults(
            NonNullList<NonNullList<CompoundChanceResult>> source
    ) {
        NonNullList<NonNullList<CompoundChanceResult>> copy =
                NonNullList.create();

        for (
                NonNullList<CompoundChanceResult> stage : source
        ) {
            NonNullList<CompoundChanceResult> stageCopy =
                    NonNullList.create();

            stageCopy.addAll(stage);
            copy.add(stageCopy);
        }

        return copy;
    }
}