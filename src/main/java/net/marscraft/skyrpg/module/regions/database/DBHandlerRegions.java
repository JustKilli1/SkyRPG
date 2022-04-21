package net.marscraft.skyrpg.module.regions.database;


import net.marscraft.skyrpg.shared.Utils;
import net.marscraft.skyrpg.shared.database.DBHandler;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.module.regions.region.Bound;
import net.marscraft.skyrpg.module.regions.region.Region;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class DBHandlerRegions extends DBHandler {

    private final ILogManager logger;
    private DBAccessLayerRegions sql;

    public DBHandlerRegions(ILogManager logger, DBAccessLayerRegions sql) {
        super(logger, sql);
        this.logger = logger;
        this.sql = sql;
    }

    public int getLastRegionId() {
        ResultSet rs = sql.getLastRegionsEntry();

        if(rs == null) return 0;
        try {
            if(!rs.next()) return 0;
            return rs.getInt("ID");
        } catch (Exception ex) {
            logger.error(ex);
            return 0;
        }
    }

    public List<Region> getAllRegions() {
        ResultSet rs = sql.getAllRegions();
        List<Region> results = new ArrayList<>();

        if(rs == null) return null;

        try {
            while(rs.next() == true) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                Location loc1 = Utils.locationFromStr(rs.getString("Loc1"));
                Location loc2 = Utils.locationFromStr(rs.getString("Loc2"));
                Bound bound = new Bound(loc1, loc2);
                Region region = new Region(logger, id, name, bound);
                results.add(region);
            }
            return results;
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

}
