// File: com/vomiter/survivorsbutchercraft/data/bbmodel/PathUtil.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

public final class PathUtil {
    private PathUtil() {}

    public static String normalize(String path) {
        if (path == null || path.isEmpty()) return "";
        if (path.startsWith("/")) return path.substring(1);
        return path;
    }

    public static String cubeName(String bonePath, int cubeIndex) {
        String prefix = bonePath.isEmpty() ? "root" : bonePath.replace('/', '_');
        return prefix + "_cube" + cubeIndex;
    }
}