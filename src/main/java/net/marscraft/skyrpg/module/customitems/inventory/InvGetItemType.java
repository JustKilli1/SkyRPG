package net.marscraft.skyrpg.module.customitems.inventory;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGetItems;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InvGetItemType extends MarsInventory implements IGuiInventory {


    public final static String title = "ItemType festlegen";
    private final ILogManager logger;
    private MessagesCustomItems messages;
    private InvFunctionGetItems invFunctionGetItems;

    public InvGetItemType(ILogManager logger, MessagesCustomItems messages) {
        super(logger);
        this.logger = logger;
        this.messages = messages;
    }

    @Override
    public Inventory build() {
        Inventory inv = buildBaseInventory(title, 1);
        invFunctionGetItems = new InvFunctionGetItems(logger, 1, 1);
        inv = invFunctionGetItems.add(inv);
        return inv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        player.openInventory(inv);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {
        InventoryCloseEvent event = eventStorage.getInventoryCloseEvent();
        if(event == null) return null;
        return (T) invFunctionGetItems.getItems();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
