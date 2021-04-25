package tetrminecraft.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import tetrminecraft.Main;
import tetrminecraft.Table;

public class TableListeners implements Listener {

    private static final TableListeners instance = new TableListeners();

    public static TableListeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
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
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
        if (player.isSneaking()) {
            if (table != null && !table.getGameover()) {
                table.userInput("shift");
            }
        }
    }
}
