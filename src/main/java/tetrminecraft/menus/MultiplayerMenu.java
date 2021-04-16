package tetrminecraft.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatColor;
import tetrminecraft.Main;
import tetrminecraft.Room;

public class MultiplayerMenu extends BaseMenu {
    
    public final static int BACK_LOCATION = 0;
    public final static int CREATEROOM_LOCATION = 21;
    public final static int LISTROOMS_LOCATION = 23;
    
    public MultiplayerMenu(Player player){
        Main.instance.lastMenuOpened.put(player, "multiplayer");
        createInventory(this, 54, "Multiplayer");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            getInventory().setItem(i, border);
        }
        for(int i=45;i<54;i++){
            getInventory().setItem(i, border);
        }
        
        //clickable items
        
        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(CREATEROOM_LOCATION, createItem(XMaterial.COAL_ORE, ChatColor.WHITE + "Create new room"));
        getInventory().setItem(LISTROOMS_LOCATION, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "Join a room"));
        
        
        player.openInventory(getInventory());
    }
    
    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        event.setCancelled(true);
        switch (slot) {
        case MultiplayerMenu.BACK_LOCATION:
            new HomeMenu(player);
            break;
        case MultiplayerMenu.CREATEROOM_LOCATION:
            new Room(player, false);
            new RoomMenu(player);
            break;
        case MultiplayerMenu.LISTROOMS_LOCATION:
            new JoinRoomMenu(player, 0);
            break;
        }
    }
}
