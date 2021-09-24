package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Strings;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HomeMenu extends Menu {

    public final static int MULTIPLAYER_LOCATION = grid(3, 4);
    public final static int SINGLEPLAYER_LOCATION = grid(3, 5);
    public final static int SKINEDITOR_LOCATION = grid(3, 6);

    public HomeMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "Home");

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        if (player.hasPermission(Strings.permMP)) {
            buttons.put(MULTIPLAYER_LOCATION, new Button(createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"), event -> new ListMenu(player)));
        } else {
            buttons.put(MULTIPLAYER_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.RED + "No permission"), event -> player.sendMessage(Strings.noPermission(Strings.permMP))));
        }
        if (player.hasPermission(Strings.permSP)) {
            buttons.put(SINGLEPLAYER_LOCATION, new Button(createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"), event -> {
                Main.gs.createSPRoom(player);
                new RoomMenu(player);
            }));
        } else {
            buttons.put(SINGLEPLAYER_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.RED + "No permission"), event -> player.sendMessage(Strings.noPermission(Strings.permSP))));
        }
        if (player.hasPermission(Strings.permSkinEditor)) {
            buttons.put(SKINEDITOR_LOCATION, new Button(createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"), event -> new SkinMenu(player)));
        } else {
            buttons.put(SKINEDITOR_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.RED + "No permission"), event -> player.sendMessage(Strings.noPermission(Strings.permSkinEditor))));
        }
    }
}
