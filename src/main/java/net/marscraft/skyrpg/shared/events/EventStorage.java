package net.marscraft.skyrpg.shared.events;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventStorage {

    private InventoryClickEvent inventoryClickEvent;
    private PlayerInteractEvent playerInteractEvent;
    private AsyncPlayerChatEvent asyncPlayerChatEvent;
    private InventoryCloseEvent inventoryCloseEvent;


    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }

    public void setInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
    }

    public PlayerInteractEvent getPlayerInteractEvent() {
        return playerInteractEvent;
    }

    public void setPlayerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        this.playerInteractEvent = playerInteractEvent;
    }

    public AsyncPlayerChatEvent getAsyncPlayerChatEvent() {
        return asyncPlayerChatEvent;
    }

    public void setAsyncPlayerChatEvent(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        this.asyncPlayerChatEvent = asyncPlayerChatEvent;
    }

    public InventoryCloseEvent getInventoryCloseEvent() {
        return inventoryCloseEvent;
    }

    public void setInventoryCloseEvent(InventoryCloseEvent inventoryCloseEvent) {
        this.inventoryCloseEvent = inventoryCloseEvent;
    }
}
