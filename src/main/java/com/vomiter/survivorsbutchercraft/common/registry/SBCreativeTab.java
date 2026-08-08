package com.vomiter.survivorsbutchercraft.common.registry;

import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.vomiter.survivorsbutchercraft.SurvivorsButchercraft.MODID;

public class SBCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static void safeAccept(DeferredHolder<Item, ? extends Item> ro, CreativeModeTab.Output output){
        if(ro == null) return;
        output.accept(ro.get());
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID + ".main"))
                    .icon(() -> SBItems.BUTCHER_KNIVES.get(Metal.COPPER).get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        SBItems.ITEMS.getEntries().forEach(ro -> {
                            safeAccept(ro, output);
                        });
                    })
                    .build()
    );
}
