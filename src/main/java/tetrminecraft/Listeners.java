package tetrminecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import tetrminecraft.menus.*;

public class Listeners implements Listener {

    private static final Listeners instance = new Listeners();

    public static Listeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder holder = event.getClickedInventory().getHolder();
            if (holder instanceof HomeMenu) {
                HomeMenu.event(event);
            } else if (holder instanceof MultiplayerMenu) {
                MultiplayerMenu.onInventoryClick(event);
            } else if (holder instanceof JoinRoomMenu) {
                JoinRoomMenu.onInventoryClick(event);
            } else if (holder instanceof RoomMenu) {
                RoomMenu.onInventoryClick(event);
            } else if (holder instanceof SkinMenu) {
                SkinMenu.onInventoryClick(event);
            } else if (holder instanceof SettingsMenu) {
                SettingsMenu.onInventoryClick(event);
            } else if (holder instanceof SimpleSettingsMenu) {
                SimpleSettingsMenu.onInventoryClick(event);
            } else if (holder instanceof SongMenu) {
                SongMenu.onInventoryClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (Main.instance.interactedWithPlugin.contains(player)) {
            if (Main.instance.hasCustomMenuOpen.get(player)) {
                Main.instance.hasCustomMenuOpen.put(player, false);
            }
            InventoryHolder holder = event.getInventory().getHolder();
            if (holder instanceof SkinMenu) {
                SkinMenu.onInventoryClose(event);
            }
        }
    }

}
