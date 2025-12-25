package com.raishxn.gticore.api.machine.trait.AECraft;

import appeng.api.networking.crafting.ICraftingProvider;
import net.minecraft.core.BlockPos;
import com.raishxn.gticore.common.machine.multiblock.part.ae.NotifiableMAHandlerTrait;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IMECraftIOPart extends ICraftingProvider {

    void init(@NotNull Set<BlockPos> proxies);

    @NotNull
    NotifiableMAHandlerTrait getNotifiableMAHandlerTrait();
}
