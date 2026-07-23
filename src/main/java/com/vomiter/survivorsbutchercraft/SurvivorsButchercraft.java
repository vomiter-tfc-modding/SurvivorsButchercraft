package com.vomiter.survivorsbutchercraft;

import com.mojang.logging.LogUtils;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.client.SBClientForgeEvents;
import com.vomiter.survivorsbutchercraft.client.SBClientModEvents;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.common.SBForgeEvents;
import com.vomiter.survivorsbutchercraft.common.ingredient.NotPreserved;
import com.vomiter.survivorsbutchercraft.common.registry.*;
import com.vomiter.survivorsbutchercraft.data.SBDataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SurvivorsButchercraft.MODID)
public class SurvivorsButchercraft
{
    //TODO: make unsheared wool animals drop wool when killed with butcher knife (mixin)
    //TODO: test butcher process of all animals
    /** TODO
     * - better recipe for butcher's suit (armor)
     * - TFC metal/plank meat hook variants
     * - Bone saw, skinning knife, gutting knife variants
     */

    public static final String MODID = "survivorsbutchercraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivorsButchercraft(FMLJavaModLoadingContext context)
    {
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(SBDataGenerator::gatherData);
        SBItems.ITEMS.register(modBus);
        SBBlocks.BLOCKS.register(modBus);
        modBus.addListener(SBBlocks::onCommonSetup);
        SBBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        SBCreativeTab.TABS.register(modBus);
        modBus.addListener(ToolAlternative::setUp);
        modBus.addListener(this::onCommonSetup);
        SBForgeEvents.init();

        if(FMLEnvironment.dist == Dist.CLIENT){
            SBClientModEvents.init(modBus);
            SBClientForgeEvents.init(modBus);
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SBFoodTraits.bootstrap();
            CraftingHelper.register(Helpers.id("not_preserved"), NotPreserved.SERIALIZER);
        });
    }

}
