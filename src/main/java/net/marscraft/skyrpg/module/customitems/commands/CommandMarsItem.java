package net.marscraft.skyrpg.module.customitems.commands;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.ModuleCustomItems;
import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.module.customitems.customitem.ItemRarity;
import net.marscraft.skyrpg.module.customitems.database.DBAccesLayerCustomItems;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.module.customitems.setups.SetupCustomItemItemType;
import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandMarsItem implements CommandExecutor {

    private final ILogManager logger;
    private DBHandlerCustomItems dbHandler;
    private IConfigManager messagesConfig;
    private DBAccesLayerCustomItems sql;

    public CommandMarsItem(ILogManager logger, DBHandlerCustomItems dbHandler, IConfigManager messagesConfig, DBAccesLayerCustomItems sql) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.messagesConfig = messagesConfig;
        this.sql = sql;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        MessagesCustomItems messages = new MessagesCustomItems(logger, messagesConfig, player);


        if(args.length == 0) {
            //Inventory opens with all CustomItems that got Generated or Created by Admin
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if(args.length < 2) {
                    messages.sendPlayerSyntaxError("mi [create] [ItemName] (ItemRarityId)");
                    return false;
                }

                ItemRarity itemRarity = ItemRarity.valueOf(Utils.intFromStr(args[args.length - 1]));
                String itemName = ChatColor.translateAlternateColorCodes('&', Utils.getStrFromArray(args, 1));
                int itemId = dbHandler.getLastCustomItemId() + 1;

                if(itemRarity != null) itemName = itemName.substring(0, itemName.length() - 1);
                else itemRarity = ItemRarity.EINZIGARTIG;
                CustomItem customItem = new CustomItem(logger, dbHandler, itemId, itemName, itemRarity);
                sql.insertCustomItem(customItem);
                ISetup setup = new SetupCustomItemItemType(logger, customItem, messages, dbHandler, sql);
                setup.handleCommands(player, args);
                ModuleCustomItems.addSetup(player.getUniqueId(), setup);
                break;
            case "edit":
                break;
            default:
                messages.sendPlayerSyntaxError("mi or /mi [create, edit]");
                break;
        }

        return false;
    }

}
