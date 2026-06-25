package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.google.gson.JsonObject;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.client.model.entity.MuskOxModel;
import net.dries007.tfc.client.model.entity.YakModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class BbmodelExportProvider implements DataProvider {
    private final PackOutput output;

    public BbmodelExportProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        return CompletableFuture.allOf(
                exportAnimal(cache, MuskOxModel.createBodyLayer(), "musk_ox", "muskOx"),
                exportAnimal(cache, YakModel.createBodyLayer(), "yak", "yak")
        );
    }

    private CompletableFuture<?> exportAnimal(CachedOutput cache, LayerDefinition layer, String serializedName, String fileName) {
        // 1) 取得 LayerDefinition 與貼圖大小
        int texW = getTextureWidth(layer, 64);
        int texH = getTextureHeight(layer, 64);

        // 2) bake 出 ModelPart 樹
        ModelPart root = layer.bakeRoot();

        // 3) 匯出 bbmodel JSON（一次完成 elements + outliner）
        BbModelExporter exporter = new BbModelExporter();
        ExportConfig cfg = new ExportConfig(serializedName, texW, texH, serializedName + ".png", true);
        JsonObject json = exporter.export(root, cfg);

        // 4) 寫檔
        Path target = output.getOutputFolder()
                .resolve("bbmodels/" + fileName + ".bbmodel")
                .toAbsolutePath()
                .normalize();

        SurvivorsButchercraft.LOGGER.info("[BBMODEL] outputFolder=" + output.getOutputFolder().toAbsolutePath().normalize());
        SurvivorsButchercraft.LOGGER.info("[BBMODEL] target=" + target);
        return DataProvider.saveStable(cache, json, target);

    }

    @Override
    public String getName() {
        return "BBModel Export";
    }

    private static int getTextureWidth(LayerDefinition layer, int fallback) {
        return tryGetIntField(layer, "textureWidth", fallback);
    }

    private static int getTextureHeight(LayerDefinition layer, int fallback) {
        return tryGetIntField(layer, "textureHeight", fallback);
    }

    private static int tryGetIntField(Object obj, String name, int fallback) {
        try {
            var f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (int) f.get(obj);
        } catch (Throwable t) {
            return fallback;
        }
    }
}