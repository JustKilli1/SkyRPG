package net.marscraft.skyrpg.module.custommobs.inventory.editinventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.invfunctions.InvFunctionMobDetails;
import net.marscraft.skyrpg.module.custommobs.inventory.invfunctions.InvFunctionMobItems;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

public class InvEditItems extends MarsInventory implements IGuiInventory {

    public final static String title = "Mob Items";
    private final ILogManager logger;
    private DBHandlerCustomMobs dbHandler;
    private MessagesCustomMobs messages;
    private MobHostile hostileMob;
    private InvFunctionGoBack invFunctionGoBack;
    private DBAccessLayerCustomMobs sql;
    private InvFunctionMobItems mobItems;

    public InvEditItems(ILogManager logger, DBHandlerCustomMobs dbHandler, MessagesCustomMobs messages, DBAccessLayerCustomMobs sql) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.messages = messages;
        this.sql = sql;
    }

    public InvEditItems(ILogManager logger, DBHandlerCustomMobs dbHandler, MessagesCustomMobs messages, MobHostile hostileMob, InvFunctionGoBack invFunctionGoBack) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.messages = messages;
        this.invFunctionGoBack = invFunctionGoBack;
        this.hostileMob = hostileMob;
    }

    @Override
    public Inventory build() {
        Inventory inv = buildBaseInventory(title, 6);
        inv = invFunctionGoBack.add(inv, 6);
        mobItems = new InvFunctionMobItems(logger, hostileMob);
        inv = mobItems.add(inv, 2);

        return inv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        player.openInventory(inv);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent invClickEvent = eventStorage.getInventoryClickEvent();
        if(invClickEvent != null) handleInvClickEvent(eventStorage, invClickEvent);
        return (T) hostileMob;
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void handleInvClickEvent(EventStorage eventStorage, InventoryClickEvent event) {
        NamespacedKey keyMobItem = new NamespacedKey(Main.getPlugin(Main.class), "mobItem");
        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");
        Inventory inv = event.getInventory();
        ItemStack[] invContents = inv.getContents();
        Player player = (Player) event.getWhoClicked();
        PlayerInventory playerInv = player.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;

        //Go Back got clicked
        if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            if(!InvEditOverview.getPreviousInvs().containsKey(player.getUniqueId())) return;
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            previousInv.open(player);
            return;
        }
        ItemStack[] armor = hostileMob.getArmor();
       if(clickedItem.getItemMeta().getPersistentDataContainer().has(keyMobItem, PersistentDataType.STRING)) {
           int armorIndex = Utils.getItemIndexInInv(armor, clickedItem);
           if(armorIndex >= 0) armor[armorIndex] = null;
           else hostileMob.setMainItem(null);
           inv.remove(clickedItem);
           clickedItem = Utils.removePersistantDataFromItem(clickedItem, keyMobItem, PersistentDataType.STRING);
           player.getInventory().addItem(clickedItem);
           return;
       } else {
           String materialStr = clickedItem.getType().toString();


           if(materialStr.contains("HELMET")) {

               if(invContents[10] != null) playerInv.addItem(Utils.removePersistantDataFromItem(invContents[10], keyMobItem, PersistentDataType.STRING));
               invContents[10] = mobItems.buildMobItem(clickedItem, "helmet");
               armor[0] = mobItems.buildMobItem(clickedItem, "helmet");

           }
           else if(materialStr.contains("CHESTPLATE")) {
               if(invContents[19] != null) playerInv.addItem(Utils.removePersistantDataFromItem(invContents[19], keyMobItem, PersistentDataType.STRING));
               invContents[19] = mobItems.buildMobItem(clickedItem, "chestplate");
               armor[1] = mobItems.buildMobItem(clickedItem, "chestplate");
           }
           else if(materialStr.contains("LEGGINGS")) {
               if(invContents[28] != null) playerInv.addItem(Utils.removePersistantDataFromItem(invContents[28], keyMobItem, PersistentDataType.STRING));
               invContents[28] = mobItems.buildMobItem(clickedItem, "leggings");
               armor[2] = mobItems.buildMobItem(clickedItem, "leggings");
           }
           else if(materialStr.contains("BOOTS")) {
               if(invContents[37] != null) playerInv.addItem(Utils.removePersistantDataFromItem(invContents[37], keyMobItem, PersistentDataType.STRING));
               invContents[37] = mobItems.buildMobItem(clickedItem, "boots");
               armor[3] = mobItems.buildMobItem(clickedItem, "boots");
           }
           else {
               if(invContents[12] != null) playerInv.addItem(Utils.removePersistantDataFromItem(invContents[12], keyMobItem, PersistentDataType.STRING));
               invContents[12] = mobItems.buildMobItem(clickedItem, "mainItem");
               hostileMob.setMainItem(mobItems.buildMobItem(clickedItem, "mainItem"));
           }
           playerInv.remove(clickedItem);

       }
       event.setCancelled(true);
       inv.setContents(invContents);
       hostileMob.setArmor(armor);

    }
}
