package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import net.marscraft.skyrpg.module.regions.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListenerPlayerMove implements Listener {

    private final ILogManager logger;
    private IConfigManager configManager;
    private DBAccessLayerRegions sql;
    private DBHandlerRegions dbHandler;
    private Map<UUID, MobSpawnRegion> playerRegions = new HashMap<>();

    public ListenerPlayerMove(ILogManager logger, IConfigManager configManager, DBAccessLayerRegions sql, DBHandlerCustomMobs dbHandlerCustomMobs, Main plugin) {
        this.logger = logger;
        this.configManager = configManager;
        this.sql = sql;
        dbHandler = new DBHandlerRegions(this.logger, this.sql, dbHandlerCustomMobs, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        MessageManager messageManager = new MessageManager(logger, configManager, player);
        MobSpawnRegion current = playerRegions.get(player.getUniqueId());

        List<MobSpawnRegion> regions = dbHandler.getAllMobSpawnRegions();
        MobSpawnRegion active = null;
        for(MobSpawnRegion region : regions) {
            if(region.getRegion().getBound().isWithinBounds(player.getLocation())) {
                active = region;
                if(current != null && current.getId() == region.getId() ) continue;
                String mobLevelInRegion = getMobLevelInRegion(region);
                player.sendTitle("§c" + region.getRegion().getName(), "§aMobLevel " + mobLevelInRegion, 15, 50, 15);
            }
        }
        playerRegions.put(player.getUniqueId(), active);
    }

    private String getMobLevelInRegion(MobSpawnRegion mobSpawnRegion) {
        Map<MobHostile, MobCharacteristics> mobs = mobSpawnRegion.getMobs();

        int minLevel = 0;
        int maxLevel = 0;

        for(MobHostile mob : mobs.keySet()) {
            MobCharacteristics value = mobs.get(mob);
            if(minLevel == 0) minLevel = value.getLevelMin();
            if(value.getLevelMin() < minLevel) minLevel = value.getLevelMin();
            if(value.getLevelMax() > maxLevel) maxLevel = value.getLevelMax();
        }
        String result = minLevel + "-" + maxLevel;
        return result;
    }

}
