package net.marscraft.skyrpg.module.custommobs.mobs;

import net.marscraft.skyrpg.shared.logmanager.ILogManager;

public class MobManager {

    private ILogManager logger;

    public MobManager(ILogManager logger) {
        this.logger = logger;
    }

    protected MobHostile scaleMobByLevel(MobHostile mob, int Level) {
        return null;
    }

}
