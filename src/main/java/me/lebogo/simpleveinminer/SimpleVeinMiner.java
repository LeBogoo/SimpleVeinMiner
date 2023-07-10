package me.lebogo.simpleveinminer;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.bukkit.Material.*;

public final class SimpleVeinMiner extends JavaPlugin {
    public static Logger LOGGER = Logger.getLogger("SimpleVeinMiner");

    public static final String PLUGIN_DIRECTORY = "plugins/SimpleVeinMiner/";

    public static final ItemStack DISABLED_ITEM_STACK = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    public static final ItemStack ENABLED_ITEM_STACK = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

    public static final List<VeinMinerCategory> VEIN_MINER_CATEGORIES = new ArrayList<>();

    static {
        ItemMeta redGlassPaneMeta = DISABLED_ITEM_STACK.getItemMeta();
        redGlassPaneMeta.displayName(Component.text("Disabled"));
        DISABLED_ITEM_STACK.setItemMeta(redGlassPaneMeta);

        ItemMeta greenGlassPaneMeta = ENABLED_ITEM_STACK.getItemMeta();
        greenGlassPaneMeta.displayName(Component.text("Enabled"));
        ENABLED_ITEM_STACK.setItemMeta(greenGlassPaneMeta);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("simpleveinminer").setExecutor(new SimpleVeinMinerCommand());
        getCommand("simpleveinminer").setTabCompleter(new SimpleVeinMinerTabCompleter());
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        VeinMinerCategory logCategory = new VeinMinerCategory("Logs", OAK_LOG);
        VEIN_MINER_CATEGORIES.add(logCategory);

        logCategory.addMaterial(OAK_LOG);
        logCategory.addMaterial(SPRUCE_LOG);
        logCategory.addMaterial(BIRCH_LOG);
        logCategory.addMaterial(JUNGLE_LOG);
        logCategory.addMaterial(ACACIA_LOG);
        logCategory.addMaterial(DARK_OAK_LOG);
        logCategory.addMaterial(MANGROVE_LOG);
        logCategory.addMaterial(CHERRY_LOG);

        VeinMinerCategory oreCategory = new VeinMinerCategory("Ores", IRON_ORE);
        VEIN_MINER_CATEGORIES.add(oreCategory);

        oreCategory.addMaterial(COAL_ORE);
        oreCategory.addMaterial(IRON_ORE);
        oreCategory.addMaterial(COPPER_ORE);
        oreCategory.addMaterial(GOLD_ORE);
        oreCategory.addMaterial(REDSTONE_ORE);
        oreCategory.addMaterial(EMERALD_ORE);
        oreCategory.addMaterial(LAPIS_ORE);
        oreCategory.addMaterial(DIAMOND_ORE);

        VeinMinerCategory deepslateOreCategory = new VeinMinerCategory("Deepslate Ores", DEEPSLATE_DIAMOND_ORE);
        VEIN_MINER_CATEGORIES.add(deepslateOreCategory);

        deepslateOreCategory.addMaterial(DEEPSLATE_COAL_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_IRON_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_COPPER_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_GOLD_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_REDSTONE_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_EMERALD_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_LAPIS_ORE);
        deepslateOreCategory.addMaterial(DEEPSLATE_DIAMOND_ORE);

        VeinMinerCategory netherCategory = new VeinMinerCategory("Nether Blocks", NETHERRACK);
        VEIN_MINER_CATEGORIES.add(netherCategory);

        netherCategory.addMaterial(NETHER_QUARTZ_ORE);
        netherCategory.addMaterial(NETHER_GOLD_ORE);
        netherCategory.addMaterial(ANCIENT_DEBRIS);
        netherCategory.addMaterial(NETHER_WART_BLOCK);
        netherCategory.addMaterial(GLOWSTONE);
        netherCategory.addMaterial(CRIMSON_STEM);
        netherCategory.addMaterial(WARPED_STEM);

        VeinMinerCategory stoneCategory = new VeinMinerCategory("Stones", STONE);
        VEIN_MINER_CATEGORIES.add(stoneCategory);

        stoneCategory.addMaterial(GRANITE);
        stoneCategory.addMaterial(DIORITE);
        stoneCategory.addMaterial(ANDESITE);
        stoneCategory.addMaterial(TUFF);
        stoneCategory.addMaterial(CALCITE);

        VeinMinerCategory leafCategory = new VeinMinerCategory("Leaves", OAK_LEAVES);
        VEIN_MINER_CATEGORIES.add(leafCategory);

        leafCategory.addMaterial(OAK_LEAVES);
        leafCategory.addMaterial(SPRUCE_LEAVES);
        leafCategory.addMaterial(BIRCH_LEAVES);
        leafCategory.addMaterial(JUNGLE_LEAVES);
        leafCategory.addMaterial(ACACIA_LEAVES);
        leafCategory.addMaterial(DARK_OAK_LEAVES);
        leafCategory.addMaterial(MANGROVE_LEAVES);
        VeinMinerCategory etcCategory = new VeinMinerCategory("Etc", DIRT);
        VEIN_MINER_CATEGORIES.add(etcCategory);

        etcCategory.addMaterial(ICE);
        etcCategory.addMaterial(PACKED_ICE);
        etcCategory.addMaterial(BLUE_ICE);
        etcCategory.addMaterial(SNOW_BLOCK);
        etcCategory.addMaterial(SNOW);
        etcCategory.addMaterial(CLAY);

        VeinMinerCategory unstableCategory = new VeinMinerCategory("Unstable", BARRIER);
        VEIN_MINER_CATEGORIES.add(unstableCategory);

        unstableCategory.addMaterial(SAND);
        unstableCategory.addMaterial(GRAVEL);
        unstableCategory.addMaterial(DEEPSLATE);
        unstableCategory.addMaterial(STONE);
        unstableCategory.addMaterial(DIRT);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
