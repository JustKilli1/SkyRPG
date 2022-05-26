package net.marscraft.skyrpg.module.customitems.setups;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;

public class SetupItemType implements ISetup {

    private final ILogManager logger;
    private CustomItem customItem;
    private MessagesCustomItems messages;
    private DBHandlerCustomItems dbHandler;

    public SetupItemType(ILogManager logger, CustomItem customItem, MessagesCustomItems messages, DBHandlerCustomItems dbHandler) {
        this.logger = logger;
        this.customItem = customItem;
        this.dbHandler = dbHandler;
        this.messages = messages;
    }

    @Override
    public boolean handleEvents(EventStorage eventStorage) {
        return false;
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
