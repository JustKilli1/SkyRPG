package net.marscraft.skyrpg.module.customitems;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import org.bukkit.entity.Player;

public class MessagesCustomItems extends MessageManager {

    public MessagesCustomItems(ILogManager logger, IConfigManager configManager, Player player) {
        super(logger, configManager, player);
    }

}
