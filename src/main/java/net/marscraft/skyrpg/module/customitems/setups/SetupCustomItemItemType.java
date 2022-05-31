package net.marscraft.skyrpg.module.customitems.setups;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.ModuleCustomItems;
import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.module.customitems.database.DBAccesLayerCustomItems;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.module.customitems.inventory.InvGetItemType;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetupCustomItemItemType implements ISetup {

    private final ILogManager logger;
    private CustomItem customItem;
    private MessagesCustomItems messages;
    private DBHandlerCustomItems dbHandler;
    private DBAccesLayerCustomItems sql;
    private IGuiInventory inv;

    public SetupCustomItemItemType(ILogManager logger, CustomItem customItem, MessagesCustomItems messages, DBHandlerCustomItems dbHandler, DBAccesLayerCustomItems sql) {
        this.logger = logger;
        this.customItem = customItem;
        this.dbHandler = dbHandler;
        this.messages = messages;
        this.sql = sql;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {
        InventoryCloseEvent invCloseEvent = eventStorage.getInventoryCloseEvent();
        if(invCloseEvent == null) return null;
        Player player = (Player) invCloseEvent.getPlayer();
        List<ItemStack> items = inv.handleEvents(eventStorage);
        ModuleCustomItems.removeSetup(player.getUniqueId());
        if(items.size() == 0) {
            messages.sendInvalidItemTypeMessage();
            sql.deleteCustomItem(customItem.getItemId());
            return null;
        }
        customItem.setItemMat(items.get(0).getType());
        player.getInventory().addItem(customItem.getItem());
        if(!setupComplete()) {
            sql.deleteCustomItem(customItem.getItemId());
            return null;
        }
        finishSetup();
        return (T) customItem;

    }

    @Override
    public void handleCommands(Player player, String... args) {
        inv = new InvGetItemType(logger, messages);
        inv.open(player);
    }

    @Override
    public boolean finishSetup() {
        sql.updateCustomItem(customItem);
        return false;
    }

    @Override
    public boolean setupComplete() {
        if(customItem == null) return false;
        Material mat = customItem.getItemMat();
        if(mat == null || mat == Material.AIR) return false;
        return true;
    }
}
