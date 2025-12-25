package com.raishxn.gticore.api.registry;

import com.raishxn.gticore.GTICORE;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class GTIRegistry {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(GTICORE.MOD_ID);

    static {
        GTIRegistry.REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private GTIRegistry() {/**/}
}
