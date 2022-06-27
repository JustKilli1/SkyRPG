package net.marscraft.skyrpg.module.customitems.customitem;

import org.bukkit.Material;

public enum ItemType {

    SWORD("Schwert"),
    BOW("Bogen"),
    TOOL("Werkzeug"),
    USABLEITEM("Benutzbares Item")

    ;

    private String displayName;

    ItemType(String displayName) {
        this.displayName = displayName;
    }

    public static ItemType valueOf(Material mat) {
        String matStr = mat.toString().toLowerCase();
        if(matStr.contains("sword")) return SWORD;
        else if(matStr.contains("bow")) return BOW;
        else if(matStr.contains("axe") || matStr.contains("pickaxe") || matStr.contains("shovel") || matStr.contains("hoe")) return TOOL;
        else return USABLEITEM;
    }

    public String getDisplayName() {
        return displayName;
    }
}
