package net.marscraft.skyrpg.module.custommobs;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import org.bukkit.entity.Player;

public class MessagesCustomMobs extends MessageManager {

    public MessagesCustomMobs(ILogManager logger, IConfigManager configManager, Player player) {
        super(logger, configManager, player);
    }

    public void sendCreateCustomMobErrorMessage() {
        sendPlayerMessage("CustomMob konnte nicht erstellt werden");
    }

    public void sendMobTypeSetMessage() {
        sendCancelOptionReminder();
        sendPlayerMessage("MobType erfolgreich gesetzt.");
        sendPlayerMessage("Bitte &cMaximale Lebenspunkte &ades Mobs im Chat eingeben.");
    }

    public void sendMaxHealthSetMessage(double maxHealth) {
        sendCancelOptionReminder();
        sendPlayerMessage("Lebenspunkte auf &c" + maxHealth + " &agesetzt");
        sendPlayerMessage("Bitte &cMobLevel &aeingeben");
    }

    public void sendLevelSetMessage(int level) {
        sendPlayerMessage("MobLevel auf &c" + level + " &agesetzt");
    }
    public void sendCancelOptionReminder() {
        sendPlayerMessage("Gebe &ccancel &aein um die Erstellung abzubrechen");
    }

    public void sendMobBaseSetupCompleteMessage() {
        sendPlayerMessage("Custom Mob wurde &cerfolgreich &aerstellt.");
        sendPlayerMessage("Du kannst den Mob mit &c/mm edit &aweiter bearbeiten.");
    }

    public void sendSetupCancelledMessage() {
        sendPlayerMessage("Erstellung wurde abgebrochen");
    }

    public void sendInvalidMaxHealthMessage(String userIn) {
        sendPlayerMessage("&c" + userIn + " &aist kein gültiger wert. Bitte eine &cpositive Kommazahl &aangeben");
    }

    public void sendInvalidLevelMessage(String userIn) {
        sendPlayerMessage("&c" + userIn + " &aist kein gültiger wert. Bitte eine &cpositive Ganzzahl &aangeben");
    }

}
