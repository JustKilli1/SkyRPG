package net.marscraft.skyrpg.shared.setups;

import net.marscraft.skyrpg.shared.events.EventStorage;
import org.bukkit.entity.Player;

public interface ISetup {

    /**
     * Interface for module setup commands
     * */


    /**
     * Handles Event calls from Listeners
     * @param eventStorage Object with Calling Listener Stored
     * @return true if Calling Event should be canceld
     * */
    <T> T handleEvents(EventStorage eventStorage);
    void handleCommands(Player player, String... args);
    boolean finishSetup();

    boolean setupComplete();

}
