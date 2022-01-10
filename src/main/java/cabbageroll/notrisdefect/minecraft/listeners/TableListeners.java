package cabbageroll.notrisdefect.minecraft.listeners;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
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
                if (table != null && table.getGameState() != Table.STATE_DEAD) {
                    int slot = event.getNewSlot();
                    switch (slot) {
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
                if (table.getGameState() != Table.STATE_DEAD) {
                    Location from = event.getFrom();
                    Location to = event.getTo();

                    double dx = to.getX() - from.getX();
                    double dz = to.getZ() - from.getZ();

                    if (dx != 0 || dz != 0) {
                        event.getPlayer().teleport(from.setDirection(to.getDirection()));
                    }

                    if (Math.abs(dx) > Math.abs(dz)) {
                        /*
                        100
                        001
                        -100
                        00-1
                         */
                        if (table.getWMX() > table.getWMZ() && table.getWMX() > 0) {
                            if (to.getX() < from.getX()) {
                                table.extMovePieceLeft();
                            } else if (to.getX() > from.getX()) {
                                table.extMovePieceRight();
                            }
                        } else if (table.getWMX() < table.getWMZ() && table.getWMZ() > 0) {
                            if (to.getX() < from.getX()) {
                                table.extDropPieceSoft();
                            }
                        } else if (table.getWMX() < table.getWMZ() && table.getWMX() < 0) {
                            if (to.getX() > from.getX()) {
                                table.extMovePieceLeft();
                            } else if (to.getX() < from.getX()) {
                                table.extMovePieceRight();
                            }
                        } else if (table.getWMX() > table.getWMZ() && table.getWMZ() < 0) {
                            if (to.getX() > from.getX()) {
                                table.extDropPieceSoft();
                            }
                        }
                    } else if (Math.abs(dx) < Math.abs(dz)) {
                        if (table.getWMX() > table.getWMZ() && table.getWMX() > 0) {
                            if (to.getZ() > from.getZ()) {
                                table.extDropPieceSoft();
                            }
                        } else if (table.getWMX() < table.getWMZ() && table.getWMZ() > 0) {
                            if (to.getZ() < from.getZ()) {
                                table.extMovePieceLeft();
                            } else if (to.getZ() > from.getZ()) {
                                table.extMovePieceRight();
                            }
                        } else if (table.getWMX() < table.getWMZ() && table.getWMX() < 0) {
                            if (to.getZ() < from.getZ()) {
                                table.extDropPieceSoft();
                            }
                        } else if (table.getWMX() > table.getWMZ() && table.getWMZ() < 0) {
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
                    if (table != null && table.getGameState() != Table.STATE_DEAD) {
                        table.extStartZone();
                    }
                }
            }
        }
    }
}
