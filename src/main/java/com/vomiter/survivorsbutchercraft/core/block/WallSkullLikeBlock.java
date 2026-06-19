package com.vomiter.survivorsbutchercraft.core.block;

import com.vomiter.survivorsbutchercraft.core.blockentity.SkullLikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WallSkullLikeBlock extends AbstractSkullBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public WallSkullLikeBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SkullLikeBlockEntity(pos, state);
    }

    private static final VoxelShape NORTH = Block.box(4, 4, 8, 12, 12, 16);
    private static final VoxelShape SOUTH = Block.box(4, 4, 0, 12, 12, 8);
    private static final VoxelShape WEST  = Block.box(8, 4, 4, 16, 12, 12);
    private static final VoxelShape EAST  = Block.box(0, 4, 4, 8, 12, 12);

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return shapeByFacing(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                 @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return shapeByFacing(state);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                   @NotNull BlockPos pos) {
        return shapeByFacing(state);
    }

    private static VoxelShape shapeByFacing(BlockState state) {
        Direction dir = state.getValue(WallSkullLikeBlock.FACING); // 下面第2部分會加這個 property
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // 牆上頭顱面向玩家：「點到的面」的反方向
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }


}
