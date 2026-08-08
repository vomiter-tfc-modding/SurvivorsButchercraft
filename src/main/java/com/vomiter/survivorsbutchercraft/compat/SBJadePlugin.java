package com.vomiter.survivorsbutchercraft.compat;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlock;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@WailaPlugin
public class SBJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration reg) {
        reg.registerBlockComponent(HookProvider.INSTANCE, MeatHookBlock.class);
    }

    public enum HookProvider implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig cfg) {
            var blockEntity = accessor.getBlockEntity();
            if (!(blockEntity instanceof MeatHookBlockEntity decay)) return;

            ItemStack stack = decay.getInsertedItem();
            if (stack.isEmpty()) return;

            tooltip.add(stack.getHoverName());
            List<Component> lines = new ArrayList<>();
            FoodCapability.addTooltipInfo(stack, lines::add);
            lines.forEach(tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return ResourceLocation.fromNamespaceAndPath(SurvivorsButchercraft.MODID, "meat_hook");
        }
    }

}

