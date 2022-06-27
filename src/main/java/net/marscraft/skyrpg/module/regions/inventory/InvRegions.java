package net.marscraft.skyrpg.module.regions.inventory;

import net.marscraft.skyrpg.module.regions.database.DBHandlerRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionDisplayRegions;
import net.marscraft.skyrpg.module.regions.inventory.invfunctions.InvFunctionRegionFilter;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.inventory.MarsInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvRegions extends MarsInventory implements IGuiInventory {

    public final static String title = "Regions";
    private final ILogManager logger;
    private DBHandlerRegions dbHandler;

    public InvRegions(ILogManager logger, DBHandlerRegions dbHandler) {
        super(logger);
        this.logger = logger;
        this.dbHandler = dbHandler;
    }

    @Override
    public Inventory build() {
        Inventory inv = buildBaseInventory(title, 6);
        InvFunctionRegionFilter invFunctionRegionFilter = new InvFunctionRegionFilter(logger, 1);
        InvFunctionDisplayRegions invFunctionDisplayRegions = new InvFunctionDisplayRegions(logger, dbHandler, 2, 6);
        inv = invFunctionRegionFilter.add(inv);
        inv = invFunctionDisplayRegions.add(inv);

        return inv;
    }

    @Override
    public Inventory open(Player player) {
        Inventory inv = build();
        player.openInventory(inv);
        return inv;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
