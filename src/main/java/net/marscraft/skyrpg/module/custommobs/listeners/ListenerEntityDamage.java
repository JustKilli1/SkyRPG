package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

public class ListenerEntityDamage implements Listener {

    private ILogManager logger;
    private DBHandlerCustomMobs dbHandler;

    public ListenerEntityDamage(ILogManager logger, DBHandlerCustomMobs dbHandler) {
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity rawEntity = event.getEntity();
        NamespacedKey keyCustomMob = new NamespacedKey(Main.getPlugin(Main.class), "customMob");

        if(!rawEntity.getPersistentDataContainer().has(keyCustomMob, PersistentDataType.INTEGER)) return;

        int mobId = rawEntity.getPersistentDataContainer().get(keyCustomMob, PersistentDataType.INTEGER);
        MobHostile mobHostile = dbHandler.getHostileMobById(mobId);
        if(mobHostile == null) return;
        if(rawEntity.isDead()) {
            rawEntity = mobHostile.setCurrentHealth(rawEntity, 0);
            mobHostile.tryDropLoot(rawEntity.getLocation());
            return;
        }
        LivingEntity entity = (LivingEntity) rawEntity;
        double damage = event.getFinalDamage(), health = entity.getHealth() + entity.getAbsorptionAmount();
        if (health > damage) {
            health -= damage;
            health = Math.ceil(health);
            rawEntity = mobHostile.setCurrentHealth(rawEntity, health);
        }
    }

}
