package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ListenerPlayerInteract implements Listener {

    private ILogManager logger;
    private IConfigManager messagesConfig;

    public ListenerPlayerInteract(ILogManager logger, IConfigManager messagesConfig) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!ModuleRegions.getSetups().containsKey(player.getUniqueId())) return;

        ISetup setup = ModuleRegions.getSetups().get(player.getUniqueId());
        EventStorage eventStorage = new EventStorage();
        eventStorage.setPlayerInteractEvent(event);
        setup.handleEvents(eventStorage);

    }

}
