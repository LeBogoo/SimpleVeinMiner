package me.lebogo.simpleveinminer;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleVeinMinerTabCompleter implements TabCompleter {

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
