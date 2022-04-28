package net.marscraft.skyrpg.shared;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ItemBuilder {

    private ItemMeta itemMeta;
    private ItemStack itemStack;

    public ItemBuilder(Material mat, int amount) {
        itemStack = new ItemStack(mat, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material mat) {
        itemStack = new ItemStack(mat);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayname(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }
    public ItemBuilder setLocalizedName(String localizedName) {
        itemMeta.setLocalizedName(localizedName);
        return this;
    }
    public ItemBuilder setLore(String... lore) {
        if(lore == null) {
            itemMeta.setLore(null);
            return this;
        }
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }
    public ItemBuilder addItemFlags(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        return this;
    }
    /**
     * Adds PersistantData to ItemStack with value Type int
     */
    public ItemBuilder addPersistantDataToItemStack(NamespacedKey key, int value) {
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }
    /**
     * Adds PersistantData to ItemStack with value Type String
     */
    public ItemBuilder addPersistantDataToItemStack(NamespacedKey key, String value) {
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }
    public ItemStack build(){
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
