package net.marscraft.skyrpg.module.regions.setups;

import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupChangeMSRMaxMobs implements ISetup {

    private final ILogManager logger;
    private Region region;
    private MessagesRegions messages;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private int newMaxMobs;

    public SetupChangeMSRMaxMobs(ILogManager logger, Region region, MessagesRegions messages, DBHandlerRegions dbHandler, DBAccessLayerRegions sql) {
        this.logger = logger;
        this.region = region;
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
        messages.sendChangeMaxMobs();
        player.closeInventory();
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        int newId = dbHandler.getLastMobSpawnRegionId() + 1;
        MobSpawnRegion mobSpawnRegion = new MobSpawnRegion(logger, newId, region, newMaxMobs, null, false);
        return sql.insertMobSpawnRegion(mobSpawnRegion);
    }

    @Override
    public boolean setupComplete() {
        return newMaxMobs > 0;
    }

    private void handleAsyncChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        newMaxMobs = Utils.intFromStr(event.getMessage());
        if(newMaxMobs <= 0) messages.sendMaxMobsInvalid(event.getMessage());
        if(!finishSetup()) return;
        ModuleRegions.removeSetup(player.getUniqueId());
        messages.sendMSRegionMaxMobsSetMessage(newMaxMobs);
    }

}
