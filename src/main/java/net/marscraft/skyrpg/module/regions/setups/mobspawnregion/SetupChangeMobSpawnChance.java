package net.marscraft.skyrpg.module.regions.setups.mobspawnregion;

import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupChangeMobSpawnChance implements ISetup {

    private final ILogManager logger;
    private MobCharacteristics mobCharacteristics;
    private MessagesRegions messages;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private double newSpawnChance;

    public SetupChangeMobSpawnChance(ILogManager logger, MobCharacteristics mobCharacteristics, MessagesRegions messages, DBHandlerRegions dbHandler, DBAccessLayerRegions sql) {
        this.logger = logger;
        this.mobCharacteristics = mobCharacteristics;
        this.messages = messages;
        this.dbHandler = dbHandler;
        this.sql = sql;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {
        AsyncPlayerChatEvent playerChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(playerChatEvent != null) handleAsyncChatEvent(eventStorage, playerChatEvent);

        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        messages.sendChangeSpawnChance();
        player.closeInventory();
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.updateMobSpawnRegionMob(mobCharacteristics);
    }

    @Override
    public boolean setupComplete() {
        if(newSpawnChance > 0 && newSpawnChance <= 100) return true;
        return false;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        newSpawnChance = Utils.intFromStr(event.getMessage());
        if(newSpawnChance <= 0 || newSpawnChance > 100) messages.sendSpawnChanceInvalid(event.getMessage());
        mobCharacteristics.setSpawnChance(newSpawnChance);
        if(!finishSetup()) return;
        ModuleRegions.removeSetup(player.getUniqueId());
        messages.sendMSRSpawnChanceSetMessage(newSpawnChance);
    }

}
