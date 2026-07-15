package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.items.KnifeItem;
import com.vomiter.survivorsbutchercraft.util.ThreadLocalFlags;
import net.dries007.tfc.common.TFCTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(KnifeItem.class)
public abstract class KnifeItemMixin {

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;" +
                            "<init>(Lnet/minecraft/world/item/Tier;" +
                            "IFLnet/minecraft/world/item/Item$Properties;)V"
            ),
            index = 0
    )
    private static Tier sbtfc$replaceTier(Tier original) {
        return ThreadLocalFlags.tierThreadLocal.get();
    }
}