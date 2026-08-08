package com.vomiter.survivorsbutchercraft.mixin;

import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.ScrapingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.recipes.ScrapingRecipe;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.InteractionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = InteractionManager.class, remap = false)
public class InteractionManagerMixin {

    @Shadow
    public static void registerBlock(Ingredient item, InteractionManager.OnItemUseAction action) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "registerDefaultInteractions", at = @At("TAIL"))
    private static void sbtfc$registerButcherScrapable(CallbackInfo ci){

        registerBlock(Ingredient.of(SBTags.Items.BUTCHER_SCRAPABLE), (stack, context) -> {
            Level level = context.getLevel();
            ScrapingRecipe recipe = ScrapingRecipe.getRecipe(stack);
            if (recipe != null && !Optional.ofNullable(context.getPlayer()).map(Player::isCrouching).orElse(false)) {
                BlockPos pos = context.getClickedPos();
                BlockPos abovePos = pos.above();
                Player player = context.getPlayer();
                if (player != null && context.getClickedFace() == Direction.UP && Helpers.isBlock(level.getBlockState(pos), TFCTags.Blocks.SCRAPING_SURFACE) && level.getBlockState(abovePos).isAir()) {
                    BlockState state = TFCBlocks.SCRAPING.get().defaultBlockState();
                    level.setBlockAndUpdate(abovePos, state);
                    return level.getBlockEntity(abovePos, (BlockEntityType<ScrapingBlockEntity>)TFCBlockEntities.SCRAPING.get()).map(entity -> {
                        ItemStack insertStack = stack.split(1);
                        stack.setCount(stack.getCount() + ((ItemStackHandler)entity.getInventory()).insertItem(0, insertStack, false).getCount());
                        entity.updateDisplayCache();
                        level.sendBlockUpdated(abovePos, state, state, 2);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }).orElse(InteractionResult.PASS);
                }
            }

            return InteractionResult.PASS;
        });
    }
}
