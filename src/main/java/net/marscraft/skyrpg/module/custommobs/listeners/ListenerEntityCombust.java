package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.base.Main;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.persistence.PersistentDataType;

public class ListenerEntityCombust implements Listener {

    private final ILogManager logger;

    public ListenerEntityCombust(ILogManager logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        NamespacedKey keyCustomMob = new NamespacedKey(Main.getPlugin(Main.class), "customMob");
        Entity entity = event.getEntity();
        if(entity.getPersistentDataContainer().has(keyCustomMob, PersistentDataType.INTEGER)) {
            event.setCancelled(true);
        }

    }
}
