package com.vomiter.survivorsbutchercraft.data.bbmodel;

public record ExportConfig(
        String name,
        int texW,
        int texH,
        String textureFile,
        boolean includeRootIfHasCubes
) {
    public ExportConfig(String name, int texW, int texH, String textureFile) {
        this(name, texW, texH, textureFile, true);
    }
}