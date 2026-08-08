package com.vomiter.survivorsbutchercraft;

import com.mojang.logging.LogUtils;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.client.SBClientForgeEvents;
import com.vomiter.survivorsbutchercraft.client.SBClientModEvents;
import com.vomiter.survivorsbutchercraft.common.SBForgeEvents;
import com.vomiter.survivorsbutchercraft.common.registry.*;
import com.vomiter.survivorsbutchercraft.data.SBDataGenerator;
import com.vomiter.survivorsbutchercraft.debug.ShowRecipesCommand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(SurvivorsButchercraft.MODID)
public class SurvivorsButchercraft
{
    public static final String MODID = "survivorsbutchercraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivorsButchercraft(ModContainer mod, IEventBus modBus) {
        modBus.addListener(SBDataGenerator::gatherData);
        SBItems.ITEMS.register(modBus);
        SBBlocks.BLOCKS.register(modBus);
        SBRecipes.RECIPE_TYPES.register(modBus);
        SBRecipes.RECIPE_SERIALIZERS.register(modBus);
        SBFoodTraits.TRAITS.register(modBus);
        modBus.addListener(SBBlocks::onCommonSetup);
        SBBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        SBCreativeTab.TABS.register(modBus);
        SBDataComponents.register(modBus);
        modBus.addListener(ToolAlternative::setUp);
        modBus.addListener(this::onCommonSetup);
        SBForgeEvents.init();
        if(!FMLEnvironment.production){
            NeoForge.EVENT_BUS.addListener(ShowRecipesCommand::onRegisterCommands);
        }

        if(FMLEnvironment.dist == Dist.CLIENT){
            SBClientModEvents.init(modBus);
            SBClientForgeEvents.init(modBus);
            modBus.addListener(this::onClientSetup);
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    public void onClientSetup(FMLClientSetupEvent event){
    }

}