package net.marscraft.skyrpg.module.custommobs.inventory.createinventory;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class InvSelectMobType extends MarsInventory implements IGuiInventory {

    public final static String title = "MobType auswählen";
    private ILogManager logger;
    private MessagesCustomMobs messages;
    private MobHostile mob;

    public InvSelectMobType(ILogManager logger, MessagesCustomMobs messages, MobHostile mob) {
        super(logger);
        this.logger = logger;
        this.messages = messages;
        this.mob = mob;
    }


    @Override
    public Inventory build() {
        Inventory inv = buildBaseInventory(title, 6);
        ItemStack[] invContents = inv.getContents();

        List<ItemStack> mobTypes = getMobTypes();
        for(int i = 0; i < mobTypes.size(); i++) {
            invContents[i] = mobTypes.get(i);
        }
        inv.setContents(invContents);
        return inv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        player.openInventory(inv);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {

        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        if(event == null) {
            logger.warn("InventoryClickEvent is null.");
            return null;
        }
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        if(event.getClick() == ClickType.LEFT) {


            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem == null) return null;
            if (clickedItem.getItemMeta().getDisplayName().length() < 1) return null;
            NamespacedKey keyMobType = new NamespacedKey(Main.getPlugin(Main.class), "mobType");
            if (!clickedItem.getItemMeta().getPersistentDataContainer().has(keyMobType, PersistentDataType.STRING)) return null;
            String mobType = clickedItem.getItemMeta().getPersistentDataContainer().get(keyMobType, PersistentDataType.STRING).toUpperCase();
            EntityType type = EntityType.valueOf(mobType);
            mob.setType(type);
            player.closeInventory();
            messages.sendMobTypeSetMessage();
            return (T) mob;
        }
        return null;
    }

    @Override
    public String getTitle() { return title; }

    /**
     * Creates Items that gets Displayed in Inventory and returns them as List. Position in List = Position in Inventory
     * @return List with created Items
     * */
    private List<ItemStack> getMobTypes() {
        List<ItemStack> mobTypes = new ArrayList<>();
        mobTypes.add(buildMobTypeItem(Material.ZOMBIE_SPAWN_EGG, "Zombie", "Zombie"));
        mobTypes.add(buildMobTypeItem(Material.ZOMBIE_VILLAGER_SPAWN_EGG, "Zombie Villager", "Zombie_Villager"));
        mobTypes.add(buildMobTypeItem(Material.SKELETON_SPAWN_EGG, "Skelett", "Skeleton"));
        mobTypes.add(buildMobTypeItem(Material.SPIDER_SPAWN_EGG, "Spinne", "Spider"));
        mobTypes.add(buildMobTypeItem(Material.CAVE_SPIDER_SPAWN_EGG, "Höhlenspinne", "Cave_Spider"));
        mobTypes.add(buildMobTypeItem(Material.CREEPER_SPAWN_EGG, "Creeper", "Creeper", "§cHow dare you?!?!"));
        mobTypes.add(buildMobTypeItem(Material.ENDERMAN_SPAWN_EGG, "Enderman", "Enderman"));
        mobTypes.add(buildMobTypeItem(Material.WITCH_SPAWN_EGG, "Hexe", "Witch"));
        mobTypes.add(buildMobTypeItem(Material.SLIME_SPAWN_EGG, "Schleim", "Slime"));
        mobTypes.add(buildMobTypeItem(Material.PILLAGER_SPAWN_EGG, "Pillager", "Pillager"));
        mobTypes.add(buildMobTypeItem(Material.STRAY_SPAWN_EGG, "Stray", "Stray"));
        mobTypes.add(buildMobTypeItem(Material.SHULKER_SPAWN_EGG, "Shulker", "Shulker"));
        mobTypes.add(buildMobTypeItem(Material.HUSK_SPAWN_EGG, "Husk", "Husk"));
        mobTypes.add(buildMobTypeItem(Material.MAGMA_CUBE_SPAWN_EGG, "Magma Cube", "Magma_Cube"));
        mobTypes.add(buildMobTypeItem(Material.WITHER_SKELETON_SPAWN_EGG, "Wither Skeleton", "Wither_Skeleton"));
        mobTypes.add(buildMobTypeItem(Material.GHAST_SPAWN_EGG, "Ghast", "Ghast"));
        mobTypes.add(buildMobTypeItem(Material.BLAZE_SPAWN_EGG, "Blaze", "Blaze"));
        mobTypes.add(buildMobTypeItem(Material.PIGLIN_SPAWN_EGG, "Piglin", "Piglin"));
        mobTypes.add(buildMobTypeItem(Material.PIGLIN_BRUTE_SPAWN_EGG, "Piglin Brute", "Piglin_Brute"));
        mobTypes.add(buildMobTypeItem(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, "Zombified Piglin", "Zombified_Piglin"));
        mobTypes.add(buildMobTypeItem(Material.HOGLIN_SPAWN_EGG, "Hoglin", "Hoglin"));
        mobTypes.add(buildMobTypeItem(Material.ENDERMITE_SPAWN_EGG, "Endermite", "Endermite"));
        mobTypes.add(buildMobTypeItem(Material.VEX_SPAWN_EGG, "Vex", "Vex"));
        return mobTypes;

    }

    /**
     * Builds Displayed Item in Inventory
     * */
    private ItemStack buildMobTypeItem(Material mat, String name, String mobType, String... lore) {
        NamespacedKey keyMobType = new NamespacedKey(Main.getPlugin(Main.class), "mobType");
        return new ItemBuilder(mat).setDisplayname(name).setLore(lore).addPersistantDataToItemStack(keyMobType, mobType).build();
    }

}
