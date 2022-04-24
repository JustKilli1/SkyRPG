package net.marscraft.skyrpg.module.custommobs.commands;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CommandSpawnCustomMob implements CommandExecutor {

    private ILogManager logger;
    private IConfigManager messagesConfigManager;

    public CommandSpawnCustomMob(ILogManager logger, IConfigManager messagesConfigManager) {
        this.logger = logger;
        this.messagesConfigManager = messagesConfigManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        NamespacedKey keyCustomMob = new NamespacedKey(Main.getPlugin(Main.class), "customMob");
        MessageManager messageManager = new MessageManager(logger, messagesConfigManager, player);
        Location spawnLoc = player.getLocation();

        MobHostile hostileMob = new MobHostile(logger, "Test1", 1, 30.0, 80.0, org.bukkit.entity.EntityType.ZOMBIE);
        hostileMob.setMainItem(new ItemStack(Material.DIAMOND_SWORD));

        LivingEntity entity = hostileMob.spawn(spawnLoc);
        entity.getPersistentDataContainer().set(keyCustomMob, PersistentDataType.INTEGER, Integer.parseInt(args[0]));

        messageManager.sendPlayerMessage("Mob gespawnt!");

        return false;
    }
}
