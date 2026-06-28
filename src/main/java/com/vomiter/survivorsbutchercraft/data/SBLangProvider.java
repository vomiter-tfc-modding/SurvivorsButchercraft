package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class SBLangProvider extends LanguageProvider {
    public SBLangProvider(PackOutput output) {
        super(output, SurvivorsButchercraft.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlocks();
        addItems();
        add("itemGroup." + SurvivorsButchercraft.MODID, "Survivor's Butchery");
    }

    private void addBlocks() {
        for (RegistryObject<Block> entry : SBBlocks.BLOCKS.getEntries()) {
            Block block = entry.get();
            String path = entry.getId().getPath();
            add(block, toDisplayNameTFCLike(path));
        }
    }

    private void addItems() {
        for (RegistryObject<Item> entry : SBItems.ITEMS.getEntries()) {
            Item item = entry.get();

            // BlockItem 直接吃 block translation key。
            if (item instanceof BlockItem) {
                continue;
            }

            String path = entry.getId().getPath();
            add(item, toDisplayNameTFCLike(path));
        }
    }

    private static String toDisplayNameTFCLike(String path){
        String[] parts = path.split("/");
        if(parts.length == 2){
            String part0;
            if(parts[0].endsWith("head")) part0 = "head";
            else if (parts[0].endsWith("head_male")) {
                return "Male " + toDisplayName(parts[1]) + " " + toDisplayName("head");
            } else if (parts[0].endsWith("hide")) {
                part0 = "hide_carpet";
            }
            else part0 = parts[0];
            return toDisplayName(parts[1]) + " " + toDisplayName(part0);
        }
        return toDisplayName(path);
    }

    private static String toDisplayName(String path) {
        String normalized = path
                .replace('/', '_')
                .replace('-', '_')
                .toLowerCase(Locale.ROOT);

        String[] parts = normalized.split("_");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            if (!builder.isEmpty()) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }

        return builder.toString();
    }
}