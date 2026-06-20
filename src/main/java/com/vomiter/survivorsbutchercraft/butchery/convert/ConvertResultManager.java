package com.vomiter.survivorsbutchercraft.butchery.convert;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.util.WeightedPool;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConvertResultManager extends SimpleJsonResourceReloadListener {
    public static final ConvertResultManager INSTANCE = new ConvertResultManager();

    private static final Logger LOGGER = SurvivorsButchercraft.LOGGER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String DIRECTORY = "survivorsbutchercraft/butcher_converts";

    private Map<ResourceLocation, ConvertEntry> entries = Map.of();

    private ConvertResultManager() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> jsonMap,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profiler
    ) {
        ImmutableMap.Builder<ResourceLocation, ConvertEntry> builder = ImmutableMap.builder();
        if(!FMLEnvironment.production){
            builder.put(
                    Helpers.id("debug"),
                    new ConvertEntry(
                            Ingredient.of(ButchercraftItems.SKINNING_KNIFE.get()),
                            new ExtraToolDamage(0),
                            List.of(
                                    new ResultConvert(
                                            Ingredient.of(SBItems.HIDES.get(Carcass.YAK).get()),
                                            Items.DIAMOND.getDefaultInstance(),
                                            1,
                                            false
                                    )
                            )
                    )
            );
        }

        for (var entry : jsonMap.entrySet()) {
            ResourceLocation id = entry.getKey();

            try {
                ConvertEntry convertEntry = parseEntry(id, entry.getValue());
                builder.put(id, convertEntry);
            } catch (Exception e) {
                LOGGER.error("[SurvivorsButchercraft] Failed to parse butcher convert entry '{}'", id, e);
            }
        }

        entries = builder.build();

        LOGGER.info("[SurvivorsButchercraft] Loaded {} butcher convert entries", entries.size());
    }

    public Map<ResourceLocation, ConvertEntry> entries() {
        return entries;
    }

    public Optional<ConvertEntry> get(ResourceLocation id) {
        return Optional.ofNullable(entries.get(id));
    }

    public List<ResultConvert> findMatchingResults(ItemStack tool, ItemStack from) {
        List<ResultConvert> matchedResults = new ArrayList<>();

        for (ConvertEntry entry : entries.values()) {
            if (!entry.toolPredicate().test(tool)) {
                continue;
            }

            matchedResults.addAll(findMatchingResults(entry, from));
        }

        return matchedResults;
    }

    public Optional<ResultConvert> rollFrom(List<ResultConvert> matchedResults, RandomSource random) {
        return Optional.ofNullable(WeightedPool.pick(matchedResults, random));
    }

    private static ConvertEntry parseEntry(ResourceLocation id, JsonElement json) {
        JsonObject root = GsonHelper.convertToJsonObject(json, "butcher convert entry");

        Ingredient toolPredicate = Ingredient.fromJson(GsonHelper.getAsJsonObject(root, "tool"));
        ExtraToolDamage extraToolDamage = parseExtraToolDamage(root);
        List<ResultConvert> results = parseResults(id, root);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("Convert entry must contain at least one result");
        }

        return new ConvertEntry(toolPredicate, extraToolDamage, results);
    }

    private static ExtraToolDamage parseExtraToolDamage(JsonObject root) {
        if (!root.has("extra_tool_damage")) {
            return new ExtraToolDamage(0);
        }

        JsonElement element = root.get("extra_tool_damage");

        if (element.isJsonPrimitive()) {
            int damage = GsonHelper.convertToInt(element, "extra_tool_damage");
            return new ExtraToolDamage(Math.max(0, damage));
        }

        JsonObject object = GsonHelper.convertToJsonObject(element, "extra_tool_damage");
        int damage = GsonHelper.getAsInt(object, "damage", 0);

        return new ExtraToolDamage(Math.max(0, damage));
    }

    private static List<ResultConvert> parseResults(ResourceLocation id, JsonObject root) {
        List<ResultConvert> results = new ArrayList<>();

        var array = GsonHelper.getAsJsonArray(root, "results");

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = GsonHelper.convertToJsonObject(
                    array.get(i),
                    "result[" + i + "] in " + id
            );

            Ingredient from = Ingredient.fromJson(GsonHelper.getAsJsonObject(object, "from"));
            boolean keep = GsonHelper.getAsBoolean(object, "keep", false);
            int weight = GsonHelper.getAsInt(object, "weight", 1);

            ItemStack to = ItemStack.EMPTY;
            if (object.has("to")) {
                to = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(object, "to"));
            }

            if (weight <= 0) {
                throw new IllegalArgumentException("Result weight must be positive in " + id + ", index " + i);
            }

            if (keep && !to.isEmpty()) {
                throw new IllegalArgumentException("Keep result must not define a non-empty target ItemStack in " + id + ", index " + i);
            }

            if (!keep && to.isEmpty()) {
                throw new IllegalArgumentException("Result target ItemStack cannot be empty in " + id + ", index " + i);
            }

            results.add(new ResultConvert(from, to, weight, keep));
        }

        return List.copyOf(results);
    }

    private static boolean hasMatchingResult(ConvertEntry entry, ItemStack from) {
        for (ResultConvert result : entry.results()) {
            if (result.from().test(from)) {
                return true;
            }
        }

        return false;
    }

    private static List<ResultConvert> findMatchingResults(ConvertEntry entry, ItemStack from) {
        List<ResultConvert> matched = new ArrayList<>();

        for (ResultConvert result : entry.results()) {
            if (result.from().test(from)) {
                matched.add(result);
            }
        }

        return matched;
    }
}