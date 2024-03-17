package me.lebogo.simpleveinminer;

import me.lebogo.simpleveinminer.commands.SimpleVeinMinerTabExecutor;
import me.lebogo.simpleveinminer.listeners.BlockBreakListener;
import me.lebogo.simpleveinminer.listeners.InventoryClickListener;
import me.lebogo.simpleveinminer.veinminer.VeinMinerCategory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

        // REGISTER COMMANDS
        SimpleVeinMinerTabExecutor simpleVeinMinerTabExecutor = new SimpleVeinMinerTabExecutor();
        getCommand("simpleveinminer").setExecutor(simpleVeinMinerTabExecutor);
        getCommand("simpleveinminer").setTabCompleter(simpleVeinMinerTabExecutor);

        // REGISTER LISTENERS
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        // SAVE CONFIG / LOAD CATEGORIES
        saveDefaultConfig();
        reloadCategories();
    }

    public void reloadCategories() {
        reloadConfig();
        VEIN_MINER_CATEGORIES.clear();
        VEIN_MINER_CATEGORIES.addAll((List<VeinMinerCategory>) getConfig().getList("vein-miner-categories"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
