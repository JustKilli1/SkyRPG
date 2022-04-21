package net.marscraft.skyrpg.module.custommobs.commands;

import net.marscraft.skyrpg.module.custommobs.MessagesCustomMobs;
import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBAccessLayerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.custommobs.setups.SetupCustomMob;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMarsMob implements CommandExecutor {

    private ILogManager logger;
    private DBAccessLayerCustomMobs sql;
    private DBHandlerCustomMobs dbHandler;
    private IConfigManager messagesConfig;


    public CommandMarsMob(ILogManager logger, DBAccessLayerCustomMobs sql, DBHandlerCustomMobs dbHandler, IConfigManager messagesConfig) {
        this.logger = logger;
        this.sql = sql;
        this.dbHandler = dbHandler;
        this.messagesConfig = messagesConfig;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        MessagesCustomMobs messages = new MessagesCustomMobs(logger, messagesConfig, player);

        if(args.length == 0) {
            showMarsMobsOverviewInv(player);
            return true;
        }

        switch (args[0]) {
            case "create":
                if(args.length == 1) {
                    messages.sendPlayerSyntaxError("mm [create] [MobName]");
                    return false;
                }
                String mobName = Utils.getStrFromArray(args, 1);
                int newMobId = dbHandler.getLastMobId() + 1;
                ISetup setup = new SetupCustomMob(logger, messages, sql, newMobId, mobName);
                setup.handleCommands(player, args);
                ModuleCustomMobs.addSetup(player.getUniqueId(), setup);
                break;
            case "edit":
                showMarsMobsEditInv(player);
                break;
            default:
                messages.sendPlayerSyntaxError("mm [create, edit]");
                break;
        }

        return false;
    }

    /**
     * Opens MarsOverview Inventory
     * @return true if success
     * */
    private boolean showMarsMobsOverviewInv(Player player) {

        return true;
    }


    /**
     * Opens MarsMobsCreate Inventory
     * @return true if success
     * */
    private boolean showMarsMobsEditInv(Player player) {

        return true;
    }

}
