package net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionsMobSpawnRegionDetails extends InvFunction {
    private ILogManager logger;
    private MobSpawnRegion mobSpawnRegion;

    public InvFunctionsMobSpawnRegionDetails(ILogManager logger, MobSpawnRegion mobSpawnRegion) {
        super(logger);
        this.logger = logger;
        this.mobSpawnRegion = mobSpawnRegion;
    }

    public Inventory add(Inventory inv, int startRow) {
        int startIndex = getStartIndex(startRow);
        ItemStack[] invContents = inv.getContents();
        List<ItemStack> items = buildDetailItems();

        invContents[startIndex] = items.get(0);
        invContents[startIndex + 1] = items.get(1);
        invContents[startIndex + 2] = items.get(2);

        inv.setContents(invContents);
        return inv;
    }

    private List<ItemStack> buildDetailItems() {
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        List<ItemStack> items = new ArrayList<>();

        ItemStack changeMaxMobs = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange MaxMobs")
                .setLore("§aMaxMobs: §c" + mobSpawnRegion.getMaxMobs())
                .addPersistantDataToItemStack(keyDetailItem, "maxMobs")
                .build()
                ;
        ItemStack spawningMobs = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cSpawning Mobs")
                .addPersistantDataToItemStack(keyDetailItem, "spawningMobs")
                .build()
                ;
        String loreState = mobSpawnRegion.isActive() ? "§aActive" : "§cInactive";
        ItemStack changeState = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange State")
                .setLore(loreState)
                .addPersistantDataToItemStack(keyDetailItem, "state")
                .build()
                ;

        items.add(changeMaxMobs);
        items.add(spawningMobs);
        items.add(changeState);
        return items;
    }

}
