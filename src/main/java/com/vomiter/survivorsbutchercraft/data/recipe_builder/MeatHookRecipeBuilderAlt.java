package com.vomiter.survivorsbutchercraft.data.recipe_builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.ButchercraftRecipeSerializers;
import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class MeatHookRecipeBuilderAlt implements RecipeBuilder {
    private final Ingredient ingredient;
    private final List<AnimatedRecipeItemUse> tools = NonNullList.create();
    private final List<Ingredient> jei = NonNullList.create();
    private final Advancement.Builder advancementBuilder = Builder.advancement();
    private String group;

    public MeatHookRecipeBuilderAlt(Ingredient carcassIn) {
        this.ingredient = carcassIn;
    }

    public static MeatHookRecipeBuilderAlt shapedRecipe(Ingredient resultIn) {
        return new MeatHookRecipeBuilderAlt(resultIn);
    }

    public MeatHookRecipeBuilderAlt tool(Ingredient tool, int count, int uses, boolean damage, ResourceLocation table, BlacklistedModel... model) {
        this.tools.add(new AnimatedRecipeItemUse(uses, tool, count, damage, table, model));
        return this;
    }

    public MeatHookRecipeBuilderAlt tool(Ingredient tool, int uses, boolean damage, ResourceLocation table, BlacklistedModel... model) {
        this.tools.add(new AnimatedRecipeItemUse(uses, tool, 1, damage, table, model));
        return this;
    }

    public MeatHookRecipeBuilderAlt JEIIngredient(Ingredient i) {
        this.jei.add(i);
        return this;
    }

    private void validate(ResourceLocation id) {
        if (this.tools.isEmpty()) {
            throw new IllegalStateException("No toolset is defined for shaped recipe %s!".formatted(id));
        }
    }

    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancementBuilder.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public RecipeBuilder group(String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public Item getResult() {
        return Arrays.asList(this.ingredient.getItems()).get(0).getItem();
    }

    public void save(Consumer<FinishedRecipe> consumerIn, ResourceLocation pRecipeId) {
        this.validate(pRecipeId);
        this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        consumerIn.accept(new Result(pRecipeId, ingredient, this.group == null ? "" : this.group, this.tools, this.jei, this.advancementBuilder, new ResourceLocation(pRecipeId.getNamespace(), "recipes/meat_hook/" + pRecipeId.getPath())));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient carcass;
        private final String group;
        private final List<AnimatedRecipeItemUse> tools;
        private final List<Ingredient> JEI;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation idIn, Ingredient carcassIn, String groupIn, List<AnimatedRecipeItemUse> toolsIn, List<Ingredient> JEI, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn) {
            this.id = idIn;
            this.carcass = carcassIn;
            this.group = groupIn;
            this.JEI = JEI;
            this.advancementBuilder = advancementBuilderIn;
            this.advancementId = advancementIdIn;
            this.tools = toolsIn;
        }

        public void serializeRecipeData(JsonObject json) {
            json.addProperty("group", this.group);
            JsonArray stepArray = new JsonArray();
            var var10000 = this.tools.stream().map(AnimatedRecipeItemUse::addProperty);
            Objects.requireNonNull(stepArray);
            var10000.forEach(stepArray::add);
            json.add("tools", stepArray);
            json.add("carcass", this.carcass.toJson());
            JsonArray jei = new JsonArray();
            this.JEI.stream().forEach((i) -> jei.add(i.toJson()));
            json.add("jei", jei);
        }

        public RecipeSerializer<?> getType() {
            return ButchercraftRecipeSerializers.HOOK_SERIALIZER.get();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
