package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Room;

public class JoinRoomMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    public final static int BACK_LOCATION = 0;
    public final static int ROOM_LOCATION_MIN = 9;
    public final static int pagesize=36;

    public final static int MINUSPAGE_LOCATION = 45;
    public final static int PLUSPAGE_LOCATION = 53;
    
    
    public JoinRoomMenu(Player player, int p){
        Main.instance.lastMenuOpened.put(player, "joinroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "Join room");
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
        if(p>0) {
            inventory.setItem(MINUSPAGE_LOCATION, BaseMenu.createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"));
        }
        
        Main.instance.joinRoomPage.put(player, p);
        int page = p;
        int display = 0;
        int counter = 0;
        int i = 0;
        
        Object[] roomlist = Main.instance.roomByID.values().toArray();
        while(true) {
            if(i<roomlist.length) {
            Room room = (Room) roomlist[i];
                if(!room.isSingleplayer) {
                    if(counter<pagesize) {
                        if(display==page) {
                            inventory.setItem(ROOM_LOCATION_MIN+counter, BaseMenu.createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + room.roomID));
                        }
                    }else {
                        if(display==page) {
                            break;
                        }
                        counter = -1;
                        display++;
                        i--;
                    }
                    counter++;
                }
            }else {
                break;
            }
            i++;
        }
        
        if(counter==pagesize) {
            inventory.setItem(PLUSPAGE_LOCATION, BaseMenu.createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"));
        }
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
