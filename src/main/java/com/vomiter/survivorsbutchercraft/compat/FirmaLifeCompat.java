package com.vomiter.survivorsbutchercraft.compat;

import com.eerussianguy.firmalife.common.items.FLItems;
import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FirmaLifeCompat {
    public static void addRennet(ObjectArrayList<ItemStack> list){
        if (list.stream().anyMatch(item -> item.is(ButchercraftItems.STOMACH.get()))){
            EntityType<?> animal = ForgeRegistries.ENTITY_TYPES.getValue(CarcassDataHelper.getId(ThreadLocalFlags.carcass.get()));
            if (animal == null) return;
            var dropRennet = TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("firmalife", "drops_rennet"));
            var drop3Rennet = TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("firmalife", "drops_three_rennet"));
            var count = 0;
            if (animal.is(drop3Rennet)) {
                count += 6;
            } else if (animal.is(dropRennet)){
                count += 4;
            }
            if (count > 0) list.add(FLItems.RENNET.get().getDefaultInstance().copyWithCount(count));
        }
    }
}
