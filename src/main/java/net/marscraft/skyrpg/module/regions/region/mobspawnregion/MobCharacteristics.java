package net.marscraft.skyrpg.module.regions.region.mobspawnregion;

public class MobCharacteristics {

    private double spawnChance;
    private int controllerId, levelMin, levelMax;

    public MobCharacteristics(double spawnChance, int controllerId, int levelMin, int levelMax) {
        this.spawnChance = spawnChance;
        this.controllerId = controllerId;
        this.levelMin = levelMin;
        this.levelMax = levelMax;
    }

    public int getLevelMin() {
        return levelMin;
    }

    public void setLevelMin(int levelMin) {
        this.levelMin = levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }

    public void setLevelMax(int levelMax) {
        this.levelMax = levelMax;
    }

    public double getSpawnChance() {
        return spawnChance;
    }

    public void setSpawnChance(double spawnChance) {
        this.spawnChance = spawnChance;
    }

    public int getControllerId() {
        return controllerId;
    }
}
