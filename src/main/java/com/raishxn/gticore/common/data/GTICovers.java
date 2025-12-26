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

    // --- ROBOT ARMS (Braços Robóticos) ---

    // 1. Robot Arm ULV (Primitive) - Corrigido para transferir POUCO (ex: 1 stack)
    public static final CoverDefinition ROBOT_ARM_ULV = GTCovers.register(
            ResourceLocation.parse("robot_arm.ulv"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.ULV) {
                @Override
                public int getTransferRate() {
                    // CORREÇÃO: Define um valor fixo baixo ou proporcional ao Tier
                    // GTValues.ULV = 0.
                    // (0 + 1) * 64 = 64 itens por operação (1 Pack)
                    return 64 * 16;
                }
            },
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));

    // 2. Robot Arm MAX - Corrigido para transferir MUITO (ex: 16 stacks ou exponencial)
    public static final CoverDefinition ROBOT_ARM_MAX = GTCovers.register(
            ResourceLocation.parse("robot_arm.max"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.MAX) {
                @Override
                public int getTransferRate() {
                    // CORREÇÃO: Valor alto para o tier MAX
                    // Exemplo: 64 * 64 = 4096 itens (64 packs) por operação
                    return 64 * 100;
                }
            },
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));


    // --- CONVEYORS (Esteiras) ---
    // Caso você tenha esteiras, use a mesma lógica:

    public static final CoverDefinition CONVEYOR_MODULE_ULV = GTCovers.register(
            ResourceLocation.parse("conveyor.ulv"),
            (def, coverable, side) -> new ConveyorCover(def, coverable, side, GTValues.ULV) {
                @Override
                public int getTransferRate() {
                    return 64 * 16; // 1 Stack
                }
            },
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/conveyor"),
                    null,
                    GTCEu.id("block/cover/conveyor_emissive"),
                    GTCEu.id("block/cover/conveyor_inverted_emissive")));

    public static final CoverDefinition CONVEYOR_MODULE_MAX = GTCovers.register(
            ResourceLocation.parse("conveyor.max"),
            (def, coverable, side) -> new ConveyorCover(def, coverable, side, GTValues.MAX) {
                @Override
                public int getTransferRate() {
                    return 64 * 100; // 64 Stacks (muito rápido)
                }
            },
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/conveyor"),
                    null,
                    GTCEu.id("block/cover/conveyor_emissive"),
                    GTCEu.id("block/cover/conveyor_inverted_emissive")));


    // --- OUTROS COVERS JÁ EXISTENTES NO SEU ARQUIVO ---

    public static final CoverDefinition ELECTRIC_PUMP_MAX = GTCovers.register(
            ResourceLocation.parse("pump.max"),
            (def, coverable, side) -> new PumpCover(def, coverable, side, GTValues.MAX), // Pump geralmente escala bem sozinho, mas pode precisar de override se estiver lento
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    public static final CoverDefinition FLUID_REGULATORS_ULV = GTCovers.register(
            ResourceLocation.parse("fluid_regulator.ulv"),
            (def, coverable, side) -> new FluidRegulatorCover(def, coverable, side, GTValues.ULV),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    public final static CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE = registerTieredWirelessCover(
            "wireless_energy_receive", 1, GTValues.MAX);

    public final static CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE_4A = registerTieredWirelessCover(
            "4a_wireless_energy_receive", 4, GTValues.MAX);

    public static CoverDefinition registerTieredWirelessCover(String id, int amperage, int tier) {
        String name = id + "." + GTValues.VN[tier].toLowerCase(Locale.ROOT);
        return GTCovers.register(ResourceLocation.parse(name),
                (holder, coverable, side) -> new WirelessEnergyReceiveCover(holder, coverable, side, tier, amperage),
                () -> () -> new SimpleCoverRenderer(GTICORE.id("block/cover/overlay_" + (amperage == 1 ? "" : amperage + "a_") + id),
                        GTICORE.id("item/" + (amperage == 1 ? "" : amperage + "a_") + id)));
    }

    public static void init() {
        // Chamada de inicialização
    }
}