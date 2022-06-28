package net.marscraft.skyrpg.module.regions.region.mobspawnregion;

import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.module.regions.region.Region;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

import java.util.Map;

public class MobSpawnRegion {

    private final ILogManager logger;
    private int id;
    private Region region;
    private int maxMobs;
    private Map<MobHostile, Double> mobs;
    private boolean active;

    public MobSpawnRegion(ILogManager logger, int id, Region region, int maxMobs, Map<MobHostile, Double> mobs, boolean active) {
        this.logger = logger;
        this.id = id;
        this.region = region;
        this.maxMobs = maxMobs;
        this.mobs = mobs;
        this.active = active;
    }

    public void spawnMobs() {
        if(mobs == null) return;
    }

    public Region getRegion() {
        return region;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public Map<MobHostile, Double> getMobs() {
        return mobs;
    }

    public void addMob(Double spawnChance, MobHostile mob) {
        mobs.put(mob, spawnChance);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }
}
