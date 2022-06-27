package net.marscraft.skyrpg.module.custommobs.commands;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.mobs.MobHostile;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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


        MessagesCustomMobs messages = new MessagesCustomMobs(logger, messagesConfigManager, player);
        if(args.length != 2) {
            messages.sendPlayerSyntaxError("spawncustommob [mobId] [amount]");
            return false;
        }
        int mobId = Utils.intFromStr(args[0]);
        int amount = Utils.intFromStr(args[1]);
        if(mobId <= 0) {
            messages.sendInvalidMobIdMessage(args[0]);
            return false;
        }
        if(amount <= 0) {
            messages.sendInvalidAmountMessage(args[1]);
            return false;
        }
        MobHostile hostileMob = dbHandler.getHostileMobById(mobId);
        if(hostileMob == null) {
            messages.sendMobIdNotFound(mobId);
            return false;
        }
        Location spawnLoc = player.getLocation();

        for(int i = 0; i < amount; i++) hostileMob.spawn(spawnLoc);
        messages.sendMobSpawned(hostileMob.getName(), amount);

        return false;
    }
}
