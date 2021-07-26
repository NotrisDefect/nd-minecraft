package cabbageroll.notrisdefect.menus;

import cabbageroll.notrisdefect.Main;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HomeMenu extends Menu {

    public final static int MULTIPLAYER_LOCATION = 21;
    public final static int SINGLEPLAYER_LOCATION = 22;
    public final static int SKINEDITOR_LOCATION = 23;

    public HomeMenu(Player player) {
        createInventory(this, 54, "Home");
        Inventory inventory = getInventory();
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            getInventory().setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        inventory.setItem(MULTIPLAYER_LOCATION, createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"));
        inventory.setItem(SINGLEPLAYER_LOCATION, createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"));
        inventory.setItem(SKINEDITOR_LOCATION, createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"));

        Main.gs.openMenu(player, this);
    }

    public static void event(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        event.setCancelled(true);
        switch (slot) {
            case HomeMenu.MULTIPLAYER_LOCATION:
                new MultiplayerMenu(player);
                break;
            case HomeMenu.SINGLEPLAYER_LOCATION:
                Main.gs.createSPRoom(player);
                new RoomMenu(player);
                break;
            case HomeMenu.SKINEDITOR_LOCATION:
                new SkinMenu(player);
                break;
        }
    }
}
