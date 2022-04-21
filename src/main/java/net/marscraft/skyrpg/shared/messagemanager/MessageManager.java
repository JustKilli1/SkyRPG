package net.marscraft.skyrpg.shared.messagemanager;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {

    protected final ILogManager logger;
    protected IConfigManager configManager;
    protected FileConfiguration messageConfig;
    protected String prefix;
    protected Player player;

    /**
     * Übernimmt Kommunikation Mit Spielern
     * Lädt Prefix aus Config
     * */

    public MessageManager(ILogManager logger, IConfigManager configManager, Player player) {
        this.logger = logger;
        this.configManager = configManager;
        messageConfig = this.configManager.getConfiguration();
        this.player = player;
        prefix = messageConfig.getString("prefix");
    }

    public void sendPlayerMessage(String message) { player.sendMessage(formatMessage(message)); }

    public void sendPlayerSyntaxError(String rightCommand) {
        player.sendMessage(formatMessage("&4Syntax error use: &e/" + rightCommand));
    }

    public void sendNotEnoughInvSpaceMessage() {
        player.sendMessage(formatMessage("&4Not enough Space in Inventory"));
    }
    public Player getPlayer() { return player; }

    private String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }





}
