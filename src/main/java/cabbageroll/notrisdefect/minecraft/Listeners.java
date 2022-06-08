package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class Listeners implements Listener {

    private static final Listeners instance = new Listeners();

    public static Listeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Menu) {
            Main.gs.getTable((Player) event.getWhoClicked()).getLastMenuOpened().onInventoryClick(event);
        }
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
