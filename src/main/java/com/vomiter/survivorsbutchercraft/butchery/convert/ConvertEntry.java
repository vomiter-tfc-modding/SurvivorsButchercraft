package com.vomiter.survivorsbutchercraft.butchery.convert;

import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record ConvertEntry(Ingredient toolPredicate, ExtraToolDamage extraToolDamage, List<ResultConvert> results) {
}
