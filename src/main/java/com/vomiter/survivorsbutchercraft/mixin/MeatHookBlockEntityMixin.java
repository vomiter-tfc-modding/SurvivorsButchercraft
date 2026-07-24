package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.survivorsbutchercraft.adapter.ButcherBlockLootConverter;
import com.vomiter.survivorsbutchercraft.adapter.MeatHookBucketAdapter;
import com.vomiter.survivorsbutchercraft.adapter.TFCFoodAdapter;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomMeatHookRecipe;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.compat.FarmersDelightCompat;
import com.vomiter.survivorsbutchercraft.compat.WaterFlaskCompat;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public abstract class MeatHookBlockEntityMixin extends BlockEntity implements IButcherBlock {

    public Optional<HookRecipe> sbtfcInterface$matchRecipe(){
        return matchRecipe();
    }

    public int sbtfcInterface$getStage(){
        return stage;
    }

    public Ingredient sbtfcInterface$getCurTool(){
        return curTool;
    }

    public ItemStack sbtfcInterface$getInserted(){
        return getInsertedItem();
    }


    @Shadow
    public abstract ItemStack getInsertedItem();

    @Shadow
    public int stage;

    @Shadow
    private Ingredient curTool;

    @Shadow
    abstract boolean isFinalStage(HookRecipe r);

    @Shadow
    protected abstract Optional<HookRecipe> matchRecipe();

    public MeatHookBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "butcher", at = @At("HEAD"), cancellable = true)
    private void sbtfc$acceptFluidHandler(Player p, ItemStack butcheringTool, CallbackInfoReturnable<InteractionResult> cir) {
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
                    target = "Lnet/minecraft/world/item/ItemStack;" +
                            "hurtAndBreak(" +
                            "ILnet/minecraft/world/entity/LivingEntity;" +
                            "Ljava/util/function/Consumer;" +
                            ")V",
                    remap = true,
                    ordinal = 0
            )
    )
    private void sbtfc$addExtraDamage(ItemStack instance, int amount, LivingEntity living, Consumer<LivingEntity> consumer, Operation<Void> original){
        var ideal = ToolAlternative.getIdealTool(curTool);
        var extra = 0;
        if(ideal != null){
            boolean isIdeal = ToolAlternative.getIdealTool(ideal).test(instance);
            if(!isIdeal) extra = 1;
        }

        original.call(instance, amount + extra, living, consumer);
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
}
