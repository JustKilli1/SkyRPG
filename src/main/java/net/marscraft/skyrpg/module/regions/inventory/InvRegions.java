package net.marscraft.skyrpg.module.regions.inventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionDisplayMobSpawnRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionDisplayRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionRegionFilter;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InvRegions extends MarsInventory implements IGuiInventory {

    public final static String title = "Regions";
    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private InvFunctionRegionFilter invFunctionRegionFilter;
    private Inventory inv;

    public InvRegions(ILogManager logger, DBHandlerRegions dbHandler) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    @Override
    public Inventory build() {
        inv = buildBaseInventory(title, 6);
        invFunctionRegionFilter = new InvFunctionRegionFilter(logger, 1);
        InvFunctionDisplayRegions invFunctionDisplayRegions = new InvFunctionDisplayRegions(logger, dbHandler, 2, 6);
        inv = invFunctionRegionFilter.add(inv, null);
        inv = invFunctionDisplayRegions.add(inv);

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

        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        if(event != null) handleInventoryClickEvent(eventStorage, event);


        return null;
    }

    private void handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        NamespacedKey keyFilterActive = new NamespacedKey(Main.getPlugin(Main.class), "filterActive");
        NamespacedKey regionId = new NamespacedKey(Main.getPlugin(Main.class), "regionId");
        NamespacedKey mobSpawnRegionId = new NamespacedKey(Main.getPlugin(Main.class), "mobSpawnRegionId");

        event.setCancelled(true);
        
        if(itemMeta.getPersistentDataContainer().has(keyFilterName, PersistentDataType.STRING)
                && !(itemMeta.getPersistentDataContainer().has(keyFilterActive, PersistentDataType.STRING))) {
            String filterName = itemMeta.getPersistentDataContainer().get(keyFilterName, PersistentDataType.STRING);
            inv = invFunctionRegionFilter.add(inv, item);
            changeDisplayedRegions(filterName);
            return;
        }

        if(event.getClick() == ClickType.RIGHT) {
            if(itemMeta.getPersistentDataContainer().has(regionId, PersistentDataType.INTEGER)) {
                int baseRegionId = itemMeta.getPersistentDataContainer().get(regionId, PersistentDataType.INTEGER);
                Region region = dbHandler.getRegion(baseRegionId);
                Location loc1 = region.getBound().getLoc1();
                player.teleport(loc1.getWorld().getHighestBlockAt(loc1).getLocation().add(0, 1, 0));
            }
        }



    }

    private void changeDisplayedRegions(String regionName) {
        inv = resetInvContents(inv, 2, 6);
        switch (regionName.toLowerCase()) {
            case "regions":

                InvFunctionDisplayRegions invFunctionDisplayRegions = new InvFunctionDisplayRegions(logger, dbHandler, 2, 6);
                inv = invFunctionDisplayRegions.add(inv);
                break;
            case "mobspawnregions":
                InvFunctionDisplayMobSpawnRegions invFunctionDisplayMobSpawnRegions = new InvFunctionDisplayMobSpawnRegions(logger, dbHandler, 2, 6);
                inv = invFunctionDisplayMobSpawnRegions.add(inv);
                break;
        }

    }


    @Override
    public String getTitle() {
        return title;
    }
}
