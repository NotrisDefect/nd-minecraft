package cabbageroll.notrisdefect.minecraft.listeners;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

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
                            table.doPressLeft();
                            table.doReleaseLeft();
                            break;
                        case 1:
                            table.doPressRight();
                            table.doReleaseRight();
                            break;
                        case 2:
                            if (table.isZONEENABLED()) {
                                table.doZone();
                            }
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
}
