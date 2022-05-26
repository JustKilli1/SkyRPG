package net.marscraft.skyrpg.module.customitems.database;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

import java.sql.ResultSet;

public class DBAccesLayerCustomItems extends DBAccessLayer {

    private final ILogManager logger;

    public DBAccesLayerCustomItems(ILogManager logger, IConfigManager mysqlConfig) {
        super(logger, mysqlConfig);
        this.logger = logger;
    }

    public boolean createTableCustomItems() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS CustomItems (" +
                "ID INT NOT NULL, " +
                "Name VARCHAR(50) NOT NULL," +
                "Item LONGTEXT NOT NULL," +
                "PRIMARY KEY (ID)" +
                ")";
        return executeSQLRequest(sqlQuery);
    }

    /**
     * Gets Last CustomItem from tabel CustomItems
     * */
    public ResultSet getLastCustomItem() {
        String sqlQuery = "SELECT * FROM CustomItems ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

}
