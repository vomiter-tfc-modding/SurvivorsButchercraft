package com.vomiter.survivorsbutchercraft.common.registry;

import com.lance5057.butchercraft.blocks.HideBlock;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.block.SkullLikeBlock;
import com.vomiter.survivorsbutchercraft.common.block.WallSkullLikeBlock;
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
    public static final Map<Carcass, RegistryObject<Block>> WALL_HEADS_MALE = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Block>> HEADS_MALE = new EnumMap<>(Carcass.class);


    static {
        var headProperties = BlockBehaviour.Properties
                .copy(Blocks.PLAYER_HEAD)
                .strength(1F);
        for (Carcass carcass : Carcass.values()) {
            var carcass_name = carcass.serializedName();
            if(carcass.hasHide()) HIDE_CARPETS.put(carcass, BLOCKS.register(
                    "hide_carpet/" + carcass_name,
                    () -> new HideBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).mapColor(carcass.mapColor()))
            ));
            HEADS.put(carcass,
                        BLOCKS.register(
                                "head/" + carcass_name,
                                () -> new SkullLikeBlock(headProperties)
                        )
            );
            WALL_HEADS.put(carcass,
                        BLOCKS.register(
                                "wall_head/" + carcass_name,
                                () -> new WallSkullLikeBlock(headProperties)
                        )
            );
            if (carcass.hasMaleHead()){
                HEADS_MALE.put(carcass,
                        BLOCKS.register(
                                "head_male/" + carcass_name,
                                () -> new SkullLikeBlock(headProperties)
                        )
                );
                WALL_HEADS_MALE.put(carcass,
                        BLOCKS.register(
                                "wall_head_male/" + carcass_name,
                                () -> new WallSkullLikeBlock(headProperties)
                        )
                );
            }
        }
    }
}
