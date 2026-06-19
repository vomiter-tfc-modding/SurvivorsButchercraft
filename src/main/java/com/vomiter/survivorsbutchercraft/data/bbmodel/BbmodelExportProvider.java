package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.google.gson.JsonObject;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.client.model.entity.MuskOxModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class BbmodelExportProvider implements DataProvider {
    private final PackOutput output;

    public BbmodelExportProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return exportMuskOx(cache);
    }

    private CompletableFuture<?> exportMuskOx(CachedOutput cache) {
        // 1) 取得 LayerDefinition 與貼圖大小
        LayerDefinition layer = MuskOxModel.createBodyLayer();
        int texW = getTextureWidth(layer, 64);
        int texH = getTextureHeight(layer, 64);

        // 2) bake 出 ModelPart 樹
        ModelPart root = layer.bakeRoot();

        // 3) 匯出 bbmodel JSON（一次完成 elements + outliner）
        BbModelExporter exporter = new BbModelExporter();
        ExportConfig cfg = new ExportConfig("musk_ox", texW, texH, "musk_ox.png", true);
        JsonObject json = exporter.export(root, cfg);

        // 4) 寫檔
        Path target = output.getOutputFolder()
                .resolve("bbmodels/muskOx/muskOx.bbmodel")
                .toAbsolutePath()
                .normalize();

        SurvivorsButchercraft.LOGGER.info("[BBMODEL] outputFolder=" + output.getOutputFolder().toAbsolutePath().normalize());
        SurvivorsButchercraft.LOGGER.info("[BBMODEL] target=" + target);

        // 這裡回傳 future，讓 datagen 等寫入完成
        return DataProvider.saveStable(cache, json, target)
                .thenRun(() -> {
                    // 只在寫完後做檢查
                    if (!Files.exists(target)) {
                        throw new IllegalStateException("[BBMODEL] write completed but file not found: " + target);
                    }
                    SurvivorsButchercraft.LOGGER.info("[BBMODEL] wrote: " + target);
                });
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