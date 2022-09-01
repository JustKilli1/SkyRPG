package net.marscraft.skyrpg.module.customitems.customitem;

public enum ItemRarity {

    UNGEWOEHNLICH("§7Ungewöhnlich", 1),
    NORMAL("§aNormal", 2),
    EPISCH("§9Episch", 3),
    LEGENDAER("§6Legendär", 4),
    EINZIGARTIG("§cEinzigartig", 5)
    ;

    private final String displayName;
    private final int id;

    ItemRarity(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }

    public static ItemRarity valueOf(int rarityAsInt) {

        switch (rarityAsInt) {
            case 1:
                return UNGEWOEHNLICH;
            case 2:
                return NORMAL;
            case 3:
                return EPISCH;
            case 4:
                return LEGENDAER;
            case 5:
                return EINZIGARTIG;
            default:
                return null;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }
}
