package me.lebogo.simpleveinminer.veinminer;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("VeinMinerCategory")
public class VeinMinerCategory implements ConfigurationSerializable {
    private final String name;
    private final Material icon;
    private final List<Material> materials = new ArrayList<>();

    public VeinMinerCategory(String name, Material icon) {
        this.name = name;
        this.icon = icon;
    }

    public VeinMinerCategory(String name, Material icon, List<Material> materials) {
        this.name = name;
        this.icon = icon;
        this.materials.addAll(materials);
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("name", getName());
        result.put("icon", getIcon().name());

        List<String> stringMaterials = new ArrayList<>();
        for (Material material : materials) {
            stringMaterials.add(material.name());
        }

        result.put("materials", stringMaterials);

        return result;
    }

    public static VeinMinerCategory deserialize(Map<String, Object> args) {
        String name = "VeinMiner";
        Material icon = Material.STONE;
        List<Material> materials = new ArrayList<>();

        if (args.containsKey("name")) name = (String) args.get("name");
        if (args.containsKey("icon")) icon = Material.valueOf((String) args.get("icon"));
        if (args.containsKey("materials")) {
            for (String material : (List<String>) args.get("materials")) {
                materials.add(Material.valueOf(material));
            }
        }


        return new VeinMinerCategory(name, icon, materials);
    }

}
