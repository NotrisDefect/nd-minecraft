package tetrminecraft.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import tetrminecraft.Main;
import tetrminecraft.Room;
import tetrminecraft.functions.dependencyutil.NoteBlockAPIYes;

public class SongMenuInventoryClickEvent implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) {
            player.sendMessage("clicked outside of inventory");
        } else {
            if (event.getClickedInventory().getHolder() instanceof SongMenu) {
                event.setCancelled(true);
                Room room = Main.inWhichRoomIs.get(player);
                if (event.getSlot() == SongMenu.BACK_LOCATION) {
                    new RoomMenu(player);
                } else if (event.getSlot() == 9) {
                    room.index = -1;
                } else if (event.getSlot() - 10 < NoteBlockAPIYes.playlist.getCount()) {
                    room.index = event.getSlot() - 10;
                }
            }
        }
    }
}
