package net.marscraft.skyrpg.shared.inventory;

import net.marscraft.skyrpg.shared.events.EventStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGuiInventory {

    Inventory build();

    Inventory open(Player player);

    void handleClickEvent(EventStorage eventStorage);

    boolean clickEventCanceled();

    String getTitle();

}
