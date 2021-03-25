package tetrminecraft.menus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Table;

public class SimpleSettingsMenu extends BaseMenu {
    
    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    
    public SimpleSettingsMenu(Player player) {
        Main.instance.lastMenuOpened.put(player, "simsettings");
        createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            getInventory().setItem(i, border);
        }
        for(int i=45;i<54;i++){
            getInventory().setItem(i, border);
        }
        
        //clickable items
        

        Table table=Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
        
        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.YELLOW + "This is standard settings menu", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Click to go to advanced menu"));
        
        getInventory().setItem(21, createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "X: " + table.getGx()));
        getInventory().setItem(22, createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "Y: " + table.getGy()));
        getInventory().setItem(23, createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "Z: " + table.getGz()));
        
        getInventory().setItem(30, createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X"));
        getInventory().setItem(31, createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y"));
        getInventory().setItem(32, createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z"));
        
        player.openInventory(getInventory());
    }
}
