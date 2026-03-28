// File: com/vomiter/survivorsbutchercraft/data/bbmodel/ElementCube.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import net.minecraft.core.Direction;

import java.util.Map;

public final class ElementCube {
    public final String name;
    public final String uuid;
    public final Bounds bbox;
    public final Vec3 partTranslation;
    public final Map<Direction, FaceUv> faces;

    public ElementCube(String name, String uuid, Bounds bbox, Vec3 partTranslation, Map<Direction, FaceUv> faces) {
        this.name = name;
        this.uuid = uuid;
        this.bbox = bbox;
        this.partTranslation = partTranslation;
        this.faces = faces;
    }
}