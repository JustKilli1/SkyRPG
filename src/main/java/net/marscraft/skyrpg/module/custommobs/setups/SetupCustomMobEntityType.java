package net.marscraft.skyrpg.module.custommobs.setups;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.createinventory.InvSelectMobType;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

public class SetupCustomMobEntityType implements ISetup {

    private final ILogManager logger;
    private final DBHandlerCustomMobs dbHandler;
    private final DBAccessLayerCustomMobs sql;
    private static Map<UUID, IGuiInventory> guiInvs = new HashMap<>();
    private MessagesCustomMobs messages;
    private int mobId;
    private MobHostile hostileMob;

    public SetupCustomMobEntityType(ILogManager logger, DBHandlerCustomMobs dbHandler, DBAccessLayerCustomMobs sql, MessagesCustomMobs messages, MobHostile hostileMob) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.messages = messages;
        this.hostileMob = hostileMob;
        this.mobId = this.hostileMob.getId();
    }


    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) handleInventoryClickEvent(eventStorage, invClickEvent);


        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {

        if(guiInvs.containsKey(player.getUniqueId())) return;
        IGuiInventory inv = new InvSelectMobType(logger, messages, hostileMob);
        inv.open(player);
        guiInvs.put(player.getUniqueId(), inv);
    }

    @Override
    public boolean finishSetup() {
        if(!setupComplete()) return false;
        return sql.updateCustomMobType(hostileMob.getId(), hostileMob.getType().toString());
    }

    @Override
    public boolean setupComplete() {
        return hostileMob.getType() != null;
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!guiInvs.containsKey(player.getUniqueId())) return;
        IGuiInventory inv = guiInvs.get(player.getUniqueId());
        hostileMob = inv.handleEvents(eventStorage);
        if(hostileMob == null) {
            player.sendMessage("Mob ist Null");
            return;
        }
        if(!finishSetup()) {
            messages.sendEntityTypeCouldNotBeSet();
            return;
        }
        guiInvs.remove(player.getUniqueId());
        ModuleCustomMobs.removeSetup(player.getUniqueId());
    }

}
