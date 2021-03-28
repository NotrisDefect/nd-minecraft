package tetrminecraft;

import org.bukkit.Location;
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
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getClickedInventory().getHolder();
        if (event.getClickedInventory() != null) {
            if (holder instanceof HomeMenu) {
                HomeMenu.event(event);
            } else if (holder instanceof MultiplayerMenu) {
                MultiplayerMenu.event(event);
            } else if (holder instanceof JoinRoomMenu) {
                JoinRoomMenu.event(event);
            } else if (holder instanceof RoomMenu) {
                RoomMenu.event(event);
            } else if (holder instanceof SkinMenu) {
                SkinMenu.event(event);
            } else if (holder instanceof SettingsMenu) {
                SettingsMenu.event(event);
            } else if (holder instanceof SimpleSettingsMenu) {
                SimpleSettingsMenu.event(event);
            } else if (holder instanceof SongMenu) {
                SongMenu.event(event);
            }
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
            if (table != null && !table.getGameover()) {
                int itemId = event.getNewSlot();
                switch (itemId) {
                case 0:
                    table.userInput("left");
                    break;
                case 1:
                    table.userInput("right");
                    break;
                case 2:
                    table.userInput("instant");
                    break;
                case 3:
                    table.userInput("space");
                    break;
                case 4:
                    table.userInput("y");
                    break;
                case 5:
                    table.userInput("x");
                    break;
                case 6:
                    table.userInput("up");
                    break;
                case 7:
                    table.userInput("c");
                    break;
                case 8:
                    return;
                }
                player.getInventory().setHeldItemSlot(8);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
            if (player.isSneaking()) {
                if (table != null && !table.getGameover()) {
                    table.userInput("shift");
                }
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
        }
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event.getEntity().getScoreboardTags().contains("sand")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Main.instance.inWhichRoomIs.containsKey(player)) {
            Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
            if (table != null && !table.getGameover()) {
                Location fromLocation = event.getFrom();
                Location toLocation = event.getTo();

                double xDiff = Math.abs(toLocation.getX() - fromLocation.getX());
                double yDiff = toLocation.getY() - fromLocation.getY();
                double zDiff = Math.abs(toLocation.getZ() - fromLocation.getZ());

                player.sendMessage("xDiff: " + xDiff);
                player.sendMessage("zDiff: " + zDiff);
                player.sendMessage("looptick: " + table.getLooptick());

                if (xDiff > 0 || yDiff > 0 || zDiff > 0) {
                    event.getPlayer().teleport(fromLocation.setDirection(toLocation.getDirection()));
                }

                if (zDiff > xDiff) {
                    if (toLocation.getZ() - fromLocation.getZ() > 0) {
                        table.userInput("down");
                        table.userInput("down");
                    }
                    return;
                }

                if (xDiff > zDiff) {
                    if (toLocation.getX() - fromLocation.getX() > 0) {
                        table.userInput("right");
                    } else {
                        table.userInput("left");
                    }
                    return;
                }
            }
        }
    }

}
