// File: com/vomiter/survivorsbutchercraft/data/bbmodel/UuidProvider.java
package com.vomiter.survivorsbutchercraft.data.bbmodel;

import java.util.UUID;

@FunctionalInterface
public interface UuidProvider {
    String next();

    static UuidProvider random() {
        return () -> UUID.randomUUID().toString();
    }
}