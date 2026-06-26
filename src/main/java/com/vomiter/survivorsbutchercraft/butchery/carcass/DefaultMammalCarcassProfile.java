package com.vomiter.survivorsbutchercraft.butchery.carcass;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
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
    public List<DropSpec> dropsFor(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(DropSpec.of(new ItemStack(SBItems.HIDES.get(carcass()).get())));
            case DISEMBOWEL -> List.of(
                    DropSpec.of(new ItemStack(SBItems.HEADS.get(carcass()).get()))
            );
            case BISECT -> List.of(
                    DropSpec.of(new ItemStack(ButchercraftItems.HEART.get())),
                    DropSpec.of(new ItemStack(ButchercraftItems.LIVER.get())),
                    DropSpec.of(new ItemStack(ButchercraftItems.KIDNEY.get(), 2)),
                    DropSpec.of(new ItemStack(ButchercraftItems.LUNG.get(), 2))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<DropSpec> dropsForSupport(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case BUTCHER -> List.of(
                    DropSpec.of(MeatMap.get(getMeatType(), MeatProduct.RIB)).withCount(2, 4),
                    DropSpec.of(MeatMap.get(getMeatType(), MeatProduct.ROAST)).withCount(1, 3)
            );
            case BISECT -> List.of(
                    DropSpec.of(new ItemStack(ButchercraftItems.STOMACH.get(), 1)),
                    DropSpec.of(new ItemStack(ButchercraftItems.TRIPE.get(), 4))
            );
            default ->
                    List.of();
        };
    }

    @Override
    public List<DropSpec> dropsForTrivial(MeatHookStage stage) {
        if(carcass() == null) return List.of();
        return switch (stage) {
            case SKIN -> List.of(
                    DropSpec.of(ButchercraftItems.FAT.get()).withCount(4, 12),
                    DropSpec.of(ButchercraftItems.LEATHER_SCRAP.get()).withCount(2, 4)
            );
            case DISEMBOWEL -> List.of(
                    DropSpec.of(ButchercraftItems.SINEW.get()).withCount(2, 6)
            );
            case BISECT -> List.of(
                    DropSpec.of(ButchercraftItems.FAT.get()).withCount(4, 12),
                    DropSpec.of(ButchercraftItems.SINEW.get()).withCount(4, 12),
                    DropSpec.of(MeatMap.get(getMeatType(), MeatProduct.SCRAP)).withCount(6, 12)
            );

            default ->
                    List.of();
        };
    }


}
