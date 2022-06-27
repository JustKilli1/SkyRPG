package net.marscraft.skyrpg.module.regions;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import org.bukkit.entity.Player;

public class MessagesRegions extends MessageManager {

    public MessagesRegions(ILogManager logger, IConfigManager configManager, Player player) {
        super(logger, configManager, player);
    }

    public void sendMessageLocSet(int locNumber) {
        sendPlayerMessage("&aLocation " + locNumber + " Set");
    }
    public void sendSelectRegion() { sendPlayerMessage("&aBitte die Region markieren");}
    public void sendRegionCreated(String regionName) { sendPlayerMessage("&aRegion &c" + regionName + " &awurde erstellt."); }

    public void sendRegionCommandCreateSyntaxError() { sendPlayerSyntaxError("mr create [RegionName]"); }
    public void sendRegionCommandSyntaxError() { sendPlayerSyntaxError("mr help"); }

}
