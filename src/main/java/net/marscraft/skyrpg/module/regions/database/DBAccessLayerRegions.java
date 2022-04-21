package net.marscraft.skyrpg.module.regions.database;

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



    /*
     * Selects
     * */

    public ResultSet getLastRegionsEntry() {
        String sqlQuery = "SELECT * FROM Regions ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    public ResultSet getAllRegions() {
        String sqlQuery = "SELECT * FROM Regions";
        return querySQLRequest(sqlQuery);
    }


}
