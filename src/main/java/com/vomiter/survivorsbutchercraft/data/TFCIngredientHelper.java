package com.vomiter.survivorsbutchercraft.data;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.stream.Stream;

public class TFCIngredientHelper {
    static String NOT_ROTTEN = "tfc:not_rotten";
    static class NotRotten extends Ingredient{
        Ingredient delegate;

        protected NotRotten(Stream<? extends Value> p_43907_) {
            super(p_43907_);
        }

        NotRotten of(Ingredient delegate){
            this.delegate = delegate;
            return this;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", NOT_ROTTEN);
            if (this.delegate != null) {
                json.add("ingredient", this.delegate.toJson());
            }

            return json;
        }

    }

    public static Ingredient getNotRotten(Ingredient ingredient){
        return new NotRotten(Stream.of()).of(ingredient);
    }


}
