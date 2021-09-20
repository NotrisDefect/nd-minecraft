package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class
HomeMenu extends Menu {

    public final static int MULTIPLAYER_LOCATION = 21;
    public final static int SINGLEPLAYER_LOCATION = 22;
    public final static int SKINEDITOR_LOCATION = 23;

    public HomeMenu(Player player) {
        createInventory(this, 54, "Home");

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(MULTIPLAYER_LOCATION, new Button(createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"), event -> new MultiplayerMenu(player)));
        buttons.put(SINGLEPLAYER_LOCATION, new Button(createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"), event -> {
            Main.gs.createSPRoom(player);
            new RoomMenu(player);
        }));
        buttons.put(SKINEDITOR_LOCATION, new Button(createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"), event -> new SkinMenu(player)));

        open(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }
}
