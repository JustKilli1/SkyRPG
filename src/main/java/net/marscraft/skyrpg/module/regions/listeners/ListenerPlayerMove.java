package net.marscraft.skyrpg.module.regions.listeners;

import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import net.marscraft.skyrpg.module.regions.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class ListenerPlayerMove implements Listener {

    private final ILogManager logger;
    private IConfigManager configManager;
    private DBAccessLayerRegions sql;
    private DBHandlerRegions dbHandler;

    public ListenerPlayerMove(ILogManager logger, IConfigManager configManager, DBAccessLayerRegions sql, DBHandlerCustomMobs dbHandlerCustomMobs) {
        this.logger = logger;
        this.configManager = configManager;
        this.sql = sql;
        dbHandler = new DBHandlerRegions(this.logger, this.sql, dbHandlerCustomMobs);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        MessageManager messageManager = new MessageManager(logger, configManager, player);

        List<Region> regions = dbHandler.getAllRegions();
        for(Region region : regions) {
            if(region.getBound().isWithinBounds(player.getLocation().getBlock().getLocation())) {
                messageManager.sendPlayerMessage("Bist drin :o");
            }
        }
    }
}
