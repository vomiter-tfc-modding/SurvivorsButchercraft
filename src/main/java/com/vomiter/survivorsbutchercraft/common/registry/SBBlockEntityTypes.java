package com.vomiter.survivorsbutchercraft.common.registry;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.blockentity.SkullLikeBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class SBBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SurvivorsButchercraft.MODID);

    public static final RegistryObject<BlockEntityType<SkullLikeBlockEntity>> DECAY_SKULL_LIKE =
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
                                    .map(RegistryObject::get)
                                    .toArray(Block[]::new)
                    ).build(null)
            );


    public static final RegistryObject<BlockEntityType<SkullLikeBlockEntity>> SKULL_LIKE =
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
                                    .map(RegistryObject::get)
                                    .toArray(Block[]::new)

                    ).build(null)
            );
}
