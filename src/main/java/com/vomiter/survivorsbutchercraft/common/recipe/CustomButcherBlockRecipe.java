package com.vomiter.survivorsbutchercraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.ButchercraftRecipeSerializers;
import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockContainer;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
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

public class CustomButcherBlockRecipe implements Recipe<ButcherBlockContainer>, IButcherRecipe{
    private final NonNullList<NonNullList<CompoundChanceResult>> chanceResults;
    private final ButcherBlockRecipe butcherBlockRecipe;

    public CustomButcherBlockRecipe(
            String groupIn,
            Ingredient carcassIn,
            NonNullList<AnimatedRecipeItemUse> recipeToolsIn,
            NonNullList<Ingredient> JEI,
            NonNullList<NonNullList<CompoundChanceResult>> chanceResults
            ) {
        this.butcherBlockRecipe = new ButcherBlockRecipe(groupIn, carcassIn, recipeToolsIn, JEI);
        this.chanceResults = chanceResults;
    }

    public ButcherBlockRecipe getButcherBlockRecipe(){
        return butcherBlockRecipe;
    }

    public boolean matches(ButcherBlockContainer pContainer, Level pLevel) {
        return butcherBlockRecipe.carcass().test(pContainer.getInsertedItem());
    }

    public ItemStack assemble(ButcherBlockContainer pContainer, HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    public String getGroup() {
        return this.butcherBlockRecipe.group();
    }

    public RecipeType<?> getType() {
        return SBRecipes.CUSTOM_BUTCHER_BLOCK.get();
    }

    public NonNullList<CompoundChanceResult> getResults(int stage){
        return chanceResults.get(stage);
    }

    @Override
    public Ingredient getButcheringTool(int stage) {
        return butcherBlockRecipe.tools().get(stage).tool();
    }

    @Override
    public AnimatedRecipeItemUse getButcheringToolStage(int stage) {
        return butcherBlockRecipe.tools().get(stage);
    }

    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<CustomButcherBlockRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<CustomButcherBlockRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Codec.STRING
                                .optionalFieldOf("group", "")
                                .forGetter(CustomButcherBlockRecipe::getGroup),

                        Ingredient.CODEC_NONEMPTY
                                .fieldOf("carcass")
                                .forGetter(recipe -> recipe.butcherBlockRecipe.carcass()),

                        NonNullList.codecOf(AnimatedRecipeItemUse.CODEC)
                                .fieldOf("tools")
                                .forGetter(recipe -> recipe.butcherBlockRecipe.tools()),

                        NonNullList.codecOf(Ingredient.CODEC_NONEMPTY)
                                .fieldOf("jei")
                                .forGetter(recipe -> recipe.butcherBlockRecipe.jei()),

                        NonNullList.codecOf(
                                        NonNullList.codecOf(CompoundChanceResult.CODEC)
                                )
                                .fieldOf("results")
                                .forGetter(recipe -> recipe.chanceResults)
                ).apply(instance, CustomButcherBlockRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CustomButcherBlockRecipe>
                STREAM_CODEC = StreamCodec.of(
                Serializer::write,
                Serializer::read
        );

        @Override
        public MapCodec<CustomButcherBlockRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CustomButcherBlockRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CustomButcherBlockRecipe read(RegistryFriendlyByteBuf buffer) {
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

            return new CustomButcherBlockRecipe(
                    group,
                    carcass,
                    tools,
                    jei,
                    chanceResults
            );
        }

        private static void write(
                RegistryFriendlyByteBuf buffer,
                CustomButcherBlockRecipe recipe
        ) {
            buffer.writeUtf(recipe.getGroup());

            Ingredient.CONTENTS_STREAM_CODEC.encode(
                    buffer,
                    recipe.butcherBlockRecipe.carcass()
            );

            buffer.writeVarInt(recipe.butcherBlockRecipe.tools().size());

            recipe.butcherBlockRecipe.tools().forEach(
                    tool -> AnimatedRecipeItemUse.STREAM_CODEC.encode(buffer, tool)
            );

            buffer.writeVarInt(recipe.butcherBlockRecipe.jei().size());

            recipe.butcherBlockRecipe.jei().forEach(
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
