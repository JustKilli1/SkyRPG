package net.marscraft.skyrpg.shared.inventory;

import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MarsInventory {

    private ILogManager logger;
    protected Inventory marsInv;

    private ItemStack fillerItem = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname(" ").setLore(null).build();

    public MarsInventory(ILogManager logger) {
        this.logger = logger;
    }

    public Inventory buildBaseInventory(String title, int rows) {
        if(rows > 6) {
            logger.error("Inventory Size to big. InventoryTitle: " + title);
            return null;
        }
        int invSize = rows*9;
        marsInv = Bukkit.createInventory(null, invSize, title);
        fillInventory(marsInv, 0, invSize - 1);

        return marsInv;
    }

    public Inventory resetInvContents(Inventory inv, int startRow, int endRow) {
        InvFunction invFunction = new InvFunction(logger);
        int startIndex = invFunction.getStartIndex(startRow);
        int endIndex = invFunction.getEndIndex(endRow);
        return fillInventory(inv, startIndex, endIndex);
    }

    private Inventory fillInventory(Inventory inv, int startIndex, int endIndex) {
        ItemStack[] invContents = inv.getContents();
        for(int i = startIndex; i <= endIndex; i++) {
            invContents[i] = fillerItem;
        }
        inv.setContents(invContents);
        return inv;
    }


}
