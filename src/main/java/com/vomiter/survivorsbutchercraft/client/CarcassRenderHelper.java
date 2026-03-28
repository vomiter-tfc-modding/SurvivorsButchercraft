package com.vomiter.survivorsbutchercraft.client;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.CarcassDataHelper;
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

    private static ResourceLocation resolveLocation(ResourceLocation base, ItemStack carcass) {
        if (base == null) {
            return null;
        }

        boolean male = CarcassDataHelper.isMale(carcass);
        boolean old = CarcassDataHelper.isOld(carcass);

        String path = base.getPath();

        // 例： hooked -> hooked_female_old
        if (!male) {
            path += "_female";
        }
        if (old) {
            path += "_old";
        }

        return Helpers.id(base.getNamespace(), path);
    }

    private static boolean modelExists(ResourceLocation modelLocation) {
        // models/<path>.json
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
                    "Missing carcass variant model: {} ; fallback to base model: {}",
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