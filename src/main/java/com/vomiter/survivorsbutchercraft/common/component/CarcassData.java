package com.vomiter.survivorsbutchercraft.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record CarcassData(
        ResourceLocation entityId,
        CompoundTag tfcAnimalData,
        boolean male,
        boolean old,
        int geneticSize,
        float familiarity
) {
    public static final Codec<CarcassData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("entity_id")
                            .forGetter(CarcassData::entityId),

                    CompoundTag.CODEC
                            .fieldOf("tfc_animal")
                            .forGetter(CarcassData::tfcAnimalData),

                    Codec.BOOL
                            .fieldOf("male")
                            .forGetter(CarcassData::male),

                    Codec.BOOL
                            .fieldOf("old")
                            .forGetter(CarcassData::old),

                    Codec.INT
                            .fieldOf("genetic_size")
                            .forGetter(CarcassData::geneticSize),

                    Codec.FLOAT
                            .fieldOf("familiarity")
                            .forGetter(CarcassData::familiarity)
            ).apply(instance, CarcassData::new)
    );

    public static final StreamCodec<ByteBuf, CarcassData> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    CarcassData::entityId,

                    ByteBufCodecs.COMPOUND_TAG,
                    CarcassData::tfcAnimalData,

                    ByteBufCodecs.BOOL,
                    CarcassData::male,

                    ByteBufCodecs.BOOL,
                    CarcassData::old,

                    ByteBufCodecs.VAR_INT,
                    CarcassData::geneticSize,

                    ByteBufCodecs.FLOAT,
                    CarcassData::familiarity,

                    CarcassData::new
            );

    public CarcassData {
        if (entityId == null) {
            throw new IllegalArgumentException("entityId cannot be null");
        }

        tfcAnimalData = tfcAnimalData == null
                ? new CompoundTag()
                : tfcAnimalData.copy();
    }

    /**
     * CompoundTag 本身可變，因此不直接暴露 Data Component 內保存的實例。
     */
    @Override
    public CompoundTag tfcAnimalData() {
        return tfcAnimalData.copy();
    }
}