package me.lebogo.simpleveinminer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SimpleVeinMinerCommand implements CommandExecutor {

    public SimpleVeinMinerCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String raw,
            @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    openGui(player);
                    break;

                case 2:
                    if (args[0].equalsIgnoreCase("disable")) {
                        disableCategory(player, args[1]);
                    }

                    if (args[0].equalsIgnoreCase("toggle"))
                        toggleBlock(player, args[1]);
                    break;
            }

            return true;
        }

        return false;
    }

    private void toggleBlock(Player player, String blockName) {
        VeinMinerBlockConfig config = new VeinMinerBlockConfig(player.getUniqueId());
        List<Material> enabledBlocks = config.getEnabledBlocks();
        Material material = Material.getMaterial(blockName.toUpperCase());
        Component message;
        if (enabledBlocks.contains(material)) {
            enabledBlocks.remove(material);
            message = Component.text("Disabled " + material.name());
            message = message.style(Style.style(TextColor.color(0xFB5454)));
        } else {
            enabledBlocks.add(material);
            message = Component.text("Enabled " + material.name());
            message = message.style(Style.style(TextColor.color(0x54FB54)));
        }

        player.sendMessage(message);
        config.setEnabledBlocks(enabledBlocks);
        config.save();
        return;
    }

    private void disableCategory(Player player, String categoryName) {
        VeinMinerBlockConfig config = new VeinMinerBlockConfig(player.getUniqueId());
        List<Material> enabledBlocks = config.getEnabledBlocks();

        Component message = Component.text("Disabled " + categoryName);
        message = message.style(Style.style(TextColor.color(0xFB5454)));

        if (categoryName.equalsIgnoreCase("all")) {
            enabledBlocks.clear();
            config.setEnabledBlocks(enabledBlocks);
            config.save();
            player.sendMessage(message);
            return;
        }

        for (VeinMinerCategory veinMinerCategory : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
            if (veinMinerCategory.getName().equalsIgnoreCase(categoryName)) {
                for (Material material : veinMinerCategory.getMaterials()) {
                    enabledBlocks.remove(material);
                }
                player.sendMessage(message);

                config.setEnabledBlocks(enabledBlocks);
                config.save();

                return;
            }
        }

        message = Component.text("Category " + categoryName + " not found");
        message = message.style(Style.style(TextColor.color(0xFB5454)));

        player.sendMessage(message);
    }

    private void openGui(Player player) {
        Inventory inventory = player.getServer().createInventory(player, InventoryType.HOPPER,
                Component.text("SVM - Category Selection"));

        if (SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() > 5 && SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() <= 9) {
            inventory = player.getServer().createInventory(player, InventoryType.DROPPER,
                    Component.text("SVM - Category Selection"));
        } else if (SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() > 9) {
            inventory = player.getServer().createInventory(player,
                    SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() / 9 * 9 + 9,
                    Component.text("SVM - Category Selection"));
        }

        for (VeinMinerCategory veinMinerCategory : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
            ItemStack itemStack = new ItemStack(veinMinerCategory.getIcon());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(veinMinerCategory.getName()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        player.openInventory(inventory);
    }

}
