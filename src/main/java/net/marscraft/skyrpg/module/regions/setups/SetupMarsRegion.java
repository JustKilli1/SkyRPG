package net.marscraft.skyrpg.module.regions.setups;

import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.module.regions.region.Bound;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupMarsRegion implements ISetup {

    private static Map<UUID, Bound> boundarySetup = new HashMap<>();
    private final ILogManager logger;
    private final DBHandlerRegions dbHandler;
    private final DBAccessLayerRegions sql;
    private MessagesRegions messages;
    private String regionName;
    private Region region;
    private Bound bound;
    private Player player;

    public SetupMarsRegion(ILogManager logger, String regionName, DBAccessLayerRegions sql, MessagesRegions messages, DBHandlerCustomMobs dbHandlerCustomMobs) {
        this.logger = logger;
        this.regionName = regionName;
        this.sql = sql;
        this.messages = messages;
        dbHandler = new DBHandlerRegions(this.logger, this.sql, dbHandlerCustomMobs);
        bound = new Bound();
        region = new Region(logger, this.regionName, bound);
    }


    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        return handlePlayerInteractEvent(eventStorage);

    }

    private <T> T handlePlayerInteractEvent(EventStorage eventStorage) {
        PlayerInteractEvent event = eventStorage.getPlayerInteractEvent();
        player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null)  return null;
        if (!boundarySetup.containsKey(player.getUniqueId())) return null;
        bound = boundarySetup.get(player.getUniqueId());

        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return null;

        if(!bound.isLoc1Set()) {
            bound.setLoc1(block.getLocation());
            region.setBound(bound);
            messages.sendMessageLocSet(1);
            return null;
        } else if(bound.isLoc1Set()){
            bound.setLoc2(block.getLocation());
            region.setBound(bound);
            messages.sendMessageLocSet(2);
            finishSetup();
            return null;
        }
        event.setCancelled(true);
        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {

        this.player = player;
        if(boundarySetup.containsKey(player.getUniqueId())) return;

        bound = new Bound();
        boundarySetup.put(player.getUniqueId(), bound);

        messages.sendSelectRegion();
    }

    @Override
    public boolean finishSetup() {
        bound.assignCorrectBounds();
        int newRegionId = dbHandler.getLastRegionId() + 1;
        Region region = new Region(logger, newRegionId, regionName, bound);
        sql.insertRegion(region);
        boundarySetup.remove(player.getUniqueId());
        ModuleRegions.removeSetup(player.getUniqueId());
        messages.sendRegionCreated(regionName);
        return true;
    }

    @Override
    public boolean setupComplete() {
        return false;
    }
}
