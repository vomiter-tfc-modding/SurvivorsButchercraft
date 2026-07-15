package com.vomiter.survivorsbutchercraft.common;

import com.lance5057.butchercraft.ButchercraftMobEffects;
import com.lance5057.butchercraft.effects.SoapableMobEffect;
import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.mixin.MobEffectInstanceAccessor;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public final class SBForgeEvents {

    public static void init(){
        MinecraftForge.EVENT_BUS.addListener(SBForgeEvents::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(SBForgeEvents::onLivingDrops);
        MinecraftForge.EVENT_BUS.addListener(SBForgeEvents::onPlayerTick);

    }

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
    }

    static List<RegistryObject<? extends SoapableMobEffect>> BLOODY_EFFECTS = List.of(
            ButchercraftMobEffects.DIRTY,
            ButchercraftMobEffects.BLOODY,
            ButchercraftMobEffects.BLOODTRAIL,
            ButchercraftMobEffects.STINKY
    );
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
        if(player.tickCount % 20 != 0) return;
        if(player.isInFluidType(TFCFluids.RIVER_WATER.get().getFluidType())){
            BLOODY_EFFECTS.forEach(registryObject -> {
                var effect = registryObject.get();
                var effectInst = player.getEffect(effect);
                if (effectInst instanceof MobEffectInstanceAccessor acc){
                    for (int i = 0; i < 40; i++) {
                        acc.sb$tickDownDuration();
                    }
                }
            });
        }
    }

    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof TFCAnimalProperties props)) {
            return;
        }
        var id = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
        if(id == null) return;

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
                CarcassDataHelper.writeFromTFCAnimal(drop, props, id);
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