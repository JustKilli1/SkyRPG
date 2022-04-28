package net.marscraft.skyrpg.module.custommobs.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionMobDetails extends InvFunction {

    private final ILogManager logger;
    private MobHostile hostileMob;

    public InvFunctionMobDetails(ILogManager logger, MobHostile hostileMob) {
        super(logger);
        this.logger = logger;
        this.hostileMob = hostileMob;
    }

    public Inventory add(Inventory inv, int startRow) {
        int startIndex = getStartIndex(startRow);
        ItemStack[] invContents = inv.getContents();
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");

        ItemStack mobItem = new ItemBuilder(getCustomMobDisplayItem(hostileMob.getType().toString(), hostileMob.getName(), hostileMob.getId(), hostileMob.getBaseHealth(), hostileMob.getSpawnChance()))
                                .addPersistantDataToItemStack(keyDetailItem, "mobType")
                                .build()
                                ;
        invContents[startIndex] = mobItem;

        List<ItemStack> detailItems = buildDetailItems();
        int listCounter = 0;
        //startIndex + 9 --> To start in second Row
        for(int i = startIndex + 9; i < invContents.length && listCounter < detailItems.size(); i++, listCounter++) {
            invContents[i] = detailItems.get(listCounter);
        }

        inv.setContents(invContents);
        return inv;
    }

    private List<ItemStack> buildDetailItems() {
        List<ItemStack> items = new ArrayList<>();
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");

        ItemStack changeName = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange Name")
                .setLore("§aName: §c" + hostileMob.getName())
                .addPersistantDataToItemStack(keyDetailItem, "name")
                .build()
                ;
        ItemStack changeBaseHealth = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange Base Health")
                .setLore("§aBase Health: §c" + hostileMob.getBaseHealth())
                .addPersistantDataToItemStack(keyDetailItem, "baseHealth")
                .build()
                ;
        ItemStack changeSpawnChance = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange Spawn Chance")
                .setLore("§aSpawn Chance: §c" + hostileMob.getSpawnChance())
                .addPersistantDataToItemStack(keyDetailItem, "spawnChance")
                .build()
                ;
        ItemStack changeItems = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cItems")
                .setLore("§aOpens Mob Inventory")
                .addPersistantDataToItemStack(keyDetailItem, "items")
                .build()
                ;
        ItemStack changeLoot = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cLoot")
                .setLore("§aOpens Mob Loot Inventory")
                .addPersistantDataToItemStack(keyDetailItem, "loot")
                .build()
                ;

        items.add(changeName);
        items.add(changeBaseHealth);
        items.add(changeSpawnChance);
        items.add(changeItems);
        items.add(changeLoot);

        return items;
    }

}
