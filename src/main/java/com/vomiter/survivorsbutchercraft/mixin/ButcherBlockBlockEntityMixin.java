package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockContainer;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipeContainer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.adapter.ButcherBlockBucketAdapter;
import com.vomiter.survivorsbutchercraft.adapter.ButcherBlockLootConverter;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.common.registry.SBRecipes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

@Mixin(value = ButcherBlockBlockEntity.class, remap = false)
public abstract class ButcherBlockBlockEntityMixin extends BlockEntity implements IButcherBlock<CustomButcherBlockRecipe> {

    public int sbtfcInterface$getStage(){
        return stage;
    }

    public Ingredient sbtfcInterface$getCurTool(){
        return curTool;
    }

    public ItemStack sbtfcInterface$getInserted(){
        return getInsertedItem();
    }

    public Optional<ButcherBlockRecipe> sbtfcInterface$matchRecipe(){
        return matchRecipe();
    }

    @Shadow
    public abstract ItemStack getInsertedItem();

    @Shadow
    public int stage;

    @Shadow
    private Ingredient curTool;

    @Shadow
    protected abstract Optional<ButcherBlockRecipe> matchRecipe();

    @Shadow
    protected abstract void dropLoot(AnimatedRecipeItemUse recipeToolsIn, Player player);

    @Shadow
    public abstract void updateInventory();

    @Shadow
    public abstract void zeroProgress();

    public ButcherBlockBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<ItemInteractionResult> cir) {
        var adapter = new ButcherBlockBucketAdapter((ButcherBlockBlockEntity) (Object) this);
        adapter.acceptFluidHandler(p, butcheringTool, cir);
    }

    @WrapOperation(
            method = "butcher",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
                    remap = true)
    )
    private void sbtfc$addExtraDamage(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot, Operation<Void> original){
        original.call(instance, amount, entity, slot);
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
        var self = (IButcherBlock)this;
        return ButcherBlockLootConverter.sbtfc$convertLoot(self, instance, params, original);
    }

    @Override
    public void sbtfcInterface$dropLoot(AnimatedRecipeItemUse recipeToolsIn, Player player) {
        dropLoot(recipeToolsIn, player);
    }

    @Unique
    private RecipeManager.CachedCheck<ButcherBlockContainer, CustomButcherBlockRecipe> sbtfc$quickCheck
            = RecipeManager.createCheck(SBRecipes.CUSTOM_BUTCHER_BLOCK.get());

    @WrapMethod(method = "matchRecipe")
    private Optional<RecipeHolder<ButcherBlockRecipe>> sbtfc$matchRecipe(Operation<Optional<RecipeHolder<ButcherBlockRecipe>>> original){
        var ori = original.call();
        if(ori.isEmpty()){
            var custom = sbtfc$quickCheck.getRecipeFor(new ButcherBlockContainer(this.getInsertedItem()), level);
            if (custom.isPresent()){
                ButcherBlockRecipe hookRecipe = custom.get().value().getButcherBlockRecipe();
                RecipeHolder<ButcherBlockRecipe> hookRecipeHolder = new RecipeHolder<>(
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



    public Optional<? extends RecipeHolder<CustomButcherBlockRecipe>> sbtfcInterface$getRecipe(){
        return sbtfc$quickCheck.getRecipeFor(
                new ButcherBlockContainer(getInsertedItem()),
                level
        );
    }


}
