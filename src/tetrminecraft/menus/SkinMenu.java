package tetrminecraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Blocks;
import tetrminecraft.Main;

public class SkinMenu implements InventoryHolder {
    private Inventory inventory = null;

    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    public final static int BLOCK_LOCATIONS[] = {28,29,30,31,32,33,34,11,13,37,38,39,40,41,42,43,15};
    
    
    public SkinMenu(Player player){
        
        Main.lastMenuOpened.put(player, "skin");
        Inventory inventory=Bukkit.createInventory(this, 54, "Skin editor");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<54;i++){
            inventory.setItem(i, border);
        }
        
        ItemStack blocks[] = Main.customBlocks.get(player);
        //changeable blocks
        for(int i=0;i<17;i++){
            if(!Main.playerIsUsingCustomBlocks.get(player)) {
                inventory.setItem(BLOCK_LOCATIONS[i], Blocks.defaultBlocks[i]);
            }else if(Main.playerIsUsingCustomBlocks.get(player)) {
                inventory.setItem(BLOCK_LOCATIONS[i], blocks[i]);
            }
        }
        
        inventory.setItem(BACK_LOCATION, BaseMenu.createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, BaseMenu.createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (!Main.playerIsUsingCustomBlocks.get(player)?ChatColor.BOLD:"") + "Default", ChatColor.WHITE + "" + (Main.playerIsUsingCustomBlocks.get(player)?ChatColor.BOLD:"") + "Custom"));
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
