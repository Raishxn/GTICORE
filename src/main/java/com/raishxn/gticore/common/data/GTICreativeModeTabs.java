package com.raishxn.gticore.common.data;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;
import com.raishxn.gticore.GTICORE;

import static com.raishxn.gticore.api.registry.GTIRegistry.REGISTRATE;

public class GTICreativeModeTabs {

    public static RegistryEntry<CreativeModeTab> GTI_CORE = REGISTRATE.defaultCreativeTab(GTICORE.MOD_ID,
            builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(GTICORE.MOD_ID, REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTICORE.id("creative_tab"), "GTI Core"))
                    .icon(GTItems.BATTERY_ZPM_NAQUADRIA::asStack)
                    .build())
            .register();

    public static void init() {}
}
