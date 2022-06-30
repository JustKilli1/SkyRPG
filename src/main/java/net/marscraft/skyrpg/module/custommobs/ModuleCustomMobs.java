package net.marscraft.skyrpg.module.custommobs;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.IModule;
import net.marscraft.skyrpg.module.ModuleMode;
import net.marscraft.skyrpg.module.ModuleState;
import net.marscraft.skyrpg.module.custommobs.commands.CommandMarsMob;
import net.marscraft.skyrpg.module.custommobs.commands.CommandSpawnCustomMob;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.listeners.*;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.logmanager.LogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.marscraft.skyrpg.module.ModuleMode.*;
import static net.marscraft.skyrpg.module.ModuleMode.DEBUG;
import static net.marscraft.skyrpg.module.ModuleMode.MAINTENANCE;
import static net.marscraft.skyrpg.module.ModuleState.*;

public class ModuleCustomMobs implements IModule {

    private static final String moduleName = "CustomMobs";
    private static final String moduleDescription = "Creates unique Mobs";
    private static Map<UUID, ISetup> setups = new HashMap<>();
    private final ILogManager logger;
    private static List<IModule> requiredModules = new ArrayList<>();
    private ModuleState moduleState;
    private ModuleMode moduleMode;
    private Main plugin;
    private DBAccessLayerCustomMobs sql;
    private DBHandlerCustomMobs dbHandler;
    private IConfigManager messagesConfig, mysqlConfig;

    public ModuleCustomMobs(Main plugin, IConfigManager mysqlConfig, IConfigManager messagesConfig, List<IModule> requiredModules) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerCustomMobs(logger, mysqlConfig);
        this.mysqlConfig = mysqlConfig;
        dbHandler = new DBHandlerCustomMobs(logger, this.sql);
        this.messagesConfig = messagesConfig;
        this.requiredModules = requiredModules;

    }

    public ModuleCustomMobs(Main plugin, ModuleMode moduleMode, IConfigManager mysqlConfig, IConfigManager messagesConfig, List<IModule> requiredModules) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerCustomMobs(logger, mysqlConfig);
        this.mysqlConfig = mysqlConfig;
        this.messagesConfig = messagesConfig;
        this.requiredModules = requiredModules;

        updateModuleMode(moduleMode);
    }

    private boolean requiredModulesActive() {

        for(IModule module : requiredModules) {
            if(module.getModuleMode() != LIVE) logger.warn("Required Module " + module.getModuleName() + " is not Live");
            if(module.getModuleState() != ACTIVE) {
                logger.error("Could not load Required Module " + module.getModuleName() + ". Module shutdown");
                return false;
            }
        }

        return true;
    }

    @Override
    public void onModuleEnable() {


        //Module get loaded logic

        if(this.moduleMode == MAINTENANCE || this.moduleMode == DEBUG) {
            //TODO Module funktioniert nur für spieler mit bestimmter permission

            return;
        }

        logger.info("Loading Module: CustomMobs");
        logger.info("Checking if required Modules are active...");
        if(!requiredModulesActive()) return;
        logger.info("All required modules Active");
        logger.info("Creating required Databases...");
        if(!createDatabaseTables()) logger.error("Could not create required databases");
        logger.info("All Databases created.");
        registerListener();
        registerCommands();
        startDamageIndicators();
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
        pluginManager.registerEvents(new ListenerInvClick(logger, messagesConfig, dbHandler, sql), plugin);
        pluginManager.registerEvents(new ListenerPlayerChat(logger, messagesConfig), plugin);
        pluginManager.registerEvents(new ListenerInvClose(logger, messagesConfig, dbHandler, sql), plugin);
        pluginManager.registerEvents(new ListenerEntityCombust(logger), plugin);
    }
    private void registerCommands() {
        plugin.getCommand("spawnCustomMob").setExecutor(new CommandSpawnCustomMob(logger, messagesConfig, dbHandler));
        plugin.getCommand("marsMobs").setExecutor(new CommandMarsMob(logger, sql, dbHandler, messagesConfig));
    }

    private void startDamageIndicators() {
        new BukkitRunnable() {
            Set<Entity> stands = ListenerEntityDamage.indicators.keySet();
            List<Entity> removal = new ArrayList<>();
            @Override
            public void run() {
                for (Entity stand : stands) {
                    int ticksLeft = ListenerEntityDamage.indicators.get(stand);
                    if (ticksLeft == 0) {
                        stand.remove();
                        removal.add(stand);
                        continue;
                    }
                    ticksLeft--;
                    ListenerEntityDamage.indicators.put(stand, ticksLeft);
                }
                stands.removeAll(removal);
            }
        }.runTaskTimer(plugin, 0L, 1L);
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

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getModuleDescription() {
        return moduleDescription;
    }

    public static Map<UUID, ISetup> getSetups() { return setups; }
    public static void setSetups(Map<UUID, ISetup> setups) { ModuleCustomMobs.setups = setups; }
    public static void addSetup(UUID key, ISetup value) { setups.put(key, value); }
    public static void removeSetup(UUID key) { setups.remove(key); }

}
