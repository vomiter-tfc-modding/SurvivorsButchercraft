package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.butchery.meat.Raw2CookedMap;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.dries007.tfc.common.items.Food;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Locale;

public class SBFoodData {
    public static void saveFoodData(SDFoodDataProvider provider){
        provider.newBuilder("large_carcass")
                .ingredient(Ingredient.of(SBTags.Items.LARGE_CARCASS).toJson())
                .setDecay(8)
                .save();

        provider.newBuilder("linked_sausage")
                .ingredient(Ingredient.of(SBTags.Items.LINKED_SAUSAGE).toJson())
                .setDecay(0.7)
                .save();

        provider.newBuilder("raw_blood_sausage")
                .item(ButchercraftItems.BLOOD_SAUSAGE.get())
                .setHunger(2)
                .setProtein(0.3)
                .setDecay(0.7)
                .save();

        provider.newBuilder("raw_sausage")
                .item(ButchercraftItems.SAUSAGE.get())
                .setHunger(2)
                .setProtein(0.2)
                .setDecay(0.7)
                .save();

        provider.newBuilder("cooked_blood_sausage")
                .item(ButchercraftItems.COOKED_BLOOD_SAUSAGE.get())
                .setHunger(3)
                .setProtein(0.7)
                .setSaturation(1)
                .setDecay(0.7)
                .save();

        provider.newBuilder("cooked_sausage")
                .item(ButchercraftItems.COOKED_SAUSAGE.get())
                .setHunger(3)
                .setProtein(0.5)
                .setSaturation(1)
                .setDecay(0.7)
                .save();

        provider.newBuilder("eyeball")
                .item(ButchercraftItems.EYEBALL.get())
                .setHunger(0)
                .setProtein(0)
                .setDecay(5)
                .save();
        provider.newBuilder("offal/cooked_eyeball")
                .item(ButchercraftItems.COOKED_EYEBALL.get())
                .setHunger(1)
                .setProtein(0.1)
                .setDecay(3.0)
                .save();

        provider.newBuilder("offal/brain")
                .item(ButchercraftItems.BRAIN.get())
                .setHunger(3)
                .setProtein(1)
                .setDecay(6)
                .save();
        provider.newBuilder("offal/cooked_brain")
                .item(ButchercraftItems.COOKED_BRAIN.get())
                .setHunger(4)
                .setProtein(1.4)
                .setDairy(0.5)
                .setSaturation(9)
                .setDecay(4.0)
                .save();

        provider.newBuilder("offal/lung")
                .item(ButchercraftItems.LUNG.get())
                .setHunger(1)
                .setProtein(0.8)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/cooked_lung")
                .item(ButchercraftItems.COOKED_LUNG.get())
                .setHunger(3)
                .setProtein(1.2)
                .setSaturation(3)
                .setDecay(2.3)
                .save();
        provider.newBuilder("offal/heart")
                .item(ButchercraftItems.HEART.get())
                .setHunger(2)
                .setProtein(1)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/cooked_heart")
                .item(ButchercraftItems.COOKED_HEART.get())
                .setHunger(4)
                .setProtein(1.4)
                .setSaturation(5)
                .setDecay(2.3)
                .save();
        provider.newBuilder("offal/stomach")
                .item(ButchercraftItems.STOMACH.get())
                .setHunger(2)
                .setProtein(1)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/cooked_stomach")
                .item(ButchercraftItems.COOKED_STOMACH.get())
                .setHunger(4)
                .setProtein(1.4)
                .setSaturation(5)
                .setDecay(2.3)
                .save();
        provider.newBuilder("offal/tripe")
                .item(ButchercraftItems.TRIPE.get())
                .setHunger(2)
                .setProtein(1)
                .setDecay(2.3)
                .save();
        provider.newBuilder("offal/cooked_tripe")
                .item(ButchercraftItems.COOKED_TRIPE.get())
                .setHunger(4)
                .setProtein(1.4)
                .setSaturation(5)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/liver")
                .item(ButchercraftItems.LIVER.get())
                .setHunger(2)
                .setProtein(1)
                .setDecay(2.3)
                .save();
        provider.newBuilder("offal/cooked_liver")
                .item(ButchercraftItems.COOKED_LIVER.get())
                .setHunger(4)
                .setProtein(1.4)
                .setSaturation(5)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/kidney")
                .item(ButchercraftItems.KIDNEY.get())
                .setHunger(2)
                .setProtein(1)
                .setDecay(3.0)
                .save();
        provider.newBuilder("offal/cooked_kidney")
                .item(ButchercraftItems.COOKED_KIDNEY.get())
                .setHunger(3)
                .setProtein(1.1)
                .setSaturation(1)
                .setDecay(2.3)
                .save();


        for (MeatProduct meatProduct : MeatProduct.values()) {
            if(meatProduct.equals(MeatProduct.ORDINARY)) continue;
            for (MeatType type : MeatType.values()) {
                var ordinary = Food.valueOf(type.name());
                var cookedOrdinary = Food.valueOf("COOKED_" + type.name());
                var item = MeatMap.get(type, meatProduct);
                if(item == null) continue;
                var cooked = Raw2CookedMap.get(item);
                var builder = provider.newBuilder(type.name().toLowerCase(Locale.ROOT) + "/" + meatProduct.name().toLowerCase(Locale.ROOT)).item(item);
                var cookedBuilder = provider.newBuilder(type.name().toLowerCase(Locale.ROOT) + "/cooked_" + meatProduct.name().toLowerCase(Locale.ROOT)).item(cooked);
                switch (meatProduct){
                    case ROAST -> {
                        builder.multipliedFrom(ordinary, 2);
                        cookedBuilder.multipliedFrom(cookedOrdinary, 2);
                    }
                    case RIB, TAIL -> {
                        builder.from(ordinary);
                        cookedBuilder.from(cookedOrdinary);
                    }
                    case CUBED -> {
                        builder.slicedFrom(ordinary, 2);
                        cookedBuilder.slicedFrom(cookedOrdinary, 2);
                    }
                    case STEW_MEAT -> {
                        builder.slicedFrom(ordinary, 4);
                        cookedBuilder.slicedFrom(cookedOrdinary, 4);
                    }
                    case SCRAP -> {
                        builder.slicedFrom(ordinary, 3);
                        cookedBuilder.slicedFrom(cookedOrdinary, 3);
                    }
                    case GROUND -> {
                        builder.slicedFrom(ordinary, 6);
                        cookedBuilder.slicedFrom(cookedOrdinary, 6);
                    }
                    default -> {}
                }
                builder.save();
                cookedBuilder.save();
            }
        }
    }

}
