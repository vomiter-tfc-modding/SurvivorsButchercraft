// File: com/vomiter/survivorsbutchercraft/data/bbmodel/BbModelJsonWriter.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.google.gson.*;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public final class BbModelJsonWriter {

    public JsonObject write(ExportConfig cfg, List<ElementCube> elements, List<JsonElement> outliner, UuidProvider uuids) {
        JsonObject model = new JsonObject();

        JsonObject meta = new JsonObject();
        meta.addProperty("format_version", "5.0");
        meta.addProperty("model_format", "free");
        meta.addProperty("box_uv", false);
        model.add("meta", meta);

        model.addProperty("name", cfg.name());

        JsonObject resolution = new JsonObject();
        resolution.addProperty("width", cfg.texW());
        resolution.addProperty("height", cfg.texH());
        model.add("resolution", resolution);

        JsonArray textures = new JsonArray();
        JsonObject tex = new JsonObject();
        tex.addProperty("name", cfg.textureFile());
        tex.addProperty("relative_path", cfg.textureFile());
        tex.addProperty("id", "0");
        tex.addProperty("width", cfg.texW());
        tex.addProperty("height", cfg.texH());
        tex.addProperty("uv_width", cfg.texW());
        tex.addProperty("uv_height", cfg.texH());
        tex.addProperty("uuid", uuids.next());
        textures.add(tex);
        model.add("textures", textures);

        JsonArray elems = new JsonArray();
        for (var e : elements) elems.add(buildCubeElement(cfg, e));
        model.add("elements", elems);

        JsonArray out = new JsonArray();
        for (var o : outliner) out.add(o);
        model.add("outliner", out);

        return model;
    }

    private JsonObject buildCubeElement(ExportConfig cfg, ElementCube e) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", e.name);
        obj.addProperty("uuid", e.uuid);
        obj.addProperty("type", "cube");
        obj.addProperty("autouv", 0);
        obj.addProperty("color", 8);
        obj.addProperty("rescale", false);
        obj.addProperty("locked", false);
        obj.addProperty("render_order", "default");
        obj.addProperty("allow_mirror_modeling", true);

        Vec3 fromMc = e.partTranslation.add(e.bbox.min);
        Vec3 toMc   = e.partTranslation.add(e.bbox.max);

        float yPivot = 24.0f; // entity 模型常用基準

        Vec3 fromBb = new Vec3(fromMc.x, yPivot - toMc.y, fromMc.z); // 注意：用 toMc.y 才是 bbox.maxY
        Vec3 toBb   = new Vec3(toMc.x,   yPivot - fromMc.y, toMc.z); // 注意：用 fromMc.y 才是 bbox.minY

        obj.add("from", vec(fromBb));
        obj.add("to", vec(toBb));
        obj.add("origin", vec(new Vec3(0, 0, 0)));
        obj.add("faces", facesToJson(cfg, e.faces));
        return obj;
    }

    private JsonObject facesToJson(ExportConfig cfg, Map<Direction, FaceUv> faces) {
        JsonObject out = new JsonObject();
        putFace(out, cfg, "north", faces.get(Direction.NORTH));
        putFace(out, cfg, "south", faces.get(Direction.SOUTH));
        putFace(out, cfg, "east",  faces.get(Direction.EAST));
        putFace(out, cfg, "west",  faces.get(Direction.WEST));
        putFace(out, cfg, "up",    faces.get(Direction.DOWN));
        putFace(out, cfg, "down",  faces.get(Direction.UP));
        return out;
    }

    private void putFace(JsonObject out, ExportConfig cfg, String key, FaceUv uv) {
        if (uv == null) return;
        JsonObject f = new JsonObject();

        JsonArray arr = new JsonArray();
        // bbmodel stores pixels in "uv": [x1,y1,x2,y2]
        arr.add(uv.u2 * cfg.texW());
        arr.add(uv.v2 * cfg.texH());
        arr.add(uv.u1 * cfg.texW());
        arr.add(uv.v1 * cfg.texH());
        f.add("uv", arr);

        f.addProperty("texture", 0);
        if (uv.rotation != 0) f.addProperty("rotation", uv.rotation);

        out.add(key, f);
    }

    private static JsonArray vec(Vec3 v) {
        JsonArray a = new JsonArray();
        a.add(v.x);
        a.add(v.y);
        a.add(v.z);
        return a;
    }
}