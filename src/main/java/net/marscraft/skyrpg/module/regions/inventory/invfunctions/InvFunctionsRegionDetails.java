package net.marscraft.skyrpg.module.regions.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionsRegionDetails extends InvFunction {
    private ILogManager logger;
    private int startRow;
    private int endRow;
    private Region region;

    public InvFunctionsRegionDetails(ILogManager logger, int startRow, int endRow, Region region) {
        super(logger);
        this.logger = logger;
        this.startRow = startRow;
        this.endRow = endRow;
        this.region = region;
    }

    public Inventory add(Inventory inv) {
        int startIndex = getStartIndex(startRow);
        ItemStack[] invContents = inv.getContents();
        List<ItemStack> items = buildDetailItems();

        invContents[startIndex] = items.get(0);
        invContents[startIndex + 1] = items.get(1);

        inv.setContents(invContents);
        return inv;
    }

    private List<ItemStack> buildDetailItems() {
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        List<ItemStack> items = new ArrayList<>();

        ItemStack changeName = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange Region Name")
                .setLore("§aName: §c" + region.getName())
                .addPersistantDataToItemStack(keyDetailItem, "regionName")
                .build()
                ;
        ItemStack regionType = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cSet Region Type")
                .addPersistantDataToItemStack(keyDetailItem, "regionType")
                .build()
                ;

        items.add(changeName);
        items.add(regionType);
        return items;
    }

}
