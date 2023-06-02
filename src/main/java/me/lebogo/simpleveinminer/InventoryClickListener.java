package me.lebogo.simpleveinminer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

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
        } else if (title.content().equals("SVM - Block Selection")) {
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

        List<Material> palette = category.getMaterials();
        openBlockMenu((Player) event.getWhoClicked(), palette);

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

        if (enabledBlocks.contains(material)) {
            enabledBlocks.remove(material);
            inventory.setItem(column, SimpleVeinMiner.DISABLED_ITEM_STACK);
        } else {
            enabledBlocks.add(material);
            inventory.setItem(column, SimpleVeinMiner.ENABLED_ITEM_STACK);
        }

        config.setEnabledBlocks(enabledBlocks);
        config.save();
    }

    private void openBlockMenu(Player player, List<Material> palette) {
        Inventory menuInventory = player.getServer().createInventory(null,
                9 * 3, Component.text("SVM - Block Selection"));

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
