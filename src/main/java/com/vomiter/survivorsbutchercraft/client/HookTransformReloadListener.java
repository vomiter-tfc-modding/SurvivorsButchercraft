package com.vomiter.survivorsbutchercraft.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class HookTransformReloadListener extends SimpleJsonResourceReloadListener {
    private static final com.google.gson.Gson GSON = new GsonBuilder().create();

    private static volatile Map<ResourceLocation, TransformDef> CACHE;

    public HookTransformReloadListener() {
        super(GSON, "hook_transforms"); // assets/<ns>/hook_transforms/*.json
    }

    public static void onAddReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new HookTransformReloadListener());
    }


    public static TransformDef get(ResourceLocation id) {
        return CACHE.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects,
                         @NotNull ResourceManager manager,
                         @NotNull ProfilerFiller profiler) {

        Map<ResourceLocation, TransformDef> next = new HashMap<>();

        for (var e : objects.entrySet()) {
            ResourceLocation target = e.getKey();

            JsonObject obj = e.getValue().getAsJsonObject();
            TransformDef def = TransformDef.fromJson(obj);
            SurvivorsButchercraft.LOGGER.debug(target.toString());
            next.put(target, def);
        }

        CACHE = Collections.unmodifiableMap(next);
    }

    private static ResourceLocation pathToRl(String path) {
        int slash = path.indexOf('/');
        if (slash < 0) return new ResourceLocation("minecraft", path);
        String ns = path.substring(0, slash);
        String p  = path.substring(slash + 1);
        return new ResourceLocation(ns, p);
    }

    public record TransformDef(float tx, float ty, float tz, float sx, float sy, float sz) {
        public static TransformDef fromJson(com.google.gson.JsonObject o) {
            float tx = 0, ty = 0, tz = 0;
            float sx = 1, sy = 1, sz = 1;

            if (o.has("translate")) {
                var a = o.getAsJsonArray("translate");
                tx = a.get(0).getAsFloat();
                ty = a.get(1).getAsFloat();
                tz = a.get(2).getAsFloat();
            }
            if (o.has("scale")) {
                var a = o.getAsJsonArray("scale");
                sx = a.get(0).getAsFloat();
                sy = a.get(1).getAsFloat();
                sz = a.get(2).getAsFloat();
            }
            return new TransformDef(tx, ty, tz, sx, sy, sz);
        }
    }
}
