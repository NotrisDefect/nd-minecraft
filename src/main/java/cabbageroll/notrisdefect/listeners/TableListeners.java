package cabbageroll.notrisdefect.listeners;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class TableListeners implements Listener {

    private static final TableListeners instance = new TableListeners();

    public static TableListeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.playerIsHere(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (table != null && !table.getGameover()) {
                    int itemId = event.getNewSlot();
                    switch (itemId) {
                        case 0:
                            table.extMovePieceLeft();
                            break;
                        case 1:
                            table.extMovePieceRight();
                            break;
                        case 2:
                            table.extDropPieceSoftMax();
                            break;
                        case 3:
                            table.extDropPieceHard();
                            break;
                        case 4:
                            table.extRotatePieceCCW();
                            break;
                        case 5:
                            table.extRotatePieceCW();
                            break;
                        case 6:
                            table.extRotatePiece180();
                            break;
                        case 7:
                            table.extHoldPiece();
                            break;
                        case 8:
                            return;
                    }
                    player.getInventory().setHeldItemSlot(8);
                }
            }
        }
    }

    /*
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent event) {
                Player player = event.getPlayer();
                if (Main.gs.playerIsHere(player)) {
                    if (Main.gs.getRoom(player) != null) {
                        Table table = Main.gs.getTable(player);
                        if (!table.getGameover()) {
                            Location fromLocation = event.getFrom();
                            Location toLocation = event.getTo();

                            double xDiff = Math.abs(toLocation.getX() - fromLocation.getX());
                            double yDiff = toLocation.getY() - fromLocation.getY();
                            double zDiff = Math.abs(toLocation.getZ() - fromLocation.getZ());

                            player.sendMessage("xDiff: " + xDiff);
                            player.sendMessage("zDiff: " + zDiff);

                            if (xDiff > 0 || yDiff > 0 || zDiff > 0) {
                                event.getPlayer().teleport(fromLocation.setDirection(toLocation.getDirection()));
                            }

                            if (zDiff > xDiff) {
                                if (toLocation.getZ() - fromLocation.getZ() > 0) {
                                    table.extDropPieceSoft();
                                    table.extDropPieceSoft();
                                }
                                return;
                            }

                            if (xDiff > zDiff) {
                                if (toLocation.getX() - fromLocation.getX() > 0) {
                                    table.extMovePieceRight();
                                } else {
                                    table.extMovePieceLeft();
                                }
                            }
                        }
                    }
                }
            }
        */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.playerIsHere(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (player.isSneaking()) {
                    if (table != null && !table.getGameover()) {
                        table.extStartZone();
                    }
                }
            }
        }
    }
}
