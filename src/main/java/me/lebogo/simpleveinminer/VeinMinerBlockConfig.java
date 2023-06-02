package me.lebogo.simpleveinminer;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VeinMinerBlockConfig {
    private YamlConfiguration config;
    private File configFile;
    private List<Material> enabledBlocks;

    public VeinMinerBlockConfig(UUID uuid) {
        enabledBlocks = new ArrayList<>();
        config = new YamlConfiguration();

        File pluginDirectoryFile = new File(SimpleVeinMiner.PLUGIN_DIRECTORY);
        if (!pluginDirectoryFile.exists()) {
            pluginDirectoryFile.mkdirs();
        }

        configFile = new File(SimpleVeinMiner.PLUGIN_DIRECTORY + uuid.toString() + ".yml");
        if (!configFile.exists()) {
            setEnabledBlocks(enabledBlocks);
            save();
            return;
        }

        try {
            config.load(configFile);

            for (String enabledBlock : (List<String>) config.getList("enabledBlocks")) {
                enabledBlocks.add(Material.valueOf(enabledBlock));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Material> getEnabledBlocks() {
        return enabledBlocks;
    }

    public void setEnabledBlocks(List<Material> enabledBlocks) {
        this.enabledBlocks = enabledBlocks;

        List<String> enabledBlockStrings = new ArrayList<>();
        for (Material enabledBlock : enabledBlocks) {
            enabledBlockStrings.add(enabledBlock.toString());
        }

        config.set("enabledBlocks", enabledBlockStrings);
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
