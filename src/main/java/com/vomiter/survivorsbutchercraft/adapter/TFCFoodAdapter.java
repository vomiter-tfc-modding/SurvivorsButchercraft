package com.vomiter.survivorsbutchercraft.adapter;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.world.item.ItemStack;

public class TFCFoodAdapter {
    public static ItemStack copyRotten(ItemStack src, ItemStack destination){
        IFood srcFood = FoodCapability.get(src);
        IFood destFood = FoodCapability.get(destination);
        if(srcFood == null || destFood == null){
            return destination;
        }
        if(srcFood.isRotten()){
            FoodCapability.setRotten(destination);
        }
        return destination;
    }
}
