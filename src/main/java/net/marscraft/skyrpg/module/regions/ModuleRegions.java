package net.marscraft.skyrpg.module.regions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.IModule;
import net.marscraft.skyrpg.module.ModuleMode;
import net.marscraft.skyrpg.module.ModuleState;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.commands.CommandMarsRegion;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.listeners.*;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.logmanager.LogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static net.marscraft.skyrpg.module.ModuleMode.*;
import static net.marscraft.skyrpg.module.ModuleState.*;
import static net.marscraft.skyrpg.module.ModuleState.ACTIVE;

public class ModuleRegions implements IModule {
    private static final String moduleName = "Regions";
    private static final String moduleDescription = "Creates Region between two locations";
    private static Map<UUID, ISetup> setups = new HashMap<>();
    private static Map<UUID, IGuiInventory> invs = new HashMap<>();
    private static List<MobSpawnRegion> activeSpawnRegions = new ArrayList<>();
    private final ILogManager logger;
    private ModuleState moduleState;
    private ModuleMode moduleMode;
    private Main plugin;
    private DBAccessLayerRegions sql;
    private DBHandlerRegions dbHandler;
    private DBHandlerCustomMobs dbHandlerCustomMobs;
    private IConfigManager messagesConfig;
    private BukkitTask task;

    public ModuleRegions(Main plugin, IConfigManager mysqlConfig, IConfigManager messagesConfig) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerRegions(logger, mysqlConfig);
        DBAccessLayerCustomMobs sqlCustomMobs = new DBAccessLayerCustomMobs(logger, mysqlConfig);
        dbHandlerCustomMobs = new DBHandlerCustomMobs(logger, sqlCustomMobs);
        dbHandler = new DBHandlerRegions(logger, this.sql, dbHandlerCustomMobs, plugin);
        this.messagesConfig = messagesConfig;

    }

    public ModuleRegions(Main plugin, ModuleMode moduleMode, IConfigManager mysqlConfig, IConfigManager messagesConfig) {
        this.plugin = plugin;
        logger = new LogManager(this.plugin, moduleName);
        this.sql = new DBAccessLayerRegions(logger, mysqlConfig);
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
        if(!createDatabaseTables()) logger.error("Could not create required databases");
        logger.info("All Databases created.");
        registerListener();
        registerCommands();
        logger.info("§aModule Regions loaded Successfully");
        //TODO Module CustomItems muss aktiviert sein damit dieses Module geladen werden kann
        updateModuleState(ACTIVE);
        updateModuleMode(LIVE);
        initializeMobSpawnRegions();
        startMobSpawning();
    }

    private boolean initializeMobSpawnRegions() {
        List<MobSpawnRegion> mobSpawnRegions = dbHandler.getAllMobSpawnRegions(true);
        for(MobSpawnRegion region : mobSpawnRegions) {
            activeSpawnRegions.add(region);
        }
        return true;
    }

    private void startMobSpawning() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                for(MobSpawnRegion mobSpawnRegion : activeSpawnRegions) {
                    mobSpawnRegion.spawnMobs();
                }

            }
        }.runTaskTimer(plugin, 0, 10 * 20);
    }

    private Location getRandomSpawnLocation(MobSpawnRegion mobSpawnRegion) {
        Location loc1 = mobSpawnRegion.getRegion().getBound().getLoc1();
        Location loc2 = mobSpawnRegion.getRegion().getBound().getLoc2();
        int loc1X = loc1.getBlockX();
        int loc2X = loc2.getBlockX();
        int loc1Z = loc1.getBlockZ();
        int loc2Z = loc2.getBlockZ();

        //loc1x 10 loc2x -10
        //x1: loc1x = 10
        //x2: -10

        int x1 = loc1X > loc2X ? loc1X : loc2X;
        int x2 = loc1X > loc2X ? loc2X : loc1X;
        int randomX = ThreadLocalRandom.current().nextInt(x2, x1 + 1);

        int z1 = loc1Z > loc2Z ? loc1Z : loc2Z;
        int z2 = loc1Z > loc2Z ? loc2Z : loc1Z;
        int randomZ = ThreadLocalRandom.current().nextInt(z2, z1 + 1);
        Block block = loc1.getWorld().getHighestBlockAt(randomX, randomZ);
        Location randomLoc = block.getLocation().clone().add(0, 1, 0);
        return randomLoc;
    }

    private boolean createDatabaseTables() {
        if(!sql.createTableRegions()) return false;
        if(!sql.createTableMobSpawnRegion()) return false;
        return sql.createTableSpawnSystemMobController();

    }
    private void registerListener() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new ListenerPlayerInteract(logger, messagesConfig), plugin);
        pluginManager.registerEvents(new ListenerPlayerMove(logger, messagesConfig, sql, dbHandlerCustomMobs, plugin), plugin);
        pluginManager.registerEvents(new ListenerInvClick(logger), plugin);
        pluginManager.registerEvents(new ListenerInvClose(logger), plugin);
        pluginManager.registerEvents(new ListenerPlayerChat(logger), plugin);
    }
    private void registerCommands() {
        plugin.getCommand("marsregion").setExecutor(new CommandMarsRegion(logger, plugin, messagesConfig, sql, dbHandlerCustomMobs));
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
    public static void setSetups(Map<UUID, ISetup> setups) { ModuleRegions.setups = setups; }
    public static void addSetup(UUID key, ISetup value) { setups.put(key, value); }
    public static void removeSetup(UUID key) { setups.remove(key); }

    public static Map<UUID, IGuiInventory> getInvs() { return invs; }
    public static void setInvs(Map<UUID, IGuiInventory> invs) { ModuleRegions.invs = invs; }
    public static void addInv(UUID key, IGuiInventory value) { invs.put(key, value); }
    public static void removeInv(UUID key) { invs.remove(key); }

    public static List<MobSpawnRegion> getActiveSpawnRegions() {
        return activeSpawnRegions;
    }

    public static void addActiveMobSpawnRegion(MobSpawnRegion mobSpawnRegion) {
        activeSpawnRegions.add(mobSpawnRegion);
    }
    public static void removeActiveMobSpawnRegion(MobSpawnRegion mobSpawnRegion) {
        for(int i = 0; i < activeSpawnRegions.size(); i++) {
            MobSpawnRegion region = activeSpawnRegions.get(i);
            if(region.getId() == mobSpawnRegion.getId()) activeSpawnRegions.remove(i);
        }
    }

}
