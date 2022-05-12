package net.marscraft.skyrpg.module.customitems.database;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.database.DBAccessLayer;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

public class DBAccesLayerCustomItems extends DBAccessLayer {

    private final ILogManager logger;

    public DBAccesLayerCustomItems(ILogManager logger, IConfigManager mysqlConfig) {
        super(logger, mysqlConfig);
        this.logger = logger;
    }

}
