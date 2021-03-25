package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Table;

public class SimpleSettingsMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    
    public SimpleSettingsMenu(Player player) {
        Main.lastMenuOpened.put(player, "simsettings");
        Inventory inventory=Bukkit.createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        

        Table table=Main.inWhichRoomIs.get(player).playerTableMap.get(player);
        
        inventory.setItem(BACK_LOCATION, BaseMenu.createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, BaseMenu.createItem(XMaterial.TORCH, ChatColor.YELLOW + "This is standard settings menu", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Click to go to advanced menu"));
        
        inventory.setItem(21, BaseMenu.createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "X: " + table.getGx()));
        inventory.setItem(22, BaseMenu.createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "Y: " + table.getGy()));
        inventory.setItem(23, BaseMenu.createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "Z: " + table.getGz()));
        
        inventory.setItem(30, BaseMenu.createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X"));
        inventory.setItem(31, BaseMenu.createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y"));
        inventory.setItem(32, BaseMenu.createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z"));
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
