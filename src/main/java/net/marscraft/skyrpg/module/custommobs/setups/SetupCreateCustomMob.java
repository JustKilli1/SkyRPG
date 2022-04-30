package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.createinventory.InvCreateMob;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
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
    public boolean handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) return handleInvClickEvent(eventStorage, invClickEvent);

        AsyncPlayerChatEvent asyncChatEvent = eventStorage.getAsyncPlayerChatEvent();
        if(asyncChatEvent != null) return handleAsyncChatEvent(asyncChatEvent);
        return false;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        MobHostile mob = new MobHostile(logger, mobId, mobName);
        if (!setupMobs.containsKey(player.getUniqueId())) {
            setupMobs.put(player.getUniqueId(), mob);
        }
        IGuiInventory iGuiInventory = new InvCreateMob(logger, messages, mob);
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
    private boolean handleInvClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!setupMobs.containsKey(player.getUniqueId())) return false;
        MobHostile target = setupMobs.get(player.getUniqueId());


        String invTitle = event.getView().getTitle();
        IGuiInventory setupInv = getInventory(invTitle, target);
        if(setupInv == null) return false;
        setupInv.handleEvents(eventStorage);

        event.setCancelled(true);
        return true;
    }

    /**
     * Handles AsyncPlayerChatEvent
     * @param event Calling Event
     */
    private boolean handleAsyncChatEvent(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if(!setupMobs.containsKey(player.getUniqueId())) return false;
        hostileMob = setupMobs.get(player.getUniqueId());
        if(hostileMob.getType() == null) {
            setupMobs.remove(player.getUniqueId());
            return false;
        }
        String message = event.getMessage();
        event.setCancelled(true);
        if(message.equalsIgnoreCase("cancel")) {
            setupMobs.remove(player.getUniqueId());
            ModuleCustomMobs.removeSetup(player.getUniqueId());
            messages.sendSetupCancelledMessage();
            return true;
        }
        // Does things when maxHealth is not set
        if(hostileMob.getBaseHealth() <= 0) {
            double maxHealth = Utils.doubleFromStr(message);
            if(maxHealth <= 0) {
                messages.sendInvalidMaxHealthMessage(message);
                return false;
            }
            hostileMob.setBaseHealth(maxHealth);
            messages.sendBaseHealthSetMessage(maxHealth);
            messages.sendEnterSpawnChanceMessage();

            return true;
        }// Does things when maxHealth is set but spawnChance not
        else if(hostileMob.getSpawnChance() <= 0) {
            double spawnChance = Utils.doubleFromStr(message);
            if(spawnChance <= 0 || spawnChance > 100) {
                messages.sendInvalidSpawnChanceMessage(message);
                return false;
            }
            hostileMob.setSpawnChance(spawnChance);
            messages.sendSpawnChanceSetMessage(spawnChance);
            if(!finishSetup()) {
                messages.sendCreateCustomMobErrorMessage();
                return false;
            }
            setupMobs.remove(player.getUniqueId());
            ModuleCustomMobs.removeSetup(player.getUniqueId());
            messages.sendMobBaseSetupCompleteMessage();
            return true;
        }
        return false;
    }

    /**
     * Gets GuiInventory matching invTitle
     */
    private IGuiInventory getInventory(String invTitle, MobHostile mob) {

        InvCreateMob invCreateMob = new InvCreateMob(logger, messages, mob);

        if (invCreateMob.getTitle().equals(invTitle)) return invCreateMob;
        else return null;

    }
}
