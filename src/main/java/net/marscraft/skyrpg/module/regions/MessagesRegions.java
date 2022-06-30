package net.marscraft.skyrpg.module.regions;

import net.marscraft.skyrpg.shared.configmanager.IConfigManager;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;
import net.marscraft.skyrpg.shared.messagemanager.MessageManager;
import org.bukkit.entity.Player;

public class MessagesRegions extends MessageManager {

    public MessagesRegions(ILogManager logger, IConfigManager configManager, Player player) {
        super(logger, configManager, player);
    }

    public void sendMessageLocSet(int locNumber) {
        sendPlayerMessage("&aLocation " + locNumber + " Set");
    }
    public void sendSelectRegion() { sendPlayerMessage("&aBitte die Region markieren");}
    public void sendRegionCreated(String regionName) { sendPlayerMessage("&aRegion &c" + regionName + " &awurde erstellt."); }

    public void sendRegionCommandCreateSyntaxError() { sendPlayerSyntaxError("mr create [RegionName]"); }
    public void sendRegionCommandSyntaxError() { sendPlayerSyntaxError("mr help"); }

    public void sendEnterNewNameMessage() { sendPlayerMessage("Bitte neuen Namen eingeben");
    }

    public void sendRegionNameSetMessage(String newName) { sendPlayerMessage("Name der Region wurde zu &c" + newName + " &ageändert");
    }

    public void sendChangeMaxMobs() { sendPlayerMessage("Bitte gebe die neue Maximale Mob Anzahl der Region ein");
    }

    public void sendMaxMobsInvalid(String message) { sendPlayerMessage("§c" + message + " §aist keine Gültige Anzahl. Bitte eine Ganzzahl eingeben!");
    }

    public void sendMSRegionMaxMobsSetMessage(int newMaxMobs) { sendPlayerMessage("Die Maximale Mob Anzahl der Region wurde auf §c" + newMaxMobs + " §agesetzt.");
    }

    public void sendChangeSpawnChance() { sendPlayerMessage("Bitte gebe die neue SpawnChance ein");
    }

    public void sendSpawnChanceInvalid(String message) { sendPlayerMessage("§c" + message + " §aist keine gültige SpawnChance.");
    }

    public void sendMSRSpawnChanceSetMessage(double newSpawnChance) { sendPlayerMessage("SpawnChance wurde auf §c" + newSpawnChance + " §ageändert.");
    }

    public void sendInvalidLevel(String message) { sendPlayerMessage("§c" + message + " §aist kein gültiges Level. Bitte gebe eine positive Ganzzahl an.");
    }

    public void sendChangeMobMinLevel() { sendPlayerMessage("Bitte geben Sie das §cMinimal Level §aan");
    }

    public void sendChangeMobMaxLevel() { sendPlayerMessage("Bitte geben Sie das §cMaximal Level §aan");
    }

    public void sendMobLevelChanged() { sendPlayerMessage("Mob Level erfolgreich geändert");
    }

    public void sendMaxLevelInvalid() { sendPlayerMessage("Das Maximal Level muss höher sein als das minimal Level");
    }

    public void sendNewMobAdded() { sendPlayerMessage("Neuer Mob erfolgreich hinzugefügt");
    }
}
