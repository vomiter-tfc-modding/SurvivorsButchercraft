package com.vomiter.survivorsbutchercraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vomiter.survivorsbutchercraft.common.blockentity.SkullLikeBlockEntity;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SkullLikeRenderer implements BlockEntityRenderer<BlockEntity> {

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                SBBlockEntityTypes.SKULL_LIKE.get(),
                SkullLikeRenderer::new
        );
        event.registerBlockEntityRenderer(
                SBBlockEntityTypes.DECAY_SKULL_LIKE.get(),
                SkullLikeRenderer::new
        );
    }


    private final BlockRenderDispatcher blockRenderer;
    public SkullLikeRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(BlockEntity be, float partialTick, PoseStack pose,
                       @NotNull MultiBufferSource buffers, int packedLight, int packedOverlay) {

        var level = be.getLevel();
        if (level == null) return;

        BlockState state = be.getBlockState();

        pose.pushPose();

        // ---- 位置：先處理牆上/地面 skull 的放置偏移 ----
        boolean isWall = state.hasProperty(HorizontalDirectionalBlock.FACING);
        Direction wallFacing = isWall ? state.getValue(HorizontalDirectionalBlock.FACING) : null;

        if(!isWall) {
            // 地面：先移到方塊中心作為旋轉樞紐
            pose.translate(0.5F, 0.0F, 0.5F);

            int rotSeg = state.getValue(SkullBlock.ROTATION); // 0..15
            float degrees = RotationSegment.convertToDegrees(rotSeg);
            pose.mulPose(Axis.YP.rotationDegrees(- degrees));

            // 旋轉完移回去，避免整個模型也被搬到中心
            pose.translate(-0.5F, 0.0F, -0.5F);
        }
        else {
            pose.translate(0, 0.25, 0);
            if(wallFacing.equals(Direction.SOUTH)) pose.translate(0, 0, -0.25);
            else if(wallFacing.equals(Direction.NORTH)) pose.translate(0, 0, 0.25);
            else if(wallFacing.equals(Direction.EAST)) pose.translate(-0.25, 0, 0);
            else if(wallFacing.equals(Direction.WEST)) pose.translate(0.25, 0, 0);
        }

        // ---- 光照：用世界位置重新取 light
        int light = LevelRenderer.getLightColor(level, be.getBlockPos());
        BakedModel baked = blockRenderer.getBlockModel(state);
        RandomSource rand = RandomSource.create();
        rand.setSeed(state.getSeed(be.getBlockPos()));
        var modelRenderer = blockRenderer.getModelRenderer();
        var modelData = ModelData.EMPTY;

        // 用 BakedModel 自帶的 render types
        for (var rt : baked.getRenderTypes(state, rand, modelData)) {
        }

        var vc = buffers.getBuffer(RenderType.translucent());
        modelRenderer.renderModel(
                pose.last(),
                vc,
                state,
                baked,
                1.0F, 1.0F, 1.0F,
                light,
                packedOverlay,
                modelData,
                RenderType.translucent()
        );

        pose.popPose();
    }
}
