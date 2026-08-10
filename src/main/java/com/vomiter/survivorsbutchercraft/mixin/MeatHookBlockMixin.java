package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MeatHookBlock.class)
public class MeatHookBlockMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;ofFullCopy(Lnet/minecraft/world/level/block/state/BlockBehaviour;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"))
    private static BlockBehaviour.Properties sbtfc$getMeatHookProperties(BlockBehaviour blockBehaviour, Operation<BlockBehaviour.Properties> original){
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3F, 6.0F).noOcclusion();
    }
}
