package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ListenerEntityDamage implements Listener {

    private ILogManager logger;
    private DBHandlerCustomMobs dbHandler;
    public static Map<Entity, Integer> indicators = new HashMap<>();
    private DecimalFormat formatter = new DecimalFormat("#.##");

    public ListenerEntityDamage(ILogManager logger, DBHandlerCustomMobs dbHandler) {
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity rawEntity = event.getEntity();
        NamespacedKey keyCustomMob = new NamespacedKey(Main.getPlugin(Main.class), "customMob");
        NamespacedKey keyCustomMobScaledHealth = new NamespacedKey(Main.getPlugin(Main.class), "scaledHealth");
        NamespacedKey keyCustomMobLevel = new NamespacedKey(Main.getPlugin(Main.class), "Level");

        if(!rawEntity.getPersistentDataContainer().has(keyCustomMob, PersistentDataType.INTEGER)) return;

        int mobId = rawEntity.getPersistentDataContainer().get(keyCustomMob, PersistentDataType.INTEGER);
        MobHostile mobHostile = dbHandler.getHostileMobById(mobId);
        if(mobHostile == null) return;
        if(!rawEntity.getPersistentDataContainer().has(keyCustomMobScaledHealth, PersistentDataType.DOUBLE) && !rawEntity.getPersistentDataContainer().has(keyCustomMobLevel, PersistentDataType.INTEGER)) return;
        double scaledHealth = rawEntity.getPersistentDataContainer().get(keyCustomMobScaledHealth, PersistentDataType.DOUBLE);
        int mobLevel = rawEntity.getPersistentDataContainer().get(keyCustomMobLevel, PersistentDataType.INTEGER);
        mobHostile.setScaledHealth(scaledHealth);
        mobHostile.setMobLevel(mobLevel);
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
        World world = entity.getWorld();
        Location loc = entity.getLocation().clone().add(Utils.getRandomOffset(), 1, Utils.getRandomOffset());
        world.spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName("§c" + formatter.format(damage) + "☼");
            indicators.put(armorStand, 20);
        });
    }

}
