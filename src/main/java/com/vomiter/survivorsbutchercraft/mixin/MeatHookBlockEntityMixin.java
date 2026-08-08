package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipeContainer;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.adapter.ButcherBlockLootConverter;
import com.vomiter.survivorsbutchercraft.adapter.MeatHookBucketAdapter;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.common.block.AbstractSkullBlock;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomMeatHookRecipe;
import com.vomiter.survivorsbutchercraft.common.registry.SBFoodTraits;
import com.vomiter.survivorsbutchercraft.common.registry.SBRecipes;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.Optional;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public abstract class MeatHookBlockEntityMixin extends BlockEntity implements IButcherBlock<CustomMeatHookRecipe> {

    public Optional<HookRecipe> sbtfcInterface$matchRecipe(){
        return matchRecipe().map(RecipeHolder::value);
    }

    public int sbtfcInterface$getStage(){
        return stage;
    }

    public Ingredient sbtfcInterface$getCurTool(){
        return matchRecipe().map(recipe -> recipe.value().tools().get(stage).tool()).orElse(Ingredient.EMPTY);
    }

    public ItemStack sbtfcInterface$getInserted(){
        return getInsertedItem();
    }


    @Shadow
    public abstract ItemStack getInsertedItem();

    @Shadow
    public int stage;

    @Shadow
    abstract boolean isFinalStage(HookRecipe r);

    @Shadow
    protected abstract Optional<RecipeHolder<HookRecipe>> matchRecipe();

    @Shadow
    protected abstract void dropLoot(AnimatedRecipeItemUse recipeToolsIn, Player player);

    @Shadow
    public abstract void updateInventory();

    @Shadow
    public abstract void zeroProgress();

    public MeatHookBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if(AbstractSkullBlock.isPreservative(butcheringTool) && FoodCapability.hasTrait(getInsertedItem(), SBFoodTraits.PRESERVED)){
            FoodCapability.applyTrait(getInsertedItem(), SBFoodTraits.PRESERVED);
            butcheringTool.shrink(1);
            updateInventory();
            cir.setReturnValue(ItemInteractionResult.SUCCESS);
        }
        var adapter = new MeatHookBucketAdapter((MeatHookBlockEntity) (Object) this);
        adapter.acceptFluidHandler(p, butcheringTool, cir);
    }

    @WrapOperation(method = "butcher", at = @At(value = "INVOKE", target = "Lcom/lance5057/butchercraft/workstations/hook/MeatHookBlockEntity;dropLoot(Lcom/lance5057/butchercraft/workstations/bases/recipes/AnimatedRecipeItemUse;Lnet/minecraft/world/entity/player/Player;)V"))
    private void sbtfc$threadLocalFlagging(MeatHookBlockEntity instance, AnimatedRecipeItemUse recipeToolsIn, Player player, Operation<Void> original){
        try{
            ThreadLocalFlags.dropLootForButchering.set(true);
            ThreadLocalFlags.carcass.set(getInsertedItem());
            original.call(instance, recipeToolsIn, player);
        }
        finally {
            ThreadLocalFlags.dropLootForButchering.set(false);
            ThreadLocalFlags.carcass.set(ItemStack.EMPTY);
        }
    }

    @WrapOperation(
            method = "butcher",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
                    remap = true,
                    ordinal = 0
            )
    )
    private void sbtfc$addExtraDamage(ItemStack instance, int amount, LivingEntity living, EquipmentSlot slot, Operation<Void> original){
        var ideal = ToolAlternative.getIdealTool(sbtfcInterface$getCurTool());
        var extra = 0;
        if(ideal != null){
            boolean isIdeal = ToolAlternative.getIdealTool(ideal).test(instance);
            if(!isIdeal) extra = 1;
        }

        original.call(instance, amount + extra, living, slot);
    }


    @WrapOperation(
            method = "dropLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;" +
                            "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)" +
                            "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
                    remap = true
            )
    )
    private ObjectArrayList<ItemStack> sbtfc$convertLoot(LootTable instance, LootParams params, Operation<ObjectArrayList<ItemStack>> original){
        return ButcherBlockLootConverter.sbtfc$convertLoot(this, instance, params, original);
    }



    @Override
    public void sbtfcInterface$dropLoot(AnimatedRecipeItemUse recipeToolsIn, Player player) {
        dropLoot(recipeToolsIn, player);
    }


    @Unique
    private RecipeManager.CachedCheck<HookRecipeContainer, CustomMeatHookRecipe> sbtfc$quickCheck
            = RecipeManager.createCheck(SBRecipes.CUSTOM_MEAT_HOOK.get());

    @WrapMethod(method = "matchRecipe")
    private Optional<RecipeHolder<HookRecipe>> sbtfc$matchRecipe(Operation<Optional<RecipeHolder<HookRecipe>>> original){
        var ori = original.call();
        if(ori.isEmpty()){
            var custom = sbtfc$quickCheck.getRecipeFor(new HookRecipeContainer(this.getInsertedItem()), level);
            if (custom.isPresent()){
                HookRecipe hookRecipe = custom.get().value().getHookRecipe();
                RecipeHolder<HookRecipe> hookRecipeHolder = new RecipeHolder<>(
                        Helpers.id(custom.get().id().getNamespace(), custom.get().id().getPath() + "_dummy"),
                        hookRecipe
                );
                return Optional.of(hookRecipeHolder);
            }
        }
        return ori;
    }


    @WrapMethod(method = "createHandler")
    private ItemStackHandler sbtfc$createHandler(Operation<ItemStackHandler> original){
        return new ItemStackHandler(1) {
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                return 1;
            }

            protected void onContentsChanged(int slot) {
                updateInventory();
                zeroProgress();
            }

            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                boolean recipeWithInputExists = false;
                if (level != null) {
                    recipeWithInputExists = level.getRecipeManager().getAllRecipesFor(SBRecipes.CUSTOM_MEAT_HOOK.get())
                            .stream().map(RecipeHolder::value)
                            .anyMatch((hookRecipe) -> hookRecipe.matches(
                                    new HookRecipeContainer(stack),
                                    level
                    ));
                }

                return recipeWithInputExists && super.isItemValid(slot, stack);
            }
        };
    }

    public Optional<? extends RecipeHolder<CustomMeatHookRecipe>> sbtfcInterface$getRecipe(){
        return sbtfc$quickCheck.getRecipeFor(
                new HookRecipeContainer(getInsertedItem()),
                level
        );
    }


}
