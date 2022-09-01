package net.marscraft.skyrpg.commands;

import net.marscraft.skyrpg.shared.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandTest implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
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
        String[] lore = new String[14];
        lore[0] = "§6§lLegendär";
        lore[1] = "§c100 §aSchaden";
        lore[2] = "";
        lore[3] = "§a+10% Beute";
        lore[4] = "§a+10% XP";
        lore[5] = " ";
        lore[6] = "§c§lExplosiver Schlag";
        lore[7] = "§aHat eine §c40% §aChance beim Schlagen";
        lore[8] = "§aeine Explosion zu verursachen";
        lore[9] = "§adie §c30 §aSchaden an Gegnern in der Umgebung anrichtet";
        lore[10] = " ";
        lore[11] = "§c§lLebenzsentzug";
        lore[12] = "§aHat eine §c70% §aChance dem gegner §c10 §aLeben zu entziehen.";
        lore[13] = "§aDer träger erhält §c5 §aLeben zurück.";


        ItemStack testCustomItem = new ItemBuilder(Material.DIAMOND_SWORD).setDisplayname("§6§lTest Item").setLore(lore).build();

        player.getInventory().addItem(testCustomItem);

        return false;
    }
}
