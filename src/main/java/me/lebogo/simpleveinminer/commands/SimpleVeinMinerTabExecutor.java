package me.lebogo.simpleveinminer.commands;

import me.lebogo.simpleveinminer.SimpleVeinMiner;
import me.lebogo.simpleveinminer.veinminer.VeinMinerBlockConfig;
import me.lebogo.simpleveinminer.veinminer.VeinMinerCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleVeinMinerTabExecutor implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String raw,
                             @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    openGui(player);
                    break;

                case 1:
                    if (args[0].equalsIgnoreCase("reload")) {
                        SimpleVeinMiner.getPlugin(SimpleVeinMiner.class).reloadCategories();
                        Component message = Component.text("Reloaded VeinMiner Categories");
                        message = message.style(Style.style(TextColor.color(0x54FB54)));
                        player.sendMessage(message);
                    }


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
        Component title = Component.text("SVM - Category Selection");
        Inventory inventory = player.getServer().createInventory(player, InventoryType.HOPPER,
                title);

        if (SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() > 5 && SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() <= 9) {
            inventory = player.getServer().createInventory(player, InventoryType.DROPPER,
                    title);
        } else if (SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() > 9) {
            inventory = player.getServer().createInventory(player,
                    SimpleVeinMiner.VEIN_MINER_CATEGORIES.size() / 9 * 9 + 9,
                    title);
        }

        for (VeinMinerCategory veinMinerCategory : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
            ItemStack itemStack = new ItemStack(veinMinerCategory.getIcon());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(veinMinerCategory.getName()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        ItemStack itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(" "));
        itemStack.setItemMeta(itemMeta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        }

        player.openInventory(inventory);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String raw, @NotNull String[] args) {

        if (args.length == 1) {
            return List.of("toggle", "disable");
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "toggle": {
                    List<String> validMaterialNames = new ArrayList<>();

                    for (VeinMinerCategory category : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
                        for (Material material : category.getMaterials()) {
                            String materialName = material.name().toLowerCase();
                            if (materialName.contains(args[1].toLowerCase())) {
                                validMaterialNames.add(materialName);
                            }
                        }
                    }

                    return validMaterialNames;
                }

                case "disable": {
                    List<String> validCategoryNames = new ArrayList<>();
                    validCategoryNames.add("all");

                    for (VeinMinerCategory category : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
                        String categoryName = category.getName().toLowerCase();
                        if (categoryName.contains(args[1].toLowerCase())) {
                            validCategoryNames.add(categoryName);
                        }
                    }

                    return validCategoryNames;
                }

                default:
                    break;
            }

        }

        return null;
    }
}
