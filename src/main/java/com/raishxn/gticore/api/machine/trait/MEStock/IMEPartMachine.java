package com.raishxn.gticore.api.machine.trait.MEStock;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface IMEPartMachine {

    default @Nullable Object2LongMap<ItemStack> getMEItemMap() {
        return null;
    }

    default @NotNull List<FluidStack> getMEFluidList() {
        return List.of();
    }

    default Object2LongOpenHashMap<ItemStack> getItemMap() {
        return null;
    }

    default @NotNull List<FluidStack> getFluidList() {
        return List.of();
    }

    default void setChanged(boolean value) {}

    default boolean getChanged() {
        return false;
    }

    void onConfigChanged();
}
