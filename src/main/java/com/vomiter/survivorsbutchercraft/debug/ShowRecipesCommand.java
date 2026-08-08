package com.vomiter.survivorsbutchercraft.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

public final class ShowRecipesCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    private ShowRecipesCommand() {
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("show_recipes")
                        .requires(source -> source.hasPermission(2))
                        .then(
                                Commands.argument(
                                                "recipe_type",
                                                ResourceLocationArgument.id()
                                        )
                                        .suggests((context, builder) ->
                                                SharedSuggestionProvider.suggestResource(
                                                        BuiltInRegistries.RECIPE_TYPE.keySet(),
                                                        builder
                                                )
                                        )
                                        .executes(context -> {
                                            ResourceLocation typeId =
                                                    ResourceLocationArgument.getId(
                                                            context,
                                                            "recipe_type"
                                                    );

                                            return showRecipes(
                                                    context.getSource(),
                                                    typeId
                                            );
                                        })
                        )
        );
    }

    private static int showRecipes(
            CommandSourceStack source,
            ResourceLocation typeId
    ) {
        if (!BuiltInRegistries.RECIPE_TYPE.containsKey(typeId)) {
            source.sendFailure(
                    net.minecraft.network.chat.Component.literal(
                            "Unknown recipe type: " + typeId
                    )
            );
            return 0;
        }

        RecipeType<?> recipeType =
                BuiltInRegistries.RECIPE_TYPE.get(typeId);

        RecipeManager recipeManager =
                source.getServer().getRecipeManager();

        List<RecipeHolder<?>> recipes = recipeManager.getRecipes()
                .stream()
                .filter(holder -> holder.value().getType() == recipeType)
                .sorted(Comparator.comparing(
                        holder -> holder.id().toString()
                ))
                .toList();

        LOGGER.info(
                "===== Recipes of type {} ({}) =====",
                typeId,
                recipes.size()
        );

        for (RecipeHolder<?> recipe : recipes) {
            LOGGER.info("{}", recipe.id());
        }

        LOGGER.info(
                "===== End recipes of type {} =====",
                typeId
        );

        source.sendSuccess(
                () -> net.minecraft.network.chat.Component.literal(
                        "Logged " + recipes.size()
                                + " recipes of type " + typeId
                ),
                false
        );

        return recipes.size();
    }
}