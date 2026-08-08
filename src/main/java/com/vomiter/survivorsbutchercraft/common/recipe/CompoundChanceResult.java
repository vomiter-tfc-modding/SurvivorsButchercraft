package com.vomiter.survivorsbutchercraft.common.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class CompoundChanceResult {
    public static final CompoundChanceResult EMPTY =
            new CompoundChanceResult(
                    ItemStack.EMPTY,
                    FluidStack.EMPTY,
                    0,
                    0,
                    0.0F
            );

    private static final Codec<CompoundChanceResult> ITEM_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ItemStack.SINGLE_ITEM_CODEC
                            .fieldOf("item")
                            .forGetter(CompoundChanceResult::getStack),

                    Codec.INT
                            .optionalFieldOf("min", 1)
                            .forGetter(CompoundChanceResult::getMinium),

                    Codec.INT
                            .optionalFieldOf("max", 1)
                            .forGetter(CompoundChanceResult::getMaximum),

                    Codec.FLOAT
                            .optionalFieldOf("chance", 1.0F)
                            .forGetter(CompoundChanceResult::getChance)
            ).apply(instance, CompoundChanceResult::createItemResult));

    private static final Codec<CompoundChanceResult> FLUID_CODEC =
            FluidStack.CODEC
                    .fieldOf("fluid")
                    .xmap(
                            CompoundChanceResult::new,
                            CompoundChanceResult::getFluid
                    )
                    .codec();

    /**
     * JSON／datapack codec。
     *
     * 物品輸出格式：
     * {
     *   "item": {
     *     "id": "minecraft:beef"
     *   },
     *   "min": 1,
     *   "max": 4,
     *   "chance": 0.5
     * }
     *
     * 流體輸出格式：
     * {
     *   "fluid": {
     *     "id": "minecraft:water",
     *     "amount": 1000
     *   }
     * }
     */
    private static final Codec<CompoundChanceResult> EMPTY_CODEC =
            Codec.BOOL
                    .fieldOf("empty")
                    .codec()
                    .comapFlatMap(
                            empty -> empty
                                    ? DataResult.success(EMPTY)
                                    : DataResult.error(
                                    () -> "\"empty\" must be true"
                            ),
                            result -> true
                    );

    public static final Codec<CompoundChanceResult> CODEC =
            Codec.either(
                            ITEM_CODEC,
                            Codec.either(
                                    FLUID_CODEC,
                                    EMPTY_CODEC
                            )
                    )
                    .xmap(
                            either -> either.map(
                                    item -> item,
                                    fluidOrEmpty -> fluidOrEmpty.map(
                                            fluid -> fluid,
                                            empty -> empty
                                    )
                            ),
                            result -> {
                                if (result.hasItem()) {
                                    return Either.left(result);
                                }

                                if (result.hasFluid()) {
                                    return Either.right(
                                            Either.left(result)
                                    );
                                }

                                return Either.right(
                                        Either.right(result)
                                );
                            }
                    )
                    .validate(CompoundChanceResult::validate);

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            CompoundChanceResult
            > STREAM_CODEC = StreamCodec.of(
            CompoundChanceResult::encode,
            CompoundChanceResult::decode
    );

    private final ItemStack stack;
    private final FluidStack fluid;
    private final int minium;
    private final int maximum;
    private final float chance;

    public CompoundChanceResult(Item item) {
        this(
                item.getDefaultInstance(),
                1,
                1,
                1.0F
        );
    }

    public CompoundChanceResult(
            Item item,
            int count,
            float chance
    ) {
        this(
                item.getDefaultInstance(),
                count,
                count,
                chance
        );
    }

    public CompoundChanceResult(
            Item item,
            int minium,
            int maximum,
            float chance
    ) {
        this(
                item.getDefaultInstance(),
                minium,
                maximum,
                chance
        );
    }

    public CompoundChanceResult(ItemStack stack) {
        this(
                stack,
                stack.getCount(),
                stack.getCount(),
                1.0F
        );
    }

    public CompoundChanceResult(
            ItemStack stack,
            float chance
    ) {
        this(
                stack,
                0,
                stack.getCount(),
                chance
        );
    }

    public CompoundChanceResult(
            ItemStack stack,
            int minium,
            int maximum,
            float chance
    ) {
        validateItemResult(
                stack,
                minium,
                maximum,
                chance
        );

        this.stack = stack.copyWithCount(1);
        this.fluid = FluidStack.EMPTY;
        this.minium = minium;
        this.maximum = maximum;
        this.chance = chance;
    }

    /**
     * 固定流體輸出。
     *
     * 流體數量直接保存在 FluidStack 中，
     * 不受 chance、minium、maximum 或 fortune 影響。
     */
    public CompoundChanceResult(FluidStack fluid) {
        if (fluid.isEmpty()) {
            throw new IllegalArgumentException(
                    "Fluid result cannot be empty"
            );
        }

        this.stack = ItemStack.EMPTY;
        this.fluid = fluid.copy();
        this.minium = 0;
        this.maximum = 0;
        this.chance = 0.0F;
    }

    private CompoundChanceResult(
            ItemStack stack,
            FluidStack fluid,
            int minium,
            int maximum,
            float chance
    ) {
        this.stack = stack;
        this.fluid = fluid;
        this.minium = minium;
        this.maximum = maximum;
        this.chance = chance;
    }

    private static CompoundChanceResult createItemResult(
            ItemStack stack,
            int minium,
            int maximum,
            float chance
    ) {
        return new CompoundChanceResult(
                stack,
                minium,
                maximum,
                chance
        );
    }

    private static DataResult<CompoundChanceResult> validate(
            CompoundChanceResult result
    ) {
        if (result.hasFluid()) {
            if (result.fluid.getAmount() <= 0) {
                return DataResult.error(
                        () -> "Fluid amount must be greater than zero"
                );
            }

            return DataResult.success(result);
        }

        if (result.minium < 0) {
            return DataResult.error(
                    () -> "Minimum output cannot be negative: "
                            + result.minium
            );
        }

        if (result.maximum < result.minium) {
            return DataResult.error(
                    () -> "Maximum output cannot be smaller than "
                            + "minimum output: "
                            + result.maximum
                            + " < "
                            + result.minium
            );
        }

        if (result.chance < 0.0F || result.chance > 1.0F) {
            return DataResult.error(
                    () -> "Chance must be between 0 and 1: "
                            + result.chance
            );
        }

        return DataResult.success(result);
    }

    private static void validateItemResult(
            ItemStack stack,
            int minium,
            int maximum,
            float chance
    ) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException(
                    "Item result cannot be empty"
            );
        }

        if (minium < 0) {
            throw new IllegalArgumentException(
                    "Minimum output cannot be negative: "
                            + minium
            );
        }

        if (maximum < minium) {
            throw new IllegalArgumentException(
                    "Maximum output cannot be smaller than "
                            + "minimum output: "
                            + maximum
                            + " < "
                            + minium
            );
        }

        if (chance < 0.0F || chance > 1.0F) {
            throw new IllegalArgumentException(
                    "Chance must be between 0 and 1: "
                            + chance
            );
        }
    }

    public boolean hasItem() {
        return !stack.isEmpty();
    }

    public boolean hasFluid() {
        return !fluid.isEmpty();
    }

    public boolean isEmpty() {
        return !hasItem() && !hasFluid();
    }

    public ItemStack getStack() {
        return stack;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public float getChance() {
        return chance;
    }

    public int getMinium() {
        return minium;
    }

    public int getMaximum() {
        return maximum;
    }

    /**
     * 僅處理物品輸出。
     *
     * 若此 result 是流體輸出，會直接回傳 ItemStack.EMPTY。
     */
    public ItemStack rollOutput(
            RandomSource random,
            int fortuneLevel
    ) {
        if (!hasItem()) {
            return ItemStack.EMPTY;
        }

        int outputAmount = maximum;

        double fortuneBonus =
                0.1D * fortuneLevel;

        double effectiveChance = Math.min(
                1.0D,
                chance + fortuneBonus
        );

        for (
                int roll = 0;
                roll < maximum - minium;
                ++roll
        ) {
            if (random.nextFloat() > effectiveChance) {
                --outputAmount;
            }
        }

        if (outputAmount <= 0) {
            return ItemStack.EMPTY;
        }

        return stack.copyWithCount(outputAmount);
    }

    private static void encode(
            RegistryFriendlyByteBuf buffer,
            CompoundChanceResult result
    ) {
        buffer.writeBoolean(result.hasFluid());

        if (result.hasFluid()) {
            FluidStack.STREAM_CODEC.encode(
                    buffer,
                    result.fluid
            );
            return;
        }
        buffer.writeBoolean(result.hasItem());
        if (result.hasItem()){
            ItemStack.STREAM_CODEC.encode(
                    buffer,
                    result.stack
            );
            buffer.writeVarInt(result.minium);
            buffer.writeVarInt(result.maximum);
            buffer.writeFloat(result.chance);
        }
    }

    private static CompoundChanceResult decode(
            RegistryFriendlyByteBuf buffer
    ) {
        boolean fluidResult = buffer.readBoolean();

        if (fluidResult) {
            FluidStack fluid =
                    FluidStack.STREAM_CODEC.decode(buffer);

            return new CompoundChanceResult(fluid);
        }
        boolean itemResult = buffer.readBoolean();
        if (itemResult){
            ItemStack stack =
                    ItemStack.STREAM_CODEC.decode(buffer);

            int minium = buffer.readVarInt();
            int maximum = buffer.readVarInt();
            float chance = buffer.readFloat();

            return new CompoundChanceResult(
                    stack,
                    minium,
                    maximum,
                    chance
            );
        }
        return EMPTY;

    }
}