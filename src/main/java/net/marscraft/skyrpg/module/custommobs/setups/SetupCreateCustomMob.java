package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.createinventory.InvSelectMobType;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupCreateCustomMob implements ISetup {

    private static Map<UUID, MobHostile> setupMobs = new HashMap<>();
    private final ILogManager logger;
    private final DBHandlerCustomMobs dbHandler;
    private final DBAccessLayerCustomMobs sql;
    private MobHostile hostileMob;
    private String mobName;
    private int mobId;
    private MessagesCustomMobs messages;

    public SetupCreateCustomMob(ILogManager logger, MessagesCustomMobs messages, DBAccessLayerCustomMobs sql, int mobId, String mobName) {
        this.logger = logger;
        this.messages = messages;
        this.mobName = mobName;
        this.mobId = mobId;
        this.sql = sql;
        dbHandler = new DBHandlerCustomMobs(this.logger, this.sql);
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) return handleInvClickEvent(eventStorage, invClickEvent);

        AsyncPlayerChatEvent asyncChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(asyncChatEvent != null) return handleAsyncChatEvent(asyncChatEvent);
        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        MobHostile mob = new MobHostile(logger, mobId, mobName);
        if (!setupMobs.containsKey(player.getUniqueId())) {
            setupMobs.put(player.getUniqueId(), mob);
        }
        IGuiInventory iGuiInventory = new InvSelectMobType(logger, messages, mob);
        iGuiInventory.open(player);
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) {
            messages.sendCreateCustomMobErrorMessage();
            return false;
        }
        sql.insertCustomMob(hostileMob);
        return true;
    }

    @Override
    public boolean setupComplete() {
        if(mobId <= 0) return false;
        if(mobName == null || mobName.length() == 0) return false;
        if(hostileMob == null) return false;
        return hostileMob.setupComplete();
    }

    /**
     * Handles InventoryClickEvent
     * @param eventStorage Storage with calling Event
     * @param event Calling Event
     */
    private <T> T handleInvClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!setupMobs.containsKey(player.getUniqueId())) return null;
        MobHostile target = setupMobs.get(player.getUniqueId());


        String invTitle = event.getView().getTitle();
        IGuiInventory setupInv = getInventory(invTitle, target);
        if(setupInv == null) return null;
        setupInv.handleEvents(eventStorage);

        event.setCancelled(true);
        if(event.getCurrentItem().getType() != Material.BLACK_STAINED_GLASS_PANE) messages.sendEnterBaseHealthMessage();
        return null;
    }

    /**
     * Handles AsyncPlayerChatEvent
     * @param event Calling Event
     */
    private <T> T handleAsyncChatEvent(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if(!setupMobs.containsKey(player.getUniqueId())) return null;
        hostileMob = setupMobs.get(player.getUniqueId());
        if(hostileMob.getType() == null) {
            setupMobs.remove(player.getUniqueId());
            return null;
        }
        String message = event.getMessage();
        event.setCancelled(true);
        if(message.equalsIgnoreCase("cancel")) {
            setupMobs.remove(player.getUniqueId());
            ModuleCustomMobs.removeSetup(player.getUniqueId());
            messages.sendSetupCancelledMessage();
            return null;
        }
        // Does things when maxHealth is not set
        if(hostileMob.getBaseHealth() <= 0) {
            double maxHealth = Utils.doubleFromStr(message);
            if(maxHealth <= 0) {
                messages.sendInvalidMaxHealthMessage(message);
                return null;
            }
            hostileMob.setBaseHealth(maxHealth);
            messages.sendBaseHealthSetMessage(maxHealth);
            messages.sendEnterSpawnChanceMessage();

            return null;
        }// Does things when maxHealth is set but spawnChance not
        else if(hostileMob.getSpawnChance() <= 0) {
            double spawnChance = Utils.doubleFromStr(message);
            if(spawnChance <= 0 || spawnChance > 100) {
                messages.sendInvalidSpawnChanceMessage(message);
                return null;
            }
            hostileMob.setSpawnChance(spawnChance);
            messages.sendSpawnChanceSetMessage(spawnChance);
            if(!finishSetup()) {
                messages.sendCreateCustomMobErrorMessage();
                return null;
            }
            setupMobs.remove(player.getUniqueId());
            ModuleCustomMobs.removeSetup(player.getUniqueId());
            messages.sendMobBaseSetupCompleteMessage();
            return null;
        }
        return null;
    }

    /**
     * Gets GuiInventory matching invTitle
     */
    private IGuiInventory getInventory(String invTitle, MobHostile mob) {

        InvSelectMobType invSelectMobType = new InvSelectMobType(logger, messages, mob);

        if (invSelectMobType.getTitle().equals(invTitle)) return invSelectMobType;
        else return null;

    }
}
