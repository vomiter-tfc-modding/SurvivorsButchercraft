package com.vomiter.survivorsbutchercraft.common;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.butchery.convert.ConvertResultManager;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public final class SBForgeEvents {

    public static void init(){
        MinecraftForge.EVENT_BUS.addListener(SBForgeEvents::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(SBForgeEvents::onLivingDrops);

    }

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ConvertResultManager.INSTANCE);
    }

    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof TFCAnimalProperties props)) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof ButcherKnifeItem)) {
            return;
        }

        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack drop = itemEntity.getItem();
            if (isSBCarcass(drop)) {
                CarcassDataHelper.writeFromTFCAnimal(drop, props);
            }
        }
    }

    private static boolean isSBCarcass(ItemStack stack) {
        for (Carcass carcass : Carcass.values()) {
            if (stack.is(carcass.carcassItem())) {
                return true;
            }
        }
        return false;
    }
}