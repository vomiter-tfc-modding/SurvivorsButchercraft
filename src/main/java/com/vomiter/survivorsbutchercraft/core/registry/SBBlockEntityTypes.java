package com.vomiter.survivorsbutchercraft.core.registry;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.blockentity.SkullLikeBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class SBBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SurvivorsButchercraft.MODID);
    public static final RegistryObject<BlockEntityType<?>> SKULL_LIKE = BLOCK_ENTITIES.register(
            "skull_like",
            () -> BlockEntityType.Builder.of(
                    SkullBlockEntity::new,
                    Stream.concat(
                            SBBlocks.HEADS.values().stream(),
                            SBBlocks.WALL_HEADS.values().stream()
                            )
                            .map(RegistryObject::get)
                            .toArray(Block[]::new)
            ).build(null)
    );
}
