package com.raishxn.gticore;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.raishxn.gticore.client.ClientProxy; // <--- ADICIONE ESTA LINHA
import com.raishxn.gticore.common.CommonProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GTICORE.MOD_ID)
@SuppressWarnings("removal")
public class GTICORE {

    public static final String MOD_ID = "gticore";
    public static final Logger LOGGER = LogManager.getLogger();
    public static GTRegistrate EXAMPLE_REGISTRATE = GTRegistrate.create(GTICORE.MOD_ID);

    public GTICORE() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
