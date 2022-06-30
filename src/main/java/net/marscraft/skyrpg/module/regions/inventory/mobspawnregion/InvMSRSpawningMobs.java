package net.marscraft.skyrpg.module.regions.inventory.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion.InvFunctionDisplayMobs;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.module.regions.setups.mobspawnregion.SetupAddNewMob;
import net.marscraft.skyrpg.module.regions.setups.mobspawnregion.SetupChangeMobSpawnChance;
import net.marscraft.skyrpg.module.regions.setups.mobspawnregion.SetupMobLevel;
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

public class InvMSRSpawningMobs extends MarsInventory implements IGuiInventory {



    public final static String title = "Spawning Mobs";
    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private InvFunctionGoBack invFunctionGoBack;
    private MobSpawnRegion mobSpawnRegion;
    private Inventory inv;
    private MessagesRegions messages;
    private DBHandlerCustomMobs dbHandlerCustomMobs;

    public InvMSRSpawningMobs(ILogManager logger, DBHandlerRegions dbHandler, DBAccessLayerRegions sql, InvFunctionGoBack invFunctionGoBack, MobSpawnRegion mobSpawnRegion, MessagesRegions messages, DBHandlerCustomMobs dbHandlerCustomMobs) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.invFunctionGoBack = invFunctionGoBack;
        this.mobSpawnRegion = mobSpawnRegion;
        this.messages = messages;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
    }

    @Override
    public Inventory build() {
        inv = buildBaseInventory(title, 6);
        ItemStack regionDisplay = new ItemBuilder(Material.MAP).setDisplayname(mobSpawnRegion.getRegion().getName()).build();
        inv.setItem(0, regionDisplay);
        inv = invFunctionGoBack.add(inv, 6);
        InvFunctionDisplayMobs invFunctionDisplayMobs = new InvFunctionDisplayMobs(logger, dbHandler, mobSpawnRegion);
        inv = invFunctionDisplayMobs.add(inv);

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

    @Override
    public String getTitle() {
        return title;
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        event.setCancelled(true);
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        NamespacedKey keyControllerId = new NamespacedKey(Main.getPlugin(Main.class), "controllerId");

        if (itemMeta.getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            player.closeInventory();
            previousInv.open(player);
        } else if (itemMeta.getPersistentDataContainer().has(keyDetailItem, PersistentDataType.STRING)) {
            String detailItemName = itemMeta.getPersistentDataContainer().get(keyDetailItem, PersistentDataType.STRING);
            int mobId = itemMeta.getPersistentDataContainer().get(keyMobId, PersistentDataType.INTEGER);
            MobCharacteristics mobCharacteristics = mobSpawnRegion.getMobCharacteristicsById(mobId);
            switch (detailItemName.toLowerCase()) {
                case "spawnchance":
                    player.closeInventory();
                    if (mobCharacteristics == null) return;
                    ISetup spawnChanceSetup = new SetupChangeMobSpawnChance(logger, mobCharacteristics, messages, dbHandler, sql);
                    ModuleRegions.addSetup(player.getUniqueId(), spawnChanceSetup);
                    spawnChanceSetup.handleCommands(player);
                    break;
                case "level":
                    player.closeInventory();
                    if (mobCharacteristics == null) return;
                    ISetup levelSetup = new SetupMobLevel(logger, mobCharacteristics, messages, dbHandler, sql);
                    ModuleRegions.addSetup(player.getUniqueId(), levelSetup);
                    levelSetup.handleCommands(player);
                    break;
                case "delete":
                    player.closeInventory();
                    int controllerId = itemMeta.getPersistentDataContainer().get(keyControllerId, PersistentDataType.INTEGER);
                    sql.deleteMob(controllerId);
                    open(player);
                    break;
                case "newmob":
                    invFunctionGoBack.addGuiInventory(this);
                    player.closeInventory();
                    SetupAddNewMob setupAddNewMob = new SetupAddNewMob(logger, mobSpawnRegion, dbHandlerCustomMobs, dbHandler, sql, invFunctionGoBack, messages);
                    setupAddNewMob.handleCommands(player);
                    ModuleRegions.addSetup(player.getUniqueId(), setupAddNewMob);
                    break;
            }
        }
    }

}
