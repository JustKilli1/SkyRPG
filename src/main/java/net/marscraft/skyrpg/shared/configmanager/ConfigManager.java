package net.marscraft.skyrpg.shared.configmanager;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager implements IConfigManager {

    private Main plugin;
    private String configName;
    private ILogManager logger;

    private FileConfiguration fileConfiguration = null;
    private File file;

    public ConfigManager(Main plugin, ILogManager logger, String configName){
        this.plugin = plugin;
        this.configName = configName;
        this.logger = logger;

        file = new File(plugin.getDataFolder() + "/", configName + ".yml");
        if (!plugin.getDataFolder().exists()) {
            logger.info("Config Folder is missing, create Folder...");
            plugin.getDataFolder().mkdir();
        }

        if(!file.exists()) {
            try {
                logger.info("Module Config is missing... Creating Folder and File...");
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                logger.error("Error while creating File and Folder", e);
            }
        }
        reloadConfig();
    }

    @Override
    public FileConfiguration getConfiguration() {
        return fileConfiguration;
    }

    @Override
    public void saveConfig() {
        try {
            logger.info("Saving Configuration...");
            fileConfiguration.save(file);
        } catch (IOException e) {
            logger.error("Error while Saving Configurationfile", e);
        }
    }

    @Override
    public void reloadConfig() {
        logger.info("Loading Configuration...");
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

}
