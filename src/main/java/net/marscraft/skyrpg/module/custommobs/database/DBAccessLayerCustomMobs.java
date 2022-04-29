package net.marscraft.skyrpg.module.custommobs.database;

import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
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
                "BaseHealth DOUBLE NOT NULL," +
                "SpawnChance DOUBLE NOT NULL," +
                "Helmet LONGTEXT," +
                "Chestplate LONGTEXT," +
                "Leggings LONGTEXT," +
                "Boots LONGTEXT," +
                "MainItem LONGTEXT," +
                "Active BOOLEAN," +
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

    /**
     * Gets Last Mob from tabel CustomMobs
     * */
    public ResultSet getLastMob() {
        String sqlQuery = "SELECT * FROM CustomMobs ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    /**
     * Gets CustomMob by Id
     * */
    public ResultSet getCustomMobById(int id) {
        String sqlQuery = "SELECT * FROM CustomMobs WHERE ID=" + id;
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getCustomMobByState(boolean active) {
        String sqlQuery = "SELECT * FROM CustomMobs WHERE Active=" + active;
        return querySQLRequest(sqlQuery);
    }

    /**
     * Gets All LootItems with CustomMobId
     * <p>Table View:</p>
     * <p>Table: |  MobLootController  |                 LootItems                |</p>
     * <p>Field: ID, MobId, LootItemId, ID, Item, DropChance, AmountMin, AmountMax</p>
     * */
    public ResultSet getLootItemsByMobId(int id) {
        String sqlQuery = "SELECT * FROM MobLootController, LootItems " +
                          "WHERE MobLootController.MobId=" + id + " " +
                          "AND MobLootController.LootItemId=LootItems.ID";
        return querySQLRequest(sqlQuery);
    }

    //INSERT Statements
    /**
     * Inserts new CustomMob in table CustomMob
     * */
    public boolean insertCustomMob(MobHostile mob) {

        ItemStack[] armor = mob.getArmor();

        String helmet = armor == null ? null : Utils.itemStackToBase64(armor[0]);
        String chestplate = armor == null ? null : Utils.itemStackToBase64(armor[1]);
        String leggings = armor == null ? null : Utils.itemStackToBase64(armor[2]);
        String boots = armor == null ? null : Utils.itemStackToBase64(armor[3]);
        String mainItem = Utils.itemStackToBase64(mob.getMainItem());

        String sqlQuery = "INSERT INTO CustomMobs" +
                          "(ID, Name, EntityType, BaseHealth, SpawnChance, Helmet, Chestplate, Leggings, Boots, MainItem, Active)" +
                          "VALUES ("
                          + mob.getId() + ", '"
                          + mob.getName() + "', '"
                          + mob.getType().toString() + "', "
                          + mob.getBaseHealth() + ", "
                          + mob.getSpawnChance() + ", '"
                          + helmet + "', '"
                          + chestplate + "', '"
                          + leggings + "', '"
                          + boots + "', '"
                          + mainItem + "', "
                          + mob.isActive() + "" +
                          ")";
        return executeSQLRequest(sqlQuery);
    }

    //UPDATE Statements

    public boolean updateCustomMobName(int mobId, String newName) {
        return updateCustomMobField(mobId, "Name", newName);
    }

    public boolean updateCustomMobType(int mobId, String newType) {
        return updateCustomMobField(mobId, "EntityType", newType);
    }

    public boolean updateCustomMobBaseHealth(int mobId, double newBaseHealth) {
        return updateCustomMobField(mobId, "BaseHealth", newBaseHealth);
    }
    public boolean updateCustomMobSpawnChance(int mobId, double newSpawnChance) {
        return updateCustomMobField(mobId, "SpawnChance", newSpawnChance);
    }
    public boolean updateCustomMobActive(int mobId, boolean active) {
        return updateCustomMobField(mobId, "Active", active);
    }
    public boolean updateCustomMobArmor(int mobId, ItemStack[] armor) {
        String helmetStr = Utils.itemStackToBase64(armor[0]);
        String chestplateStr = Utils.itemStackToBase64(armor[1]);
        String leggingsStr = Utils.itemStackToBase64(armor[2]);
        String bootsStr = Utils.itemStackToBase64(armor[3]);

        if(!updateCustomMobField(mobId, "Helmet", helmetStr)) return false;
        if(!updateCustomMobField(mobId, "Chestplate", chestplateStr)) return false;
        if(!updateCustomMobField(mobId, "Leggings", leggingsStr)) return false;
        return updateCustomMobField(mobId, "Boots", bootsStr);
    }

    public boolean updateCustomMobMainItem(int mobId, ItemStack mainItem) {
        String mainItemStr = Utils.itemStackToBase64(mainItem);
        return updateCustomMobField(mobId, "MainItem", mainItemStr);
    }

    private boolean updateCustomMobField(int mobId, String fieldName, String value) {
        String sqlQuery = "UPDATE CustomMobs " +
                          "SET " + fieldName + "='" + value + "' " +
                          "WHERE ID=" + mobId
                          ;
        return executeSQLRequest(sqlQuery);
    }

    private boolean updateCustomMobField(int mobId, String fieldName, double value) {
        String sqlQuery = "UPDATE CustomMobs " +
                "SET " + fieldName + "=" + value + " " +
                "WHERE ID=" + mobId
                ;
        return executeSQLRequest(sqlQuery);
    }

    private boolean updateCustomMobField(int mobId, String fieldName, boolean value) {
        String sqlQuery = "UPDATE CustomMobs " +
                "SET " + fieldName + "=" + value + " " +
                "WHERE ID=" + mobId
                ;
        return executeSQLRequest(sqlQuery);
    }

}
