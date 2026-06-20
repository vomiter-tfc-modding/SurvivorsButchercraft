package com.vomiter.survivorsbutchercraft.util;

import net.minecraft.util.RandomSource;

import java.util.List;

public final class WeightedPool {

        public static <T extends WeightedEntry> T pick(List<T> entries, RandomSource random) {
            if (entries.isEmpty()) {
                return null;
            }

            int totalWeight = 0;

            for (T entry : entries) {
                int weight = entry.weight();

                if (weight <= 0) {
                    continue;
                }

                totalWeight += weight;
            }

            if (totalWeight <= 0) {
                return null;
            }

            int value = random.nextInt(totalWeight);

            for (T entry : entries) {
                int weight = entry.weight();

                if (weight <= 0) {
                    continue;
                }

                value -= weight;

                if (value < 0) {
                    return entry;
                }
            }

            return entries.get(entries.size() - 1);
        }

        public interface WeightedEntry {
            int weight();
        }
    }
