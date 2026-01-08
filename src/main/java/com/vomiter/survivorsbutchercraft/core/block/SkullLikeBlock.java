package com.vomiter.survivorsbutchercraft.core.block;

import com.vomiter.survivorsbutchercraft.core.blockentity.SkullLikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkullLikeBlock extends BaseEntityBlock {
    public static final IntegerProperty ROTATION = SkullBlock.ROTATION; // 直接沿用 0..15

    public SkullLikeBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(ROTATION);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SkullLikeBlockEntity(pos, state);
    }
}
