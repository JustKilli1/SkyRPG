package net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InvFunctionDisplayMobs extends InvFunction {

    private final ILogManager logger;
    private DBHandlerRegions dbHandler;
    private MobSpawnRegion mobSpawnRegion;

    public InvFunctionDisplayMobs(ILogManager logger, DBHandlerRegions dbHandler, MobSpawnRegion mobSpawnRegion) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.mobSpawnRegion = mobSpawnRegion;
    }


    public Inventory add(Inventory inv) {
        Map<MobHostile, MobCharacteristics> mobs = mobSpawnRegion.getMobs();
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");
        ItemStack[] invContents = inv.getContents();
        int x = 0;
        ItemStack addNemMobItem = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayname("§cAdd new Mob")
                .addPersistantDataToItemStack(keyDetailItem, "newMob")
                .addPersistantDataToItemStack(keyMobId, 0)
                .build();
        invContents[49] = addNemMobItem;

        for(MobHostile key : mobs.keySet()) {
            if(x >= 9) break;
            MobCharacteristics value = mobs.get(key);
            ItemStack mobItem = getMobDisplayItem(key);
            ItemStack changeSpawnChanceItem = getSpawnChanceDisplayItem(value.getSpawnChance(), key.getId());
            ItemStack changeLevelItem = getLevelDisplayItem(value.getLevelMin(), value.getLevelMax(), key.getId());
            ItemStack deleteItem = getDeleteDisplayItem(value, key.getId());

            invContents[x] = mobItem;
            invContents[x + 9] = changeSpawnChanceItem;
            invContents[x + 18] = changeLevelItem;
            invContents[x + 27] = deleteItem;
            x++;
        }

        inv.setContents(invContents);
        return inv;
    }

    private ItemStack getMobDisplayItem(MobHostile mobHostile) {
        ItemStack displayItem = getCustomMobDisplayItem(mobHostile.getType().toString(), mobHostile.getName(), mobHostile.getId(), mobHostile.getBaseHealth(), mobHostile.getSpawnChance());

        return displayItem;
    }

    private ItemStack getSpawnChanceDisplayItem(double spawnChance, int mobId) {
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");

        ItemStack item = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange SpawnChance")
                .setLore("§aSpawnChance: §c" + spawnChance)
                .addPersistantDataToItemStack(keyDetailItem, "spawnChance")
                .addPersistantDataToItemStack(keyMobId, mobId)
                .build();

        return item;
    }

    private ItemStack getLevelDisplayItem(int levelMin, int levelMax, int mobId) {
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");

        ItemStack item = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayname("§cChange Level")
                .setLore("§aLevelMin: §c" + levelMin,
                        "§aLevelMax: §c" + levelMax)
                .addPersistantDataToItemStack(keyDetailItem, "level")
                .addPersistantDataToItemStack(keyMobId, mobId)
                .build();

        return item;
    }

    private ItemStack getDeleteDisplayItem(MobCharacteristics mobCharacteristics, int mobId) {
        NamespacedKey keyDetailItem = new NamespacedKey(Main.getPlugin(Main.class), "detailItem");
        NamespacedKey keyControllerId = new NamespacedKey(Main.getPlugin(Main.class), "controllerId");
        NamespacedKey keyMobId = new NamespacedKey(Main.getPlugin(Main.class), "mobId");

        ItemStack item = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setDisplayname("§cDelete")
                .addPersistantDataToItemStack(keyDetailItem, "delete")
                .addPersistantDataToItemStack(keyControllerId, mobCharacteristics.getControllerId())
                .addPersistantDataToItemStack(keyMobId, mobId)
                .build();

        return item;
    }

}
