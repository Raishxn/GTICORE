package com.raishxn.gticore;

import com.raishxn.gticore.api.data.tag.GTITagPrefix;
import com.raishxn.gticore.api.registry.GTIRegistry;
import com.raishxn.gticore.common.data.*;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.lowdragmc.lowdraglib.Platform;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;

@GTAddon
public class GTICOREGTADDON implements IGTAddon {

    @Override
    public String addonModId() {
        return GTICORE.MOD_ID;
    }

    @Override
    public GTRegistrate getRegistrate() {
        return GTIRegistry.REGISTRATE;
    }

    @Override
    public boolean requiresHighTier() {
        return true;
    }

    @Override
    public void initializeAddon() {
        GTIItems.init();
        GTIBlocks.init();
    }

    @Override
    public void registerSounds() {
        GTISoundEntries.init();
    }

    @Override
    public void registerCovers() {
        GTICovers.init();
    }

    @Override
    public void registerElements() {
        GTIElements.init();
    }

    @Override
    public void registerTagPrefixes() {
        GTITagPrefix.init();
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        /*GCyMRecipes.init(provider);
        FuelRecipes.init(provider);
        MachineRecipe.init(provider);
        Misc.init(provider);
        ElementCopying.init(provider);
        StoneDustProcess.init(provider);
        Lanthanidetreatment.init(provider);
        CircuitRecipes.init(provider);
        MixerRecipes.init(provider);
        SkyTearsAndGregHeart.init(provider);
        DimensionallyMixerRecipes.init(provider);*/
    }

    @Override
    public void removeRecipes(Consumer<ResourceLocation> consumer) {
       // RemoveRecipe.init(consumer);
    }

    @Override
    public void registerFluidVeins() {
        if (!Platform.isDevEnv()) {
            GTIBedrockFluids.init();
        }
    }
}
