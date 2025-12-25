package com.raishxn.gticore.common.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.raishxn.gticore.common.data.GTIMaterials;
import com.raishxn.gticore.common.data.GTIRecipeTypes;
import com.raishxn.gticore.data.recipe.GenerateDisassembly;

import java.text.NumberFormat;

public class RecipeModify {

    public static void init() {
        GTRecipeTypes.SIFTER_RECIPES.setMaxIOSize(1, 6, 1, 1);
        GTRecipeTypes.ASSEMBLER_RECIPES.onRecipeBuild(GenerateDisassembly::generateDisassembly);
        GTRecipeTypes.ASSEMBLER_RECIPES.onRecipeBuild(GenerateDisassembly::generateDisassembly);
        GTRecipeTypes.PLASMA_GENERATOR_FUELS.onRecipeBuild((recipeBuilder, provider) -> {
            long eu = recipeBuilder.duration * GTValues.V[GTValues.EV];
            GTIRecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(recipeBuilder.id)
                    .inputFluids(FluidRecipeCapability.CAP.of(recipeBuilder.input
                            .get(FluidRecipeCapability.CAP).get(0).getContent()))
                    .inputFluids(GTMaterials.DistilledWater.getFluid((int) (eu / 160)))
                    .outputFluids(FluidRecipeCapability.CAP.of(recipeBuilder.output
                            .get(FluidRecipeCapability.CAP).get(0).getContent()))
                    .outputFluids(GTIMaterials.SupercriticalSteam.getFluid((int) eu))
                    .addData("eu", eu)
                    .duration(200)
                    .save(provider);
        });

        GTRecipeTypes.LASER_ENGRAVER_RECIPES.setMaxIOSize(2, 1, 2, 1).onRecipeBuild((recipeBuilder, provider) -> {
            if (recipeBuilder.data.contains("special")) return;
            if (recipeBuilder.output.containsKey(FluidRecipeCapability.CAP)) return;
            GTRecipeBuilder recipe = GTIRecipeTypes.DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.copyFrom(recipeBuilder)
                    .duration((int) (recipeBuilder.duration * 0.2))
                    .EUt(recipeBuilder.EUt().voltage() << 2);
            double value = Math.log10(recipeBuilder.EUt().voltage()) / Math.log10(4);
            if (value > 10) {
                recipe.inputFluids(GTIMaterials.EuvPhotoresist.getFluid((int) (value / 2)));
            } else {
                recipe.inputFluids(GTIMaterials.Photoresist.getFluid((int) value));
            }
            recipe.save(provider);
        });

        GTRecipeTypes.FUSION_RECIPES.getDataInfos().clear();
        GTRecipeTypes.FUSION_RECIPES.addDataInfo((data) -> LocalizationUtils.format("gtceu.recipe.eu_to_starts",
                NumberFormat.getCompactNumberInstance().format(data.getLong("eu_to_start") / 1000000),
                getFusionTier(data.getLong("eu_to_start") / 1000000)));
    }

    private static String getFusionTier(long startEu) {
        String tier = "I";
        if (startEu > 1280L) tier = "V";
        else if (startEu > 640L) tier = "IV";
        else if (startEu > 320L) tier = "III";
        else if (startEu > 160L) tier = "II";
        return tier;
    }
}
