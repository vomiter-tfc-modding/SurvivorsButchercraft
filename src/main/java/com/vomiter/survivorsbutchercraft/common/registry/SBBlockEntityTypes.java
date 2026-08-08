package com.vomiter.survivorsbutchercraft.common.registry;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlock;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.blockentity.CustomHookBlockEntity;
import com.vomiter.survivorsbutchercraft.common.blockentity.SkullLikeBlockEntity;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public class SBBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SurvivorsButchercraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomHookBlockEntity>> CUSTOM_HOOK
            = BLOCK_ENTITIES.register(
                    "custom_hook",
            ()-> BlockEntityType.Builder.of(
                    (pos, state) -> {
                        try{
                            ThreadLocalFlags.blockEntityTypeSwitch.set(true);
                            return new CustomHookBlockEntity(pos, state);
                        } finally {
                            ThreadLocalFlags.blockEntityTypeSwitch.remove();
                        }
                    },
                    BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof MeatHookBlock)
                            .toArray(Block[]::new)
            ).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkullLikeBlockEntity>> DECAY_SKULL_LIKE =
            BLOCK_ENTITIES.register(
                    "decay_skull_like",
                    () -> BlockEntityType.Builder.of(
                            SkullLikeBlockEntity::new,
                            Stream.of(
                                            SBBlocks.HEADS.values().stream(),
                                            SBBlocks.WALL_HEADS.values().stream(),
                                            SBBlocks.HEADS_MALE.values().stream(),
                                            SBBlocks.WALL_HEADS_MALE.values().stream()
                                    )
                                    .flatMap(s -> s)
                                    .map(DeferredHolder::get)
                                    .toArray(Block[]::new)
                    ).build(null)
            );


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkullLikeBlockEntity>> SKULL_LIKE =
            BLOCK_ENTITIES.register(
                    "skull_like",
                    () -> BlockEntityType.Builder.of(
                            SkullLikeBlockEntity::new,
                            Stream.of(
                                            SBBlocks.SKULLS.values().stream(),
                                            SBBlocks.WALL_SKULLS.values().stream(),
                                            SBBlocks.SKULLS_MALE.values().stream(),
                                            SBBlocks.WALL_SKULLS_MALE.values().stream()
                                    )
                                    .flatMap(s -> s)
                                    .map(DeferredHolder::get)
                                    .toArray(Block[]::new)

                    ).build(null)
            );
}
