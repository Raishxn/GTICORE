package com.raishxn.gticore.common.block;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

public interface BlockMap {

    Object2ObjectOpenHashMap<String, Lazy<Block[]>> tierBlockMap = new Object2ObjectOpenHashMap<>(4);

    Int2ObjectOpenHashMap<Supplier<?>> scMap = new Int2ObjectOpenHashMap<>(4);
    Int2ObjectOpenHashMap<Supplier<?>> sepmMap = new Int2ObjectOpenHashMap<>(8);
    Int2ObjectOpenHashMap<Supplier<?>> calMap = new Int2ObjectOpenHashMap<>(16);

    static void init() {
        tierBlockMap.put("sc", Lazy.of(() -> scMap.int2ObjectEntrySet().stream()
                .sorted(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey))
                .map(Int2ObjectMap.Entry::getValue).map(Supplier::get).toArray(Block[]::new)));
        tierBlockMap.put("sepm", Lazy.of(() -> sepmMap.int2ObjectEntrySet().stream()
                .sorted(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey))
                .map(Int2ObjectMap.Entry::getValue).map(Supplier::get).toArray(Block[]::new)));
        tierBlockMap.put("cal", Lazy.of(() -> calMap.int2ObjectEntrySet().stream()
                .sorted(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey))
                .map(Int2ObjectMap.Entry::getValue).map(Supplier::get).toArray(Block[]::new)));
        tierBlockMap.put("coil", Lazy.of(() -> GTCEuAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().getCoilTemperature()))
                .map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new)));
    }
}
