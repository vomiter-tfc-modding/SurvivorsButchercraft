package com.vomiter.survivorsbutchercraft.common.recipe;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class CompoundChanceResult {
    public static final CompoundChanceResult EMPTY =
            new CompoundChanceResult(ItemStack.EMPTY, 0, 0, 0.0F);

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

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

    public CompoundChanceResult(Item item, int count, float chance) {
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

    /**
     * 每一個可能輸出單位各自以 chance 骰一次。
     *
     * 例如 stack count 為 4、chance 為 0.5：
     * 最終可能輸出 0～4 個。
     */
    public CompoundChanceResult(ItemStack stack, float chance) {
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
        validateItemResult(minium, maximum, chance);

        this.stack = stack.isEmpty()
                ? ItemStack.EMPTY
                : stack.copyWithCount(1);

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
            this.fluid = FluidStack.EMPTY;
        } else {
            this.fluid = fluid.copy();
        }

        this.stack = ItemStack.EMPTY;
        this.minium = 0;
        this.maximum = 0;
        this.chance = 0.0F;
    }

    private static void validateItemResult(
            int minium,
            int maximum,
            float chance
    ) {
        if (minium < 0) {
            throw new IllegalArgumentException(
                    "Minimum output cannot be negative: " + minium
            );
        }

        if (maximum < minium) {
            throw new IllegalArgumentException(
                    "Maximum output cannot be smaller than minimum output: "
                            + maximum + " < " + minium
            );
        }

        if (chance < 0.0F || chance > 1.0F) {
            throw new IllegalArgumentException(
                    "Chance must be between 0 and 1: " + chance
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

        double fortuneBonus = 0.1 * (double) fortuneLevel;

        double effectiveChance = Math.min(
                1.0D,
                chance + fortuneBonus
        );

        for (int roll = 0; roll < maximum - minium; ++roll) {
            if (random.nextFloat() > effectiveChance) {
                --outputAmount;
            }
        }

        if (outputAmount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack output = stack.copy();
        output.setCount(outputAmount);
        return output;
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();

        if (hasFluid()) {
            serializeFluid(json);
        } else {
            serializeItem(json);
        }

        return json;
    }

    private void serializeItem(JsonObject json) {
        ResourceLocation itemId =
                ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (itemId == null) {
            throw new IllegalStateException(
                    "Cannot serialize unregistered item: " + stack
            );
        }

        json.addProperty("item", itemId.toString());
        json.addProperty("max", maximum);
        json.addProperty("min", minium);

        if (stack.hasTag()) {
            json.add(
                    "nbt",
                    JsonParser.parseString(stack.getTag().toString())
            );
        }

        if (chance != 1.0F) {
            json.addProperty("chance", chance);
        }
    }

    private void serializeFluid(JsonObject json) {
        ResourceLocation fluidId =
                ForgeRegistries.FLUIDS.getKey(fluid.getFluid());

        if (fluidId == null) {
            throw new IllegalStateException(
                    "Cannot serialize unregistered fluid: " + fluid
            );
        }

        json.addProperty("fluid", fluidId.toString());
        json.addProperty("amount", fluid.getAmount());

        if (fluid.hasTag()) {
            json.add(
                    "nbt",
                    JsonParser.parseString(fluid.getTag().toString())
            );
        }
    }

    public static CompoundChanceResult deserialize(JsonElement element) {
        if (!element.isJsonObject()) {
            throw new JsonSyntaxException(
                    "Compound result must be a JSON object"
            );
        }

        JsonObject json = element.getAsJsonObject();

        boolean hasItem = json.has("item");
        boolean hasFluid = json.has("fluid");

        if (hasItem && hasFluid) {
            throw new JsonSyntaxException(
                    "Compound result cannot contain both 'item' and 'fluid'"
            );
        }

        if (!hasItem && !hasFluid) {
            throw new JsonSyntaxException(
                    "Compound result must contain either 'item' or 'fluid'"
            );
        }

        if (hasFluid) {
            return deserializeFluid(json);
        }

        return deserializeItem(json);
    }

    private static CompoundChanceResult deserializeItem(
            JsonObject json
    ) {
        String itemIdString =
                GsonHelper.getAsString(json, "item");

        ResourceLocation itemId =
                ResourceLocation.tryParse(itemIdString);

        if (itemId == null) {
            throw new JsonSyntaxException(
                    "Invalid item id: " + itemIdString
            );
        }

        if (!ForgeRegistries.ITEMS.containsKey(itemId)) {
            throw new JsonSyntaxException(
                    "Unknown item: " + itemId
            );
        }

        Item item = ForgeRegistries.ITEMS.getValue(itemId);

        if (item == null) {
            throw new JsonSyntaxException(
                    "Unknown item: " + itemId
            );
        }

        int maximum = GsonHelper.getAsInt(
                json,
                "max",
                1
        );

        int minium = GsonHelper.getAsInt(
                json,
                "min",
                1
        );

        float chance = GsonHelper.getAsFloat(
                json,
                "chance",
                1.0F
        );

        ItemStack stack = new ItemStack(item);

        if (json.has("nbt")) {
            stack.setTag(parseNbt(json.get("nbt"), "item"));
        }

        try {
            return new CompoundChanceResult(
                    stack,
                    minium,
                    maximum,
                    chance
            );
        } catch (IllegalArgumentException exception) {
            throw new JsonSyntaxException(
                    "Invalid item result: " + exception.getMessage(),
                    exception
            );
        }
    }

    private static CompoundChanceResult deserializeFluid(
            JsonObject json
    ) {
        String fluidIdString =
                GsonHelper.getAsString(json, "fluid");

        ResourceLocation fluidId =
                ResourceLocation.tryParse(fluidIdString);

        if (fluidId == null) {
            throw new JsonSyntaxException(
                    "Invalid fluid id: " + fluidIdString
            );
        }

        if (!ForgeRegistries.FLUIDS.containsKey(fluidId)) {
            throw new JsonSyntaxException(
                    "Unknown fluid: " + fluidId
            );
        }

        var fluid = ForgeRegistries.FLUIDS.getValue(fluidId);

        if (fluid == null) {
            throw new JsonSyntaxException(
                    "Unknown fluid: " + fluidId
            );
        }

        int amount = GsonHelper.getAsInt(json, "amount");

        if (amount <= 0) {
            throw new JsonSyntaxException(
                    "Fluid amount must be greater than zero: " + amount
            );
        }

        FluidStack fluidStack = new FluidStack(fluid, amount);

        if (json.has("nbt")) {
            fluidStack.setTag(
                    parseNbt(json.get("nbt"), "fluid")
            );
        }

        return new CompoundChanceResult(fluidStack);
    }

    private static CompoundTag parseNbt(
            JsonElement element,
            String resultType
    ) {
        try {
            String nbtString = element.isJsonObject()
                    ? GSON.toJson(element)
                    : GsonHelper.convertToString(element, "nbt");

            return TagParser.parseTag(nbtString);
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException(
                    "Invalid " + resultType + " result NBT",
                    exception
            );
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(hasFluid());

        if (hasFluid()) {
            fluid.writeToPacket(buf);
            return;
        }

        buf.writeItem(stack);
        buf.writeVarInt(minium);
        buf.writeVarInt(maximum);
        buf.writeFloat(chance);
    }

    public static CompoundChanceResult read(FriendlyByteBuf buf) {
        boolean isFluidResult = buf.readBoolean();

        if (isFluidResult) {
            FluidStack fluid = FluidStack.readFromPacket(buf);
            return new CompoundChanceResult(fluid);
        }

        ItemStack stack = buf.readItem();
        int minium = buf.readVarInt();
        int maximum = buf.readVarInt();
        float chance = buf.readFloat();

        return new CompoundChanceResult(
                stack,
                minium,
                maximum,
                chance
        );
    }
}