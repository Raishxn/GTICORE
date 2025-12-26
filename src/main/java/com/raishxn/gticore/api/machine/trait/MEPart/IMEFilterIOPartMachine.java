package com.raishxn.gticore.api.machine.trait.MEPart;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.raishxn.gticore.common.machine.multiblock.part.ae.MEPatternBufferRecipeHandlerTrait;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import com.raishxn.gticore.api.machine.trait.IMERecipeHandlerTrait;
import org.jetbrains.annotations.NotNull;

public interface IMEFilterIOPartMachine extends IMEIOPartMachine {

    // Altere para usar a interface genérica na parte direita do Pair também:
    Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> getMERecipeHandlerTraits();
    @NotNull
    IMEFilterIOTrait getMETrait();
}