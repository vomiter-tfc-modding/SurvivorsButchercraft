package com.vomiter.survivorsbutchercraft.data.bbmodel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;

public final class BoneTree {
    private final Node root;
    private final UuidProvider uuids;

    public BoneTree(UuidProvider uuids) {
        this.uuids = uuids;
        this.root = new Node("", uuids.next());
    }

    public void ensurePath(String path) {
        if (path.isEmpty()) return;
        String[] segs = path.split("/");
        Node cur = root;
        for (String s : segs) {
            if (s.isEmpty()) continue;
            cur = cur.children.computeIfAbsent(s, k -> new Node(k, uuids.next()));
        }
    }

    public void addCube(String path, String cubeUuid) {
        Node node = root;
        if (!path.isEmpty()) {
            String[] segs = path.split("/");
            for (String s : segs) {
                if (s.isEmpty()) continue;
                node = node.children.computeIfAbsent(s, k -> new Node(k, uuids.next()));
            }
        }
        node.cubes.add(cubeUuid);
    }

    public List<JsonElement> buildOutlinerJson(boolean includeRootIfHasCubes) {
        List<JsonElement> out = new ArrayList<>();
        for (Node child : root.children.values()) out.add(child.toJson());
        if (includeRootIfHasCubes && !root.cubes.isEmpty()) out.add(root.toJson());
        return out;
    }

    private static final class Node {
        final String name;
        final String uuid;
        final Map<String, Node> children = new LinkedHashMap<>();
        final List<String> cubes = new ArrayList<>();

        Node(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        JsonObject toJson() {
            JsonObject o = new JsonObject();
            o.addProperty("name", name.isEmpty() ? "root" : name);
            o.add("origin", vec0());
            o.add("rotation", vec0());
            o.addProperty("uuid", uuid);

            JsonArray ch = new JsonArray();
            for (Node c : children.values()) ch.add(c.toJson());
            for (String cu : cubes) ch.add(new JsonPrimitive(cu));
            o.add("children", ch);
            return o;
        }

        private static JsonArray vec0() {
            JsonArray a = new JsonArray();
            a.add(0); a.add(0); a.add(0);
            return a;
        }
    }
}