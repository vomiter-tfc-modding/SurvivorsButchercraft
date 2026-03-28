package com.vomiter.survivorsbutchercraft.common;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.CarcassDataHelper;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SurvivorsButchercraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SBForgeEvents {
    private SBForgeEvents() {
    }

    @SubscribeEvent
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