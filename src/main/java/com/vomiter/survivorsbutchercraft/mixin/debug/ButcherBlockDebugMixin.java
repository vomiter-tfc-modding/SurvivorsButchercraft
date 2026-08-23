package com.vomiter.survivorsbutchercraft.mixin.debug;

import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlock;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ButcherBlockBlock.class)
public abstract class ButcherBlockDebugMixin {

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(
            method = "useItemOn",
            at = @At("HEAD")
    )
    private void survivorsbutchercraft$debugUseItemOn(
            ItemStack heldMain,
            BlockState state,
            Level world,
            BlockPos blockPos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<ItemInteractionResult> cir
    ) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        LOGGER.info(
                """
                
                [MeatHook Debug] === useItemOn START ===
                side          = {}
                pos           = {}
                blockState    = {}
                dummy         = {}
                hand          = {}
                crouching     = {}
                heldItem      = {}
                blockEntity   = {}
                """,
                world.isClientSide ? "CLIENT" : "SERVER",
                blockPos,
                state,
                state.getValue(ButcherBlockBlock.DUMMY),
                hand,
                player.isCrouching(),
                describeStack(heldMain),
                blockEntity == null ? "null" : blockEntity.getClass().getName()
        );
    }

    @WrapOperation(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/workstations/butcherblock/ButcherBlockBlockEntity;isEmpty()Z"
            )
    )
    private boolean survivorsbutchercraft$debugIsEmpty(
            ButcherBlockBlockEntity instance, Operation<Boolean> original
    ) {
        boolean result = original.call(instance);

        LOGGER.info(
                "[MeatHook Debug] te.isEmpty() = {}, inventory = {}",
                result,
                describeStack(instance.getHandler().getStackInSlot(0))
        );

        return result;
    }

    @WrapOperation(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/workstations/butcherblock/ButcherBlockBlock;isEmptyAbove(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean survivorsbutchercraft$debugIsEmptyBelow(
            ButcherBlockBlock instance,
            Level level,
            BlockPos pos,
            Operation<Boolean> original
    ) {
        boolean result = original.call(instance, level, pos);

        LOGGER.info(
                """
                [MeatHook Debug] isEmptyBelow = {}
                  above(1) = {}
                  above(2) = {}
                """,
                result,
                level.getBlockState(pos.above()),
                level.getBlockState(pos.above(2))
        );

        return result;
    }

    @Inject(
            method = "useItemOn",
            at = @At("RETURN")
    )
    private void survivorsbutchercraft$debugUseItemOnReturn(
            ItemStack heldMain,
            BlockState state,
            Level world,
            BlockPos blockPos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<ItemInteractionResult> cir
    ) {
        LOGGER.info(
                """
                [MeatHook Debug] === useItemOn END ===
                side       = {}
                result     = {}
                heldItem   = {}
                """,
                world.isClientSide ? "CLIENT" : "SERVER",
                cir.getReturnValue(),
                describeStack(heldMain)
        );
    }

    private static String describeStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return "EMPTY";
        }

        return BuiltInRegistries.ITEM.getKey(stack.getItem())
                + " x" + stack.getCount()
                + " components=" + stack.getComponents();
    }
}