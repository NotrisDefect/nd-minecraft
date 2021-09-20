package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class JoinRoomMenu extends Menu {

    public final static int ROOM_LOCATION_MIN = 9;
    public final static int pagesize = 36;

    public final static int MINUSPAGE_LOCATION = 45;
    public final static int PLUSPAGE_LOCATION = 53;

    public JoinRoomMenu(Player player, int p) {
        createInventory(this, 54, "Join room");
        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), event -> new MultiplayerMenu(player)));
        if (p > 0) {
            buttons.put(MINUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"), event -> new JoinRoomMenu(player, 0)));
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
                            buttons.put(ROOM_LOCATION_MIN + counter, new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + roomlist[i]), event -> {
                                Main.gs.getRoom(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())).addPlayer(player);
                                new RoomMenu(player);
                            }));
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
            buttons.put(PLUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"), event -> new JoinRoomMenu(player, 0)));
        }

        open(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }
}
