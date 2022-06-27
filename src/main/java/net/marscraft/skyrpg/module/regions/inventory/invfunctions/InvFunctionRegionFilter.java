package net.marscraft.skyrpg.module.regions.inventory.invfunctions;

import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvFunctionRegionFilter extends InvFunction {


    private ILogManager logger;
    private int startRow;

    public InvFunctionRegionFilter(ILogManager logger, int startRow) {
        super(logger);
        this.logger = logger;
        this.startRow = startRow;
    }

    public Inventory add(Inventory inv) {

        ItemStack[] invContents = inv.getContents();
        ItemStack regionFilter = buildFilterItem("Regions", "regions", true, "§aZeigt alle Regionen an");
        ItemStack mobSpawnRegionFilter = buildFilterItem("MobSpawnRegions", "mobSpawnRegions", false, "§aZeigt alle MobSpawn Regionen an");
        int startIndex = getStartIndex(startRow);
        invContents[startIndex] = regionFilter;
        invContents[startIndex + 1] = mobSpawnRegionFilter;

        inv.setContents(invContents);
        return inv;
    }



}
