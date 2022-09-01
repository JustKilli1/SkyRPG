package net.marscraft.skyrpg.module.regions.inventory.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion.InvFunctionShowCustomMobs;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InvAddMob extends MarsInventory implements IGuiInventory {

    public final static String title = "Add new Mob";
    private final ILogManager logger;
    private DBHandlerCustomMobs dbHandlerCustomMobs;
    private InvFunctionGoBack invFunctionGoBack;
    private Inventory inv;
    private MobHostile mobHostile;

    public InvAddMob(ILogManager logger, DBHandlerCustomMobs dbHandlerCustomMobs, InvFunctionGoBack invFunctionGoBack) {
        super(logger);
        this.logger = logger;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
        this.invFunctionGoBack = invFunctionGoBack;
    }

    @Override
    public Inventory build() {
        inv = buildBaseInventory(title, 6);
        inv = invFunctionGoBack.add(inv, 6);
        InvFunctionShowCustomMobs invFunctionShowCustomMobs = new InvFunctionShowCustomMobs(logger, dbHandlerCustomMobs);
        inv = invFunctionShowCustomMobs.add(inv, 1, 5);

        return inv;
    }

    @Override
    public Inventory open(Player player) {
        inv = build();
        player.openInventory(inv);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        if(event != null) return handleInventoryClickEvent(eventStorage, event);

        return null;
    }

    private <T> T handleInventoryClickEvent(EventStorage eventStorage, InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(item == null) return null;
        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        NamespacedKey keyGoBack = new NamespacedKey(Main.getPlugin(Main.class), "goBackItem");

        if (itemMeta.getPersistentDataContainer().has(keyGoBack, PersistentDataType.INTEGER)) {
            IGuiInventory previousInv = invFunctionGoBack.getPreviousInventory();
            player.closeInventory();
            previousInv.open(player);
            if(ModuleRegions.getSetups().containsKey(player.getUniqueId())) ModuleRegions.removeSetup(player.getUniqueId());
            return null;
        }

        if(itemMeta.getPersistentDataContainer().has(keyMobId, PersistentDataType.INTEGER)) {
            int mobId = itemMeta.getPersistentDataContainer().get(keyMobId, PersistentDataType.INTEGER);
            mobHostile = dbHandlerCustomMobs.getHostileMobById(mobId);
            return (T) mobHostile;
        }
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
