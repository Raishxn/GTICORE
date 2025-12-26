package com.raishxn.gticore.common.data;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.models.GTModels;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.raishxn.gticore.GTICORE;
import com.raishxn.gticore.common.block.GTICoilBlock;
import com.raishxn.gticore.common.blockentity.GTICoilBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import com.raishxn.gticore.common.block.BlockMap;
import com.raishxn.gticore.common.block.CraftingUnitType;
import com.raishxn.gticore.common.block.GTIFusionCasingBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.common.data.GTBlocks.ALL_FUSION_CASINGS;
import static com.raishxn.gticore.api.registry.GTIRegistry.REGISTRATE;

public class GTIBlocks {

    public static void init() {
        BlockMap.init();
        for (int i = 1; i < 15; i++) {
            GTIBlocks.createTierCasings("component_assembly_line_casing_" + GTValues.VN[i].toLowerCase(),
                    GTICORE.id("block/casings/component_assembly_line/component_assembly_line_casing_" + GTValues.VN[i].toLowerCase()), BlockMap.calMap, i);
        }
    }

    static {
        REGISTRATE.creativeModeTab(() -> GTICreativeModeTabs.GTI_CORE_BLOCKS);
    }

    private static BlockEntry<CraftingUnitBlock> registerCraftingUnitBlock(int tier, CraftingUnitType Type) {
        return REGISTRATE
                .block(tier == -1 ? "max_storage" : tier + "m_storage",
                        p -> new CraftingUnitBlock(Type))
                .blockstate((ctx, provider) -> {
                    String formed = "block/crafting/" + ctx.getName() + "_formed";
                    String unformed = "block/crafting/" + ctx.getName();
                    provider.models().cubeAll(unformed, provider.modLoc("block/crafting/" + ctx.getName()));
                    provider.models().getBuilder(formed);
                    provider.getVariantBuilder(ctx.get())
                            .forAllStatesExcept(state -> {
                                boolean b = state.getValue(AbstractCraftingUnitBlock.FORMED);
                                return ConfiguredModel.builder()
                                        .modelFile(provider.models()
                                                .getExistingFile(provider.modLoc(b ? formed : unformed)))
                                        .build();
                            }, AbstractCraftingUnitBlock.POWERED);
                })
                .defaultLoot()
                .item(BlockItem::new)
                .model((ctx, provider) -> provider.withExistingParent(ctx.getName(),
                        provider.modLoc("block/crafting/" + ctx.getName())))
                .build()
                .register();
    }

    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_1M = registerCraftingUnitBlock(1,
            CraftingUnitType.STORAGE_1M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_4M = registerCraftingUnitBlock(4,
            CraftingUnitType.STORAGE_4M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_16M = registerCraftingUnitBlock(16,
            CraftingUnitType.STORAGE_16M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_64M = registerCraftingUnitBlock(64,
            CraftingUnitType.STORAGE_64M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_256M = registerCraftingUnitBlock(256,
            CraftingUnitType.STORAGE_256M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_MAX = registerCraftingUnitBlock(-1,
            CraftingUnitType.STORAGE_MAX);

    public static BlockEntityEntry<CraftingBlockEntity> CRAFTING_STORAGE = REGISTRATE
            .blockEntity("crafting_storage", CraftingBlockEntity::new)
            .validBlocks(
                    CRAFTING_STORAGE_1M,
                    CRAFTING_STORAGE_4M,
                    CRAFTING_STORAGE_16M,
                    CRAFTING_STORAGE_64M,
                    CRAFTING_STORAGE_256M,
                    CRAFTING_STORAGE_MAX)
            .onRegister(type -> {
                for (CraftingUnitType craftingUnitType : CraftingUnitType.values()) {
                    AEBaseBlockEntity.registerBlockEntityItem(type, craftingUnitType.getItemFromType());
                    craftingUnitType.getDefinition().get().setBlockEntity(CraftingBlockEntity.class, type, null, null);
                }
            })
            .register();

    @SuppressWarnings("all")
    public static BlockEntry<ActiveBlock> createActiveCasing(String name, String baseModelPath) {
        return REGISTRATE.block(name, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK).addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(GTICORE.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTICORE.id(baseModelPath)))
                .build()
                .register();
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createTierCasings(String name, ResourceLocation texture,
                                                      Int2ObjectMap<Supplier<?>> map, int tier) {
        BlockEntry<Block> Block = REGISTRATE.block(name, p -> (Block) new Block(p) {
                    @Override
                    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level,
                                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                        tooltip.add(Component.translatable("gtceu.casings.tier", tier));
                    }
                })
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
        REGISTRATE.setCreativeTab(Block, GTICreativeModeTabs.GTI_CORE_BLOCKS);
        map.put(tier, Block);
        return Block;
    }

    @SuppressWarnings("all")
    public static BlockEntry<ActiveBlock> createActiveTierCasing(String name, String baseModelPath,
                                                                 Int2ObjectMap<Supplier<?>> map, int tier) {
        BlockEntry<ActiveBlock> Block = REGISTRATE.block("%s".formatted(name), p -> (ActiveBlock) new ActiveBlock(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level,
                                        @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                tooltip.add(Component.translatable("gtceu.casings.tier", tier));
            }
        })
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(GTICORE.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTICORE.id(baseModelPath)))
                .build()
                .register();
        REGISTRATE.setCreativeTab(Block, GTICreativeModeTabs.GTI_CORE_BLOCKS);
        map.put(tier, Block);
        return Block;
    }

    @SuppressWarnings("all")
    private static BlockEntry<Block> createCleanroomFilter(IFilterType filterType) {
        var filterBlock = REGISTRATE.block(filterType.getSerializedName(), Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(2.0f, 8.0f).sound(SoundType.METAL)
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[1])
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.CLEANROOM_FILTERS.put(filterType, filterBlock);
        return filterBlock;
    }

    private static BlockEntry<Block> createGlassCasingBlock(String name, ResourceLocation texture,
                                                            Supplier<Supplier<RenderType>> type) {
        return createCasingBlock(name, GlassBlock::new, texture, () -> Blocks.GLASS, type);
    }

    public static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture) {
        return createCasingBlock(name, Block::new, texture, () -> Blocks.IRON_BLOCK,
                () -> RenderType::cutoutMipped);
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createCasingBlock(String name,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      ResourceLocation texture,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type) {
        return REGISTRATE.block(name, blockSupplier)
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    @SuppressWarnings("all")
    private static BlockEntry<FusionCasingBlock> createFusionCasing(IFusionCasingType casingType) {
        BlockEntry<FusionCasingBlock> casingBlock = REGISTRATE
                .block(casingType.getSerializedName(), p -> (FusionCasingBlock) new GTIFusionCasingBlock(p, casingType))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(5.0f, 10.0f).sound(SoundType.METAL))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ActiveBlock block = ctx.getEntry();
                    ModelFile inactive = prov.models().getExistingFile(GTICORE.id(casingType.getSerializedName()));
                    ModelFile active = prov.models().getExistingFile(GTICORE.id(casingType.getSerializedName()).withSuffix("_active"));
                    prov.getVariantBuilder(block)
                            .partialState()
                            .with(GTBlockStateProperties.ACTIVE, false) // AQUI
                            .modelForState().modelFile(inactive).addModel()
                            .partialState()
                            .with(GTBlockStateProperties.ACTIVE, true)  // E AQUI
                            .modelForState().modelFile(active).addModel();
                })
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[casingType.getHarvestLevel()])
                .item(BlockItem::new)
                .build()
                .register();
        ALL_FUSION_CASINGS.put(casingType, casingBlock);
        return casingBlock;
    }

    public static final BlockEntityEntry<GTICoilBlockEntity> GTI_COIL_BLOCK_ENTITY = REGISTRATE
            .blockEntity("gti_coil_block", GTICoilBlockEntity::new)
            .register();

    // Registro dos Blocos de Bobina
    public static final BlockEntry<GTICoilBlock> ABYSSALALLOY_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.ABYSSALALLOY);
    public static final BlockEntry<GTICoilBlock> TITANSTEEL_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.TITANSTEEL);
    public static final BlockEntry<GTICoilBlock> STARMETAL_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.STARMETAL);
    public static final BlockEntry<GTICoilBlock> NAQUADRIATICTARANIUM_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.NAQUADRIATICTARANIUM);
    public static final BlockEntry<GTICoilBlock> HYPOGEN_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.HYPOGEN);
    public static final BlockEntry<GTICoilBlock> ETERNITY_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.ETERNITY);
    public static final BlockEntry<GTICoilBlock> INFINITY_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.INFINITY);
    public static final BlockEntry<GTICoilBlock> URUIUM_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.URUIUM);
    public static final BlockEntry<GTICoilBlock> ADAMANTINE_COIL_BLOCK = createCoilBlock(GTICoilBlock.CoilType.ADAMANTINE);

    private static BlockEntry<GTICoilBlock> createCoilBlock(GTICoilBlock.CoilType coilType) {
        var builder = REGISTRATE.block(coilType.getName() + "_coil_block", p -> new GTICoilBlock(p, coilType))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.strength(5.0f, 10.0f).requiresCorrectToolForDrops())
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
        if (GTToolType.WRENCH.harvestTags != null) {
            for (TagKey<Block> tag : GTToolType.WRENCH.harvestTags) {
                builder.tag(tag);
            }
        }
        return builder
                .item()
                .build()
                .register();
    }





    @SuppressWarnings("all")
    private static BlockEntry<Block> createHermeticCasing(int tier) {
        String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
        return REGISTRATE
                .block("%s_hermetic_casing".formatted(tierName), Block::new)
                .lang("Hermetic Casing %s".formatted(GTValues.LVT[tier]))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static final BlockEntry<Block> HERMETIC_CASING_UEV = createHermeticCasing(GTValues.UEV);
    public static final BlockEntry<Block> HERMETIC_CASING_UIV = createHermeticCasing(GTValues.UIV);
    public static final BlockEntry<Block> HERMETIC_CASING_UXV = createHermeticCasing(GTValues.UXV);
    public static final BlockEntry<Block> HERMETIC_CASING_OpV = createHermeticCasing(GTValues.OpV);





    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK4 = createFusionCasing(
            GTIFusionCasingBlock.CasingType.FUSION_CASING_MK4);
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK5 = createFusionCasing(
            GTIFusionCasingBlock.CasingType.FUSION_CASING_MK5);

    public static final BlockEntry<ActiveBlock> ADVANCED_FUSION_COIL = createActiveCasing("advanced_fusion_coil",
            "block/variant/advanced_fusion_coil");
    public static final BlockEntry<ActiveBlock> FUSION_COIL_MK2 = createActiveCasing("fusion_coil_mk2",
            "block/variant/fusion_coil_mk2");
    public static final BlockEntry<ActiveBlock> IMPROVED_SUPERCONDUCTOR_COIL = createActiveCasing(
            "improved_superconductor_coil", "block/variant/improved_superconductor_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL = createActiveCasing("compressed_fusion_coil",
            "block/variant/compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> ADVANCED_COMPRESSED_FUSION_COIL = createActiveCasing(
            "advanced_compressed_fusion_coil", "block/variant/advanced_compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2_PROTOTYPE = createActiveCasing(
            "compressed_fusion_coil_mk2_prototype", "block/variant/compressed_fusion_coil_mk2_prototype");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2 = createActiveCasing(
            "compressed_fusion_coil_mk2", "block/variant/compressed_fusion_coil_mk2");


    public static final BlockEntry<Block> CASING_SUPERCRITICAL_TURBINE = createCasingBlock(
            "supercritical_turbine_casing", GTICORE.id("block/supercritical_turbine_casing"));
    public static final BlockEntry<Block> MULTI_FUNCTIONAL_CASING = createCasingBlock(
            "multi_functional_casing", GTICORE.id("block/multi_functional_casing"));
    public static final BlockEntry<Block> CREATE_CASING = createCasingBlock(
            "create_casing", GTICORE.id("block/create_casing"));
    public static final BlockEntry<Block> SPACE_ELEVATOR_MECHANICAL_CASING = createCasingBlock(
            "space_elevator_mechanical_casing", GTICORE.id("block/space_elevator_mechanical_casing"));
    public static final BlockEntry<Block> MANIPULATOR = createCasingBlock(
            "manipulator", GTICORE.id("block/manipulator"));
    public static final BlockEntry<Block> BLAZE_BLAST_FURNACE_CASING = createCasingBlock(
            "blaze_blast_furnace_casing", GTICORE.id("block/blaze_blast_furnace_casing"));
    public static final BlockEntry<Block> COLD_ICE_CASING = createCasingBlock(
            "cold_ice_casing", GTICORE.id("block/cold_ice_casing"));
    public static final BlockEntry<Block> DIMENSION_CONNECTION_CASING = createCasingBlock(
            "dimension_connection_casing", GTICORE.id("block/dimension_connection_casing"));
    public static final BlockEntry<Block> MOLECULAR_CASING = createCasingBlock(
            "molecular_casing", GTICORE.id("block/molecular_casing"));

    public static final BlockEntry<Block> DIMENSION_INJECTION_CASING = createCasingBlock(
            "dimension_injection_casing", GTICORE.id("block/casings/dimension_injection_casing"));
    public static final BlockEntry<Block> DIMENSIONALLY_TRANSCENDENT_CASING = createCasingBlock(
            "dimensionally_transcendent_casing", GTICORE.id("block/casings/dimensionally_transcendent_casing"));
    public static final BlockEntry<Block> ECHO_CASING = createCasingBlock(
            "echo_casing", GTICORE.id("block/casings/echo_casing"));
    public static final BlockEntry<Block> DRAGON_STRENGTH_TRITANIUM_CASING = createCasingBlock(
            "dragon_strength_tritanium_casing", GTICORE.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> ALUMINIUM_BRONZE_CASING = createCasingBlock(
            "aluminium_bronze_casing", GTICORE.id("block/casings/aluminium_bronze_casing"));
    public static final BlockEntry<Block> ANTIFREEZE_HEATPROOF_MACHINE_CASING = createCasingBlock(
            "antifreeze_heatproof_machine_casing", GTICORE.id("block/casings/antifreeze_heatproof_machine_casing"));
    public static final BlockEntry<Block> ENHANCE_HYPER_MECHANICAL_CASING = createCasingBlock(
            "enhance_hyper_mechanical_casing", GTICORE.id("block/casings/enhance_hyper_mechanical_casing"));
    public static final BlockEntry<Block> EXTREME_STRENGTH_TRITANIUM_CASING = createCasingBlock(
            "extreme_strength_tritanium_casing", GTICORE.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> GRAVITON_FIELD_CONSTRAINT_CASING = createCasingBlock(
            "graviton_field_constraint_casing", GTICORE.id("block/casings/graviton_field_constraint_casing"));
    public static final BlockEntry<Block> HYPER_MECHANICAL_CASING = createCasingBlock(
            "hyper_mechanical_casing", GTICORE.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> IRIDIUM_CASING = createCasingBlock(
            "iridium_casing", GTICORE.id("block/casings/iridium_casing"));
    public static final BlockEntry<Block> LAFIUM_MECHANICAL_CASING = createCasingBlock(
            "lafium_mechanical_casing", GTICORE.id("block/casings/lafium_mechanical_casing"));
    public static final BlockEntry<Block> OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING = createCasingBlock(
            "oxidation_resistant_hastelloy_n_mechanical_casing", GTICORE.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing"));
    public static final BlockEntry<Block> PIKYONIUM_MACHINE_CASING = createCasingBlock(
            "pikyonium_machine_casing", GTICORE.id("block/casings/pikyonium_machine_casing"));
    public static final BlockEntry<Block> SPS_CASING = createCasingBlock(
            "sps_casing", GTICORE.id("block/casings/sps_casing"));
    public static final BlockEntry<Block> NAQUADAH_ALLOY_CASING = createCasingBlock(
            "naquadah_alloy_casing", GTICORE.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> PROCESS_MACHINE_CASING = createCasingBlock(
            "process_machine_casing", GTICORE.id("block/casings/process_machine_casing"));
    public static final BlockEntry<Block> FISSION_REACTOR_CASING = createCasingBlock(
            "fission_reactor_casing", GTICORE.id("block/casings/fission_reactor_casing"));
    public static final BlockEntry<Block> DEGENERATE_RHENIUM_CONSTRAINED_CASING = createCasingBlock(
            "degenerate_rhenium_constrained_casing", GTICORE.id("block/casings/degenerate_rhenium_constrained_casing"));

    public static final BlockEntry<Block> INFINITY_GLASS = createGlassCasingBlock(
            "infinity_glass", GTICORE.id("block/casings/infinity_glass"), () -> RenderType::cutoutMipped);
    public static final BlockEntry<Block> RHENIUM_REINFORCED_ENERGY_GLASS = createGlassCasingBlock(
            "rhenium_reinforced_energy_glass", GTICORE.id("block/casings/rhenium_reinforced_energy_glass"), () -> RenderType::cutoutMipped);
    public static final BlockEntry<Block> HSSS_REINFORCED_BOROSILICATE_GLASS = createGlassCasingBlock(
            "hsss_reinforced_borosilicate_glass", GTICORE.id("block/casings/hsss_reinforced_borosilicate_glass"), () -> RenderType::cutoutMipped);

    public static final BlockEntry<ActiveBlock> POWER_CORE = createActiveCasing("power_core",
            "block/variant/hyper_core");
    public static final BlockEntry<ActiveBlock> HYPER_CORE = createActiveCasing("hyper_core",
            "block/variant/hyper_core");
    public static final BlockEntry<ActiveBlock> SUPER_COMPUTATION_COMPONENT = createActiveCasing(
            "super_computation_component", "block/variant/super_computation_component");
    public static final BlockEntry<ActiveBlock> SUPER_COOLER_COMPONENT = createActiveCasing("super_cooler_component",
            "block/variant/super_cooler_component");
    public static final BlockEntry<ActiveBlock> SPACETIMECONTINUUMRIPPER = createActiveCasing(
            "spacetimecontinuumripper", "block/variant/spacetimecontinuumripper");
    public static final BlockEntry<ActiveBlock> SPACETIMEBENDINGCORE = createActiveCasing("spacetimebendingcore",
            "block/variant/spacetimebendingcore");
    public static final BlockEntry<ActiveBlock> QFT_COIL = createActiveCasing("qft_coil", "block/variant/qft_coil");
    public static final BlockEntry<ActiveBlock> FISSION_FUEL_ASSEMBLY = createActiveCasing("fission_fuel_assembly",
            "block/variant/fission_fuel_assembly");
    public static final BlockEntry<ActiveBlock> COOLER = createActiveCasing("cooler", "block/variant/cooler");
    public static final BlockEntry<ActiveBlock> ADVANCED_ASSEMBLY_LINE_UNIT = createActiveCasing(
            "advanced_assembly_line_unit", "block/variant/advanced_assembly_line_unit");
    public static final BlockEntry<ActiveBlock> SPACE_ELEVATOR_SUPPORT = createActiveCasing("space_elevator_support",
            "block/variant/space_elevator_support");

    public static final BlockEntry<Block> STELLAR_CONTAINMENT_CASING = createTierCasings(
            "stellar_containment_casing", GTICORE.id("block/stellar_containment_casing"), BlockMap.scMap, 1);
    public static final BlockEntry<Block> ADVANCED_STELLAR_CONTAINMENT_CASING = createTierCasings(
            "advanced_stellar_containment_casing", GTICORE.id("block/stellar_containment_casing"),
            BlockMap.scMap, 2);
    public static final BlockEntry<Block> ULTIMATE_STELLAR_CONTAINMENT_CASING = createTierCasings(
            "ultimate_stellar_containment_casing", GTICORE.id("block/stellar_containment_casing"),
            BlockMap.scMap, 3);

    public static final BlockEntry<ActiveBlock> POWER_MODULE = GTIBlocks.createActiveTierCasing("power_module",
            "block/variant/power_module", BlockMap.sepmMap, 1);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_2 = GTIBlocks.createActiveTierCasing("power_module_2",
            "block/variant/power_module", BlockMap.sepmMap, 2);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_3 = GTIBlocks.createActiveTierCasing("power_module_3",
            "block/variant/power_module", BlockMap.sepmMap, 3);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_4 = GTIBlocks.createActiveTierCasing("power_module_4",
            "block/variant/power_module", BlockMap.sepmMap, 4);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_5 = GTIBlocks.createActiveTierCasing("power_module_5",
            "block/variant/power_module", BlockMap.sepmMap, 5);


    // Adicione este método auxiliar no final da classe GTIBlocks se não existir,
    // ou use o seu existente adaptando os nomes.
    private static BlockEntry<Block> createCasingBlock(String name, String textureName) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.strength(5.0f, 10.0f).sound(SoundType.METAL).requiresCorrectToolForDrops())
                .blockstate((ctx, prov) -> {
                    prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll(name, GTICORE.id("block/" + textureName)));
                })
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .simpleItem()
                .register();
    }
    private static BlockEntry<Block> createCasingBlock(String name) {
        return createCasingBlock(name, name);
    }
    public static final BlockEntry<Block> ACCELERATED_PIPELINE = createCasingBlock("accelerated_pipeline");
    public static final BlockEntry<Block> AGGREGATION_CORE = createCasingBlock("aggregation_core", "aggregatione_core");
    public static final BlockEntry<Block> ANNIHILATE_CORE = createCasingBlock("annihilate_core");
    public static final BlockEntry<Block> ANTIMATTER_CHARGE = createCasingBlock("antimatter_charge");

    // Madeiras / Plantas (Ajustei propriedades para madeira/planta)
    public static final BlockEntry<Block> BARNARDA_LEAVES = REGISTRATE.block("barnarda_leaves", Block::new)
            .initialProperties(() -> Blocks.OAK_LEAVES).simpleItem().register();
    public static final BlockEntry<Block> BARNARDA_PLANKS = REGISTRATE.block("barnarda_planks", Block::new)
            .initialProperties(() -> Blocks.OAK_PLANKS).simpleItem().register();
    // Nota: Logs geralmente precisam de rotação (createLogBlock), usando casing simples por enquanto:
    public static final BlockEntry<Block> BARNARDA_LOG = createCasingBlock("barnarda_log", "barnarda_log_side");

    public static final BlockEntry<Block> CERES_GRUNT = createCasingBlock("ceres_grunt", "ceresgrunt");
    public static final BlockEntry<Block> CERES_STONE = createCasingBlock("ceres_stone", "ceresstone");
    public static final BlockEntry<Block> CHAIN_COMMAND_BLOCK_BROKEN = createCasingBlock("chain_command_block_broken");
    public static final BlockEntry<Block> COMMAND_BLOCK_BROKEN = createCasingBlock("command_block_broken");
    public static final BlockEntry<Block> CONTAINMENT_FIELD_GENERATOR = createCasingBlock("containment_field_generator");
    public static final BlockEntry<Block> CREATE_AGGREGATION_CORE = createCasingBlock("create_aggregation_core", "create_aggregatione_core");
    public static final BlockEntry<Block> CREATE_HPCA_COMPONENT = createCasingBlock("create_hpca_component");
    public static final BlockEntry<Block> DIMENSION_CREATION_CASING = createCasingBlock("dimension_creation_casing");
    public static final BlockEntry<Block> DIMENSIONAL_BRIDGE_CASING = createCasingBlock("dimensional_bridge_casing");
    public static final BlockEntry<Block> DIMENSIONAL_STABILITY_CASING = createCasingBlock("dimensional_stability_casing");
    public static final BlockEntry<Block> DRACONIUM_BLOCK_CHARGED = createCasingBlock("draconium_block_charged");
    public static final BlockEntry<Block> DYSON_CONTROL_TOROID = createCasingBlock("dyson_control_toroid");
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CASING = createCasingBlock("dyson_deployment_casing");
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CORE = createCasingBlock("dyson_deployment_core");
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_MAGNET = createCasingBlock("dyson_deployment_magnet"); // Assumindo textura padrão
    public static final BlockEntry<Block> DYSON_RECEIVER_CASING = createCasingBlock("dyson_receiver_casing");
    public static final BlockEntry<Block> ENCELADUS_STONE = createCasingBlock("enceladus_stone", "enceladusstone");
    public static final BlockEntry<Block> ENDER_OBSIDIAN = createCasingBlock("ender_obsidian");
    public static final BlockEntry<Block> ESSENCE_BLOCK = createCasingBlock("essence_block");
    public static final BlockEntry<Block> FLOTATION_CELL = createCasingBlock("flotation_cell");

    // Vidros geralmente usam render layer 'translucent' ou 'cutout'
    public static final BlockEntry<Block> FORCE_FIELD_GLASS = REGISTRATE.block("force_field_glass", Block::new)
            .initialProperties(() -> Blocks.GLASS).addLayer(() -> RenderType::cutout)
            .simpleItem().register();

    public static final BlockEntry<Block> GANYMEDE_GRUNT = createCasingBlock("ganymede_grunt", "ganymedegrunt");
    public static final BlockEntry<Block> GANYMEDE_STONE = createCasingBlock("ganymede_stone", "ganymedestone");
    public static final BlockEntry<Block> HASTELLOY_N_75_CASING = createCasingBlock("hastelloy_n_75_casing");
    public static final BlockEntry<Block> HASTELLOY_N_75_GEARBOX = createCasingBlock("hastelloy_n_75_gearbox");
    public static final BlockEntry<Block> HASTELLOY_N_75_PIPE = createCasingBlock("hastelloy_n_75_pipe");
    public static final BlockEntry<Block> HIGH_STRENGTH_CONCRETE = createCasingBlock("high_strength_concrete");
    public static final BlockEntry<Block> HOLLOW_CASING = createCasingBlock("hollow_casing");
    public static final BlockEntry<Block> INCONEL_625_CASING = createCasingBlock("inconel_625_casing");
    public static final BlockEntry<Block> INCONEL_625_GEARBOX = createCasingBlock("inconel_625_gearbox");
    public static final BlockEntry<Block> INCONEL_625_PIPE = createCasingBlock("inconel_625_pipe");
    public static final BlockEntry<Block> INFUSED_OBSIDIAN = createCasingBlock("infused_obsidian");
    public static final BlockEntry<Block> INTERNAL_SUPPORT = createCasingBlock("internal_support");
    public static final BlockEntry<Block> IO_ASH = createCasingBlock("io_ash", "ioash");
    public static final BlockEntry<Block> IO_STONE = createCasingBlock("io_stone", "iostone");
    public static final BlockEntry<Block> LASER_COOLING_CASING = createCasingBlock("laser_cooling_casing");
    public static final BlockEntry<Block> LEPTONIC_CHARGE = createCasingBlock("leptonic_charge");
    public static final BlockEntry<Block> MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE = createCasingBlock("machine_casing_circuit_assembly_line");
    public static final BlockEntry<Block> MACHINE_CASING_GRINDING_HEAD = createCasingBlock("machine_casing_grinding_head");
    public static final BlockEntry<Block> MODULE_CONNECTOR = createCasingBlock("module_connector");
    public static final BlockEntry<Block> MOLECULAR_COIL = createCasingBlock("molecular_coil"); // Mantive pois parece máquina, não aquecedor
    public static final BlockEntry<Block> MOTOR_GLOW = createCasingBlock("motor_glow");
    public static final BlockEntry<Block> NAQUADRIA_CHARGE = createCasingBlock("naquadria_charge");
    public static final BlockEntry<Block> NEUTRONIUM_GEARBOX = createCasingBlock("neutronium_gearbox");
    public static final BlockEntry<Block> NEUTRONIUM_PIPE_CASING = createCasingBlock("neutronium_pipe_casing");
    public static final BlockEntry<Block> PLUTO_GRUNT = createCasingBlock("pluto_grunt", "plutogrunt");
    public static final BlockEntry<Block> PLUTO_STONE = createCasingBlock("pluto_stone", "plutostone");
    public static final BlockEntry<Block> QUANTUM_CHROMODYNAMIC_CHARGE = createCasingBlock("quantum_chromodynamic_charge");
    public static final BlockEntry<Block> REACTOR_CORE = createCasingBlock("reactor_core");
    public static final BlockEntry<Block> RED_STEEL_CASING = createCasingBlock("red_steel_casing", "red_steel_casing_side");
    public static final BlockEntry<Block> RESTRAINT_DEVICE = createCasingBlock("restraint_device");
    public static final BlockEntry<Block> SHINING_OBSIDIAN = createCasingBlock("shining_obsidian");
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_CASING = createCasingBlock("spacetime_assembly_line_casing");
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_UNIT = createCasingBlock("spacetime_assembly_line_unit");
    public static final BlockEntry<Block> SPACETIME_COMPRESSION_FIELD_GENERATOR = createCasingBlock("spacetime_compression_field_generator");
    public static final BlockEntry<Block> SPACETIME_BENDING_CORE = createCasingBlock("spacetime_bending_core", "spacetimebendingcore");
    public static final BlockEntry<Block> SPACETIME_CONTINUUM_RIPPER = createCasingBlock("spacetime_continuum_ripper", "spacetimecontinuumripper");
    public static final BlockEntry<Block> SPEEDING_PIPE = createCasingBlock("speeding_pipe", "speeding_pipe_side");
    public static final BlockEntry<Block> STEAM_ASSEMBLY_BLOCK = createCasingBlock("steam_assembly_block");
    public static final BlockEntry<Block> TITAN_GRUNT = createCasingBlock("titan_grunt", "titangrunt");
    public static final BlockEntry<Block> TITAN_STONE = createCasingBlock("titan_stone", "titanstone");
    public static final BlockEntry<Block> VARIATION_WOOD = createCasingBlock("variation_wood");
}
