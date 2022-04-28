package net.marscraft.skyrpg.module.custommobs.inventory.editinventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.invfunctions.InvFunctionShowMobOverview;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGoBack;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InvEditOverview extends MarsInventory implements IGuiInventory {

    public static final String title = "MobType auswählen";
    private static Map<UUID, InvFunctionGoBack> previousInvs = new HashMap<>();
    private final ILogManager logger;
    private MessagesCustomMobs messages;
    private DBHandlerCustomMobs dbHandler;
    private boolean active;

    public InvEditOverview(ILogManager logger, MessagesCustomMobs messages, DBHandlerCustomMobs dbHandler) {
        super(logger);
        this.logger = logger;
        this.messages = messages;
        this.dbHandler = dbHandler;
    }

    public InvEditOverview(ILogManager logger, MessagesCustomMobs messages, DBHandlerCustomMobs dbHandler, boolean active) {
        super(logger);
        this.logger = logger;
        this.messages = messages;
        this.dbHandler = dbHandler;
        this.active = active;
    }

    @Override
    public Inventory build() {

        Inventory inv = buildBaseInventory(title, 6);
        InvFunctionShowMobOverview mobOverview = new InvFunctionShowMobOverview(logger, dbHandler, 0, 44);
        Inventory newInv = mobOverview.add(inv, active);
        return newInv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        player.openInventory(inv);
        previousInvs.put(player.getUniqueId(), new InvFunctionGoBack(logger));
        return inv;
    }

    @Override
    public void handleEvents(EventStorage eventStorage) {
        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) handleInvClickEvent(eventStorage, invClickEvent);

    }

    @Override
    public String getTitle() {
        return title;
    }

    private void handleInvClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        InvFunctionGoBack invFunctionGoBack = previousInvs.get(player.getUniqueId());

        event.setCancelled(true);
        if(clickedItem == null) return;

        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyMobId, PersistentDataType.INTEGER)) {
            int mobId = clickedItem.getItemMeta().getPersistentDataContainer().get(keyMobId, PersistentDataType.INTEGER);
            player.sendMessage(mobId + "");
            invFunctionGoBack.addGuiInventory(this);
            IGuiInventory invMobDetails = new InvEditMobDetails(logger, dbHandler, messages, mobId, invFunctionGoBack);
            invMobDetails.open(player);
            return;
        }

        //Filter Item gets clicked
        //New instance from this class gets created. Method open gets executed
        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyFilterName, PersistentDataType.STRING)) {
            String filterName = clickedItem.getItemMeta().getPersistentDataContainer().get(keyFilterName, PersistentDataType.STRING);
            boolean active = filterName.equalsIgnoreCase("Active") ? true : false;
            player.sendMessage("Filter Name: " + filterName);
            player.sendMessage("Filter Active: " + active);
            IGuiInventory guiInv = getInventory(event.getView().getTitle(), active);
            guiInv.open(player);
            return;
        }


    }

    private IGuiInventory getInventory(String invTitle, boolean active) {

        InvEditOverview invEditOverview = new InvEditOverview(logger, messages, dbHandler, active);

        if (invEditOverview.getTitle().equals(invTitle)) return invEditOverview;
        return null;

    }

    public static Map<UUID, InvFunctionGoBack> getPreviousInvs() {
        return previousInvs;
    }

    public static void addPreviousInvFunction(InvFunctionGoBack previousInvFunction, UUID uuid) {
        previousInvs.put(uuid, previousInvFunction);
    }

}
