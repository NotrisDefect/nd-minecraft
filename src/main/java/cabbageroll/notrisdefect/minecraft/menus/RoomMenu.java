package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.Strings;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class RoomMenu extends Menu {

    public final static int GAME_LOCATION = grid(6, 5);
    public final static int SONG_LOCATION = grid(6, 8);
    public final static int SETTINGS_LOCATION = grid(6, 9);

    public RoomMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION && event.getSlot() != SONG_LOCATION && event.getSlot() != SETTINGS_LOCATION) {
            new RoomMenu((Player) event.getWhoClicked());
        }
    }

    @Override
    protected void prepare() {
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
            }

            item.setItemMeta(itemmeta);
            buttons.put(9 + i, new Button(item));
            i++;
        }

        if (room.getHost().equals(player)) {
            if (room.isRunning()) {
                buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.ANVIL, ChatColor.YELLOW + "ABORT"), event -> room.stopRoom()));
            } else {
                if (!room.isSingleplayer() && room.players.size() == 1) {
                    buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.BARRIER, ChatColor.DARK_RED + "2 players needed"), event -> player.sendMessage(Strings.notEnoughPlayers)));
                } else {
                    buttons.put(GAME_LOCATION, new Button(createItem(XMaterial.DIAMOND_SWORD, ChatColor.DARK_GREEN + "START"), event -> room.startRoom()));
                }
                buttons.put(SONG_LOCATION, new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + "Song"), event -> new RoomSongMenu(player)));
                buttons.put(SETTINGS_LOCATION, new Button(createItem(XMaterial.COMPASS, ChatColor.WHITE + "Settings"), event -> new RoomSettingsMenu(player)));

            }
        } else {
            if (room.isRunning()) {

            } else {
                buttons.put(SETTINGS_LOCATION, new Button(createItem(XMaterial.COMPASS, ChatColor.WHITE + "Settings", ChatColor.YELLOW + "" + ChatColor.ITALIC + "read only")));
            }
        }

        if (room.isSingleplayer()) {
            buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.RED + "Leave"), event -> {
                new HomeMenu(player);
                room.removePlayer(player);
            }));
        } else {
            buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.RED + "Leave"), event -> {
                new ListMenu(player);
                room.removePlayer(player);
            }));
        }

    }
}
