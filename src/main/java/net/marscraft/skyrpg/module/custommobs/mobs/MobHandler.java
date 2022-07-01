package net.marscraft.skyrpg.module.custommobs.mobs;

import net.marscraft.skyrpg.shared.logmanager.ILogManager;

public class MobHandler {

    private ILogManager logger;
    private final double standardLevelMulti = 0.1;

    public MobHandler(ILogManager logger) {
        this.logger = logger;
    }

    public MobHostile scaleMobByLevel(MobHostile mob, int level) {
        double multi = mob.getLevelMultiplier() <= 0 ? standardLevelMulti : mob.getLevelMultiplier();
        double statMultiplier = (level * multi);
        double newMobHealth = Math.ceil((mob.getBaseHealth() * statMultiplier) + mob.getBaseHealth());
        mob.setScaledHealth(newMobHealth);

        return mob;
    }

}
