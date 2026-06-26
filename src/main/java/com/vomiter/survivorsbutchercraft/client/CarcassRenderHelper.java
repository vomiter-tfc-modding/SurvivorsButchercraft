package com.vomiter.survivorsbutchercraft.client;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.util.CarcassDataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CarcassRenderHelper {
    private static final Set<ResourceLocation> MISSING_MODEL_WARNED = new HashSet<>();

    private CarcassRenderHelper() {
    }

    private static boolean isCarcass(ItemStack stack){
        return SBItems.CARCASSES.values().stream().anyMatch(o -> stack.is(o.get()));
    }

    public static List<BlacklistedModel> buildModels(List<BlacklistedModel> baseModels, ItemStack carcass) {
        List<BlacklistedModel> out = new ArrayList<>();

        if (baseModels == null || baseModels.isEmpty()) {
            return out;
        }

        for (BlacklistedModel baseModel : baseModels) {
            out.add(resolveVariant(baseModel, carcass));
        }

        if (!carcass.isEmpty() && CarcassDataHelper.hasData(carcass)) {
            appendConditionalParts(out, baseModels, carcass);
        }

        if (isCarcass(carcass)){
            tryAddConditionalModel(
                    out,
                    Helpers.id("meathook/hook"),
                    baseModels.get(0)
            );
        }


        return out;
    }

    public static BlacklistedModel resolveVariant(BlacklistedModel base, ItemStack carcass) {
        if (base == null || carcass.isEmpty() || !CarcassDataHelper.hasData(carcass)) {
            return base;
        }

        ResourceLocation resolvedLocation = resolveLocation(base.rc, carcass);
        List<Integer> resolvedBlacklist = resolveBlacklist(base.blacklist, carcass);

        if (resolvedLocation == null) {
            return base;
        }

        if (!modelExists(resolvedLocation)) {
            warnMissingOnce(resolvedLocation, base.rc);
            return new BlacklistedModel(base.rc, resolvedBlacklist, base.isBlock, base.transform);
        }

        return new BlacklistedModel(resolvedLocation, resolvedBlacklist, base.isBlock, base.transform);
    }

    private static void appendConditionalParts(List<BlacklistedModel> out, List<BlacklistedModel> baseModels, ItemStack carcass) {
        if (baseModels.isEmpty()) {
            return;
        }

        BlacklistedModel anchor = baseModels.get(0);
        if (anchor == null || anchor.rc == null) {
            return;
        }

        boolean male = CarcassDataHelper.isMale(carcass);
        boolean old = CarcassDataHelper.isOld(carcass);

        // base = survivorsbutchercraft:meathook/yak/hooked
        // male part = survivorsbutchercraft:meathook/yak/hooked_male_parts
        if (male) {
            tryAddConditionalModel(
                    out,
                    derivePartLocation(anchor.rc, "male_parts"),
                    anchor
            );
        }
        else {
            tryAddConditionalModel(
                    out,
                    derivePartLocation(anchor.rc, "female_parts"),
                    anchor
            );
        }
    }

    private static void tryAddConditionalModel(List<BlacklistedModel> out, ResourceLocation location, BlacklistedModel anchor) {
        if (location == null) {
            return;
        }

        if (!modelExists(location)) {
            warnMissingOnce(location, anchor.rc);
            return;
        }

        out.add(new BlacklistedModel(
                location,
                new ArrayList<>(),
                anchor.isBlock,
                anchor.transform
        ));
    }

    private static ResourceLocation derivePartLocation(ResourceLocation base, String suffix) {
        if (base == null || suffix == null || suffix.isBlank()) {
            return null;
        }
        return Helpers.id(base.getNamespace(), base.getPath() + "_" + suffix);
    }

    private static ResourceLocation resolveLocation(ResourceLocation base, ItemStack carcass) {
        if (base == null) {
            return null;
        }

        String path = base.getPath();
        boolean old = CarcassDataHelper.isOld(carcass);
        if (old) {
            path += "_old";
        }

        /*
        boolean male = CarcassDataHelper.isMale(carcass);
        boolean old = CarcassDataHelper.isOld(carcass);

        if (!male) {
            path += "_female";
        }
        if (old) {
            path += "_old";
        }
         */

        return Helpers.id(base.getNamespace(), path);
    }

    private static boolean modelExists(ResourceLocation modelLocation) {
        ResourceLocation fileLocation = Helpers.id(
                modelLocation.getNamespace(),
                "models/" + modelLocation.getPath() + ".json"
        );

        Optional<Resource> resource = Minecraft.getInstance()
                .getResourceManager()
                .getResource(fileLocation);

        return resource.isPresent();
    }

    private static void warnMissingOnce(ResourceLocation missingVariant, ResourceLocation fallbackBase) {
        if (MISSING_MODEL_WARNED.add(missingVariant)) {
            SurvivorsButchercraft.LOGGER.warn(
                    "Missing carcass model/part: {} ; fallback/skip from base: {}",
                    missingVariant,
                    fallbackBase
            );
        }
    }

    private static List<Integer> resolveBlacklist(List<Integer> baseBlacklist, ItemStack carcass) {
        List<Integer> out = new ArrayList<>();
        if (baseBlacklist != null) {
            out.addAll(baseBlacklist);
        }
        return out;
    }
}