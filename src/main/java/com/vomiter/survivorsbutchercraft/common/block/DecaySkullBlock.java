package com.vomiter.survivorsbutchercraft.common.block;

import com.vomiter.survivorsbutchercraft.common.blockentity.DecaySkullLikeBlockEntity;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DecaySkullBlock extends SkullLikeBlock{
    public DecaySkullBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new DecaySkullLikeBlockEntity(p_153215_, p_153216_);
    }

    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        var result = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);

        if(blockEntity instanceof DecaySkullLikeBlockEntity decay){
            if (result.removeIf(item -> ItemStack.isSameItem(item, decay.getStack()))){
                result.add(decay.getStack());
            }
        }
        return result;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (isPreservative(player.getItemInHand(hand)) && level.getBlockEntity(pos) instanceof DecaySkullLikeBlockEntity decay){
            Optional.ofNullable(FoodCapability.get(decay.getStack())).ifPresent(food -> {
                if(decay.setPreserved() && !player.getAbilities().instabuild)
                    player.getItemInHand(hand).shrink(1);
            });
            return InteractionResult.sidedSuccess(level.isClientSide());

        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

}
