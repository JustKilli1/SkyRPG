package net.marscraft.skyrpg.module.regions.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InvFunctionDisplayRegions extends InvFunction {

    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private int startRow, endRow;

    public InvFunctionDisplayRegions(ILogManager logger, DBHandlerRegions dbHandler, int startRow, int endRow) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    public Inventory add(Inventory inv) {
        List<Region> regions = dbHandler.getAllRegions();
        ItemStack[] invContents = inv.getContents();
        int startIndex = getStartIndex(startRow);
        int endIndex = getEndIndex(endRow);
        int x = 0;

        for(int i = startIndex; i <= endIndex; i++, x++) {
            if(x >= regions.size()) break;
            invContents[i] = getRegionDisplayItem(regions.get(x));
        }
        inv.setContents(invContents);
        logger.error("Geklappt");
        return inv;
    }

    private ItemStack getRegionDisplayItem(Region region) {
        NamespacedKey regionId = new NamespacedKey(Main.getPlugin(Main.class), "regionId");
        ItemStack displayItem = new ItemBuilder(Material.MAP)
                .setDisplayname("§c" + region.getName())
                .setLore(
                        "§aWelt: §c" + region.getBound().getLoc1().getWorld().getName(),
                        "§aLoc1",
                        "§aX: §c" + region.getBound().getLoc1().getBlockX(),
                        "§aZ: §c" + region.getBound().getLoc1().getBlockZ(),
                        " ",
                        "§aLoc2",
                        "§aX: §c" + region.getBound().getLoc2().getBlockX(),
                        "§aZ: §c" + region.getBound().getLoc2().getBlockZ(),
                        " ",
                        "§aRechtsklick zum teleportieren"
                )
                .addPersistantDataToItemStack(regionId, region.getId())
                .build()
                ;
        return displayItem;
    }



}
