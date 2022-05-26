package net.marscraft.skyrpg.module.customitems;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.IModule;
import net.marscraft.skyrpg.module.ModuleMode;
import net.marscraft.skyrpg.module.ModuleState;
import net.marscraft.skyrpg.module.customitems.database.DBAccesLayerCustomItems;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.logmanager.LogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.marscraft.skyrpg.module.ModuleMode.*;
import static net.marscraft.skyrpg.module.ModuleState.*;

public class ModuleCustomItems implements IModule {

    private static final String moduleName = "CustomItems";
    private static final String moduleDescription = "Implements Custom Items";
    private static Map<UUID, ISetup> setups = new HashMap<>();
    private final ILogManager logger;
    private ModuleState moduleState;
    private ModuleMode moduleMode;
    private Main plugin;
    private DBAccesLayerCustomItems sql;
    private DBHandlerCustomItems dbHandler;
    private IConfigManager messagesConfig;

    public ModuleCustomItems(Main plugin, IConfigManager mysqlConfig, IConfigManager messagesConfig) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccesLayerCustomItems(logger, mysqlConfig);
        dbHandler = new DBHandlerCustomItems(logger, this.sql);
        this.messagesConfig = messagesConfig;

    }

    public ModuleCustomItems(Main plugin, ModuleMode moduleMode, IConfigManager mysqlConfig, IConfigManager messagesConfig) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccesLayerCustomItems(logger, mysqlConfig);
        this.messagesConfig = messagesConfig;

        updateModuleMode(moduleMode);
    }

    @Override
    public void onModuleEnable() {

        //Module get loaded logic

        if(this.moduleMode == MAINTENANCE || this.moduleMode == DEBUG) {
            //TODO Module funktioniert nur für spieler mit bestimmter permission

            return;
        }

        logger.info("Loading Module: Regions");
        logger.info("Creating required Databases...");
        //if(!createDatabaseTables()) logger.error("Could not create required databases");
        logger.info("All Databases created.");
        registerListener();
        registerCommands();
        logger.info("§aModule Regions loaded Successfully");
        //TODO Module CustomItems muss aktiviert sein damit dieses Module geladen werden kann
        updateModuleState(ACTIVE);
        updateModuleMode(LIVE);
    }

    private boolean createDatabaseTables() {
        //return sql.createTableRegions();
        return true;
    }
    private void registerListener() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
    }
    private void registerCommands() {
    }

    @Override
    public void onModuleDisable() {
        logger.info("Disabling Module Regions...");
        updateModuleState(INACTIVE);
        logger.info("Module Regions Disabled");
    }

    @Override
    public void reloadModule() {
        updateModuleState(RELOADING);


        //Module Reload logic
        //...
        //...
        //...


        updateModuleState(ACTIVE);
    }

    @Override
    public void onListenerCall(EventStorage storage) {

    }

    @Override
    public ModuleState getModuleState() {
        return moduleState;
    }

    @Override
    public void updateModuleState(ModuleState moduleState) { this.moduleState = moduleState; }

    @Override
    public ModuleMode getModuleMode() { return moduleMode; }

    @Override
    public void updateModuleMode(ModuleMode moduleMode) { this.moduleMode = moduleMode; }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getModuleDescription() {
        return moduleDescription;
    }

    public static Map<UUID, ISetup> getSetups() { return setups; }
    public static void setSetups(Map<UUID, ISetup> setups) { ModuleCustomItems.setups = setups; }
    public static void addSetup(UUID key, ISetup value) { setups.put(key, value); }
    public static void removeSetup(UUID key) { setups.remove(key); }

}
