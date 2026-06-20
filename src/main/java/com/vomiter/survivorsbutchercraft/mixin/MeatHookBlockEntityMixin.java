package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.adapter.MeatHookBucketAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public abstract class MeatHookBlockEntityMixin extends BlockEntity {
    public MeatHookBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir) {
        var adapter = new MeatHookBucketAdapter((MeatHookBlockEntity) (Object) this);
        adapter.acceptFluidHandler(p, butcheringTool, cir);
    }
}
