package com.vomiter.survivorsbutchercraft.butchery.tool_alternative;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ToolAlternative {
    private static final Map<Item, Ingredient> TOOL_MAP = new HashMap<>();

    /*
    Not safe and ugly, but I guess it's efficient enough.
     */
    public static Item getIdealTool(Ingredient curTool){
        if(curTool == null) return null;
        for (Item item : TOOL_MAP.keySet()) {
            if(curTool.test(item.getDefaultInstance())) return item;
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
            Map<Metal.Default, RegistryObject<Item>> items
    ) {
        return Ingredient.of(
                items.values()
                        .stream()
                        .map(RegistryObject::get)
                        .map(Item::getDefaultInstance)
        );
    }


    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TOOL_MAP.put(
                    ButchercraftItems.BUTCHER_KNIFE.get(),
                    toIngredient(SBItems.BUTCHER_KNIVES)
            );
            TOOL_MAP.put(
                    ButchercraftItems.SKINNING_KNIFE.get(),
                    toIngredient(SBItems.SKINNING_KNIVES)
            );
            TOOL_MAP.put(
                    ButchercraftItems.BONE_SAW.get(),
                    toIngredient(SBItems.BONESAWS)
            );
            TOOL_MAP.put(
                    ButchercraftItems.GUT_KNIFE.get(),
                    toIngredient(SBItems.GUT_KNIVES)
            );
        });
    }

}
