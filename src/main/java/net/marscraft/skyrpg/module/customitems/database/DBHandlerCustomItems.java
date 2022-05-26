package net.marscraft.skyrpg.module.customitems.database;

import net.marscraft.skyrpg.shared.database.DBHandler;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;

public class DBHandlerCustomItems extends DBHandler {

    private final ILogManager logger;
    private DBAccesLayerCustomItems sql;

    public DBHandlerCustomItems(ILogManager logger, DBAccesLayerCustomItems sql) {
        super(logger, sql);
        this.logger = logger;
        this.sql = sql;
    }


    /**
     * Gets Last Id from CustomItems Table
     * */
    public @Nullable int getLastCustomItemId() {
        ResultSet rs = sql.getLastCustomItem();
        if(rs == null) return 0;

        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }


}
