package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatColor;
import tetrminecraft.Main;

public class HomeMenu implements InventoryHolder {
    private Inventory inventory = null;

    public final static int MULTIPLAYER_LOCATION = 21;
    public final static int SINGLEPLAYER_LOCATION = 22;
    public final static int SKINEDITOR_LOCATION = 23;
    
    public HomeMenu(Player player){
        Main.lastMenuOpened.put(player, "home");
        Inventory inventory=Bukkit.createInventory(this, 54, "Home");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        inventory.setItem(MULTIPLAYER_LOCATION, BaseMenu.createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Multiplayer"));
        inventory.setItem(SINGLEPLAYER_LOCATION, BaseMenu.createItem(XMaterial.PLAYER_HEAD, ChatColor.WHITE + "Singleplayer"));
        inventory.setItem(SKINEDITOR_LOCATION, BaseMenu.createItem(XMaterial.SHEARS, ChatColor.WHITE + "Skin editor"));
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
