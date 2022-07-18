package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class Listeners implements Listener {

    private static final Listeners INSTANCE = new Listeners();

    public static Listeners getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Menu) {
            Main.GS.getMenu((Player) event.getWhoClicked()).onInventoryClick(event);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Main.GS.isPlayerUsingThePlugin(player)) {
            if (Main.GS.getRoom(player) != null) {
                Table table = Main.GS.getTable(player);
                if (table != null && table.readyForClick) {
                    table.confirmPosition();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!Main.GS.isPlayerUsingThePlugin(player) || Main.GS.getRoom(player) == null) {
            return;
        }

        Table table = Main.GS.getTable(player);
        if (table == null || table.getGameState() == Table.STATE_DEAD) {
            return;
        }

        switch (event.getNewSlot()) {
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
