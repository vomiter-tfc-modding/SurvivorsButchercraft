package com.vomiter.survivorsbutchercraft.common.block;

import com.vomiter.survivorsbutchercraft.common.blockentity.SkullLikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkullLikeBlock extends AbstractSkullBlock {
    public static final IntegerProperty ROTATION = SkullBlock.ROTATION; // 直接沿用 0..15
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                 @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                   @NotNull BlockPos pos) {
        return SHAPE;
    }


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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // 0~15，跟原版頭顱一樣把玩家水平角映射到 16 段
        int rot = (int) Math.floor((ctx.getRotation() * 16.0F / 360.0F) + 0.5D) & 15;
        return this.defaultBlockState().setValue(ROTATION, rot);
    }

}
