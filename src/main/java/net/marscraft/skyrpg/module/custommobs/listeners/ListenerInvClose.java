package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditItems;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ListenerInvClose implements Listener {


    private ILogManager logger;
    private IConfigManager messagesConfig;
    private MessagesCustomMobs messages;
    private DBHandlerCustomMobs dbHandler;
    private DBAccessLayerCustomMobs sql;

    public ListenerInvClose(ILogManager logger, IConfigManager messagesConfig, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
        this.dbHandler = dbHandler;
        this.sql = sql;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String invTitle = event.getView().getTitle();
        if(invTitle.equalsIgnoreCase(InvEditItems.title)) {
            ModuleCustomMobs.removeSetup(player.getUniqueId());
        }
        //Experimental
        if(ModuleCustomMobs.getSetups().containsKey(player.getUniqueId())) {
            ISetup setup = ModuleCustomMobs.getSetups().get(player.getUniqueId());
            if(setup.setupComplete()) ModuleCustomMobs.removeSetup(player.getUniqueId());
        }
    }

}
