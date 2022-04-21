package net.marscraft.skyrpg.module.custommobs.listeners;

import net.marscraft.skyrpg.module.custommobs.ModuleCustomMobs;
import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.events.EventStorage;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.setups.ISetup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ListenerPlayerChat implements Listener {

    private ILogManager logger;
    private IConfigManager messagesConfig;

    public ListenerPlayerChat(ILogManager logger, IConfigManager messagesConfig) {
        this.logger = logger;
        this.messagesConfig = messagesConfig;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if(!ModuleCustomMobs.getSetups().containsKey(player.getUniqueId())) return;
        EventStorage eventStorage = new EventStorage();
        eventStorage.setAsyncPlayerChatEvent(event);
        ISetup setup = ModuleCustomMobs.getSetups().get(player.getUniqueId());
        //event.setCancelled(setup.cancelEvent());
        setup.handleEvents(eventStorage);

/*        Player player = event.getPlayer();
        MessageManager messageManager = new MessageManager(logger, messagesConfig, player);
        Map<UUID, MobHostile> setupMobs = CommandMarsMob.getSetupMobs();
        if(!setupMobs.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);
        String message = event.getMessage();
        if(message.equals("cancel")) {
            setupMobs.remove(player.getUniqueId());
            messageManager.sendPlayerMessage("Erstellung abgebrochen");
            return;
        }
        if(setupMobs.get(player.getUniqueId()).getName() == null) {
            setupMobs.get(player.getUniqueId()).setName(message);
            messageManager.sendPlayerMessage("Name " + message + " erfolgreich gesetzt");
            messageManager.sendPlayerMessage("Maximale Anzahl an Lebenspunkten: ");

            return;

        }
        if(setupMobs.get(player.getUniqueId()).getMaxHealth() == 0.0){
            double health = Utils.doubleFromStr(message);
            if(health == -1) {
                messageManager.sendPlayerMessage("§c" + message + " §aist keine gültige Nummer");
                return;
            }
            setupMobs.get(player.getUniqueId()).setMaxHealth(Utils.doubleFromStr(message));
            messageManager.sendPlayerMessage("Lebenspunkte gesetzt");
            messageManager.sendPlayerMessage("Erste einrichtung erfolgreich. /mm edit um weitere anpassungen vorzunehmen");
            return;
        }*/

    }

}
