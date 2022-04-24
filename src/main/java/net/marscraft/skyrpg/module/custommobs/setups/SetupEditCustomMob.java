package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditOverview;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SetupEditCustomMob implements ISetup {

    private final ILogManager logger;
    private MessagesCustomMobs messages;
    private DBHandlerCustomMobs dbHandler;
    private InvEditOverview editOverview;

    public SetupEditCustomMob(ILogManager logger, MessagesCustomMobs messages, DBHandlerCustomMobs dbHandler) {
        this.logger = logger;
        this.messages = messages;
        this.dbHandler = dbHandler;
    }

    @Override
    public boolean handleEvents(EventStorage eventStorage) {
        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        IGuiInventory inv = getInventory(event.getView().getTitle(), false);
        if(inv == null) return false;

        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyFilterName, PersistentDataType.STRING)) {
            String filterName = clickedItem.getItemMeta().getPersistentDataContainer().get(keyFilterName, PersistentDataType.STRING);
            boolean active = filterName == "Active" ? true : false;

            IGuiInventory guiInv = getInventory(event.getView().getTitle(), active);
            guiInv.open(player);
            event.setCancelled(true);
            return true;
        }
        event.setCancelled(true);
        return false;
    }

    @Override
    public void handleCommands(Player player, String... args) {

        editOverview = new InvEditOverview(logger, messages, dbHandler, false);
        editOverview.open(player);

    }

    @Override
    public void finishSetup() {

    }

    @Override
    public boolean setupComplete() {
        return false;
    }

    /**
     * Gets GuiInventory matching invTitle
     */
    private IGuiInventory getInventory(String invTitle, boolean active) {

        InvEditOverview invEditOverview = new InvEditOverview(logger, messages, dbHandler, active);

        if (invEditOverview.getTitle().equals(invTitle)) return invEditOverview;
        return null;

    }

}
