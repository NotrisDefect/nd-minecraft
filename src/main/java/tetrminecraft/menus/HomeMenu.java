package tetrminecraft.menus;

import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import tetrminecraft.Main;
import tetrminecraft.Room;

public class HomeMenu extends BaseMenu {

    public final static int MULTIPLAYER_LOCATION = 21;
    public final static int SINGLEPLAYER_LOCATION = 22;
    public final static int SKINEDITOR_LOCATION = 23;

    public HomeMenu(Player player) {
        Main.instance.lastMenuOpened.put(player, "home");
        createInventory(this, 54, "Home");
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            getInventory().setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        getInventory().setItem(MULTIPLAYER_LOCATION,
            createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"));
        getInventory().setItem(SINGLEPLAYER_LOCATION,
            createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"));
        getInventory().setItem(SKINEDITOR_LOCATION, createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"));

        player.openInventory(getInventory());
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
                new Room(player, true);
                new RoomMenu(player);
                break;
            case HomeMenu.SKINEDITOR_LOCATION:
                new SkinMenu(player);
                break;
        }
    }
}
