package net.marscraft.skyrpg.module.custommobs.inventory.editinventory;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.inventory.invfunctions.InvFunctionShowMobOverview;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvEditOverview extends MarsInventory implements IGuiInventory {

    private final String title = "MobType auswählen";
    private final ILogManager logger;
    private MessagesCustomMobs messages;
    private DBHandlerCustomMobs dbHandler;
    private boolean active;

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
        return inv;
    }

    @Override
    public void handleEvents(EventStorage eventStorage) {

    }

    @Override
    public String getTitle() {
        return title;
    }

    private ItemStack getCustomMobDisplayItem(String mobName, int mobId, double maxHealth, double spawnChance) {
        String mobMatName = mobName + "_spawn_egg";
        Material mobTypeMat = Material.valueOf(mobMatName.toUpperCase());
        return new ItemBuilder(mobTypeMat).setDisplayname("§c" + mobId + ". " + mobName).setLore("§aMaxHealth: " + maxHealth, "§aSpawnChance: " + spawnChance).build();

    }

}
