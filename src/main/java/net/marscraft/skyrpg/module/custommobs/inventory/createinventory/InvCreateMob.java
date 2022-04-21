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

public class InvCreateMob extends MarsInventory implements IGuiInventory {

    private final String title = "MobType auswählen";
    private ILogManager logger;
    private MessagesCustomMobs messages;
    private MobHostile mob;

    public InvCreateMob(ILogManager logger, MessagesCustomMobs messages, MobHostile mob) {
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
    public void handleClickEvent(EventStorage eventStorage) {

        InventoryClickEvent event = eventStorage.getInventoryClickEvent();
        if(event == null) {
            logger.warn("InventoryClickEvent is null. Class: InvCreateMob, Method: handleClickEvent");
            return;
        }
        if(event.getClick() == ClickType.LEFT) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem == null) return;
            if (clickedItem.getItemMeta().getDisplayName().length() < 1) return;
            NamespacedKey keyMobType = new NamespacedKey(Main.getPlugin(Main.class), "mobType");
            if (!clickedItem.getItemMeta().getPersistentDataContainer().has(keyMobType, PersistentDataType.STRING)) return;
            String mobType = clickedItem.getItemMeta().getPersistentDataContainer().get(keyMobType, PersistentDataType.STRING).toUpperCase();
            EntityType type = EntityType.valueOf(mobType);
            mob.setType(type);
            player.closeInventory();
            messages.sendMobTypeSetMessage();
        }

    }

    @Override
    public boolean clickEventCanceled() {
        return true;
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
        mobTypes.add(buildMobTypeItem(Material.SKELETON_SPAWN_EGG, "Skelett", "Skeleton"));
        mobTypes.add(buildMobTypeItem(Material.SPIDER_SPAWN_EGG, "Spinne", "Spider"));
        mobTypes.add(buildMobTypeItem(Material.CREEPER_SPAWN_EGG, "Creeper", "Creeper", "§cHow dare you?!?!"));
        mobTypes.add(buildMobTypeItem(Material.ENDERMAN_SPAWN_EGG, "Enderman", "Enderman"));
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
