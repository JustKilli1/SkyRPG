package net.marscraft.skyrpg.module.regions.database;

import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.module.regions.region.Region;

import java.sql.ResultSet;

public class DBAccessLayerRegions extends DBAccessLayer {

    private final ILogManager logger;

    public DBAccessLayerRegions(ILogManager logger, IConfigManager mysqlConfig) {
        super(logger, mysqlConfig);
        this.logger = logger;
    }



    /*Create Table Methods
     * */

    public boolean createTableRegions() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS Regions (" +
                "ID INT NOT NULL, " +
                "Name VARCHAR(50) NOT NULL," +
                "Loc1 VARCHAR(100) NOT NULL," +
                "Loc2 VARCHAR(100) NOT NULL," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    public boolean createTableSpawnSystemMobController() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS SpawnSystemMobController (" +
                "ID INT NOT NULL, " +
                "MobSpawnRegionId INT NOT NULL," +
                "MobId INT NOT NULL," +
                "SpawnChance DOUBLE DEFAULT 0.0," +
                "LevelMin INT DEFAULT 0," +
                "LevelMax INT DEFAULT 0," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    public boolean createTableMobSpawnRegion() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS MobSpawnRegion (" +
                "ID INT NOT NULL, " +
                "RegionId INT NOT NULL," +
                "MaxMobs INT DEFAULT 0," +
                "Active BOOLEAN DEFAULT false," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }


    /*
     * Inserts
     * */

    public boolean insertRegion(Region region) {
        String loc1Str = Utils.locationToStr(region.getBound().getLoc1());
        String loc2Str = Utils.locationToStr(region.getBound().getLoc2());

        String sqlQuery = "INSERT INTO Regions " +
                "(ID, Name, Loc1, Loc2) VALUES ("
                + region.getId() + ", '"
                + region.getName() + "', '"
                + loc1Str + "', '"
                + loc2Str + "')";

        return executeSQLRequest(sqlQuery);
    }

    public boolean insertMobSpawnRegion(MobSpawnRegion mobSpawnRegion) {
        String sqlQuery = "INSERT INTO MobSpawnRegion " +
                "(ID, RegionId, MaxMobs, Active)" +
                "VALUES (" +
                mobSpawnRegion.getId() + "," +
                mobSpawnRegion.getRegion().getId() + "," +
                mobSpawnRegion.getMaxMobs() + "," +
                mobSpawnRegion.isActive() +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    public boolean insertMob(int mobSpawnRegionId, int mobId, MobCharacteristics mobCharacteristics) {
        String sqlQuery = "INSERT INTO SpawnSystemMobController " +
                "(ID, MobSpawnRegionId, MobId, SpawnChance, LevelMin, LevelMax) " +
                "VALUES (" +
                mobCharacteristics.getControllerId() + ", " +
                mobSpawnRegionId + ", " +
                mobId + ", " +
                mobCharacteristics.getSpawnChance() + ", " +
                mobCharacteristics.getLevelMin() + ", " +
                mobCharacteristics.getLevelMax() + ")";
        return executeSQLRequest(sqlQuery);
    }


    /*
     * Selects
     * */

    public ResultSet getLastRegionsEntry() {
        String sqlQuery = "SELECT * FROM Regions ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getLastMobSpawnRegionsEntry() {
        String sqlQuery = "SELECT * FROM MobSpawnRegion ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getLastMobControllerId() {
        String sqlQuery = "SELECT * FROM SpawnSystemMobController ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getAllRegions() {
        String sqlQuery = "SELECT * FROM Regions";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getRegion(int regionID) {
        String sqlQuery = "SELECT * FROM Regions WHERE ID=" + regionID;
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getAllMobSpawnRegions() {
        String sqlQuery = "SELECT * FROM MobSpawnRegion";
        return querySQLRequest(sqlQuery);
    }
    public ResultSet getAllMobSpawnRegions(boolean active) {
        String sqlQuery = "SELECT * FROM MobSpawnRegion WHERE Active=" + active;
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getMobSpawnRegion(int id) {
        String sqlQuery = "SELECT * FROM MobSpawnRegion WHERE ID=" + id;
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getMobSpawnRegion(int id, boolean active) {
        String sqlQuery = "SELECT * FROM MobSpawnRegion WHERE ID=" + id + " AND Active=" + active;
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getAllMobsInMobSpawnRegion(int mobSpawnRegionId) {
        String sqlQuery = "SELECT * FROM SpawnSystemMobController WHERE MobSpawnRegionId=" + mobSpawnRegionId;
        return querySQLRequest(sqlQuery);
    }
    /*

     * Updates
     * */
    public boolean updateRegionsName(int regionId, String newName) {
        String sqlQuery = "UPDATE Regions SET Name='" + newName + "' WHERE ID=" + regionId;
        return executeSQLRequest(sqlQuery);
    }

    public boolean updateMobSpawnRegion(MobSpawnRegion mobSpawnRegion) {
        String sqlQuery = "UPDATE MobSpawnRegion SET MaxMobs=" + mobSpawnRegion.getMaxMobs() + ", Active=" + mobSpawnRegion.isActive() +
                " WHERE ID=" + mobSpawnRegion.getId();
        return executeSQLRequest(sqlQuery);
    }

    public boolean updateMobSpawnRegionMob(MobCharacteristics mobCharacteristics) {
        String sqlQuery = "UPDATE SpawnSystemMobController SET " +
                "SpawnChance=" + mobCharacteristics.getSpawnChance() + ", " +
                "LevelMin=" + mobCharacteristics.getLevelMin() + ", " +
                "LevelMax=" + mobCharacteristics.getLevelMax() +
                " WHERE ID=" + mobCharacteristics.getControllerId();

        return executeSQLRequest(sqlQuery);

    }

    /*
    * Deletes
    * */

    public boolean deleteMob(int controllerId) {
        String sqlQuery = "DELETE FROM SpawnSystemMobController WHERE ID=" + controllerId;
        return executeSQLRequest(sqlQuery);
    }

}
