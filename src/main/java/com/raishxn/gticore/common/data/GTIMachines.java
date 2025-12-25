package com.raishxn.gticore.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.integration.ae2.machine.*;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.raishxn.gticore.common.machine.multiblock.part.LargeSteamHatchPartMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTIMachines {

    public static final BiConsumer<ItemStack, List<Component>> GTI_MODIFY = (stack, components) -> components
            .add(Component.translatable("gticore.registry.modify")
                    .withStyle(style -> style.withColor(TooltipHelper.RAINBOW.getCurrent())));

    public static final BiConsumer<ItemStack, List<Component>> GTI_ADD = (stack, components) -> components
            .add(Component.translatable("gticore.registry.add")
                    .withStyle(style -> style.withColor(TooltipHelper.RAINBOW_SLOW.getCurrent())));
    static {
        REGISTRATE.creativeModeTab(() -> GTCreativeModeTabs.MACHINE);
    }

    public static final MachineDefinition LARGE_STEAM_HATCH = REGISTRATE
            .machine("large_steam_input_hatch", holder -> new LargeSteamHatchPartMachine(holder, IO.IN, 8192, false))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM)
            .tooltips(Component.translatable("gtceu.machine.large_steam_input_hatch.tooltip.0"),
                    Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                            8192 * FluidHelper.getBucket()),
                    Component.translatable("gtceu.machine.steam.steam_hatch.tooltip"))
            .tooltipBuilder(GTI_ADD)
            .register();
}
