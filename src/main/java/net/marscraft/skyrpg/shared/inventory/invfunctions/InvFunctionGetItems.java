package net.marscraft.skyrpg.shared.inventory.invfunctions;

import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionGetItems extends InvFunction{

    private ILogManager logger;
    private int startRow, endRow, startIndex, endIndex;
    private Inventory inv;

    public InvFunctionGetItems(ILogManager logger, int startRow, int endRow) {
        super(logger);
        this.logger = logger;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    public @Nullable Inventory add(Inventory target) {
        ItemStack[] invContents = target.getContents();
        inv = target;
        if(!setIndex(inv)) return null;
        for(int i = startIndex; i <= endIndex; i++) {
            invContents[i] = null;
        }
        target.setContents(invContents);
        inv = target;
        return inv;
    }

    /**
     * Gets all Items added to InventoryFunction and returns them as List
     * */
    public @Nullable List<ItemStack> getItems() {
        ItemStack[] invContents = inv.getContents();
        List<ItemStack> items = new ArrayList<>();
        if(!setIndex(inv)) return null;
        for(int i = startIndex; i <= endIndex; i++) {
            if(invContents[i] == null || invContents[i].getType() == Material.AIR) continue;
            items.add(invContents[i]);
        }
        return items;
    }

    private boolean setIndex(Inventory target) {
        ItemStack[] invContents = target.getContents();
        startIndex = getStartIndex(startRow);
        endIndex = getEndIndex(endRow);
        if(endIndex >= invContents.length) {
            logger.error("Could not add InvFunction GetItems to Inventory cause endIndex > Inventory Size", new Exception());
            return false;
        }
        return true;
    }

}
