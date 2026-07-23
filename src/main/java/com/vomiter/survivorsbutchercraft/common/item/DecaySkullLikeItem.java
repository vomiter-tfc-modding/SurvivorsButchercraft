package com.vomiter.survivorsbutchercraft.common.item;

import com.vomiter.survivorsbutchercraft.common.blockentity.DecaySkullLikeBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class DecaySkullLikeItem extends SkullLikeItem{
    public DecaySkullLikeItem(Block head, Block wallHead, Properties properties, Direction direction) {
        super(head, wallHead, properties, direction);
    }

    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        var item = context.getItemInHand().copyWithCount(1);
        var result = super.place(context);
        if (result.equals(InteractionResult.FAIL)) return result;
        var blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof DecaySkullLikeBlockEntity decay){
            decay.setStack(item);
        }
        return result;
    }
}
