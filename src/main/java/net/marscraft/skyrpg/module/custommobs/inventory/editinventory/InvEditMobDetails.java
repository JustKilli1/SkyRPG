package net.marscraft.skyrpg.module.custommobs.inventory.editinventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.createinventory.InvCreateMob;
import net.marscraft.skyrpg.module.custommobs.inventory.invfunctions.InvFunctionMobDetails;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.custommobs.setups.SetupCustomMobName;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGoBack;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InvEditMobDetails extends MarsInventory implements IGuiInventory {

    public final static String title = "Mob Details";
    private final ILogManager logger;
    private DBHandlerCustomMobs dbHandler;
    private MessagesCustomMobs messages;
    private int mobId;
    private MobHostile hostileMob;
    private InvFunctionGoBack invFunctionGoBack;
    private DBAccessLayerCustomMobs sql;

    public InvEditMobDetails(ILogManager logger, DBHandlerCustomMobs dbHandler, MessagesCustomMobs messages, DBAccessLayerCustomMobs sql) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.messages = messages;
        this.sql = sql;
    }

    public InvEditMobDetails(ILogManager logger, DBHandlerCustomMobs dbHandler, MessagesCustomMobs messages, int mobId, InvFunctionGoBack invFunctionGoBack) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.messages = messages;
        this.mobId = mobId;
        this.invFunctionGoBack = invFunctionGoBack;
        hostileMob = getHostileMob(this.mobId);
    }

    @Override
    public Inventory build() {
        if(hostileMob == null) {
            logger.warn("Mob with id " + mobId + " could not be found.");
            return null;
        }
        Inventory inv = buildBaseInventory(title, 3);
        inv = invFunctionGoBack.add(inv, 3);
        InvFunctionMobDetails invFunctionMobDetails = new InvFunctionMobDetails(logger, hostileMob);
        inv = invFunctionMobDetails.add(inv, 1);

        return inv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        if(inv == null) return null;
        player.openInventory(inv);
        return inv;
    }

    @Override
    public void handleEvents(EventStorage eventStorage) {
        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) handleInventoryClickEvent(eventStorage, invClickEvent);

    }

    @Override
    public String getTitle() {
        return null;
    }


    private MobHostile getHostileMob(int mobId) {
        return dbHandler.getHostileMobById(mobId);
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        invFunctionGoBack = InvEditOverview.getPreviousInvs().get(player.getUniqueId());
        event.setCancelled(true);

        if(clickedItem == null) return;
        ItemStack mobOverview = event.getInventory().getItem(0);
        if(mobOverview.getItemMeta().getPersistentDataContainer().has(keyMobId, PersistentDataType.INTEGER)) {
            mobId = mobOverview.getItemMeta().getPersistentDataContainer().get(keyMobId, PersistentDataType.INTEGER);
            hostileMob = getHostileMob(mobId);
        }

        //Go Back got clicked
        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            if(!InvEditOverview.getPreviousInvs().containsKey(player.getUniqueId())) return;
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            previousInv.open(player);
            return;
        }

        //Change Field Item got clicked
        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyDetailItem, PersistentDataType.STRING)) {
            String detailItemName = clickedItem.getItemMeta().getPersistentDataContainer().get(keyDetailItem, PersistentDataType.STRING);
            switch (detailItemName.toLowerCase()) {
                case "name":
                    ISetup nameSetup = new SetupCustomMobName(logger, dbHandler, sql, messages, mobId);
                    ModuleCustomMobs.addSetup(player.getUniqueId(), nameSetup);
                    messages.sendEnterNewNameMessage();
                    player.closeInventory();
                    break;
                case "mobtype":
                    IGuiInventory inv = new InvCreateMob(logger, messages, hostileMob);
                    inv.open(player);
                    player.sendMessage("Test2");
                    break;
                default:
                    break;
            }

            return;
        }
    }

}
