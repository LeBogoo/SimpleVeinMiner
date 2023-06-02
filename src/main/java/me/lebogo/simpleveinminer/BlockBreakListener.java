package me.lebogo.simpleveinminer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockMaterial = block.getType();
        VeinMinerBlockConfig veinMinerBlockConfig = new VeinMinerBlockConfig(player.getUniqueId());
        List<Material> enabledBlocks = veinMinerBlockConfig.getEnabledBlocks();

        if (!enabledBlocks.contains(blockMaterial)) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType() == Material.AIR) {
            return;
        }

        if (block.getDrops(tool).isEmpty()) {
            return;
        }


        Map<Location,Block> veinBlocks = new HashMap<>();
        getVeinBlocks(block,veinBlocks, 0);

        SimpleVeinMiner.LOGGER.info("Vein Blocks: " + veinBlocks.size());
        veinBlocks.values().forEach(veinBlock -> veinBlock.breakNaturally(tool));
    }

    private void getVeinBlocks(Block block, Map<Location, Block> veinBlocks, int depth) {
        if (depth > 100) {
            return;
        }

        List<Block> surroundingBlocks = new ArrayList<>();
        surroundingBlocks.add(block.getRelative(1, 0, 0));
        surroundingBlocks.add(block.getRelative(-1, 0, 0));
        surroundingBlocks.add(block.getRelative(0, 1, 0));
        surroundingBlocks.add(block.getRelative(0, -1, 0));
        surroundingBlocks.add(block.getRelative(0, 0, 1));
        surroundingBlocks.add(block.getRelative(0, 0, -1));

        Material blockMaterial = block.getType();

        for (Block surroundingBlock : surroundingBlocks) {
            if (surroundingBlock.getType() == blockMaterial) {
                if (veinBlocks.containsKey(surroundingBlock.getLocation())) {
                    continue;
                }
                veinBlocks.put(surroundingBlock.getLocation(), surroundingBlock);
                getVeinBlocks(surroundingBlock, veinBlocks, depth + 1);
            }
        }
    }
}
