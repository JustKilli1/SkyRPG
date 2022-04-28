package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupCustomMobEntityType implements ISetup {

    private final ILogManager logger;
    private final DBHandlerCustomMobs dbHandler;
    private final DBAccessLayerCustomMobs sql;
    private MessagesCustomMobs messages;
    private int mobId;
    private EntityType newType;

    public SetupCustomMobEntityType(ILogManager logger, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql, MessagesCustomMobs messages, int mobId) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.messages = messages;
        this.mobId = mobId;
    }


    @Override
    public boolean handleEvents(EventStorage eventStorage) {

        AsyncPlayerChatEvent playerChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(playerChatEvent != null) handleAsyncChatEvent(eventStorage, playerChatEvent);

        return false;
    }

    @Override
    public void handleCommands(Player player, String... args) {

    }

    @Override
    public void finishSetup() {
        if(setupComplete()) sql.updateCustomMobType(mobId, newType.toString());
    }

    @Override
    public boolean setupComplete() {
        return newType != null;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        newType = EntityType.valueOf(event.getMessage());
        if(newType == null) {
            return;
        }
        event.setCancelled(true);
        finishSetup();
        ModuleCustomMobs.removeSetup(player.getUniqueId());
        messages.sendTypeSetMessage(newType.toString());
    }

}
