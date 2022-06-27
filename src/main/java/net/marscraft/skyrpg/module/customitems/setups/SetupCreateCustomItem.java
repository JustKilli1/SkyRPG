package net.marscraft.skyrpg.module.customitems.setups;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupCreateCustomItem implements ISetup {

    private static Map<UUID, CustomItem> setupItems = new HashMap<>();
    private final ILogManager logger;
    private CustomItem customItem;
    private MessagesCustomItems messages;
    private DBHandlerCustomItems dbHandler;

    public SetupCreateCustomItem(ILogManager logger, MessagesCustomItems messages, DBHandlerCustomItems dbHandler) {
        this.logger = logger;
        this.dbHandler = dbHandler;
        this.customItem = customItem;
        this.messages = messages;
    }

    @Override
    public <T> T handleEvents(EventStorage eventStorage) {
        return null;
    }

    @Override
    public void handleCommands(Player player, String... args) {


    }

    @Override
    public boolean finishSetup() {
        return false;
    }

    @Override
    public boolean setupComplete() {
        return false;
    }
}
