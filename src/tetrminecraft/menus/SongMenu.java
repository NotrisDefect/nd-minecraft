package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;

import tetrminecraft.Main;
import tetrminecraft.functions.dependencyutil.NoteBlockAPIYes;


//used only if noteblockapi is present
public class SongMenu implements InventoryHolder {
private Inventory inventory = null;
    
    public final static int BACK_LOCATION = 0;
    
    public SongMenu(Player player) {
        Main.lastMenuOpened.put(player, "song");
        Inventory inventory=Bukkit.createInventory(this, 54, "Choose song");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        
        inventory.setItem(BACK_LOCATION, BaseMenu.createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(9, BaseMenu.createItem(XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "Random"));
        

        Playlist playlist = NoteBlockAPIYes.playlist;
        for(int i=0;i<playlist.getCount();i++) {
            inventory.setItem(10+i, BaseMenu.createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + playlist.get(i).getPath().getName().replaceAll(".nbs$", "")));
        }
        
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
