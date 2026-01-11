package com.vomiter.survivorsbutchercraft.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSkullBlock  extends BaseEntityBlock {
    protected AbstractSkullBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return false;
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        // 跟 skull 一樣，交給 BlockEntityRenderer 畫
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, net.minecraft.world.level.@NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 0;
    }


    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }


    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }



}
