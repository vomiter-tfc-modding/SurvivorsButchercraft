package com.vomiter.survivorsbutchercraft;

import com.mojang.logging.LogUtils;
import com.vomiter.survivorsbutchercraft.client.SBClientModEvents;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.common.SBForgeEvents;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlockEntityTypes;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.SBDataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SurvivorsButchercraft.MODID)
public class SurvivorsButchercraft
{
    //TODO: add butcher tools for different metal tiers
    /*
    *TODO: add butcher modifier data manager
    * 0. ingredient (tool predicate)
    * 1. result convert: {from, to, weight}
    * 2. tool action: extra durability damage
     */

    public static final String MODID = "survivorsbutchercraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivorsButchercraft(FMLJavaModLoadingContext context)
    {
        MeatHookStage.values();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(SBDataGenerator::gatherData);
        SBItems.ITEMS.register(modBus);
        SBBlocks.BLOCKS.register(modBus);
        SBBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        SBForgeEvents.init();

        if(FMLEnvironment.dist == Dist.CLIENT){
            SBClientModEvents.init(modBus);
        }
    }
}
