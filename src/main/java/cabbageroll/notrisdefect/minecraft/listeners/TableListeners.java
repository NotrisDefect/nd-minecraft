package cabbageroll.notrisdefect.minecraft.listeners;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
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
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Main.gs.isPlayerUsingThePlugin(player)) {
            if (Main.gs.getRoom(player) != null) {
                Table table = Main.gs.getTable(player);
                if (player.isSneaking()) {
                    if (table != null && table.getGameState() != Table.STATE_DEAD && table.isZONEENABLED()) {
                        table.doZone();
                    }
                }
            }
        }
    }
}
