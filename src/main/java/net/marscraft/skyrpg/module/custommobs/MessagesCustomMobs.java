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

    }

    public void sendEnterBaseHealthMessage() {
        sendPlayerMessage("Bitte &cBasis Lebenspunkte &ades Mobs im Chat eingeben.");
    }

    public void sendBaseHealthSetMessage(double maxHealth) {
        sendCancelOptionReminder();
        sendPlayerMessage("Lebenspunkte auf &c" + maxHealth + " &agesetzt");
    }

    public void sendEnterSpawnChanceMessage() {
        sendPlayerMessage("Bitte &cSpawnChance &aeingeben");
    }

    public void sendInvalidMobIdMessage(String mobId) {
        sendPlayerMessage("&c" + mobId + " &aist keine gültige MobId. Bitte eine &cGanzzahl &aangeben");
    }

    public void sendInvalidAmountMessage(String mobId) {
        sendPlayerMessage("&c" + mobId + " &aist keine gültige Anzahl. Bitte eine &cGanzzahl &aangeben");
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
    public void sendInvalidSpawnChanceMessage(String userIn) {
        sendPlayerMessage("&c" + userIn + " &aist kein gültiger wert. Bitte eine Zahl zwischen 1 und 100 angeben");
    }

    public void sendSpawnChanceSetMessage(double spawnChance) {
        sendPlayerMessage("SpawnChance auf &c" + spawnChance + " &agesetzt");
    }

    public void sendEnterNewNameMessage() {
        sendPlayerMessage("Bitte gebe den neuen Namen im Chat ein");
    }

    public void sendNameSetMessage(String newName) {
        sendPlayerMessage("Name wurde auf &c" + newName + " &ageändert.");
    }
    public void sendTypeSetMessage(String newType) {
        sendPlayerMessage("MobType wurde auf &c" + newType + " &ageändert.");
    }

    public void sendEntityTypeCouldNotBeSet() {
        sendPlayerMessage("Entity Type konnte nicht gesetzt werden");
    }

    public void sendMobIdNotFound(int mobId) {
        sendPlayerMessage("Mob mit der Id &c" + mobId + " &akonnte nicht gefunden werden.");
    }

    public void sendMobSpawned(String mobName) {
        sendPlayerMessage("&c" + mobName + " &awurde gespawnt!");
    }
}
