package com.vomiter.survivorsbutchercraft.common.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.common.registry.SBFoodTraits;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class NotPreserved extends AbstractIngredient {
    public static final Serializer SERIALIZER = new Serializer();

    private final Ingredient ingredient;

    public NotPreserved(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", Helpers.id("not_preserved").toString());
        json.add("ingredient", ingredient.toJson());
        return json;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null) {
            return false;
        }

        var food = FoodCapability.get(stack);

        if (food != null && food.hasTrait(SBFoodTraits.PRESERVED)) {
            return false;
        }

        return ingredient.test(stack);
    }

    public static class Serializer implements IIngredientSerializer<NotPreserved> {
        @Override
        public NotPreserved parse(JsonObject json) {
            JsonElement ingredientJson = GsonHelper.getNonNull(json, "ingredient");

            return new NotPreserved(
                    Ingredient.fromJson(ingredientJson)
            );
        }

        @Override
        public NotPreserved parse(FriendlyByteBuf buffer) {
            return new NotPreserved(
                    Ingredient.fromNetwork(buffer)
            );
        }

        @Override
        public void write(FriendlyByteBuf buffer, NotPreserved ingredient) {
            ingredient.ingredient.toNetwork(buffer);
        }
    }
}