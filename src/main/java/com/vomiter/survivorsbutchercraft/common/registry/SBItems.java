package com.vomiter.survivorsbutchercraft.common.registry;

import com.lance5057.butchercraft.items.ButcherKnifeItem;
import com.lance5057.butchercraft.items.CarcassItem;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.item.SkullLikeItem;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class SBItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsButchercraft.MODID);
    public static final Map<Carcass, RegistryObject<Item>> CARCASSES = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Item>> HIDES = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Item>> HEADS = new EnumMap<>(Carcass.class);
    public static final Map<Carcass, RegistryObject<Item>> HEADS_MALE = new EnumMap<>(Carcass.class);
    public static final Map<Metal.Default, RegistryObject<Item>> BUTCHER_KNIVES = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> BUTCHER_KNIFE_HEADS = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> SKINNING_KNIVES = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> SKINNING_KNIFE_HEADS = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> BONESAWS = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> BONESAW_HEADS = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> GUT_KNIVES = new EnumMap<>(Metal.Default.class);
    public static final Map<Metal.Default, RegistryObject<Item>> GUT_KNIFE_HEADS = new EnumMap<>(Metal.Default.class);


    static {
        for (Metal.Default metal : Metal.Default.values()) {
            if(!metal.hasTools()) continue;
            BUTCHER_KNIFE_HEADS.put(metal, ITEMS.register("metal/butcher_knife_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties().rarity(metal.getRarity()))));
            BUTCHER_KNIVES.put(metal, ITEMS.register("metal/butcher_knife/" + metal.getSerializedName(),
                    () -> new ButcherKnifeItem(
                            new Item.Properties()
                                    .rarity(metal.getRarity())
                                    .durability(metal.toolTier().getUses())
                    )));

            SKINNING_KNIFE_HEADS.put(metal, ITEMS.register("metal/skinning_knife_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties().rarity(metal.getRarity()))));
            SKINNING_KNIVES.put(metal, ITEMS.register("metal/skinning_knife/" + metal.getSerializedName(),
                    () -> new SwordItem(
                            metal.toolTier(),
                            0,0,
                            new Item.Properties()
                                    .rarity(metal.getRarity())
                                    .durability(metal.toolTier().getUses())
                    )));

            BONESAW_HEADS.put(
                    metal,
                    ITEMS.register(
                            "metal/bonesaw_head/" + metal.getSerializedName(),
                            () -> new Item(
                                    new Item.Properties()
                                            .rarity(metal.getRarity())
                            )
                    )
            );

            BONESAWS.put(
                    metal,
                    ITEMS.register(
                            "metal/bonesaw/" + metal.getSerializedName(),
                            () -> new SwordItem(
                                    metal.toolTier(),
                                    0,
                                    0,
                                    new Item.Properties()
                                            .rarity(metal.getRarity())
                                            .durability(metal.toolTier().getUses())
                            )
                    )
            );

            GUT_KNIFE_HEADS.put(
                    metal,
                    ITEMS.register(
                            "metal/gut_knife_head/" + metal.getSerializedName(),
                            () -> new Item(
                                    new Item.Properties()
                                            .rarity(metal.getRarity())
                            )
                    )
            );

            GUT_KNIVES.put(
                    metal,
                    ITEMS.register(
                            "metal/gut_knife/" + metal.getSerializedName(),
                            () -> new SwordItem(
                                    metal.toolTier(),
                                    0,
                                    0,
                                    new Item.Properties()
                                            .rarity(metal.getRarity())
                                            .durability(metal.toolTier().getUses())
                            )
                    )
            );
        }



        for (Carcass carcass : Carcass.values()) {
            CARCASSES.put(carcass, ITEMS.register("carcass/" + carcass.serializedName(),
                    () -> new CarcassItem(new Item.Properties().stacksTo(1))));
            HEADS.put(carcass, ITEMS.register("head/" + carcass.serializedName(),
                    () -> new SkullLikeItem(
                            SBBlocks.HEADS.get(carcass).get(),
                            SBBlocks.WALL_HEADS.get(carcass).get(),
                            new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN)));

            if (carcass.hasHide()) {
                HIDES.put(carcass, ITEMS.register("hide/" + carcass.serializedName(),
                        () -> new BlockItem(SBBlocks.HIDE_CARPETS.get(carcass).get(), new Item.Properties())));
            }

            if(carcass.hasMaleHead()) {
                HEADS_MALE.put(carcass, ITEMS.register("head_male/" + carcass.serializedName(),
                        () -> new SkullLikeItem(
                                SBBlocks.HEADS_MALE.get(carcass).get(),
                                SBBlocks.WALL_HEADS_MALE.get(carcass).get(),
                                new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN)));
            }
        }
    }
}
