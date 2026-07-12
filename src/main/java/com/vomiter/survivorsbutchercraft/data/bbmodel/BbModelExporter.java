package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.model.geom.ModelPart;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class BbModelExporter {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final UuidProvider uuids;
    private final ModelPartTraverser traverser = new ModelPartTraverser();
    private final CubeBounds bounds = new CubeBounds();
    private final CubeUvExtractor uv = new CubeUvExtractor();
    private final BbModelJsonWriter writer = new BbModelJsonWriter();

    public BbModelExporter() {
        this(UuidProvider.random());
    }

    public BbModelExporter(UuidProvider uuids) {
        this.uuids = uuids;
    }

    public JsonObject export(ModelPart root, ExportConfig cfg) {
        BoneTree tree = new BoneTree(uuids);
        List<ElementCube> elements = new ArrayList<>();

        traverser.visit(root, (path, cubeIndex, cube, pose) -> {
            String bonePath = pathNormalize(path);
            tree.ensurePath(bonePath);

            String cubeUuid = uuids.next();
            String cubeName = cubeName(bonePath, cubeIndex);

            Bounds bbox = bounds.computeBounds(cube);
            Vec3 partTranslation = bounds.extractTranslationModelUnits(pose.pose());

            var faces = uv.captureFaces(cube, pose, cfg.texW(), cfg.texH());

            elements.add(new ElementCube(cubeName, cubeUuid, bbox, partTranslation, faces));
            tree.addCube(bonePath, cubeUuid);
        });

        List<JsonElement> outliner = tree.buildOutlinerJson(cfg.includeRootIfHasCubes());
        return writer.write(cfg, elements, outliner, uuids);
    }

    private String pathNormalize(String path) {
        if (path == null || path.isEmpty()) return "";
        if (path.startsWith("/")) return path.substring(1);
        return path;
    }

    private String cubeName(String bonePath, int cubeIndex) {
        String prefix = bonePath.isEmpty() ? "root" : bonePath.replace('/', '_');
        return prefix + "_cube" + cubeIndex;
    }

}