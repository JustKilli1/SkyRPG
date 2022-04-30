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
import org.jetbrains.annotations.NotNull;

public class CommandTest implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        player.sendMessage("Hello World");
        NamespacedKey keyMobItem = new NamespacedKey(Main.getPlugin(Main.class), "mobItem");
        Inventory inv =  Bukkit.createInventory(null, InventoryType.PLAYER, "Mob Items");
        player.openInventory(inv);
        ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD).addPersistantDataToItemStack(keyMobItem, "mainItem").build();
        String itemStr = Utils.itemStackToBase64(item);
        ItemStack itemFromStr = Utils.itemStackFromBase64(itemStr);
        player.getInventory().addItem(itemFromStr);

        return false;
    }
}
