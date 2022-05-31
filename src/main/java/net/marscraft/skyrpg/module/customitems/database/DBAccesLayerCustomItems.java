package net.marscraft.skyrpg.module.customitems.database;

import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.shared.Utils;
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

    //SELECT Statements

    /**
     * Gets Last CustomItem from tabel CustomItems
     * */
    public ResultSet getLastCustomItem() {
        String sqlQuery = "SELECT * FROM CustomItems ORDER BY ID DESC LIMIT 1";
        return querySQLRequest(sqlQuery);
    }

    //UPDATE Statements

    public boolean updateCustomItem(CustomItem customItem) {
        String sqlQuery = "UPDATE CustomItems " +
                "SET Name = '" + customItem.getItemName() + "', " +
                "Item = '" + Utils.itemStackToBase64(customItem.getItem()) + "' " +
                "WHERE ID=" + customItem.getItemId();
        return executeSQLRequest(sqlQuery);
    }

    //INSERT Statements
    public boolean insertCustomItem(CustomItem customItem) {
        String sqlQuery = "INSERT INTO CustomItems" +
                "(ID, Name, Item) VALUES " +
                "(" + customItem.getItemId() + ", '" + customItem.getItemName() + "', '" + Utils.itemStackToBase64(customItem.getItem()) + "')";
        return  executeSQLRequest(sqlQuery);
    }

    //DELETE Statements

    public boolean deleteCustomItem(int itemId) {
        String sqlQuery = "DELETE FROM CustomItems WHERE ID=" + itemId;
        return executeSQLRequest(sqlQuery);
    }

}
