package com.vomiter.survivorsbutchercraft;

import com.mojang.logging.LogUtils;
import com.vomiter.survivorsbutchercraft.client.HookTransformReloadListener;
import com.vomiter.survivorsbutchercraft.core.registry.SBBlockEntityTypes;
import com.vomiter.survivorsbutchercraft.core.registry.SBBlocks;
import com.vomiter.survivorsbutchercraft.core.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.SBDataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SurvivorsButchercraft.MODID)
public class SurvivorsButchercraft
{
    public static final String MODID = "survivorsbutchercraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivorsButchercraft(FMLJavaModLoadingContext context)
    {
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(SBDataGenerator::gatherData);
        SBItems.ITEMS.register(modBus);
        SBBlocks.BLOCKS.register(modBus);
        SBBlockEntityTypes.BLOCK_ENTITIES.register(modBus);

        if(FMLEnvironment.dist == Dist.CLIENT){
            modBus.addListener(HookTransformReloadListener::onAddReloadListener);
        }
    }
}
