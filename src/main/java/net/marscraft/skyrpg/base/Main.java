package net.marscraft.skyrpg.base;

import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.shared.configmanager.ConfigManager;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.LogManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private LogManager logger;
    private DBAccessLayer sql;
    private IConfigManager mysqlConfig, messageConfig;

    @Override
    public void onEnable() {
        logger = new LogManager(this, "Main");
        logger.info("Loading Configs...");
        loadConfigs();
        logger.info("Configs loaded.");

        logger.info("Connect to Database...");
        sql = new DBAccessLayer(logger, mysqlConfig);
        logger.info("Database Connected");

        logger.info("Creating required Databases...");
        if(!createDatabaseTables()) logger.error("Could not create required databases");
        logger.info("All Databases created.");
        registerListener();
        registerCommands();
        logger.info("Loading Modules...");
        loadModules();
        logger.info("Modules loaded.");
        logger.info("SkyRPG Plugin loaded.");
    }

    @Override
    public void onDisable() {
        sql.disable();
    }



    private boolean createDatabaseTables() {
        return true;
    }
    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
    }
    private void registerCommands() {
        //getCommand("mcquest").setExecutor(new MCQuestCommand(logger, sql, messageConfig, this));

    }

    private void loadConfigs() {
        mysqlConfig = new ConfigManager(this, logger, "mysql");
        createMessageDefaultConfig();
    }

    private boolean loadModules() {

        ModuleCustomMobs moduleCustomMobs = new ModuleCustomMobs(this, mysqlConfig, messageConfig);
        ModuleRegions moduleRegions = new ModuleRegions(this, mysqlConfig, messageConfig);
        return true;
    }


    /**
     * Creates Default Messages Config
     */
    private void createMessageDefaultConfig() {
        messageConfig = new ConfigManager(this, logger, "messages");
        FileConfiguration config = messageConfig.getConfiguration();
        config.addDefault("prefix", "&7&l> &a");
        config.addDefault("admin.mcquest.create.questcreated", "&aDer Quest &c[QuestName] &awurde erfolgreich erstellt.");
        config.addDefault("admin.mcquest.create.questnamealreadyexists", "&aEin Quest mit dem Namen &c[QuestName] existiert bereits.");
        config.options().copyDefaults(true);
        messageConfig.saveConfig();
    }

}
