package net.marscraft.skyrpg.module.custommobs.inventory.invfunctions;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.inventory.invfunctions.InvFunction;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvFunctionMobItems extends InvFunction {

    private final ILogManager logger;
    private MobHostile hostileMob;

    public InvFunctionMobItems(ILogManager logger, MobHostile hostileMob) {
        super(logger);
        this.logger = logger;
        this.hostileMob = hostileMob;
    }

    public Inventory add(Inventory inv, int startRow) {
        int startIndex = getStartIndex(startRow);
        ItemStack[] invContents = inv.getContents();
        NamespacedKey keyMobItem = new NamespacedKey(Main.getPlugin(Main.class), "mobItem");

        int mainItemIndex = startIndex + 3;
        int helmetIndex = startIndex + 1;
        int chestplateIndex = helmetIndex + 9;
        int leggingsIndex = chestplateIndex + 9;
        int bootsIndex = leggingsIndex + 9;

        ItemStack helmet = buildMobItem(hostileMob.getArmor()[0], "helmet");
        ItemStack chestplate = buildMobItem(hostileMob.getArmor()[1], "chestplate");
        ItemStack leggings = buildMobItem(hostileMob.getArmor()[2], "leggings");
        ItemStack boots = buildMobItem(hostileMob.getArmor()[3], "boots");
        ItemStack mainItem = buildMobItem(hostileMob.getMainItem(), "mainItem");

        invContents[helmetIndex] = helmet;
        invContents[chestplateIndex] = chestplate;
        invContents[leggingsIndex] = leggings;
        invContents[bootsIndex] = boots;
        invContents[mainItemIndex] = mainItem;

        inv.setContents(invContents);
        return inv;
    }

    public ItemStack buildMobItem(ItemStack item, String itemName) {
        NamespacedKey keyMobItem = new NamespacedKey(Main.getPlugin(Main.class), "mobItem");
        if(item == null) return null;
        return new ItemBuilder(item)
                .addPersistantDataToItemStack(keyMobItem, itemName)
                .build()
                ;
    }
}
