package net.marscraft.skyrpg.module.regions.region.mobspawnregion;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MobSpawnRegion {

    private final ILogManager logger;
    private int id;
    private Region region;
    private int maxMobs;
    private Map<MobHostile, MobCharacteristics> mobs;
    private List<Entity> spawnedMobs = new ArrayList<>();
    private boolean active;

    private Main plugin;

    public MobSpawnRegion(ILogManager logger, int id, Region region, int maxMobs, Map<MobHostile, MobCharacteristics> mobs, boolean active, Main plugin) {
        this.logger = logger;
        this.id = id;
        this.region = region;
        this.maxMobs = maxMobs;
        this.mobs = mobs;
        this.active = active;
        this.plugin = plugin;
    }

    public void spawnMobs() {
        if(mobs == null) return;

        List<Entity> removal = new ArrayList<>();

        for (Entity entity : spawnedMobs) {
            if(!entity.isValid() || entity.isDead()) removal.add(entity);
        }
        spawnedMobs.removeAll(removal);
        int mobsInRegion = spawnedMobs.size();
        if(mobsInRegion >= maxMobs) return;
        int maxSpawnableAmount = maxMobs - mobsInRegion;
        MobHostile spawningMob = getSpawningMob();
        if(spawningMob == null) return;
        int amount = ThreadLocalRandom.current().nextInt(0, maxSpawnableAmount);

        for(int i = 0; i <= amount; i++) {
            Location spawnLoc = getRandomSpawnLocation();
            if(!isSpawnable(spawnLoc))continue;
            Entity spawnedMob = spawningMob.spawn(spawnLoc);
            spawnedMobs.add(spawnedMob);
        }
    }

    private boolean isSpawnable(Location loc) {
        Block feetBlock = loc.getBlock();
        Block headBlock = loc.clone().add(0, 1, 0).getBlock();
        Block upperBlock = loc.clone().add(0, 2, 0).getBlock();
        return feetBlock.isPassable() && !feetBlock.isLiquid() && headBlock.isPassable() && !headBlock.isLiquid() && upperBlock.isPassable() && !upperBlock.isLiquid();
    }

    private Location getRandomSpawnLocation() {
        Location loc1 = region.getBound().getLoc1();
        Location loc2 = region.getBound().getLoc2();
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

    private MobHostile getSpawningMob() {
        for(MobHostile mob : mobs.keySet()) {
            MobCharacteristics mobCharacteristics = mobs.get(mob);
            double spawnChance = mobCharacteristics.getSpawnChance();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 101);
            if(randomNum <= spawnChance) return mob;
        }
        return null;
    }


    public MobHostile getMobById(int mobId) {
        for(MobHostile key : mobs.keySet()) {
            if(key.getId() == mobId) return key;
        }
        return null;
    }
    public MobCharacteristics getMobCharacteristicsById(int mobId) {
        for(MobHostile key : mobs.keySet()) {
            MobCharacteristics value = mobs.get(key);
            if(key.getId() == mobId) return value;
        }
        return null;
    }

    public Region getRegion() {
        return region;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public Map<MobHostile, MobCharacteristics> getMobs() {
        return mobs;
    }

    public void addMob(MobCharacteristics mobCharacteristics, MobHostile mob) {
        mobs.put(mob, mobCharacteristics);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if(active == false) ModuleRegions.removeActiveMobSpawnRegion(this);
        else ModuleRegions.addActiveMobSpawnRegion(this);
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setMaxMobs(int newMaxMobs) { maxMobs = newMaxMobs;
    }
}
