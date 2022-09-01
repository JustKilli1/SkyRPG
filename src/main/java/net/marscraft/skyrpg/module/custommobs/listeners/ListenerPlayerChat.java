package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ListenerPlayerChat implements Listener {

    private ILogManager logger;
    private IConfigManager messagesConfig;

    public ListenerPlayerChat(ILogManager logger, IConfigManager messagesConfig) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!ModuleCustomMobs.getSetups().containsKey(player.getUniqueId())) return;
        EventStorage eventStorage = new EventStorage();
        eventStorage.setAsyncPlayerChatEvent(event);
        ISetup setup = ModuleCustomMobs.getSetups().get(player.getUniqueId());
        setup.handleEvents(eventStorage);
    }

}
