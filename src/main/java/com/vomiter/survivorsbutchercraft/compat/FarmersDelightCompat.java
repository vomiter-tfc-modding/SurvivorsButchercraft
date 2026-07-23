package com.vomiter.survivorsbutchercraft.compat;

import com.lance5057.butchercraft.ButchercraftItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import org.labellum.mc.waterflasks.setup.Registration;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightCompat {
    public static void addHam(ObjectArrayList<ItemStack> list){
        if (list.stream().anyMatch(item -> item.is(ButchercraftItems.PORK_ROAST.get()))){
            list.add(ModItems.HAM.get().getDefaultInstance().copyWithCount(2));
        }
    }

}
