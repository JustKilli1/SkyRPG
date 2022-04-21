package net.marscraft.skyrpg.shared.database;

import net.marscraft.skyrpg.shared.logmanager.ILogManager;


public class DBHandler {

    /**
     * Gets ResultSets from sql request
     * --> Gets Information needed from ResultSet and returns it
     * Works with sql return data(ResultSets)
     * */

    protected ILogManager logger;
    protected DBAccessLayer sql;

    public DBHandler(ILogManager logger, DBAccessLayer sql) {
        this.logger = logger;
        this.sql = sql;
    }




}
