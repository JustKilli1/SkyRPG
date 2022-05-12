package net.marscraft.skyrpg.module.customitems.database;

import net.marscraft.skyrpg.module.regions.database.DBAccessLayerRegions;
import net.marscraft.skyrpg.shared.database.DBHandler;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

public class DBHandlerCustomItems extends DBHandler {

    private final ILogManager logger;
    private DBAccesLayerCustomItems sql;

    public DBHandlerCustomItems(ILogManager logger, DBAccesLayerCustomItems sql) {
        super(logger, sql);
        this.logger = logger;
        this.sql = sql;
    }


}
