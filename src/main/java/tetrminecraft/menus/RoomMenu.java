package tetrminecraft.menus;

import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatColor;
import tetrminecraft.Main;

public class RoomMenu extends BaseMenu {

    public final static int BACK_LOCATION = 0;
    public final static int GAME_LOCATION = 49;
    public final static int SONG_LOCATION = 52;
    public final static int SETTINGS_LOCATION = 53;

    public RoomMenu(Player player) {
        Main.instance.lastMenuOpened.put(player, "room");
        createInventory(this, 54, Main.instance.inWhichRoomIs.get(player).getRoomName());
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
        for (Player p : Main.instance.inWhichRoomIs.get(player).playerList) {
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.WHITE + p.getName());
            if (Main.instance.inWhichRoomIs.get(player).getHost().equals(p)) {
                itemmeta.setLore(Collections.singletonList(ChatColor.DARK_RED + "HOST"));
            } else {
                itemmeta.setLore(null);
            }

            item.setItemMeta(itemmeta);
            getInventory().setItem(9 + i, item);
            i++;
        }

        if (Main.instance.inWhichRoomIs.get(player).getHost().equals(player)) {
            if (Main.instance.inWhichRoomIs.get(player).getIsRunning()) {
                getInventory().setItem(GAME_LOCATION, createItem(XMaterial.ANVIL, ChatColor.WHITE + "ABORT"));
            } else {
                if (!Main.instance.inWhichRoomIs.get(player).getIsSingleplayer()
                        && Main.instance.inWhichRoomIs.get(player).playerList.size() == 1) {
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
        getInventory().setItem(SETTINGS_LOCATION, createItem(XMaterial.COMPASS, ChatColor.WHITE + "Table settings"));

        player.openInventory(getInventory());
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        event.setCancelled(true);
        switch (slot) {
        case RoomMenu.BACK_LOCATION:
            if (Main.instance.inWhichRoomIs.get(player).getIsSingleplayer()) {
                new HomeMenu(player);
            } else {
                new MultiplayerMenu(player);
            }
            Main.instance.inWhichRoomIs.get(player).removePlayer(player);
            break;
        case 49:
            if (Main.instance.inWhichRoomIs.get(player).getHost().equals(player)) {
                if (Main.instance.inWhichRoomIs.get(player).getIsRunning()) {
                    Main.instance.inWhichRoomIs.get(player).stopRoom();
                } else {
                    Main.instance.inWhichRoomIs.get(player).startRoom();
                }
                new RoomMenu(player);
            }
            break;
        case RoomMenu.SETTINGS_LOCATION:
            new SimpleSettingsMenu(player);
            break;
        case RoomMenu.SONG_LOCATION:
            new SongMenu(player);
            break;
        }
    }
}
