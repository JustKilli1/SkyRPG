package net.marscraft.skyrpg.module.custommobs.database;

import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;

public class DBAccessLayerCustomMobs extends DBAccessLayer {

    private final ILogManager logger;

    public DBAccessLayerCustomMobs(ILogManager logger, IConfigManager mysqlConfig) {
        super(logger, mysqlConfig);
        this.logger = logger;
    }

    public boolean createTableCustomMobs() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS CustomMobs (" +
                "ID INT NOT NULL, " +
                "Name VARCHAR(50) NOT NULL," +
                "EntityType VARCHAR(100) NOT NULL," +
                "MaxHealth DOUBLE NOT NULL," +
                "SpawnChance DOUBLE NOT NULL," +
                "Level INT," +
                "Helmet LONGTEXT," +
                "Chestplate LONGTEXT," +
                "Leggings LONGTEXT," +
                "Boots LONGTEXT," +
                "MainItem LONGTEXT," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    public boolean createTableLootItems() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS LootItems (" +
                "ID INT NOT NULL, " +
                "Item LONGTEXT NOT NULL," +
                "DropChance DOUBLE NOT NULL," +
                "AmountMin DOUBLE," +
                "AmountMax DOUBLE," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    public boolean createTableMobLootController() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS MobLootController (" +
                "ID INT NOT NULL, " +
                "MobId INT NOT NULL," +
                "LootItemId INT NOT NULL," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    //SELECT Statements

    public ResultSet getLastMob() {
        String sqlQuery = "SELECT * FROM CustomMobs ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getCustomMobById(int id) {
        String sqlQuery = "SELECT * FROM CustomMobs WHERE ID=" + id;
        return querySQLRequest(sqlQuery);
    }

    /**
     * Gets All LootItems with CustomMobId
     * <p>Table View:</p>
     * <p>Table: |  MobLootController  |                 LootItems                |</p>
     * <p>Field: ID, MobId, LootItemId, ID, Item, DropChance, AmountMin, AmountMax</p>
     * */
    public ResultSet getLootItemsByMobId(int id) {
        String sqlQuery = "SELECT * FROM MobLootController, LootItems WHERE MobLootController.MobId=" + id + " AND MobLootController.LootItemId=LootItems.ID";
        return querySQLRequest(sqlQuery);
    }

    //INSERT Statements

    public boolean insertCustomMob(MobHostile mob) {

        Inventory inv = Bukkit.createInventory(null, 9, "Test");
        ItemStack[] armor = mob.getArmor();

        String helmet = armor == null ? null : Utils.itemStackToBase64(armor[0]);
        String chestplate = armor == null ? null : Utils.itemStackToBase64(armor[1]);
        String leggings = armor == null ? null : Utils.itemStackToBase64(armor[2]);
        String boots = armor == null ? null : Utils.itemStackToBase64(armor[3]);
        String mainItem = Utils.itemStackToBase64(mob.getMainItem());

        String sqlQuery = "INSERT INTO CustomMobs" +
                          "(ID, Name, EntityType, MaxHealth, SpawnChance, Level, Helmet, Chestplate, Leggings, Boots, MainItem)" +
                          "VALUES ("
                          + mob.getId() + ", '"
                          + mob.getName() + "', '"
                          + mob.getType().toString() + "', "
                          + mob.getMaxHealth() + ", "
                          + mob.getSpawnChance() + ", "
                          + mob.getLevel() + ", '"
                          + helmet + "', '"
                          + chestplate + "', '"
                          + leggings + "', '"
                          + boots + "', '"
                          + mainItem + "'" +
                          ")";
        return executeSQLRequest(sqlQuery);
    }


}
