package net.marscraft.skyrpg.module.regions.inventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionRegionTypes;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionsRegionDetails;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.module.regions.setups.SetupChangeMSRMaxMobs;
import net.marscraft.skyrpg.module.regions.setups.SetupChangeRegionName;
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

public class InvSelectRegionType extends MarsInventory implements IGuiInventory {

    public final static String title = "Region Details";
    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    private InvFunctionGoBack invFunctionGoBack;
    private Region region;
    private Inventory inv;
    private MessagesRegions messages;

    public InvSelectRegionType(ILogManager logger, DBHandlerRegions dbHandler, DBAccessLayerRegions sql, InvFunctionGoBack invFunctionGoBack, Region region, MessagesRegions messages) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.sql = sql;
        this.invFunctionGoBack = invFunctionGoBack;
        this.region = region;
        this.messages = messages;
    }

    @Override
    public Inventory build() {
        inv = buildBaseInventory(title, 3);
        ItemStack regionDisplay = new ItemBuilder(Material.MAP).setDisplayname(region.getName()).build();
        inv.setItem(0, regionDisplay);
        inv = invFunctionGoBack.add(inv, 3);
        InvFunctionRegionTypes invFunctionRegionTypes = new InvFunctionRegionTypes(logger);
        inv = invFunctionRegionTypes.add(inv, 2);

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
        NamespacedKey keyRegionType = new NamespacedKey(Main.getPlugin(Main.class), "regionType");

        if(itemMeta.getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            player.closeInventory();
            previousInv.open(player);
            return;
        } else if(itemMeta.getPersistentDataContainer().has(keyRegionType, PersistentDataType.STRING)) {
            String regionTypeName = itemMeta.getPersistentDataContainer().get(keyRegionType, PersistentDataType.STRING);
            switch (regionTypeName.toLowerCase()) {
                case "mobspawnregion":
                    ISetup typeSetup = new SetupChangeMSRMaxMobs(logger, region, messages, dbHandler, sql);
                    ModuleRegions.addSetup(player.getUniqueId(), typeSetup);
                    typeSetup.handleCommands(player);
                    break;
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }
}
