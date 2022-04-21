package net.marscraft.skyrpg.module.custommobs.mobs;

import net.marscraft.skyrpg.module.custommobs.loot.LootItem;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MobHostile {

    private ILogManager logger;
    private String name;
    private int id, level;
    private double maxHealth, spawnChance;
    private EntityType type;
    private ItemStack mainItem;
    private ItemStack[] armor;
    private List<LootItem> loot;

    public MobHostile(ILogManager logger, int id, String name) {
        this.logger = logger;
        this.id = id;
        this.name = name;
    }

    public MobHostile(ILogManager logger, String name, int id, double maxHealth, double spawnChance, EntityType type) {
        this.logger = logger;
        this.name = name;
        this.id = id;
        this.maxHealth = maxHealth;
        this.spawnChance = spawnChance;
        this.type = type;
    }

    public MobHostile(ILogManager logger, String name, int id, int level, double maxHealth, double spawnChance, EntityType type, ItemStack mainItem, ItemStack[] armor, List<LootItem> loot) {
        this.logger = logger;
        this.name = name;
        this.id = id;
        this.level = level;
        this.maxHealth = maxHealth;
        this.spawnChance = spawnChance;
        this.type = type;
        this.mainItem = mainItem;
        this.armor = armor;
        this.loot = loot;
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
        entity.setCustomName("§a" + name + " §r§c" + (int) maxHealth + "/" + (int) maxHealth + "❤");
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(5.0);
        entity.setHealth(maxHealth);
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

    public void tryDropLoot(Location location) {
        for (LootItem item : loot) {
            item.tryDropItem(location);
        }
    }

    public boolean setupComplete() {
        if(id == 0) return false;
        if(name == null) return false;
        if(maxHealth == 0) return false;
        return type != null;

    }
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public double getMaxHealth() { return maxHealth; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }

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
}
