package com.vomiter.survivorsbutchercraft.mixin.debug;

import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipeContainer;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(MeatHookBlockEntity.class)
public abstract class MeatHookBlockEntityDebugMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(
            method = "insertItem",
            at = @At("HEAD")
    )
    private void survivorsbutchercraft$debugInsertHead(
            ItemStack heldItem,
            CallbackInfo ci
    ) {
        MeatHookBlockEntity self =
                (MeatHookBlockEntity) (Object) this;

        Level level = self.getLevel();

        LOGGER.info(
                """
                
                [MeatHook Debug] === insertItem START ===
                side        = {}
                pos         = {}
                heldItem    = {}
                inventory   = {}
                handler     = {}
                """,
                level == null
                        ? "NO LEVEL"
                        : level.isClientSide ? "CLIENT" : "SERVER",
                self.getBlockPos(),
                describeStack(heldItem),
                describeStack(self.getHandler().getStackInSlot(0)),
                self.getHandler().getClass().getName()
        );

        if (level == null) {
            LOGGER.warn(
                    "[MeatHook Debug] level == null, cannot check recipes"
            );
            return;
        }

        List<RecipeHolder<?>> hookRecipes = level
                .getRecipeManager()
                .getRecipes()
                .stream()
                .filter(holder -> holder.value() instanceof HookRecipe)
                .toList();

        LOGGER.info(
                "[MeatHook Debug] loaded HookRecipe count = {}",
                hookRecipes.size()
        );

        boolean foundMatchingCarcass = false;

        for (RecipeHolder<?> holder : hookRecipes) {
            HookRecipe recipe = (HookRecipe) holder.value();

            boolean carcassTest;
            boolean recipeMatches;

            try {
                carcassTest = recipe.carcass().test(heldItem);
            } catch (Throwable throwable) {
                LOGGER.error(
                        "[MeatHook Debug] carcass.test THREW for recipe {}",
                        holder.id(),
                        throwable
                );
                continue;
            }

            try {
                recipeMatches = recipe.matches(
                        new HookRecipeContainer(heldItem),
                        level
                );
            } catch (Throwable throwable) {
                LOGGER.error(
                        "[MeatHook Debug] recipe.matches THREW for recipe {}",
                        holder.id(),
                        throwable
                );
                recipeMatches = false;
            }

            if (carcassTest) {
                foundMatchingCarcass = true;
            }

            LOGGER.info(
                    """
                    [MeatHook Debug] recipe {}
                      type            = {}
                      group           = {}
                      carcass         = {}
                      carcass.test    = {}
                      recipe.matches  = {}
                    """,
                    holder.id(),
                    BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()),
                    recipe.group(),
                    recipe.carcass(),
                    carcassTest,
                    recipeMatches
            );
        }

        LOGGER.info(
                "[MeatHook Debug] ANY HookRecipe carcass match = {}",
                foundMatchingCarcass
        );
    }

    @WrapOperation(
            method = "insertItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/items/ItemStackHandler;isItemValid(ILnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean survivorsbutchercraft$debugIsItemValid(
            ItemStackHandler handler,
            int slot,
            ItemStack stack,
            Operation<Boolean> original
    ) {
        boolean result = original.call(handler, slot, stack);

        LOGGER.info(
                """
                [MeatHook Debug] inventory.isItemValid
                  handlerClass = {}
                  slot         = {}
                  current      = {}
                  inserting    = {}
                  result       = {}
                """,
                handler.getClass().getName(),
                slot,
                describeStack(handler.getStackInSlot(slot)),
                describeStack(stack),
                result
        );

        return result;
    }

    @WrapOperation(
            method = "insertItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            )
    )
    private ItemStack survivorsbutchercraft$debugSimulatedInsert(
            ItemStackHandler handler,
            int slot,
            ItemStack stack,
            boolean simulate,
            Operation<ItemStack> original
    ) {
        ItemStack before = handler.getStackInSlot(slot).copy();

        ItemStack remainder = original.call(
                handler,
                slot,
                stack,
                simulate
        );

        boolean matchesOriginal =
                ItemStack.matches(remainder, stack);

        LOGGER.info(
                """
                [MeatHook Debug] SIMULATED insert
                  slot               = {}
                  simulate           = {}
                  inventoryBefore    = {}
                  input              = {}
                  remainder          = {}
                  ItemStack.matches  = {}
                  insertionAccepted  = {}
                """,
                slot,
                simulate,
                describeStack(before),
                describeStack(stack),
                describeStack(remainder),
                matchesOriginal,
                !matchesOriginal
        );

        return remainder;
    }

    @WrapOperation(
            method = "insertItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 1
            )
    )
    private ItemStack survivorsbutchercraft$debugRealInsert(
            ItemStackHandler handler,
            int slot,
            ItemStack stack,
            boolean simulate,
            Operation<ItemStack> original
    ) {
        ItemStack before = handler.getStackInSlot(slot).copy();

        ItemStack remainder = original.call(
                handler,
                slot,
                stack,
                simulate
        );

        ItemStack after = handler.getStackInSlot(slot).copy();

        LOGGER.info(
                """
                [MeatHook Debug] REAL insert
                  slot             = {}
                  simulate         = {}
                  inventoryBefore  = {}
                  input            = {}
                  remainder        = {}
                  inventoryAfter   = {}
                """,
                slot,
                simulate,
                describeStack(before),
                describeStack(stack),
                describeStack(remainder),
                describeStack(after)
        );

        return remainder;
    }

    @WrapOperation(
            method = "insertItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/workstations/hook/MeatHookBlockEntity;matchRecipe()Ljava/util/Optional;"
            )
    )
    private Optional<RecipeHolder<HookRecipe>>
    survivorsbutchercraft$debugMatchRecipe(
            MeatHookBlockEntity instance,
            Operation<Optional<RecipeHolder<HookRecipe>>> original
    ) {
        LOGGER.info(
                "[MeatHook Debug] calling matchRecipe(), inventory = {}",
                describeStack(instance.getHandler().getStackInSlot(0))
        );

        Optional<RecipeHolder<HookRecipe>> result =
                original.call(instance);

        if (result.isPresent()) {
            LOGGER.info(
                    "[MeatHook Debug] matchRecipe SUCCESS: {}",
                    result.get().id()
            );
        } else {
            LOGGER.warn(
                    "[MeatHook Debug] matchRecipe FAILED"
            );
        }

        return result;
    }

    @Inject(
            method = "insertItem",
            at = @At("RETURN")
    )
    private void survivorsbutchercraft$debugInsertReturn(
            ItemStack heldItem,
            CallbackInfo ci
    ) {
        MeatHookBlockEntity self =
                (MeatHookBlockEntity) (Object) this;

        LOGGER.info(
                """
                [MeatHook Debug] === insertItem END ===
                heldItemAfter = {}
                inventoryAfter = {}
                stage          = {}
                progress       = {}
                maxProgress    = {}
                """,
                describeStack(heldItem),
                describeStack(self.getHandler().getStackInSlot(0)),
                self.stage,
                self.progress,
                self.maxProgress
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