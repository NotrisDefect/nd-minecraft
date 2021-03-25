package tetrminecraft.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatColor;
import tetrminecraft.Main;

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
                BaseMenu.createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"));
        getInventory().setItem(SINGLEPLAYER_LOCATION,
                BaseMenu.createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"));
        getInventory().setItem(SKINEDITOR_LOCATION, BaseMenu.createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"));

        player.openInventory(getInventory());
    }
}
