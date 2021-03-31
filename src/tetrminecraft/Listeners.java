package tetrminecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryHolder;

import tetrminecraft.menus.HomeMenu;
import tetrminecraft.menus.JoinRoomMenu;
import tetrminecraft.menus.MultiplayerMenu;
import tetrminecraft.menus.RoomMenu;
import tetrminecraft.menus.SettingsMenu;
import tetrminecraft.menus.SimpleSettingsMenu;
import tetrminecraft.menus.SkinMenu;
import tetrminecraft.menus.SongMenu;

public class Listeners implements Listener {

    private static Listeners instance = new Listeners();

    public static Listeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event.getEntity().getScoreboardTags().contains("sand")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder holder = event.getClickedInventory().getHolder();
            if (holder instanceof HomeMenu) {
                HomeMenu.event(event);
            } else if (holder instanceof MultiplayerMenu) {
                MultiplayerMenu.onInventoryClick(event);
            } else if (holder instanceof JoinRoomMenu) {
                JoinRoomMenu.onInventoryClick(event);
            } else if (holder instanceof RoomMenu) {
                RoomMenu.onInventoryClick(event);
            } else if (holder instanceof SkinMenu) {
                SkinMenu.onInventoryClick(event);
            } else if (holder instanceof SettingsMenu) {
                SettingsMenu.onInventoryClick(event);
            } else if (holder instanceof SimpleSettingsMenu) {
                SimpleSettingsMenu.onInventoryClick(event);
            } else if (holder instanceof SongMenu) {
                SongMenu.onInventoryClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (Main.instance.interactedWithPlugin.contains(player)) {
            if (Main.instance.hasCustomMenuOpen.get(player) == true) {
                Main.instance.hasCustomMenuOpen.put(player, false);
            }
            InventoryHolder holder = event.getInventory().getHolder();
            if (holder instanceof SkinMenu) {
                SkinMenu.onInventoryClose(event);
            }
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table.onPlayerItemHeld(event);
        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table.onPlayerMove(event);
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table.onPlayerToggleSneak(event);
        }
    }

}
