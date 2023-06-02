package me.lebogo.simpleveinminer;

import net.kyori.adventure.text.Component;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s,
            @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

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

            return true;
        }

        return false;
    }
}
