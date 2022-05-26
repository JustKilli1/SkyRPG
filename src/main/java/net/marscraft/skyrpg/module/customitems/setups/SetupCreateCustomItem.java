package net.marscraft.skyrpg.module.customitems.setups;

import net.marscraft.skyrpg.module.customitems.MessagesCustomItems;
import net.marscraft.skyrpg.module.customitems.customitem.CustomItem;
import net.marscraft.skyrpg.module.customitems.customitem.ItemRarity;
import net.marscraft.skyrpg.module.customitems.database.DBHandlerCustomItems;
import net.marscraft.skyrpg.shared.Utils;
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
    public boolean handleEvents(EventStorage eventStorage) {
        return false;
    }

    @Override
    public void handleCommands(Player player, String... args) {
        ItemRarity itemRarity = ItemRarity.valueOf(Utils.intFromStr(args[args.length - 1]));
        String itemName = Utils.getStrFromArray(args, 1);
        int itemId = dbHandler.getLastCustomItemId();

        if(itemRarity != null) itemName = itemName.substring(0, itemName.length() - 1);
        else itemRarity = ItemRarity.EINZIGARTIG;

        customItem = new CustomItem(logger, dbHandler, itemId, itemName, itemRarity);
        setupItems.put(player.getUniqueId(), customItem);
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
