package net.marscraft.skyrpg.shared.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InvFunctionGoBack extends InvFunction{

    private final ILogManager logger;
    private List<IGuiInventory> previousGuiInv;

    public InvFunctionGoBack(ILogManager logger) {
        super(logger);
        this.logger = logger;
        previousGuiInv = new ArrayList<>();
    }

    public Inventory add(Inventory inv, int row) {
        int startIndex = (row * 9) - 9;
        ItemStack[] invContents = inv.getContents();
        if(invContents.length <= startIndex) {
            logger.error("Could not add back function to Inventory");
            return null;
        }

        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");
        ItemStack goBackItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                                    .setDisplayname("§cZurück")
                                    .setLore("§aGehe ein Inventory zurück")
                                    .addPersistantDataToItemStack(keyGoBack, 1)
                                    .build();
        invContents[startIndex] = goBackItem;
        inv.setContents(invContents);
        return inv;
    }

    public @Nullable IGuiInventory getPreviousInventory() {
        if(previousGuiInv.size() == 0) return null;
        int lastIndex = previousGuiInv.size() - 1;
        IGuiInventory previousInventory = previousGuiInv.get(lastIndex);
        previousGuiInv.remove(lastIndex);
        return previousInventory;
    }

    public void addGuiInventory(IGuiInventory inv) {
        previousGuiInv.add(inv);
    }


}
