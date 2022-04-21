package net.marscraft.skyrpg.shared.configmanager;

import org.bukkit.configuration.file.FileConfiguration;

public interface IConfigManager {

    FileConfiguration getConfiguration();
    void saveConfig();
    void reloadConfig();

}
