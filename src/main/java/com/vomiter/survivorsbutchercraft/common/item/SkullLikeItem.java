package com.vomiter.survivorsbutchercraft.common.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class SkullLikeItem extends StandingAndWallBlockItem implements IItemExtension {
    public SkullLikeItem(Block head, Block wallHead, Properties properties, Direction direction) {
        super(head, wallHead, properties, direction);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }


}
