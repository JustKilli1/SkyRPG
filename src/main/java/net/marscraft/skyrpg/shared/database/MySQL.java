package net.marscraft.skyrpg.shared.database;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private ILogManager logger;
    private IConfigManager configManager;
    private FileConfiguration config;
    private String host = "";
    private String port = "";
    private String database = "";
    private String username = "";
    private String password = "";
    private Connection con;

    public MySQL(ILogManager logger, IConfigManager configManager) {
        this.logger = logger;
        this.configManager = configManager;
        this.config = configManager.getConfiguration();
        this.config.addDefault("sql.host", "SQL.IP");
        this.config.addDefault("sql.port", "3306");
        this.config.addDefault("sql.username", "USERNAME");
        this.config.addDefault("sql.password", "PASSWORD");
        this.config.addDefault("sql.database", "DATABASE");
        this.config.options().copyDefaults(true);
        this.configManager.saveConfig();
    }

    private void reloadDBSettings() {
        configManager.reloadConfig();
        host = config.getString("sql.host");
        port = config.getString("sql.port");
        database = config.getString("sql.database");
        username = config.getString("sql.username");
        password = config.getString("sql.password");
    }

    // connect
    public void connect() {
        if(isConnected()) return;

        reloadDBSettings();
        try {
            con = DriverManager.getConnection("jdbc:mysql://"
                                                + host + ":"
                                                + port + "/"
                                                + database
                                                + "?autoReconnect=true&useSSL=false",
                                                username,
                                                password);
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    // disconnect
    public void disconnect() {
        if(!isConnected()) return;

        try {
            con.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    // isConnected
    public boolean isConnected() {
        return (con != null);
    }

    // getConnection
    public Connection getConnection() {
        return con;
    }

}
