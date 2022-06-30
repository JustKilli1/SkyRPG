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

public class SetupMobLevel implements ISetup {

    private final ILogManager logger;
    private MobCharacteristics mobCharacteristics;
    private MessagesRegions messages;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private int minLevel, maxLevel;

    public SetupMobLevel(ILogManager logger, MobCharacteristics mobCharacteristics, MessagesRegions messages, DBHandlerRegions dbHandler, DBAccessLayerRegions sql) {
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
        messages.sendChangeMobMinLevel();
        player.closeInventory();
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.updateMobSpawnRegionMob(mobCharacteristics);
    }

    @Override
    public boolean setupComplete() {
        if(minLevel > 0 && maxLevel > 0) return true;
        return false;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        if(minLevel <= 0) {
            minLevel = Utils.intFromStr(event.getMessage());
            if(minLevel <= 0) {
                messages.sendInvalidLevel(event.getMessage());
                return;
            }
            mobCharacteristics.setLevelMin(minLevel);
            messages.sendChangeMobMaxLevel();
        } else {
            maxLevel = Utils.intFromStr(event.getMessage());
            if(maxLevel <= 0) {
                messages.sendInvalidLevel(event.getMessage());
                return;
            }
            if(maxLevel <= minLevel) {
                messages.sendMaxLevelInvalid();
                return;
            }
            mobCharacteristics.setLevelMax(maxLevel);
            if(!finishSetup()) return;
            ModuleRegions.removeSetup(player.getUniqueId());
            messages.sendMobLevelChanged();
        }
    }

}
