package com.vomiter.survivorsbutchercraft.common.blockentity;

import com.vomiter.survivorsbutchercraft.common.registry.SBBlockEntityTypes;
import com.vomiter.survivorsbutchercraft.common.registry.SBFoodTraits;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DecaySkullLikeBlockEntity extends BlockEntity {
    ItemStack stack;

    public DecaySkullLikeBlockEntity(BlockPos pos, BlockState state) {
        super(SBBlockEntityTypes.DECAY_SKULL_LIKE.get(), pos, state);
        this.stack = ItemStack.EMPTY;
    }

    public void setStack(ItemStack stack){
        this.stack = stack;
    }

    public ItemStack getStack(){
        return stack;
    }

    public boolean setPreserved(){
        return Optional.ofNullable(FoodCapability.get(stack)).map(iFood -> {
            boolean hasPreserved = iFood.hasTrait(SBFoodTraits.PRESERVED);
            FoodCapability.applyTrait(iFood, SBFoodTraits.PRESERVED);
            return !hasPreserved;
        }).orElse(false);
    }

    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.stack = ItemStack.of(nbt.getCompound("item"));
    }

    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("item", this.stack.save(new CompoundTag()));
    }

}
