package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupCustomMobSpawnChance implements ISetup {

    private final ILogManager logger;
    private final DBHandlerCustomMobs dbHandler;
    private final DBAccessLayerCustomMobs sql;
    private MessagesCustomMobs messages;
    private int mobId;
    private double spawnChance;

    public SetupCustomMobSpawnChance(ILogManager logger, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql, MessagesCustomMobs messages, int mobId) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.messages = messages;
        this.mobId = mobId;
    }


    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        AsyncPlayerChatEvent playerChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(playerChatEvent != null) handleAsyncChatEvent(eventStorage, playerChatEvent);

        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        messages.sendEnterSpawnChanceMessage();
        player.closeInventory();
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.updateCustomMobSpawnChance(mobId, spawnChance);

    }

    @Override
    public boolean setupComplete() {
        return spawnChance > 0 && spawnChance < 101;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setCancelled(true);
        spawnChance = Utils.doubleFromStr(message);
        if(spawnChance <= 0 || spawnChance > 100) {
            messages.sendInvalidSpawnChanceMessage(message);
            return;
        }


        if(!finishSetup()) {
            messages.sendInvalidSpawnChanceMessage(message);
            return;
        }
        finishSetup();
        ModuleCustomMobs.removeSetup(player.getUniqueId());
        messages.sendSpawnChanceSetMessage(spawnChance);
    }

}
