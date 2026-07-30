package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.ToolAlternative;
import com.vomiter.survivorsbutchercraft.common.recipe.IButcherRecipe;
import com.vomiter.survivorsbutchercraft.common.registry.SBFoodTraits;
import com.vomiter.survivorsbutchercraft.compat.FarmersDelightCompat;
import com.vomiter.survivorsbutchercraft.compat.FirmaLifeCompat;
import com.vomiter.survivorsbutchercraft.compat.WaterFlaskCompat;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import com.vomiter.survivorsbutchercraft.util.ToolTierGetter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class ButcherBlockLootConverter {
    public static ObjectArrayList<ItemStack> sbtfc$convertLoot(IButcherBlock blockEntity, LootTable instance, LootParams params, Operation<ObjectArrayList<ItemStack>> original){
        if (!(blockEntity instanceof BlockEntity be))return original.call(instance, params);
        if(be.getLevel() == null) return original.call(instance, params);
        var tool = Optional.ofNullable(params.getParamOrNull(LootContextParams.TOOL)).orElse(ItemStack.EMPTY);
        var tier = ToolTierGetter.get(tool.getItem());
        var fortuneMod = tier == null? 0: Math.round(tier.getLevel() / 2f);

        var random = be.getLevel().random;
        var originalList = original.call(instance, params);
        originalList.removeIf(stack -> stack.is(SBTags.Items.BUTCHERY_SKIP_LOOT));
        blockEntity.sbtfcInterface$matchRecipe().ifPresent(recipe -> {
            if (recipe instanceof IButcherRecipe custom){
                int fortuneLevel = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) + fortuneMod;
                custom.getResults(blockEntity.sbtfcInterface$getStage()).stream()
                        .map(chanceResult -> chanceResult.rollOutput(random, fortuneLevel))
                        .forEach(originalList::add);
            }
        });

        if (be instanceof MeatHookBlockEntity hook && ThreadLocalFlags.dropLootForButchering.get()){
            if (ModList.get().isLoaded("waterflasks")) WaterFlaskCompat.addBladder(originalList);
            if (ModList.get().isLoaded("firmalife")) FirmaLifeCompat.addRennet(originalList);
            if (ModList.get().isLoaded("farmersdelight")){
                FarmersDelightCompat.addHam(originalList);
            }
        }
        var inserted = blockEntity.sbtfcInterface$getInserted();

        if(Optional.ofNullable(FoodCapability.get(inserted))
                .map(food -> food.hasTrait(SBFoodTraits.PRESERVED))
                .orElse(false)
        ) {
            originalList.removeIf(item -> FoodCapability.get(item) != null);
        }

        var ideal = Optional.ofNullable(ToolAlternative.getIdealTool(blockEntity.sbtfcInterface$getCurTool())).orElse(Items.AIR);
        if(ideal.getDefaultInstance().isEmpty()) return originalList;
        boolean isIdeal = ToolAlternative.getIdealTool(ideal).test(tool);
        ObjectArrayList<ItemStack> newList = ObjectArrayList.of();
        originalList.forEach(originalItemStack -> {
            if(isIdeal){
                newList.add(originalItemStack);
            } else {
                var singleInput = originalItemStack.copyWithCount(1);
                for (int i = 0; i < originalItemStack.getCount(); i++) {
                    if(random.nextFloat() < 3/4f){
                        newList.add(singleInput);
                    }
                }
            }
        });
        return ObjectArrayList.wrap(
                newList.stream().map(item -> TFCFoodAdapter.copyRotten(blockEntity.sbtfcInterface$getInserted(), item))
                        .toArray(ItemStack[]::new)
        );
    }

}
