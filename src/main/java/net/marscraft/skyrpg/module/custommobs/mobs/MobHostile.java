package net.marscraft.skyrpg.module.custommobs.mobs;

import net.marscraft.skyrpg.module.custommobs.loot.LootItem;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MobHostile {

    private ILogManager logger;
    private String name;
    private int id;
    private double currentHealth, baseHealth, spawnChance;
    private EntityType type;
    private ItemStack mainItem;
    private ItemStack[] armor;
    private List<LootItem> loot;
    private boolean active;

    public MobHostile(ILogManager logger, int id, String name) {
        this.logger = logger;
        this.id = id;
        this.name = name;
    }

    public MobHostile(ILogManager logger, String name, int id, double baseHealth, double spawnChance, EntityType type) {
        this.logger = logger;
        this.name = name;
        this.id = id;
        this.baseHealth = baseHealth;
        this.spawnChance = spawnChance;
        this.type = type;
    }

    public MobHostile(ILogManager logger, String name, int id, double baseHealth, double spawnChance, EntityType type, ItemStack mainItem, ItemStack[] armor, List<LootItem> loot, boolean active) {
        this.logger = logger;
        this.name = name;
        this.id = id;
        this.baseHealth = baseHealth;
        this.spawnChance = spawnChance;
        this.type = type;
        this.mainItem = mainItem;
        this.armor = armor;
        this.loot = loot;
        this.active = active;
    }

    /**
     * Entity gets spawned at Location
     * @param loc Location where Mob gets spawned
     * @return The Spawned Mob as LivingEntity
     * */
    public LivingEntity spawn(Location loc) {
        LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);

        //entity.setVisualFire(true);
        entity.setCustomNameVisible(true);
        entity.setCustomName(buildCustomName(baseHealth));
        //entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth);
        //entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(5.0);
        entity.setHealth(baseHealth);
        EntityEquipment inv = entity.getEquipment();
        if (armor != null) inv.setArmorContents(armor);
        inv.setHelmetDropChance(0f);
        inv.setChestplateDropChance(0f);
        inv.setLeggingsDropChance(0f);
        inv.setBootsDropChance(0f);
        inv.setItemInMainHand(mainItem);
        inv.setItemInMainHandDropChance(0f);

        return entity;
    }

    /**
     * Trys to drop Loot Items
     * @param location Location where the items get dropped
     */
    public void tryDropLoot(Location location) {
        for (LootItem item : loot) {
            item.tryDropItem(location);
        }
    }

    public boolean setupComplete() {
        if(id == 0) return false;
        if(name == null) return false;
        if(baseHealth == 0) return false;
        if(spawnChance == 0) return false;
        return type != null;

    }

    private Entity updateHealth(Entity entity) {
        entity.setCustomName(buildCustomName(currentHealth));
        return entity;
    }

    /**
     * Builds CustomName with given health
     * @param health Current Health from Mob
     * @return Formatted CustomName
     */
    private String buildCustomName(double health) { return "§c" + name + " §a" + (int) health + "/" + (int) baseHealth + "§c❤"; }

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseHealth() { return baseHealth; }
    public void setBaseHealth(double baseHealth) { this.baseHealth = baseHealth; }

    public void setSpawnChance(double spawnChance) { this.spawnChance = spawnChance; }
    public double getSpawnChance() { return spawnChance; }

    public List<LootItem> getLoot() { return loot; }
    public void setLoot(List<LootItem> loot) { this.loot = loot; }

    public EntityType getType() { return type; }
    public void setType(EntityType type) { this.type = type; }

    public ItemStack getMainItem() { return mainItem; }
    public void setMainItem(ItemStack mainItem) { this.mainItem = mainItem; }

    public ItemStack[] getArmor() { return armor; }
    public void setArmor(ItemStack[] armor) { this.armor = armor; }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public Entity setCurrentHealth(Entity entity, double currentHealth) {
        this.currentHealth = currentHealth;
        return updateHealth(entity);
    }
}
