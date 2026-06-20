package com.vomiter.survivorsbutchercraft.common.blockentity;

import com.vomiter.survivorsbutchercraft.common.registry.SBBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkullLikeBlockEntity extends BlockEntity {

    public SkullLikeBlockEntity(BlockPos pos, BlockState state) {
        super(SBBlockEntityTypes.SKULL_LIKE.get(), pos, state);
    }
}
