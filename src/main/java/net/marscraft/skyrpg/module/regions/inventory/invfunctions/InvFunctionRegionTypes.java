package net.marscraft.skyrpg.module.regions.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionRegionTypes extends InvFunction {

    private final ILogManager logger;

    public InvFunctionRegionTypes(ILogManager logger) {
        super(logger);
        this.logger = logger;
    }

    public Inventory add(Inventory inv, int startRow) {
        ItemStack[] invContents = inv.getContents();
        List<ItemStack> items = buildRegionTypeItems();
        int startIndex = getStartIndex(startRow);

        invContents[startIndex] = items.get(0);

        inv.setContents(invContents);
        return inv;
    }

    private List<ItemStack> buildRegionTypeItems() {
        List<ItemStack> items = new ArrayList<>();
        NamespacedKey keyRegionType = new NamespacedKey(Main.getPlugin(Main.class), "regionType");

        ItemStack mobSpawnRegion = new ItemBuilder(Material.ZOMBIE_SPAWN_EGG)
                .setDisplayname("§cMobSpawnRegion")
                .setLore("§aErstellt eine neue MobSpawnRegion")
                .addPersistantDataToItemStack(keyRegionType, "mobSpawnRegion")
                .build();

        items.add(mobSpawnRegion);
        return items;
    }

}
