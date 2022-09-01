package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ListenerPlayerChat implements Listener {

    private ILogManager logger;

    public ListenerPlayerChat(ILogManager logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!ModuleRegions.getSetups().containsKey(player.getUniqueId())) return;
        EventStorage eventStorage = new EventStorage();
        eventStorage.setAsyncPlayerChatEvent(event);
        ISetup setup = ModuleRegions.getSetups().get(player.getUniqueId());
        setup.handleEvents(eventStorage);
    }

}
