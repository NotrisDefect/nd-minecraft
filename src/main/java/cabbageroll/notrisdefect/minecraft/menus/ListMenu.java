package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.Strings;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ListMenu extends Menu {

    private final static int ROOM_LOCATION_MIN = 9;
    private final static int pagesize = 36;
    private final static int MINUSPAGE_LOCATION = grid(6, 1);
    private final static int PLUSPAGE_LOCATION = grid(6, 9);

    private List<Room> rooms;

    private int page = 0;

    public ListMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == MINUSPAGE_LOCATION || event.getSlot() == PLUSPAGE_LOCATION) {
            updateButtons();
            placeAll();
        }
    }

    @Override
    protected void prepare() {
        rooms = Main.gs.cloneRoomList();
        createInventory(this, 54, "Join room");
        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), event -> new HomeMenu(player)));

        buttons.put(grid(1, 2), new Button(createItem(XMaterial.COAL_ORE, net.md_5.bungee.api.ChatColor.WHITE + "Create new room"), event -> {
            Main.gs.createMPRoom(player);
            new RoomMenu(player);
        }));

        updateButtons();
    }

    private void updateButtons() {
        if (page > 0) {
            buttons.put(MINUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"), event -> page--));
        } else {
            buttons.put(MINUSPAGE_LOCATION, border);
        }

        if (rooms.size() > (page + 1) * pagesize) {
            buttons.put(PLUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"), event -> page++));
        } else {
            buttons.put(PLUSPAGE_LOCATION, border);
        }

        int i;

        for (i = 0; i < pagesize; i++) {
            if (rooms.size() <= page * pagesize + i) {
                break;
            }
            Room room = rooms.get(page * pagesize + i);
            buttons.put(ROOM_LOCATION_MIN + i, new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + room.getRoomName()), event -> {
                if (!Main.gs.roomExists(room)) {
                    player.sendMessage(Strings.doesntExist);
                    return;
                }
                room.addPlayer(player);
                new RoomMenu(player);
            }));
        }
        for (int j = i; j < pagesize; j++) {
            buttons.remove(ROOM_LOCATION_MIN + j);
        }
    }
}
