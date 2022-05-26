package net.marscraft.skyrpg.module.customitems.customitem;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class CustomItem {

    private final ILogManager logger;
    private final DBHandlerCustomItems dbHandler;
    private boolean active;
    private int itemId;
    private String itemName;
    private ItemRarity rarity;
    private Material itemMat;
    private ItemStack item;
    private NamespacedKey keyItemId = new NamespacedKey(Main.getPlugin(Main.class), "customItem");

    public CustomItem(ILogManager logger, DBHandlerCustomItems dbHandler, int itemId, String itemName, ItemRarity rarity) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.itemId = itemId;
        this.itemName = itemName;
        this.rarity = rarity;
    }

    public CustomItem(ILogManager logger, DBHandlerCustomItems dbHandler, int itemId, String itemName, ItemRarity rarity, ItemStack item) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.itemId = itemId;
        this.itemName = itemName;
        this.rarity = rarity;
        this.item = item;
    }

    public ItemStack build() {
        if(itemMat == null) {
            logger.error("Could not Build Custom Item. Material is null", new Exception());
            return null;
        }
        itemId = dbHandler.getLastCustomItemId() + 1;
        item = new ItemBuilder(itemMat)
                .setDisplayname(itemName)
                .setLore(rarity.getDisplayName())
                .addPersistantDataToItemStack(keyItemId, itemId)
                .build()
                ;

        return item;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setItemMat(Material itemMat) {
        this.itemMat = itemMat;
    }
}
