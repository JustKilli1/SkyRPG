package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditMobDetails;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditOverview;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nullable;

public class ListenerInvClick implements Listener {

    private ILogManager logger;
    private IConfigManager messagesConfig;
    private MessagesCustomMobs messages;
    private DBHandlerCustomMobs dbHandler;
    private DBAccessLayerCustomMobs sql;

    public ListenerInvClick(ILogManager logger, IConfigManager messagesConfig, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
        this.dbHandler = dbHandler;
        this.sql = sql;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        EventStorage eventStorage = new EventStorage();
        eventStorage.setInventoryClickEvent(event);
        Player player = (Player) event.getWhoClicked();
        messages = new MessagesCustomMobs(logger, messagesConfig, player);

        if (ModuleCustomMobs.getSetups().containsKey(player.getUniqueId())) {
            ISetup setup = ModuleCustomMobs.getSetups().get(player.getUniqueId());
            setup.handleEvents(eventStorage);
            return;
        }

        IGuiInventory guiInventory = getGuiInventory(event.getView().getTitle());
        if(guiInventory == null) return;
        guiInventory.handleEvents(eventStorage);

    }

    private @Nullable IGuiInventory getGuiInventory(@Nullable String title) {

        switch (title) {
            case InvEditMobDetails.title:
                return new InvEditMobDetails(logger, dbHandler, messages, sql);
            case InvEditOverview.title:
                return new InvEditOverview(logger, messages, dbHandler);
            default:
                return null;
        }

    }
}
