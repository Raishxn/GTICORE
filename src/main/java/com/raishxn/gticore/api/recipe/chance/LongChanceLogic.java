package com.raishxn.gticore.api.recipe.chance;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

import static com.raishxn.gticore.api.recipe.IAdvancedContentModifier.preciseDivision;

public abstract class LongChanceLogic extends ChanceLogic {

    public static final LongChanceLogic OR;

    public LongChanceLogic(String id) {
        super(id);
    }

    public static int getChance(@NotNull Content entry, @NotNull ChanceBoostFunction boostFunction, int baseTier, int machineTier) {
        return boostFunction.getBoostedChance(entry, baseTier, machineTier);
    }

    public static int getCachedChance(Content entry, @Nullable Object2IntMap<?> cache) {
        if (cache == null) return GTValues.RNG.nextInt(entry.maxChance);
        return cache.getOrDefault(entry.content, GTValues.RNG.nextInt(entry.maxChance));
    }

    @SuppressWarnings("all")
    public static void updateCachedChance(Object ingredient, @Nullable Object2IntMap<?> cache, int chance) {
        if (cache != null) {
            ((Object2IntMap) cache).put(ingredient, chance);
        }
    }

    public static void modifyByChance(@Nullable Object2IntMap<?> cache, long times, RecipeCapability<?> cap, List<Content> out, Content entry, int maxChance, long totalChance) {
        long guaranteed = totalChance / maxChance;
        if (guaranteed > 0) out.add(entry.copy(cap, preciseDivision(guaranteed, times)));
        int newChance = (int) (totalChance % maxChance);

        int cached = getCachedChance(entry, cache);
        int chance = newChance + cached;
        if (chance >= maxChance) {
            do {
                out.add(entry.copy(cap, preciseDivision(1, times)));
                chance -= maxChance;
                newChance -= maxChance;
            } while (chance >= maxChance);
        }

        updateCachedChance(entry.content, cache, newChance / 2 + cached);
    }

    @Nullable
    @Unmodifiable
    public abstract List<@NotNull Content> roll(
            @NotNull @Unmodifiable List<@NotNull Content> chancedEntries,
            @NotNull ChanceBoostFunction boostFunction,
            int baseTier, int machineTier,
            @Nullable Object2IntMap<?> cache,
            long times, // <--- Versão LONG
            RecipeCapability<?> cap);

    static {
        GTRegistries.CHANCE_LOGICS.unfreeze();
        OR = new LongChanceLogic("longOr") {

            // 1. Implementação da sua versão CUSTOMIZADA (long times)
            // A assinatura aqui deve bater com o método abstract que você criou logo acima
            @Override
            public @Nullable @Unmodifiable List<@NotNull Content> roll(@NotNull List<Content> chancedEntries,
                                                                       @NotNull ChanceBoostFunction boostFunction,
                                                                       int baseTier,
                                                                       int machineTier,
                                                                       @Nullable Object2IntMap<?> cache,
                                                                       long times,
                                                                       RecipeCapability<?> cap) {
                List<Content> out = new ObjectArrayList<>(chancedEntries.size());

                for (Content entry : chancedEntries) {
                    int maxChance = entry.maxChance;

                    int newChance = getChance(entry, boostFunction, baseTier, machineTier);
                    // Lógica para Long
                    long totalChance = times * newChance;
                    modifyByChance(cache, times, cap, out, entry, maxChance, totalChance);
                }

                return out.isEmpty() ? null : out;
            }

            // 2. Implementação da versão ORIGINAL do GTCEu (int times)
            // O erro ocorria aqui. O RecipeCapability TEM que ser o primeiro argumento.
            @Override
            public @Nullable @Unmodifiable List<@NotNull Content> roll(RecipeCapability<?> cap, // <--- CORREÇÃO: Cap vem primeiro
                                                                       @NotNull List<Content> chancedEntries,
                                                                       @NotNull ChanceBoostFunction boostFunction,
                                                                       int baseTier,
                                                                       int machineTier,
                                                                       @Nullable Object2IntMap<?> cache,
                                                                       int times) {
                // Redireciona para a lógica do long para evitar duplicar código,
                // ou implementa diretamente se preferir performance.
                return this.roll(chancedEntries, boostFunction, baseTier, machineTier, cache, (long) times, cap);
            }

            @Override
            public @NotNull Component getTranslation() {
                return Component.translatable("gtceu.chance_logic.or");
            }

            public String toString() {
                return "LongChanceLogic{OR}";
            }
        };
        GTRegistries.CHANCE_LOGICS.freeze();
    }
}