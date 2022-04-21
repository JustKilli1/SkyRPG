package net.marscraft.skyrpg.shared.inventory;

import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MarsInventory {

    private ILogManager logger;
    protected Inventory marsInv;

    public MarsInventory(ILogManager logger) {
        this.logger = logger;
    }

    public Inventory buildBaseInventory(String title, int rows) {
        if(rows > 6) {
            logger.error("Inventory Size to big. InventoryTitle: " + title);
            return null;
        }
        int invSize = rows*9;
        Inventory inv = Bukkit.createInventory(null, invSize, title);
        ItemStack[] invContent = inv.getContents();

        ItemStack fillerItem = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname(" ").setLore(null).build();

        for(int i = 0; i < invSize; i++) {
            invContent[i] = fillerItem;
        }

        inv.setContents(invContent);
        return inv;
    }


}
