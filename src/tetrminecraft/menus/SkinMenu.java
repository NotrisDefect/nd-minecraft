package tetrminecraft.menus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Blocks;
import tetrminecraft.Main;

public class SkinMenu extends BaseMenu {
    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;
    public final static int BLOCK_LOCATIONS[] = {28,29,30,31,32,33,34,11,13,37,38,39,40,41,42,43,15};
    
    
    public SkinMenu(Player player){
        
        Main.instance.lastMenuOpened.put(player, "skin");
        createInventory(this, 54, "Skin editor");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<54;i++){
            getInventory().setItem(i, border);
        }
        
        ItemStack blocks[] = Main.instance.customBlocks.get(player);
        //changeable blocks
        for(int i=0;i<17;i++){
            if(!Main.instance.playerIsUsingCustomBlocks.get(player)) {
                getInventory().setItem(BLOCK_LOCATIONS[i], Blocks.defaultBlocks[i]);
            }else if(Main.instance.playerIsUsingCustomBlocks.get(player)) {
                getInventory().setItem(BLOCK_LOCATIONS[i], blocks[i]);
            }
        }
        
        getInventory().setItem(BACK_LOCATION, BaseMenu.createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION, BaseMenu.createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (!Main.instance.playerIsUsingCustomBlocks.get(player)?ChatColor.BOLD:"") + "Default", ChatColor.WHITE + "" + (Main.instance.playerIsUsingCustomBlocks.get(player)?ChatColor.BOLD:"") + "Custom"));
        
        player.openInventory(getInventory());
    }
}
