package tetrminecraft.menus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Room;

public class JoinRoomMenu extends BaseMenu {

    public final static int BACK_LOCATION = 0;
    public final static int ROOM_LOCATION_MIN = 9;
    public final static int pagesize = 36;

    public final static int MINUSPAGE_LOCATION = 45;
    public final static int PLUSPAGE_LOCATION = 53;

    public JoinRoomMenu(Player player, int p) {
        Main.instance.lastMenuOpened.put(player, "joinroom");
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

        Main.instance.joinRoomPage.put(player, p);
        int page = p;
        int display = 0;
        int counter = 0;
        int i = 0;

        Object[] roomlist = Main.instance.roomByID.values().toArray();
        while (true) {
            if (i < roomlist.length) {
                Room room = (Room) roomlist[i];
                if (!room.isSingleplayer) {
                    if (counter < pagesize) {
                        if (display == page) {
                            getInventory().setItem(ROOM_LOCATION_MIN + counter,
                                    createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + room.roomID));
                        }
                    } else {
                        if (display == page) {
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
            Main.instance.roomByID.get(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))
                    .addPlayer(player);
            new RoomMenu(player);
        } else {
            switch (slot) {
            case JoinRoomMenu.BACK_LOCATION:
                new MultiplayerMenu(player);
                break;
            case JoinRoomMenu.MINUSPAGE_LOCATION:
                new JoinRoomMenu(player, Main.instance.joinRoomPage.get(player) - 1);
                break;
            case JoinRoomMenu.PLUSPAGE_LOCATION:
                new JoinRoomMenu(player, Main.instance.joinRoomPage.get(player) + 1);
                break;

            }
        }
    }
}
