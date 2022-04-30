package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditItems;
import net.marscraft.skyrpg.module.custommobs.inventory.editinventory.InvEditOverview;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGoBack;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupCustomMobItems implements ISetup {

    private final ILogManager logger;
    private final DBHandlerCustomMobs dbHandler;
    private final DBAccessLayerCustomMobs sql;
    private MessagesCustomMobs messages;
    private MobHostile hostileMob;
    private IGuiInventory inv;

    public SetupCustomMobItems(ILogManager logger, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql, MessagesCustomMobs messages, int mobId) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.messages = messages;
        hostileMob = this.dbHandler.getHostileMobById(mobId);
    }


    @Override
    public boolean handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) handleInventoryClickEvent(eventStorage, invClickEvent);

        return false;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        InvFunctionGoBack goBack = InvEditOverview.getPreviousInvs().get(player.getUniqueId());
        inv = new InvEditItems(logger, dbHandler, messages, hostileMob, goBack);
        inv.open(player);
    }

    @Override
    public boolean finishSetup() {
        if(!sql.updateCustomMobArmor(hostileMob.getId(), hostileMob.getArmor())) return false;
        return sql.updateCustomMobMainItem(hostileMob.getId(), hostileMob.getMainItem());
    }

    @Override
    public boolean setupComplete() {
        return true;
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        hostileMob = inv.handleEvents(eventStorage);
        finishSetup();

        event.setCancelled(true);

    }

}
