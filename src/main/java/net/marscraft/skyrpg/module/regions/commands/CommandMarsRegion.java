package net.marscraft.skyrpg.module.regions.commands;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.module.custommobs.database.DBHandlerCustomMobs;
import net.marscraft.skyrpg.module.regions.MessagesRegions;
import net.marscraft.skyrpg.module.regions.ModuleRegions;
import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.InvRegions;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import net.marscraft.skyrpg.module.regions.setups.SetupMarsRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMarsRegion implements CommandExecutor {


    private final ILogManager logger;
    private Main plugin;
    private IConfigManager configManager;
    private DBHandlerRegions dbHandler;
    private DBAccessLayerRegions sql;
    DBHandlerCustomMobs dbHandlerCustomMobs;


    public CommandMarsRegion(ILogManager logger, Main plugin, IConfigManager configManager, DBAccessLayerRegions sql, DBHandlerCustomMobs dbHandlerCustomMobs) {
        this.logger = logger;
        this.plugin = plugin;
        this.configManager = configManager;
        this.sql = sql;
        this.dbHandlerCustomMobs = dbHandlerCustomMobs;
        dbHandler = new DBHandlerRegions(this.logger, this.sql, dbHandlerCustomMobs);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        MessagesRegions messages = new MessagesRegions(logger, configManager, player);

        if(args.length == 0) {
            showRegionOverview(player, messages);
            return true;
        }

        switch (args[0]) {
            case "create":

                if(args.length < 2) {
                    messages.sendRegionCommandCreateSyntaxError();
                    return true;
                }
                String regionName = Utils.getStrFromArray(args, 1);
                ISetup setup = new SetupMarsRegion(logger, regionName, sql, messages, dbHandlerCustomMobs);
                ModuleRegions.addSetup(player.getUniqueId(), setup);
                setup.handleCommands(player, args);
                break;
            default:
                messages.sendRegionCommandSyntaxError();
                break;
        }


        return false;
    }

    /**
     * Opens MarsRegions Inventory
     * @param player Player who sees the Inventory
     * */
    private void showRegionOverview(Player player, MessagesRegions messages) {
        IGuiInventory inv = new InvRegions(logger, dbHandler, sql, messages);
        inv.open(player);
        ModuleRegions.addInv(player.getUniqueId(), inv);
    }
}
