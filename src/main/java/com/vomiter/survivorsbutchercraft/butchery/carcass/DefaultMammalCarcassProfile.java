package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class DefaultMammalCarcassProfile implements ICarcassProfile {

    public abstract Carcass carcass();

    public abstract MeatType getMeatType();

    @Override
    public Item carcassItem(){
        return SBItems.CARCASSES.get(carcass()).get();
    }

    @Override
    public boolean hasHide() {
        return true;
    }

    @Override
    public List<CompoundChanceResult> dropsFor(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(new CompoundChanceResult(SBItems.HIDES.get(carcass()).get().getDefaultInstance()));
            case DISEMBOWEL -> List.of(
                    new CompoundChanceResult(new ItemStack(SBItems.HEADS.get(carcass()).get()))
            );
            case BISECT -> List.of(
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.HEART.get())),
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.LIVER.get())),
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.KIDNEY.get(), 2)),
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.LUNG.get(), 2))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<CompoundChanceResult> dropsForSupport(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case BUTCHER -> List.of(
                    new CompoundChanceResult(MeatMap.get(getMeatType(), MeatProduct.RIB), 2, 6, 0.25f),
                    new CompoundChanceResult(MeatMap.get(getMeatType(), MeatProduct.ROAST), 2, 6, 0.25f)
            );
            case BISECT -> List.of(
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.STOMACH.get(), 1)),
                    new CompoundChanceResult(new ItemStack(ButchercraftItems.TRIPE.get(), 4))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<CompoundChanceResult> dropsForTrivial(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(
                    new CompoundChanceResult(ButchercraftItems.FAT.get(), 12, 0.5f)
            );
            case DISEMBOWEL -> List.of(
                    new CompoundChanceResult(ButchercraftItems.SINEW.get(), 6, 0.5f)
            );
            case BISECT -> List.of(
                    new CompoundChanceResult(ButchercraftItems.FAT.get(), 12, 0.5f),
                    new CompoundChanceResult(ButchercraftItems.SINEW.get(), 12, 0.5f),
                    new CompoundChanceResult(MeatMap.get(getMeatType(), MeatProduct.SCRAP), 12, 0.75f)
            );

            default ->
                    List.of();
        };
    }


}
