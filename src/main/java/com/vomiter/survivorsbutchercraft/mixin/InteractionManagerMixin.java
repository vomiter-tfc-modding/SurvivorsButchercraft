package com.vomiter.survivorsbutchercraft.mixin;

import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.ScrapingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.recipes.ScrapingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.InteractionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = InteractionManager.class, remap = false)
public class InteractionManagerMixin {
    @Shadow
    public static void register(Ingredient item, boolean targetAir, InteractionManager.OnItemUseAction action) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "registerDefaultInteractions", at = @At("TAIL"))
    private static void sbtfc$registerButcherScrapable(CallbackInfo ci){

        register(Ingredient.of(SBTags.Items.BUTCHER_SCRAPABLE), false, (stack, context) -> {
            Level level = context.getLevel();
            ScrapingRecipe recipe = ScrapingRecipe.getRecipe(level, new ItemStackInventory(stack));
            if (recipe != null && !Optional.ofNullable(context.getPlayer()).map(Player::isCrouching).orElse(false)) {
                BlockPos pos = context.getClickedPos();
                BlockPos abovePos = pos.above();
                Player player = context.getPlayer();
                if (player != null && context.getClickedFace() == Direction.UP && Helpers.isBlock(level.getBlockState(pos), TFCTags.Blocks.SCRAPING_SURFACE) && level.getBlockState(abovePos).isAir()) {
                    BlockState state = TFCBlocks.SCRAPING.get().defaultBlockState();
                    level.setBlockAndUpdate(abovePos, state);
                    level.getBlockEntity(abovePos, (BlockEntityType) TFCBlockEntities.SCRAPING.get()).map((entity) -> ((ScrapingBlockEntity)entity).getCapability(Capabilities.ITEM).map((cap) -> {
                        ItemStack insertStack = stack.split(1);
                        stack.setCount(stack.getCount() + cap.insertItem(0, insertStack, false).getCount());
                        ((ScrapingBlockEntity) entity).updateDisplayCache();
                        level.sendBlockUpdated(abovePos, state, state, 2);
                        return InteractionResult.SUCCESS;
                    }).orElse(InteractionResult.PASS));
                }
            }

            return InteractionResult.PASS;
        });
    }
}
