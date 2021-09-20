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
public class SongMenu extends Menu {

    public SongMenu(Player player) {
        createInventory(this, 54, "Choose song");
        Room room = Main.gs.getRoom(player);

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), (event) -> new RoomMenu(player)));
        buttons.put(grid(2, 1), new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "Random"), (event) -> room.index = -1));


        Playlist playlist = NoteBlockAPIYes.playlist;
        for (int i = 0; i < playlist.getCount(); i++) {
            String name;
            if (playlist.get(i).getPath() == null) {
                name = NoteBlockAPIYes.classpathSongs[i];
            } else {
                name = playlist.get(i).getPath().getName().replaceAll(".nbs$", "");
            }
            buttons.put(grid(2, 2 + i), new Button(createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + name), event -> room.index = event.getSlot() - 10));
        }

        open(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }
}
