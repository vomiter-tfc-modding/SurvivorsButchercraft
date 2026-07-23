package com.vomiter.survivorsbutchercraft.compat;

import com.lance5057.butchercraft.ButchercraftItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import org.labellum.mc.waterflasks.setup.Registration;

public class WaterFlaskCompat {
    public static void addBladder(ObjectArrayList<ItemStack> list){
        if (list.stream().anyMatch(item -> item.is(ButchercraftItems.KIDNEY.get()))){
            list.add(Registration.BLADDER.get().getDefaultInstance());
        }
    }
}
