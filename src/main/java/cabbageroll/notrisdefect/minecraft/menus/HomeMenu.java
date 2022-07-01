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
    public final static int SINGLEPLAYER2_LOCATION = grid(4, 5);
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
        addBorder();

        if (player.hasPermission(Strings.permMP)) {
            addButton(MULTIPLAYER_LOCATION, event -> new ListMenu(player), XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Tetris Multiplayer");
        } else {
            addButton(MULTIPLAYER_LOCATION, event -> player.sendMessage(Strings.noPermission(Strings.permMP)), XMaterial.BARRIER, ChatColor.RED + "No permission");
        }
        if (player.hasPermission(Strings.permSP)) {
            addButton(SINGLEPLAYER_LOCATION, event -> {
                Main.gs.createSPRoom(player);
                new RoomMenu(player);
            }, XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Tetris Singleplayer");
            addButton(SINGLEPLAYER2_LOCATION, event -> {
                player.sendMessage("soon");
            }, XMaterial.ORANGE_WOOL, ChatColor.WHITE + "Lumines Singleplayer");
        } else {
            addButton(SINGLEPLAYER_LOCATION, event -> player.sendMessage(Strings.noPermission(Strings.permSP)), XMaterial.BARRIER, ChatColor.RED + "No permission");
            addButton(SINGLEPLAYER2_LOCATION, event -> player.sendMessage(Strings.noPermission(Strings.permSP)), XMaterial.BARRIER, ChatColor.RED + "No permission");
        }
        if (player.hasPermission(Strings.permSkinEditor)) {
            addButton(SKINEDITOR_LOCATION, event -> new NewSkinMenu(player), XMaterial.DAMAGED_ANVIL, ChatColor.WHITE + "Skin editor");
        } else {
            addButton(SKINEDITOR_LOCATION, event -> player.sendMessage(Strings.noPermission(Strings.permSkinEditor)), XMaterial.BARRIER, ChatColor.RED + "No permission");
        }
    }
}
