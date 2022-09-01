package net.marscraft.skyrpg.module.customitems.listeners;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.ModuleCustomItems;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ListenerInvClose implements Listener {

    private final ILogManager logger;
    private MessagesCustomItems messages;
    private IConfigManager messagesConfig;


    public ListenerInvClose(ILogManager logger, IConfigManager messagesConfig) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        messages = new MessagesCustomItems(logger, messagesConfig, player);
        EventStorage eventStorage = new EventStorage();
        eventStorage.setInventoryCloseEvent(event);

        if (ModuleCustomItems.getSetups().containsKey(player.getUniqueId())) {
            ISetup setup = ModuleCustomItems.getSetups().get(player.getUniqueId());
            setup.handleEvents(eventStorage);
        }
    }
}
