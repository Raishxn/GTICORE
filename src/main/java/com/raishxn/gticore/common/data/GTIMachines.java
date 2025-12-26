package com.raishxn.gticore.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.integration.ae2.machine.*;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferProxyPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.raishxn.gticore.api.machine.multiblock.GTIPartAbility;
import com.raishxn.gticore.common.machine.multiblock.part.LargeSteamHatchPartMachine;
import com.raishxn.gticore.common.machine.multiblock.part.ae.*;
import net.minecraft.ChatFormatting;
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

    public static class GTAEMachines {

        public static final MachineDefinition ITEM_IMPORT_BUS_ME = GTRegistration.REGISTRATE
                .machine("me_input_bus", MEInputBusPartMachine::new)
                .langValue("ME Input Bus")
                .tier(4)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS)
                .overlayTieredHullModel("me_item_bus.import")
                .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                        Component.translatable("gtceu.machine.me.item_import.tooltip"),
                        Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition STOCKING_IMPORT_BUS_ME = GTRegistration.REGISTRATE
                .machine("me_stocking_input_bus", MEStockingBusPartMachine::new)
                .langValue("ME Stocking Input Bus")
                .tier(6)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS)
                .overlayTieredHullModel("me_item_bus.import")
                .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                        Component.translatable("gtceu.machine.me.stocking_item.tooltip.0"),
                        Component.translatable("gtceu.machine.me_import_item_hatch.configs.tooltip"),
                        Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                        Component.translatable("gtceu.machine.me.stocking_item.tooltip.1"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition ITEM_EXPORT_BUS_ME = GTRegistration.REGISTRATE
                .machine("me_output_bus", MEOutputBusPartMachine::new)
                .langValue("ME Output Bus")
                .tier(4)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_ITEMS)
                .overlayTieredHullModel("me_item_bus.export")
                .tooltips(Component.translatable("gtceu.machine.item_bus.export.tooltip"),
                        Component.translatable("gtceu.machine.me.item_export.tooltip"),
                        Component.translatable("gtceu.machine.me.export.tooltip"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition FLUID_IMPORT_HATCH_ME = GTRegistration.REGISTRATE
                .machine("me_input_hatch", MEInputHatchPartMachine::new)
                .langValue("ME Input Hatch")
                .tier(4)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_FLUIDS)
                .overlayTieredHullModel("me_fluid_hatch.import")
                .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"),
                        Component.translatable("gtceu.machine.me.fluid_import.tooltip"),
                        Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition STOCKING_IMPORT_HATCH_ME = GTRegistration.REGISTRATE
                .machine("me_stocking_input_hatch", MEStockingHatchPartMachine::new)
                .langValue("ME Stocking Input Hatch")
                .tier(6)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_FLUIDS)
                .overlayTieredHullModel("me_fluid_hatch.import")
                .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"),
                        Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.0"),
                        Component.translatable("gtceu.machine.me_import_fluid_hatch.configs.tooltip"),
                        Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                        Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.1"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition FLUID_EXPORT_HATCH_ME = GTRegistration.REGISTRATE
                .machine("me_output_hatch", MEOutputHatchPartMachine::new)
                .langValue("ME Output Hatch")
                .tier(4)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_FLUIDS)
                .overlayTieredHullModel("me_fluid_hatch.export")
                .tooltips(Component.translatable("gtceu.machine.fluid_hatch.export.tooltip"),
                        Component.translatable("gtceu.machine.me.fluid_export.tooltip"),
                        Component.translatable("gtceu.machine.me.export.tooltip"),
                        Component.translatable("gtceu.universal.enabled"))
                .register();

        public static final MachineDefinition ME_MINI_PATTERN_BUFFER = REGISTRATE
                .machine("me_mini_pattern_buffer", (h) -> new MEPatternBufferPartMachine(h, 9, IO.IN))
                .tier(5)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
                .overlayTieredHullModel("me_mini_pattern_buffer")
                .langValue("ME Mini Pattern Buffer")
                .tooltips(Component.translatable("block.gtceu.pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.1"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.2"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.3"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.4"),
                        Component.translatable("gtceu.machine.me_mini_pattern_buffer.desc.0"),
                        Component.translatable("block.gtceu.pattern_buffer.desc.2"),
                        Component.translatable("gtceu.universal.enabled"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_EXTEND_PATTERN_BUFFER = REGISTRATE
                .machine("me_extend_pattern_buffer", (h) -> new MEPatternBufferPartMachine(h, 36, IO.BOTH))
                .tier(8)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS)
                .overlayTieredHullModel("me_pattern_buffer")
                .langValue("ME Extend Pattern Buffer")
                .tooltips(Component.translatable("block.gtceu.pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.1"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.2"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.3"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.4"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.5"),
                        Component.translatable("block.gtceu.pattern_buffer.desc.2"),
                        Component.translatable("gtceu.universal.enabled"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_FINAL_PATTERN_BUFFER = REGISTRATE
                .machine("me_final_pattern_buffer", (h) -> new MEPatternBufferPartMachine(h, 72, IO.BOTH))
                .tier(10)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS)
                .overlayTieredHullModel("me_pattern_buffer")
                .langValue("ME Final Pattern Buffer")
                .tooltips(Component.translatable("tooltip.gtlcore.bigger_stronger").withStyle(ChatFormatting.GOLD),
                        Component.translatable("block.gtceu.pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.0"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.1"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.2"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.3"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.4"),
                        Component.translatable("gtceu.machine.me_pattern_buffer.desc.5"),
                        Component.translatable("block.gtceu.pattern_buffer.desc.2"),
                        Component.translatable("gtceu.universal.enabled"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_PATTERN_BUFFER_PROXY = REGISTRATE
                .machine("me_pattern_buffer_proxy", MEPatternBufferProxyPartMachine::new)
                .tier(8)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS)
                .overlayTieredHullModel("me_pattern_buffer_proxy")
                .langValue("ME Pattern Buffer Proxy")
                .tooltips(Component.translatable("block.gtceu.pattern_buffer_proxy.desc.0"),
                        Component.translatable("block.gtceu.pattern_buffer_proxy.desc.1"),
                        Component.translatable("block.gtceu.pattern_buffer_proxy.desc.2"),
                        Component.translatable("gtceu.machine.me_pattern_buffer_proxy.desc.0"),
                        Component.translatable("gtceu.universal.enabled"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_EXTENDED_EXPORT_BUFFER = REGISTRATE
                .machine("me_extended_export_buffer", MEExtendedOutputPartMachine::new)
                .tier(9)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS)
                .langValue("ME Extended Export Buffer")
                .tooltips(Component.translatable("gtmthings.machine.me_export_buffer.tooltip"),
                        Component.translatable("gtceu.machine.me_extended_export_buffer.tooltip.0"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_EXTENDED_ASYNC_EXPORT_BUFFER = REGISTRATE
                .machine("me_extended_async_export_buffer", MEExtendedAsyncOutputPartMachine::new)
                .tier(9)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS)
                .langValue("ME Extended Async Export Buffer")
                .tooltips(Component.translatable("gtmthings.machine.me_export_buffer.tooltip"),
                        Component.translatable("gtceu.machine.me_extended_async_export_buffer.tooltip.0"),
                        Component.translatable("gtceu.machine.me_extended_async_export_buffer.tooltip.1"))
                .tooltipBuilder(GTI_ADD)
                .register();
        public static final MachineDefinition ME_CRAFT_SPEED_CORE = REGISTRATE
                .machine("me_craft_speed_core", MECraftSpeedCorePartMachine::new)
                .rotationState(RotationState.ALL)
                .abilities(GTIPartAbility.MOLECULAR_ASSEMBLER_MATRIX)
                .workableCasingModel(GTCEu.id("block/casings/speed_core_casing"), GTCEu.id("block/casings/speed_core_casing"))
                .langValue("ME CRAFT Speed Core")
                .tooltips(Component.translatable("gtceu.universal.disabled"),
                        Component.translatable("gtceu.machine.me_craft_speed_core.tooltip.0"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_CRAFT_PARALLEL_CORE = REGISTRATE
                .machine("me_craft_parallel_core", MECraftParallelCorePartMachine::new)
                .rotationState(RotationState.ALL)
                .abilities(GTIPartAbility.MOLECULAR_ASSEMBLER_MATRIX)
                .workableCasingModel(GTCEu.id("block/casings/crafter_core_casing"), GTCEu.id("block/casings/crafter_core_casing"))
                .langValue("ME CRAFT Parallel Core")
                .tooltips(Component.translatable("gtceu.universal.disabled"),
                        Component.translatable("gtceu.machine.me_craft_parallel_core.tooltip.0"))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_CRAFT_PATTERN_CONTAINER = REGISTRATE.machine("me_craft_pattern_container", MECraftPatternContainerPartMachine::new)
                .rotationState(RotationState.ALL)
                .abilities(GTIPartAbility.MOLECULAR_ASSEMBLER_MATRIX)
                .workableCasingModel(GTCEu.id("block/casings/pattern_core_casing"), GTCEu.id("block/casings/pattern_core_casing"))
                .langValue("ME Craft Pattern Container")
                .tooltips(Component.translatable("gtceu.universal.disabled"), Component.translatable("gtceu.machine.me_craft_pattern_core.tooltip",
                        Component.literal(FormattingUtil.formatNumbers(12 * 9)).withStyle(ChatFormatting.GOLD)))
                .tooltipBuilder(GTI_ADD)
                .register();

        public static final MachineDefinition ME_MOLECULAR_ASSEMBLER_IO = REGISTRATE
                .machine("me_molecular_assembler_io", MEMolecularAssemblerIOPartMachine::new)
                .tier(8)
                .rotationState(RotationState.ALL)
                .langValue("ME Molecular Assembler IO")
                .tooltips(Component.translatable("gtceu.universal.disabled"),
                        Component.translatable("gtceu.machine.me_molecular_assembler_io.tooltip.0"),
                        Component.translatable("gtceu.machine.me_molecular_assembler_io.tooltip.1"))
                .tooltipBuilder(GTI_ADD)
                .register();
        public static void init() {}
    }
}
