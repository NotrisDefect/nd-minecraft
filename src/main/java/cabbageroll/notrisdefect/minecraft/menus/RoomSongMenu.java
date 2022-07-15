package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPIYes;
import com.cryptomorin.xseries.XMaterial;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

//used only if noteblockapi is present
public class RoomSongMenu extends Menu {

    private final Room room = Main.gs.getRoom(player);
    private Playlist playlist;

    private int page = 0;

    public RoomSongMenu(Player player) {
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
        playlist = NoteBlockAPIYes.playlist;
        createInventory(this, 54, "Choose song");
        Room room = Main.gs.getRoom(player);
        addBorder();

        addButton(BACK_LOCATION, (event) -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");
        addButton(grid(2, 1), (event) -> room.songIndex = -2, XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "None");
        addButton(grid(3, 1), (event) -> room.songIndex = -1, XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "Random");

        updateContent();
    }

    private void updateContent() {
        if (page > 0) {
            addButton(PREV_PAGE_LOCATION, event -> page--, XMaterial.ARROW, ChatColor.WHITE + "Previous page");
        } else {
            addButton(PREV_PAGE_LOCATION, empty);
        }

        if (playlist.getCount() > (page + 1) * PAGE_SIZE) {
            addButton(NEXT_PAGE_LOCATION, event -> page++, XMaterial.ARROW, ChatColor.WHITE + "Next page");
        } else {
            addButton(NEXT_PAGE_LOCATION, empty);
        }

        for (int i = 0; i < PAGE_SIZE; i++) {
            if (playlist.getCount() <= page * PAGE_SIZE + i) {
                break;
            }

            String name;
            if (playlist.get(page * PAGE_SIZE + i).getPath() == null) {
                name = NoteBlockAPIYes.classpathSongs[i];
            } else {
                name = playlist.get(page * PAGE_SIZE + i).getPath().getName().replaceAll(".nbs$", "");
            }
            int index = page * PAGE_SIZE + i;
            addButton(CONTENT_BEGINNING + i, event -> room.songIndex = index, XMaterial.NOTE_BLOCK, ChatColor.WHITE + name);
        }
    }
}
