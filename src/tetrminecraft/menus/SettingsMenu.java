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

public class SettingsMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    
    public SettingsMenu(Player player){
        Main.lastui.put(player, "settings");
        Inventory inventory=Bukkit.createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        

        Table table=Main.inwhichroom.get(player).playerTableMap.get(player);
        
        inventory.setItem(BACK_LOCATION, BaseMenu.createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, BaseMenu.createItem(XMaterial.REDSTONE_TORCH, ChatColor.DARK_RED + "This is advanced settings menu", ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to go to standard menu"));
        
        inventory.setItem(11, BaseMenu.createItem(XMaterial.DIRT, "your pos"));
        inventory.setItem(12, BaseMenu.createItem(XMaterial.DIRT, "GX: "+table.getGx()));
        inventory.setItem(13, BaseMenu.createItem(XMaterial.DIRT, "GY: "+table.getGy()));
        inventory.setItem(14, BaseMenu.createItem(XMaterial.DIRT, "GZ: "+table.getGz()));
        inventory.setItem(37, BaseMenu.createItem(XMaterial.DIRT, "M1X: "+table.m1x));
        inventory.setItem(38, BaseMenu.createItem(XMaterial.DIRT, "M2X: "+table.m2x));
        inventory.setItem(39, BaseMenu.createItem(XMaterial.DIRT, "M3X: "+table.m3x));
        inventory.setItem(41, BaseMenu.createItem(XMaterial.DIRT, "M1Y: "+table.m1y));
        inventory.setItem(42, BaseMenu.createItem(XMaterial.DIRT, "M2Y: "+table.m2y));
        inventory.setItem(43, BaseMenu.createItem(XMaterial.DIRT, "M3Y: "+table.m3y));
        inventory.setItem(53, BaseMenu.createItem(XMaterial.DIRT, "BACKFIRE: "+Main.inwhichroom.get(player).backfire));
        inventory.setItem(1, BaseMenu.createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks: "+table.ULTRAGRAPHICS));
        
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory(){
        return inventory;
    }

}
