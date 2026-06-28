package com.vomiter.survivorsbutchercraft.common.registry;

import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.Arrays;

import static com.vomiter.survivorsbutchercraft.SurvivorsButchercraft.MODID;

public class SBCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static void safeAccept(RegistryObject<Item> ro, CreativeModeTab.Output output){
        if(ro == null) return;
        output.accept(ro.get());
    }

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID + ".main"))
                    .icon(() -> SBItems.BUTCHER_KNIVES.get(Metal.Default.COPPER).get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        SBItems.ITEMS.getEntries().forEach(ro -> {
                            safeAccept(ro, output);
                        });
                    })
                    .build()
    );
}
