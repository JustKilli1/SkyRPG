package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ListenerInvClick implements Listener {

    private ILogManager logger;
    private IConfigManager messagesConfigManager;

    public ListenerInvClick(ILogManager logger, IConfigManager messagesConfigManager) {
        this.logger = logger;
        this.messagesConfigManager = messagesConfigManager;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        EventStorage eventStorage = new EventStorage();
        eventStorage.setInventoryClickEvent(event);
        Player player = (Player) event.getWhoClicked();

        if(ModuleCustomMobs.getSetups().containsKey(player.getUniqueId())) {
            ISetup setup = ModuleCustomMobs.getSetups().get(player.getUniqueId());
            setup.handleEvents(eventStorage);
        }


/*        Inventory eventInv = event.getClickedInventory();
        IGuiInventory guiInv = getInventory(event.getView().getTitle());
        if(guiInv == null) return;
        EventStorage eventStorage = new EventStorage();
        eventStorage.setInventoryClickEvent(event);
        event.setCancelled(guiInv.clickEventCanceled());
        guiInv.handleClickEvent(eventStorage);*/
    }


/*    private IGuiInventory getInventory(String invTitle) {

        InvCreateMob invCreateMob = new InvCreateMob(logger, messagesConfigManager);

        if(invCreateMob.getTitle().equals(invTitle)) return invCreateMob;
        else return null;
    }*/

}
