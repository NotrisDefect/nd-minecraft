package cabbageroll.notrisdefect.menus;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Menu;
import cabbageroll.notrisdefect.Room;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class JoinRoomMenu extends BaseMenu {

    public final static int BACK_LOCATION = 0;
    public final static int ROOM_LOCATION_MIN = 9;
    public final static int pagesize = 36;

    public final static int MINUSPAGE_LOCATION = 45;
    public final static int PLUSPAGE_LOCATION = 53;

    public JoinRoomMenu(Player player, int p) {
        Main.gs.setLastMenuOpened(player, Menu.JOINROOM);
        createInventory(this, 54, "Join room");
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            getInventory().setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        // clickable items
        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        if (p > 0) {
            getInventory().setItem(MINUSPAGE_LOCATION, createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"));
        }

        int display = 0;
        int counter = 0;
        int i = 0;

        String[] roomlist = Main.gs.generateRoomList();
        while (true) {
            if (i < roomlist.length) {
                Room room = Main.gs.getRoom(roomlist[i]);
                if (!room.getIsSingleplayer()) {
                    if (counter < pagesize) {
                        if (display == p) {
                            getInventory().setItem(ROOM_LOCATION_MIN + counter, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + roomlist[i]));
                        }
                    } else {
                        if (display == p) {
                            break;
                        }
                        counter = -1;
                        display++;
                        i--;
                    }
                    counter++;
                }
            } else {
                break;
            }
            i++;
        }

        if (counter == pagesize) {
            getInventory().setItem(PLUSPAGE_LOCATION, createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"));
        }

        player.openInventory(getInventory());
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        event.setCancelled(true);
        if (JoinRoomMenu.ROOM_LOCATION_MIN <= slot && slot < JoinRoomMenu.ROOM_LOCATION_MIN + JoinRoomMenu.pagesize) {
            Main.gs.getRoom(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())).addPlayer(player);
            new RoomMenu(player);
        } else {
            switch (slot) {
                case JoinRoomMenu.BACK_LOCATION:
                    new MultiplayerMenu(player);
                    break;
                case JoinRoomMenu.MINUSPAGE_LOCATION:
                    new JoinRoomMenu(player, 0);
                    break;
                case JoinRoomMenu.PLUSPAGE_LOCATION:
                    new JoinRoomMenu(player, 0);
                    break;

            }
        }
    }
}
