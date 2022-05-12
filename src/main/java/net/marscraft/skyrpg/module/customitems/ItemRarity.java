package net.marscraft.skyrpg.module.customitems;

public enum ItemRarity {

    UNGEWOEHNLICH("§7Ungewöhnlich", 1),
    NORMAL("§aNormal", 2),
    EPISCH("§9Episch", 3),
    LEGENDAER("§6Legendär", 4),
    EINZIGARTIG("§cEinzigartig", 5)
    ;

    private String displayName;
    private int id;

    ItemRarity(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }


    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }
}
