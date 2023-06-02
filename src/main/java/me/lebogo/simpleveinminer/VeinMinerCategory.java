package me.lebogo.simpleveinminer;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class VeinMinerCategory {
    private final String name;
    private final Material icon;
    private final List<Material> materials = new ArrayList<>();

    public VeinMinerCategory(String name, Material icon) {
        this.name = name;
        this.icon = icon;
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
}
