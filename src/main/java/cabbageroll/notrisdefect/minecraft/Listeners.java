package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
}
