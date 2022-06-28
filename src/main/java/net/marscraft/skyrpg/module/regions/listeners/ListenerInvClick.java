package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ListenerInvClick implements Listener {

    private final ILogManager logger;

    public ListenerInvClick(ILogManager logger) {
        this.logger = logger;
    }


    @EventHandler
    public void onInvClick(InventoryClickEvent event) {

        EventStorage eventStorage = new EventStorage();
        eventStorage.setInventoryClickEvent(event);
        Player player = (Player) event.getWhoClicked();

        if(ModuleRegions.getInvs().containsKey(player.getUniqueId())) {
            IGuiInventory inv = ModuleRegions.getInvs().get(player.getUniqueId());
            inv.handleEvents(eventStorage);
            ModuleRegions.addInv(player.getUniqueId(), inv);
        }

    }


}
