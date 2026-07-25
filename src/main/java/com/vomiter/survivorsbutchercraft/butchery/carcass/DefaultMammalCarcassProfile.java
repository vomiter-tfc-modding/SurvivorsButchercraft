package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.recipe.ChanceResult;
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
    public List<ChanceResult> dropsFor(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(new ChanceResult(SBItems.HIDES.get(carcass()).get().getDefaultInstance()));
            case DISEMBOWEL -> List.of(
                    new ChanceResult(new ItemStack(SBItems.HEADS.get(carcass()).get()))
            );
            case BISECT -> List.of(
                    new ChanceResult(new ItemStack(ButchercraftItems.HEART.get())),
                    new ChanceResult(new ItemStack(ButchercraftItems.LIVER.get())),
                    new ChanceResult(new ItemStack(ButchercraftItems.KIDNEY.get(), 2)),
                    new ChanceResult(new ItemStack(ButchercraftItems.LUNG.get(), 2))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<ChanceResult> dropsForSupport(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case BUTCHER -> List.of(
                    new ChanceResult(MeatMap.get(getMeatType(), MeatProduct.RIB), 4, 0.25f),
                    new ChanceResult(MeatMap.get(getMeatType(), MeatProduct.ROAST), 4, 0.25f)
            );
            case BISECT -> List.of(
                    new ChanceResult(new ItemStack(ButchercraftItems.STOMACH.get(), 1)),
                    new ChanceResult(new ItemStack(ButchercraftItems.TRIPE.get(), 4))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<ChanceResult> dropsForTrivial(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(
                    new ChanceResult(ButchercraftItems.FAT.get(), 12, 0.5f)
            );
            case DISEMBOWEL -> List.of(
                    new ChanceResult(ButchercraftItems.SINEW.get(), 6, 0.5f)
            );
            case BISECT -> List.of(
                    new ChanceResult(ButchercraftItems.FAT.get(), 12, 0.5f),
                    new ChanceResult(ButchercraftItems.SINEW.get(), 12, 0.5f),
                    new ChanceResult(MeatMap.get(getMeatType(), MeatProduct.SCRAP), 12, 0.75f)
            );

            default ->
                    List.of();
        };
    }


}
