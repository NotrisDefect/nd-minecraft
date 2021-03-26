package tetrminecraft.menus;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Table;

public class SettingsMenu extends BaseMenu {
    
    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    
    public SettingsMenu(Player player){
        Main.instance.lastMenuOpened.put(player, "settings");
        createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            getInventory().setItem(i, border);
        }
        for(int i=45;i<54;i++){
            getInventory().setItem(i, border);
        }
        

        Table table=Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);
        
        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION, createItem(XMaterial.REDSTONE_TORCH, ChatColor.DARK_RED + "This is advanced settings menu", ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to go to standard menu"));
        
        getInventory().setItem(11, createItem(XMaterial.DIRT, "your pos"));
        getInventory().setItem(12, createItem(XMaterial.DIRT, "GX: "+table.getGx()));
        getInventory().setItem(13, createItem(XMaterial.DIRT, "GY: "+table.getGy()));
        getInventory().setItem(14, createItem(XMaterial.DIRT, "GZ: "+table.getGz()));
        getInventory().setItem(37, createItem(XMaterial.DIRT, "M1X: "+table.m1x));
        getInventory().setItem(38, createItem(XMaterial.DIRT, "M2X: "+table.m2x));
        getInventory().setItem(39, createItem(XMaterial.DIRT, "M3X: "+table.m3x));
        getInventory().setItem(41, createItem(XMaterial.DIRT, "M1Y: "+table.m1y));
        getInventory().setItem(42, createItem(XMaterial.DIRT, "M2Y: "+table.m2y));
        getInventory().setItem(43, createItem(XMaterial.DIRT, "M3Y: "+table.m3y));
        getInventory().setItem(53, createItem(XMaterial.DIRT, "BACKFIRE: "+Main.instance.inWhichRoomIs.get(player).backfire));
        getInventory().setItem(1, createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks: "+table.enableFallingSand));
        
        
        player.openInventory(getInventory());
    }

}
