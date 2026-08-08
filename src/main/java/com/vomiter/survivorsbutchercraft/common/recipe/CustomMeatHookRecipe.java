package com.vomiter.survivorsbutchercraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.ButchercraftRecipeSerializers;
import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipeContainer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vomiter.survivorsbutchercraft.common.registry.SBRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CustomMeatHookRecipe implements Recipe<HookRecipeContainer>, IButcherRecipe{
    private final HookRecipe hookRecipe;
    private final NonNullList<NonNullList<CompoundChanceResult>> chanceResults;

    public CustomMeatHookRecipe(
            String groupIn,
            Ingredient carcassIn,
            NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
            NonNullList<Ingredient> JEI,
            NonNullList<NonNullList<CompoundChanceResult>> chanceResults
            ) {
        hookRecipe = new HookRecipe (groupIn, carcassIn, recipeToolsIn, JEI);
        this.chanceResults = chanceResults;
    }

    public HookRecipe getHookRecipe(){
        return hookRecipe;
    }

    public boolean matches(HookRecipeContainer pContainer, Level pLevel) {
        return hookRecipe.carcass().test(pContainer.getInsertedItem());
    }

    public ItemStack assemble(HookRecipeContainer pContainer, HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    public String getGroup() {
        return hookRecipe.group();
    }

    public RecipeType<?> getType() {
        return SBRecipes.CUSTOM_MEAT_HOOK.get();
    }


    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public NonNullList<CompoundChanceResult> getResults(int stage) {
        return chanceResults.get(stage);
    }

    @Override
    public Ingredient getButcheringTool(int stage) {
        return hookRecipe.tools().get(stage).tool();
    }

    @Override
    public AnimatedRecipeItemUse getButcheringToolStage(int stage) {
        return hookRecipe.tools().get(stage);
    }

    public static class Serializer implements RecipeSerializer<CustomMeatHookRecipe> {
        public static Serializer INSTANCE = new Serializer();

        public static final MapCodec<CustomMeatHookRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Codec.STRING
                                .optionalFieldOf("group", "")
                                .forGetter(CustomMeatHookRecipe::getGroup),

                        Ingredient.CODEC_NONEMPTY
                                .fieldOf("carcass")
                                .forGetter(recipe -> recipe.hookRecipe.carcass()),

                        NonNullList.codecOf(AnimatedRecipeItemUse.CODEC)
                                .fieldOf("tools")
                                .forGetter(recipe -> recipe.hookRecipe.tools()),

                        NonNullList.codecOf(Ingredient.CODEC_NONEMPTY)
                                .fieldOf("jei")
                                .forGetter(recipe -> recipe.hookRecipe.jei()),

                        NonNullList.codecOf(
                                        NonNullList.codecOf(CompoundChanceResult.CODEC)
                                )
                                .fieldOf("results")
                                .forGetter(recipe -> recipe.chanceResults)
                ).apply(instance, CustomMeatHookRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CustomMeatHookRecipe>
                STREAM_CODEC = StreamCodec.of(
                CustomMeatHookRecipe.Serializer::write,
                CustomMeatHookRecipe.Serializer::read
        );

        @Override
        public MapCodec<CustomMeatHookRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CustomMeatHookRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CustomMeatHookRecipe read(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();

            Ingredient carcass =
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

            int toolCount = buffer.readVarInt();
            NonNullList<AnimatedRecipeItemUse> tools =
                    NonNullList.withSize(toolCount, AnimatedRecipeItemUse.EMPTY);

            tools.replaceAll(
                    ignored -> AnimatedRecipeItemUse.STREAM_CODEC.decode(buffer)
            );

            int jeiCount = buffer.readVarInt();
            NonNullList<Ingredient> jei =
                    NonNullList.withSize(jeiCount, Ingredient.EMPTY);

            jei.replaceAll(
                    ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
            );

            int stageCount = buffer.readVarInt();
            NonNullList<NonNullList<CompoundChanceResult>> chanceResults =
                    NonNullList.create();

            for (int stage = 0; stage < stageCount; stage++) {
                int resultCount = buffer.readVarInt();

                NonNullList<CompoundChanceResult> stageResults =
                        NonNullList.withSize(
                                resultCount,
                                CompoundChanceResult.EMPTY
                        );

                stageResults.replaceAll(
                        ignored -> CompoundChanceResult.STREAM_CODEC.decode(
                                buffer
                        )
                );

                chanceResults.add(stageResults);
            }

            return new CustomMeatHookRecipe(
                    group,
                    carcass,
                    tools,
                    jei,
                    chanceResults
            );
        }

        private static void write(
                RegistryFriendlyByteBuf buffer,
                CustomMeatHookRecipe recipe
        ) {
            buffer.writeUtf(recipe.getGroup());

            Ingredient.CONTENTS_STREAM_CODEC.encode(
                    buffer,
                    recipe.hookRecipe.carcass()
            );

            buffer.writeVarInt(recipe.hookRecipe.tools().size());

            recipe.hookRecipe.tools().forEach(
                    tool -> AnimatedRecipeItemUse.STREAM_CODEC.encode(buffer, tool)
            );

            buffer.writeVarInt(recipe.hookRecipe.jei().size());

            recipe.hookRecipe.jei().forEach(
                    ingredient ->
                            Ingredient.CONTENTS_STREAM_CODEC.encode(
                                    buffer,
                                    ingredient
                            )
            );

            buffer.writeVarInt(recipe.chanceResults.size());

            recipe.chanceResults.forEach(stageResults -> {
                buffer.writeVarInt(stageResults.size());

                stageResults.forEach(
                        result -> CompoundChanceResult.STREAM_CODEC.encode(
                                buffer,
                                result
                        )
                );
            });
        }
    }
}
