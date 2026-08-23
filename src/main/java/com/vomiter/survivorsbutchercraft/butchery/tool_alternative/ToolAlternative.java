package com.vomiter.survivorsbutchercraft.butchery.tool_alternative;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ToolAlternative {
    private static final Map<Item, Ingredient> TOOL_MAP = new HashMap<>();

    /*
    Not safe and ugly, but I guess it's efficient enough.
     */
    public static Item getIdealTool(Ingredient curTool){
        if(curTool == null) return null;
        for (Item item : TOOL_MAP.keySet()) {
            if(
                    DifferenceIngredient.of(curTool, Ingredient.of(CommonTags.Items.TOOLS_KNIFE))
                    .test(item.getDefaultInstance())) return item;
        }
        return null;
    }

    public static Ingredient getStageTool(Item item){
        for (int i = 0; i < MeatHookStage.values().length; i++) {
            if(MeatHookStage.values()[i].iconicTool().equals(item)){
                return MeatHookStage.values()[i].acceptableTools();
            }
        }
        return Ingredient.EMPTY;
    }

    public static Ingredient getIdealTool(Item item){
        return TOOL_MAP.getOrDefault(item, Ingredient.EMPTY);
    }

    private static Ingredient toIngredient(
            Map<Metal, DeferredHolder<Item, ? extends Item>> items
    ) {
        return Ingredient.of(
                items.values()
                        .stream()
                        .map(Supplier::get)
                        .map(Item::getDefaultInstance)
        );
    }

    public static Map<Item, Item> SB_TOOL_TO_BC_TOOL
            = new HashMap<>();

    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            SB_TOOL_TO_BC_TOOL.put(SBItems.BUTCHER_KNIVES.get(Metal.WROUGHT_IRON).get(), ButchercraftItems.BUTCHER_KNIFE.get());
            TOOL_MAP.put(
                    SBItems.BUTCHER_KNIVES.get(Metal.WROUGHT_IRON).get(),
                    toIngredient(SBItems.BUTCHER_KNIVES)
            );

            SB_TOOL_TO_BC_TOOL.put(SBItems.SKINNING_KNIVES.get(Metal.WROUGHT_IRON).get(), ButchercraftItems.SKINNING_KNIFE.get());
            TOOL_MAP.put(
                    SBItems.SKINNING_KNIVES.get(Metal.WROUGHT_IRON).get(),
                    toIngredient(SBItems.SKINNING_KNIVES)
            );

            SB_TOOL_TO_BC_TOOL.put(SBItems.BONESAWS.get(Metal.WROUGHT_IRON).get(), ButchercraftItems.BONE_SAW.get());
            TOOL_MAP.put(
                    SBItems.BONESAWS.get(Metal.WROUGHT_IRON).get(),
                    toIngredient(SBItems.BONESAWS)
            );

            SB_TOOL_TO_BC_TOOL.put(SBItems.GUT_KNIVES.get(Metal.WROUGHT_IRON).get(), ButchercraftItems.GUT_KNIFE.get());
            TOOL_MAP.put(
                    SBItems.GUT_KNIVES.get(Metal.WROUGHT_IRON).get(),
                    toIngredient(SBItems.GUT_KNIVES)
            );
        });
    }


}
