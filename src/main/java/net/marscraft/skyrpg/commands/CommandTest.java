package net.marscraft.skyrpg.commands;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.ItemBuilder;
import net.marscraft.skyrpg.shared.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CommandTest implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        player.sendMessage("Hello World");
/*        NamespacedKey keyMobItem = new NamespacedKey(Main.getPlugin(Main.class), "mobItem");
        Inventory inv =  Bukkit.createInventory(null, InventoryType.PLAYER, "Mob Items");
        player.openInventory(inv);
        ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD).addPersistantDataToItemStack(keyMobItem, "mainItem").build();
        String itemStr = Utils.itemStackToBase64(item);
        ItemStack itemFromStr = Utils.itemStackFromBase64(itemStr);
        player.getInventory().addItem(itemFromStr);*/

        /*
        * Einzigartig(Siehe 1)/Legendär/Episch/Normal/Ungewöhnlich
        * 100 Schaden
        *
        * Effekte
        * +10% Beute
        * +10% XP
        *
        * Besondere Fähigkeiten
        * Explosiver Schlag
        * Hat eine 40% Chance beim Schlagen
        * eine Explosion zu verursachen
        * die 30 Schaden an Gegner in der Umgebung macht
        *
        * Lebenzsentzug
        * Hat eine 40% Chance dem gegner 10 Leben zu entziehen.
        * Der träger erhält 5 Leben zurück.
        *
        *
        *
        *
        * 1: Einzigartige waffe die speziell erstellt wurde über create CustomItem command.
        * */


        String[] lore = new String[13];
        lore[0] = "§c100 §aSchaden";
        lore[1] = "";
        lore[2] = "§a+10% Beute";
        lore[3] = "§a+10% XP";
        lore[4] = " ";
        lore[5] = "§c§lExplosiver Schlag";
        lore[6] = "§aHat eine §c40% §aChance beim Schlagen";
        lore[7] = "§aeine Explosion zu verursachen";
        lore[8] = "§adie §c30 §aSchaden an Gegnern in der Umgebung anrichtet";
        lore[9] = " ";
        lore[10] = "§c§lLebenzsentzug";
        lore[11] = "§aHat eine §c40% §aChance dem gegner §c10 §aLeben zu entziehen.";
        lore[12] = "§aDer träger erhält §c5 §aLeben zurück.";


        ItemStack testCustomItem = new ItemBuilder(Material.DIAMOND_SWORD).setDisplayname("§6§lZerstörer der Welten").setLore(lore).build();

        player.getInventory().addItem(testCustomItem);


        return false;
    }
}
