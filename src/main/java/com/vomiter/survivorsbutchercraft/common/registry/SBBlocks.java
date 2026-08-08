package com.vomiter.survivorsbutchercraft.common.registry;

import com.lance5057.butchercraft.ButchercraftBlockEntities;
import com.lance5057.butchercraft.blocks.HideBlock;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlock;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.block.DecaySkullBlock;
import com.vomiter.survivorsbutchercraft.common.block.DecayWallSkullBlock;
import com.vomiter.survivorsbutchercraft.common.block.SkullLikeBlock;
import com.vomiter.survivorsbutchercraft.common.block.WallSkullLikeBlock;
import com.vomiter.survivorsbutchercraft.mixin.BlockEntityTypeAccessor;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, SurvivorsButchercraft.MODID);
    public static final Map<Carcass, DeferredHolder<Block, Block>> HIDE_CARPETS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> WALL_HEADS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> HEADS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> WALL_HEADS_MALE = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> HEADS_MALE = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> WALL_SKULLS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> SKULLS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> WALL_SKULLS_MALE = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, DeferredHolder<Block, Block>> SKULLS_MALE = new EnumMap<>(Carcass.class);

    public static final Map<Metal, DeferredHolder<Block, Block>> MEAT_HOOKS = new EnumMap<>(Metal.class);


    static {
        for (Metal metal : Metal.values()) {
            if(!metal.allParts()) continue;

            MEAT_HOOKS.put(
                    metal,
                    BLOCKS.register(
                            "metal/meat_hook/" + metal.getSerializedName(),
                            MeatHookBlock::new
                    )
            );
        }

        var headProperties = BlockBehaviour.Properties
                .ofFullCopy(Blocks.PLAYER_HEAD)
                .strength(1F);
        for (Carcass carcass : Carcass.values()) {
            var carcass_name = carcass.serializedName();
            if(carcass.hasHide()) HIDE_CARPETS.put(carcass, BLOCKS.register(
                    "hide_carpet/" + carcass_name,
                    (Supplier<? extends HideBlock>) () -> new HideBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).mapColor(carcass.mapColor()))
            ));
            HEADS.put(carcass,
                        BLOCKS.register(
                                "head/" + carcass_name,
                                () -> new DecaySkullBlock(headProperties)
                        )
            );
            WALL_HEADS.put(carcass,
                        BLOCKS.register(
                                "wall_head/" + carcass_name,
                                () -> new DecayWallSkullBlock(headProperties)
                        )
            );
            SKULLS.put(carcass,
                    BLOCKS.register(
                            "skull/" + carcass_name,
                            () -> new SkullLikeBlock(headProperties)
                    )
            );
            WALL_SKULLS.put(carcass,
                    BLOCKS.register(
                            "wall_skull/" + carcass_name,
                            () -> new WallSkullLikeBlock(headProperties)
                    )
            );

            if (carcass.hasMaleHead()){
                HEADS_MALE.put(carcass,
                        BLOCKS.register(
                                "head_male/" + carcass_name,
                                () -> new DecaySkullBlock(headProperties)
                        )
                );
                WALL_HEADS_MALE.put(carcass,
                        BLOCKS.register(
                                "wall_head_male/" + carcass_name,
                                () -> new DecayWallSkullBlock(headProperties)
                        )
                );
                SKULLS_MALE.put(carcass,
                        BLOCKS.register(
                                "skull_male/" + carcass_name,
                                () -> new SkullLikeBlock(headProperties)
                        )
                );
                WALL_SKULLS_MALE.put(carcass,
                        BLOCKS.register(
                                "wall_skull_male/" + carcass_name,
                                () -> new WallSkullLikeBlock(headProperties)
                        )
                );

            }
        }
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityType<MeatHookBlockEntity> type = ButchercraftBlockEntities.MEAT_HOOK.get();
            BlockEntityTypeAccessor acc = (BlockEntityTypeAccessor)type;
            HashSet<Block> validBlocks = new HashSet<>(acc.getValidBlocks());
            acc.setValidBlocks(validBlocks);
            validBlocks.addAll(SBBlocks.MEAT_HOOKS.values().stream().map(Supplier::get).collect(Collectors.toSet()));
        });
    }

}
