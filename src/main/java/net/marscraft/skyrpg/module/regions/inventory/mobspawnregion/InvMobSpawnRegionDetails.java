package net.marscraft.skyrpg.module.regions.inventory.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion.InvFunctionsMobSpawnRegionDetails;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.module.regions.setups.mobspawnregion.SetupChangeMSRMaxMobs;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunctionGoBack;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InvMobSpawnRegionDetails extends MarsInventory implements IGuiInventory {


    public final static String title = "Region Details";
    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private InvFunctionGoBack invFunctionGoBack;
    private MobSpawnRegion mobSpawnRegion;
    private Inventory inv;
    private MessagesRegions messages;

    public InvMobSpawnRegionDetails(ILogManager logger, DBHandlerRegions dbHandler, DBAccessLayerRegions sql, InvFunctionGoBack invFunctionGoBack, MobSpawnRegion mobSpawnRegion, MessagesRegions messages) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.invFunctionGoBack = invFunctionGoBack;
        this.mobSpawnRegion = mobSpawnRegion;
        this.messages = messages;
    }

    @Override
    public Inventory build() {
        inv = buildBaseInventory(title, 3);
        ItemStack regionDisplay = new ItemBuilder(Material.MAP).setDisplayname(mobSpawnRegion.getRegion().getName()).build();
        inv.setItem(0, regionDisplay);
        inv = invFunctionGoBack.add(inv, 3);
        InvFunctionsMobSpawnRegionDetails invFunctionsMobSpawnRegionDetails = new InvFunctionsMobSpawnRegionDetails(logger, mobSpawnRegion);
        inv = invFunctionsMobSpawnRegionDetails.add(inv, 2);

        return inv;
    }

    @Override
    public Inventory open(Player player) {
        inv = build();
        player.openInventory(inv);
        ModuleRegions.addInv(player.getUniqueId(), this);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        if(event != null) handleInventoryClickEvent(eventStorage, event);

        return null;
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");

        if(itemMeta.getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            player.closeInventory();
            previousInv.open(player);
            return;
        } else if(itemMeta.getPersistentDataContainer().has(keyDetailItem, PersistentDataType.STRING)) {
            String detailItemName = itemMeta.getPersistentDataContainer().get(keyDetailItem, PersistentDataType.STRING);
            switch (detailItemName.toLowerCase()) {
                case "maxmobs":
                    player.closeInventory();
                    ISetup maxMobsSetup = new SetupChangeMSRMaxMobs(logger, mobSpawnRegion, messages, dbHandler, sql);
                    ModuleRegions.addSetup(player.getUniqueId(), maxMobsSetup);
                    maxMobsSetup.handleCommands(player);
                    break;
                case "spawningMobs":
                    /*invFunctionGoBack.addGuiInventory(this);
                    player.closeInventory();
                    IGuiInventory regionTypeInv = new InvSelectRegionType(logger, dbHandler, sql, invFunctionGoBack, region, messages);
                    regionTypeInv.open(player);*/
                    break;
                case "state":
                    mobSpawnRegion.setActive(!mobSpawnRegion.isActive());
                    sql.updateMobSpawnRegion(mobSpawnRegion);
                    player.closeInventory();
                    open(player);
                    break;
            }
        }


    }

    @Override
    public String getTitle() {
        return title;
    }

}
