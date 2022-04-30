package net.marscraft.skyrpg.module.custommobs.commands;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
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
    private DBHandlerCustomMobs dbHandler;

    public CommandSpawnCustomMob(ILogManager logger, IConfigManager messagesConfigManager, DBHandlerCustomMobs dbHandler) {
        this.logger = logger;
        this.messagesConfigManager = messagesConfigManager;
        this.dbHandler = dbHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        NamespacedKey keyCustomMob = new NamespacedKey(Main.getPlugin(Main.class), "customMob");
        MessagesCustomMobs messages = new MessagesCustomMobs(logger, messagesConfigManager, player);
        if(args.length != 1) {
            messages.sendPlayerSyntaxError("spawncustommob [mobId]");
            return false;
        }
        int mobId = Utils.intFromStr(args[0]);
        if(mobId <= 0) {
            messages.sendInvalidMobIdMessage(args[0]);
            return false;
        }
        MobHostile hostileMob = dbHandler.getHostileMobById(mobId);
        if(hostileMob == null) {
            messages.sendMobIdNotFound(mobId);
            return false;
        }
        Location spawnLoc = player.getLocation();

        LivingEntity entity = hostileMob.spawn(spawnLoc);
        entity.getPersistentDataContainer().set(keyCustomMob, PersistentDataType.INTEGER, Integer.parseInt(args[0]));

        messages.sendMobSpawned(hostileMob.getName());

        return false;
    }
}
