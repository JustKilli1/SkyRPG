package net.marscraft.skyrpg.module.regions.setups;

import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupChangeRegionName implements ISetup {

    private final ILogManager logger;
    private final DBAccessLayerRegions sql;
    private MessagesRegions messages;
    private int regionId;
    private String newName;

    public SetupChangeRegionName(ILogManager logger, DBAccessLayerRegions sql, MessagesRegions messages, int regionId) {
        this.logger = logger;
        this.sql = sql;
        this.messages = messages;
        this.regionId = regionId;
    }


    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        AsyncPlayerChatEvent playerChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(playerChatEvent != null) handleAsyncChatEvent(eventStorage, playerChatEvent);

        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        messages.sendEnterNewNameMessage();
        player.closeInventory();
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.updateRegionsName(regionId, newName);
    }

    @Override
    public boolean setupComplete() {
        return newName != null;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        newName = event.getMessage();
        event.setCancelled(true);
        if(!finishSetup()) return;
        ModuleRegions.removeSetup(player.getUniqueId());
        messages.sendRegionNameSetMessage(newName);
    }

}
