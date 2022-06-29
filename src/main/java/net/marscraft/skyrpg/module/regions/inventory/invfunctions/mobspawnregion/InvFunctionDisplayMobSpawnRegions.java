package net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InvFunctionDisplayMobSpawnRegions extends InvFunction {

    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private int startRow, endRow;

    public InvFunctionDisplayMobSpawnRegions(ILogManager logger, DBHandlerRegions dbHandler, int startRow, int endRow) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.startRow = startRow;
        this.endRow = endRow;
    }


    public Inventory add(Inventory inv) {
        List<MobSpawnRegion> mobSpawnRegions = dbHandler.getAllMobSpawnRegions();
        ItemStack[] invContents = inv.getContents();
        int startIndex = getStartIndex(startRow);
        int endIndex = getEndIndex(endRow);
        int x = 0;

        for(int i = startIndex; i <= endIndex; i++, x++) {
            if(x >= mobSpawnRegions.size()) break;
            invContents[i] = getRegionDisplayItem(mobSpawnRegions.get(x));
        }
        inv.setContents(invContents);
        return inv;
    }

    private ItemStack getRegionDisplayItem(MobSpawnRegion mobSpawnRegion) {
        NamespacedKey mobSpawnRegionId = new NamespacedKey(Main.getPlugin(Main.class), "mobSpawnRegionId");
        NamespacedKey regionId = new NamespacedKey(Main.getPlugin(Main.class), "regionId");
        ItemStack displayItem = new ItemBuilder(Material.MAP)
                .setDisplayname("§c" + mobSpawnRegion.getRegion().getName())
                .setLore(
                        "§aMaxMobs: §c" + mobSpawnRegion.getMaxMobs(),
                        " ",
                        "§cRegion",
                        "§aID: §c" + mobSpawnRegion.getRegion().getId(),
                        "§aWelt: §c" + mobSpawnRegion.getRegion().getBound().getLoc1().getWorld().getName(),
                        "§aLoc1",
                        "§aX: §c" + mobSpawnRegion.getRegion().getBound().getLoc1().getBlockX(),
                        "§aZ: §c" + mobSpawnRegion.getRegion().getBound().getLoc1().getBlockZ(),
                        " ",
                        "§aLoc2",
                        "§aX: §c" + mobSpawnRegion.getRegion().getBound().getLoc2().getBlockX(),
                        "§aZ: §c" + mobSpawnRegion.getRegion().getBound().getLoc2().getBlockZ(),
                        " ",
                        "§aRechtsklick zum teleportieren"
                )
                .addPersistantDataToItemStack(mobSpawnRegionId, mobSpawnRegion.getId())
                .addPersistantDataToItemStack(regionId, mobSpawnRegion.getRegion().getId())
                .build()
                ;
        return displayItem;
    }



}
