package net.marscraft.skyrpg.module.regions.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InvFunctionRegionFilter extends InvFunction {


    private ILogManager logger;
    private int startRow;

    public InvFunctionRegionFilter(ILogManager logger, int startRow) {
        super(logger);
        this.logger = logger;
        this.startRow = startRow;
    }

    public Inventory add(@NotNull Inventory inv, @Nullable ItemStack filterActive) {
        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        String activeFilterName = null;
        if(filterActive != null) activeFilterName = filterActive.getItemMeta().getPersistentDataContainer().get(keyFilterName, PersistentDataType.STRING);

        ItemStack regionFilter, mobSpawnRegionFilter;

        ItemStack[] invContents = inv.getContents();
        if(activeFilterName == null) {
            regionFilter = buildFilterItem("Regions", "regions", true, "§aZeigt alle Regionen an");
            mobSpawnRegionFilter = buildFilterItem("MobSpawnRegions", "mobSpawnRegions", false, "§aZeigt alle MobSpawn Regionen an");
        } else {
            boolean active = activeFilterName.equalsIgnoreCase("regions");
            regionFilter = buildFilterItem("Regions", "regions", active, "§aZeigt alle Regionen an");
            active = activeFilterName.equalsIgnoreCase("mobSpawnRegions");
            mobSpawnRegionFilter = buildFilterItem("MobSpawnRegions", "mobSpawnRegions", active, "§aZeigt alle MobSpawn Regionen an");
        }
        int startIndex = getStartIndex(startRow);
        invContents[startIndex] = regionFilter;
        invContents[startIndex + 1] = mobSpawnRegionFilter;

        inv.setContents(invContents);
        return inv;
    }

}
