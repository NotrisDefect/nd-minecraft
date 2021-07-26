package cabbageroll.notrisdefect.listeners;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Table;
import cabbageroll.notrisdefect.functions.util.Point3Dint;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class TableListeners implements Listener {

    private static final TableListeners instance = new TableListeners();

    public static TableListeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.isPlayerHere(player)) {
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.isPlayerHere(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (!table.getGameover()) {
                    Location from = event.getFrom();
                    Location to = event.getTo();

                    double dx = to.getX() - from.getX();
                    double dz = to.getZ() - from.getZ();

                    player.sendMessage("dx: " + dx);
                    player.sendMessage("dz: " + dz);

                    if (dx != 0 || dz != 0) {
                        event.getPlayer().teleport(from.setDirection(to.getDirection()));
                    }

                    if (Math.abs(dx) > Math.abs(dz)) {
                        Point3Dint mx = table.getWidthMultiplier();
                        /*
                        100
                        001
                        -100
                        00-1
                         */
                        if (mx.getX() > mx.getZ() && mx.getX() > 0) {
                            if (to.getX() < from.getX()) {
                                table.extMovePieceLeft();
                            } else if (to.getX() > from.getX()) {
                                table.extMovePieceRight();
                            }
                        } else if (mx.getX() < mx.getZ() && mx.getZ() > 0) {
                            if (to.getX() < from.getX()) {
                                table.extDropPieceSoft();
                            }
                        } else if (mx.getX() < mx.getZ() && mx.getX() < 0) {
                            if (to.getX() > from.getX()) {
                                table.extMovePieceLeft();
                            } else if (to.getX() < from.getX()) {
                                table.extMovePieceRight();
                            }
                        } else if (mx.getX() > mx.getZ() && mx.getZ() < 0) {
                            if (to.getX() > from.getX()) {
                                table.extDropPieceSoft();
                            }
                        }
                    } else if (Math.abs(dx) < Math.abs(dz)) {
                        Point3Dint mx = table.getWidthMultiplier();
                        if (mx.getX() > mx.getZ() && mx.getX() > 0) {
                            if (to.getZ() > from.getZ()) {
                                table.extDropPieceSoft();
                            }
                        } else if (mx.getX() < mx.getZ() && mx.getZ() > 0) {
                            if (to.getZ() < from.getZ()) {
                                table.extMovePieceLeft();
                            } else if (to.getZ() > from.getZ()) {
                                table.extMovePieceRight();
                            }
                        } else if (mx.getX() < mx.getZ() && mx.getX() < 0) {
                            if (to.getZ() < from.getZ()) {
                                table.extDropPieceSoft();
                            }
                        } else if (mx.getX() > mx.getZ() && mx.getZ() < 0) {
                            if (to.getZ() > from.getZ()) {
                                table.extMovePieceLeft();
                            } else if (to.getZ() < from.getZ()) {
                                table.extMovePieceRight();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.isPlayerHere(player)) {
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
