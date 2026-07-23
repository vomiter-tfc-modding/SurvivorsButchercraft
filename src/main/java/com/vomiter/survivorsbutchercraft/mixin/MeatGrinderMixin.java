package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.grinder.GrinderBlock;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(GrinderBlock.class)
public class MeatGrinderMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void sbtfc$gateRottenFood(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult blockRayTraceResult, CallbackInfoReturnable<InteractionResult> cir){
        if(world.isClientSide()) return;
        var item = playerEntity.getItemInHand(hand);
        var isRotten = Optional.ofNullable(FoodCapability.get(item)).map(IFood::isRotten).orElse(false);
        if(isRotten){
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
