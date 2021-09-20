package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class RoomMenu extends Menu {

    public final static int GAME_LOCATION = 49;
    public final static int SONG_LOCATION = 52;
    public final static int SETTINGS_LOCATION = 53;

    public RoomMenu(Player player) {
        Room room = Main.gs.getRoom(player);
        createInventory(this, 54, room.getRoomName());

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        ItemStack item;
        ItemMeta itemmeta;

        item = XMaterial.PLAYER_HEAD.parseItem();
        int i = 0;
        for (Player p : room.players) {
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.WHITE + p.getName());
            if (room.getHost().equals(p)) {
                itemmeta.setLore(Collections.singletonList(ChatColor.DARK_RED + "HOST"));
            } else {
                itemmeta.setLore(null);
            }

            item.setItemMeta(itemmeta);
            buttons.put(9 + i, new Button(item));
            i++;
        }

        if (room.getHost().equals(player)) {
            if (room.getIsRunning()) {
                buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.ANVIL, ChatColor.WHITE + "ABORT"), event -> room.stopRoom()));
            } else {
                if (!room.getIsSingleplayer() && room.players.size() == 1) {
                    buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.DARK_PURPLE + "2 players needed")));
                } else {
                    buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.DIAMOND_SWORD, ChatColor.WHITE + "START"), event -> room.startRoom()));
                }
            }
        } else {
            buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.WHITE + "YOU ARE NOT THE HOST")));
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), event -> {
            if (room.getIsSingleplayer()) {
                new HomeMenu(player);
            } else {
                new MultiplayerMenu(player);
            }
            room.removePlayer(player);
        }));
        buttons.put(SONG_LOCATION, new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + "Song"), event -> new SongMenu(player)));
        if (!room.getIsRunning() && room.getHost() == player) {
            buttons.put(SETTINGS_LOCATION, new Button(createItem(XMaterial.COMPASS, ChatColor.WHITE + "Settings"), event -> new SettingsMenu(player)));
        }

        open(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION && event.getSlot() != SONG_LOCATION && event.getSlot() != SETTINGS_LOCATION) {
            new SettingsMenu((Player) event.getWhoClicked());
        }
    }
}
