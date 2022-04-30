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

    protected ItemStack getCustomMobDisplayItem(String mobType, String mobName, int mobId, double maxHealth, double spawnChance) {
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        String mobMatName = mobType + "_spawn_egg";
        Material mobTypeMat = Material.valueOf(mobMatName.toUpperCase());
        return new ItemBuilder(mobTypeMat)
                    .setDisplayname("§c" + mobId + ". " + mobName)
                    .setLore("§aBaseHealth: " + maxHealth, "§aSpawnChance: " + spawnChance)
                    .addPersistantDataToItemStack(keyMobId, mobId)
                    .build()
                    ;
    }

    protected int getStartIndex(int row) { return (row * 9) - 9; }

    protected int getEndIndex(int row) { return (row * 9) - 1; }
}
