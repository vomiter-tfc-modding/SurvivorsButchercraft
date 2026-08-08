package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.component.CarcassData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SBDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE , SurvivorsButchercraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CarcassData>>
            CARCASS_DATA = DATA_COMPONENTS.register(
                    "carcass_data",
            () -> DataComponentType.<CarcassData>builder()
                            .persistent(CarcassData.CODEC)
                            .networkSynchronized(CarcassData.STREAM_CODEC)
                    .cacheEncoding()
                    .build()
            );

    private SBDataComponents() {
    }

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}