package com.raishxn.gticore.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.client.renderer.cover.IOCoverRenderer;
import com.gregtechceu.gtceu.client.renderer.cover.SimpleCoverRenderer;
import com.gregtechceu.gtceu.common.cover.ConveyorCover;
import com.gregtechceu.gtceu.common.cover.FluidRegulatorCover;
import com.gregtechceu.gtceu.common.cover.PumpCover;
import com.gregtechceu.gtceu.common.cover.RobotArmCover;
import com.gregtechceu.gtceu.common.data.GTCovers;
import com.hepdd.gtmthings.common.cover.WirelessEnergyReceiveCover;
import com.raishxn.gticore.GTICORE;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class GTICovers {

    // Fix: Adicionado o renderizador correto (IOCoverRenderer) envolto em lambdas () -> () ->
    public static final CoverDefinition ELECTRIC_PUMP_MAX = GTCovers.register(
            ResourceLocation.parse("pump.max"),
            (def, coverable, side) -> new PumpCover(def, coverable, side, GTValues.MAX),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);
    public static final CoverDefinition FLUID_REGULATORS_ULV = GTCovers.register(
            ResourceLocation.parse("fluid_regulator.ulv"),
            (def, coverable, side) -> new FluidRegulatorCover(def, coverable, side, GTValues.ULV),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);
    public static final CoverDefinition CONVEYOR_MODULE_MAX = GTCovers.register(
            ResourceLocation.parse("conveyor.max"),
            (def, coverable, side) -> new ConveyorCover(def, coverable, side, GTValues.MAX),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/conveyor"),
                    null,
                    GTCEu.id("block/cover/conveyor_emissive"),
                    GTCEu.id("block/cover/conveyor_inverted_emissive")));
    public static final CoverDefinition ROBOT_ARM_MAX = GTCovers.register(
            ResourceLocation.parse("robot_arm.max"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.MAX),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));
    public static final CoverDefinition ROBOT_ARM_ULV = GTCovers.register(
            ResourceLocation.parse("robot_arm.ulv"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.ULV),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));

    public final static CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE = registerTieredWirelessCover(
            "wireless_energy_receive", 1, GTValues.MAX);

    public final static CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE_4A = registerTieredWirelessCover(
            "4a_wireless_energy_receive", 4, GTValues.MAX);

    public static CoverDefinition registerTieredWirelessCover(String id, int amperage, int tier) {
        String name = id + "." + GTValues.VN[tier].toLowerCase(Locale.ROOT);
        return GTCovers.register(ResourceLocation.parse(name),
                (holder, coverable, side) -> new WirelessEnergyReceiveCover(holder, coverable, side, tier, amperage),
                () -> () -> new SimpleCoverRenderer(GTICORE.id("block/cover/overlay_" + (amperage == 1 ? "" : "4a_") + "wireless_energy_receive")));
    }

    public static void init() {}
}