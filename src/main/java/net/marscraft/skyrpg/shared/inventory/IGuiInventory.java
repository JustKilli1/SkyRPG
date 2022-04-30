package net.marscraft.skyrpg.shared.inventory;

import net.marscraft.skyrpg.shared.events.EventStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGuiInventory {

    /**
     * Builds Inventory and returns it
     * @return Created Inventory
     * */
    Inventory build();

    /**
     * Calls build Method and Opens returning Inventory for player.
     * @param player Player that sees the Inventory
     * @return Inventory which gets opened
     * */
    Inventory open(Player player);

    /**
     * Handles Event Calls for Inventory
     * @param eventStorage Storage Object which contains calling Event
     * */
    <T> T handleEvents(EventStorage eventStorage);

    /**
     * Returns the Inventory Title
     * @return Inventory Title
     * */
    String getTitle();

}
