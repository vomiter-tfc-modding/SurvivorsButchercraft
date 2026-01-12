package com.vomiter.survivorsbutchercraft.core.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeItem;

public class SkullLikeItem extends StandingAndWallBlockItem implements IForgeItem {
    public SkullLikeItem(Block head, Block wallHead, Properties properties, Direction direction) {
        super(head, wallHead, properties, direction);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }


}
