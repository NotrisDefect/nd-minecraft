package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.menus.HomeMenu;
import cabbageroll.notrisdefect.minecraft.menus.JoinRoomMenu;
import cabbageroll.notrisdefect.minecraft.menus.MultiplayerMenu;
import cabbageroll.notrisdefect.minecraft.menus.RoomMenu;
import cabbageroll.notrisdefect.minecraft.menus.SettingsMenu;
import cabbageroll.notrisdefect.minecraft.menus.SkinMenu;
import cabbageroll.notrisdefect.minecraft.menus.SongMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

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
            } else if (holder instanceof SongMenu) {
                SongMenu.onInventoryClick(event);
            }
        }
    }
}
