package com.vomiter.survivorsbutchercraft.util;

import com.vomiter.survivorsbutchercraft.common.component.CarcassData;
import com.vomiter.survivorsbutchercraft.common.registry.SBDataComponents;
import net.dries007.tfc.common.entities.livestock.Age;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class CarcassDataHelper {
    private CarcassDataHelper() {
    }

    public static boolean hasData(ItemStack stack) {
        return stack.has(SBDataComponents.CARCASS_DATA.get());
    }

    @Nullable
    public static CarcassData getData(ItemStack stack) {
        return stack.get(SBDataComponents.CARCASS_DATA.get());
    }

    public static void setData(ItemStack stack, CarcassData data) {
        stack.set(SBDataComponents.CARCASS_DATA.get(), data);
    }

    public static void removeData(ItemStack stack) {
        stack.remove(SBDataComponents.CARCASS_DATA.get());
    }

    public static void writeFromTFCAnimal(
            ItemStack stack,
            TFCAnimalProperties properties,
            ResourceLocation entityId
    ) {
        CompoundTag tfcAnimalData = new CompoundTag();
        properties.saveCommonAnimalData(tfcAnimalData);

        CarcassData data = new CarcassData(
                entityId,
                tfcAnimalData,
                properties.isMale(),
                properties.getAgeType() == Age.OLD,
                properties.getGeneticSize(),
                properties.getFamiliarity()
        );

        setData(stack, data);
    }

    @Nullable
    public static ResourceLocation getId(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null ? data.entityId() : null;
    }

    public static boolean isMale(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null && data.male();
    }

    public static boolean isOld(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null && data.old();
    }

    public static int getGeneticSize(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null ? data.geneticSize() : 0;
    }

    public static float getFamiliarity(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null ? data.familiarity() : 0f;
    }

    @Nullable
    public static CompoundTag getTFCAnimalData(ItemStack stack) {
        CarcassData data = getData(stack);
        return data != null ? data.tfcAnimalData() : null;
    }
}