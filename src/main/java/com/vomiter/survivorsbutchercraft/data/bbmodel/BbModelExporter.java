// File: com/vomiter/survivorsbutchercraft/data/bbmodel/BbModelExporter.java
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
            String bonePath = PathUtil.normalize(path);
            tree.ensurePath(bonePath);

            String cubeUuid = uuids.next();
            String cubeName = PathUtil.cubeName(bonePath, cubeIndex);

            Bounds bbox = bounds.computeBounds(cube);
            Vec3 partTranslation = bounds.extractTranslationModelUnits(pose.pose());

            var faces = uv.captureFaces(cube, pose, cfg.texW(), cfg.texH());

            elements.add(new ElementCube(cubeName, cubeUuid, bbox, partTranslation, faces));
            tree.addCube(bonePath, cubeUuid);
        });

        List<JsonElement> outliner = tree.buildOutlinerJson(cfg.includeRootIfHasCubes());
        return writer.write(cfg, elements, outliner, uuids);
    }

    public byte[] exportBytes(ModelPart root, ExportConfig cfg) {
        return gson.toJson(export(root, cfg)).getBytes(StandardCharsets.UTF_8);
    }
}