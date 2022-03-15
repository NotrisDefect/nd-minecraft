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
        if (Main.gs.isPlayerUsingThePlugin(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (table != null && table.getGameState() != Table.STATE_DEAD) {
                    int slot = event.getNewSlot();
                    switch (slot) {
                        case 0:
                            table.doMoveLeft();
                            break;
                        case 1:
                            table.doMoveRight();
                            break;
                        case 2:
                            table.doInstantSoftDrop();
                            break;
                        case 3:
                            table.doHardDrop();
                            break;
                        case 4:
                            table.doRotateCCW();
                            break;
                        case 5:
                            table.doRotateCW();
                            break;
                        case 6:
                            table.doRotate180();
                            break;
                        case 7:
                            table.doHold();
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
        if (Main.gs.isPlayerUsingThePlugin(player)) {
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

                    if (Math.abs(dx) * 3 > Math.abs(dz)) {
                        if (table.getWMX() > 0) {
                            if (dx < 0) {
                                table.doMoveLeft();
                            } else {
                                table.doMoveRight();
                            }
                        } else if (table.getWMX() < 0) {
                            if (dx > 0) {
                                table.doMoveLeft();
                            } else {
                                table.doMoveRight();
                            }
                        }

                        if (table.getHMX() > 0) {
                            if (dx < 0) {
                                table.doSoftDrop();
                            } else {
                                table.doRotateCW();
                            }
                        } else if (table.getHMX() < 0) {
                            if (dx > 0) {
                                table.doSoftDrop();
                            } else {
                                table.doRotateCW();
                            }
                        } else {
                            //pissyshitty
                            if (table.getWMZ() > 0) {
                                if (dx < 0) {
                                    table.doSoftDrop();
                                } else {
                                    table.doRotateCW();
                                }
                            } else if (table.getWMZ() < 0) {
                                if (dx > 0) {
                                    table.doSoftDrop();
                                } else {
                                    table.doRotateCW();
                                }
                            }
                        }
                    }

                    if (Math.abs(dz) * 3 > Math.abs(dx)) {
                        if (table.getWMZ() > 0) {
                            if (dz < 0) {
                                table.doMoveLeft();
                            } else {
                                table.doMoveRight();
                            }
                        } else if (table.getWMZ() < 0) {
                            if (dz > 0) {
                                table.doMoveLeft();
                            } else {
                                table.doMoveRight();
                            }
                        }

                        if (table.getHMZ() > 0) {
                            if (dz < 0) {
                                table.doSoftDrop();
                            } else {
                                table.doRotateCW();
                            }
                        } else if (table.getHMZ() < 0) {
                            if (dz > 0) {
                                table.doSoftDrop();
                            } else {
                                table.doRotateCW();
                            }
                        } else {
                            //pissyshitty
                            if (table.getWMX() > 0) {
                                if (dz > 0) {
                                    table.doSoftDrop();
                                } else {
                                    table.doRotateCW();
                                }
                            } else if (table.getWMX() < 0) {
                                if (dz < 0) {
                                    table.doSoftDrop();
                                } else {
                                    table.doRotateCW();
                                }
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
        if (Main.gs.isPlayerUsingThePlugin(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (player.isSneaking()) {
                    if (table != null && table.getGameState() != Table.STATE_DEAD) {
                        table.doZone();
                    }
                }
            }
        }
    }
}
