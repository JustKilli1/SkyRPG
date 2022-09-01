package net.marscraft.skyrpg.module.regions.inventory.invfunctions.mobspawnregion;

import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvFunctionShowCustomMobs extends InvFunction {
    private final ILogManager logger;
    private DBHandlerCustomMobs dbHandlerCustomMobs;

    public InvFunctionShowCustomMobs(ILogManager logger, DBHandlerCustomMobs dbHandlerCustomMobs) {
        super(logger);
        this.logger = logger;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
    }

    public Inventory add(Inventory inv, int startRow, int endRow) {
        ItemStack[] invContents = inv.getContents();
        int startIndex = getStartIndex(startRow);
        int endIndex = getEndIndex(endRow);
        List<MobHostile> mobs = dbHandlerCustomMobs.getAllHostileMobs(true);
        List<ItemStack> mobItems = getMobDisplayItems(mobs);
        int x = 0;
        for(int i = startIndex; i <= endIndex; i++, x++) {
            if(x >= mobItems.size()) break;
            invContents[i] = mobItems.get(x);
        }

        inv.setContents(invContents);
        return inv;
    }

    public List<ItemStack> getMobDisplayItems(List<MobHostile> mobs) {
        List<ItemStack> mobItems = new ArrayList<>();
        for(MobHostile mob : mobs) {
            ItemStack item = getCustomMobDisplayItem(mob.getType().toString(), mob.getName(), mob.getId(), mob.getBaseHealth(), mob.getSpawnChance());
            mobItems.add(item);
        }
        return mobItems;
    }

}
