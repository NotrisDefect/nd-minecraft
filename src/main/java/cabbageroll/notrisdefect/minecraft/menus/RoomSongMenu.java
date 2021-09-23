package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.functions.softdepend.NoteBlockAPIYes;
import com.cryptomorin.xseries.XMaterial;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

//used only if noteblockapi is present
public class RoomSongMenu extends Menu {

    private final static int SONG_LOCATION_MIN = 9;
    private final static int pagesize = 36;
    private final static int MINUSPAGE_LOCATION = 45;
    private final static int PLUSPAGE_LOCATION = 53;
    private final Room room = Main.gs.getRoom(player);
    private final Playlist playlist = NoteBlockAPIYes.playlist;
    private int page = 0;

    public RoomSongMenu(Player player) {
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
        createInventory(this, 54, "Choose song");
        Room room = Main.gs.getRoom(player);

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), (event) -> new RoomMenu(player)));
        buttons.put(grid(6, 2), new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "None"), (event) -> room.index = -2));
        buttons.put(grid(6, 3), new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "Random"), (event) -> room.index = -1));

        updateButtons();
    }

    private void updateButtons() {
        if (page > 0) {
            buttons.put(MINUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"), event -> page--));
        }
        if (playlist.getCount() > (page + 1) * pagesize) {
            buttons.put(PLUSPAGE_LOCATION, new Button(createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"), event -> page++));
        }

        int i;

        for (i = 0; i < pagesize; i++) {
            if (playlist.getCount() > page * pagesize) {
                break;
            }
            String name;
            if (playlist.get(i).getPath() == null) {
                name = NoteBlockAPIYes.classpathSongs[i];
            } else {
                name = playlist.get(i).getPath().getName().replaceAll(".nbs$", "");
            }
            int index = pagesize * page + i;
            buttons.put(SONG_LOCATION_MIN + i, new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + name), event -> room.index = index));
        }
        for (int j = i; j < pagesize; j++) {
            buttons.remove(SONG_LOCATION_MIN + i);
        }

    }
}
