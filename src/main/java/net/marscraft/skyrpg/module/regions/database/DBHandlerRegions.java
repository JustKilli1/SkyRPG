package net.marscraft.skyrpg.module.regions.database;


import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobCharacteristics;
import net.marscraft.skyrpg.module.regions.region.mobspawnregion.MobSpawnRegion;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.database.DBHandler;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.module.regions.region.Bound;
import net.marscraft.skyrpg.module.regions.region.Region;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBHandlerRegions extends DBHandler {

    private final ILogManager logger;
    private DBAccessLayerRegions sql;
    private DBHandlerCustomMobs dbHandlerCustomMobs;
    private Main plugin;

    public DBHandlerRegions(ILogManager logger, DBAccessLayerRegions sql, DBHandlerCustomMobs dbHandlerCustomMobs, Main plugin) {
        super(logger, sql);
        this.logger = logger;
        this.sql = sql;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
        this.plugin = plugin;
    }

    public int getLastRegionId() {
        ResultSet rs = sql.getLastRegionsEntry();

        if(rs == null) return 0;
        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }

    public int getLastMobControllerId() {
        ResultSet rs = sql.getLastMobControllerId();

        if(rs == null) return 0;
        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }

    public int getLastMobSpawnRegionId() {
        ResultSet rs = sql.getLastMobSpawnRegionsEntry();

        if(rs == null) return 0;
        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }

    public Region getRegion(int regionId) {
        ResultSet rs = sql.getRegion(regionId);

        if(rs == null) return null;

        try {
            if(!rs.next()) return null;
            String name = rs.getString("Name");
            Location loc1 = Utils.locationFromStr(rs.getString("Loc1"));
            Location loc2 = Utils.locationFromStr(rs.getString("Loc2"));
            Bound bound = new Bound(loc1, loc2);
            return new Region(logger, regionId, name, bound);

        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public List<Region> getAllRegions() {
        ResultSet rs = sql.getAllRegions();
        List<Region> results = new ArrayList<>();

        if(rs == null) return null;

        try {
            while(rs.next()) {
                int id = rs.getInt("ID");
                Region region = getRegion(id);
                results.add(region);
            }
            return results;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public MobSpawnRegion getMobSpawnRegion(int id) {
        ResultSet rs = sql.getMobSpawnRegion(id);

        if(rs == null) return null;

        try {
            if(!rs.next()) return null;
            int regionId = rs.getInt("RegionId");
            int maxMobs = rs.getInt("MaxMobs");
            boolean active = rs.getBoolean("Active");
            Map<MobHostile, MobCharacteristics> mobs = getAllMobsInSpawnRegion(id);
            Region region = getRegion(regionId);
            return new MobSpawnRegion(logger, id, region, maxMobs, mobs, active, plugin);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public MobSpawnRegion getMobSpawnRegion(int id, boolean active) {
        ResultSet rs = sql.getMobSpawnRegion(id, active);

        if(rs == null) return null;

        try {
            if(!rs.next()) return null;
            int regionId = rs.getInt("RegionId");
            int maxMobs = rs.getInt("MaxMobs");
            boolean isActive = rs.getBoolean("Active");
            Map<MobHostile, MobCharacteristics> mobs = getAllMobsInSpawnRegion(id);
            Region region = getRegion(regionId);
            return new MobSpawnRegion(logger, id, region, maxMobs, mobs, active, plugin);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public List<MobSpawnRegion> getAllMobSpawnRegions() {
        ResultSet rs = sql.getAllMobSpawnRegions();
        List<MobSpawnRegion> results = new ArrayList<>();

        if(rs == null) return null;

        try {
            while(rs.next()) {
                int id = rs.getInt("ID");
                MobSpawnRegion mobSpawnRegion = getMobSpawnRegion(id);
                results.add(mobSpawnRegion);
            }
            return results;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public List<MobSpawnRegion> getAllMobSpawnRegions(boolean active) {
        ResultSet rs = sql.getAllMobSpawnRegions(active);
        List<MobSpawnRegion> results = new ArrayList<>();

        if(rs == null) return null;

        try {
            while(rs.next()) {
                int id = rs.getInt("ID");
                MobSpawnRegion mobSpawnRegion = getMobSpawnRegion(id, active);
                results.add(mobSpawnRegion);
            }
            return results;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public Map<MobHostile, MobCharacteristics> getAllMobsInSpawnRegion(int mobSpawnRegionId) {
        ResultSet rs = sql.getAllMobsInMobSpawnRegion(mobSpawnRegionId);
        Map<MobHostile, MobCharacteristics> results = new HashMap<>();

        if(rs == null) return null;

        try {
            while(rs.next()) {
                int controllerId = rs.getInt("ID");
                int mobId = rs.getInt("MobId");
                double spawnChance = rs.getDouble("SpawnChance");
                int levelMin = rs.getInt("LevelMin");
                int levelMax = rs.getInt("LevelMax");
                MobCharacteristics mobCharacteristics = new MobCharacteristics(spawnChance, controllerId, levelMin, levelMax);
                MobHostile mob = dbHandlerCustomMobs.getHostileMobById(mobId);
                results.put(mob, mobCharacteristics);
            }
            return results;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

}
