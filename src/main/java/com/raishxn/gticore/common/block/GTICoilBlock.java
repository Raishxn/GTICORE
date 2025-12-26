package com.raishxn.gticore.common.block;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.raishxn.gticore.GTICORE;
import com.raishxn.gticore.common.blockentity.GTICoilBlockEntity;
import com.raishxn.gticore.common.data.GTIBlocks; // Importante: Importar sua classe de registro
import com.raishxn.gticore.common.data.GTIMaterials;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class GTICoilBlock extends CoilBlock implements EntityBlock {

    public GTICoilBlock(Properties properties, ICoilType coilType) {
        super(properties, coilType);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        // CORREÇÃO: Passamos o tipo de entidade (GTI_COIL_BLOCK_ENTITY.get()) como primeiro argumento
        return new GTICoilBlockEntity(GTIBlocks.GTI_COIL_BLOCK_ENTITY.get(), pos, state);
    }

    @Getter
    public enum CoilType implements StringRepresentable, ICoilType {
        ABYSSALALLOY("abyssalalloy", 12600, 9, 16, 1, () -> GTIMaterials.AbyssalAlloy),
        TITANSTEEL("titansteel", 14400, 9, 16, 1, () -> GTIMaterials.TitanSteel),
        STARMETAL("starmetal", 21600, 10, 16, 1, () -> GTIMaterials.Starmetal),
        NAQUADRIATICTARANIUM("naquadriatictaranium", 18900, 10, 16, 1, () -> GTIMaterials.NaquadriaticTaranium),
        HYPOGEN("hypogen", 62000, 11, 16, 1, () -> GTIMaterials.Hypogen),
        ETERNITY("eternity", 96000, 12, 16, 1, () -> GTIMaterials.Eternity),
        INFINITY("infinity", 36000, 13, 16, 1, () -> GTIMaterials.Infinity),
        URUIUM("uruium", 276000, 14, 1000, 1, () -> GTIMaterials.Uruium),
        ADAMANTINE("adamantine", 16200, 15, 16, 1, () -> GTIMaterials.Adamantine);

        private final String name;
        private final int coilTemperature;
        private final int tier;
        private final int level;
        private final int energyDiscount;
        @NotNull
        private final Supplier<Material> material;
        @NotNull
        private final ResourceLocation texture;

        CoilType(String name, int coilTemperature, int tier, int level, int energyDiscount, Supplier<Material> material) {
            this.name = name;
            this.coilTemperature = coilTemperature;
            this.tier = tier;
            this.level = level;
            this.energyDiscount = energyDiscount;
            this.material = material;
            this.texture = GTICORE.id("block/" + name + "_coil_block");
        }

        public Material getMaterial() {
            return material.get();
        }

        @NotNull
        @Override
        public String toString() {
            return getSerializedName();
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return name;
        }
    }
}