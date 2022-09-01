package net.marscraft.skyrpg.shared.database;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAccessLayer {

    /**
     * Normal Sql Commands no Data processing
     * */

    private final ILogManager logger;
    private MySQL mySql;

    public DBAccessLayer(ILogManager logger, IConfigManager mysqlConfig) {
        this.logger = logger;
        mySql = new MySQL(this.logger, mysqlConfig);
        mySql.connect();
    }

    /*Create Table Methods
    * */



    //Module: [Module Name]




    public void disable() {
        mySql.disconnect();
    }

    /**
     * Reconnects to database if not connected
     */
    private void checkAndReconnectConnection() {
        if (!mySql.isConnected()) {
            logger.info("Connection lost! Reconnecting...");
            mySql.connect();
        }
    }

    protected boolean executeSQLRequest(String sqlQuery) {
        checkAndReconnectConnection();
        if (mySql.isConnected()) {
            Connection connection = mySql.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(sqlQuery);
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                logger.error(e);
                return false;
            }
        } else {
            return false;
        }
    }
    protected ResultSet querySQLRequest(String sqlQuery) {
        checkAndReconnectConnection();
        if (mySql.isConnected()) {
            Connection connection = mySql.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(sqlQuery);
                return ps.executeQuery();
            } catch (SQLException e) {
                logger.error(e);
                return null;
            }
        } else {
            return null;
        }
    }

}
