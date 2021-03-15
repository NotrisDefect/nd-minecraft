package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatColor;
import tetrminecraft.Main;

public class MultiplayerMenu implements InventoryHolder{
    private Inventory inventory=null;
    
    public final static int BACK_LOCATION = 0;
    public final static int CREATEROOM_LOCATION = 21;
    public final static int LISTROOMS_LOCATION = 23;
    
    public MultiplayerMenu(Player player){
        Main.lastui.put(player, "multiplayer");
        Inventory inventory=Bukkit.createInventory(this, 54, "Multiplayer");
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
        inventory.setItem(CREATEROOM_LOCATION, BaseMenu.createItem(XMaterial.COAL_ORE, ChatColor.WHITE + "Create new room"));
        inventory.setItem(LISTROOMS_LOCATION, BaseMenu.createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "Join a room"));
        
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
