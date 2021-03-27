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
        getInventory().setItem(37, createItem(XMaterial.DIRT, "Width X multiplier: "+table.mwx));
        getInventory().setItem(38, createItem(XMaterial.DIRT, "Width Y multiplier: "+table.mwy));
        getInventory().setItem(39, createItem(XMaterial.DIRT, "Width Z multiplier: "+table.mwz));
        getInventory().setItem(41, createItem(XMaterial.DIRT, "Height X multiplier: "+table.mhx));
        getInventory().setItem(42, createItem(XMaterial.DIRT, "Height Y multiplier: "+table.mhy));
        getInventory().setItem(43, createItem(XMaterial.DIRT, "Height Z multiplier: "+table.mhz));
        getInventory().setItem(53, createItem(XMaterial.DIRT, "BACKFIRE: "+Main.instance.inWhichRoomIs.get(player).backfire));
        getInventory().setItem(1, createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks: "+table.enableFallingSand));
        
        
        player.openInventory(getInventory());
    }

}
