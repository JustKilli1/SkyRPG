package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ListenerInvClose implements Listener {

    private final ILogManager logger;

    public ListenerInvClose(ILogManager logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if(ModuleRegions.getInvs().containsKey(player.getUniqueId())) {
            ModuleRegions.removeInv(player.getUniqueId());

        }

    }

}
