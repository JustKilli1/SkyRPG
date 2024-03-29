package net.marscraft.skyrpg.module.custommobs.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InvFunctionShowMobOverview extends InvFunction {

    private final ILogManager logger;
    private DBHandlerCustomMobs dbHandler;
    private int startPos, endPos;

    public InvFunctionShowMobOverview(ILogManager logger, DBHandlerCustomMobs dbHandler, int startPos, int endPos) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    /**
     * Adds Function to inventory
     * */
    public @Nullable Inventory add(Inventory inv, boolean active) {
        ItemStack[] invContents = inv.getContents();
        List<MobHostile> hostileMobs = dbHandler.getAllHostileMobs(active);
        if(endPos > invContents.length - 1) return null;
        invContents = setTopControlBar(invContents, active);
        int hostileMobIndex = 0;
        // startPos + 9 --> Because first row is reserved for filter
        for(int i = startPos + 9; i <= endPos; i++) {
            if(hostileMobIndex == hostileMobs.size()) break;
            MobHostile hostileMob = hostileMobs.get(hostileMobIndex);
            if(hostileMob == null) continue;
            ItemStack displayItem = getCustomMobDisplayItem(hostileMob.getType().toString(), hostileMob.getName(), hostileMob.getId(), hostileMob.getBaseHealth(), hostileMob.getSpawnChance());
            invContents[i] = displayItem;
            hostileMobIndex++;

        }
        inv.setContents(invContents);
        return inv;
    }

    private ItemStack[] setTopControlBar(ItemStack[] invContents, boolean active) {
        NamespacedKey keyFilterName = new NamespacedKey(Main.getPlugin(Main.class), "filterName");
        NamespacedKey keyFilterActive = new NamespacedKey(Main.getPlugin(Main.class), "filterActive");
        ItemStack activeFilter = !active ? new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                                                    .setDisplayname("Active")
                                                    .addPersistantDataToItemStack(keyFilterName, "active")
                                                    .build()
                                            :
                                            new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                                                    .setDisplayname("Active")
                                                    .addPersistantDataToItemStack(keyFilterActive, "active")
                                                    .build()
                                            ;

        ItemStack inactiveFilter = active ? new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                                                    .setDisplayname("Inactive")
                                                    .addPersistantDataToItemStack(keyFilterName, "inactive")
                                                    .build()
                                              :
                                              new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                                                    .setDisplayname("Inactive")
                                                    .addPersistantDataToItemStack(keyFilterActive, "inactive")
                                                    .build()
                                             ;
        invContents[startPos] = activeFilter;
        invContents[startPos + 1] = inactiveFilter;
        return invContents;
    }

    private Inventory changeActiveFilter(Inventory inv, boolean active) {
        return add(inv, active);
    }

}
