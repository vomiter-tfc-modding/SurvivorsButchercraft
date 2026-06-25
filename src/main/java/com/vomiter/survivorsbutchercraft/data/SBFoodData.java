package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.butchery.meat.Raw2CookedMap;
import net.dries007.tfc.common.items.Food;

import java.util.Locale;

public class SBFoodData {
    public static void saveFoodData(SDFoodDataProvider provider){
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
