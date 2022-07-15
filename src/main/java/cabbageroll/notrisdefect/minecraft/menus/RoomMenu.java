package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.Strings;
import cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPIYes;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class RoomMenu extends Menu {

    public final static int GAME_LOCATION = grid(5, 6);

    public final static int MY_SETTINGS_LOCATION = grid(1, 6);
    public final static int EXTRA_SETTINGS_LOCATION = grid(2, 6);

    public final static int SONG_LOCATION = grid(8, 6);
    public final static int SETTINGS_LOCATION = grid(9, 6);

    private Room room;

    private int page = 0;

    public RoomMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == PREV_PAGE_LOCATION || event.getSlot() == NEXT_PAGE_LOCATION) {
            clearContent();
            updateContent();
            placeAll();
        } else if (event.getSlot() != BACK_LOCATION && event.getSlot() != SONG_LOCATION && event.getSlot() != SETTINGS_LOCATION && event.getSlot() != EXTRA_SETTINGS_LOCATION && event.getSlot() != MY_SETTINGS_LOCATION && event.getSlot() != GAME_LOCATION) {
            new RoomMenu((Player) event.getWhoClicked());
        }
    }

    @Override
    protected void prepare() {
        room = Main.gs.getRoom(player);
        createInventory(this, 54, room.getRoomName());
        addBorder();

        if (room.getOwner().equals(player)) {
            if (room.isRunning()) {
                addButton(GAME_LOCATION, event -> {
                    room.stopRoom();
                    new RoomMenu((Player) event.getWhoClicked());
                }, XMaterial.ANVIL, ChatColor.YELLOW + "ABORT");
            } else {
                if (!room.isSingleplayer() && room.players.size() == 1) {
                    addButton(GAME_LOCATION, event -> player.sendMessage(Strings.notEnoughPlayers), XMaterial.BARRIER, ChatColor.DARK_RED + "2 PLAYERS NEEDED");
                } else {
                    addButton(GAME_LOCATION, event -> room.startRoom(), XMaterial.DIAMOND_SWORD, ChatColor.DARK_GREEN + "START");
                }
                if (Main.noteBlockAPI instanceof NoteBlockAPIYes) {
                    addButton(SONG_LOCATION, event -> new RoomSongMenu(player), XMaterial.NOTE_BLOCK, ChatColor.WHITE + "Song");
                }
                addButton(SETTINGS_LOCATION, event -> new RoomSettingsMenu(player), XMaterial.BLAZE_ROD, ChatColor.WHITE + "Room settings");
            }
        }

        if (!room.isRunning()) {
            addButton(MY_SETTINGS_LOCATION, event -> new PersonalSettingsMenu(player), XMaterial.STICK, ChatColor.WHITE + "My settings");

            if (player.hasPermission(Strings.permUnsafe)) {
                addButton(EXTRA_SETTINGS_LOCATION, event -> new ExperimentalSettingsMenu(player), XMaterial.CLOCK, ChatColor.WHITE + "Experimental");
            }
        }

        if (room.isSingleplayer()) {
            addButton(BACK_LOCATION, event -> {
                new HomeMenu(player);
                room.removePlayer(player);
            }, XMaterial.BEDROCK, ChatColor.RED + "Leave");
        } else {
            addButton(BACK_LOCATION, event -> {
                new ListMenu(player);
                room.removePlayer(player);
            }, XMaterial.BEDROCK, ChatColor.RED + "Leave");
        }

        updateContent();
    }

    private void updateContent() {
        if (page > 0) {
            addButton(PREV_PAGE_LOCATION, event -> page--, XMaterial.ARROW, org.bukkit.ChatColor.WHITE + "Previous page");
        } else {
            addButton(PREV_PAGE_LOCATION, empty);
        }

        if (room.players.size() > (page + 1) * PAGE_SIZE) {
            addButton(NEXT_PAGE_LOCATION, event -> page++, XMaterial.ARROW, org.bukkit.ChatColor.WHITE + "Next page");
        } else {
            addButton(NEXT_PAGE_LOCATION, empty);
        }

        for (int i = 0; i < PAGE_SIZE; i++) {
            if (room.players.size() <= page * PAGE_SIZE + i) {
                break;
            }

            Player player = room.players.get(page * PAGE_SIZE + i);

            ItemStack item;
            ItemMeta itemmeta;

            item = XMaterial.PLAYER_HEAD.parseItem();

            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.WHITE + player.getName());
            if (room.getOwner().equals(player)) {
                itemmeta.setLore(Collections.singletonList(ChatColor.DARK_RED + "OWNER"));
            }

            item.setItemMeta(itemmeta);
            addButton(CONTENT_BEGINNING + i, event -> {
            }, item);
        }
    }
}
