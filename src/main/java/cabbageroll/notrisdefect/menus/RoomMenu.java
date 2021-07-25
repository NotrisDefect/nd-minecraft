package cabbageroll.notrisdefect.menus;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Room;
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
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            getInventory().setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        // clickable items
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
            getInventory().setItem(9 + i, item);
            i++;
        }

        if (room.getHost().equals(player)) {
            if (room.getIsRunning()) {
                getInventory().setItem(GAME_LOCATION, createItem(XMaterial.ANVIL, ChatColor.WHITE + "ABORT"));
            } else {
                if (!room.getIsSingleplayer()
                    && room.players.size() == 1) {
                    getInventory().setItem(GAME_LOCATION,
                        createItem(XMaterial.BARRIER, ChatColor.DARK_PURPLE + "2 players needed"));
                } else {
                    getInventory().setItem(GAME_LOCATION,
                        createItem(XMaterial.DIAMOND_SWORD, ChatColor.WHITE + "START"));
                }
            }
        } else {
            getInventory().setItem(GAME_LOCATION,
                createItem(XMaterial.BARRIER, ChatColor.WHITE + "YOU ARE NOT THE HOST"));
        }

        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(SONG_LOCATION, createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + "Song"));
        if (!room.getIsRunning() && room.getHost() == player) {
            getInventory().setItem(SETTINGS_LOCATION, createItem(XMaterial.COMPASS, ChatColor.WHITE + "Settings"));
        }

        Main.gs.openMenu(player, this);
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Room room = Main.gs.getRoom(player);
        int slot = event.getSlot();
        event.setCancelled(true);
        switch (slot) {
            case RoomMenu.BACK_LOCATION:
                if (room.getIsSingleplayer()) {
                    new HomeMenu(player);
                } else {
                    new MultiplayerMenu(player);
                }
                room.removePlayer(player);
                break;
            case 49:
                if (room.getHost().equals(player)) {
                    if (room.getIsRunning()) {
                        room.stopRoom();
                    } else {
                        room.startRoom();
                    }
                    new RoomMenu(player);
                }
                break;
            case RoomMenu.SETTINGS_LOCATION:
                if (!room.getIsRunning() && room.getHost() == player) {
                    new SettingsMenu(player);
                }
                break;
            case RoomMenu.SONG_LOCATION:
                new SongMenu(player);
                break;
        }
    }
}
