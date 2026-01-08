package com.vomiter.survivorsbutchercraft.core.registry;

import com.lance5057.butchercraft.blocks.HideBlock;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.block.SkullLikeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class SBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SurvivorsButchercraft.MODID);
    public static final Map<Carcass, RegistryObject<Block>> HIDE_CARPETS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Block>> WALL_HEADS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Block>> HEADS = new EnumMap<>(Carcass.class);

    static {
        var head_types = Map.of(
                "head", HEADS,
                "wall_head", WALL_HEADS
        );
        for (Carcass carcass : Carcass.values()) {
            var carcass_name = carcass.serializedName();
            if(carcass.hasHide) HIDE_CARPETS.put(carcass, BLOCKS.register(
                    carcass_name + "_hide_carpet",
                    () -> new HideBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).mapColor(carcass.mapColor))
            ));
            head_types.forEach((s, m) -> {
                m.put(carcass,
                        BLOCKS.register(
                                carcass_name + "_" + s,
                                () -> new SkullLikeBlock(
                                        BlockBehaviour.Properties
                                                .copy(Blocks.PLAYER_HEAD)
                                                .strength(1F))
                        )
                );

            });
        }
    }
}
