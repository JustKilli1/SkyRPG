package net.marscraft.skyrpg.module.regions.setups.mobspawnregion;

import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.mobspawnregion.InvAddMob;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGoBack;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupAddNewMob implements ISetup {

    private final ILogManager logger;
    private MobSpawnRegion mobSpawnRegion;
    private DBHandlerCustomMobs dbHandlerCustomMobs;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private InvFunctionGoBack invFunctionGoBack;
    private MessagesRegions messages;
    private IGuiInventory guiInventory;
    private MobHostile mobHostile;
    private double spawnChance;
    private int minLevel, maxLevel;
    private MobCharacteristics mobCharacteristics;

    public SetupAddNewMob(ILogManager logger, MobSpawnRegion mobSpawnRegion, DBHandlerCustomMobs dbHandlerCustomMobs, DBHandlerRegions dbHandler, DBAccessLayerRegions sql, InvFunctionGoBack invFunctionGoBack, MessagesRegions messages) {
        this.logger = logger;
        this.mobSpawnRegion = mobSpawnRegion;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.invFunctionGoBack = invFunctionGoBack;
        this.messages = messages;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) {
            Player player = (Player) invClickEvent.getWhoClicked();
            T returnedMob = handleInvClickEvent(eventStorage, invClickEvent);
            if(returnedMob != null) {
                mobHostile = (MobHostile) returnedMob;
                player.closeInventory();
                messages.sendChangeSpawnChance();
                return null;
            }
        }

        AsyncPlayerChatEvent playerChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(playerChatEvent != null) {
            handleChatEvent(eventStorage, playerChatEvent);
        }

        return null;
    }

    private void handleChatEvent(EventStorage eventStorage, AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        event.setCancelled(true);
        if(spawnChance <= 0) {
            spawnChance = Utils.intFromStr(event.getMessage());
            if (spawnChance <= 0 || spawnChance > 100) {
                messages.sendSpawnChanceInvalid(event.getMessage());
                return;
            }
            messages.sendMSRSpawnChanceSetMessage(spawnChance);
            messages.sendChangeMobMinLevel();
        } else if(minLevel <= 0) {
            minLevel = Utils.intFromStr(event.getMessage());
            if(minLevel <= 0) {
                messages.sendInvalidLevel(event.getMessage());
                return;
            }
            messages.sendChangeMobMaxLevel();
        } else {
            maxLevel = Utils.intFromStr(event.getMessage());
            if(maxLevel <= 0) {
                messages.sendInvalidLevel(event.getMessage());
                return;
            }
            if(maxLevel <= minLevel) {
                messages.sendMaxLevelInvalid();
                maxLevel = 0;
                return;
            }
            int controllerId = dbHandler.getLastMobControllerId() + 1;
            mobCharacteristics = new MobCharacteristics(spawnChance, controllerId, minLevel, maxLevel);
            if(!finishSetup()) return;
            ModuleRegions.removeSetup(player.getUniqueId());
            messages.sendNewMobAdded();
        }
    }

    private <T> T handleInvClickEvent(EventStorage eventStorage, InventoryClickEvent invClickEvent) {

        return guiInventory.handleEvents(eventStorage);

    }

    @Override
    public void handleCommands(Player player, String... args) {
        guiInventory = new InvAddMob(logger, dbHandlerCustomMobs, invFunctionGoBack);
        guiInventory.open(player);
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.insertMob(mobSpawnRegion.getId(), mobHostile.getId(), mobCharacteristics);
    }

    @Override
    public boolean setupComplete() {
        if(spawnChance <= 0 || minLevel <= 0 || maxLevel <= 0 || maxLevel <= minLevel || mobCharacteristics == null || mobHostile == null) return false;
        return true;
    }
}
