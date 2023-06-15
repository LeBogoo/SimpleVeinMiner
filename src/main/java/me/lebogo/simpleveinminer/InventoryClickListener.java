package me.lebogo.simpleveinminer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // check if title is type of TextComponent
        if (!(event.getView().title() instanceof TextComponent)) {
            return;
        }

        TextComponent title = (TextComponent) event.getView().title();
        boolean isVeinMinerMenu = title.content().startsWith("SVM -");

        if (!isVeinMinerMenu) {
            return;
        }

        event.setCancelled(true);

        Inventory inventory = event.getClickedInventory();

        if (inventory == null) {
            return;
        }

        if (title.content().equals("SVM - Category Selection")) {
            handleCategorySelectionMenu(event);
        } else {
            handleBlockToggleMenu(event, inventory);
        }

    }

    private void handleCategorySelectionMenu(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        String itemName = ((TextComponent) clickedItem.getItemMeta().displayName()).content();

        VeinMinerCategory category = null;
        for (VeinMinerCategory veinMinerCategory : SimpleVeinMiner.VEIN_MINER_CATEGORIES) {
            if (veinMinerCategory.getName().equals(itemName)) {
                category = veinMinerCategory;
                break;
            }
        }

        if (category == null) {
            return;
        }

        openBlockMenu((Player) event.getWhoClicked(), category);

    }

    private void handleBlockToggleMenu(InventoryClickEvent event, Inventory inventory) {
        int column = event.getRawSlot() % 9;
        ItemStack item = event.getView().getItem(column);
        if (item == null) {
            return;
        }

        Material material = item.getType();
        VeinMinerBlockConfig config = new VeinMinerBlockConfig(event.getWhoClicked().getUniqueId());
        List<Material> enabledBlocks = config.getEnabledBlocks();
        column += 9;

        TextComponent message;

        if (enabledBlocks.contains(material)) {
            enabledBlocks.remove(material);
            inventory.setItem(column, SimpleVeinMiner.DISABLED_ITEM_STACK);
            message = Component.text("Disabled " + material.name());
            message = message.style(Style.style(TextColor.color(0xFB5454)));
        } else {
            enabledBlocks.add(material);
            inventory.setItem(column, SimpleVeinMiner.ENABLED_ITEM_STACK);
            message = Component.text("Enabled " + material.name());
            message = message.style(Style.style(TextColor.color(0x54FB54)));
        }

        Player player = (Player) event.getWhoClicked();
        player.sendMessage(message);

        config.setEnabledBlocks(enabledBlocks);
        config.save();
    }

    private void openBlockMenu(Player player, VeinMinerCategory category) {
        List<Material> palette = category.getMaterials();
        Inventory menuInventory = player.getServer().createInventory(null,
                9 * 3, Component.text("SVM - " + category.getName()));

        for (Material veinBlock : palette) {
            ItemStack itemStack = new ItemStack(veinBlock);
            menuInventory.addItem(itemStack);
        }

        VeinMinerBlockConfig veinMinerBlockConfig = new VeinMinerBlockConfig(player.getUniqueId());

        for (int i = 0; i < palette.size(); i++) {
            Material blockMaterial = palette.get(i);
            List<Material> enabledBlocks = veinMinerBlockConfig.getEnabledBlocks();

            if (enabledBlocks.contains(blockMaterial)) {
                menuInventory.setItem(i + 9, SimpleVeinMiner.ENABLED_ITEM_STACK);
            } else {
                menuInventory.setItem(i + 9, SimpleVeinMiner.DISABLED_ITEM_STACK);
            }
        }

        player.openInventory(menuInventory);
    }

}
