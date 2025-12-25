package com.raishxn.gticore.config;

import com.raishxn.gticore.GTICORE;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTICORE.MOD_ID)
public class ConfigHolder {

    public static ConfigHolder INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
    }

    @Configurable
    public boolean disableDrift = true;
    @Configurable
    public boolean enableSkyBlokeMode = false;
    @Configurable
    @Configurable.Range(min = 1)
    public int oreMultiplier = 4;
    @Configurable
    @Configurable.Range(min = 1)
    public int cellType = 4;
    @Configurable
    @Configurable.Range(min = 1)
    public int spacetimePip = Integer.MAX_VALUE;
    @Configurable
    @Configurable.Range(min = 0)
    public double durationMultiplier = 1;
    @Configurable
    @Configurable.Range(min = 1)
    public int travelStaffCD = 2;
    @Configurable
    @Configurable.Comment({ "Larger values may cause display issues; it is recommended to manage them in the template management terminal." })
    @Configurable.Range(min = 36, max = 360)
    public int exPatternProvider = 36;
    @Configurable
    public boolean enablePrimitiveVoidOre = false;
    @Configurable
    @Configurable.Comment("Chain blacklist, supports wildcard *")
    @Configurable.Synchronized
    public String[] blackBlockList = { "ae2:cable_bus", "minecraft:grass_block" };
    @Configurable
    @Configurable.Comment("ME template assembly output minimum interval")
    @Configurable.Range(min = 1, max = 100)
    public int MEPatternOutputMin = 5;
    @Configurable
    @Configurable.Comment("ME prototype assembly output maximum interval")
    @Configurable.Range(min = 1, max = 200)
    public int MEPatternOutputMax = 80;

    @Configurable
    @Configurable.Comment("Should we enable the ME inventory limit pull mode (this ensures the machine won't stop, but will significantly reduce TPS!)")
    public boolean enableUltimateMEStocking = false;

    @Configurable
    @Configurable.Comment("AE2 composition update interval (tick). A larger value results in better performance but slower response. It must be a power of 2 (1, 2, 4, 8, 16).")
    @Configurable.Range(min = 1, max = 16)
    public int ae2CraftingServiceUpdateInterval = 4;

    @Configurable
    @Configurable.Comment("AE2 inventory update interval (tick): A larger value results in better performance but slower response. It must be a power of 2 (1, 2, 4, 8, 16).")
    @Configurable.Range(min = 1, max = 16)
    public int ae2StorageServiceUpdateInterval = 8;

    @Configurable
    @Configurable.Comment("AE2 compositing calculation modes: LEGACY (original), FAST, ULTRA_FAST (fastest).")
    public AE2CalculationMode ae2CalculationMode = AE2CalculationMode.ULTRA_FAST;

    @Configurable
    public String[] mobList1 = new String[] { "chicken", "rabbit", "sheep", "cow", "horse", "pig", "donkey", "skeleton_horse", "iron_golem", "wolf", "goat", "parrot", "camel", "cat", "fox", "llama", "panda", "polar_bear" };
    @Configurable
    public String[] mobList2 = new String[] { "ghast", "zombie", "pillager", "zombie_villager", "skeleton", "drowned", "witch", "spider", "creeper", "husk", "wither_skeleton", "blaze", "zombified_piglin", "slime", "vindicator", "enderman" };
}
