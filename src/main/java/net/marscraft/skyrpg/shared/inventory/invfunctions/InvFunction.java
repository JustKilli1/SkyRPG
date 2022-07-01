package net.marscraft.skyrpg.shared.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class InvFunction {

    private final ILogManager logger;

    public InvFunction(ILogManager logger) {
        this.logger = logger;
    }

    public ItemStack getCustomMobDisplayItem(String mobType, String mobName, int mobId, double maxHealth, double spawnChance) {
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        String mobMatName = mobType + "_spawn_egg";
        Material mobTypeMat = Material.valueOf(mobMatName.toUpperCase());
        return new ItemBuilder(mobTypeMat)
                    .setDisplayname("§c" + mobId + ". " + mobName)
                    .setLore("§aBaseHealth: " + maxHealth)
                    .addPersistantDataToItemStack(keyMobId, mobId)
                    .build()
                    ;
    }

    public int getStartIndex(int row) { return (row * 9) - 9; }

    public int getEndIndex(int row) { return (row * 9) - 1; }

    public ItemStack buildFilterItem(String displayName, String filterName, boolean filterActive, String... lore) {
        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        NamespacedKey keyFilterActive = new NamespacedKey(Main.getPlugin(Main.class), "filterActive");

        ItemBuilder filterItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                .setDisplayname(displayName)
                .setLore(lore)
                .addPersistantDataToItemStack(keyFilterName, filterName);

        if(filterActive) {
            filterItem.setMaterial(Material.GREEN_STAINED_GLASS_PANE)
                    .addPersistantDataToItemStack(keyFilterActive, "Active")
            ;
        }
        return filterItem.build();
    }

}
