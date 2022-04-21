package net.marscraft.skyrpg.module.custommobs;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.IModule;
import net.marscraft.skyrpg.module.ModuleMode;
import net.marscraft.skyrpg.module.ModuleState;
import net.marscraft.skyrpg.module.custommobs.commands.CommandMarsMob;
import net.marscraft.skyrpg.module.custommobs.commands.CommandSpawnCustomMob;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.listeners.ListenerEntityDamage;
import net.marscraft.skyrpg.module.custommobs.listeners.ListenerInvClick;
import net.marscraft.skyrpg.module.custommobs.listeners.ListenerPlayerChat;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.logmanager.LogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.marscraft.skyrpg.module.ModuleMode.DEBUG;
import static net.marscraft.skyrpg.module.ModuleMode.MAINTENANCE;
import static net.marscraft.skyrpg.module.ModuleState.*;

public class ModuleCustomMobs implements IModule {

    public static final String moduleName = "CustomMobs";
    public static final String moduleDescription = "Creates unique Mobs";
    private static Map<UUID, ISetup> setups = new HashMap<>();
    private final ILogManager logger;
    private ModuleState moduleState;
    private ModuleMode moduleMode;
    private Main plugin;
    private DBAccessLayerCustomMobs sql;
    private DBHandlerCustomMobs dbHandler;
    private IConfigManager messagesConfigManager;

    public ModuleCustomMobs(Main plugin, IConfigManager mysqlConfig, IConfigManager messagesConfigManager) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerCustomMobs(logger, mysqlConfig);
        dbHandler = new DBHandlerCustomMobs(logger, this.sql);
        this.messagesConfigManager = messagesConfigManager;

        onModuleEnable();
    }

    public ModuleCustomMobs(Main plugin, ModuleMode moduleMode, IConfigManager mysqlConfig, IConfigManager messagesConfigManager) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerCustomMobs(logger, mysqlConfig);
        this.messagesConfigManager = messagesConfigManager;


        updateModuleMode(moduleMode);
        onModuleEnable();
    }



    @Override
    public void onModuleEnable() {


        //Module get loaded logic

        if(this.moduleMode == MAINTENANCE || this.moduleMode == DEBUG) {
            //TODO Module funktioniert nur für spieler mit bestimmter permission

            return;
        }

        logger.info("Loading Module: CustomMobs");
        logger.info("Creating required Databases...");
        if(!createDatabaseTables()) logger.error("Could not create required databases");
        logger.info("All Databases created.");
        registerListener();
        registerCommands();
        logger.info("§aModule CustomMobs loaded Successfully");
        //TODO Module CustomItems muss aktiviert sein damit dieses Module geladen werden kann
        updateModuleState(ACTIVE);
    }

    private boolean createDatabaseTables() {

        if(!sql.createTableCustomMobs()) return false;
        if(!sql.createTableLootItems()) return false;
        return sql.createTableMobLootController();

    }
    private void registerListener() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new ListenerEntityDamage(logger, dbHandler), plugin);
        pluginManager.registerEvents(new ListenerInvClick(logger, messagesConfigManager), plugin);
        pluginManager.registerEvents(new ListenerPlayerChat(logger, messagesConfigManager), plugin);
    }
    private void registerCommands() {
        plugin.getCommand("spawnCustomMob").setExecutor(new CommandSpawnCustomMob(logger, messagesConfigManager));
        plugin.getCommand("marsMobs").setExecutor(new CommandMarsMob(logger, sql, dbHandler, messagesConfigManager));
    }

    @Override
    public void onModuleDisable() {
        logger.info("Disabling Module CustomMobs...");
        updateModuleState(INACTIVE);
        logger.info("Module CustomMobs Disabled");
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

    public static Map<UUID, ISetup> getSetups() { return setups; }
    public static void setSetups(Map<UUID, ISetup> setups) { ModuleCustomMobs.setups = setups; }
    public static void addSetup(UUID key, ISetup value) { setups.put(key, value); }
    public static void removeSetup(UUID key) { setups.remove(key); }

}