package net.marscraft.skyrpg.module.custommobs.database;

import net.marscraft.skyrpg.module.custommobs.loot.LootItem;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.database.DBHandler;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHandlerCustomMobs extends DBHandler {

    private final ILogManager logger;
    private DBAccessLayerCustomMobs sql;

    public DBHandlerCustomMobs(ILogManager logger, DBAccessLayerCustomMobs sql) {
        super(logger, sql);
        this.logger = logger;
        this.sql = sql;
    }

    /**
     * Gets Last Id from CustomMobs Table
     * */
    public int getLastMobId() {
        ResultSet rs = sql.getLastMob();
        if(rs == null) return 0;

        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }

    /**
     * Gets HostileMob by CustomMobId
     * */
    public @Nullable MobHostile getHostileMobById(int id) {
        ResultSet rs = sql.getCustomMobById(id);

        if (rs == null) return null;
        try {
            if (!rs.next()) return null;

            String name = rs.getString("Name");
            EntityType entityType = EntityType.valueOf(rs.getString("EntityType"));
            double maxHealth = rs.getDouble("MaxHealth");
            double spawnChance = rs.getDouble("SpawnChance");
            int level = rs.getInt("Level");

            ItemStack[] armor = new ItemStack[4];
            armor[0] = Utils.itemStackFromBase64(rs.getString("Helmet"));
            armor[1] = Utils.itemStackFromBase64(rs.getString("Chestplate"));
            armor[2] = Utils.itemStackFromBase64(rs.getString("Leggings"));
            armor[3] = Utils.itemStackFromBase64(rs.getString("Boots"));

            ItemStack mainItem = Utils.itemStackFromBase64(rs.getString("MainItem"));
            List<LootItem> lootItems = getAllLootItemsByMobId(id);

            return new MobHostile(logger, name, id, level, maxHealth, spawnChance, entityType, mainItem, armor, lootItems);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    /**
     * Gets All LootItems with CustomMobId
     * */
    public @Nullable List<LootItem> getAllLootItemsByMobId(int id) {
        ResultSet rs = sql.getLootItemsByMobId(id);
        List<LootItem> lootItems = new ArrayList<>();

        if(rs == null) return null;

        try {
            while(rs.next()) {
                ItemStack item = Utils.itemStackFromBase64(rs.getString("Item"));
                int amountMin = rs.getInt("AmountMin");
                int amountMax = rs.getInt("AmountMax");
                double dropChance = rs.getDouble("DropChance");
                LootItem lootItem = new LootItem(item, amountMin, amountMax, dropChance);
                lootItems.add(lootItem);
            }
            return lootItems;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

}
