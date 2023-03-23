package in.cabbageroll.notrisdefect.minecraft.menus;

import in.cabbageroll.notrisdefect.minecraft.Main;
import in.cabbageroll.notrisdefect.minecraft.Room;
import in.cabbageroll.notrisdefect.minecraft.Strings;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ListMenu extends Menu {

    private List<Room> rooms;

    private int page = 0;

    public ListMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == PREV_PAGE_LOCATION || event.getSlot() == NEXT_PAGE_LOCATION) {
            clearContent();
            updateContent();
            placeAll();
        }
    }

    @Override
    protected void prepare() {
        rooms = Main.GS.getMultiplayerRoomList();
        createInventory(this, 54, "Join room");
        addBorder();

        addButton(BACK_LOCATION, event -> new HomeMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(2, 1), event -> {
            Main.GS.createMPRoom(player);
        }, XMaterial.COAL_ORE, ChatColor.WHITE + "Create new room");

        updateContent();
    }

    private void updateContent() {
        if (page > 0) {
            addButton(PREV_PAGE_LOCATION, event -> page--, XMaterial.ARROW, ChatColor.WHITE + "Previous page");
        } else {
            addButton(PREV_PAGE_LOCATION, empty);
        }

        if (rooms.size() > (page + 1) * PAGE_SIZE) {
            addButton(NEXT_PAGE_LOCATION, event -> page++, XMaterial.ARROW, ChatColor.WHITE + "Next page");
        } else {
            addButton(NEXT_PAGE_LOCATION, empty);
        }

        for (int i = 0; i < PAGE_SIZE; i++) {
            if (rooms.size() <= page * PAGE_SIZE + i) {
                break;
            }

            Room room = rooms.get(page * PAGE_SIZE + i);
            addButton(CONTENT_BEGINNING + i, event -> {
                if (!Main.GS.roomExists(room)) {
                    player.sendMessage(Strings.doesntExist);
                    return;
                }
                room.addPlayer(player);
            }, XMaterial.COAL_BLOCK, ChatColor.WHITE + room.getRoomName());
        }
    }
}
