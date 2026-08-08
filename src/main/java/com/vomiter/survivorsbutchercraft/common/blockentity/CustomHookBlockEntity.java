package com.vomiter.survivorsbutchercraft.common.blockentity;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CustomHookBlockEntity extends MeatHookBlockEntity {
    public CustomHookBlockEntity(BlockPos pPos, BlockState pState) {
        super(pPos, pState);
    }


}
